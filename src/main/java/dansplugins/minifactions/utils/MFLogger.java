package dansplugins.minifactions.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import dansplugins.minifactions.MiniFactions;

/**
 * @author Daniel McCoy Stephenson
 * @since April 17th, 2022
 */
public class MFLogger {
    private static MFLogger instance;
    private static Logger logger = MiniFactions.getInstance().getLogger();

    private MFLogger() {
        
    }

    public static MFLogger getInstance() {
        if (instance == null) {
            instance = new MFLogger();
        }
        return instance;
    }

    public void print(String message) {
        logger.info("[MF] " + message);
    }

    public void debug(String message) {
        if (MiniFactions.getInstance().isDebugEnabled()) {
            logger.info("[MF] [DEBUG]" + message);
        }
    }

    public void error(String message) {
        logger.log(Level.SEVERE, "[MF] [ERROR] " + message);
    }
}