package bg.duosoft.ipas.persistence.repository.entity.ext;


import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.IndexQueue;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IndexQueueRepository extends BaseRepository<IndexQueue, Integer> {
    List<IndexQueue> findAllByCheckedFalseAndIndexedAtNullOrderByInsertedAtAscIdAsc();

    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, String operationCode);
    List<IndexQueue> findAllByProcTypAndProcNbrAndActionNbrAndOperationAndCheckedFalse(String procTyp, Integer procNbr, Integer actionNbr, String operationCode);
    List<IndexQueue> findAllByPersonNbrAndAddrNbrAndOperationAndCheckedFalse(Integer personNbr,Integer addrNbr, String operationCode);
    List<IndexQueue> findAllByProcTypAndProcNbrAndOperationAndCheckedFalse(String procTyp, Integer procNbr, String operationCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndViennaClassCodeAndViennaGroupCodeAndViennaElemCodeAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, Long viennaClassCode, Long viennaGroupCode, Long viennaElemCode, String operationCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndViennaClassCodeAndViennaGroupCodeAndViennaElemCodeAndAttachmentIdAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, Long viennaClassCode, Long viennaGroupCode, Long viennaElemCode, Integer attachmentId, String operationCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndNiceClassCodeAndNiceClassStatusWcodeAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, Long niceClassCode, String niceClassStatusWcode, String operationCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndIpcEditionCodeAndIpcSectionCodeAndIpcClassCodeAndIpcSubclassCodeAndIpcGroupCodeAndIpcSubgroupCodeAndIpcQualificationCodeAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,String ipcEditionCode, String ipcSectionCode, String ipcClassCode, String ipcSubclassCode, String ipcGroupCode, String ipcSubgroupCode, String ipcQualificationCode, String operationCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndCpcEditionCodeAndCpcSectionCodeAndCpcClassCodeAndCpcSubclassCodeAndCpcGroupCodeAndCpcSubgroupCodeAndCpcQualificationCodeAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,String cpcEditionCode, String cpcSectionCode, String cpcClassCode, String cpcSubclassCode, String cpcGroupCode, String cpcSubgroupCode, String cpcQualificationCode, String operationCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndLanguageCodeAndOperationAndCheckedFalse(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, String languageCode, String operationCode);
    List<IndexQueue> findAllByDocOriAndDocLogAndDocSerAndDocNbrAndOperationAndCheckedFalse(String docOri, String docLog, Integer docSer, Integer docNbr, String operationCode);
    List<IndexQueue> findAllByDocOriAndDocLogAndDocSerAndDocNbrAndPersonNbrAndAddrNbrAndRoleAndOperationAndCheckedFalse(String docOri, String docLog, Integer docSer, Integer docNbr, Integer personNbr, Integer addrNbr, UserdocPersonRole role, String operationCode);

    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndLocarnoClassCodeAndOperationAndCheckedFalse(
            String fileSeq,
            String fileType,
            Integer fileSer,
            Integer fileNbr,
            String languageCode,
            String locarnoClassCode);
    List<IndexQueue> findAllByFileSeqAndFileTypAndFileSerAndFileNbrAndFileSeq2AndFileTyp2AndFileSer2AndFileNbr2AndRelationshipTypAndOperationAndCheckedFalse(
            String fileSeq,
            String fileType,
            Integer fileSer,
            Integer fileNbr,
            String fileSeq2,
            String fileType2,
            Integer fileSer2,
            Integer fileNbr2,
            String relationshipTyp,
            String operationCode);
    @Modifying
    @Query("DELETE FROM IndexQueue WHERE id <= :id")
    void deleteAll(@Param("id") int id);

    @Query("select max(id) from IndexQueue ")
    public Optional<Integer> getMaxId();

}
