package ru.skynet.jvfsftpd;



import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.vfs.*;

/**
 * This is the server data transfer process. It is responsible for transferring
 * files to and from the client. A separate data socket is created to transfer
 * the data.
 */
public class ServerDTP
{
    /**
     * The ServerPI that uses this DTP.
     */
    private ServerPI serverPI;

    /**
     * The host of the data socket.
     */
    private String dataHost;

    /**
     * The port of the data socket.
     */
    private int dataPort = -1;

    /**
     * The transmission mode to be used. The initial transmission mode is STREAM
     * mode.
     */
    private TransmissionMode transmissionMode = TransmissionMode.STREAM;

    /**
     * The representation being used for transmission. The initial
     * representation type is ASCII.
     */
    private Representation representation = Representation.ASCII;

    private boolean isActive = true;

    private ServerSocket serverSocket = null;

    // private Socket passiveSocket = null;

    // private static final Logger log = Logger.getLogger(ServerDTP.class);
    
    //private FileSystem fileSystem=null;

    /**
     * Creates a server data transfer process for the specified ServerPI.
     */
    public ServerDTP(ServerPI serverPI)
    {
        this.serverPI = serverPI;
//        this.fileSystem=serverPI.fileSystem;
    }

    /**
     * Sets the transmission mode.
     */
    public void setTransmissionMode(TransmissionMode transmissionMode)
    {
        this.transmissionMode = transmissionMode;
    }

    /**
     * Sets the structure.
     */
    public void setDataStructure(char stru)
    {
        // Ignore. Java itself only supports file-structure, so there
        // is no sense adding record-structure support in the server.
    }

    /**
     * @return the representation type used for transmission.
     */
    public Representation getRepresentation()
    {
        return representation;
    }

    /**
     * Sets the representation type used for transmission.
     */
    public void setRepresentation(Representation representation)
    {
        this.representation = representation;
    }

    /**
     * Sets the data port for an active transmission.
     * 
     * @param host
     *            the host name to connect to.
     * @param port
     *            the port number to connect to.
     */
    public void setDataPort(String host, int port)
    {
        dataHost = host;
        dataPort = port;
    }

    private Socket getSocket() throws IOException, CommandException
    {
        if (isActive)
        {
            if (dataPort == -1)
                throw new CommandException(500,
                        "Can't establish data connection: no PORT specified.");
            return new Socket(dataHost, dataPort);
        }
        else
        {
            // if(passiveSocket==null)
            // {
            // passiveSocket = serverSocket.accept();
            // }
            // return passiveSocket;
            return serverSocket.accept();
        }
    }

    private void closeSocket(Socket socket)
    {
        // if(isActive)
        try
        {
            if (socket != null)
            {
                socket.close();
            }
            if (!isActive)
            {
                serverSocket.close();
                isActive = true;
            }
        }
        catch (IOException e)
        {
        }
    }

    public String passivate() throws IOException
    {
        try
        {
            isActive = false;
            serverSocket = new ServerSocket();
            serverSocket.bind(null);
            String IP = serverSocket.getInetAddress().getHostAddress();
            if (IP.equals("0.0.0.0"))
            {
//                IP = "127.0.0.1";
                IP = serverPI.clientSocket.getLocalAddress().getHostAddress();
            }                
            int localPort = serverSocket.getLocalPort();
            String portStr = "" + ((int) (localPort >> 8)) + ","
                    + ((int) localPort % 256);
            String ret = IP.replaceAll("\\.", ",") + "," + portStr;
            return ret;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isActive = true;
            throw e;
        }
    }

    /**
     * Opens the data connection, reads the data according to the current
     * transmission mode, representation type and structure, and writes it into
     * the local file "path".
     */
    public FtpReplay receiveFile(FileObject file) throws CommandException
    {
    	FtpReplay reply = null;
        OutputStream fos = null;
        Socket dataSocket = null;
        try
        {
            if (file.exists())
                throw new CommandException(550, "File exists in that location.");
            if(!file.isWriteable()) throw new CommandException(550, "You havn't write permissions");

            fos = file.getContent().getOutputStream();

            // Connect to User DTP.
            //
            dataSocket = getSocket();

            // Read file contents.
            //
            serverPI.reply(150, "Opening " + representation.getName()
                    + " mode data connection.");
            transmissionMode.receiveFile(dataSocket, fos, representation);
            reply = FtpReplay.createReplay(226, "Transfer complete.");
        }
        catch (ConnectException e)
        {
            throw new CommandException(425, "Can't open data connection.");
        }
        catch (IOException e)
        {
            throw new CommandException(550, "Can't write to file");
        }
        finally
        {
            try
            {
                if (fos != null)
                    fos.close();
                closeSocket(dataSocket);
            }
            catch (IOException e)
            {
            }
        }
        return reply;
    }

