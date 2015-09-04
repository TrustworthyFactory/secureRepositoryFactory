/*
 *		OPTET Factory
 *
 *	Class browseRepoHandler 1.0 31 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.securerepository.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.thalesgroup.optet.securerepository.views.RepositoryBrowser;

/**
 * @author F. Motte
 *
 */
public class browseRepoHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public browseRepoHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event)
			throws ExecutionException
			{
		RepositoryBrowser browser = new RepositoryBrowser(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		browser.open();
//		try
//		{
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow
//			().getActivePage().showView("com.thalesgroup.optet.securerepository.views.RepositoryBrowser");
//		} catch (PartInitException e) {
//			e.printStackTrace();
//		}
		return null;
			}
}