package bg.duosoft.ipas.core.validation.config;

import lombok.*;


@ToString
@EqualsAndHashCode
@Getter
public class ValidationError {
    @Setter
    private String pointer;
    @Setter
    private String pointerMessageCode;
    private String messageCode;
    private String message;
    private Object invalidValue;
    @Getter(AccessLevel.NONE)
    private String caller;

    @Builder
    public ValidationError(String pointer, String messageCode, String message, Object invalidValue) {
        this.pointer = pointer;
        this.messageCode = messageCode;
        this.message = message;
        this.invalidValue = invalidValue;
        int callerIndex = Thread.currentThread().getStackTrace()[2].getClassName().equals(ValidationErrorBuilder.class.getName()) ? 3 : 2;//if the ValidationError is created through constructor, the callerIndex should be 2, otherwise (through Builder) -> 3
        StackTraceElement el = Thread.currentThread().getStackTrace()[callerIndex];
        this.caller = "class:" + el.getClassName() + ", method:" + el.getMethodName() + ", line:" + el.getLineNumber();
    }
}