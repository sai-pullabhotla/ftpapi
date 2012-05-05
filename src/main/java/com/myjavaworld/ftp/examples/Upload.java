/*
 * Copyright 2012 jMethods, Inc. 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * 
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License. 
 */
package com.myjavaworld.ftp.examples;

import java.io.File;

import com.myjavaworld.ftp.ConnectionException;
import com.myjavaworld.ftp.FTPClient;
import com.myjavaworld.ftp.FTPConstants;
import com.myjavaworld.ftp.FTPException;
import com.myjavaworld.ftp.ListParser;
import com.myjavaworld.ftp.RemoteFile;

/**
 * This example demonstrates how to connect to an FTP site and upload a file to
 * the FTP server.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 1.0
 */
public class Upload {

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
		String className = "com.myjavaworld.ftp.DefaultFTPClient";

		// Use reflection to find the class and obtain an instance of the class.
		FTPClient client = (FTPClient) Class.forName(className).newInstance();

		// Determine the directory list parser that we would like to use in this
		// session. For most FTP servers, the default implementatiom should work
		// fine.
		ListParser parser = (ListParser) Class.forName(
				"com.myjavaworld.ftp.DefaultListParser").newInstance();

		// Set the list parser to use with the FTP client.
		client.setListParser(parser);

		// Connect to the FTP server.
		System.out.println("Connecting...");

		// Change the host name to one of your FTP servers in which you have
		// write permissions.
		client.connect("ftp.netscape.com");
		System.out.println("Connected. ");

		// Login to the FTP server.
		System.out.println("Logging in...");
		client.login("anonymous", "you@yourcompany.com");
		System.out.println("Logged in. ");

		// Upload a file
		File source = new File("/Users/sai/temp/NSSetup.exe");
		RemoteFile destination = parser.createRemoteFile(
				"/pub/netscape7/english/7.1/windows/win32/NSSetup.exe", false);
		System.out.println("Uploading: " + source);
		client.upload(source, destination, FTPConstants.TYPE_BINARY, false, 0L);
		System.out.println("Done. ");

		// Disconnect from the FTP server.
		System.out.println("Disconnecting...");
		client.disconnect();
		System.out.println("Disconnected. ");
	}
}