package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.action.CActionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ActionTypeData {
    private String actionType;
    private String actionName;

    public ActionTypeData(CActionType cActionType) {
        this.actionType = cActionType.getActionType();
        this.actionName = cActionType.getActionName();
    }
}
