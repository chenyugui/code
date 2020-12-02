package com.taichuan.code.utils;

import java.util.List;

/**
 * 列表工具
 * <p>
 * Created by gui on 2018/8/7.
 */

public final class ListUtil {

    /**
     * 判断列表是否非空
     *
     * @param list 列表
     * @return 列表是否非空
     */
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 将新的元素添加到列表中
     *
     * @param list     列表
     * @param newItems 新的元素
     * @param <T>      列表中元素的类型
     * @return 是否添加成功
     */
    public static <T> boolean addAll(List<T> list, List<T> newItems) {
        if (list == null || isNullOrEmpty(newItems)) {
            return false;
        }
        return list.addAll(newItems);
    }
}
