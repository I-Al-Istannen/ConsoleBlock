package me.ialistannen.consoleblock.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.consoleblock.ConsoleBlock;
import me.ialistannen.consoleblock.filter.FilterManager;
import me.ialistannen.consoleblock.filter.MessageFilter;

/**
 * Parses Filters
 */
public class FilterParser {

    /**
     * @param section The section to parse.
     *
     * @return The parsed filters
     */
    public List<MessageFilter> parse(ConfigurationSection section) {
        FilterManager filterManager = ConsoleBlock.getInstance().getFilterManager();

        Logger logger = ConsoleBlock.getInstance().getLogger();

        List<MessageFilter> filters = new LinkedList<>();

        for (String key : section.getKeys(false)) {
            if (!section.isConfigurationSection(key)) {
                logger.warning(
                        String.format(
                                Locale.ENGLISH,
                                "The element under key '%s' is not a section.",
                                section.getCurrentPath() + "." + key
                        )
                );
                continue;
            }
            MessageFilter messageFilter = filterManager.constructFilter(section.getConfigurationSection(key));
            filters.add(messageFilter);
        }

        logger.info(
                String.format(
                        Locale.ENGLISH,
                        "Loaded %d filter(s)",
                        filters.size()
                )
        );

        return filters;
    }
}
