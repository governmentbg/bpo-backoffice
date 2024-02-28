package bg.duosoft.ipas.rest.custommodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestApiRequest<T> {
    private String username;
    private T data;
    public RestApiRequest(T data) {
        this.data = data;
    }
}
