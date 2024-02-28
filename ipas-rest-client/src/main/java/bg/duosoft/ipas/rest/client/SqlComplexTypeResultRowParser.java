package bg.duosoft.ipas.rest.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 16:36
 */
public class SqlComplexTypeResultRowParser<T> implements SqlResultRowParser<T>{
    protected SqlResultRowValueParser resultRowValueParser;
    protected Class<T> objectType;

    public SqlComplexTypeResultRowParser(Map<String, Object> row, Class<T> objectType) {
        resultRowValueParser = new SqlResultRowValueParser(row);
        this.objectType = objectType;
    }

    public T createObject() {
        try {
            T instance = objectType.getDeclaredConstructor().newInstance();
            Field[] fields = getUsableFields(objectType);
            for (Field f : fields) {
                Class<?> type = f.getType();
                f.setAccessible(true);
                f.set(instance, resultRowValueParser.getValue(f.getName(), type));
            }
            return instance;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field[] getUsableFields(Class<?> c) {
        List<Field> f = getUsableFieldsArrReversed(c);
        Collections.reverse(f);
        return (Field[])f.toArray(new Field[0]);
    }

    private static List<Field> getUsableFieldsArrReversed(Class<?> startClass) {
        List<Field> currentClassFields = new ArrayList();
        Field[] var2 = startClass.getDeclaredFields();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field f = var2[var4];
            if ((f.getModifiers() & 8) != 8) {
                currentClassFields.add(f);
            }
        }

        Collections.reverse(currentClassFields);
        Class<?> parentClass = startClass.getSuperclass();
        if (parentClass != null) {
            List<Field> parentClassFields = getUsableFieldsArrReversed(parentClass);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

}
