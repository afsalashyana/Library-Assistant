package library.assistant.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author afsal
 */
public class DefaultExceptionHandler implements UncaughtExceptionHandler {

    private final static Logger LOGGER = LogManager.getLogger(DefaultExceptionHandler.class.getName());

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LOGGER.log(Level.ERROR, "Exception occurred {}", ex);
    }
}
