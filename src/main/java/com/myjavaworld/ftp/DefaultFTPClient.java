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

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.swing.event.EventListenerList;

import com.myjavaworld.util.Filter;

/**
 * The default implementation of <code>FTPClient</code>. Works well with most of
 * the UNIX type FTP servers.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class DefaultFTPClient implements FTPClient, FTPConstants {

	/**
	 * Timeout for this <code>FTPClient</code>.
	 */
	protected int timeout = 0;
	/**
	 * Buffer size for transferring data over the network.
	 */
	protected int bufferSize = 0;
	/**
	 * Data representation type. For e.g. ASCII, Binary etc.
	 */
	protected int type = 0;
	/**
	 * Data transfer mode. For e.g. Stream, Block etc.
	 */
	protected int mode = 0;
	/**
	 * Structure. For e.g. File, Record, Page etc.
	 */
	protected int structure = 0;
	/**
	 * Whether or not to open data connections in passive mode.
	 */
	protected boolean passive = false;
	/**
	 * A flag that tells we are still connected to the remote host.
	 */
	protected boolean connected = false;
	/**
	 * A flag that tells if we are logged in to the remote host.
	 */
	protected boolean loggedIn = false;
	/**
	 * Keep track of the current working directory on the remote host.
	 */
	protected RemoteFile workingDirectory = null;
	/**
	 * Stores that last reply received from the remote host.
	 */
	protected String reply = null;
	/**
	 * A Control Connection object for sending commands and receiving replies.
	 */
	protected ControlConnection controlConnection = null;
	/**
	 * A DataConnection object used for transferring data to/from the remote
	 * system.
	 */
	protected DataConnection dataConnection = null;
	/**
	 * A ListParser object used to parse the directory listing produced by the
	 * remote host.
	 */
	protected ListParser listParser = null;
	/**
	 * SSL usage of this ftp client.
	 */
	protected int sslUsage = 0;
	/**
	 * SSL context
	 */
	protected SSLContext sslContext = null;
	/**
	 * A flag to determine if the data channel will be encrypted or not.
	 */
	protected boolean dataChannelUnencrypted = false;
	/**
	 * The SSL protocol to use for negotiating Explicit SSL connections.
	 */
	protected String explicitSSLProtocol = null;
	/**
	 * List of registered listeners.
	 */
	protected EventListenerList listenerList = null;
	/**
	 * Flag that determines if passive connections IP address should be
	 * substituted with the original server's IP address.
	 */
	protected boolean passiveIPSubstitutionEnabled = false;
	/**
	 * The proxy server to use when connecting to the FTP server
	 */
	protected Proxy proxy = null;

	/**
	 * Constructs an <code>DefaultFTPClient</code> object that is not connected
	 * to any host.
	 */
	public DefaultFTPClient() {
		this.timeout = DEFAULT_TIMEOUT;
		this.bufferSize = DEFAULT_BUFFER_SIZE;
		this.type = DEFAULT_TYPE;
		this.mode = DEFAULT_MODE;
		this.structure = DEFAULT_STRUCTURE;
		this.reply = "";
		listenerList = new EventListenerList();
		sslUsage = USE_NO_SSL;
		dataChannelUnencrypted = false;
		explicitSSLProtocol = "SSL";
		passiveIPSubstitutionEnabled = false;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setListParser(ListParser listParser) {
		this.listParser = listParser;
	}

	public ListParser getListParser() {
		return listParser;
	}

	public void setSSLUsage(int sslUsage) {
		this.sslUsage = sslUsage;
	}

	public int getSSLUsage() {
		return sslUsage;
	}

	public void setSSLContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	public SSLContext getSSLContext() {
		return sslContext;
	}

	public void setDataChannelUnencrypted(boolean dataChannelUnencrypted) {
		this.dataChannelUnencrypted = dataChannelUnencrypted;
	}

	public boolean isDataChannelUnencrypted() {
		return dataChannelUnencrypted;
	}

	public boolean isSecured() {
		if (!loggedIn) {
			return false;
		}
		return controlConnection.isSecured();
	}

	public void setExplicitSSLProtocol(String protocol) {
		if (protocol == null) {
			throw new NullPointerException();
		}
		if (protocol.trim().length() == 0) {
			throw new IllegalArgumentException("protocol cannot be empty");
		}
		this.explicitSSLProtocol = protocol;
	}

	public String getExplicitSSLProtocol() {
		return explicitSSLProtocol;
	}

	public synchronized void connect(String host) throws FTPException,
			ConnectionException {
		connect(host, DEFAULT_PORT);
	}

	public synchronized void connect(String host, int port)
			throws FTPException, ConnectionException {
		if (sslUsage == USE_IMPLICIT_SSL) {
			controlConnection = new ImplicitSSLControlConnection(this);
		} else if (sslUsage == USE_EXPLICIT_SSL
				|| sslUsage == USE_SSL_IF_AVAILABLE) {
			controlConnection = new ExplicitSSLControlConnection(this);
		} else {
			controlConnection = new ControlConnection(this);
		}
		controlConnection.connect(host, port);
		// reply = controlConnection.getReply();
		// if (reply.charAt(0) == '5' || reply.charAt(0) == '4')
		// throw new FTPException(reply);
		connected = true;
	}

	public synchronized void connect(FTPHost ftpHost) throws FTPException,
			ConnectionException {
		connect(ftpHost.getHostName(), ftpHost.getPort());
		login(ftpHost.getUserName(), ftpHost.getPassword(),
				ftpHost.getAccount());
	}

	public boolean isConnected() {
		return connected;
	}

	public synchronized void login(String user, String password)
			throws FTPException, ConnectionException {
		login(user, password, "");
	}

	public synchronized void login(String user, String password, String account)
			throws FTPException, ConnectionException {
		executeCommand("USER " + user);
		if (reply.charAt(0) == '3') {
			executeCommand("PASS " + password);
		}
		if (reply.charAt(0) == '3') {
			if (account.trim().length() > 0) {
				executeCommand("ACCT " + account);
			} else {
				throw new FTPException(
						"Account information required to login. ");
			}
		}
		loggedIn = true;
		String connectionMessage = "Connected to " + getRemoteHost() + "/"
				+ getRemoteIPAddress() + "\n";
		// fireConnectionOpened(
		// new FTPConnectionEvent(
		// this,
		// "Connected to "
		// + getRemoteHost()
		// + "/"
		// + getRemoteIPAddress()));
		if (controlConnection.isSecured()) {
			SSLSession session = controlConnection.getSSLSession();
			connectionMessage += "This is a secured FTP session \n"
					+ "Protocol: " + session.getProtocol() + "\n"
					+ "Cipher Suite: " + session.getCipherSuite() + "\n"
					+ "Data Channel Encryption: "
					+ (isDataChannelUnencrypted() ? "OFF" : "ON") + "\n";
		}

		fireConnectionOpened(new FTPConnectionEvent(this, connectionMessage));

		setType(TYPE_ASCII);
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public synchronized RemoteFile setWorkingDirectory(RemoteFile dir)
			throws FTPException, ConnectionException {
		executeCommand("CWD " + dir.getNormalizedPath());
		// Though RFC 959 says that response to CWD command must include
		// The new working directory name, some FTP servers like personal
		// web server do not include the path. So execute PWD command to
		// update the current working directory.
		executeCommand("PWD");
		workingDirectory = listParser
				.createRemoteFile(FTPUtil.parsePath(reply));
		return workingDirectory;
	}

	public synchronized RemoteFile setToParentDirectory() throws FTPException,
			ConnectionException {
		executeCommand("CDUP");
		// Though RFC 959 says that response to CDUP command must include
		// The new working directory name, some FTP servers like personal
		// web server do not include the path. So execute PWD command to
		// update the current working directory.
		executeCommand("PWD");
		workingDirectory = listParser
				.createRemoteFile(FTPUtil.parsePath(reply));
		return workingDirectory;
	}

	public synchronized RemoteFile getWorkingDirectory() throws FTPException,
			ConnectionException {
		// This happens for the first time after login.
		if (workingDirectory == null) {
			executeCommand("PWD");
			workingDirectory = listParser.createRemoteFile(FTPUtil
					.parsePath(reply));
		}
		// Otherwise, we always have the updated working directory.
		return workingDirectory;
	}

	public synchronized void setType(int type) throws FTPException,
			ConnectionException {
		// Send TYPE command only if the current type is not same as
		// the requested type.
		if (this.type != type) {
			executeCommand("TYPE " + FTPUtil.getType(type));
			this.type = type;
		}
	}

	public int getType() {
		return type;
	}

	public synchronized void setStructure(int structure) throws FTPException,
			ConnectionException {
		if (this.structure != structure) {
			executeCommand("STRU " + FTPUtil.getStructure(structure));
			this.structure = structure;
		}
	}

	public int getStructure() {
		return structure;
	}

	public synchronized void setMode(int mode) throws FTPException,
			ConnectionException {
		if (this.mode != mode) {
			executeCommand("MODE " + FTPUtil.getMode(mode));
			this.mode = mode;
		}
	}

	public int getMode() {
		return mode;
	}

	public void setPassive(boolean passive) {
		this.passive = passive;
	}

	public boolean isPassive() {
		return passive;
	}

	public synchronized void createDirectory(RemoteFile dir)
			throws FTPException, ConnectionException {
		executeCommand("MKD " + dir.getPath());
	}

	public synchronized void deleteDirectory(RemoteFile dir)
			throws FTPException, ConnectionException {
		executeCommand("RMD " + dir.getPath());
	}

	public synchronized void deleteFile(RemoteFile file) throws FTPException,
			ConnectionException {
		executeCommand("DELE " + file.getPath());
	}

	public synchronized void delete(RemoteFile path) throws FTPException,
			ConnectionException {
		if (path.isFile()) {
			deleteFile(path);
		} else {
			deleteDirectory(path);
		}
	}

	public synchronized void rename(RemoteFile from, RemoteFile to)
			throws FTPException, ConnectionException {
		executeCommand("RNFR " + from.getPath());
		executeCommand("RNTO " + to.getPath());
	}

	public synchronized void noop() throws FTPException, ConnectionException {
		executeCommand("NOOP");
	}

	public void abort() throws FTPException, ConnectionException {
		if (dataConnection != null) {
			dataConnection.abort();
		}
	}

	public synchronized void reinitialize() throws FTPException,
			ConnectionException {
		executeCommand("REIN");
		loggedIn = false;
	}

	public void disconnect() throws FTPException, ConnectionException {
		try {
			if (dataConnection != null) {
				dataConnection.abort();
			}
			if (controlConnection != null) {
				executeCommand("QUIT");
				controlConnection.close();
			}
		} catch (Exception exp) {
			// Ignore this.
		} finally {
			connected = false;
			loggedIn = false;
			controlConnection = null;
			dataConnection = null;
			fireConnectionClosed(new FTPConnectionEvent(this,
					"Connection Closed. "));
		}
	}

	public void close() {
		try {
			if (dataConnection != null) {
				dataConnection.abort();
			}
			if (controlConnection != null) {
				controlConnection.close();
			}
		} catch (Exception exp) {
			// Ignore this.
		} finally {
			connected = false;
			loggedIn = false;
			controlConnection = null;
			dataConnection = null;
			fireConnectionClosed(new FTPConnectionEvent(this,
					"Connection Closed. "));
		}
	}

	public synchronized void allocate(long bytes) throws FTPException,
			ConnectionException {
		executeCommand("ALLO " + bytes);
	}

	public synchronized void restart(long bytes) throws FTPException,
			ConnectionException {
		executeCommand("REST " + bytes);
	}

	public synchronized String getSystemInfo() throws FTPException,
			ConnectionException {
		return executeCommand("SYST");
	}

	public synchronized String getHelp() throws FTPException,
			ConnectionException {
		return executeCommand("HELP");
	}

	public synchronized void setSiteParameter(String param)
			throws FTPException, ConnectionException {
		executeCommand("SITE " + param);
	}

	public synchronized void mountStructure(String path) throws FTPException,
			ConnectionException {
		executeCommand("SMNT " + path);
	}

	public synchronized String executeCommand(String command)
			throws FTPException, ConnectionException {
		try {
			reply = controlConnection.executeCommand(command);
		} catch (ConnectionException exp) {
			close();
			throw exp;
		}
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
		return reply;
	}

	public InetAddress getRemoteAddress() {
		return controlConnection.getRemoteAddress();
	}

	public String getRemoteHost() {
		return controlConnection.getRemoteHost();
	}

	public String getRemoteIPAddress() {
		return controlConnection.getRemoteIPAddress();
	}

	public int getRemotePort() {
		return controlConnection.getRemotePort();
	}

	public InetAddress getLocalAddress() {
		return controlConnection.getLocalAddress();
	}

	public String getLocalHost() {
		return controlConnection.getLocalHost();
	}

	public String getLocalIPAddress() {
		return controlConnection.getLocalIPAddress();
	}

	public int getLocalPort() {
		return controlConnection.getLocalPort();
	}

	public synchronized RemoteFile[] list() throws FTPException,
			ParseException, ConnectionException {
		setType(TYPE_ASCII);
		if (sslUsage == USE_NO_SSL || dataChannelUnencrypted) {
			dataConnection = new DataConnection(this);
		} else {
			dataConnection = new SSLDataConnection(this);
		}
		if (passive) {
			executeCommand("PASV");
			String ip = FTPUtil.parseAddress(reply);
			int port = FTPUtil.parsePort(reply);
			dataConnection.connect(ip, port);
			executeCommand("LIST");
		} else {
			int port = dataConnection.bind();
			String portCommand = FTPUtil.getPortCommand(getLocalIPAddress(),
					port);
			executeCommand(portCommand);
			executeCommand("LIST");
			dataConnection.accept();
		}
		try {
			RemoteFile[] list = dataConnection.list(workingDirectory);
			reply = controlConnection.getReply();
			dataConnection = null;
			if (reply.charAt(0) == '5' || reply.charAt(0) == 4) {
				throw new FTPException(reply);
			}
			return list;
		} catch (ParseException exp) {
			if (controlConnection != null) {
				reply = controlConnection.getReply();
			}
			dataConnection = null;
			throw exp;
		}
	}

	public synchronized RemoteFile[] list(Filter filter) throws FTPException,
			ParseException, ConnectionException {
		if (filter == null) {
			return list();
		}
		RemoteFile[] f = list();
		if (f == null) {
			return null;
		}
		List<RemoteFile> list = new ArrayList<RemoteFile>(f.length);
		for (int i = 0; i < f.length; i++) {
			if (filter.accept(f[i])) {
				list.add(f[i]);
			}
		}
		RemoteFile[] children = new RemoteFile[list.size()];
		children = list.toArray(children);
		return children;
	}

	public synchronized RemoteFile[] list(RemoteFile dir) throws FTPException,
			ParseException, ConnectionException {
		return null;
	}

	public synchronized RemoteFile[] list(RemoteFile dir, Filter filter)
			throws FTPException, ParseException, ConnectionException {
		return null;
	}

	public synchronized void createFile(RemoteFile file) throws FTPException,
			ConnectionException {
		if (sslUsage == USE_NO_SSL || dataChannelUnencrypted) {
			dataConnection = new DataConnection(this);
		} else {
			dataConnection = new SSLDataConnection(this);
		}
		// dataConnection = new DataConnection(this);
		try {
			if (passive) {
				executeCommand("PASV");
				String ip = FTPUtil.parseAddress(reply);
				int port = FTPUtil.parsePort(reply);
				dataConnection.connect(ip, port);
				executeCommand("STOR " + file.getPath());
			} else {
				int port = dataConnection.bind();
				String portCommand = FTPUtil.getPortCommand(
						getLocalIPAddress(), port);
				executeCommand(portCommand);
				executeCommand("STOR " + file.getPath());
				dataConnection.accept();
			}
			dataConnection.close();
			reply = controlConnection.getReply();
		} finally {
			if (dataConnection != null) {
				dataConnection.close();
			}
			dataConnection = null;
			if (reply.charAt(0) == '5' || reply.charAt(0) == 4) {
				throw new FTPException(reply);
			}
		}
	}

	public synchronized void download(RemoteFile source, File destination,
			int type, boolean append) throws FTPException, ConnectionException {
		setType(type);
		if (sslUsage == USE_NO_SSL || dataChannelUnencrypted) {
			dataConnection = new DataConnection(this);
		} else {
			dataConnection = new SSLDataConnection(this);
		}
		// dataConnection = new DataConnection(this);
		// FTPException ftpException = null;
		boolean ftpException = true;
		String ioException = null;
		try {
			if (passive) {
				executeCommand("PASV");
				String ip = FTPUtil.parseAddress(reply);
				int port = FTPUtil.parsePort(reply);
				dataConnection.connect(ip, port);
				// executeCommand("RETR " + source.getPath());
				executeCommand("RETR " + source.getNormalizedPath());
			} else {
				int port = dataConnection.bind();
				String portCommand = FTPUtil.getPortCommand(
						getLocalIPAddress(), port);
				executeCommand(portCommand);
				// executeCommand("RETR " + source.getPath());
				executeCommand("RETR " + source.getNormalizedPath());
				dataConnection.accept();
			}
			ftpException = false;
			try {
				dataConnection.download(destination, append);
			} catch (IOException exp) {
				// if (dataConnection != null) {
				// dataConnection.close();
				// }
				// throw new FTPException("599 " + exp.getMessage());
				ioException = exp.getMessage();
			}
		} finally {
			if (dataConnection != null) {
				dataConnection.close();
			}
			dataConnection = null;
			if (!ftpException) {
				if (controlConnection != null) {
					reply = controlConnection.getReply();
					if (ioException != null) {
						throw new FTPException("599 " + ioException);
					}
					if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
						throw new FTPException(reply);
					}
				}
			}
		}
	}

	public synchronized void upload(File source, RemoteFile destination,
			int type, boolean append, long skip) throws FTPException,
			ConnectionException {
		setType(type);
		if (sslUsage == USE_NO_SSL || dataChannelUnencrypted) {
			dataConnection = new DataConnection(this);
		} else {
			dataConnection = new SSLDataConnection(this);
		}
		// dataConnection = new DataConnection(this);
		boolean ftpException = true;
		String ioException = null;
		try {
			if (passive) {
				reply = executeCommand("PASV");
				String ip = FTPUtil.parseAddress(reply);
				int port = FTPUtil.parsePort(reply);
				dataConnection.connect(ip, port);
				String command = append ? "APPE " : "STOR ";
				executeCommand(command + destination.getPath());
			} else {
				int port = dataConnection.bind();
				String portCommand = FTPUtil.getPortCommand(
						getLocalIPAddress(), port);
				executeCommand(portCommand);
				String command = append ? "APPE " : "STOR ";
				executeCommand(command + destination.getPath());
				dataConnection.accept();
			}
			ftpException = false;
			try {
				dataConnection.upload(source, skip);
			} catch (IOException exp) {
				// if (dataConnection != null) {
				// dataConnection.close();
				// }
				// throw new FTPException("599 " + exp.getMessage());
				ioException = exp.getMessage();
			}
		} finally {
			if (dataConnection != null) {
				dataConnection.close();
			}
			dataConnection = null;
			if (!ftpException) {
				if (controlConnection != null) {
					reply = controlConnection.getReply();
					if (ioException != null) {
						throw new FTPException("599 " + ioException);
					}
					if (reply.charAt(0) == '5' || reply.charAt(1) == '4') {
						throw new FTPException(reply);
					}
				}
			}
		}
	}

	public void addControlConnectionListener(ControlConnectionListener l) {
		listenerList.add(ControlConnectionListener.class, l);
	}

	public void removeControlConnectionListener(ControlConnectionListener l) {
		listenerList.remove(ControlConnectionListener.class, l);
	}

	public void addDataConnectionListener(DataConnectionListener l) {
		listenerList.add(DataConnectionListener.class, l);
	}

	public void removeDataConnectionListener(DataConnectionListener l) {
		listenerList.remove(DataConnectionListener.class, l);
	}

	public EventListenerList getListenerList() {
		return listenerList;
	}

	public void addFTPConnectionListener(FTPConnectionListener l) {
		listenerList.add(FTPConnectionListener.class, l);
	}

	public void removeFTPConnectionListener(FTPConnectionListener l) {
		listenerList.remove(FTPConnectionListener.class, l);
	}

	public void setPassiveIPSubstitutionEnabled(boolean enable) {
		this.passiveIPSubstitutionEnabled = enable;
	}

	public boolean isPassiveIPSubstitutionEnabled() {
		return passiveIPSubstitutionEnabled;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * Fires the ConnectionOpened event to all registered listeners.
	 * 
	 * @param evt
	 *            connection opened event.
	 */
	protected void fireConnectionOpened(FTPConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FTPConnectionListener.class) {
				((FTPConnectionListener) listeners[i + 1])
						.connectionOpened(evt);
			}
		}
	}

	/**
	 * Fires the connection closed event to all registered listeners.
	 * 
	 * @param evt
	 *            connection closed event.
	 */
	protected void fireConnectionClosed(FTPConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FTPConnectionListener.class) {
				((FTPConnectionListener) listeners[i + 1])
						.connectionClosed(evt);
			}
		}
	}
}