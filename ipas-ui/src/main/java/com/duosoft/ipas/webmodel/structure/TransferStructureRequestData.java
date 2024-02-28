package com.duosoft.ipas.webmodel.structure;

import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import lombok.Data;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 18:28
 */
@Data
public class TransferStructureRequestData {
    private OfficeStructureId newStructureId;
    private OfficeStructureId oldStructureId;
    private List<OfficeStructureId> structureIds;
}
