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
 * under the License
 */
package com.myjavaworld.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * This class represents a Control Connection as specified in the FTP protocol
 * specification. For more details refer to RFC 959.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class ImplicitSSLControlConnection extends ControlConnection implements
		FTPConstants {

	/**
	 * Creates an instance of <code>ImplicitSSLControlConnection</code>.
	 * 
	 * @param client
	 *            FTP client that created this control connection.
	 */
	public ImplicitSSLControlConnection(FTPClient client) {
		super(client);
		stdout("Implicit Control Connection");
	}

	/**
	 * Connects to the specified remote host on the specified port number.
	 * 
	 * @param host
	 *            Host name or IP address of the remote host.
	 * @param port
	 *            Port number to connect to.
	 * @exception ConnectionException
	 *                If unable to connect to the specified host.
	 */
	@Override
	public void connect(String host, int port) throws ConnectionException,
			FTPException {
		try {
			SSLContext ctx = client.getSSLContext();
			SSLSocketFactory factory = ctx.getSocketFactory();
			socket = factory.createSocket(host, port);
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream(), true);
			((SSLSocket) socket).startHandshake();
		} catch (UnknownHostException exp) {
			throw new ConnectionException(exp.toString());
		} catch (NoRouteToHostException exp) {
			throw new ConnectionException(exp.toString());
		} catch (SocketException exp) {
			throw new ConnectionException(exp.toString());
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		} catch (Exception exp) {
			throw new ConnectionException(exp.toString());
		}
		try {
			socket.setSoTimeout(client.getTimeout());
			// socket.setKeepAlive(true);
		} catch (SocketException exp) {
			// We will ignore this exception as it is not very important even
			// if we failed to set the time out.
			stderr("Could not set timeout for the socket. Original exception "
					+ "below. \n" + exp.getMessage());
		}
		String reply = getReply();
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}

		reply = executeCommand("PBSZ 0");
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
		reply = executeCommand("PROT "
				+ (client.isDataChannelUnencrypted() ? "C" : "P"));
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
	}
}