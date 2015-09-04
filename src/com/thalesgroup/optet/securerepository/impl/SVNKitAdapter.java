package com.thalesgroup.optet.securerepository.impl;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;



/**
 * User: DeToN@ToR
 * Date: 17.02.2009
 * Time: 13:28:34
 *
 *  SVN protocol is supposed to be for testing (on Windows, because Apache doesn't work with my mod_dav_svn)
 *  HTTP is the only choice for production system, although I was tempting to access SVN repo locally,
 *  but SVNKit doesn't work with BDB repositories directly (via local protocol)
 *

 */
public class SVNKitAdapter {

    public final static long HEAD_REV = -1;
    public final static long UNDEFINED_REV = -2;

    public long getLatestRevision() throws SVNException {
        try {
            return repository.getLatestRevision();
        } catch (SVNException e) {
            processSVNException(null, e);
        }
        return -2; //this statement should never be reachable since statement inside 'catch' will always throw an exception
    }

    enum RepoAccessKind {
        SVN, HTTP;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static String defUserName = "user";
    private static String defPassword = "";

    private final String url;
    private final RepoAccessKind repoAccessKind;
    private final SVNRepository repository;
    ISVNAuthenticationManager authManager;
    String userName = null;
    String password = null;


    /** Provide null userName and password if you want to use default values or think that authorization is not needed
     *
     * @param repoUrl repository url (SVN or HTTP(s))
     * @param userName svn user name
     * @param password svn password
     * @throws SVNException any SVNException
     */
    public SVNKitAdapter(String repoUrl, String userName, String password) throws SVNException {
        this.userName = (userName == null) ? defUserName : userName;
        this.password = (password == null) ? defPassword: password;
        url = repoUrl.trim();
         if (url.startsWith("svn")) {
            repoAccessKind = RepoAccessKind.SVN;
             SVNRepositoryFactoryImpl.setup();
        } else if (url.startsWith("http")) {
            repoAccessKind = RepoAccessKind.HTTP;
            DAVRepositoryFactory.setup();
        } else {
            throw new SVNException(SVNErrorMessage.create(SVNErrorCode.BAD_URL, "This URL is not supported by TNTBase, " +
                    "\n Only SVN and HTTP(s) protocols are allowed"));
        }
        authManager = SVNWCUtil.createDefaultAuthenticationManager(this.userName, this.password);
        repository = getSVNRepositoryInstance();
    }

    private SVNRepository getSVNRepositoryInstance() throws SVNException {
        SVNRepository repo = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
        repo.setAuthenticationManager(authManager);
        return repo;
    }

    public String getUrl() {
        return url;
    }

    public RepoAccessKind getRepoAccessKind() {
        return repoAccessKind;
    }

