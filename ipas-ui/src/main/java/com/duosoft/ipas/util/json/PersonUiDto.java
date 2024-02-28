package com.duosoft.ipas.util.json;

import bg.duosoft.ipas.core.model.person.CPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonUiDto {
    private Integer personKind;
    private String representativeType;
    private Integer personNbr;
    private String agentCode;
    private Integer addressNbr;
    private String personName;
    private String residenceCountryCode;
    private String nationalityCountryCode;
    private String legalNature;
    private String addressStreet;
    private String cityName;
    private String zipCode;
    private String email;
    private String telephone;
    private String stateName;
    private Boolean indCompany;
    private Integer gralPersonIdNbr;
    private String gralPersonIdTyp;
    private Integer tempParentPersonNbr;
    private String individualIdType;
    private String individualIdTxt;

    public CPerson convertToCPerson() {
        CPerson cPerson = new CPerson();
        cPerson.setPersonNbr(this.personNbr);
        cPerson.setAddressNbr(this.addressNbr);
        cPerson.setPersonName(Objects.isNull(this.personName) ? null : this.personName.trim());
        cPerson.setResidenceCountryCode(Objects.isNull(this.residenceCountryCode) ? null : this.residenceCountryCode.trim());
        cPerson.setNationalityCountryCode(Objects.isNull(this.nationalityCountryCode) ? null : this.nationalityCountryCode.trim());
        cPerson.setLegalNature(Objects.isNull(this.legalNature) ? null : this.legalNature.trim());
        cPerson.setAddressStreet(Objects.isNull(this.addressStreet) ? null : this.addressStreet.trim());
        cPerson.setCityName(Objects.isNull(this.cityName) ? null : this.cityName.trim());
        cPerson.setZipCode(Objects.isNull(this.zipCode) ? null : this.zipCode.trim());
        cPerson.setEmail(Objects.isNull(this.email) ? null : this.email.trim());
        cPerson.setTelephone(Objects.isNull(this.telephone) ? null : this.telephone.trim());
        cPerson.setStateName(Objects.isNull(this.stateName) ? null : this.stateName.trim());
        cPerson.setIndCompany(this.indCompany);
        cPerson.setAgentCode(this.agentCode);
        cPerson.setGralPersonIdNbr(this.gralPersonIdNbr);
        cPerson.setGralPersonIdTyp(this.gralPersonIdTyp);
        cPerson.setTempParentPersonNbr(this.tempParentPersonNbr);
        cPerson.setIndividualIdTxt(this.individualIdTxt);
        cPerson.setIndividualIdType(this.individualIdType);
        return cPerson;
    }

}
