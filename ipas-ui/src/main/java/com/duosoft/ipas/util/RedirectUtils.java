package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.controller.ipobjects.common.reception.ReceptionDetailsController;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedirectUtils {

    public static String redirectToOffidocViewPage(String offidocId) {
        return "redirect:/offidoc/detail/" + offidocId;
    }

    public static String redirectToOffidocViewPage(COffidocId cOffidocId) {
        return redirectToOffidocViewPage(cOffidocId.createFilingNumber());
    }

    public static  String redirectToMarkPage(RedirectAttributes redirectAttributes, CMark mark, String sessionIdentifier, List<String> editedPanels) {
        redirectAttributes.addFlashAttribute("mark", mark);
        redirectAttributes.addFlashAttribute("sessionObjectIdentifier", sessionIdentifier);
        redirectAttributes.addFlashAttribute("editedPanels", editedPanels);
        return RedirectUtils.redirectToObjectViewPage(mark.getFile().getFileId(), mark.isReception());
    }

    public static String redirectToObjectViewPage(CFileId cFileId, boolean isReception) {
        String receptionParam = isReception ? ReceptionDetailsController.RECEPTION_PARAMETER : "";
        String encodedFilingNumber = Stream.of(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries().toString(), cFileId.getFileNbr().toString())
                .map(RedirectUtils::encodeValue)
                .collect(Collectors.joining(DefaultValue.IPAS_OBJECT_ID_SEPARATOR));

        FileType fileType = FileType.selectByCode(cFileId.getFileType());
        return "redirect:/" + CoreUtils.getUrlPrefixByFileType(fileType) + "/detail/" + encodedFilingNumber + receptionParam;
    }

    public static String redirectToObjectViewPage(String filingNumber, boolean isReception) {
        CFileId cFileId = BasicUtils.createCFileId(filingNumber);
        if (Objects.isNull(cFileId))
            return null;

        return redirectToObjectViewPage(cFileId, isReception);
    }

    public static String redirectToObjectViewPage(String filingNumber) {
        CFileId cFileId = BasicUtils.createCFileId(filingNumber);
        if (Objects.isNull(cFileId))
            return null;

        return redirectToObjectViewPage(cFileId, false);
    }

    public static String redirectToManualSubProcessViewPage(CProcessId id) {
        return "redirect:/msprocess/detail/" + id.getProcessType() + "/" + id.getProcessNbr();
    }

    public static String redirectToOpenExistingObject(RedirectAttributes redirectAttributes, String sessionObjectIdentifier, boolean openExistingObject, String scrollToPanel) {
        if (!StringUtils.isEmpty(scrollToPanel))
            redirectAttributes.addFlashAttribute("scrollToPanel", scrollToPanel);

        redirectAttributes.addAttribute("sessionObject", sessionObjectIdentifier);
        redirectAttributes.addAttribute("openExistingObject", openExistingObject);
        return "redirect:/session/open-existing-object";
    }

    public static String encodeValue(String value) {
        return URLEncoder.encode(value, Charset.forName("UTF-8"));
    }
}
