package com.duosoft.ipas.controller.ipobjects.userdoc.recordal;

import com.duosoft.ipas.controller.ipobjects.userdoc.ExtraDataBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/userdoc/entry_changes")
public class EntryChangesController extends ExtraDataBaseController {
    @Override
    public String getPanelPage() {
        return "ipobjects/userdoc/recordal/entry_changes/entry_changes_panel :: entry-changes";
    }
}
