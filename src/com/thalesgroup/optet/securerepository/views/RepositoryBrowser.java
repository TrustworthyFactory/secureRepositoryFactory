/*
 *		OPTET Factory
 *
 *	Class RepositoryBrowser 1.0 31 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.securerepository.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.tmatesoft.svn.core.SVNException;

import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;

import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;
import com.thalesgroup.optet.twmanagement.SettingsModule;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;


/**
 * @author F. Motte
 *
 */
public class RepositoryBrowser extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;


	private SVNWrapper svn;
	private SvnEntry selectedSvnentry = null;
	private TWProfile selectedTWProfile = null;
	private TreeViewer treeViewer;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RepositoryBrowser(Shell parent, int style) {
		super(parent, style);
		setText("Secure repository browser");
		svn = new SVNWrapperImpl();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 481);
		shell.setText(getText());

		Group grpProject = new Group(shell, SWT.NONE);
		grpProject.setText("Projects");
		grpProject.setBounds(10, 10, 424, 398);

		ListViewer listViewer = new ListViewer(grpProject, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setBounds(10, 20, 170, 368);

		Set<SvnEntry> projectList = svn.getProjectFromSVN();
		System.out.println("set size " + projectList.size());
		//		for (Iterator<String> iterator = projectList.iterator(); iterator.hasNext();) {
		//			String string = (String) iterator.next();
		//			list.add(string);
		//		}
		//		


		listViewer.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return ((Set<String>)inputElement).toArray();
			}});

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				try {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					System.out.println("Selected: " + selection.getFirstElement());
					System.out.println(selection.size());

					System.out.println(svn.getProjectDescription((SvnEntry)selection.getFirstElement()));
					text.setMessage(svn.getProjectDescription((SvnEntry)selection.getFirstElement()).toString());
					selectedSvnentry = (SvnEntry)selection.getFirstElement();
					selectedTWProfile = svn.getTWProfile(selectedSvnentry);

					System.out.println("-----------------------------");
					treeViewer.setInput(svn.getFilesFromSVN(selectedSvnentry));
				} catch (ProjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		listViewer.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return ((SvnEntry)element).getAuthor();
			}

		});


		listViewer.setInput(projectList);

		text = new Text(grpProject, SWT.BORDER);
		text.setEditable(false);
		text.setEnabled(false);

		text.setBounds(186, 20, 228, 178);

		treeViewer = new TreeViewer(grpProject, SWT.BORDER);

		treeViewer.setLabelProvider(
				new SvnEntryListLabelProvider());
		treeViewer.setContentProvider(
				new SvnEntryTreeContentProvider());
		treeViewer.expandAll();

		Tree tree = treeViewer.getTree();
		tree.setBounds(186, 204, 228, 184);

		Button btnValidate = new Button(shell, SWT.NONE);
		btnValidate.setBounds(366, 416, 68, 23);
		btnValidate.setText("Validate");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(281, 416, 68, 23);
		btnCancel.setText("Cancel");

		btnValidate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				java.util.List<String> list = new ArrayList<String>();
				for (int i = 0; i < projects.length; i++) {
					System.out.println("Project name " + projects[i].getName());
					list.add(projects[i].getName());
				}
				
				ListSelectionDialog dlg = new ListSelectionDialog(
					    shell,
					    ResourcesPlugin.getWorkspace().getRoot(), 
					    new BaseWorkbenchContentProvider(),
					    new WorkbenchLabelProvider(),
					    "Select the Project:");
					dlg.setTitle("Project Selection");
					dlg.open();
					Object[] selectionResult = dlg.getResult();
					for (int i = 0; i < selectionResult.length; i++) {
						if (selectionResult[i] instanceof IProject) {
							IProject project = (IProject) selectionResult[i];
							try {
								IFolder folder = project.getFolder("Optet");
								if (!folder.exists())
								{
								folder.create(false, true, null);
								}
								IFile optetProp = folder.getFile("Optet.properties");
								if (optetProp.exists())
								{
									optetProp.delete(false, null);
								}
								String prop = "svn.project.selected=" + selectedSvnentry.getAuthor();
								optetProp.create(new ByteArrayInputStream(prop.getBytes()), false, null);
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}	
					}

						        
//				ProjectSelection projectSelect = new ProjectSelection(getParent());
//				projectSelect.open();
				
				if (selectedSvnentry != null && selectedTWProfile != null){
					SettingsModule settings = SettingsModuleImpl.getInstance();
					System.out.println("****" + selectedSvnentry.getId());
					if (settings == null)
						System.out.println("error settings");
					settings.setTWProfile(selectedSvnentry.getAuthor(), selectedTWProfile);
				}
				shell.close();
			}
		});
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});

	}
}
