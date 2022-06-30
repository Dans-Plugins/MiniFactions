package dansplugins.minifactions.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import dansplugins.minifactions.MiniFactions;

/**
 * @author Daniel McCoy Stephenson
 * @since April 17th, 2022
 */
public class MFLogger {
    private final MiniFactions miniFactions;

    private static Logger logger;

    public MFLogger(MiniFactions miniFactions) {
        this.miniFactions = miniFactions;
        logger = miniFactions.getLogger();
    }

    public void print(String message) {
        logger.info("[MF] " + message);
    }

    public void debug(String message) {
        if (miniFactions.isDebugEnabled()) {
            logger.info("[MF] [DEBUG]" + message);
        }
    }

    public void error(String message) {
        logger.log(Level.SEVERE, "[MF] [ERROR] " + message);
    }
}