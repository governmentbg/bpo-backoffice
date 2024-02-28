package bg.duosoft.ipas.persistence.repository.entity.custom;

public interface IpActionRepositoryCustom {

    boolean deleteProcessAction(String procTyp, String procNbr, Integer actionNbr, Integer userId, String reason);

}
