UPDATE
    EXT_CORE.CF_SETTLEMENT
SET isdistrict = 1
FROM EXT_CORE.CF_DISTRICT d
         JOIN EXT_CORE.CF_SETTLEMENT s ON (d.code = s.districtcode and d.name = s.settlementname) OR
                                          (d.name = 'София (столица)' AND s.settlementname = 'София');


