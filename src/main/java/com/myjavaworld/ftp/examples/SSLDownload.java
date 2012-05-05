/*
 * Copyright 2005 jMethods, Inc. All rights reserved.
 * jMethods PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.myjavaworld.ftp.examples;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

import com.myjavaworld.ftp.ConnectionException;
import com.myjavaworld.ftp.ControlConnectionEvent;
import com.myjavaworld.ftp.ControlConnectionListener;
import com.myjavaworld.ftp.FTPClient;
import com.myjavaworld.ftp.FTPConstants;
import com.myjavaworld.ftp.FTPException;
import com.myjavaworld.ftp.ListParser;
import com.myjavaworld.ftp.RemoteFile;

/**
 * This example demonstrates how to connect to an FTP site and download a file
 * to the local system using SSL. This example assumes that the server is
 * already set up for FTP over SSL. In order for this example to work, you must
 * have server's certificate installed in <java_home>/lib/security/jssecacerts
 * file or <java_home>/lib/security/cacerts file. For more information on this
 * and to learn overriding procedures, please refer to the JSSE Reference Guilde
 * that ships with JavaDoc 1.4.x. Or visit the URL
 * http://java.sun.com/j2se/1.4.2/docs/guide/security/jsse/JSSERefGuide.html.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 1.0
 */
public class SSLDownload {

	/**
	 * @param args
	 *            command line arguments.
	 * @throws InstantiationException
	 *             propogated
	 * @throws ClassNotFoundException
	 *             propogated
	 * @throws IllegalAccessException
	 *             propogated
	 * @throws FTPException
	 *             propogated
	 * @throws ConnectionException
	 *             propogated
	 */
	public static void main(String[] args) throws InstantiationException,
			ClassNotFoundException, IllegalAccessException, FTPException,
			ConnectionException {

		// Determine the FTPClient implementation that you would like to use.
		// For most FTP servers, the DefaultFTPClient should work.
		final String className = "com.myjavaworld.ftp.DefaultFTPClient";
		// final String className = "com.myjavaworld.ftp.AS400FTPClient";

		final String hostName = "hostname_or_ipaddress";

		final String user = "login_name";

		final String password = "password";

		// Use reflection to find the class and obtain an instance of the class.
		FTPClient client = (FTPClient) Class.forName(className).newInstance();

		// Add a ControlConnectionListener to look at the client-server
		// conversation. This is optional.
		client.addControlConnectionListener(new ControlConnectionListener() {

			public void commandSent(ControlConnectionEvent evt) {
				System.out.println("Command    " + evt.getMessage());
			}

			public void replyReceived(ControlConnectionEvent evt) {
				System.out.println("Reply      " + evt.getMessage());
			}
		});

		// Determine the directory list parser that we would like to use in this
		// session. For most FTP servers, the default implementatiom should work
		// fine.
		ListParser parser = (ListParser) Class.forName(
				"com.myjavaworld.ftp.DefaultListParser").newInstance();

		// Set the list parser to use with the FTP client.
		client.setListParser(parser);

		// Configure for SSL connection.
		client.setSSLUsage(FTPConstants.USE_EXPLICIT_SSL);
		client.setSSLContext(createSSLContext());

		// Connect to the FTP server.
		System.out.println("Connecting...");
		client.connect(hostName);
		System.out.println("Connected. ");

		// Login to the FTP server.
		System.out.println("Logging in...");
		client.login(user, password);
		System.out.println("Logged in. ");

		// Download a file
		RemoteFile source = parser.createRemoteFile("/remote/test.txt", false);
		File destination = new File("/local/test.txt");
		System.out.println("Downloading: " + source);
		client.download(source, destination, FTPConstants.TYPE_ASCII, false);
		System.out.println("Done. ");

		// Disconnect from the FTP server.
		System.out.println("Disconnecting...");
		client.disconnect();
		System.out.println("Disconnected. ");
	}

	/**
	 * Creates and returns a default SSLContext.
	 * 
	 * @return Default SSL Context.
	 */
	private static SSLContext createSSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, null, new SecureRandom());
			return context;
		} catch (KeyManagementException e) {
			System.err.println("Failed to initialize SSLContext");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Failed to initialize SSLContext");
			e.printStackTrace();
		}
		return null;
	}
}