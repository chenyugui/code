package com.taichuan.code.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author gui
 * @date 2020-02-26
 */
public class BeanCopier {
    private Class sourceClass;
    private Class targetClass;

    private BeanCopier(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    public static BeanCopier create(Class sourceClass, Class targetClass) {
        if (targetClass == null) {
            throw new IllegalArgumentException("targetClass is null");
        }
        return new BeanCopier(sourceClass, targetClass);
    }

    public Object copy(Object source) {
        if (source == null) {
            return null;
        }
        Gson gson = GsonUtil.getGson();
        String json = gson.toJson(source);
        return gson.fromJson(json, targetClass);
    }

    public <T> List<T> copyList(List sourceList, Type type) {
        Gson gson = GsonUtil.getGson();
        String json = gson.toJson(sourceList);
        return GsonUtil.getGson().fromJson(json, type);
    }
}
