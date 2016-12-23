package me.ialistannen.consoleblock.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.bukkit.configuration.ConfigurationSection;

import me.ialistannen.consoleblock.filter.types.ExactMatchFilter;
import me.ialistannen.consoleblock.filter.types.PartialMatchFilter;
import me.ialistannen.consoleblock.filter.types.RegexFilter;
import me.ialistannen.consoleblock.util.ConfigUtils;

/**
 * Manages the filters
 */
public class FilterManager {

    private Map<String, MessageFilter> filterMap = new HashMap<>();

    private List<MessageFilter> activeFilters = new ArrayList<>();

    {
        addDefaults();
    }

    /**
     * @param messageFilter The {@link MessageFilter} to add
     */
    public void addFilter(MessageFilter messageFilter) {
        Objects.requireNonNull(messageFilter, "messageFilter can not be null!");

        filterMap.put(messageFilter.getFilterType(), messageFilter);
    }

    /**
     * @param name The name of the filter. Case sensitive
     *
     * @return The {@link MessageFilter}, if any
     */
    public Optional<MessageFilter> getFilter(String name) {
        return Optional.ofNullable(filterMap.get(name));
    }

    /**
     * Constructs a filter from a {@link ConfigurationSection}
     * <p>
     * Must contain:
     * <ul>
     * <li>"filter_type"</li>
     * </ul>
     *
     * @param section The section to read stuff from.
     *
     * @return The MessageFilter
     */
    public MessageFilter constructFilter(ConfigurationSection section) {
        String name = ConfigUtils.ensureAndGetString(section, "filter_type");
        MessageFilter filter = getFilter(name).orElseThrow(() -> new IllegalArgumentException(
                String.format(Locale.ENGLISH, "Filter '%s' is not known.", name)
        ));

        return filter.cloneWithSettings(section);
    }

    /**
     * @param messageFilter The {@link MessageFilter} to add
     */
    public void addActiveFilter(MessageFilter messageFilter) {
        activeFilters.add(messageFilter);
    }

    /**
     * @return All active filters. Unmodifiable.
     */
    public List<MessageFilter> getActiveFilters() {
        return Collections.unmodifiableList(activeFilters);
    }

    /**
     * Adds the default filters
     */
    private void addDefaults() {
        addFilter(new RegexFilter());
        addFilter(new PartialMatchFilter());
        addFilter(new ExactMatchFilter());
    }
}
