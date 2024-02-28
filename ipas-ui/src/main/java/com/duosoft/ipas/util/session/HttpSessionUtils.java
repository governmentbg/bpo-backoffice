package com.duosoft.ipas.util.session;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import bg.duosoft.ipas.util.DefaultValue;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionObjects;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class HttpSessionUtils {
    public static String SESSION_OBJECT_DELIMITER = "-";

    public static <T> T getSessionAttribute(String key, String identifier, HttpServletRequest request) {
        return (T) request.getSession().getAttribute(createSessionObjectName(key, identifier));
    }

    public static <T> T getSessionAttribute(String key, HttpServletRequest request) {
        return (T) request.getSession().getAttribute(key);
    }

    public static void removeSessionAttributes(HttpServletRequest request, String identifier, String... keys) {
        Arrays.stream(keys).forEach(s -> request.getSession().removeAttribute(createSessionObjectName(s, identifier)));
    }

    public static <T> void setSessionAttribute(Map<String, T> map, String identifier, HttpServletRequest request) {
        Set<String> keySet = map.keySet();
        keySet.forEach(key -> request.getSession().setAttribute(createSessionObjectName(key, identifier), map.get(key)));
    }

    public static <T> void setSessionAttribute(String key, String identifier, T value, HttpServletRequest request) {
        request.getSession().setAttribute(createSessionObjectName(key, identifier), value);
    }

    public static <T> void setSessionAttribute(String key, T value, HttpServletRequest request) {
        request.getSession().setAttribute(key, value);
    }

    public static <T extends Serializable> void setAndCloneSessionAttribute(String key, String identifier, T value, HttpServletRequest request) {
        request.getSession().setAttribute(createSessionObjectName(key, identifier), SerializationUtils.clone(value));
    }

    public static <T extends Serializable> void setAndCloneSessionAttribute(String key, String identifier, List<T> value, HttpServletRequest request) {
        List<T> list = new ArrayList<>();
        if (Objects.nonNull(value)) {
            value.forEach(t -> {
                list.add((T) SerializationUtils.clone(t));
            });
        }
        request.getSession().setAttribute(createSessionObjectName(key, identifier), list);
    }

    public static String generateIdentifier(String key) {
        return key + SESSION_OBJECT_DELIMITER + UUID.randomUUID().toString();
    }

    public static String generateIdentifier(CFileId cFileId) {
        String filingNumber = CoreUtils.createFilingNumber(cFileId, false);
        return filingNumber + SESSION_OBJECT_DELIMITER + UUID.randomUUID().toString();
    }

    public static String createSessionObjectName(String sessionObjectName, String identifier) {
        return sessionObjectName + SESSION_OBJECT_DELIMITER + identifier;
    }

    public static String getFilingNumberFromSessionObject(String fullSessionIdentifier) {
        int startIndex = fullSessionIdentifier.indexOf(SESSION_OBJECT_DELIMITER);
        String withoutPrefix = fullSessionIdentifier.substring(startIndex + 1);
        int endIndex = withoutPrefix.indexOf(SESSION_OBJECT_DELIMITER);
        return withoutPrefix.substring(0, endIndex);
    }

    public static String getUniqueIdentifierFromSessionObject(String sessionObject) {
        int startIndex = sessionObject.indexOf(SESSION_OBJECT_DELIMITER);
        String withoutPrefix = sessionObject.substring(startIndex + 1);
        int endIndex = withoutPrefix.indexOf(SESSION_OBJECT_DELIMITER);
        return withoutPrefix.substring(endIndex + 1);
    }

    public static String getFilingNumberAndUniqueIdentifierFromSessionObject(String sessionObject) {
        int startIndex = sessionObject.indexOf(SESSION_OBJECT_DELIMITER);
        return sessionObject.substring(startIndex + 1);
    }

    public static void removeSessionAttribute(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
    }

    public static List<String> getMainSessionObjects(HttpServletRequest request) {
        List<String> sessionObjects = new ArrayList<>();

        HttpSession session = request.getSession();
        Enumeration<String> attributeNames = session.getAttributeNames();
        Iterator<String> stringIterator = attributeNames.asIterator();
        while (stringIterator.hasNext()) {
            String sessionObject = stringIterator.next();
            if (sessionObject.startsWith(MarkSessionObjects.SESSION_MARK + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
                    || sessionObject.startsWith(PatentSessionObject.SESSION_PATENT + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
                    || sessionObject.startsWith(UserdocSessionObjects.SESSION_USERDOC + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
                    || sessionObject.startsWith(OffidocSessionObjects.SESSION_OFFIDOC + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
            )
                sessionObjects.add(sessionObject);
        }
        return sessionObjects;
    }


    public static String findExistingMainSessionObjectIdentifier(HttpServletRequest request, String filingNumber) {
        List<String> mainSessionObjects = getMainSessionObjects(request);
        if (CollectionUtils.isEmpty(mainSessionObjects))
            return null;

        List<String> foundedIdentifiers = mainSessionObjects.stream()
                .filter(s -> s.contains(HttpSessionUtils.SESSION_OBJECT_DELIMITER + filingNumber + HttpSessionUtils.SESSION_OBJECT_DELIMITER))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(foundedIdentifiers))
            return null;
        else if (foundedIdentifiers.size() > DefaultValue.ONE_RESULT_SIZE)
            throw new RuntimeException("Cannot exists more than one session objects with same filing number ! ");
        else
            return foundedIdentifiers.get(DefaultValue.FIRST_RESULT);
    }

    public static int getMainSessionObjectsCount(HttpServletRequest request) {
        int result = 0;
        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        Iterator<String> stringIterator = attributeNames.asIterator();
        while (stringIterator.hasNext()) {
            String sessionObjectIdentifier = stringIterator.next();
            if (sessionObjectIdentifier.startsWith(MarkSessionObjects.SESSION_MARK + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
                    || sessionObjectIdentifier.startsWith(PatentSessionObject.SESSION_PATENT + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
                    || sessionObjectIdentifier.startsWith(UserdocSessionObjects.SESSION_USERDOC + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
                    || sessionObjectIdentifier.startsWith(OffidocSessionObjects.SESSION_OFFIDOC + HttpSessionUtils.SESSION_OBJECT_DELIMITER)
            )
                result += 1;
        }
        return result;
    }

    public static String findMainObjectFullSessionIdentifier(HttpServletRequest request, String sessionObjectWithoutPrefix) {
        List<String> mainSessionObjects = getMainSessionObjects(request);
        for (String sessionObject : mainSessionObjects) {
            int startIndex = sessionObject.indexOf(SESSION_OBJECT_DELIMITER);
            String substring = sessionObject.substring(startIndex + 1);
            if (substring.equalsIgnoreCase(sessionObjectWithoutPrefix))
                return sessionObject;
        }
        throw new SessionObjectNotFoundException("Cannot find full session identifier ! " + sessionObjectWithoutPrefix);
    }

}