    /**
     * Opens the data connection reads the specified local file and writes it to
     * the data socket using the current transmission mode, representation type
     * and structure.
     */
    public FtpReplay sendFile(FileObject file) throws CommandException
    {
        FtpReplay reply = null;
        InputStream fis = null;
        Socket dataSocket = null;
        try
        {
            if (file.getType().equals(FileType.FOLDER))
                throw new CommandException(550, "Not a plain file.");

            fis = file.getContent().getInputStream();

            // Connect to User DTP.
            //
            dataSocket = getSocket();

            // Send file contents.
            //
            serverPI.reply(150, "Opening " + representation.getName()
                    + " mode data connection.");
            transmissionMode.sendFile(fis, dataSocket, representation);
            reply = FtpReplay.createReplay(226, "Transfer complete.");
        }
        catch (FileNotFoundException e)
        {
            // log.debug("No such file: " + e);
            throw new CommandException(550, "No such file.");
        }
        catch (ConnectException e)
        {
            throw new CommandException(425, "Can't open data connection.");
        }
        catch (IOException e)
        {
            // log.debug("Not a regular file: " + e);
            throw new CommandException(553, "Not a regular file.");
        }
        finally
        {
            try
            {
                if (fis != null)
                    fis.close();
                closeSocket(dataSocket);
            }
            catch (IOException e)
            {
            }
        }
        return reply;
    }

    /**
     * Sends a list of file names to the User DTP. Each line contains only the
     * file name, not modification dates and file sizes (see sendList).
     * 
     * @param path
     *            the path of the directory to list.
     */
    public FtpReplay sendNameList(FileObject dir) throws CommandException
    {
    	FtpReplay reply = null;
        Socket dataSocket = null;
        try
        {
            FileObject[] children = dir.getChildren();

            // Connect to User DTP.
            //
            dataSocket = getSocket();
            Representation representation = Representation.ASCII;
            OutputStream outStream = representation.getOutputStream(dataSocket);
            OutputStreamWriter outWriter = new OutputStreamWriter(outStream, "windows-1251");
            PrintWriter writer = new PrintWriter(outWriter);

            // Send file name list.
            //
            serverPI.reply(150, "Opening " + representation.getName()
                    + " mode data connection.");
            for (int i = 0; i < children.length; i++)
            {
                writer.print(children[i].getName().getBaseName());
                writer.print('\n');
            }
            writer.flush();
            reply = FtpReplay.createReplay(226, "Transfer complete.");
        }
        catch (ConnectException e)
        {
            throw new CommandException(425, "Can't open data connection.");
        }
        catch (Exception e)
        {
            throw new CommandException(550, "No such directory.");
        }
        finally
        {
            closeSocket(dataSocket);
        }
        return reply;
    }

    /**
     * Sends a list of files in the specified directory to the User DTP. Each
     * line contains the file name, modification date, file size and other
     * information.
     * 
     * @param path
     *            the path of the directory to list.
     */
    public FtpReplay sendList(FileObject dir) throws CommandException
    {
        FtpReplay reply = null;
        Socket dataSocket = null;
        try
        {
            FileObject[] files = dir.getChildren();
            int numFiles = files != null ? files.length : 0;

            // Connect to User DTP.
            //
            dataSocket = getSocket();
            Representation representation = Representation.ASCII;
            OutputStream outStream = representation.getOutputStream(dataSocket);
            OutputStreamWriter outWriter = new OutputStreamWriter(outStream, "windows-1251");
            PrintWriter writer = new PrintWriter(outWriter);

            // Send long file list.
            //
            serverPI.reply(150, "Opening " + representation.getName()
                    + " mode data connection.");

            // Print the total number of files.
            //
            writer.print("total " + numFiles + "\n");

            // Loop through each file and print its name, size,
            // modification date etc.
            //
            for (int i = 0; i < numFiles; i++)
            {
                FileObject file = files[i];
                listFile(file, writer);
            }

            writer.flush();

            reply = FtpReplay.createReplay(226, "Transfer complete.");
        }
        catch (ConnectException e)
        {
            throw new CommandException(425, "Can't open data connection.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new CommandException(550, "No such directory.");
        }
        finally
        {
            closeSocket(dataSocket);
        }
        return reply;
    }

    /**
     * Lists a single file in long format (including file sizes and modification
     * dates etc.).
     * 
     * @param file
     *            the file to list.
     * @param writer
     *            the writer to print to.
     * @throws FileSystemException 
     */
    private void listFile(FileObject file, PrintWriter writer) throws FileSystemException
    {
        Date date = new Date(file.getContent().getLastModifiedTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd hh:mm",
                Locale.ENGLISH);
        String dateStr = dateFormat.format(date);

        boolean isFolder = file.getType().equals(FileType.FOLDER);
        long size = isFolder?0:file.getContent().getSize();
        String sizeStr = Long.toString(size);
        int sizePadLength = Math.max(8 - sizeStr.length(), 0);
        String sizeField = pad(sizePadLength) + sizeStr;

        writer.print(isFolder ? 'd' : '-');
        writer.print("rwxrwxrwx");
        writer.print("\t");
        // writer.print("\t1");
        writer.print("\t");
        writer.print("ftp\t");
        // writer.print("\t");
        // writer.print("ftp\t");
        // writer.print(" ");
        writer.print(sizeField);
        writer.print(" ");
        writer.print(dateStr);
        writer.print(" ");
        writer.print(file.getName().getBaseName());
        writer.print("\r\n");
    }

    private static String pad(int length)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++)
            buf.append((char) ' ');
        return buf.toString();
    }
}
