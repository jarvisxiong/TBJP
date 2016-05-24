package com.tbjp.util;

import com.tbjp.profiler.CallerInfo;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class TPCounter {
    public static final long MAX_TP_COUNT_ELAPSED_TIME = 400L;
    private static final long COUNT_TP_PERIOD = 5000L;
    private static final long WRITE_TP_LOG_DELAY = 1000L;
    private static final String KEY_STORE_KEY_SPLIT_STR = "###";
    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final TPCounter counter = new TPCounter();
    private ConcurrentHashMap<String, ConcurrentHashMultiSet<Integer>> tpCountMap;
    private static final String TP_LOG_TEMPLATE = "{\"time\":\"{}\",\"key\":\"{}\",\"hostname\":\"" + CacheUtil.HOST_NAME + "\",\"processState\":" + "\"" + 0 + "\",\"elapsedTime\":\"{}\",\"count\":\"{}\"}";

    private static final String AUTO_TP_LOG_TEMPLATE = "{\"time\":\"{}\",\"key\":\"{}\",\"appName\":\"{}\",\"hostname\":\"" + CacheUtil.HOST_NAME + "\",\"processState\":" + "\"" + 0 + "\",\"elapsedTime\":\"{}\",\"count\":\"{}\"}";

    private TPCounter() {
        this.tpCountMap = new ConcurrentHashMap();

        long lastTimePoint = new Date().getTime() / 5000L * 5000L;
        Date firstWriteTime = new Date(lastTimePoint + 5000L);
        Timer writeTPLogTimer = new Timer("UMP-WriteTPLogThread", true);
        writeTPLogTimer.scheduleAtFixedRate(new WriteTPLogTask(), firstWriteTime, 5000L);
    }

    public static TPCounter getInstance() {
        return counter;
    }

    public void count(CallerInfo callerInfo, long elapsedTime) {
        String countMapKey = getCountMapKey(callerInfo);

        ConcurrentHashMultiSet elapsedTimeCounter = (ConcurrentHashMultiSet) this.tpCountMap.get(countMapKey);

        if (elapsedTimeCounter == null) {
            ConcurrentHashMultiSet newElapsedTimeCounter = new ConcurrentHashMultiSet();
            elapsedTimeCounter = (ConcurrentHashMultiSet) this.tpCountMap.putIfAbsent(countMapKey, newElapsedTimeCounter);
            if (elapsedTimeCounter == null) {
                elapsedTimeCounter = newElapsedTimeCounter;
            }

        }

        elapsedTimeCounter.add(Integer.valueOf((int) elapsedTime));
    }

    private String getCountMapKey(CallerInfo callerInfo) {
        if ((null == callerInfo.getAppName()) || ("".equals(callerInfo.getAppName()))) {
            return callerInfo.getKey() + "###" + System.currentTimeMillis() / 5000L * 5000L;
        }
        return callerInfo.getKey() + "###" + callerInfo.getAppName() + "###" + System.currentTimeMillis() / 5000L * 5000L;
    }

    private class WriteTPLogTask extends TimerTask {
        private WriteTPLogTask() {
        }

        public void run() {
            try {
                Map writeCountMap = TPCounter.this.tpCountMap;
                TPCounter.this.tpCountMap = new ConcurrentHashMap();

                writeTPLog(writeCountMap);
            } catch (Throwable ex) {
            }
        }

        private void writeTPLog(Map<String, ConcurrentHashMultiSet<Integer>> writeCountMap)
                throws InterruptedException {
            StringBuilder logs;
            if (writeCountMap != null) {
                Thread.sleep(1000L);
                logs = new StringBuilder(1024);
                for (Map.Entry entry : writeCountMap.entrySet()) {
                    String[] keyTime = ((String) entry.getKey()).split("###");

                    if ((keyTime != null) && (keyTime.length == 2)) {
                        String key = keyTime[0].trim();
                        String time = CacheUtil.changeLongToDate(Long.valueOf(keyTime[1].trim()).longValue());

                        ConcurrentHashMultiSet<Integer> elapsedTimeCounter = (ConcurrentHashMultiSet) entry.getValue();
                        boolean needSetLineSep = false;

                        for (Integer elapsedTime : elapsedTimeCounter.elementSet()) {
                            if (needSetLineSep)
                                logs.append(TPCounter.LINE_SEP);
                            else {
                                needSetLineSep = true;
                            }

                            Integer count = Integer.valueOf(elapsedTimeCounter.count(elapsedTime));
                            String log = LogFormatter.format(TPCounter.TP_LOG_TEMPLATE, new Object[]{time, key, elapsedTime, count});
                            logs.append(log);
                        }

                        int length = logs.length();
                        if (length > 0) {
                            CustomLogger.TpLogger.info(logs.toString());
                            logs.setLength(0);
                        }
                    } else if ((keyTime != null) && (keyTime.length == 3)) {
                        String key = keyTime[0].trim();
                        String appName = keyTime[1].trim();
                        String time = CacheUtil.changeLongToDate(Long.valueOf(keyTime[2].trim()).longValue());

                        ConcurrentHashMultiSet<Integer> elapsedTimeCounter = (ConcurrentHashMultiSet) entry.getValue();
                        boolean needSetLineSep = false;

                        for (Integer elapsedTime : elapsedTimeCounter.elementSet()) {
                            if (needSetLineSep)
                                logs.append(TPCounter.LINE_SEP);
                            else {
                                needSetLineSep = true;
                            }

                            Integer count = Integer.valueOf(elapsedTimeCounter.count(elapsedTime));
                            String log = LogFormatter.format(TPCounter.AUTO_TP_LOG_TEMPLATE, new Object[]{time, key, appName, elapsedTime, count});
                            logs.append(log);
                        }

                        int length = logs.length();
                        if (length > 0) {
                            CustomLogger.TpLogger.info(logs.toString());
                            logs.setLength(0);
                        }
                    }
                }
            }
        }
    }
}
