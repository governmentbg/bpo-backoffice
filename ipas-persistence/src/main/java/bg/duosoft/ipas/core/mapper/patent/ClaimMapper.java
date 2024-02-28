package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentClaims;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentClaimsPK;
import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ClaimMapper {

    public List<CClaim> toCore(List<IpPatentClaims> ipPtClaims) {
        List<CClaim> cClaims=new ArrayList<>();
        if (ipPtClaims!=null && !ipPtClaims.isEmpty()){

            List<IpPatentClaims> ipPatentClaims=ipPtClaims;

            for (IpPatentClaims ipPatentClaim: ipPatentClaims) {

                if (!cClaims.isEmpty()){

                    boolean claimContains=false;

                    for (CClaim cClaim :cClaims) {
                        if (cClaim.getClaimNbr().equals(ipPatentClaim.getPk().getClaimNbr().longValue())){
                            if (cClaim.getClaimDescription() != null) {
                                cClaim.setClaimEnglishDescription(ipPatentClaim.getClaimDescription());
                            } else {
                                cClaim.setClaimDescription(ipPatentClaim.getClaimDescription());
                            }
                            claimContains=true;
                        }
                    }
                    if (!claimContains){
                        if (ipPatentClaim.getPk().getLanguageCode().equals("MX")){
                            cClaims.add(new CClaim(ipPatentClaim.getPk().getClaimNbr().longValue(),ipPatentClaim.getClaimDescription(),null));
                        }
                        if (ipPatentClaim.getPk().getLanguageCode().equals("US")){
                            cClaims.add(new CClaim(ipPatentClaim.getPk().getClaimNbr().longValue(),null,ipPatentClaim.getClaimDescription()));
                        }
                    }

                }else{
                    if (ipPatentClaim.getPk().getLanguageCode().equals("MX")){
                        cClaims.add(new CClaim(ipPatentClaim.getPk().getClaimNbr().longValue(),ipPatentClaim.getClaimDescription(),null));
                    }
                    if (ipPatentClaim.getPk().getLanguageCode().equals("US")){
                        cClaims.add(new CClaim(ipPatentClaim.getPk().getClaimNbr().longValue(),null,ipPatentClaim.getClaimDescription()));
                    }
                }
            }
        }
       return cClaims;

    }

    public List<IpPatentClaims> toEntity(List<CClaim> cClaimsList) {

        List<IpPatentClaims>ipPatentClaims=new ArrayList<>();
        List<CClaim> cClaims=cClaimsList;
        if(!CollectionUtils.isEmpty(cClaims)){
            for (CClaim cClaim:cClaims) {
                IpPatentClaimsPK ipPatentClaimsPkMX=new IpPatentClaimsPK();
                ipPatentClaimsPkMX.setLanguageCode("MX");
                ipPatentClaimsPkMX.setClaimNbr(cClaim.getClaimNbr().intValue());
                IpPatentClaims ipPatentClaimsMX=new IpPatentClaims();
                ipPatentClaimsMX.setRowVersion(1);
                ipPatentClaimsMX.setClaimDescription(cClaim.getClaimDescription());
                ipPatentClaimsMX.setPk(ipPatentClaimsPkMX);

                IpPatentClaimsPK ipPatentClaimsPkUS=new IpPatentClaimsPK();
                ipPatentClaimsPkUS.setLanguageCode("US");
                ipPatentClaimsPkUS.setClaimNbr(cClaim.getClaimNbr().intValue());
                IpPatentClaims ipPatentClaimsUS=new IpPatentClaims();
                ipPatentClaimsUS.setRowVersion(1);
                ipPatentClaimsUS.setClaimDescription(cClaim.getClaimEnglishDescription());
                ipPatentClaimsUS.setPk(ipPatentClaimsPkUS);

                ipPatentClaims.add(ipPatentClaimsUS);
                ipPatentClaims.add(ipPatentClaimsMX);
            }
        }


        return  ipPatentClaims;

    }
}
