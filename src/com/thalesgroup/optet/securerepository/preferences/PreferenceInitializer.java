package com.thalesgroup.optet.securerepository.preferences;



import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.Bundle;

import com.thalesgroup.optet.securerepository.Activator;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {

		
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.SVN_LOCATION, "svn://localhost/");
		store.setDefault(PreferenceConstants.SVN_USER, "user");
		store.setDefault(PreferenceConstants.SVN_PASSWORD, "password");
	}
}
