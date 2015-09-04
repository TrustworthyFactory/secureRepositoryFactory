package com.thalesgroup.optet.securerepository.preferences;

import org.eclipse.core.runtime.Platform;

import com.thalesgroup.optet.securerepository.Activator;

/**
 * CryptoPreference return the user preference from the preference page 
 * @author F. Motte
 *
 */
public class SVNPreferences {
	/**

	 * @return the certificate path
	 */
	public String getSvnLocation () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.SVN_LOCATION, "", null);
	}
	
	/**

	 * @return the alias
	 */
	public String getSvnUser () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.SVN_USER, "", null);
	}

	/**

	 * @return the password
	 */
	public String getSvnPassword () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.SVN_PASSWORD, "", null);
	}
	
}
