package com.tbjp.util;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class RootLogger extends Logger {
    public RootLogger(Level level) {
        super("root");
        setLevel(level);
    }

    public final Level getChainedLevel() {
        return this.level;
    }

    public final void setLevel(Level level) {
        if (level == null)
            LogLog.error("You have tried to set a null level to root.", new Throwable());
        else
            this.level = level;
    }
}
