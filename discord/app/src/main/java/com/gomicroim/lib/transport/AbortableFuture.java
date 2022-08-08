package com.gomicroim.lib.transport;

/**
 * 可中断操作的接口
 */
public interface AbortableFuture<T> extends InvocationFuture {
    /**
     * 中断操作。
     * 耗时长的操作可供用户中途取消。比如登录，上传下载文件等。
     *
     * @return 中断是否成功。如果中断时操作已经完成，会返回false
     */
    boolean abort();
}
