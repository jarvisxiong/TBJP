package com.tbjp.common;

import com.tbjp.util.CacheUtil;
import com.tbjp.util.CustomLogger;

import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class AliveModule extends TimerTask {
    private String key;

    public AliveModule(String key) {
        this.key = key;
    }

    public void run() {
        try {
            CustomLogger.AliveLogger.info("{\"key\":\"" + this.key + "\"" + ",\"hostname\":" + "\"" + CacheUtil.HOST_NAME + "\"" + ",\"time\":" + "\"" + CacheUtil.getNowTime() + "\"}");
        } catch (Exception e) {
        }
    }
}
