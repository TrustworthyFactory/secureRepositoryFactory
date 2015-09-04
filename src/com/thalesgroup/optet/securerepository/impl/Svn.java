package com.thalesgroup.optet.securerepository.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;



/**
 * Svn : Classe de gestion du SVN a l\'aide de la librairie SVNKit http://www.svnkit.com/
 * @author Valentin CARRUESCO (valentincarruesco@yahoo.fr)
 * @date 08/11/2011 11:48:24
 * @version 1.0
 */

public class Svn {

	/**
	 * Déclaration des attributs de la classe Svn
	 */

	/**
	 * Integer "id" de l'objet Svn courant
	 */
	Integer id;
	/**
	 * Chaine longue "url" de l'objet Svn courant
	 */
	private String url;

	/**
	 * Chaine courte "login" de l'objet Svn courant
	 */
	private String login;

	/**
	 * Chaine courte "password" de l'objet Svn courant
	 */
	private String password;


	public final static long HEAD_REV = -1;
	public final static long UNDEFINED_REV = -2;


	SVNKitAdapter svnAdapter;


	/**
	 * Constructeur de la classe avec initialisation des attributs <b>url</b>,<b>login</b>,<b>password</b>
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @param url
	 * @param login
	 * @param password
	 */
	public Svn(String url ,String login ,String password ) {
		super();
		this.url = url;
		this.login = login;
		this.password = password;

		try {
			svnAdapter = new SVNKitAdapter(url, login, password);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//	/**
	//	 * Se connecte et s'authentifie a un depot svn
	//	 * @return
	//	 */
	//	public boolean connect(){
	//		boolean success = false;
	//
	//		try {
	//			//Connexion
	//			repository = SVNRepositoryFactory.create( SVNURL.parseURIDecoded( this.url ) );
	//			//Authentification
	//			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( this.login , this.password );
	//			this.repository.setAuthenticationManager(authManager);
	//			success = true;
	//		} catch (SVNException e) {
	//			System.out.println("Impossible de se connecter/autentifier au SVN : "+e);                
	//		}
	//		return success;
	//	}


	/************************/
	/** METHODES REECRITES **/
	/************************/


	/**
	 * Cette méthode retourne un clone de l'objet courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @see java.lang.Object#clone()
	 * @return<Svn> objet de type Svn cloné a partir de l'objet courant
	 **/
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}


	/**
	 * Cette méthode compare l'objet courant a l'objet passé en parametre et retourne un bouléen
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return true si les deux objets sont semblables, false dans les autres cas.
	 **/
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}



	/** 
	 * Retourne le toString de l'objet (chaine affaichant le contenu des attributs de l'objet)
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @see java.lang.Object#toString()
	 * @return<String> chaine representant le contenu de l'objet Svn courant
	 **/
	@Override
	public String toString() {
		String sortie = "==> Instance de la classe Svn : \n";
		sortie += "String url = "+this.getUrl()+" \n";
		sortie += "String login = "+this.getLogin()+" \n";
		sortie += "String password = "+this.getPassword()+" \n";

		return sortie;
	}


	/**
	 * Vérifie un chemin et definis si c'ets un dossier, un fichier ou rien(si il n'existe pas)
	 * @param path
	 * @return
	 */
	public String check(String path){
		return svnAdapter.check(path);
	}

