package bg.duosoft.ipas.core.model.logging;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 13:57
 */
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class CLogChangeElement implements Serializable {
    private CLogChangeElement parent;
    private List<String> key;
    private String stringValue;

    private List<CLogChangeElement> children = new ArrayList<>();
    private Map<String, List<CLogChangeElement>> childrenMap = new LinkedHashMap<>();
    public CLogChangeElement(CLogChangeElement parent) {
        this.parent = parent;
    }

    public String getKey() {
        return key == null || key.size() == 0 ? null : key.get(key.size() - 1);
    }
    public String getFullKey() {
        return key == null ? null : key.stream().collect(Collectors.joining(">"));
    }

    public void addChild(CLogChangeElement child) {
        childrenMap.computeIfAbsent(child.getKey(), (k) -> new ArrayList<>());
        childrenMap.get(child.getKey()).add(child);
        children.add(child);
    }

    public boolean removeChild(CLogChangeElement child) {
        childrenMap.get(child.getKey()).remove(child);
        return children.remove(child);

    }

    public void setParent(CLogChangeElement parent) {
        this.parent = parent;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public CLogChangeElement getParent() {
        return parent;
    }

    public String getStringValue() {
        return stringValue;
    }

    public List<CLogChangeElement> getAllChildren() {
        return Collections.unmodifiableList(children);
    }
    public boolean hasChildren() {
        return children.size() > 0;
    }
    public List<CLogChangeElement> getChildrenByKey(String key) {
        return childrenMap.get(key);
    }
}
