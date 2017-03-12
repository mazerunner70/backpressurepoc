package uk.wils.backpressure.util;

import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * Created by vagrant on 11/03/17.
 */
public class ExceptionHelper {
    public static void printExceptionText(Throwable throwable, PrintWriter printWriter) {
        if (throwable != null) {
            throwable.printStackTrace(printWriter);
            printExceptionText(throwable.getCause(), printWriter);
        }
    }

    public static String getExceptionText(Throwable throwable) {
        String result = "";
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        PrintWriter printWriter = new PrintWriter(charArrayWriter);
        printExceptionText(throwable, printWriter);
        result = charArrayWriter.toString();
        return result;
    }
}
