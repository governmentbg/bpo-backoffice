package bg.duosoft.ipas.enums;

public enum ActionTypeKind {
    BOTH(0),
    AUTOMATIC(1),
    MANUAL(2);

    ActionTypeKind(int code) {
        this.code = code;
    }

    private int code;

    /**
     * Method that returns the code
     *
     * @return the code
     */
    public int code() {
        return code;
    }
}
