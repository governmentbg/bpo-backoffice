package com.duosoft.ipas.webmodel.structure;

import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import lombok.Data;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 4.7.2019 г.
 * Time: 12:13
 */
@Data
public class TransferUserRequestData {
    private OfficeStructureId oldStructureId;
    private OfficeStructureId newStructureId;
    private List<Integer> userIds;
}
