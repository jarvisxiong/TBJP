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
public class BizModule {
    private static final String LOG_TYPE = "BIZ";
    private static final String PROC_TYPE = "4";
    private static final String INSTANCE_ID = JvmInfoPickerFactory.create("Local").getJvmInstanceCode();

    private static final String BIZ_LOG_TEMPLATE1 = new StringBuilder().append("{\"bTime\":\"{}\",\"logtype\":\"BIZ\",\"bKey\":\"{}\",\"bHost\":\"").append(CacheUtil.HOST_NAME).append("\"").append(",\"type\":\"{}\"").append(",\"{}\":\"{}\"").append("}").toString();

    private static final String BIZ_LOG_TEMPLATE2 = new StringBuilder().append("{\"bTime\":\"{}\",\"logtype\":\"BIZ\",\"bKey\":\"{}\",\"bHost\":\"").append(CacheUtil.HOST_NAME).append("\"").append(",\"type\":\"{}\"").append("{}").append("}").toString();

    private static final String PROC_LOG_TEMPLATE = new StringBuilder().append("{\"bTime\":\"{}\",\"logtype\":\"BIZ\",\"bKey\":\"{}\",\"bHost\":\"").append(CacheUtil.HOST_NAME).append("\"").append(",\"type\":\"").append("4").append("\"").append(",\"iCode\":\"").append(INSTANCE_ID).append("\"").append(",\"data\":{}").append("}").toString();

    public static void bizHandle(String key, int type, Number value) {
        try {
            CustomLogger.BizLogger.info(LogFormatter.format(BIZ_LOG_TEMPLATE1, new Object[]{CacheUtil.getNowTime(), key, Integer.valueOf(type), type == 1 ? "bValue" : "bCount", value}));
        } catch (Exception e) {
        }
    }

    public static void bizHandle(String key, int type, Map<String, ?> dataMap) {
        try {
            StringBuilder sb = new StringBuilder(32);

            for (Map.Entry entry : dataMap.entrySet()) {
                sb.append(",").append("\"").append((String) entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"");
            }

            Object[] args = {CacheUtil.getNowTime(), key, Integer.valueOf(type), sb.toString()};

            CustomLogger.BizLogger.info(LogFormatter.format(BIZ_LOG_TEMPLATE2, args));
        } catch (Exception e) {
        }
    }

    public static void bizNode(String nodeID, String data) {
        CustomLogger.BizLogger.info(LogFormatter.format(PROC_LOG_TEMPLATE, new Object[]{CacheUtil.getNowTime(), nodeID, data}));
    }

    public static void bizNode(String nodeID, Map<String, String> data) {
        StringBuilder sb = new StringBuilder("{");

        for (Map.Entry entry : data.entrySet()) {
            sb.append("\"").append((String) entry.getKey()).append("\"").append(":").append("\"").append((String) entry.getValue()).append("\"").append(",");
        }

        int length = sb.length();

        if (length > 1) {
            sb.deleteCharAt(length - 1);
        }

        sb.append("}");

        Object[] args = {CacheUtil.getNowTime(), nodeID, sb.toString()};

        CustomLogger.BizLogger.info(LogFormatter.format(PROC_LOG_TEMPLATE, args));
    }
}
