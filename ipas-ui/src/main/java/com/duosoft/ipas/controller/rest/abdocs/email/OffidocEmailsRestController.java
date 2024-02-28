package com.duosoft.ipas.controller.rest.abdocs.email;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.RAbdocsOffidocEmailRequest;
import bg.duosoft.ipas.util.json.JsonUtil;
import com.duosoft.ipas.controller.rest.BaseRestController;
import com.duosoft.ipas.controller.rest.RestApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.duosoft.ipas.controller.rest.BaseRestController.BASE_REST_URL;

@Slf4j
@RestController
@Api(tags = {"ABDOCS"})
@RequiredArgsConstructor
@RequestMapping(BASE_REST_URL + "/abdocs")
public class OffidocEmailsRestController extends BaseRestController {

    private final AbdocsService abdocsServiceAdmin;

    @ApiOperation(value = "Check if abdocs offidoc email exist")
    @PostMapping(value = "/checkIfOffidocEmailExist", produces = "application/json")
    public ResponseEntity<RestApiResponse<Boolean>> checkIfOffidocEmailExist(@RequestBody RestApiRequest<RAbdocsOffidocEmailRequest> request) {
        RAbdocsOffidocEmailRequest data = request.getData();
        if (Objects.isNull(data) || Objects.isNull(data.getDocumentId()) || StringUtils.isEmpty(data.getUuidEmail())) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(
                new RestApiResponse<>(abdocsServiceAdmin.checkIfOffidocEmailExist(data.getDocumentId(), data.getUuidEmail()))
        );
    }

    @ApiOperation(value = "Update abdocs offidoc email status to 'READ' ")
    @PostMapping(value = "/updateOffidocEmailStatusAsRead", produces = "application/json")
    public RestApiResponse<EmptyResponse> updateOffidocEmailStatusAsRead(@RequestBody RestApiRequest<RAbdocsOffidocEmailRequest> request) {
        RAbdocsOffidocEmailRequest data = request.getData();
        if (Objects.isNull(data) || Objects.isNull(data.getDocumentId()) || StringUtils.isEmpty(data.getUuidEmail())) {
            throw new RestApiException("Invalid data ! Request JSON: " + JsonUtil.createJson(request));
        }

        abdocsServiceAdmin.updateOffidocEmailStatusAsRead(data.getDocumentId(), data.getUuidEmail());
        return generateResponse(new EmptyResponse());
    }

}
