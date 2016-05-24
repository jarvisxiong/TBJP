package com.tbjp.util;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public class LogFormatter {
    public static String format(String messagePattern, Object[] args) {
        return MessageFormatter.arrayFormat(messagePattern, args);
    }
}
