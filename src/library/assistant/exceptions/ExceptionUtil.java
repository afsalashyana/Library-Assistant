package library.assistant.exceptions;

import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author afsal
 */
public class ExceptionUtil {

    public static void init() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));
    }

    private final static Logger LOGGER = LogManager.getLogger(ExceptionUtil.class.getName());

    public static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            @Override
            public void print(final String string) {
                LOGGER.info(string);
            }

            @Override
            public void println(final String string) {
                LOGGER.info(string);
            }
        };
    }
}
