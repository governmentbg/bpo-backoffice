package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 23:40
 */
public interface VwIpObjectIndex extends VwIndex {
    IpFilePK getPk();

    VwFileIndex getFile();

    String getOwnerPersonNumbers();

    String getRepresentativePersonNumbers();
}
