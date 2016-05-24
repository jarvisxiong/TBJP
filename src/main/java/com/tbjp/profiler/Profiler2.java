package com.tbjp.profiler;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class Profiler2 {
    public static CallerInfo registerInfo(String key) {
        return registerInfo(key, true, false);
    }

    public static CallerInfo registerInfo(String key, boolean enableHeartbeat, boolean enableTP) {
        CallerInfo callerInfo = null;
        if ((key != null) && (!"".equals(key.trim()))) {
            callerInfo = Profiler.start(key.trim(), enableHeartbeat, enableTP);
        }
        return callerInfo;
    }

    public static CallerInfo registerInfo(String key, String appName, boolean enableHeartbeat, boolean enableTP) {
        CallerInfo callerInfo = null;
        if ((key != null) && (!"".equals(key.trim())) && (appName != null) && (!"".equals(appName.trim()))) {
            callerInfo = Profiler.start(key.trim(), appName.trim(), enableHeartbeat, enableTP);
        }
        return callerInfo;
    }

    public static void registerInfoEnd(CallerInfo callerInfo) {
        Profiler.end(callerInfo);
    }

    public static void businessAlarm(String key, String detail) {
        if ((key != null) && (!"".equals(key.trim())) && (detail != null) && (!"".equals(detail.trim())))
            Profiler.registerBusiness(key.trim(), 0, 0, detail.trim());
    }

    public static void businessAlarm(String key, String detail, String rtxList, String mailList, String smsList) {
        if ((key != null) && (!"".equals(key.trim())) && (detail != null) && (!"".equals(detail.trim())))
            Profiler.registerBusiness(key.trim(), 5, 0, detail.trim(), rtxList, mailList, smsList);
    }

    public static void InitHeartBeats(String key) {
        if ((key != null) && (!"".equals(key.trim())))
            Profiler.scopeAlive(key.trim());
    }

    public static void functionError(CallerInfo callerInfo) {
        Profiler.error(callerInfo);
    }

    public static void valueAccumulate(String key, Number bValue) {
        if ((key != null) && (!"".equals(key.trim())) && (bValue != null))
            Profiler.registerBizData(key.trim(), 1, bValue);
    }

    public static void countAccumulate(String key) {
        if ((key != null) && (!"".equals(key.trim())))
            Profiler.registerBizData(key.trim(), 2, Integer.valueOf(1));
    }

    public static void sourceDataByNum(String key, Map<String, Number> dataMap) {
        if ((key != null) && (!"".equals(key.trim())) && (dataMap != null) && (!dataMap.isEmpty()))
            Profiler.registerBizData(key.trim(), 3, dataMap);
    }

    public static void sourceDataByStr(String key, Map<String, String> dataMap) {
        if ((key != null) && (!"".equals(key.trim())) && (dataMap != null) && (!dataMap.isEmpty()))
            Profiler.registerBizData(key.trim(), 3, dataMap);
    }

    public static void registerJVMInfo(String key) {
        if ((key != null) && (!"".equals(key.trim())))
            Profiler.registerJvmData(key.trim());
    }

    public static void bizNode(String nodeID, Map<String, String> data) {
        try {
            if ((nodeID != null) && (!"".equals(nodeID = nodeID.trim())) && (data != null) && (!data.isEmpty())) {
                Profiler.bizNode(nodeID, data);
            }
        } catch (Throwable t) {
        }
    }

    public static void bizNode(String nodeID, String data) {
        try {
            if ((nodeID != null) && (!"".equals(nodeID = nodeID.trim())) && (data != null) && (!"".equals(data = data.trim()))) {
                Profiler.bizNode(nodeID, data);
            }
        } catch (Throwable t) {
        }
    }

    public static void log(String type, String data) {
        try {
            if ((type != null) && (!"".equals(type = type.trim())) && (data != null) && (!"".equals(data = data.trim()))) {
                Profiler.log(type, data);
            }
        } catch (Throwable t) {
        }
    }

    public static void log(String type, Map<String, String> data) {
        try {
            if ((type != null) && (!"".equals(type = type.trim())) && (data != null) && (!data.isEmpty())) {
                Profiler.log(type, data);
            }
        } catch (Throwable t) {
        }
    }
}
