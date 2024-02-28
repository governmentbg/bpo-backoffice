package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl extends BaseRepositoryCustomImpl implements UserRepository {
    @Override
    public List<Integer> findDepartmentUsersByMainUser(Integer userId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select u.USER_ID\n" +
                "from ipasprod.ip_user u\n" +
                "join ipasprod.CF_OFFICE_DEPARTMENT d on u.OFFICE_DIVISION_CODE = d.OFFICE_DIVISION_CODE and u.OFFICE_DEPARTMENT_CODE = d.OFFICE_DEPARTMENT_CODE\n" +
                "where concat(d.OFFICE_DIVISION_CODE,d.OFFICE_DEPARTMENT_CODE)  = (select concat(d.OFFICE_DIVISION_CODE, d.OFFICE_DEPARTMENT_CODE)\n" +
                "from ipasprod.ip_user u\n" +
                "left outer join ipasprod.CF_OFFICE_DEPARTMENT d on u.USER_ID=d.SIGNATURE_USER_ID\n" +
                "where user_id=:userId)");

        Query query = em.createNativeQuery(queryBuilder.toString());
        query.setParameter("userId",userId);
        List<Integer> resultList=new ArrayList<>();
        resultList.add(userId);
        List<BigDecimal> userIds = query.getResultList();
        if (!CollectionUtils.isEmpty(userIds)){
            resultList.addAll(userIds.stream().map(BigDecimal::intValue).collect(Collectors.toList()));
        }
        return  resultList;
    }
}
