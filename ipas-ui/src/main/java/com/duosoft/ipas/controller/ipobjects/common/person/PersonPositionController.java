package com.duosoft.ipas.controller.ipobjects.common.person;

import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.enums.Direction;
import com.duosoft.ipas.enums.PersonKind;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.PersonUtils;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.person.PersonSessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/person/change-position")
public class PersonPositionController extends BasePersonActionController {

    @PostMapping
    public String changePositin(HttpServletRequest request, Model model,
                               @RequestParam String sessionIdentifier,
                               @RequestParam Integer personKind,
                               @RequestParam Integer personNbr,
                               @RequestParam Direction direction,
                               @RequestParam Integer addressNbr) {
        PersonKind personKindEnum = PersonKind.selectByCode(personKind);
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case PATENT:
                changePatentPersonPosition(request, sessionIdentifier, personNbr, addressNbr, personKindEnum, direction);
                break;

        }
        return personListPage(request, model, sessionIdentifier, personKind);
    }

    private void changePatentPersonPosition(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, PersonKind personKind, Direction direction) {
        switch (personKind) {
            case Inventor:
                changeInventorPosition(request, sessionIdentifier, personNbr, addressNbr, direction);
                break;
        }
    }

    private void changeInventorPosition(HttpServletRequest request, String sessionIdentifier, Integer personNbr, Integer addressNbr, Direction direction) {
        CAuthorshipData authorshipData = PersonSessionUtils.getSessionAuthorshipData(request, sessionIdentifier);
        if (Objects.nonNull(authorshipData)) {
            List<CAuthor> authorList = authorshipData.getAuthorList();
            reorderAuthors(authorshipData);
            CAuthor inventor = PersonUtils.selectAuthor(personNbr, addressNbr, authorList);
            if (Objects.isNull(inventor)){
                throw new RuntimeException("Cannot find inventor with PK " + personNbr + "-" + addressNbr + " in inventors list !");
            }

            Long number = inventor.getAuthorSeq();
            if (Objects.isNull(authorList)){
                throw new RuntimeException("Inventor sequence number is empty !");
            }

            Long otherNumber = null;
            switch (direction){
                case UP:
                    otherNumber  = number - 1;
                    break;
                case DOWN:
                    otherNumber  = number + 1;
                    break;
            }

            CAuthor otherInventor = PersonUtils.selectAuthorBySequenceNumber(otherNumber, authorList);
            if (Objects.isNull(otherInventor)){
                throw new RuntimeException("Cannot find inventor with sequence number " + otherNumber);
            }

            inventor.setAuthorSeq(otherNumber);
            otherInventor.setAuthorSeq(number);
        }
    }

}
