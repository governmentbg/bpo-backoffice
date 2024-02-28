package bg.duosoft.ipas.rest.custommodel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestApiResponse<T> {
    private T data;
}
