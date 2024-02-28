package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.patent.RAcceptEuPatentRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptedEuPatentResponse;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface EpoPatentRestClient {


    RAcceptedEuPatentResponse acceptEpoPatent(RAcceptEuPatentRequest rq);
}
