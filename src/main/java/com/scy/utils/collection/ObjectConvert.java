package com.scy.utils.collection;

/**
 * 将对象根据自定义规则转换成新的对象
 *
 * @param <P> 转换前的对象类型
 * @param <R> 转换后的对象类型
 */
public interface ObjectConvert<P, R> {
    /**
     * 转换
     *
     * @param obj 转换前的对象
     * @return 转换后的对象
     */
    R convert(P obj);
}
