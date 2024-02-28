package com.duosoft.ipas.mapper.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.*;

public class RestObjectMapperTestUtils {
    private static Random random = new Random();

    public static <T> T createAndFill(Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(clazz)) {
                continue;
            }
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                if (isListOrSet(field.getType())) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    if (genericClass.getName().equals(clazz.getName())) {
                        continue;
                    }
                }
                field.setAccessible(true);
                Object value = getRandomValueForField(field, clazz);
                field.set(instance, value);
            }
        }
        return instance;
    }

    private static Object getRandomValueForField(Field field, Class<?> clazz) throws Exception {
        Class<?> type = field.getType();

        if (type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[random.nextInt(enumValues.length)];
        } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return random.nextInt();
        } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return random.nextLong();
        } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return random.nextDouble();
        } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return random.nextFloat();
        } else if (type.equals(String.class)) {
            return UUID.randomUUID().toString();
        } else if (type.equals(Date.class)) {
            return new Date();
        } else if (type.equals(BigInteger.class)) {
            return BigInteger.valueOf(random.nextInt());
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return true;
        } else if (type.equals(byte[].class) || type.equals(Byte[].class)) {
            return "Some text".getBytes();
        } else if (type.equals(List.class)) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            int size = random.nextInt(10);
            if (size > 0) {
                List list = new ArrayList();
                for (int i = 0; i < size; i++) {
                    Object genericObject = createAndFill(genericClass);
                    list.add(genericObject);
                }
                return list;
            } else {
                return null;
            }
        } else if (type.equals(Set.class)) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            int size = random.nextInt(10);
            if (size > 0) {
                Set set = new HashSet();
                for (int i = 0; i < size; i++) {
                    Object genericObject = createAndFill(genericClass);
                    set.add(genericObject);
                }
                return set;
            } else {
                return null;
            }
        }
        return createAndFill(type);
    }

    private static boolean isListOrSet(Class<?> type) {
        return type.equals(List.class) || type.equals(Set.class);
    }


}