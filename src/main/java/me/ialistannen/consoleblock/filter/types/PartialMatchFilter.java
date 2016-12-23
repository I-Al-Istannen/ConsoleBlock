package me.ialistannen.consoleblock.filter.types;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.function.BiFunction;

import org.apache.logging.log4j.Level;
import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.consoleblock.ConsoleBlock;
import me.ialistannen.consoleblock.filter.FilterResult;
import me.ialistannen.consoleblock.filter.MessageFilter;
import me.ialistannen.consoleblock.util.ConfigUtils;

/**
 * Matches things that are contained, start with or end with the pattern
 */
public class PartialMatchFilter extends AbstractMessageFilter {

    private static final String TYPE_NAME = "PartialMatch";

    private ContainMode mode;

    /**
     * An empty filter
     */
    public PartialMatchFilter() {
        super(Collections.emptyList(), TYPE_NAME, TYPE_NAME, "", false);
    }

    /**
     * @param levels The levels
     * @param name The name of the filter. Set by the user
     * @param pattern The pattern to use
     * @param caseSensitive Whether this filter is case sensitive
     * @param mode The {@link ContainMode} to use
     */
    public PartialMatchFilter(Collection<Level> levels, String name, String pattern, boolean caseSensitive,
                              ContainMode mode) {
        super(levels, TYPE_NAME, name, pattern, caseSensitive);

        this.mode = mode;
    }

    /**
     * @param message The message to filter
     * @param level The {@link Level} of the message. You do not need to check if it is inside {@link #getLevels()}
     *
     * @return The result
     */
    @Override
    protected FilterResult doFilter(String message, Level level) {
        if (!isCaseSensitive()) {
            String modifiedPattern = getPattern().toLowerCase();
            String modifiedMessage = message.toLowerCase();
            return getFilterResult(mode.test(modifiedMessage, modifiedPattern));
        }
        return getFilterResult(mode.test(message, getPattern()));
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
        ContainMode mode = ContainMode.valueOf(
                ConfigUtils.ensureAndGetString(section, "contain_mode")
        );
        return new PartialMatchFilter(levels, name, pattern, caseSensitive, mode);
    }

    public enum ContainMode {

        /**
         * The String must contain the pattern
         */
        CONTAINS(String::contains),
        /**
         * The String must end with the pattern
         */
        ENDS_WITH(String::endsWith),
        /**
         * The String must start with the pattern
         */
        STARTS_WITH(String::startsWith);

        private BiFunction<String, String, Boolean> predicate;

        ContainMode(BiFunction<String, String, Boolean> predicate) {
            this.predicate = predicate;
        }

        /**
         * @param input The String to check
         * @param pattern The Pattern to use
         *
         * @return True if the String matched the predicate
         */
        public boolean test(String input, String pattern) {
            return predicate.apply(input, pattern);
        }

        /**
         * @param string The String to parse
         *
         * @return The ContainMode, {@link #CONTAINS} as default
         */
        public static ContainMode fromString(String string) {
            String name = string.toUpperCase(Locale.ROOT).replace(" ", "_");
            try {
                return ContainMode.valueOf(name);
            } catch (IllegalArgumentException e) {
                ConsoleBlock.getInstance().getLogger().warning(
                        String.format(
                                Locale.ENGLISH,
                                "Contain mode '%s' not found",
                                string
                        )
                );
                return CONTAINS;
            }
        }
    }
}
