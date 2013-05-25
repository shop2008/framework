/**
 * 
 */
package com.wxxr.mobile.preference.api;

import java.util.Dictionary;
import java.util.Set;

/**
 * @author Neil
 *
 */
public interface IPreferenceManager {
    /**
     * Get the set of registered PIDs
     * @return The set of registered PIDs or an empty set.
     */
    Set<String> getPreferences();

    /**
     * True if therer is a configuration for the given PID.
     */
    boolean hasPreference(String pid);

    /**
     * Get the configuration dictionary for the given PID.
     * @return The configuration dictionary or <code>null</code>
     */
    Dictionary<String, String> getPreference(String pid);

    /**
     * Put or update the configuration for the given PID.
     * @return The previously registered configuration or <code>null</code>
     */
    Dictionary<String, String> putPreference(String pid, Dictionary<String, String> config);

    /**
     * create a new preference
     * @param pid
     * @param config
     */
    void newPreference(String pid, Dictionary<String, String> config);
    /**
     * Remove the configuration for the given PID.
     * @return The previously registered configuration or <code>null</code>
     */
    Dictionary<String, String> removePreference(String pid);

    /**
     * Add a configuration listener.
     */
    void addListener(IPreferenceChangedListener listener);

    /**
     * Remove a configuration listener.
     */
    void removeListener(IPreferenceChangedListener listener);

}
