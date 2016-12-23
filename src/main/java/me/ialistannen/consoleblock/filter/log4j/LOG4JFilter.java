package me.ialistannen.consoleblock.filter.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import me.ialistannen.consoleblock.ConsoleBlock;
import me.ialistannen.consoleblock.filter.FilterManager;
import me.ialistannen.consoleblock.filter.FilterResult;
import me.ialistannen.consoleblock.filter.MessageFilter;

/**
 * A Filter for 'Log4J'
 */
public class LOG4JFilter implements Filter {

    /**
     * @param message The message to filter
     * @param level The Level to filter
     *
     * @return The final result
     */
    private Result getFinalResult(String message, Level level) {
        if (message == null || level == null) {
            return Result.NEUTRAL;
        }
        FilterManager filterManager = ConsoleBlock.getInstance().getFilterManager();

        for (MessageFilter messageFilter : filterManager.getActiveFilters()) {
            if (messageFilter.filter(message, level) == FilterResult.FILTER) {
                return Result.DENY;
            }
        }

        return Result.NEUTRAL;
    }

    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s,
                         Object... objects) {
        return getFinalResult(s, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o,
                         Throwable throwable) {
        return o == null ? Result.NEUTRAL : getFinalResult(o.toString(), level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message,
                         Throwable throwable) {
        return getFinalResult(message.getFormattedMessage(), level);
    }

    @Override
    public Result filter(LogEvent logEvent) {
        return getFinalResult(logEvent.getMessage().getFormattedMessage(), logEvent.getLevel());
    }
}
