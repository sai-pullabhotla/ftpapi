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
package com.myjavaworld.ftp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * A <code>DataConnection</code> object is used to transfer data over the data
 * connection in an FTP process. <code>FTPClient</code> object will initiate a
 * <code>DataConnection</code> based on the commands the send to the remote
 * host. For more details about data connection, refer to the FTP protocol
 * specification (RFC 959).
 */
public class SSLDataConnection extends DataConnection {

	/**
	 * Constructs a <code>DataConnection</code> object.
	 * 
	 * @param client
	 *            <code>FTPClient</code> that created this data connection.
	 */
	public SSLDataConnection(FTPClient client) {
		super(client);
	}

	/**
	 * Binds a server socket on the local host on a free port. This server
	 * socket is used for transmitting data in active mode.
	 * 
	 * @exception ConnectionException
	 *                If could not bind a server.
	 */
	@Override
	public synchronized int bind() throws ConnectionException {
		try {
			SSLContext ctx = client.getSSLContext();
			SSLServerSocketFactory factory = ctx.getServerSocketFactory();
			// server = new ServerSocket(0, 0, client.getLocalAddress());
			server = factory.createServerSocket(0, 0, client.getLocalAddress());
			((SSLServerSocket) server).setUseClientMode(true);
			return server.getLocalPort();
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
	}

	/**
	 * Listenes for connections. This method blocks until a connection is made
	 * or a timeout occurs.
	 * 
	 * @exception ConnectionException
	 *                If a network or IO error occurs.
	 */
	@Override
	public synchronized void accept() throws ConnectionException {
		try {
			server.setSoTimeout(client.getTimeout());
		} catch (SocketException exp) {
			// Let's ignore this.
		}
		try {
			socket = server.accept();
			// ((SSLSocket) socket).setUseClientMode(true);
			// ((SSLSocket) socket).startHandshake();
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
		try {
			socket.setSoTimeout(client.getTimeout());
		} catch (SocketException exp) {
			// Let's ignore this.
		}
		try {
			socket.setSendBufferSize(client.getBufferSize());
			socket.setReceiveBufferSize(client.getBufferSize());
		} catch (SocketException exp) {
			// Let's ignore this.
		}
	}

	/**
	 * Connects to the specified IP address to the specified port number. This
	 * method is called by the FTPClients if they prefer to transfer data in
	 * passive mode.
	 * 
	 * @param ipAddress
	 *            IP address of the remote host in xxx.xxx.xxx.xxx format.
	 * @param port
	 *            Port number to connect to.
	 * @exception ConnectionException
	 *                if a network or IO error occurs.
	 */
	@Override
	public synchronized void connect(String ipAddress, int port)
			throws ConnectionException {
		try {
			if (client.isPassiveIPSubstitutionEnabled()) {
				connect(client.getRemoteAddress(), port);
			} else {
				connect(InetAddress.getByName(ipAddress), port);
			}
		} catch (UnknownHostException exp) {
			throw new ConnectionException(exp.toString());
		}
	}

	/**
	 * Connects to the specified <code>InetAddress</code> to the specified port
	 * number. This method is called by the FTPClients if they prefer to
	 * transfer data in passive mode.
	 * 
	 * @param address
	 *            Ineternet address of the remote host.
	 * @param port
	 *            Port number to connect to.
	 * @exception ConnectionException
	 *                if a network or IO error occurs.
	 */
	@Override
	public synchronized void connect(InetAddress address, int port)
			throws ConnectionException {
		try {
			SSLContext ctx = client.getSSLContext();
			SocketFactory factory = ctx.getSocketFactory();
			socket = factory.createSocket(address, port);
			// socket = new Socket(address, port);
			try {
				socket.setSoTimeout(client.getTimeout());
			} catch (SocketException exp) {
				// Let's ignore this.
			}
			try {
				socket.setSendBufferSize(client.getBufferSize());
				socket.setReceiveBufferSize(client.getBufferSize());
			} catch (SocketException exp) {
				// Let's ignore this.
			}
		} catch (UnknownHostException exp) {
			throw new ConnectionException(exp.toString());
		} catch (NoRouteToHostException exp) {
			throw new ConnectionException(exp.toString());
		} catch (SocketException exp) {
			throw new ConnectionException(exp.toString());
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
	}
}