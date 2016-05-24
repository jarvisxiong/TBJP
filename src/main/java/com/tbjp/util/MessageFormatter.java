package com.tbjp.util;

/**
 * Created with IntelliJ IDEA.
 * User: tubingbing
 * Date: 16-2-25
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public class MessageFormatter {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static final String arrayFormat(String messagePattern, Object[] argArray) {
        if (messagePattern == null) {
            return null;
        }

        if (argArray == null) {
            return messagePattern;
        }

        int i = 0;

        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 64);

        for (int L = 0; L < argArray.length; L++) {
            int j = messagePattern.indexOf("{}", i);

            if (j == -1) {
                if (i == 0) {
                    return messagePattern;
                }

                sbuf.append(messagePattern.substring(i, messagePattern.length()));
                return sbuf.toString();
            }

            if (isEscapedDelimeter(messagePattern, j)) {
                if (!isDoubleEscaped(messagePattern, j)) {
                    L--;
                    sbuf.append(messagePattern.substring(i, j - 1));
                    sbuf.append('{');
                    i = j + 1;
                } else {
                    sbuf.append(messagePattern.substring(i, j - 1));
                    deeplyAppendParameter(sbuf, argArray[L]);
                    i = j + 2;
                }
            } else {
                sbuf.append(messagePattern.substring(i, j));
                deeplyAppendParameter(sbuf, argArray[L]);
                i = j + 2;
            }

        }

        sbuf.append(messagePattern.substring(i, messagePattern.length()));
        return sbuf.toString();
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        if (potentialEscape == '\\') {
            return true;
        }
        return false;
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        if ((delimeterStartIndex >= 2) && (messagePattern.charAt(delimeterStartIndex - 2) == '\\')) {
            return true;
        }
        return false;
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o) {
        if (o == null) {
            sbuf.append("null");
            return;
        }

        safeObjectAppend(sbuf, o);
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
        }
    }
}
