package com.thalesgroup.optet.securerepository.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;

public class svnPreference extends Composite {
	private Text txtSvnlocalhost;
	private Text txtMylogin;
	private Text text_1;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public svnPreference(Composite parent, int style) {
		super(parent, style);
		
		Group grpSvn = new Group(this, SWT.NONE);
		grpSvn.setText("SVN");
		grpSvn.setBounds(10, 19, 418, 85);
		
		Label lblNewLabel = new Label(grpSvn, SWT.NONE);
		lblNewLabel.setLocation(10, 26);
		lblNewLabel.setSize(108, 13);
		lblNewLabel.setText("SVN Repository");
		
		txtSvnlocalhost = new Text(grpSvn, SWT.BORDER);
		txtSvnlocalhost.setLocation(10, 44);
		txtSvnlocalhost.setSize(314, 19);
		txtSvnlocalhost.setText("svn://host/...");
		
		Group grpUserCredential = new Group(this, SWT.NONE);
		grpUserCredential.setText("user credential");
		grpUserCredential.setBounds(10, 110, 418, 122);
		
		Label lblLogin = new Label(grpUserCredential, SWT.NONE);
		lblLogin.setBounds(10, 27, 49, 13);
		lblLogin.setText("login");
		
		txtMylogin = new Text(grpUserCredential, SWT.BORDER);
		txtMylogin.setText("Mylogin");
		txtMylogin.setBounds(10, 46, 130, 19);
		
		Label lblPassword = new Label(grpUserCredential, SWT.NONE);
		lblPassword.setBounds(10, 71, 49, 13);
		lblPassword.setText("password");
		
		text_1 = new Text(grpUserCredential, SWT.BORDER);
		text_1.setText("********");
		text_1.setBounds(10, 90, 130, 19);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
