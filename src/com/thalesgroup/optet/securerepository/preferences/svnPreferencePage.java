/*
 *		OPTET Factory
 *
 *	Class svnPreferencePage 1.0 31 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.securerepository.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;

/**
 * @author F. Motte
 *
 */
public class svnPreferencePage 
extends PreferencePage
implements IWorkbenchPreferencePage {

	
	private Text txtSvnlocalhost;
	private Text txtMylogin;
	private Text text_1;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		
		SVNPreferences pref = new SVNPreferences();
		Group grpSvn = new Group(parent, SWT.NONE);
		grpSvn.setText("SVN");
		grpSvn.setBounds(10, 19, 418, 85);
		
		Label lblNewLabel = new Label(grpSvn, SWT.NONE);
		lblNewLabel.setLocation(10, 26);
		lblNewLabel.setSize(108, 13);
		lblNewLabel.setText("SVN Repository");
		
		txtSvnlocalhost = new Text(grpSvn, SWT.BORDER);
		txtSvnlocalhost.setLocation(10, 44);
		txtSvnlocalhost.setSize(314, 19);
		txtSvnlocalhost.setText(pref.getSvnLocation());
		
		Group grpUserCredential = new Group(parent, SWT.NONE);
		grpUserCredential.setText("user credential");
		grpUserCredential.setBounds(10, 110, 418, 122);
		
		Label lblLogin = new Label(grpUserCredential, SWT.NONE);
		lblLogin.setBounds(10, 27, 49, 13);
		lblLogin.setText("login");
		
		txtMylogin = new Text(grpUserCredential, SWT.BORDER);
		txtMylogin.setText(pref.getSvnUser());
		txtMylogin.setBounds(10, 46, 130, 19);
		
		Label lblPassword = new Label(grpUserCredential, SWT.NONE);
		lblPassword.setBounds(10, 71, 49, 13);
		lblPassword.setText("password");
		
		text_1 = new Text(grpUserCredential, SWT.BORDER);
		text_1.setText(pref.getSvnPassword());
		text_1.setBounds(10, 90, 130, 19);
		
		return null;
	}

}
