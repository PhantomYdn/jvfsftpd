package ru.skynet.jvfsftpd;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ru.skynet.jvfsftpd.io.IOTestCase;

@RunWith(Suite.class)
@SuiteClasses( {IOTestCase.class})


public class AllJVFSFTPdTestSuite
{
	public static Test suite() 
	{
        return new JUnit4TestAdapter(AllJVFSFTPdTestSuite.class);
    }

}
