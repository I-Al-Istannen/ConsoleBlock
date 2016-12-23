package me.ialistannen.consoleblock.filter.types;

import java.util.Collection;
import java.util.Collections;

import org.apache.logging.log4j.Level;
import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.consoleblock.filter.FilterResult;
import me.ialistannen.consoleblock.filter.MessageFilter;

/**
 * Filters if it matches exactly
 */
public class ExactMatchFilter extends AbstractMessageFilter {

    private static final String TYPE_NAME = "ExactMatch";

    /**
     * Creates an empty, default filter
     */
    public ExactMatchFilter() {
        super(Collections.emptyList(), TYPE_NAME, TYPE_NAME, "", false);
    }

    /**
     * @param levels The levels
     * @param name The name of the filter. Set by the user
     * @param pattern The pattern for the filter
     * @param caseSensitive Whether the case matters
     */
    public ExactMatchFilter(Collection<Level> levels, String name, String pattern, boolean caseSensitive) {
        super(levels, TYPE_NAME, name, pattern, caseSensitive);
    }

    /**
     * @param message The message to filter
     * @param level The {@link Level} of the message. You do not need to check if it is inside {@link #getLevels()}
     *
     * @return The result
     */
    @Override
    protected FilterResult doFilter(String message, Level level) {
        if (isCaseSensitive()) {
            return getFilterResult(getPattern().equals(message));
        }
        else {
            return getFilterResult(getPattern().equalsIgnoreCase(message));
        }
    }

    @Override
    protected MessageFilter cloneWithSettings(ConfigurationSection section, Collection<Level> levels,
                                              String name, String pattern, boolean caseSensitive) {
        return new ExactMatchFilter(levels, name, pattern, caseSensitive);
    }
}
