package uk.wils.backpressure.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by vagrant on 11/03/17.
 */
public class ExceptionHelperTest {

    @Test
    public void testGetExceptionText() {
        Exception exception = new Exception("Exception: Hello World");
        String text = ExceptionHelper.getExceptionText(null);
        assertEquals("Unexpected text", "", text);
        text = ExceptionHelper.getExceptionText(exception);
        assertEquals("Unexpected line 1", "java.lang.Exception: Exception: Hello World", text.split("\n")[0]);
//        assertEquals("Unexpected body", "java.lang.Exception: Exception: Hello World", text);
        assertEquals("Unexpected line count", 25, text.split("\n").length);
        IOException ioException = new IOException("IOExc - hello", exception);
        text = ExceptionHelper.getExceptionText(ioException);
        assertEquals("Unexpected line 1", "java.io.IOException: IOExc - hello", text.split("\n")[0]);
        assertEquals("Unexpected line count", 53, text.split("\n").length);
//        assertEquals("Unexpected body", "java.lang.Exception: Exception: Hello World", text);
        assertEquals("Unexpected line 25", "Caused by: java.lang.Exception: Exception: Hello World", text.split("\n")[25]);
    }

}