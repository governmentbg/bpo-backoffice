package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.util.search.EntityIndexProgressMonitor;
import bg.duosoft.ipas.util.search.IndexProgressMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.search.exception.ErrorContext;
import org.hibernate.search.exception.impl.LogErrorHandler;

/**
 * User: ggeorgiev
 * Date: 17.03.2021
 * Time: 13:31
 */
@Slf4j
public class IpasHibernateSearchErrorHandler extends LogErrorHandler {
    @Override
    public void handle(ErrorContext context) {
        super.handle(context);//TODO:Tuk ima problem, tyj kato super.handle-a nakraq vika handleException, t.e. pri vlizane v tozi metod, vednyj shte se logne v entityProgressMonitor.error i vednyj v IndexProgressMonitor.globalError
        try {
            IndexProgressMonitor progressMonitor = IndexProgressMonitor.getCurrentIndexProgressMonitor();
            String entityClassName = context.getOperationAtFault().getEntityType().getName();
            Class<?> cls = Class.forName(entityClassName);
            EntityIndexProgressMonitor entityProgressMonitor = progressMonitor.getEntityProgressMonitor(cls);
            if (entityProgressMonitor != null) {
                entityProgressMonitor.setError(ExceptionUtils.getFullStackTrace(context.getThrowable()));
            }
        } catch (Exception e) {
            log.error("Error trying to log hibernate search exception ", e);
        }
    }
    public void handleException(String errorMsg, Throwable exception) {
        super.handleException(errorMsg, exception);
        IndexProgressMonitor progressMonitor = IndexProgressMonitor.getCurrentIndexProgressMonitor();
        progressMonitor.addGlobalError(ExceptionUtils.getFullStackTrace(exception));
    }
}
