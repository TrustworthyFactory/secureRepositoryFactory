/*
 *		OPTET Factory
 *
 *	Class ProjectSelection 1.0 4 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.securerepository.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.resources.IProject;

import org.eclipse.core.resources.ResourcesPlugin;
/**
 * @author F. Motte
 *
 */
public class ProjectSelection extends Dialog {

	private String currentProject = null;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ProjectSelection(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblSelectTheProject = new Label(container, SWT.NONE);
		lblSelectTheProject.setText("Select the project to apply");
		
		List list = new List(container, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_list.heightHint = 176;
		gd_list.widthHint = 418;
		list.setLayoutData(gd_list);
		


		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			System.out.println("Project name " + projects[i].getName());
			list.add(projects[i].getName());
		}
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public String getSelectedProject(){
		return currentProject;
	}
	
}
