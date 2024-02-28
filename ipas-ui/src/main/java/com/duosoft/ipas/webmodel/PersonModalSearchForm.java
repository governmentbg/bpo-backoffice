package com.duosoft.ipas.webmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonModalSearchForm {
    private String personNameSearchType;
    private String personName;
    private String street;
    private String city;
    private String zipCode;
    private String email;
    private String telephone;
    private Boolean indCompany;
    private String egn;
    private String lnch;
    private String eik;
}
