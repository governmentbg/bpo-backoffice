package bg.duosoft.ipas.integration.efuserchange.service;

public interface EfUserChangeService {
    String NOTIFY_USER_CHANGED_URL = "?callerUser={callerUser}&oldOwner={oldOwner}&newOwner={newOwner}&applicationNumber={applicationNumber}";

    boolean notifyUserChanged(String oldOwner,String newOwner,String applicationNumber);
}
