package com.thalesgroup.optet.securerepository.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tmatesoft.svn.core.SVNLock;



/**
 * SvnEntry : Repr&Atilde;&copy;sente une entr&Atilde;&copy;e (fichier ou dossier) du d&Atilde;&copy;pot SVN
 * @author Valentin CARRUESCO (valentincarruesco@yahoo.fr)
 * @date 08/11/2011 12:11:44
 * @version 1.0
 */

public class SvnEntry {
	
	/**
	 * Déclaration des attributs de la classe SvnEntry
	 */
	 
	 /**
	 * Integer "id" de l'objet SvnEntry courant
	 */
	 Integer id;
	/**
	 * Chaine courte "author" de l'objet SvnEntry courant
	 */
	private String author;
	
	/**
	 * Entier "revision" de l'objet SvnEntry courant
	 */
	private long revision;
	
	/**
	 * Chaine longue "date" de l'objet SvnEntry courant
	 */
	private Date date;
	
	/**
	 * Chaine longue "path" de l'objet SvnEntry courant
	 */
	private String path;
	
	/**
	 * Chaine courte "type" de l'objet SvnEntry courant
	 */
	private String type;
	
	/**
	 * Chaine courte "lock" de l'objet SvnEntry courant
	 */
	private SVNLock lock;
	
	/**
	 * Entier "size" de l'objet SvnEntry courant
	 */
	private long size;
	
	/**
	 * Chaine longue "url" de l'objet SvnEntry courant
	 */
	private String url;
	

	private List<SvnEntry> childrenList;
	
	private SvnEntry parent;
	
	public SvnEntry getParent() {
		return parent;
	}

	public void setParent(SvnEntry parent) {
		this.parent = parent;
	}

	/**
	 * Constructeur de la classe SvnEntry pour une instanciation à vide
	 * @author Valentin CARRUESCO
	 * @version 1
	 */
	public SvnEntry() {
		childrenList = new ArrayList<>();
	}
	
	/**
	 * Constructeur de la classe avec initialisation des attributs <b>author</b>,<b>revision</b>,<b>date</b>,<b>path</b>,<b>type</b>,<b>lock</b>,<b>size</b>,<b>url</b>
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @param author
	 * @param revision
	 * @param date
	 * @param path
	 * @param type
	 * @param lock
	 * @param size
	 * @param url
	 */
	public SvnEntry(String author ,Integer revision ,Date date ,String path ,String type ,SVNLock lock ,Integer size ,String url ) {
		super();
		this.author = author;
		this.revision = revision;
		this.date = date;
		this.path = path;
		this.type = type;
		this.lock = lock;
		this.size = size;
		this.url = url;
		}
	
	
	
	/************************/
	/** METHODES REECRITES **/
	/************************/
	
	
	/**
	 * Cette méthode retourne un clone de l'objet courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @see java.lang.Object#clone()
	 * @return<SvnEntry> objet de type SvnEntry cloné a partir de l'objet courant
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
	 * @return<String> chaine representant le contenu de l'objet SvnEntry courant
	 **/
	@Override
	public String toString() {
		String sortie = "==> Instance de la classe SvnEntry : \n";
		sortie += "String author = "+this.getAuthor()+" \n";
		sortie += "Integer revision = "+this.getRevision()+" \n";
		sortie += "String date = "+this.getDate()+" \n";
		sortie += "String path = "+this.getPath()+" \n";
		sortie += "String type = "+this.getType()+" \n";
		sortie += "String lock = "+this.getLock()+" \n";
		sortie += "Integer size = "+this.getSize()+" \n";
		sortie += "String url = "+this.getUrl()+" \n";
		
		return sortie;
	}
	
	
	
	/****************/
	/** ACCESSEURS **/
	/****************/
	
	/**
	 * Retourne l'attribut id de l'objet SvnEntry courant sous forme de Integer
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<Integer> id de l'objet SvnEntry courant
	 */
	public Integer getId() {
		return this.id;
	}
	
	/**
	 * Definis l'attribut id de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<Integer> id de l'objet SvnEntry courant
	 */
	public void setId(Integer id) {
		this.id= id;
	}
	
	/**
	 * Retourne l'attribut author de l'objet SvnEntry courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> author de l'SvnEntry courant
	 */
	public String getAuthor() {
		if (author==null)author="";
		return author;
	}
	
	/**
	 * Definis l'attribut author de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * Retourne l'attribut revision de l'objet SvnEntry courant sous forme de Integer
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<Integer> revision de l'SvnEntry courant
	 */
	public long getRevision() {
		return revision;
	}
	
	/**
	 * Definis l'attribut revision de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<Integer> revision
	 */
	public void setRevision(long l) {
		this.revision = l;
	}
	
	/**
	 * Retourne l'attribut date de l'objet SvnEntry courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> date de l'SvnEntry courant
	 */
	public Date getDate() {
		if (date==null)date=new Date();
		return date;
	}
	
	/**
	 * Definis l'attribut date de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> date
	 */
	public void setDate(Date date2) {
		this.date = date2;
	}
	
	/**
	 * Retourne l'attribut path de l'objet SvnEntry courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> path de l'SvnEntry courant
	 */
	public String getPath() {
		if (path==null)path="";
		return path;
	}
	
	/**
	 * Definis l'attribut path de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Retourne l'attribut type de l'objet SvnEntry courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> type de l'SvnEntry courant
	 */
	public String getType() {
		if (type==null)type="";
		return type;
	}
	
	/**
	 * Definis l'attribut type de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Retourne l'attribut lock de l'objet SvnEntry courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> lock de l'SvnEntry courant
	 */
	public SVNLock getLock() {
		return lock;
	}
	
	/**
	 * Definis l'attribut lock de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> lock
	 */
	public void setLock(SVNLock svnLock) {
		this.lock = svnLock;
	}
	
	/**
	 * Retourne l'attribut size de l'objet SvnEntry courant sous forme de Integer
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<Integer> size de l'SvnEntry courant
	 */
	public long getSize() {
		return size;
	}
	
	/**
	 * Definis l'attribut size de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<Integer> size
	 */
	public void setSize(long l) {
		this.size = l;
	}
	
	/**
	 * Retourne l'attribut url de l'objet SvnEntry courant sous forme de String
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Getter)
	 * @return<String> url de l'SvnEntry courant
	 */
	public String getUrl() {
		if (url==null)url="";
		return url;
	}
	
	/**
	 * Definis l'attribut url de l'objet SvnEntry courant
	 * @author Valentin CARRUESCO
	 * @version 1
	 * @category Accesseur(Setter)
	 * @param<String> url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void addChildren (SvnEntry children){
		childrenList.add(children);
	}

	public List<SvnEntry> getChildren(){
		return childrenList;
	}
}
