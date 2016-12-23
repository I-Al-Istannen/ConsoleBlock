package me.ialistannen.consoleblock.filter;

import org.apache.logging.log4j.Level;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A filter
 */
public interface MessageFilter {

    /**
     * @param message The message to filter
     * @param level The {@link Level}
     *
     * @return The Result
     */
    FilterResult filter(String message, Level level);

    /**
     * @return The name of this filter to be used in configs
     */
    String getFilterType();

    /**
     * @return The name of the filter. Set by the user
     */
    String getName();

    /**
     * @param section The section to read from
     *
     * @return A new {@link MessageFilter}, constructed from the {@link ConfigurationSection}
     */
    MessageFilter cloneWithSettings(ConfigurationSection section);
}
