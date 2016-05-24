package com.tbjp.profiler;

import com.tbjp.util.CacheUtil;
import com.tbjp.util.CustomLogger;
import com.tbjp.util.LogFormatter;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public class CallerInfo {
    public static final int STATE_TRUE = 0;
    public static final int STATE_FALSE = 1;
    private static final String TP_LOG_TEMPLATE = "{\"time\":\"{}\",\"key\":\"{}\",\"hostname\":\"" + CacheUtil.HOST_NAME + "\",\"processState\":" + "\"{}\",\"elapsedTime\":\"{}\"}";

    private static final String AUTO_TP_LOG_TEMPLATE = "{\"time\":\"{}\",\"key\":\"{}\",\"appName\":\"{}\",\"hostname\":\"" + CacheUtil.HOST_NAME + "\",\"processState\":" + "\"{}\",\"elapsedTime\":\"{}\"}";
    private long startTime;
    private String key;
    private String appName;
    private int processState;
    private long elapsedTime;

    public CallerInfo(String key) {
        this.key = key;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = -1L;
        this.processState = 0;
    }

    public CallerInfo(String key, String appName) {
        this.key = key;
        this.appName = appName;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = -1L;
        this.processState = 0;
    }

    public int getProcessState() {
        return this.processState;
    }

    public void error() {
        this.processState = 1;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public String getKey() {
        return this.key;
    }

    public String getAppName() {
        return this.appName;
    }

    public long getElapsedTime() {
        if (this.elapsedTime == -1L) {
            this.elapsedTime = (System.currentTimeMillis() - this.startTime);
        }
        return this.elapsedTime;
    }

    public void stop() {
        if ((null == this.appName) || ("".equals(this.appName)))
            CustomLogger.TpLogger.info(toString());
        else
            CustomLogger.TpLogger.info(packagLogInfo());
    }

    public String toString() {
        return LogFormatter.format(TP_LOG_TEMPLATE, new Object[]{CacheUtil.getNowTime(), this.key, Integer.valueOf(this.processState), Long.valueOf(getElapsedTime())});
    }

    private String packagLogInfo() {
        return LogFormatter.format(AUTO_TP_LOG_TEMPLATE, new Object[]{CacheUtil.getNowTime(), this.key, this.appName, Integer.valueOf(this.processState), Long.valueOf(getElapsedTime())});
    }
}
