package me.ialistannen.consoleblock.util;

import java.util.Locale;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Configuration utility methods
 */
public class ConfigUtils {

    /**
     * @param section The {@link ConfigurationSection} to check
     * @param path The path to the String
     *
     * @return The fetched String
     *
     * @throws IllegalArgumentException if the String didn't exit
     */
    public static String ensureAndGetString(ConfigurationSection section, String path) {
        if (!section.isString(path)) {
            throw new IllegalArgumentException(
                    String.format(
                            Locale.ENGLISH, "Key '%s' is missing or not a String in the section '%s'!",
                            path,
                            section.getCurrentPath()
                    )
            );
        }

        return section.getString(path);
    }

    /**
     * @param section The {@link ConfigurationSection} to check
     * @param path The path to the boolean
     *
     * @return The fetched boolean
     *
     * @throws IllegalArgumentException if the boolean didn't exit
     */
    public static boolean ensureAndGetBoolean(ConfigurationSection section, String path) {
        if (!section.isBoolean(path)) {
            throw new IllegalArgumentException(
                    String.format(
                            Locale.ENGLISH, "Key '%s' is missing or not a Boolean in the section '%s'!",
                            path,
                            section.getCurrentPath()
                    )
            );
        }

        return section.getBoolean(path);
    }
}
