package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpActionRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.Objects;

@Slf4j
@Repository
public class IpActionRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpActionRepositoryCustom {

    @Override
    public boolean deleteProcessAction(String procTyp, String procNbr, Integer actionNbr, Integer userId, String reason) {

        try {
            StoredProcedureQuery query = this.em.createStoredProcedureQuery("deleteProcessAction");
            query.registerStoredProcedureParameter("procTyp", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("procNbr", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("actionNbr", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("deleteUserId", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("deleteReason", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("executionStatus", Boolean.class, ParameterMode.OUT);
            query.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);

            query.setParameter("procTyp", procTyp);
            query.setParameter("procNbr", procNbr);
            query.setParameter("actionNbr", actionNbr);
            query.setParameter("deleteUserId", userId);
            query.setParameter("deleteReason", reason);

            query.execute();

            Boolean errorExists = (Boolean) query.getOutputParameterValue("executionStatus");
            String errorMessage = (String) query.getOutputParameterValue("errorMessage");
            if (Objects.nonNull(errorExists) && errorExists) {
                throw new RuntimeException(errorMessage);
            }

            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
