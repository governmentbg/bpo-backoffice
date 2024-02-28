package bg.duosoft.ipas.persistence.model.entity;

public interface FileSeqTypSerNbrPK {
    Integer getFileNbr();

    String getFileSeq();

    String getFileTyp();

    Integer getFileSer();

    void setFileNbr(Integer fileNbr);

    void setFileSeq(String fileSeq);

    void setFileTyp(String fileTyp);

    void setFileSer(Integer fileSer);
}
