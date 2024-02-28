package com.duosoft.ipas.controller.ipobjects.patentlike.patent;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.security.bpo.authorization.domain.UserDetails;
import bg.duosoft.security.bpo.authorization.domain.impl.UserDetailsImpl;
import bg.duosoft.security.bpo.authorization.token.BPOAuthenticationToken;
import com.duosoft.ipas.IpasApplication;
import com.duosoft.ipas.config.YAMLConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IpasApplication.class})
@ContextConfiguration(classes = IpasDatabaseConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@Ignore
public class PatentSearchControllerTest {

    private static final String URL = "/patent/search?TOKEN=test";
    private static final String title = "Търсене на изобретения";
    private final static String FILE_SEQ = "BG";
    private final static String FILE_TYP = "P";
    private final static int FILE_SER = 2018;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IpoSearchService searchService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WebApplicationContext context;

    private IpFilePK ipFilePK0;
    private IpFilePK ipFilePK1;
    private IpFilePK ipFilePK2;
    private IpFilePK ipFilePK3;
    private IpFilePK ipFilePK4;
    private IpFilePK ipFilePK5;
    private IpFilePK ipFilePK6;

    private Locale locale;

    private void setUpAuthentication() {
        UserDetails userDetails = new UserDetailsImpl("test",
                "pass",
                false,
                false,
                false,
                true,
                new ArrayList<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ADMIN"))),
                false);

        BPOAuthenticationToken authentication = new BPOAuthenticationToken(
                "username", "", userDetails,
                userDetails,
                new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ADMIN"))),
                true);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Before
    public void setUp() {
        Locale.Builder localeBuilder = new Locale.Builder();
        localeBuilder.setLanguage("bg");
        localeBuilder.setRegion("BG");

        locale = localeBuilder.build();


        indexService.delete(IpPatent.class);
        ipFilePK0 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112661);
        indexService.index(ipFilePK0, IpPatent.class);

        ipFilePK1 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112662);
        indexService.index(ipFilePK1, IpPatent.class);

        ipFilePK2 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112663);
        indexService.index(ipFilePK2, IpPatent.class);

        ipFilePK3 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112664);
        indexService.index(ipFilePK3, IpPatent.class);

        ipFilePK4 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112665);
        indexService.index(ipFilePK4, IpPatent.class);

        ipFilePK5 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112666);
        indexService.index(ipFilePK5, IpPatent.class);

        ipFilePK6 = new IpFilePK(FILE_SEQ, FILE_TYP, 1937, 29479);
        indexService.index(ipFilePK6, IpPatent.class);
    }

    @Test
    public void contextLoads() throws Exception {
        setUpAuthentication();
        this.mockMvc.perform(get(URL)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prop"))
                .andExpect(model().attributeExists("searchParam"))
                .andExpect(view().name("ipobjects/common/search/index"));

    }

    @Test
    public void prop() throws Exception {

        setUpAuthentication();
        MvcResult mvcResult = this.mockMvc.perform(get(URL)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        YAMLConfig.Search.SearchBase prop = (YAMLConfig.Search.SearchBase) mvcResult.getModelAndView().getModel().get("prop");

        assertEquals("patent", prop.getMsgPrefix());
    }

    @Test
    public void getResult() throws Exception {
        String json = "{  \n" +
                "   \"title\":\"\",\n" +
                "   \"fromFileNbr\":\"\",\n" +
                "   \"toFileNbr\":\"\",\n" +
                "   \"fromFilingDate\":\"\",\n" +
                "   \"toFilingDate\":\"\",\n" +
                "   \"fromRegistrationNbr\":\"\",\n" +
                "   \"toRegistrationNbr\":\"\",\n" +
                "   \"fromRegistrationDate\":\"\",\n" +
                "   \"toRegistrationDate\":\"\",\n" +
                "   \"fromExpirationDate\":\"\",\n" +
                "   \"toExpirationDate\":\"\",\n" +
                "   \"fromEntitlementDate\":\"\",\n" +
                "   \"toEntitlementDate\":\"\",\n" +
                "   \"ownerName\":\"\",\n" +
                "   \"ownerNationality\":\"\",\n" +
                "   \"inventorName\":\"\",\n" +
                "   \"representativeName\":\"\",\n" +
                "   \"agentCode\":\"\",\n" +
                "   \"statusCodes\":[],\n" +
                "   \"patentSummary\":\"\",\n" +
                "   \"ipcClasses\":[],\n" +
                "   \"ipcClassType\":\"AND\",\n" +
                "   \"page\":0,\n" +
                "   \"pageSize\":\"10\",\n" +
                "   \"sortColumn\":\"title\",\n" +
                "   \"sortOrder\":\"asc\"\n" +
                "}";

        setUpAuthentication();
        this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prop"))
                .andExpect(model().attributeExists("searchParam"))
                .andExpect(view().name("ipobjects/common/search/list::result"));

        MvcResult mvcResult = this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
        Page page = (Page) mvcResult.getModelAndView().getModel().get("page");
        assertEquals(7, page.getContent().size());
    }

    @Test
    public void getResultByFilingDate() throws Exception {
        String json = "{  \n" +
                "   \"title\":\"\",\n" +
                "   \"fromFileNbr\":\"\",\n" +
                "   \"toFileNbr\":\"\",\n" +
                "   \"fromFilingDate\":\"22.06.1937\",\n" +
                "   \"toFilingDate\":\"\",\n" +
                "   \"fromRegistrationNbr\":\"\",\n" +
                "   \"toRegistrationNbr\":\"\",\n" +
                "   \"fromRegistrationDate\":\"\",\n" +
                "   \"toRegistrationDate\":\"\",\n" +
                "   \"fromExpirationDate\":\"\",\n" +
                "   \"toExpirationDate\":\"\",\n" +
                "   \"fromEntitlementDate\":\"\",\n" +
                "   \"toEntitlementDate\":\"\",\n" +
                "   \"ownerName\":\"\",\n" +
                "   \"ownerNationality\":\"\",\n" +
                "   \"inventorName\":\"\",\n" +
                "   \"representativeName\":\"\",\n" +
                "   \"agentCode\":\"\",\n" +
                "   \"statusCodes\":[],\n" +
                "   \"patentSummary\":\"\",\n" +
                "   \"ipcClasses\":[],\n" +
                "   \"ipcClassType\":\"AND\",\n" +
                "   \"page\":0,\n" +
                "   \"pageSize\":\"10\",\n" +
                "   \"sortColumn\":\"title\",\n" +
                "   \"sortOrder\":\"asc\"\n" +
                "}";

        setUpAuthentication();
        this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prop"))
                .andExpect(model().attributeExists("searchParam"))
                .andExpect(view().name("ipobjects/common/search/list::result"));

        MvcResult mvcResult = this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        Page page = (Page) mvcResult.getModelAndView().getModel().get("page");

        assertEquals(7, page.getContent().size());
    }

    @Test
    public void getResultByRangeFilingDate() throws Exception {
        String json = "{  \n" +
                "   \"title\":\"\",\n" +
                "   \"fromFileNbr\":\"\",\n" +
                "   \"toFileNbr\":\"\",\n" +
                "   \"fromFilingDate\":\"22.06.1937\",\n" +
                "   \"toFilingDate\":\"22.06.1937\",\n" +
                "   \"fromRegistrationNbr\":\"\",\n" +
                "   \"toRegistrationNbr\":\"\",\n" +
                "   \"fromRegistrationDate\":\"\",\n" +
                "   \"toRegistrationDate\":\"\",\n" +
                "   \"fromExpirationDate\":\"\",\n" +
                "   \"toExpirationDate\":\"\",\n" +
                "   \"fromEntitlementDate\":\"\",\n" +
                "   \"toEntitlementDate\":\"\",\n" +
                "   \"ownerName\":\"\",\n" +
                "   \"ownerNationality\":\"\",\n" +
                "   \"inventorName\":\"\",\n" +
                "   \"representativeName\":\"\",\n" +
                "   \"agentCode\":\"\",\n" +
                "   \"statusCodes\":[],\n" +
                "   \"patentSummary\":\"\",\n" +
                "   \"ipcClasses\":[],\n" +
                "   \"ipcClassType\":\"AND\",\n" +
                "   \"page\":0,\n" +
                "   \"pageSize\":\"10\",\n" +
                "   \"sortColumn\":\"title\",\n" +
                "   \"sortOrder\":\"asc\"\n" +
                "}";


        setUpAuthentication();
        this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prop"))
                .andExpect(model().attributeExists("searchParam"))
                .andExpect(view().name("ipobjects/common/search/list::result"));

        MvcResult mvcResult = this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        Page page = (Page) mvcResult.getModelAndView().getModel().get("page");

        assertEquals(1, page.getContent().size());
    }

    @Test
    public void testLeftSideMenu() throws Exception {

        assertEquals(messageSource.getMessage("search.patent.name", null, locale), "Наименование");
        assertEquals(messageSource.getMessage("search.patent.summary", null, locale), "Ключови думи от реферата");
        assertEquals(messageSource.getMessage("search.patent.file-number", null, locale), "Заявителски номер");
        assertEquals(messageSource.getMessage("search.patent.filing-date", null, locale), "Дата на заявяване");
        assertEquals(messageSource.getMessage("search.patent.registration-number", null, locale), "Регистров номер");
        assertEquals(messageSource.getMessage("search.patent.registration-date", null, locale), "Дата на регистрация");
        assertEquals(messageSource.getMessage("search.patent.entitlement-date", null, locale), "Начало на закрила");
        assertEquals(messageSource.getMessage("search.patent.expiration-date", null, locale), "Срок на закрила");
        assertEquals(messageSource.getMessage("search.patent.owner.menu", null, locale), "Заявител/притежател");
        assertEquals(messageSource.getMessage("search.patent.inventor.menu", null, locale), "Изобретател");
        assertEquals(messageSource.getMessage("search.patent.representative.menu", null, locale), "ПИС");
        assertEquals(messageSource.getMessage("search.patent.status-codes", null, locale), "Статус");
        assertEquals(messageSource.getMessage("search.patent.action-type", null, locale), "Действия");
        assertEquals(messageSource.getMessage("search.patent.publication", null, locale), "Данни за публикации");
        assertEquals(messageSource.getMessage("search.patent.ipc-class", null, locale), "Клас по МПК");

/*
        this.mockMvc.perform(get(URL).with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prop"))
                .andExpect(model().attributeExists("searchParam"))
                .andExpect(view().name("ipobjects/common/search/index"))
                .andExpect(xpath("//ul[@id=\"detail-list\"]/li[1]/span").string(messageSource.getMessage("search.patent.name", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[3]/span").string(messageSource.getMessage("search.patent.summary", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[3]/span").string(messageSource.getMessage("search.patent.file-number", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[4]/span").string(messageSource.getMessage("search.patent.filing-date", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[5]/span").string(messageSource.getMessage("search.patent.registration-number", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[6]/span").string(messageSource.getMessage("search.patent.registration-date", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[7]/span").string(messageSource.getMessage("search.patent.entitlement-date", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[8]/span").string(messageSource.getMessage("search.patent.expiration-date", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[9]/span").string(messageSource.getMessage("search.patent.owner.menu", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[10]/span").string(messageSource.getMessage("search.patent.inventor.menu", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[11]/span").string(messageSource.getMessage("search.patent.representative.menu", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[12]/span").string(messageSource.getMessage("search.patent.status-codes", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[13]/span").string(messageSource.getMessage("search.patent.action-type", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[14]/span").string(messageSource.getMessage("search.patent.publication", null, locale)))
                .andExpect(xpath("//*[@id=\"detail-list\"]/li[15]/span").string(messageSource.getMessage("search.patent.ipc-class", null, locale)));
*/
    }

    @Test
    public void getResultByRangeFilingDateTestResultTable() throws Exception {
        String json = "{  \n" +
                "   \"title\":\"\",\n" +
                "   \"fromFileNbr\":\"\",\n" +
                "   \"toFileNbr\":\"\",\n" +
                "   \"fromFilingDate\":\"22.06.1937\",\n" +
                "   \"toFilingDate\":\"22.06.1937\",\n" +
                "   \"fromRegistrationNbr\":\"\",\n" +
                "   \"toRegistrationNbr\":\"\",\n" +
                "   \"fromRegistrationDate\":\"\",\n" +
                "   \"toRegistrationDate\":\"\",\n" +
                "   \"fromExpirationDate\":\"\",\n" +
                "   \"toExpirationDate\":\"\",\n" +
                "   \"fromEntitlementDate\":\"\",\n" +
                "   \"toEntitlementDate\":\"\",\n" +
                "   \"ownerName\":\"\",\n" +
                "   \"ownerNationality\":\"\",\n" +
                "   \"inventorName\":\"\",\n" +
                "   \"representativeName\":\"\",\n" +
                "   \"agentCode\":\"\",\n" +
                "   \"statusCodes\":[],\n" +
                "   \"patentSummary\":\"\",\n" +
                "   \"ipcClasses\":[],\n" +
                "   \"ipcClassType\":\"AND\",\n" +
                "   \"page\":0,\n" +
                "   \"pageSize\":\"10\",\n" +
                "   \"sortColumn\":\"title\",\n" +
                "   \"sortOrder\":\"asc\"\n" +
                "}";

        assertEquals(messageSource.getMessage("list.result.count", new String[]{"1"}, locale), "Общ брой: 1");
        assertEquals(messageSource.getMessage("search.result.number", null, locale), "Номер");
        assertEquals(messageSource.getMessage("search.result.filingDate", null, locale), "Дата на заявяване");
        assertEquals(messageSource.getMessage("search.result.registrationNbr", null, locale), "Регистров номер");
        assertEquals(messageSource.getMessage("search.result.mainOwner", null, locale), "Заявител");
        assertEquals(messageSource.getMessage("search.result.title", null, locale), "Наименование");
        assertEquals(messageSource.getMessage("search.result.status", null, locale), "Статус");

        setUpAuthentication();
        this.mockMvc.perform(post(URL)
                .param("data", json)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("prop"))
                .andExpect(model().attributeExists("searchParam"))
                .andExpect(view().name("ipobjects/common/search/list::result"))
                .andExpect(xpath("//div/div[1]/div/span").string(messageSource.getMessage("list.result.count", new String[]{"1"}, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[1]/span").string(messageSource.getMessage("search.result.number", null, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[1][@class=\"col-2\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[1][@data-col=\"pk\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[1]/div").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[2]/span").string(messageSource.getMessage("search.result.filingDate", null, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[2][@class=\"col-1\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[2][@data-col=\"filingDate\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[2]/div").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[3]/span").string(messageSource.getMessage("search.result.registrationNbr", null, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[3][@class=\"col-1\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[3][@data-col=\"registrationNbr\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[3]/div").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[4]/span").string(messageSource.getMessage("search.result.mainOwner", null, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[4][@class=\"col-3\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[4][@data-col=\"servicePerson\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[4]/div").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[5]/span").string(messageSource.getMessage("search.result.title", null, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[5][@class=\"col-3\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[5][@data-col=\"title\"]").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[5]/div").exists())
                .andExpect(xpath("//*[@id=\"result-header\"]/div[6]/span").string(messageSource.getMessage("search.result.status", null, locale)))
                .andExpect(xpath("//*[@id=\"result-header\"]/div[6][@class=\"col-2\"]").exists())
                .andExpect(xpath("//div/div[2]/ul/li[2]/div/div[1]/a").exists());

    }
}