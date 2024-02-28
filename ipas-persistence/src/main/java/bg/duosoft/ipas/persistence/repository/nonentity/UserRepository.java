package bg.duosoft.ipas.persistence.repository.nonentity;

import java.util.List;

public interface UserRepository {

    List<Integer> findDepartmentUsersByMainUser(Integer userId);
}
