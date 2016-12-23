package me.ialistannen.consoleblock;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.bukkit.plugin.java.JavaPlugin;

import me.ialistannen.consoleblock.commands.CommandHandler;
import me.ialistannen.consoleblock.filter.FilterManager;
import me.ialistannen.consoleblock.filter.MessageFilter;
import me.ialistannen.consoleblock.filter.log4j.LOG4JFilter;
import me.ialistannen.consoleblock.parser.FilterParser;

public final class ConsoleBlock extends JavaPlugin {

    private static ConsoleBlock instance;

    private FilterManager filterManager;

    public void onEnable() {
        instance = this;

//        new File(getDataFolder(), "config.yml").delete();

        saveDefaultConfig();
        
        getCommand("consoleblock").setExecutor(new CommandHandler());

        reloadFilters();
    }

    @Override
    public void onDisable() {
        killFilters();
        
        // prevent the old instance from still being around.
        instance = null;
    }

    private List<LOG4JFilter> findFilters(XMLConfiguration configuration) {
        List<LOG4JFilter> filters = new ArrayList<>();

        Filter filter = configuration.getFilter();
        if (filter instanceof CompositeFilter) {
            ((CompositeFilter) filter).iterator().forEachRemaining(filter1 -> {
                if (filter1 instanceof LOG4JFilter) {
                    filters.add((LOG4JFilter) filter1);
                }
            });
        }
        else {
            if (filter instanceof LOG4JFilter) {
                filters.add((LOG4JFilter) filter);
            }
        }

        return filters;
    }
    
    private void killFilters() {
        Logger rootLogger = (Logger) LogManager.getRootLogger();

        XMLConfiguration configuration = (XMLConfiguration) rootLogger.getContext().getConfiguration();

        for (LOG4JFilter log4JFilter : findFilters(configuration)) {
            configuration.removeFilter(log4JFilter);
        }
    }
    
    public void reloadFilters() {
        killFilters();

        Logger rootLogger = (Logger) LogManager.getRootLogger();

        XMLConfiguration configuration = (XMLConfiguration) rootLogger.getContext().getConfiguration();

        for (LOG4JFilter log4JFilter : findFilters(configuration)) {
            configuration.removeFilter(log4JFilter);
        }

        filterManager = new FilterManager();

        for (MessageFilter filters : new FilterParser().parse(getConfig().getConfigurationSection("filters"))) {
            filterManager.addActiveFilter(filters);
        }

        configuration.addFilter(new LOG4JFilter());
    }
    
    /**
     * @return The {@link FilterManager}
     */
    public FilterManager getFilterManager() {
        return filterManager;
    }

    /**
     * Returns the plugins instance
     *
     * @return The plugin instance
     */
    public static ConsoleBlock getInstance() {
        return instance;
    }
}
