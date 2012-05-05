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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.myjavaworld.ftp.ConnectionException;
import com.myjavaworld.ftp.ControlConnectionEvent;
import com.myjavaworld.ftp.ControlConnectionListener;
import com.myjavaworld.ftp.FTPClient;
import com.myjavaworld.ftp.FTPConnectionEvent;
import com.myjavaworld.ftp.FTPConnectionListener;
import com.myjavaworld.ftp.FTPException;
import com.myjavaworld.ftp.ListParser;
import com.myjavaworld.ftp.RemoteFile;

/**
 * This example demonstrates how to connect to an FTP site and list the contents
 * of a directory.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 1.0
 */
public class DirList {

	/**
	 * A date format object format last modified date of remote files.
	 */
	private static final DateFormat dateFormat = DateFormat
			.getDateTimeInstance();

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
	 * @throws ParseException
	 *             propogated
	 */
	public static void main(String[] args) throws InstantiationException,
			ClassNotFoundException, IllegalAccessException, FTPException,
			ParseException, ConnectionException {

		// Determine the FTPClient implementation that you would like to use.
		// For most FTP servers, the DefaultFTPClient should work. There is
		// another implementation for working with AS/400 (iSeries) FTP servers.
		// The class name for it is com.myjavaworld.ftp.AS400FTPClient.
		// And, of course, you can always write your own implementation either
		// by implementing the FTPClient interface or by extending one of the
		// exsting implementations.

		String ftpClientClassName = "com.myjavaworld.ftp.DefaultFTPClient";
		// String ftpClientClassName = "com.myjavaworld.ftp.AS400FTPClient";

		// Determine the directory list parser that we would like to use in this
		// session. For most FTP servers, the default implementatiom should work
		// fine. There is another implementation provided for handling
		// MS-DOS directory list format. The class name is
		// com.myjavaworld.ftp.DosListPareser. You can always write one of your
		// own implementation if you want to work with an FTP server that uses a
		// list format other than UNIX or DOS. This can be done implementing the
		// com.myjavaworld.ftp.ListParser interface.

		String listParserClassName = "com.myjavaworld.ftp.DefaultListParser";
		// String listParserClassName = "com.myjavaworld.ftp.DosListParser";

		// Use reflection to find the FTP Client class and obtain an instance
		// of FTPClient.
		FTPClient client = (FTPClient) Class.forName(ftpClientClassName)
				.newInstance();

		// Use reflection to find the list parser class and obtain an instance
		// of the list parser.
		ListParser parser = (ListParser) Class.forName(listParserClassName)
				.newInstance();

		// Set the list parser to use with this FTP client.
		client.setListParser(parser);

		// Listen for connection events. This is optional.
		client.addFTPConnectionListener(new FTPConnectionListener() {

			public void connectionOpened(FTPConnectionEvent evt) {
				System.out.println(evt.getMessage());
			}

			public void connectionClosed(FTPConnectionEvent evt) {
				System.out.println(evt.getMessage());
			}
		});

		// Listen for various commands that are sent to the server and the
		// replies received. This is optional.
		client.addControlConnectionListener(new ControlConnectionListener() {

			public void commandSent(ControlConnectionEvent evt) {
				System.out.println(evt.getMessage());
			}

			public void replyReceived(ControlConnectionEvent evt) {
				System.out.println(evt.getMessage());
			}
		});

		client.setPassive(true);

		// Set proxy
		// client.setProxy(new Proxy(Type.SOCKS, new InetSocketAddress(
		// "localhost", 1080)));

		// Connect to the FTP server.
		client.connect("ftp.netscape.com");

		// Login to the FTP server.
		client.login("anonymous", "you@yourcompany.com");

		// Findout the current working directory on the FTP server.
		RemoteFile workingDirectory = client.getWorkingDirectory();

		// print the dir listing.
		list(client);

		// Change the working directory to a different directory. Make sure that
		// this dir exists on the FTP server.
		client.setWorkingDirectory(parser.createRemoteFile(workingDirectory,
				"pub", true));

		// Print the dir list of the new working directory.
		list(client);

		// Close the connection.
		client.disconnect();
	}

	private static void list(FTPClient client) throws FTPException,
			ParseException, ConnectionException {
		RemoteFile[] children = client.list();
		// For each file in the list, print out the attributes, whether the file
		// is a directory or a regular file, the size of the file, the date and
		// the name of the file.
		for (int i = 0; i < children.length; i++) {
			System.out
					.println(children[i].getAttributes()
							+ "\t"
							+ (children[i].isDirectory() ? "<DIR>" : "<FILE>")
							+ "\t"
							+ children[i].getSize()
							+ "\t"
							+ dateFormat.format(new Date(children[i]
									.getLastModified())) + "\t"
							+ children[i].getName());
		}
	}
}