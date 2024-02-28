package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.util.person.OwnerUtils;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/person")
public class PersonChangeMainOwnerController extends BasePersonActionController {

    @PostMapping("/change-main-owner")
    public String changeMainOwner(HttpServletRequest request, Model model,
                                  @RequestParam String sessionIdentifier,
                                  @RequestParam Integer personKind,
                                  @RequestParam Integer personNbr,
                                  @RequestParam Integer addressNbr) {

        if (Objects.equals(PersonKind.Applicant.code(), personKind)) {
            COwnershipData ownershipData = PersonSessionUtils.getSessionOwnershipData(request, sessionIdentifier);
            if (Objects.nonNull(ownershipData)) {
                List<COwner> ownerList = ownershipData.getOwnerList();

                Integer minOrderNumber = OwnerUtils.getMinOrderNumber(ownerList);

                final Integer finalMinOrderNumber = minOrderNumber;
                COwner oldMainOwner = ownerList.stream()
                        .filter(cOwner -> Objects.nonNull(cOwner.getOrderNbr()))
                        .filter(cOwner -> cOwner.getOrderNbr().equals(finalMinOrderNumber))
                        .findFirst()
                        .orElse(null);

                COwner selectedOwner = OwnerUtils.selectOwnerFromList(personNbr, addressNbr, ownerList);
                if (Objects.nonNull(selectedOwner)) {
                    Integer numberBeforeUpdate = selectedOwner.getOrderNbr();
                    selectedOwner.setOrderNbr(minOrderNumber);
                    if (Objects.nonNull(oldMainOwner))
                        oldMainOwner.setOrderNbr(numberBeforeUpdate);
                }
            }
        }

        return personListPage(request, model, sessionIdentifier, personKind);
    }

}
