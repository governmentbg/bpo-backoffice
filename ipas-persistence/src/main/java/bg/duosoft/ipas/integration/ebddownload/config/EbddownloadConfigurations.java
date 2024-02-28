package bg.duosoft.ipas.integration.ebddownload.config;

import bg.bpo.ebd.ebddpersistence.entity.*;
import bg.bpo.ebd.ebddpersistence.service.EbdNomenclatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EbddownloadConfigurations {

    @Autowired
    private EbdNomenclatureService ebdNomenclatureService;
    private boolean initialized = false;

    private static Map<String, EbdIdoperActionTypMap> idoperToActionTypMap;
    private static Map<Integer, Integer> usersMap = null;
    private static Map<Integer, String> ipasStatusMap = null;
    private static Map<Integer, String> ipasPublicationSectionMap = null;
    private static Set<Integer> csStatusesForUpdateInIpas = null;

    @PostConstruct
    public void readConfigurations() {
        try {
            init();
        } catch (Exception e) {
            log.warn("Cannot init ebd configurations", e);
        }
    }
    private void init() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    initActionTypMap();
                    initUsersMap();
                    initStatusMap();
                    initPublicationSectionMap();
                    initCsStatusesForUpdateInIpas();
                    initialized = true;
                }
            }
        }
    }

    private void initActionTypMap() {
        List<EbdIdoperActionTypMap> ebdIdoperActionTypMaps = ebdNomenclatureService.selectAllIdoperActionTypes();
        idoperToActionTypMap = ebdIdoperActionTypMaps.stream().collect(Collectors.toMap(r -> r.getId().getIdoper(), Function.identity()));
    }

    private void initUsersMap() {
        usersMap = new HashMap<>();

        List<EbdUsersMap> ebdUsersMaps = ebdNomenclatureService.selectAllUsers();
        ebdUsersMaps.forEach(result -> {
            EbdUsersMapId id = result.getId();
            usersMap.put(id.getCsId(), id.getIpasId());
        });
    }

    private void initStatusMap() {
        ipasStatusMap = new HashMap<>();

        List<EbdCsIpasStatusMap> ebdCsIpasStatusMaps = ebdNomenclatureService.selectAllCsIpasStatuses();
        ebdCsIpasStatusMaps.forEach(result -> {
            EbdCsIpasStatusMapId id = result.getId();
            ipasStatusMap.put(id.getCsStatus(), String.valueOf(id.getIpasStatus()));
        });
    }

    private void initCsStatusesForUpdateInIpas() {
        csStatusesForUpdateInIpas = new HashSet<>();

        List<EbdCsIpasStatusMap> ebdCsIpasStatusMaps = ebdNomenclatureService.selectAllCsIpasStatusesForUpdate();
        ebdCsIpasStatusMaps.forEach(result -> {
            EbdCsIpasStatusMapId id = result.getId();
            csStatusesForUpdateInIpas.add(id.getCsStatus());
        });
    }

    private void initPublicationSectionMap() {
        ipasPublicationSectionMap = new HashMap<>();

        List<EbdPublisect> ebdPublisects = ebdNomenclatureService.selectAllPublisects();
        ebdPublisects.forEach(result -> ipasPublicationSectionMap.put(result.getIdsection(), result.getNmsection()));
    }

    public String getIpasStatus(int cstatus) {
        init();
        return ipasStatusMap.get(cstatus);
    }

    public String getPublicationSectionName(int sectionId) {
        init();
        return ipasPublicationSectionMap.get(sectionId);
    }


    public String getNormalActionTyp(String idoper) {
        init();
        EbdIdoperActionTypMap rec = idoperToActionTypMap.get(idoper);
        if (rec == null || StringUtils.isEmpty(rec.getNormalActionTyp())) {
            return null;
        }
        return rec.getNormalActionTyp();
    }

    public String getNoteActionTyp(String idoper) {
        init();
        EbdIdoperActionTypMap rec = idoperToActionTypMap.get(idoper);
        if (rec == null || StringUtils.isEmpty(rec.getId().getNoteActionTyp())) {
            return null;
        }
        return rec.getId().getNoteActionTyp();
    }

    public boolean hasNormalActionType(String idoper) {
        init();
        return getNormalActionTyp(idoper) != null;
    }

    public boolean hasNoteActionType(String idoper) {
        init();
        return getNoteActionTyp(idoper) != null;

    }

    public boolean hasAnyActionType(String idoper) {
        init();
        return hasNormalActionType(idoper) || hasNoteActionType(idoper);
    }

    public Integer getIpasUserId(int csUserId) {
        init();
        return usersMap.get(csUserId);
    }

    public boolean isCsStatusForUpdateInIpas(Integer csStatus) {
        init();
        return csStatusesForUpdateInIpas.contains(csStatus);
    }


}
