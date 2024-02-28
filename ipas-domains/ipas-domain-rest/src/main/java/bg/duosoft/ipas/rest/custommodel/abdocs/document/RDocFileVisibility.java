package bg.duosoft.ipas.rest.custommodel.abdocs.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;


@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum RDocFileVisibility {

    PrivateAttachedFile(1, "privateAttachedFile"),
    PublicAttachedFile(2, "publicAttachedFile");

    private int value;
    private String alias;

    private RDocFileVisibility(int value, String alias) {
        this.value = value;
        this.alias = alias;
    }

    @JsonValue
    public int value() {
        return this.value;
    }

    public String alias() {
        return this.alias;
    }

    @JsonCreator
   public static RDocFileVisibility fromAlias(String alias) {
        return (RDocFileVisibility) Stream.of(values()).filter((state) -> {
            return state.alias.equals(alias);
        }).findFirst().get();
    }
}
