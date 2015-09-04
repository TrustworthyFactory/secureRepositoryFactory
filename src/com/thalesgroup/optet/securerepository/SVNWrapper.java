package com.thalesgroup.optet.securerepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.tmatesoft.svn.core.SVNException;

import com.thalesgroup.optet.common.data.CSM;
import com.thalesgroup.optet.common.data.ProjectInfo;

import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;

public interface SVNWrapper {

	public Set<SvnEntry> getProjectFromSVN();

	/**
	 * return the description of the project 
	 * @param projectID the ID of the project
	 * @return the description of the project
	 * @throws IOException 
	 * @throws SVNException 
	 */
	public ByteArrayOutputStream getProjectDescription(SvnEntry projectID) ;

	/**
	 * get the TWProfile of the project
	 * 
	 * @param projectID
	 *            the ID of the project
	 * @return the TW profile of the project
	 * @throws ProjectNotFoundException
	 * @throws JAXBException
	 * @throws IOException 
	 * @throws SVNException 
	 */
	public TWProfile getTWProfile(SvnEntry projectID)
			throws ProjectNotFoundException, JAXBException;

	/**
	 * get the CSM of the project
	 * 
	 * @param projectID
	 *            the ID of the project
	 * @return the CSM of the project
	 * @throws ProjectNotFoundException
	 */
	public CSM getCSM(SvnEntry projectID) throws ProjectNotFoundException;

	/**
	 * return the list of information on the project
	 * 
	 * @param projectID
	 *            the ID of the project
	 * @return list of information
	 * @throws ProjectNotFoundException
	 */
	public List<ProjectInfo> getProjectInfos(SvnEntry projectID)
			throws ProjectNotFoundException;

	/**
	 * archive the source files of the project into the secure repository
	 * 
	 * @param projectID
	 *            the ID of the project
	 * @param commitedList
	 *            the source files to save
	 * @throws ProjectNotFoundException
	 */
	public void setFilesToSVN(SvnEntry projectID, List<IFile> commitedList)
			throws ProjectNotFoundException;

	/**
	 * archive the source files of the project into the secure repository
	 * @param projectID
	 *            the ID of the project
	 * @param commitedList
	 *            the source files to save
	 * @throws ProjectNotFoundException
	 */
	public void setFilesToSVN(String projectID, List<IFile> commitedList)
			throws ProjectNotFoundException;

	/**
	 * recover the source files of the project from the secure repository
	 * 
	 * @param projectID
	 *            the ID of the project
	 * @return the list of source files of the project
	 * @throws ProjectNotFoundException
	 */
	public List<SvnEntry> getFilesFromSVN(SvnEntry projectID)
			throws ProjectNotFoundException;

	/**
	 * @param author
	 * @param project
	 */
	public void getFileFromSVN(String projectID, IProject project)
			throws ProjectNotFoundException;

	/**
	 * @param projectID
	 * @throws IOException 
	 * @throws SVNException 
	 */
	public TWProfile getTWProfile(String projectID);
}
