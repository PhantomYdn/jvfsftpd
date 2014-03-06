package ru.skynet.jvfsftpd.handlers;

import java.util.StringTokenizer;

import org.apache.commons.vfs.FileSystemException;

import ru.skynet.jvfsftpd.CommandException;
import ru.skynet.jvfsftpd.FtpReplay;

/**
 * Command handler for the PORT command.
 * 
 * <pre>
 * PORT %SP% %host-port% %CRLF%
 * </pre>
 */

public class PortHandler extends AbstractHandler
{

    public FtpReplay handle(String command, String params)
            throws CommandException, FileSystemException
    {
        String portStr = params;
        StringTokenizer st = new StringTokenizer(portStr, ",");
        String h1 = st.nextToken();
        String h2 = st.nextToken();
        String h3 = st.nextToken();
        String h4 = st.nextToken();
        int p1 = Integer.parseInt(st.nextToken());
        int p2 = Integer.parseInt(st.nextToken());

        String dataHost = h1 + "." + h2 + "." + h3 + "." + h4;
        int dataPort = (p1 << 8) | p2;

        getServerPI().dtp.setDataPort(dataHost, dataPort);

        return FtpReplay.createReplay(200, "{0} command successful.",command);
    }

}
