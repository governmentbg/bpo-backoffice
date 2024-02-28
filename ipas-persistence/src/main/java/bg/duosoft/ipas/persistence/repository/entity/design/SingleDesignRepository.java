package bg.duosoft.ipas.persistence.repository.entity.design;

import bg.duosoft.ipas.persistence.model.entity.design.SingleDesign;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SingleDesignRepository extends BaseRepository<SingleDesign, IpFilePK> {

    @Modifying
    @Query(value = "update IP_PATENT set SERVICE_PERSON_NBR = ?,SERVICE_ADDR_NBR = ?, " +
            " MAIN_OWNER_PERSON_NBR = ?,MAIN_OWNER_ADDR_NBR = ? where FILE_SEQ = ? and FILE_TYP = ? and  FILE_SER = ? and FILE_NBR = ?",nativeQuery = true)
    void updateSingleDesignServiceAndMainPerson(Integer servicePersonNbr,Integer serviceAddrNbr,Integer mainOnwerPersonNbr,
                                                Integer mainOwnerAddrNbr,String fileSeq, String fileType, Integer singleDesignFileSer,Integer singleDesignFileNbr);
}
