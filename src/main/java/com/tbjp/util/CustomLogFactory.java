package com.tbjp.util;

import com.tbjp.common.UpdateModule;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggerRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class CustomLogFactory {
    private static LoggerRepository h = new Hierarchy(new RootLogger(Level.INFO));
    private static final String DEFAULTPATH = "/export/home/tomcat/UMP-Monitor";
    private static String MaxFileSize = "50MB";
    private static int MaxBackupIndex = 3;
    private static String logPath = null;

    private static String TIMESTAMP = getTimeStamp();
    private static int PID = getPid();
    private static int RANDOM_CODE = getRandomCode();

    private static long CHECK_FILE_REMOVED_PERIOD = 2000L;

    private static int tpLoggerFileRemovedCount = 0;
    private static int aliveLoggerFileRemovedCount = 0;
    private static int businessLoggerFileRemovedCount = 0;
    private static int bizLoggerFileRemovedCount = 0;
    private static int jvmLoggerFileRemovedCount = 0;
    private static int commonLoggerFileRemovedCount = 0;

    private static int getPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

            String name = runtime.getName();
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Throwable e) {
        }
        return new Random().nextInt(50000) + 9900000;
    }

    private static int getRandomCode() {
        return new Random().nextInt(1000000);
    }

    private static String getTimeStamp() {
        return new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
    }

    public static Logger getLogger(String loggerName) {
        return h.getLogger(loggerName);
    }

    private static Properties InitLog4jProperties(String path) {
        Properties properties = new Properties();

        String tpLogFile = createFileName("tp.log");
        String aliveLogFile = createFileName("alive.log");
        String businessLogFile = createFileName("business.log");
        String bizLogFile = createFileName("biz.log");
        String jvmLogFile = createFileName("jvm.log");
        String commonLogFile = createFileName("common.log");

        setProperties(properties, "tpLogger", "A1", tpLogFile);
        setProperties(properties, "aliveLogger", "A2", aliveLogFile);
        setProperties(properties, "businessLogger", "A3", businessLogFile);
        setProperties(properties, "bizLogger", "A4", bizLogFile);
        setProperties(properties, "jvmLogger", "A5", jvmLogFile);
        setProperties(properties, "commonLogger", "A6", commonLogFile);

        return properties;
    }

    private static void setProperties(Properties properties, String loggerName, String appenderName, String fileName) {
        properties.setProperty(String.format("log4j.logger.%s", new Object[]{loggerName}), String.format("INFO,%s", new Object[]{appenderName}));
        properties.setProperty(String.format("log4j.appender.%s", new Object[]{appenderName}), "com.jd.ump.log4j.RollingFileAppender");
        properties.setProperty(String.format("log4j.appender.%s.File", new Object[]{appenderName}), fileName);
        properties.setProperty(String.format("log4j.appender.%s.MaxFileSize", new Object[]{appenderName}), MaxFileSize);
        properties.setProperty(String.format("log4j.appender.%s.MaxBackupIndex", new Object[]{appenderName}), String.valueOf(MaxBackupIndex));
        properties.setProperty(String.format("log4j.appender.%s.layout", new Object[]{appenderName}), "com.jd.ump.log4j.SimpleLayout");
        properties.setProperty(String.format("log4j.appender.%s.encoding", new Object[]{appenderName}), "UTF-8");
    }

    private static String createFileName(String name) {
        return logPath + TIMESTAMP + "_" + PID + "_" + RANDOM_CODE + "_" + name;
    }

    static {
        Properties conf = new Properties();
        Properties props = null;
        InputStream is = null;
        try {
            is = CacheUtil.class.getResourceAsStream("/config.properties");
            if (is != null) {
                conf.load(is);
                logPath = conf.getProperty("jiankonglogPath", "/export/home/tomcat/UMP-Monitor");
                if (logPath.equals(""))
                    logPath = "/export/home/tomcat/UMP-Monitor";
            } else {
                logPath = "/export/home/tomcat/UMP-Monitor";
            }

            logPath = logPath + File.separator + "logs" + File.separator;
            File ump_root_path = new File(logPath);
            if ((ump_root_path.exists()) && (ump_root_path.isDirectory())) {
                props = InitLog4jProperties(logPath);
            } else {
                ump_root_path.mkdir();
                props = InitLog4jProperties(logPath);
            }

            new PropertyConfigurator().doConfigure(props, h);

            Timer timer = new Timer("UMP-ProfilerFileUpdateThread", true);
            timer.scheduleAtFixedRate(new UpdateModule(), 43200000L, 43200000L);

            Timer checkFileRemovedTimer = new Timer("UMP-CheckFileRemovedThread", true);
            checkFileRemovedTimer.schedule(new CheckFileRemovedTask(), CHECK_FILE_REMOVED_PERIOD, CHECK_FILE_REMOVED_PERIOD);
        } catch (Throwable e) {
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Throwable ex) {
                }
        }
    }

    static class CheckFileRemovedTask extends TimerTask {
        public void run() {
            try {
                checkFileRemoved(CustomLogger.TpLogger.getLogger(), "tp.log", "A1");
                checkFileRemoved(CustomLogger.AliveLogger.getLogger(), "alive.log", "A2");
                checkFileRemoved(CustomLogger.BusinessLogger.getLogger(), "business.log", "A3");
                checkFileRemoved(CustomLogger.BizLogger.getLogger(), "biz.log", "A4");
                checkFileRemoved(CustomLogger.JVMLogger.getLogger(), "jvm.log", "A5");
                checkFileRemoved(CustomLogger.CommonLogger.getLogger(), "common.log", "A6");
            } catch (Throwable e) {
            }
        }

        private void checkFileRemoved(Logger logger, String logName, String appenderName)
                throws IOException {
            RollingFileAppender appender = (RollingFileAppender) logger.getAppender(appenderName);

            if (appender != null) {
                String oldFile = appender.getFile();
                File file = new File(oldFile);
                if (file.exists()) {
                    setCount2Zero(logName);
                } else {
                    incrCount(logName);
                }

                if (getCount(logName) >= 2) {
                    String newFile = CustomLogFactory.createFileName(logName);

                    SimpleLayout layout = new SimpleLayout();

                    RollingFileAppender newAppender = new RollingFileAppender(layout, newFile);
                    newAppender.setName(appenderName);
                    newAppender.setMaxFileSize(CustomLogFactory.MaxFileSize);
                    newAppender.setMaxBackupIndex(CustomLogFactory.MaxBackupIndex);
                    newAppender.setEncoding("UTF-8");

                    logger.removeAppender(appenderName);
                    logger.addAppender(newAppender);

                    setCount2Zero(logName);
                }
            }
        }

        private int getCount(String logName) {
            if ("tp.log".equals(logName)) {
                return CustomLogFactory.tpLoggerFileRemovedCount;
            }

            if ("alive.log".equals(logName)) {
                return CustomLogFactory.aliveLoggerFileRemovedCount;
            }

            if ("business.log".equals(logName)) {
                return CustomLogFactory.businessLoggerFileRemovedCount;
            }

            if ("biz.log".equals(logName)) {
                return CustomLogFactory.bizLoggerFileRemovedCount;
            }

            if ("jvm.log".equals(logName)) {
                return CustomLogFactory.jvmLoggerFileRemovedCount;
            }

            if ("common.log".equals(logName)) {
                return CustomLogFactory.commonLoggerFileRemovedCount;
            }

            return 0;
        }

        private void setCount2Zero(String logName) {
            if ("tp.log".equals(logName)) {
                CustomLogFactory.tpLoggerFileRemovedCount=0;
            }

            if ("alive.log".equals(logName)) {
                CustomLogFactory.aliveLoggerFileRemovedCount=0;
            }

            if ("business.log".equals(logName)) {
                CustomLogFactory.businessLoggerFileRemovedCount=0;
            }

            if ("biz.log".equals(logName)) {
                CustomLogFactory.bizLoggerFileRemovedCount=0;
            }

            if ("jvm.log".equals(logName)) {
                CustomLogFactory.jvmLoggerFileRemovedCount=0;
            }

            if ("common.log".equals(logName))
                CustomLogFactory.commonLoggerFileRemovedCount=0;
        }

        private void incrCount(String logName) {
            if ("tp.log".equals(logName)) {
                CustomLogFactory.tpLoggerFileRemovedCount++;
            }

            if ("alive.log".equals(logName)) {
                CustomLogFactory.aliveLoggerFileRemovedCount++;
            }

            if ("business.log".equals(logName)) {
                CustomLogFactory.businessLoggerFileRemovedCount++;
            }

            if ("biz.log".equals(logName)) {
                CustomLogFactory.bizLoggerFileRemovedCount++;
            }

            if ("jvm.log".equals(logName)) {
                CustomLogFactory.jvmLoggerFileRemovedCount++;
            }

            if ("common.log".equals(logName))
                CustomLogFactory.commonLoggerFileRemovedCount++;
        }
    }
}
