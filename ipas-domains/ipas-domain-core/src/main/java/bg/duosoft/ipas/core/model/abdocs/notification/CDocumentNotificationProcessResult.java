package bg.duosoft.ipas.core.model.abdocs.notification;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CDocumentNotificationProcessResult implements Serializable {
    private boolean isSuccessful;
    private List<String> messages;

    public List<String> getMessages() {
        if (Objects.isNull(messages)) {
            this.messages = new ArrayList<>();
        }
        return messages;
    }

}
