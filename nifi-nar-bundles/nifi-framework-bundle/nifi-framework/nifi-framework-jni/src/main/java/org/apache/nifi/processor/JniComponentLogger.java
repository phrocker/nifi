package org.apache.nifi.processor;

import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JniComponentLogger implements ComponentLog {

    private static final Logger logger = LoggerFactory
            .getLogger(JniComponentLogger.class);

    public JniComponentLogger(){

    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public void warn(String msg, Object[] os) {
        logger.warn(msg, os);
    }

    @Override
    public void warn(String msg, Object[] os, Throwable t) {
        logger.warn(msg, os);
        logger.warn("", t);
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public void trace(String msg, Object[] os) {
        logger.trace(msg, os);
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String msg, Object[] os, Throwable t) {
        logger.trace(msg, os);
        logger.trace("", t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public void info(String msg, Object[] os) {
        logger.info(msg, os);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);

    }

    @Override
    public void info(String msg, Object[] os, Throwable t) {
        logger.trace(msg, os);
        logger.trace("", t);

    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    @Override
    public void error(String msg, Object[] os) {
        logger.error(msg, os);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String msg, Object[] os, Throwable t) {
        logger.error(msg, os);
        logger.error("", t);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public void debug(String msg, Object[] os) {
        logger.debug(msg, os);
    }

    @Override
    public void debug(String msg, Object[] os, Throwable t) {
        logger.debug(msg, os);
        logger.debug("", t);
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void log(LogLevel level, String msg, Throwable t) {
        switch (level) {
            case DEBUG:
                debug(msg, t);
                break;
            case ERROR:
            case FATAL:
                error(msg, t);
                break;
            case INFO:
                info(msg, t);
                break;
            case TRACE:
                trace(msg, t);
                break;
            case WARN:
                warn(msg, t);
                break;
        }
    }

    @Override
    public void log(LogLevel level, String msg, Object[] os) {
        switch (level) {
            case DEBUG:
                debug(msg, os);
                break;
            case ERROR:
            case FATAL:
                error(msg, os);
                break;
            case INFO:
                info(msg, os);
                break;
            case TRACE:
                trace(msg, os);
                break;
            case WARN:
                warn(msg, os);
                break;
        }
    }

    @Override
    public void log(LogLevel level, String msg) {
        switch (level) {
            case DEBUG:
                debug(msg);
                break;
            case ERROR:
            case FATAL:
                error(msg);
                break;
            case INFO:
                info(msg);
                break;
            case TRACE:
                trace(msg);
                break;
            case WARN:
                warn(msg);
                break;
        }
    }

    @Override
    public void log(LogLevel level, String msg, Object[] os, Throwable t) {
        switch (level) {
            case DEBUG:
                debug(msg, os, t);
                break;
            case ERROR:
            case FATAL:
                error(msg, os, t);
                break;
            case INFO:
                info(msg, os, t);
                break;
            case TRACE:
                trace(msg, os, t);
                break;
            case WARN:
                warn(msg, os, t);
                break;
        }
    }
}
