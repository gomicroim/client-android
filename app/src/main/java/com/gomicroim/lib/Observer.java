package com.gomicroim.lib;

/**
 * 通知观察器
 *
 * @param <T>
 */
public interface Observer<T> {
    /**
     * 事件发生
     *
     * @param t
     */
    void onEvent(T t);
}
