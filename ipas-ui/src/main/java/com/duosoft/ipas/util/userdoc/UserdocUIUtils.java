package com.duosoft.ipas.util.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import com.duosoft.ipas.enums.UserdocPanel;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class UserdocUIUtils {

    public static List<String> selectUserdocPanelCodes(List<CUserdocPanel> panels) {
        List<String> codes = new ArrayList<>();

        if (!CollectionUtils.isEmpty(panels)) {
            for (CUserdocPanel panelObject : panels) {
                String panel = panelObject.getPanel();
                UserdocPanel userdocPanel = UserdocPanel.valueOf(panel);
                codes.add(userdocPanel.code());
            }
        }

        return codes;
    }

}
