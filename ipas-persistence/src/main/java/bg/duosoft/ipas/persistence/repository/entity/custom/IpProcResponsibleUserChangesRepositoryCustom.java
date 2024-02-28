package bg.duosoft.ipas.persistence.repository.entity.custom;

public interface IpProcResponsibleUserChangesRepositoryCustom {
    public void addResponsibleUserChanges(Integer userChanged, String processType, Integer processNbr, Integer oldResponsibleUser, Integer newResponsibleUser);
}
