package net.getbang.core.exception;

import java.io.PrintWriter;

public class Exceptions {
    /**
     * 将ErrorStack转化为String.
     *
     * @param ex Throwable
     * @return {String}
     */
    public static String getStackTraceAsString(Throwable ex) {
        FastStringWriter stringWriter = new FastStringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
