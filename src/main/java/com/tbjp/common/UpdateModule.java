package com.tbjp.common;

import com.tbjp.util.CustomLogger;

import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class UpdateModule extends TimerTask {
    private static final String alive = "{\"key\":\"UMPAutoGenerateAliveKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"time\":\"19900101010101000\"}";
    private static final String biz = "{\"bTime\":\"19900101010101000\",\"logtype\":\"BIZ\",\"bKey\":\"UMPAutoGenerateBizKeyDoNotUseThisKey\",\"bHost\":\"UMPTESTHOST\",\"type\":\"1\",\"bValue\":\"0\"}";
    private static final String business = "{\"time\":\"19900101010101000\",\"key\":\"UMPAutoGenerateBusinessKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"type\":\"0\",\"value\":\"0\",\"detail\":\"\"}";
    private static final String tp = "{\"time\":\"19900101010101000\",\"key\":\"UMPAutoGenerateTpKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"processState\":\"0\",\"elapsedTime\":\"0\"}";
    private static final String common = "{\"time\":\"19900101010101000\",\"host\":\"UMPTESTHOST\",\"ip\":\"0.0.0.0\",\"iCode\":\"000000\",\"type\":\"TESTTYPE\",\"data\":{}}";

    public void run() {
        try {
            CustomLogger.AliveLogger.info("{\"key\":\"UMPAutoGenerateAliveKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"time\":\"19900101010101000\"}");
            CustomLogger.BizLogger.info("{\"bTime\":\"19900101010101000\",\"logtype\":\"BIZ\",\"bKey\":\"UMPAutoGenerateBizKeyDoNotUseThisKey\",\"bHost\":\"UMPTESTHOST\",\"type\":\"1\",\"bValue\":\"0\"}");
            CustomLogger.BusinessLogger.info("{\"time\":\"19900101010101000\",\"key\":\"UMPAutoGenerateBusinessKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"type\":\"0\",\"value\":\"0\",\"detail\":\"\"}");
            CustomLogger.TpLogger.info("{\"time\":\"19900101010101000\",\"key\":\"UMPAutoGenerateTpKeyDoNotUseThisKey\",\"hostname\":\"UMPTESTHOST\",\"processState\":\"0\",\"elapsedTime\":\"0\"}");
            CustomLogger.CommonLogger.info("{\"time\":\"19900101010101000\",\"host\":\"UMPTESTHOST\",\"ip\":\"0.0.0.0\",\"iCode\":\"000000\",\"type\":\"TESTTYPE\",\"data\":{}}");
        } catch (Throwable e) {
        }
    }
}