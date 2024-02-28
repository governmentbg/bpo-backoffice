package bg.duosoft.ipas.persistence.repository.entity.custom;

public interface SequenceRepository {
    public static enum SEQUENCE_NAME {
        SEQUENCE_NAME_PERSON_NBR("IPASPROD.SYS_SEC_PERSONA"),
        SEQUENCE_NAME_PROC_NBR("IPASPROD.SYS_SEC_TRAMITE");
        private String name;
        SEQUENCE_NAME(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    Integer getNextSequenceValue(SEQUENCE_NAME sequenceName);

    Integer getNextDocNumber(String docOri);

    Integer getDocSeries(String docOri);

    Integer getDocSeqSeries(String docOri, String docSeqTyp);

    public boolean isGenerateNextDocSequenceFromExternalSystemId(String docSeqTyp);

    public Integer getNextDocSequenceNumber(String docSeqType, Integer docSeqSeries);
}
