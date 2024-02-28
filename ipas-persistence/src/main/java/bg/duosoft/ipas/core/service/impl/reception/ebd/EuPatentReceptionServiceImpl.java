package bg.duosoft.ipas.core.service.impl.reception.ebd;

import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.bpo.ebd.ebddpersistence.service.EbdDPatentService;
import bg.duosoft.abdocs.exception.AbdocsServiceException;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.reception.*;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.impl.reception.BaseReceptionServiceImpl;
import bg.duosoft.ipas.integration.ebddownload.mapper.EbdPersonBaseMapper;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.integration.ebddownload.service.impl.ExternalEbdPatentIpasServiceImpl;
import bg.duosoft.ipas.services.core.IpasFileNumber;
import bg.duosoft.ipas.services.core.IpasPatentService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * pri insert na patent (a ne samo na userdoc), tozi service pravi call kym {@link LocalEbdPatentIpasServiceImpl} i sled tova kym {@link ExternalEbdPatentIpasServiceImpl}, koito dovkarva danni prez IpasAPI-to!
 * {@link LocalEbdPatentIpasServiceImpl} si otvarq samostoqtelna tranzakciq i insertva patent + userdoc. Ako neshto grymne tam, tranzakciqta shte se rollback-ne.
 * ako neshto grymne pri call-a na {@link ExternalEbdPatentIpasServiceImpl}, trqbva da se iztrie patenta + vsichki userdocs. Trieneto stava prez Ipas API-to!!!! Patenta se trie i ako ne moje da se update-ne statusa na patenta v ebd bazata!!!
 */
@Slf4j
@Service
@Transactional
@LogExecutionTime
public class EuPatentReceptionServiceImpl extends BaseReceptionServiceImpl {

    @Autowired
    private EbdDPatentService ebdDPatentService;

    @Autowired
    private LocalEbdPatentIpasServiceImpl localEbdPatentIpasService;

    @Autowired
    private ExternalEbdPatentIpasServiceImpl externalEbdPatentIpasService;

    @Autowired
    private IpasPatentService ipasPatentService;

    @Autowired
    private FileService fileService;
    @Autowired
    private EbdPatentService ebdPatentService;
    @Autowired
    private EbdPersonBaseMapper ebdPersonBaseMapper;

    public ReceiveEuropeanPatentResult insertEuPatentReception(CReception receptionForm) throws EbdReceptionException {
        CReceptionEuPatent receptionEuPatent = receptionForm.getEuPatent();
        if (Objects.isNull(receptionEuPatent))
            throw new RuntimeException("Reception EU patent is empty !");

        Integer objectNumber = receptionEuPatent.getObjectNumber();
        EbdDPatent cEuPatent = ebdDPatentService.getEbdDPatentById(objectNumber.toString());
        if (Objects.isNull(cEuPatent))
            throw new RuntimeException("Cannot find ebd patent !" + objectNumber);
        transferOwnersIfNecessary(receptionForm, cEuPatent);
        return processEuPatentReception(receptionForm, cEuPatent);
    }
    //ako v zaqvkata nqma vyvedeni owners, se slagat ownerite na patenta kato takiva
    private void transferOwnersIfNecessary(CReception receptionForm, EbdDPatent cEuPatent) {
        if (receptionForm.getOwnershipData() == null) {
            receptionForm.setOwnershipData(new COwnershipData());
        }

        if (CollectionUtils.isEmpty(receptionForm.getOwnershipData().getOwnerList())) {
            receptionForm.getOwnershipData().setOwnerList(new ArrayList<>());
            cEuPatent.getOwners().stream().map(r -> OwnerUtils.convertToOwner(receptionForm.getOwnershipData(), ebdPersonBaseMapper.toCore(r))).forEach(r -> receptionForm.getOwnershipData().getOwnerList().add(r));
        }
    }

    private ReceiveEuropeanPatentResult processEuPatentReception(CReception receptionForm, EbdDPatent cEuPatent) throws EbdReceptionException {
        CFileId euPatentFileId = EuPatentUtils.generateEUPatentCFileId(cEuPatent);
        boolean isExist = fileService.isFileExist(euPatentFileId);
        if (isExist) {
            CReceptionResponse cUserdocDetails =  localEbdPatentIpasService.insertUserdoc(euPatentFileId, receptionForm);
            return new ReceiveEuropeanPatentResult(null, cUserdocDetails);
        } else {
            return _insertEuPatent(receptionForm, cEuPatent);
        }
    }

    private void updateEuPatentFileNumber(EbdDPatent cEuPatent, CFileId euPatentFileId) {
        if (Objects.isNull(cEuPatent.getFileNbr())) {
            ebdPatentService.updateEbdPatentFileNumber(cEuPatent.getIdappli(), euPatentFileId.getFileNbr().longValue());
            log.info("File number is successfully updated for " + euPatentFileId.toString());
        }
    }
    private ReceiveEuropeanPatentResult _insertEuPatent(CReception receptionForm, EbdDPatent ebdDPatent) throws EbdReceptionException {
        //za da ne cheta 2 pyti EbdDPatent - vednyj v localEbdPatentIpasService i vednyj v externalEbdPatentIpasService, go prochitam tuk i go predavam i na davata service-a!!!
        ReceiveEuropeanPatentResult cEuPatentReceptionResult = localEbdPatentIpasService.transferEuropeanPatentFromEbdToIpas(ebdDPatent, receptionForm);
        CFileId fileId = cEuPatentReceptionResult.getInsertPatentReceptionResponse().getFileId();
        try {
            externalEbdPatentIpasService.processExternalIpasReception(ebdDPatent, fileId, receptionForm);
        } catch (IpasServiceException e) {
            deletePatentAndThrowException(cEuPatentReceptionResult, e);
        }

        try {
            updateEuPatentFileNumber(ebdDPatent, cEuPatentReceptionResult.getInsertPatentReceptionResponse().getFileId());
        } catch (Exception e) {
            deletePatentAndThrowException(cEuPatentReceptionResult, e);
        }
        return cEuPatentReceptionResult;
    }
    private void deletePatentAndThrowException(ReceiveEuropeanPatentResult cEuPatentReceptionResult, Exception e) throws EbdReceptionException {
        EbdReceptionException ex = new EbdReceptionException(cEuPatentReceptionResult.getInsertPatentReceptionResponse().getDocflowDocumentId(), e);
        try {
            CFileId fileId = cEuPatentReceptionResult.getInsertPatentReceptionResponse().getFileId();
            ipasPatentService.deletePatent(new IpasFileNumber(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr()));
        } catch (IpasServiceException ipasServiceException) {
            ex.addSuppressed(ipasServiceException);//TODO:Kakvo da se pravi ako ne moje da se iztrie patenta???? Da se lishe v log tablicata na Denis??????
        }
        throw ex;
    }



}
