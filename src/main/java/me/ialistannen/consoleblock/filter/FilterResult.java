package me.ialistannen.consoleblock.filter;

/**
 * The result of applying a {@link MessageFilter}
 */
public enum FilterResult {

    /**
     * Filter it out, do not log it
     */
    FILTER,
    /**
     * Let is pass the filter, log it
     */
    PASS
}
