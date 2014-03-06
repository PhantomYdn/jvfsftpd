package ru.skynet.jvfsftpd.handlers;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.Handler;
import ru.skynet.jvfsftpd.ServerPI;
import ru.skynet.jvfsftpd.config.FtpSession;

public abstract class AbstractHandler implements Handler
{
     protected Log log = LogFactory.getLog(this.getClass());
     
     /**
      * Creates a native absolute path from a path string sent from the client.
      * The absolute path constructed will always be prefixed with baseDir. If
      * ftpPath does not begin with a '/', the constructed path will also be
      * relativee to currentDir.
      */
    
    public boolean requireLogin()
    {
        return true;
    }
    
    protected String getCurrentPath() throws FileSystemException
    {
        return getRelativePath(getSession().getBaseDir(), getSession().getCurrentDir());
    }
    
    protected String getRelativePath(FileObject to) throws FileSystemException
    {
        return getRelativePath(getSession().getCurrentDir(), to);
    }
    
    protected String getRelativePath(FileObject from, FileObject to) throws FileSystemException
    {
        String ret =  from.getName().getRelativeName(to.getName());
        if(ret.equals(".")) return "/";
        else return "/"+ret;
    }
    
    protected FileObject getPath(String ftpPath) throws FileSystemException
    {
        return getPath(getSession().getBaseDir(), getSession().getCurrentDir(), ftpPath);
    }
    
    protected FileObject getPath(FileObject baseDir, FileObject currentDir, String ftpPath) throws FileSystemException
    {
        FileObject prepath = null;
        try
        {
            if(ftpPath==null || ftpPath.equals("")) return currentDir;
            else if(ftpPath.charAt(0)==FileName.SEPARATOR_CHAR)
            {
                int indx = 1;
                for(;indx<ftpPath.length();indx++)
                {
                    if(ftpPath.charAt(indx)!=FileName.SEPARATOR_CHAR) break;
                }
                ftpPath = ftpPath.substring(indx);
                prepath = baseDir.resolveFile(ftpPath);
            }
            else
            {
                prepath = currentDir.resolveFile(ftpPath);
            }
        }
        catch (FileSystemException e)
        {
            return baseDir;
        }
        if(baseDir.getName().isDescendent(prepath.getName()))
        {
           return prepath; 
        }
        else
        {
            return baseDir;
        }
    }
    
    protected FtpSession getSession()
    {
        return FtpSession.getSession();
    } 
    
    protected ServerPI getServerPI()
    {
    	return FtpSession.getSession().getServerPI();
    }
}
