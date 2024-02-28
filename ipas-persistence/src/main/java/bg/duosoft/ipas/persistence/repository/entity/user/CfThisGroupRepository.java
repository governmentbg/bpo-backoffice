package bg.duosoft.ipas.persistence.repository.entity.user;

import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User: ggeorgiev
 * Date: 20.6.2019 г.
 * Time: 17:59
 */
public interface CfThisGroupRepository extends BaseRepository<CfThisGroup, Integer> {
    @Query("select coalesce(max (g.groupId), 0) + 1 from CfThisGroup g")
    int getNextGroupId();
    CfThisGroup getByGroupname(String groupName);
}
