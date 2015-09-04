/*
 *		OPTET Factory
 *
 *	Class SVNWrapperImpl 1.0 24 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.securerepository.impl;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.tmatesoft.svn.core.SVNException;

import com.thalesgroup.optet.common.data.CSM;
import com.thalesgroup.optet.common.data.ProjectInfo;
import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.preferences.SVNPreferences;

/**
 * @author F. Motte
 *
 */
public class SVNWrapperImpl implements SVNWrapper {


	private boolean svnConnection;
	private Svn svn;

	private SVNPreferences pref = null;

	public SVNWrapperImpl() {
		super();
		// TODO Auto-generated constructor stub

		pref = new SVNPreferences();

		System.out.println("Svn connection");
		System.out.println("Svn connection path :" + pref.getSvnLocation());
		System.out.println("Svn connection user :" + pref.getSvnUser());
		System.out.println("Svn connection password : " + pref.getSvnPassword());


		svn = new Svn(pref.getSvnLocation(), pref.getSvnUser(), pref.getSvnPassword());
		//		//On definis l'url du dépot subversion
		//		svn.setUrl("svn://localhost/");
		//		//On définis l'identifiant pour accèder au dépot (si l'accès est anonyme, ne rien définir)
		//		svn.setLogin("user");
		//		//On définis le mot de passe pour accèder au dépot (si l'accès est anonyme, ne rien définir)
		//		svn.setPassword("password");
		//
		//		//on se connect au svn
		//		svnConnection = svn.connect();

	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getProjectFromSVN()
	 */
	@Override
	public Set<SvnEntry> getProjectFromSVN() {
		Set<SvnEntry> projectList = new HashSet<>();




		//On récupere la liste des dossier et fichiers du dépot a partir de la racine (donc le chemin "")
		try {
			List<SvnEntry> tree= svn.list("", null);
			//Pour chaques élément récupéré
			for (int i = 0; i < tree.size(); i++) {
				SvnEntry entry = tree.get(i);
				//On imprime les infos de l'élément dans la console
				System.out.println("add "  + entry.toString());
				projectList.add(entry);
			}
		} catch (SVNException e) {
			System.out.println("listing de '' impossible : "+e);
		}

		return projectList;
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getTWProfile(java.lang.String)
	 */
	@Override
	public TWProfile getTWProfile(SvnEntry projectID)
			throws ProjectNotFoundException, JAXBException  {

		ByteArrayOutputStream twprofile= null;
		TWProfile twProfile = null;
		try {
			twprofile = svn.getFile(projectID.getUrl()+"/TWProfile.xml");

		System.out.println("JAXBContext.newInstance");
		JAXBContext jaxbContext = JAXBContext.newInstance(TWProfile.class);
		System.out.println("jaxbContext.createUnmarshaller();");
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		System.out.println(" jaxbUnmarshaller.unmarshal");
		twProfile = (TWProfile) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(twprofile.toByteArray()));
		} catch (SVNException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return twProfile;
	}

//	public File getTWProfile(String projectID){
//		ByteArrayOutputStream twprofile;
//
//		try {
//			twprofile = svn.getFile("/" + projectID+"/TWProfile.xml");
//		}
//	 catch (SVNException | IOException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
//	}
	
	public TWProfile getTWProfile(String projectID)   {
		ByteArrayOutputStream twprofile;
		TWProfile twProfile = null;
		try {
			twprofile = svn.getFile("/" + projectID+"/TWProfile.xml");


			System.out.println("JAXBContext.newInstance");
			JAXBContext jaxbContext;
			
			try {
				jaxbContext = JAXBContext.newInstance(TWProfile.class);
				System.out.println("jaxbContext.createUnmarshaller();");
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				System.out.println(" jaxbUnmarshaller.unmarshal");
				twProfile = (TWProfile) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(twprofile.toByteArray()));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SVNException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// TODO Auto-generated method stub
		return twProfile;
	}
	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getCSM(java.lang.String)
	 */
	@Override
	public CSM getCSM(SvnEntry projectID) throws ProjectNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getProjectInfos(java.lang.String)
	 */
	@Override
	public List<ProjectInfo> getProjectInfos(SvnEntry projectID)
			throws ProjectNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#setFilesToSVN(java.lang.String, java.util.List)
	 */
	@Override
	public void setFilesToSVN(SvnEntry projectID, List<IFile> files)
			throws ProjectNotFoundException {
		setFilesToSVN(projectID.getAuthor(), files);

	}
	public void setFilesToSVN(String projectID, List<IFile> files)
			throws ProjectNotFoundException {
		svn.commitSources( projectID, files);

	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getFilesFromSVN(com.thalesgroup.optet.securerepository.impl.SvnEntry)
	 */
	@Override
	public List<SvnEntry> getFilesFromSVN(SvnEntry projectID)
			throws ProjectNotFoundException {
		try {
			return svn.list(projectID.getAuthor(), null);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public  List<SvnEntry> getFileFromSVN(String projectID)
			throws ProjectNotFoundException{
		try {
			return svn.list(projectID, null);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getFilesFromSVN(java.lang.String)
	 */
	@Override
	public void getFileFromSVN(String projectID, IProject project)
			throws ProjectNotFoundException{
		svn.checkout( projectID,  project);


	}

	/* (non-Javadoc)
	 * @see com.thalesgroup.optet.securerepository.SVNWrapper#getProjectDescription(java.lang.String)
	 */
	@Override
	public ByteArrayOutputStream getProjectDescription(SvnEntry projectID) {
		try {
			return svn.getFile(projectID.getUrl()+"/readme.txt");
		} catch (SVNException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}




}
