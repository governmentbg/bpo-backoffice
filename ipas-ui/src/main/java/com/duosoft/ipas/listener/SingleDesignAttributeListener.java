package com.duosoft.ipas.listener;

import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.List;

@Slf4j
@WebListener
public class SingleDesignAttributeListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        if (isSingleDesignAttribute(attributeName)) {
            List singleDesigns = (List) event.getValue();
            log.debug("Singe designs attribute is added! Key:".concat(attributeName).concat("and size:").concat(CollectionUtils.isEmpty(singleDesigns) ? "no elements" : String.valueOf(singleDesigns.size())));
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        if (isSingleDesignAttribute(attributeName)) {
            List singleDesigns = (List) event.getValue();
            log.debug("Singe designs attribute is removed! Key:".concat(attributeName).concat("and size:").concat(CollectionUtils.isEmpty(singleDesigns) ? "no elements" : String.valueOf(singleDesigns.size())));
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        if (isSingleDesignAttribute(attributeName)) {
            List singleDesigns = (List) event.getValue();
            log.debug("Singe designs attribute is replaced! Key:".concat(attributeName).concat("and size:").concat(CollectionUtils.isEmpty(singleDesigns) ? "no elements" : String.valueOf(singleDesigns.size())));
        }
    }

    private boolean isSingleDesignAttribute(String attributeName) {
        return attributeName.contains(PatentSessionObject.SESSION_SINGLE_DESIGNS.concat(DefaultValue.DASH));
    }
}
