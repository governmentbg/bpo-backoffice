package com.duosoft.ipas.config;

import bg.duosoft.ipas.core.service.structure.GroupService;
import bg.duosoft.ipas.enums.security.SecurityRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 26.7.2019 г.
 * Time: 12:18
 */
@Configuration
@Slf4j
public class DatabaseRolesValidator {
    @Autowired
    private GroupService groupService;
    @Autowired
    private YAMLConfig yamlConfig;

    @PostConstruct
    public void validateRolesConfiguration() {
        Map<String, String> dbRoles = groupService.getRolesMap();
        List<String> errors = new ArrayList<>();
        List<String> generatedSqls = new ArrayList<>();
        List<SecurityRole> missingDbRecords = Arrays.stream(SecurityRole.values()).filter(r -> !dbRoles.containsKey(r.code())).collect(Collectors.toList());
        missingDbRecords.forEach(r -> errors.add("MISSING DB CONFIGURATION FOR ROLE " + r.code() + " INSIDE TABLE EXT_USER.CF_SECURITY_ROLES !"));
        missingDbRecords.forEach(r -> generatedSqls.add(String.format("INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('%s','%s');", r.code(), r.code() )));

        List<Map.Entry<String, String>> incorrectReocrds = dbRoles.entrySet().stream().filter(r -> SecurityRole.getSecurityRole(r.getKey()) == null).collect(Collectors.toList());
        incorrectReocrds.forEach(r -> errors.add(String.format("Role %s is defined in EXT_USER.CF_SECURITY_ROLES but do not exist in the SecurityRoles class ", r.getKey())));
        incorrectReocrds.forEach(r -> generatedSqls.add(String.format("DELETE FROM EXT_USER.CF_GROUP_SECURITY_ROLE WHERE ROLE_NAME = '%s'; DELETE FROM EXT_USER.CF_SECURITY_ROLES WHERE ROLE_NAME = '%s'; ", r.getKey(), r.getKey())));

        if (errors.size() > 0) {
            String errorMessage = "Inconsistent table EXT_USER.CF_SECURITY_ROLES inside the database. Please correct the errors and continue! Errors:\n" + errors.stream().collect(Collectors.joining("\n")) + "\nSuggested SQLs:\n" + generatedSqls.stream().collect(Collectors.joining("\n"));
            //TODO:NE E OK V procesa na development da se hvyrlq exception, tyj kato chovek shte si dobavi rolq lokalno, koqto q nqma oshte v bazata i drugite developers shte imat problemi (ili obratnoto - dobavq q v bazata i SecrurityRoles, no oshte ne e commitnal - drugite shte imat problemi....)
            log.warn(errorMessage);
            if (yamlConfig.isValidateSecurityRoles()) {
                throw new RuntimeException(errorMessage);
            }
        }
    }

}
