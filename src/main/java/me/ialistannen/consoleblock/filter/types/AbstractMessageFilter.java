package me.ialistannen.consoleblock.filter.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.Level;
import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.consoleblock.ConsoleBlock;
import me.ialistannen.consoleblock.filter.FilterResult;
import me.ialistannen.consoleblock.filter.MessageFilter;
import me.ialistannen.consoleblock.util.ConfigUtils;

/**
 * A base class for {@link MessageFilter}s, to ease the creation
 */
public abstract class AbstractMessageFilter implements MessageFilter {

    private final Collection<Level> levels;
    private final String filterType, pattern, name;
    private final boolean caseSensitive;

    /**
     * @param levels The levels
     * @param filterType The filterType of the filter
     * @param name The name of this filter. Set by the user
     * @param pattern The pattern to use
     * @param caseSensitive Whether this filter is case sensitive
     */
    public AbstractMessageFilter(Collection<Level> levels, String filterType, String name, String pattern,
                                 boolean caseSensitive) {
        this.levels = new HashSet<>(levels);
        this.filterType = filterType;
        this.pattern = pattern;
        this.caseSensitive = caseSensitive;
        this.name = name;
    }

    @Override
    public MessageFilter cloneWithSettings(ConfigurationSection section) {
        Collection<Level> levels = new ArrayList<>();
        for (String levelString : section.getStringList("levels")) {
            try {
                Level parsedLevel = Level.toLevel(levelString);
                levels.add(parsedLevel);
            } catch (IllegalArgumentException e) {
                ConsoleBlock.getInstance().getLogger().warning("Illegal level: '" + levelString + "'");
            }
        }

        if (levels.isEmpty()) {
            ConsoleBlock.getInstance().getLogger().warning("No log level specified, adding all");
            levels.add(Level.ALL);
        }

        return cloneWithSettings(section, levels,
                ConfigUtils.ensureAndGetString(section, "name"),
                ConfigUtils.ensureAndGetString(section, "pattern"),
                ConfigUtils.ensureAndGetBoolean(section, "case_sensitive")
        );
    }

    /**
     * @param section The section to read from
     * @param levels The levels to operate on
     * @param name The name of this filter. Set by the user
     * @param pattern The pattern to use
     * @param caseSensitive Whether this filter is case sensitive
     *
     * @return A new {@link MessageFilter}, constructed from the {@link ConfigurationSection}
     */
    protected abstract MessageFilter cloneWithSettings(ConfigurationSection section, Collection<Level> levels,
                                                       String name, String pattern, boolean caseSensitive);


    /**
     * @param message The message to filter
     * @param level The {@link Level}
     *
     * @return The Result
     */
    @Override
    public FilterResult filter(String message, Level level) {
        if (levels.contains(level) || levels.contains(Level.ALL)) {
            return doFilter(message, level);
        }
        return FilterResult.PASS;
    }

    /**
     * @param message The message to filter
     * @param level The {@link Level}
     *
     * @return The Result
     */
    protected abstract FilterResult doFilter(String message, Level level);

    /**
     * @param filterCondition The condition for it to filter
     *
     * @return The {@link FilterResult}. Filter if {@code filterCondition} was true
     */
    protected FilterResult getFilterResult(boolean filterCondition) {
        return filterCondition ? FilterResult.FILTER : FilterResult.PASS;
    }

    @Override
    public String getFilterType() {
        return filterType;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return All {@link Level}s this filters works on
     */
    public Collection<Level> getLevels() {
        return levels;
    }

    /**
     * @return The pattern for this {@link MessageFilter}
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @return True if the filter is case sensitive
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }
}
