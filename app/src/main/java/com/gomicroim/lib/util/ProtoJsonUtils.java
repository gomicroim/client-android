package com.gomicroim.lib.util;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;

/**
 * protobuf 和 json 互转
 * 特别主要：
 * <ul>
 *  <li>该实现无法处理含有Any类型字段的Message</li>
 *  <li>enum类型数据会转化为enum的字符串名</li>
 *  <li>bytes会转化为utf8编码的字符串</li>
 * </ul>
 *
 * @author Yang Guanrong
 * @see <a href="https://segmentfault.com/a/1190000020270374">Protobuf与Json的相互转化</a>
 */
public class ProtoJsonUtils {
    /**
     * 转成json，不支持any字段
     *
     * @param sourceMessage: 源字符串
     * @return json结果
     */
    public static String toJson(Message sourceMessage)
            throws IOException {
        return JsonFormat.printer().print(sourceMessage);
    }

    /**
     * json转换为protobuf
     *
     * @param targetBuilder: 目标proto结构
     * @param json: 源json字符串
     * @return proto实例
     * @throws IOException 异常
     */
    public static Message toProtoBean(Message.Builder targetBuilder, String json) throws IOException {
        JsonFormat.parser().merge(json, targetBuilder);
        return targetBuilder.build();
    }
}
