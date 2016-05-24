package com.tbjp.profiler;

import com.tbjp.common.*;
import com.tbjp.util.CacheUtil;
import com.tbjp.util.CustomLogger;
import com.tbjp.util.TPCounter;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
public final class Profiler {
    private static final TPCounter tpCounter = TPCounter.getInstance();

    public static CallerInfo start(String key, boolean enableHeart, boolean enableTP) {
        CallerInfo callerInfo = null;
        try {
            if (enableTP) {
                callerInfo = new CallerInfo(key);
            }
            if (enableHeart) {
                if (!CacheUtil.FUNC_HB.containsKey(key)) {
                    synchronized (CacheUtil.FUNC_HB) {
                        if (!CacheUtil.FUNC_HB.containsKey(key)) {
                            CacheUtil.FUNC_HB.put(key, Long.valueOf(System.currentTimeMillis()));
                            CustomLogger.AliveLogger.info("{\"key\":\"" + key + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"}");
                        }
                    }
                } else {
                    Long hbPoint = (Long) CacheUtil.FUNC_HB.get(key);
                    if (System.currentTimeMillis() - hbPoint.longValue() >= 20000L) {
                        synchronized (CacheUtil.FUNC_HB) {
                            if (System.currentTimeMillis() - ((Long) CacheUtil.FUNC_HB.get(key)).longValue() >= 20000L) {
                                CacheUtil.FUNC_HB.put(key, Long.valueOf(System.currentTimeMillis()));
                                CustomLogger.AliveLogger.info("{\"key\":\"" + key + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"}");
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
        }
        return callerInfo;
    }

    public static CallerInfo start(String key, String appName, boolean enableHeart, boolean enableTP) {
        CallerInfo callerInfo = null;
        try {
            if (enableTP) {
                callerInfo = new CallerInfo(key, appName);
            }

            if (enableHeart) {
                if (!CacheUtil.FUNC_HB.containsKey(key)) {
                    synchronized (CacheUtil.FUNC_HB) {
                        if (!CacheUtil.FUNC_HB.containsKey(key)) {
                            CacheUtil.FUNC_HB.put(key, Long.valueOf(System.currentTimeMillis()));
                            CustomLogger.AliveLogger.info("{\"key\":\"" + key + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"}");
                        }
                    }
                } else {
                    Long hbPoint = (Long) CacheUtil.FUNC_HB.get(key);
                    if (System.currentTimeMillis() - hbPoint.longValue() >= 20000L) {
                        synchronized (CacheUtil.FUNC_HB) {
                            if (System.currentTimeMillis() - ((Long) CacheUtil.FUNC_HB.get(key)).longValue() >= 20000L) {
                                CacheUtil.FUNC_HB.put(key, Long.valueOf(System.currentTimeMillis()));
                                CustomLogger.AliveLogger.info("{\"key\":\"" + key + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"}");
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
        }
        return callerInfo;
    }


    public static void end(CallerInfo callerInfo) {
        try {
            if (callerInfo != null) {
                if (callerInfo.getProcessState() == 1) {
                    callerInfo.stop();
                } else {
                    long elapsedTime = callerInfo.getElapsedTime();
                    /*if (elapsedTime >= 400L)
                        callerInfo.stop();
                    else*/
                        tpCounter.count(callerInfo, elapsedTime);
                }
            }
        } catch (Throwable t) {
        }
    }

    public static synchronized void scopeAlive(String key) {
        try {
            if (!CacheUtil.SYSTEM_HEART_INIT.booleanValue()) {
                Timer timer = new Timer("UMP-AliveThread", true);
                timer.scheduleAtFixedRate(new AliveModule(key), 1000L, 20000L);
                CacheUtil.SYSTEM_HEART_INIT = Boolean.valueOf(true);
            }
        } catch (Throwable t) {
        }
    }

    public static void registerBusiness(String key, int type, int value, String detail) {
        try {
            BusinessModule.businessHandle(key, type, value, detail);
        } catch (Throwable t) {
        }
    }

    public static void registerBusiness(String key, int type, int value, String detail, String rtxList, String mailList, String smsList) {
        try {
            BusinessModule.businessHandle(key, type, value, detail, rtxList, mailList, smsList);
        } catch (Throwable t) {
        }
    }

    public static void error(CallerInfo callerInfo) {
        try {
            if (callerInfo != null)
                callerInfo.error();
        } catch (Throwable t) {
        }
    }

    public static void registerBizData(String key, int type, Number value) {
        try {
            BizModule.bizHandle(key, type, value);
        } catch (Throwable t) {
        }
    }

    public static void registerBizData(String key, int type, Map<String, ?> dataMap) {
        try {
            BizModule.bizHandle(key, type, dataMap);
        } catch (Throwable t) {
        }
    }

    public static synchronized void registerJvmData(final String key) {
        try {
            if (!CacheUtil.JVM_MONITOR_INIT.booleanValue()) {
                Timer timer = new Timer("UMP-CollectJvmInfoThread", true);
                timer.scheduleAtFixedRate(new JvmModule(key), 1000L, 10000L);

                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        JvmModule.jvmHandle(key);
                    }
                }, 0L, 14400000L);
                CacheUtil.JVM_MONITOR_INIT = Boolean.valueOf(true);
            }
        } catch (Throwable t) {
        }
    }

    public static void bizNode(String nodeID, Map<String, String> data) {
        BizModule.bizNode(nodeID, data);
    }

    public static void bizNode(String nodeID, String data) {
        BizModule.bizNode(nodeID, data);
    }

    public static void log(String type, String data) {
        CommonModule.log(type, data);
    }

    public static void log(String type, Map<String, String> data) {
        CommonModule.log(type, data);
    }
}
