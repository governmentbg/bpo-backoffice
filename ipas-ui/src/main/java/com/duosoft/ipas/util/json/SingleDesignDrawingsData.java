package com.duosoft.ipas.util.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SingleDesignDrawingsData {
  private Long drawingNbr;
  private Integer viewType;
  private Boolean imagePublished;
  private Boolean imageRefused;
}
