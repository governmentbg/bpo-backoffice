package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.util.date.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by Raya
 * 14.03.2019
 */
@Getter
@Setter
@NoArgsConstructor
public class ExhibitionData {

    private boolean hasExhibitionData;
    private String exhibitionNotes;
    @JsonFormat(pattern = DateUtils.DATE_FORMAT_DOT)
    private Date exhibitionDate;
}
