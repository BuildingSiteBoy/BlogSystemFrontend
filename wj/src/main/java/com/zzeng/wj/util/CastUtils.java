package com.zzeng.wj.util;

import java.util.ArrayList;
import java.util.List;

public class CastUtils {
    public static <T> List<T> objectConvertToList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                //cast()方法将此Object强制转换为该Class或此Class对象表示的接口
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}