	public ByteArrayOutputStream getFile(String filePath)  throws SVNException, IOException {

		ByteArrayOutputStream out = null;
		try {
			String file = filePath.replaceFirst(url, "");
			out = new ByteArrayOutputStream();
			out.write(svnAdapter.getFile(file, -1, true).getBytes());
		} catch (IOException | SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;

		//		try {
		//			System.out.println("path " + filePath);
		//			System.out.println("svn://localhost/MyProject3/readme.txt");
		//			SVNNodeKind kind = this.repository.checkPath("/MyProject3/readme.txt", -1);	
		//			System.out.println("get location" + this.repository.getLocation());
		//			System.out.println("kind " + kind.getNodeKindById(kind.getID()));
		//		} catch (SVNException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}
		//		
		//
		//				
		//		
		//		SVNProperties fileProperties = new SVNProperties();
		//		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		//		try {
		//			String file = filePath.replaceFirst(this.repository.getLocation().toString(), "");
		//			this.repository.getFile(file, -1, fileProperties, boas);
		//		} catch (SVNException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		return boas;
	}



	/**
	 * Liste un dossier du dépot
	 * @param path dossier du dépot
	 * @throws SVNException
	 */
	@SuppressWarnings("rawtypes")
	public List<SvnEntry> list(String path , SvnEntry rootEntry) throws SVNException {
		return svnAdapter.list(path, rootEntry);
	}

	public void commitSources(String projectID, List<IFile> files){

		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			IFile iFile = (IFile) iterator.next();
			try {				
				svnAdapter.addFile("/" + projectID + "/sources" + (iFile.getFullPath().toString()).replaceFirst("/"+iFile.getProject().getName(), ""), iFile.getContents(), "commit");
			} catch (SVNException | CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * @param projectID
	 * @param project
	 */
	public void checkout(String projectID, IProject project) {
		// TODO Auto-generated method stub
		try {
			Map<String, String> listOfFiles = svnAdapter.getFiles("/" + projectID + "/sources", svnAdapter.getLatestRevision(), true);
			for(Entry<String, String> entry : listOfFiles.entrySet()) {
			    String cle = entry.getKey();
			    System.out.println("file " + cle);
			    String valeur = entry.getValue();
			    IPath path = new Path(cle.replaceFirst("/" + projectID + "/sources", ""));
			    System.out.println("path " + path);
			    IFile file = project.getFile(path);

			    System.out.println("reltive path = " +file.getProjectRelativePath());
			    
			    try {
			    	if (file.getParent() instanceof IFolder){
			    		prepareFolder((IFolder) file.getParent());
			    	}
			    	
			    	if (file.exists()){
			    		file.setContents(new ByteArrayInputStream(valeur.getBytes()) , true,true, null);
			    	} else {
			    		file.create(new ByteArrayInputStream(valeur.getBytes()) , true, null);
			    	}					
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				IFolder folder = project.getFolder("Optet");
//				folder.create(true, true, null);
//				IFile optetProp = folder.getFile("Optet.properties");
//				String prop = "svn.project.selected=" + svnentry.getAuthor();
//				optetProp.create(new ByteArrayInputStream(prop.getBytes()), true, null);
			    // traitements
			}
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/****************/
	/** ACCESSEURS **/
	/****************/

	/**
	 * @param parent
	 * @throws CoreException 
	 */
	private void prepareFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if(parent instanceof IFolder){
			prepareFolder((IFolder) parent);
		}
		if(!folder.exists()){
			folder.create(true, true, null);
		}
		
	}


	/**
	 * Retourne l'attribut id de l'objet Svn courant sous forme de Integer
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<Integer> id de l'objet Svn courant
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Definis l'attribut id de l'objet Svn courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<Integer> id de l'objet Svn courant
	 */
	public void setId(Integer id) {
		this.id= id;
	}

	/**
	 * Retourne l'attribut url de l'objet Svn courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> url de l'Svn courant
	 */
	public String getUrl() {
		if (url==null)url="";
		return url;
	}

	/**
	 * Definis l'attribut url de l'objet Svn courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Retourne l'attribut login de l'objet Svn courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> login de l'Svn courant
	 */
	public String getLogin() {
		if (login==null)login="";
		return login;
	}

	/**
	 * Definis l'attribut login de l'objet Svn courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Retourne l'attribut password de l'objet Svn courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> password de l'Svn courant
	 */
	public String getPassword() {
		if (password==null)password="";
		return password;
	}

	/**
	 * Definis l'attribut password de l'objet Svn courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> password
	 */
	public void setPassword(String password) {
		this.password = password;
	}



//	/**
//	 * Récupère la racine du dépot
//	 * @return racine du dépot
//	 */
//	public String getRoot(){
//		String root = "Pas de racine";
//		try {
//			root = this.repository.getRepositoryRoot( true ).toString() ;
//		} catch (SVNException e) {
//			System.out.println("Impossible de récuperer la racine:"+e);
//		}
//		return root;
//	}
//
//	/**
//	 * Récupère l'UID du dépot
//	 * @return UID du dépot
//	 */
//	public String getUID(){
//		String uid = "Pas d'UID";
//		try {
//			uid = this.repository.getRepositoryUUID( true ).toString() ;
//		} catch (SVNException e) {
//			System.out.println("Impossible de récuperer l'UID:"+e);
//		}
//		return uid;
//	}

}
