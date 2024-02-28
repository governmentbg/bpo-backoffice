package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class CPriorityData
        implements Serializable {
    private static final long serialVersionUID = 5559084408294238650L;
    private Date earliestAcceptedParisPriorityDate;
    private Date exhibitionDate;
    private String exhibitionNotes;
    private boolean hasExhibitionData;
    private boolean hasParisPriorityData;//za da moje v UI-a da gyrmi validation error kato se cykne otmetkata Konvencionalen prioritet no ne se prikachi nikakyv, trqbva da ima i flag hasParisPriorityData i da se setva parisPriorityList, t.e. ako e vdignat flaga no e prazen parisPriorityList, gyrmi validation error. Za celta kato se slagat parisPriorities trqbva da se vdiga i flaga!!!!
    private List<CParisPriority> parisPriorityList;
}


