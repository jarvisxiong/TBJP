package com.tbjp.common;

import com.tbjp.jvm.JvmInfoPicker;
import com.tbjp.jvm.JvmInfoPickerFactory;
import com.tbjp.util.CacheUtil;
import com.tbjp.util.CustomLogger;

import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class JvmModule extends TimerTask {
    private String key;
    private static JvmInfoPicker localJvm = JvmInfoPickerFactory.create("Local");
    private static String instanceCode = localJvm.getJvmInstanceCode();
    private static String logType = "JVM";

    public JvmModule(String key) {
        this.key = key;
    }

    public void run() {
        try {
            CustomLogger.JVMLogger.info("{\"logtype\":\"" + logType + "\"" + ",\"key\":" + "\"" + this.key + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"" + ",\"datatype\":" + "\"" + "2" + "\"" + ",\"instancecode\":" + "\"" + instanceCode + "\"" + ",\"userdata\":" + localJvm.pickJvmRumtimeInfo() + "}");
        } catch (Throwable e) {
        }
    }

    public static void jvmHandle(String jvmKey) {
        try {
            CustomLogger.JVMLogger.info("{\"logtype\":\"" + logType + "\"" + ",\"key\":" + "\"" + jvmKey + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"" + ",\"datatype\":" + "\"" + "1" + "\"" + ",\"instancecode\":" + "\"" + instanceCode + "\"" + ",\"userdata\":" + localJvm.pickJvmEnvironmentInfo() + "}");
        } catch (Throwable e) {
        }
    }
}
