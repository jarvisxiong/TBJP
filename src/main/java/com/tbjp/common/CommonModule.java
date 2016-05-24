package com.tbjp.common;

import com.tbjp.jvm.JvmInfoPickerFactory;
import com.tbjp.util.CacheUtil;
import com.tbjp.util.CustomLogger;
import com.tbjp.util.LogFormatter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class CommonModule {
    private static final String INSTANCE_ID = JvmInfoPickerFactory.create("Local").getJvmInstanceCode();
    private static final String LOG_TEMPLATE = new StringBuilder().append("{\"time\":\"{}\",\"host\":\"").append(CacheUtil.HOST_NAME).append("\"").append(",\"ip\":\"").append(CacheUtil.HOST_IP).append("\"").append(",\"iCode\":\"").append(INSTANCE_ID).append("\"").append(",\"type\":\"{}\"").append(",\"data\":{}").append("}").toString();

    public static void log(String type, String data) {
        CustomLogger.CommonLogger.info(LogFormatter.format(LOG_TEMPLATE, new Object[]{CacheUtil.getNowTime(), type, data}));
    }

    public static void log(String type, Map<String, String> data) {
        StringBuilder sb = new StringBuilder("{");

        for (Map.Entry entry : data.entrySet()) {
            sb.append("\"").append((String) entry.getKey()).append("\"").append(":").append("\"").append((String) entry.getValue()).append("\"").append(",");
        }

        sb.deleteCharAt(sb.length() - 1).append("}");

        CustomLogger.CommonLogger.info(LogFormatter.format(LOG_TEMPLATE, new Object[]{CacheUtil.getNowTime(), type, sb.toString()}));
    }
}
