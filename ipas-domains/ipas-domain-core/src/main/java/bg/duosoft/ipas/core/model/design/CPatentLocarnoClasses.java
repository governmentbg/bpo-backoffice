package bg.duosoft.ipas.core.model.design;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPatentLocarnoClasses implements Serializable {
      private static final long serialVersionUID = 1854751220390754096L;
      private String locarnoClassCode;
      private String locWPublishValidated;
      private String locarnoEditionCode;
      private CLocarnoClasses locarnoClasses;
}
