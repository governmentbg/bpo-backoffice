package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocProcs;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocProcsPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

public interface IpUserdocProcsRepository extends BaseRepository<IpUserdocProcs, IpUserdocProcsPK> {

    IpUserdocProcs findByPk_DocOriAndPk_DocLogAndPk_DocSerAndPk_DocNbr(String docOri, String docLog, Integer docSer, Integer docNbr);

}
