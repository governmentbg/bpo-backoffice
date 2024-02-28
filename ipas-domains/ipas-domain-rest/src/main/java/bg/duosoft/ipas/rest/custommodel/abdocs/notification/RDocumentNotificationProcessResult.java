package bg.duosoft.ipas.rest.custommodel.abdocs.notification;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RDocumentNotificationProcessResult {
    private boolean isSuccessful;
    private List<String> messages;

    public List<String> getMessages() {
        if (Objects.isNull(messages)) {
            this.messages = new ArrayList<>();
        }
        return messages;
    }

}
