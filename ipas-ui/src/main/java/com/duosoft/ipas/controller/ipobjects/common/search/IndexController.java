package com.duosoft.ipas.controller.ipobjects.common.search;


import bg.duosoft.ipas.core.service.action.ActionService;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.ext.IndexQueueService;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.core.service.mark.*;
import bg.duosoft.ipas.core.service.patent.*;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.util.search.IndexProgressMonitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/index")
public class IndexController {

    @Autowired
    protected IndexService indexService;

    @Autowired
    private MarkService markService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PatentSummaryService patentSummaryService;

    @Autowired
    private PatentIpcClassesService ipcClassesService;

    @Autowired
    private PatentCpcClassesService cpcClassesService;

    @Autowired
    private MarkNiceClassesService niceClassesService;

    @Autowired
    private LogoViennaClassesService viennaClassesService;

    @Autowired
    private AttachmentViennaClassesService attachmentViennaClassesService;

    @Autowired
    private PatentLocarnoClassesService patentLocarnoClassesService;

    @Autowired
    private FileRelationshipsService fileRelationshipsService;

    @Autowired
    private DocService docService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private UserdocPersonService userdocPersonService;

    @Autowired
    private IndexQueueService indexQueueService;

    @ModelAttribute
    public void setupModelAttr(final Model model) {
        model.addAttribute("isStartedIndexing", IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning());

        log.debug("Setup model attributes  : {}", model);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public final ModelAndView index(final ModelAndView mav) {

        mav.setViewName("ipobjects/common/search/index-page");

        setup(mav);

        return mav;
    }

    @RequestMapping(value = {"/all"}, method = RequestMethod.GET)
    public final ModelAndView indexAll(final ModelAndView mav, @RequestParam(required = false, name = "async") Boolean async) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            //po podrazbirane startira indexiraneto sinhronno v otdelna nishka, za da moje tekushtata da vyrne stranicata s indexite
            if (async == null || !async) {
                new Thread(() -> indexService.indexAll(false)).start();
                //zaspiva za 5 sekundi, za da dade shans na IndexProgressMonitor-a da se inicializira...Ne e naj-umnoto neshto....
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                indexService.indexAll(true);
            }
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/patents"}, method = RequestMethod.GET)
    public final ModelAndView indexPatents(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexPatents();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/patents"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingPatents(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailPatentToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/marks"}, method = RequestMethod.GET)
    public final ModelAndView indexMarks(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexMarks();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/marks"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingMarks(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailMarksToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/actions"}, method = RequestMethod.GET)
    public final ModelAndView indexAction(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexActions();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/actions"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingActions(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailIpActionToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/processes"}, method = RequestMethod.GET)
    public final ModelAndView indexProc(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexProcesses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/processes"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingProcesses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailIpProcToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/persons"}, method = RequestMethod.GET)
    public final ModelAndView indexPersons(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexPersonAddresses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/persons"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingPersons(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailPersonAddressesToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/patent-summaries"}, method = RequestMethod.GET)
    public final ModelAndView indexPatentSummaries(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexPatentSummary();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/patent-summaries"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingPatentSummaries(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailPatentSummaryToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/ipc-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexIpcClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexIpcClasses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/cpc-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexCpcClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexCpcClasses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/ipc-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingIpcClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailIpcClassesToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/nice-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexNiceClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexNiceClasses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/nice-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingNiceClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailNiceClassesToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/vienna-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexViennaClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexViennaClasses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }
    @RequestMapping(value = {"/mark-attachment-vienna-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexMarkAttachmentViennaClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexMarkAttachmentViennaClasses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/vienna-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingViennaClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailViennaClassesToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/locarno-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexPatentLocarnoClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexPatentLocarnoClasses();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }
    @RequestMapping(value = "/delete-all")
    public ModelAndView deleteAll(final ModelAndView mav) {
        indexService.deleteAll();

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/locarno-classes"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingLocarnoClasses(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailLocarnoClassesToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/file-relationships"}, method = RequestMethod.GET)
    public final ModelAndView indexPatentRelationships(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexFileRelationships();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/file-relationships"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingPatentRelationships(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailFileRelationshipsToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/docs"}, method = RequestMethod.GET)
    public final ModelAndView indexDocs(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexDocs();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/docs"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingDocs(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailIpDocsToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/userdocs"}, method = RequestMethod.GET)
    public final ModelAndView indexUserDocs(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexUserDocs();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/userdocs"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingUerdocs(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailIpUserdocsToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/userdoc-persons"}, method = RequestMethod.GET)
    public final ModelAndView indexUserDocPersons(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexService.indexUserDocPersons();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/missing/userdoc-persons"}, method = RequestMethod.GET)
    public final ModelAndView indexMissingUerdocPersons(final ModelAndView mav) {
        if (!IndexProgressMonitor.getCurrentIndexProgressMonitor().isRunning()) {
            indexQueueService.addFailIpUserdocPersonToIndexQueue();
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    @RequestMapping(value = {"/status"}, method = RequestMethod.GET, produces = "application/json")
    public final @ResponseBody Status getStatus() {
        IndexProgressMonitor progressMonitor = IndexProgressMonitor.getCurrentIndexProgressMonitor();
        Status res = new Status();
        if (progressMonitor != null) {
            res.setRunning(progressMonitor.isRunning());
            res.setGlobalError(progressMonitor.getGlobalError());
            progressMonitor.getEntityProgressMonitors()
                    .stream()
                    .map(r -> new Status.EntityStatus(r.getEntityType().getName(), r.getPercentsCompleted(), r.getDocumentsDoneCounter().longValue(), r.getTotalCounter().longValue(), r.getError(), r.isRunning()))
                    .forEach(res::addEntityStatus);
        }
        return res;
    }

    private void setup(ModelAndView mav) {
        mav.addObject("IndexCount", indexService);
        mav.addObject("indexQueue", indexQueueService.findAllNotIndexed());

        mav.addObject("dbCountMarks", markService.count());
        mav.addObject("dbCountPatents", patentService.count());
        mav.addObject("dbCountActions", actionService.count());
        mav.addObject("dbCountProcesses", processService.count());
        mav.addObject("dbCountPersonAddress", personService.count());
        mav.addObject("dbCountPatentSummaries", patentSummaryService.count());
        mav.addObject("dbCountIpcClasses", ipcClassesService.count());
        mav.addObject("dbCountCpcClasses", cpcClassesService.count());
        mav.addObject("dbCountNiceClasses", niceClassesService.count());
        mav.addObject("dbCountViennaClasses", viennaClassesService.count());
        mav.addObject("dbCountLocarnoClasses", patentLocarnoClassesService.count());
        mav.addObject("dbCountFileRelationships", fileRelationshipsService.count());
        mav.addObject("dbCountDocs", docService.count());
        mav.addObject("dbCountUserDocs", userdocService.count());
        mav.addObject("dbCountUserDocPersons", userdocPersonService.count());
        mav.addObject("dbCountMarkAttachmentViennaClasses", attachmentViennaClassesService.count());
    }

    @Getter
    @Setter
    public static class Status {
        private boolean running;
        private String globalError;
        private List<EntityStatus> entities = new ArrayList<>();
        @AllArgsConstructor
        @Getter
        @Setter
        public static class EntityStatus {
            private String entityType;
            private float percentsCompleted;
            private long current;
            private long totalCount;
            private String error;
            private boolean running;
        }
        public void addEntityStatus(EntityStatus s) {
            entities.add(s);
        }
    }

}
