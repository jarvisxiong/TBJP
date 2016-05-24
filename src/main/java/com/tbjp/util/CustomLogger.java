package com.tbjp.util;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class CustomLogger {
    public static final CustomLogger TpLogger = new CustomLogger(CustomLogFactory.getLogger("tpLogger"));
    public static final CustomLogger AliveLogger = new CustomLogger(CustomLogFactory.getLogger("aliveLogger"));
    public static final CustomLogger BusinessLogger = new CustomLogger(CustomLogFactory.getLogger("businessLogger"));
    public static final CustomLogger BizLogger = new CustomLogger(CustomLogFactory.getLogger("bizLogger"));
    public static final CustomLogger JVMLogger = new CustomLogger(CustomLogFactory.getLogger("jvmLogger"));
    public static final CustomLogger CommonLogger = new CustomLogger(CustomLogFactory.getLogger("commonLogger"));
    private Logger logger;

    public CustomLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String message) {
        this.logger.info(message);
    }

    public Logger getLogger() {
        return this.logger;
    }
}