    public SVNCommitInfo addFile(String filePath, InputStream inputStream, String logMessage) throws SVNException {
    	
    	if (pathExist(filePath)){
    		System.out.println("File already exist " + filePath);
    		try {
				commitChangesFromDiff(filePath, new ByteArrayInputStream(getFile(filePath, -1).getBytes()), inputStream, logMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else {
    	
        ISVNEditor editor = null;
        Path path = new Path(filePath);
        String dirName = path.getDir();
        try {
            editor = repository.getCommitEditor(logMessage , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );

            editor.openRoot(HEAD_REV);

            SVNRepository localRepo = getSVNRepositoryInstance();
            String tmpPath = dirName;
            List<String> dirsToCreate = new ArrayList<String>();
            while(localRepo.checkPath(tmpPath, HEAD_REV) == SVNNodeKind.NONE ){
                dirsToCreate.add(tmpPath);
                tmpPath = Path.getParent(tmpPath);
                //reached the root path
                if(Path.isRoot(tmpPath)) {
                    break;
                }
            }
            for(int i = dirsToCreate.size() - 1; i >= 0; i--) {
                String dir = dirsToCreate.get(i);
                editor.addDir(dir, null, HEAD_REV);
            }

            //editor.openDir(dirName, HEAD_REV);
            editor.addFile(filePath, null, HEAD_REV);
            editor.changeFileProperty(filePath, SVNProperty.MIME_TYPE, SVNPropertyValue.create("text/xml"));
            editor.applyTextDelta(filePath, null);
            SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator( );
            String checksum = deltaGenerator.sendDelta(filePath, inputStream, editor, true);
            editor.closeFile(filePath, checksum);
            //closing root in the repository
            editor.closeDir();

            SVNCommitInfo info = editor.closeEdit();
            return info;
        } catch (SVNException e) {
            processSVNException(editor, e);
        }
        }
    	
        return SVNCommitInfo.NULL;
    }

    public SVNCommitInfo deleteFileFromHead(String filePath, String logMessage) throws SVNException {
        ISVNEditor editor = null;
        Path path = new Path(filePath);
        String dirName = path.getDir();
        try {
            editor = repository.getCommitEditor(logMessage , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );

            editor.openRoot(HEAD_REV);
            editor.openDir(dirName, HEAD_REV);

            editor.deleteEntry(filePath, HEAD_REV);

            //closing dirName direcotry in a repository
            editor.closeDir();
            //closing root in the repository
            editor.closeDir();

            SVNCommitInfo info = editor.closeEdit();
            return info;
        } catch (SVNException e) {
            processSVNException(editor, e);
        }
        return SVNCommitInfo.NULL;
    }


    /**
     *
     * @param filePath File path in a repository
     * @param baseData InputStream which represents the file contents before modifying
     * @param workingData InputStream which represents the file contents after modifying
     * @param logMessage log message for commit
     * @throws org.tmatesoft.svn.core.SVNException Any SVNException which might occur
     * @return SVN commit info
     */
    public SVNCommitInfo commitChangesFromDiff(String filePath, InputStream baseData, InputStream workingData, String logMessage) throws SVNException {
        List<CommitTriple> commitTriples = new ArrayList<CommitTriple>(1);
        commitTriples.add(new CommitTriple(filePath, baseData, workingData));
        return commitChangesFromDiff(commitTriples, logMessage);
/*        ISVNEditor editor = null;
        Path path = new Path(filePath);
        String dirName = path.getDir();
        try {
            editor = repository.getCommitEditor(logMessage , null *//*locks*//* , true *//*keepLocks*//* , null *//*mediator*//* );

            editor.openRoot(HEAD_REV);
            editor.openDir(dirName, HEAD_REV);

            editor.openFile(filePath, HEAD_REV);

            editor.applyTextDelta(filePath, null);
            SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator( );
            String checksum = deltaGenerator.sendDelta(filePath, baseData, 0 , workingData, editor, true);
            editor.closeFile(filePath, checksum);

            //closing dirName direcotry in a repository
            editor.closeDir();
            //closing root in the repository
            editor.closeDir();

            SVNCommitInfo info = editor.closeEdit();
            log.info(info.toString());
            return info;
        } catch (SVNException e) {
            processSVNException(editor, e);
        }
        return SVNCommitInfo.NULL;*/
    }

    public SVNCommitInfo commitChangesFromDiff(List<CommitTriple> commitTriples, String logMessage) throws SVNException {
        ISVNEditor editor = null;
        if(commitTriples.isEmpty()) {
        	System.out.println("not empty");
            return SVNCommitInfo.NULL;
        }
        try {
            editor = repository.getCommitEditor(logMessage , null /*locks*/ , true /*keepLocks*/ , null /*mediator*/ );

            editor.openRoot(HEAD_REV);

            for (CommitTriple triple : commitTriples) {
                String filePath = triple.getFilePath();
                editor.openFile(filePath, HEAD_REV);
                editor.applyTextDelta(filePath, null);
                SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator( );

                String checksum = deltaGenerator.sendDelta(filePath, triple.getBaseData(), 0 , triple.getWorkingData(), editor, true);
                editor.closeFile(filePath, checksum);
            }

            editor.closeDir();

            SVNCommitInfo info = editor.closeEdit();
            return info;
        } catch (SVNException e) {
            processSVNException(editor, e);
        }
        return SVNCommitInfo.NULL;
    }

    public boolean pathExist(String path, SVNNodeKind acceptedNodeKind) throws SVNException {
        SVNNodeKind nodeKind = repository.checkPath(path, HEAD_REV);
        if(null == acceptedNodeKind) {
            return nodeKind == SVNNodeKind.DIR || nodeKind == SVNNodeKind.FILE;
        } else {
            return nodeKind == acceptedNodeKind;
        }
    }

    public boolean pathExist(String path) throws SVNException {
        return pathExist(path, null);
    }

    public String getFile(String filePath, long revision) throws SVNException, IOException {
        return getFile(filePath, revision,  false);
    }

    public String
    getFile(String filePath, long revision, boolean allFiles) throws SVNException, IOException {
        SVNNodeKind nodeKind = repository.checkPath(filePath, revision);
        if(nodeKind != SVNNodeKind.FILE) {
            throw new SVNException(SVNErrorMessage.create(SVNErrorCode.ENTRY_NOT_FOUND, String.format("Path %s not found in xSVN under revision %s", filePath, revision)));
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream( );
        SVNProperties fileProperties = new SVNProperties();
        try{
            repository.getFile(filePath, revision, fileProperties, baos);
        } catch(SVNException e) {
            processSVNException(null, e);
        }
        if(!allFiles) {
            return isXmlFile(filePath, fileProperties) ? baos.toString() : null;
        } else {
            return baos.toString();
        }
    }


    public Map<String, String> getFiles(String path, long revision, boolean recursive) throws SVNException, IOException {
    	if(isHead(revision)) {
            throw new IllegalStateException("For retrieving HEAD revision DB XML facilities should be used");
        }
        Map<String, String> map = new HashMap<String, String>();

        path = Path.normalizeDirPath(path);
        try {
            Collection entries = repository.getDir(path, revision, null, (Collection) null );
            for(Object obj : entries){
                SVNDirEntry entry = (SVNDirEntry) obj;
                String fullPath = path + entry.getName();
                if(entry.getKind() == SVNNodeKind.FILE) {
                    String content = getFile(fullPath, revision);
                    if(content != null) {
                        map.put(fullPath, content);
                    }
                } else if(recursive && entry.getKind() == SVNNodeKind.DIR) {
                    map.putAll(getFiles(fullPath + "/", revision, recursive));
                }
            }
        } catch (SVNException e) {
            processSVNException(null, e);
        } 

        return map;
    }

    public long getLatestFileRevision(String filePath, long startRevision) throws SVNException {
        List revsList = new ArrayList();
        try {
            repository.getFileRevisions(filePath, revsList, startRevision, getLatestRevision());
            SVNFileRevision lastFileRevision = (SVNFileRevision) revsList.get(revsList.size() - 1);
            return lastFileRevision.getRevision();
        } catch (SVNException e) {
            processSVNException(null, e);
        }
        return UNDEFINED_REV;
    }

	/**
	 * Vérifie un chemin et definis si c'ets un dossier, un fichier ou rien(si il n'existe pas)
	 * @param path
	 * @return
	 */
	public String check(String path){
		String type = "UNKNOWN";
		SVNNodeKind nodeKind;
		try {
			nodeKind = this.repository.checkPath( path ,  -1 );
			
			if ( nodeKind == SVNNodeKind.NONE ) {
				type = "EMPTY";
			} else if ( nodeKind == SVNNodeKind.FILE ) {
				type = "FILE";
			} else if ( nodeKind == SVNNodeKind.DIR ) {
				type = "FOLDER";	
			} else if ( nodeKind == SVNNodeKind.UNKNOWN ) {
				type = "UNKNOWN";	
			}
		} catch (SVNException e) {
			System.out.println("Impossible de verifier le chemin "+path+" : "+e);
		}

		
		return type;
	}
	
	/**
	 * Liste un dossier du dépot
	 * @param path dossier du dépot
	 * @throws SVNException
	 */
	@SuppressWarnings("rawtypes")
	public List<SvnEntry> list(String path , SvnEntry rootEntry) throws SVNException {
		System.out.println("88888888  Path " + path);
		List<SvnEntry> tree = new ArrayList<SvnEntry>();
		if("FOLDER".equalsIgnoreCase(this.check(path))){
			System.out.println("it's a folder");
			Collection entries = this.repository.getDir( path, -1 , null , (Collection) null );
			Iterator iterator = entries.iterator( );
			while ( iterator.hasNext( ) ) {
				SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );
				System.out.println("***" + entry.getName());
				SvnEntry svnEntry = new SvnEntry();
				svnEntry.setAuthor(entry.getName());
				svnEntry.setDate(entry.getDate());
				svnEntry.setLock(entry.getLock());
				svnEntry.setPath((path.equals( "" ) ? "" : path + "/" ));
				svnEntry.setRevision(entry.getRevision());
				svnEntry.setSize(entry.getSize());
				svnEntry.setParent(rootEntry);
				
				if(entry.getKind() == SVNNodeKind.NONE){
					svnEntry.setType("EMPTY");
				}else if(entry.getKind() == SVNNodeKind.FILE){
					svnEntry.setType("FILE");
				}else if(entry.getKind() == SVNNodeKind.DIR){
					svnEntry.setType("FOLDER");
				}else if(entry.getKind() == SVNNodeKind.UNKNOWN){
					svnEntry.setType("UNKNOWN");
				}
				svnEntry.setUrl(entry.getURL().toString());
				tree.add(svnEntry);

				if (rootEntry != null)
				{
					rootEntry.addChildren(svnEntry);
				}
				
				if ( entry.getKind() == SVNNodeKind.DIR ) {
					System.out.println("this is a folder, check inside" + entry.getPath());
					System.out.println(( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ));
					this.list( ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ), svnEntry );
				}
			}
		}else{
			System.out.println("Impossible de lister "+path+" car ce n'est pas un dossier");
		}

		System.out.println("tree " + tree.size());
		return tree;
	}
    
    //helper functions
    private void processSVNException(ISVNEditor editor, SVNException e) throws SVNException {
            if(editor != null) {
                editor.abortEdit();
            }
            SVNErrorMessage err = e.getErrorMessage();
            /*
             * Display all tree of error messages.
             * Utility method SVNErrorMessage.getFullMessage() may be used instead of the loop.
             */
            while(err != null) {
                err = err.getChildErrorMessage();
            }
            throw e;
    }

    public static boolean isHead(long revision) {
        return revision < 0;
    }

    public static long adaptRevision(long revision) {
        return revision < 0 ? -1 : revision; 
    }

    /*
    This method should be in consistency with xSVN analogue
     */
    private static boolean isXmlFile(String filePath, SVNProperties fileProperties) {
        if(filePath.endsWith(".omdoc") || filePath.endsWith(".xml")) {
            return true;
        }
        String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
        return null != mimeType && (mimeType.equals("text/xml") || mimeType.equals("application/xml"));
    }

}
