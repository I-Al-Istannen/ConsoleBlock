package me.ialistannen.consoleblock.filter.types;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.consoleblock.filter.FilterResult;
import me.ialistannen.consoleblock.filter.MessageFilter;
import me.ialistannen.consoleblock.util.ConfigUtils;

/**
 * A regex filter
 */
public class RegexFilter extends AbstractMessageFilter {

    private static final String TYPE_NAME = "RegEx";
    private final Pattern PATTERN;
    private final boolean partialMatch;

    /**
     * An empty filter
     */
    public RegexFilter() {
        this(Collections.emptyList(), TYPE_NAME, "", false, true);
    }

    /**
     * @param levels The levels
     * @param name The name of the filter. Set by the user
     * @param pattern The pattern to use
     * @param caseSensitive Whether this filter is case sensitive
     * @param partialMatch Whether a partial match is okay ({@link Matcher#find()})
     */
    public RegexFilter(Collection<Level> levels, String name, String pattern, boolean caseSensitive, boolean
            partialMatch) {
        super(levels, TYPE_NAME, name, pattern, caseSensitive);

        this.partialMatch = partialMatch;

        if (caseSensitive) {
            PATTERN = Pattern.compile(pattern);
        }
        else {
            PATTERN = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        }
    }

    /**
     * @param message The message to filter
     * @param level The {@link Level} of the message. You do not need to check if it is inside {@link #getLevels()}
     *
     * @return The result
     */
    @Override
    protected FilterResult doFilter(String message, Level level) {
        Matcher matcher = PATTERN.matcher(message);
        if (partialMatch) {
            return getFilterResult(matcher.find());
        }
        else {
            return getFilterResult(matcher.matches());
        }
    }

    /**
     * @param section The section to read from
     * @param levels The levels to operate on
     * @param pattern The pattern to use
     * @param caseSensitive Whether this filter is case sensitive
     *
     * @return A new {@link MessageFilter}, constructed from the {@link ConfigurationSection}
     */
    @Override
    protected MessageFilter cloneWithSettings(ConfigurationSection section, Collection<Level> levels,
                                              String name, String pattern, boolean caseSensitive) {

        boolean partialMatch = ConfigUtils.ensureAndGetBoolean(section, "partial_match");

        return new RegexFilter(levels, name, pattern, caseSensitive, partialMatch);
    }
}
