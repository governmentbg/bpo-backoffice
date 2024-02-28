package com.duosoft.ipas.controller.ipobjects.common.process.tree;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.journal.CJournalElement;
import bg.duosoft.ipas.core.service.journal.JournalElementService;
import bg.duosoft.ipas.util.attachment.AttachmentUtils;
import bg.duosoft.ipas.util.journal.JournalUtils;
import bg.duosoft.ipas.util.process.ProcessEventUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/process/tree/journal")
public class ProcessTreeJournalController {

    private final JournalElementService journalElementService;

    @Value("${ipas.properties.journalURL}")
    private String journalUrl;

    @PostMapping(value = "/open-journal")
    @ResponseBody
    public String selectJournalUrl(@RequestParam String processEventId) {
        CActionId actionId = ProcessEventUtils.getActionIdFromString(processEventId);
        CJournalElement journalElement = journalElementService.selectByAction(actionId, false);
        return JournalUtils.selectJournalElementUrl(journalUrl, journalElement);
    }

    @RequestMapping(value = "/download-published")
    public void downloadPublishedJournal(HttpServletResponse response, @RequestParam String processEventId) {
        CActionId actionId = ProcessEventUtils.getActionIdFromString(processEventId);
        CJournalElement journalElement = journalElementService.selectByAction(actionId, true);
        if (Objects.isNull(journalElement.getPdfFile())) {
            throw new RuntimeException("Cannot find PDF for published publication ! ProcessEventId = " + processEventId);
        }

        AttachmentUtils.writeData(response, journalElement.getPdfFile().getPdfContent(), MediaType.APPLICATION_PDF.toString(), journalElement.getName() + AttachmentUtils.contentTypeToExtensionMap.get(MediaType.APPLICATION_PDF_VALUE));
    }

    @RequestMapping(value = "/download-not-published")
    public void downloadNotPublishedJournal(HttpServletResponse response, @RequestParam String processEventId) {
        CActionId actionId = ProcessEventUtils.getActionIdFromString(processEventId);
        CJournalElement journalElement = journalElementService.selectByAction(actionId, false);
        byte[] content = journalElementService.selectNotPublishedElementFile(journalUrl, actionId);
        AttachmentUtils.writeData(response, content, MediaType.APPLICATION_PDF.toString(), journalElement.getName() + AttachmentUtils.contentTypeToExtensionMap.get(MediaType.APPLICATION_PDF_VALUE));
    }

}
