package bg.duosoft.ipas.util.home_page;

import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.util.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageUtils {

    public static List<Integer> getResponsibleUsersForMyObjectPanels(UserService userService, Integer filterResponsibleUser){
        if (Objects.isNull(filterResponsibleUser)){
            return null;
        }

        List<Integer> responsibleUsersForMyObjectPanels=new ArrayList<>();
        List<Integer> loggedUserAndAuthorizedByUsers = userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId());
        Integer filterResponsibleUserAsResult = loggedUserAndAuthorizedByUsers.stream().filter(r->r.equals(filterResponsibleUser)).findFirst().orElse(null);

        if (Objects.isNull(filterResponsibleUserAsResult)){
            return null;
        }
        if (filterResponsibleUserAsResult.equals(SecurityUtils.getLoggedUserId())){
            responsibleUsersForMyObjectPanels.addAll(loggedUserAndAuthorizedByUsers);
        }else{
            responsibleUsersForMyObjectPanels.add(filterResponsibleUserAsResult);
        }

        return responsibleUsersForMyObjectPanels;
    }
}
