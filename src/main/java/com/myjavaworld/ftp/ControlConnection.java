/*
 * Copyright 2012 jMethods, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.myjavaworld.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.swing.event.EventListenerList;

/**
 * This class represents a Control Connection as specified in the FTP protocol
 * specification. For more details refer to RFC 959.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class ControlConnection implements FTPConstants {

	/**
	 * <code>FTPClient</code> object that created this
	 * <code>ControlConnection</code>.
	 */
	protected FTPClient client = null;
	/**
	 * A Socket that represents a connection to the remote host.
	 */
	protected Socket socket = null;
	/**
	 * A Reader object for receiving replies from the remote host.
	 */
	protected BufferedReader reader = null;
	/**
	 * A Writer object that sends commands to the remote host.
	 */
	protected PrintStream writer = null;
	/**
	 * List of registered listeners that are willing to get notifications about
	 * the activity of this <code>ControlConnection</code>.
	 */
	protected EventListenerList listenerList = null;

	/**
	 * Constructs a <code>ControlConnection</code> object.
	 * 
	 * @param client
	 *            The <code>FTPClient</code> that created this control
	 *            connection.
	 */
	public ControlConnection(FTPClient client) {
		super();
		this.client = client;
		listenerList = client.getListenerList();
	}

	/**
	 * Connects to the given remote host on the default FTP port as defined in
	 * <code>FTPConstants.DEFAULT_PORT</code>.
	 * 
	 * @param host
	 *            Host name or IP address of the remote host.
	 * @exception ConnectionException
	 *                if unable to connect to the specified host.
	 * @exception FTPException
	 */
	public void connect(String host) throws ConnectionException, FTPException {
		connect(host, DEFAULT_PORT);
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
	 * @exception FTPException
	 */
	public void connect(String host, int port) throws ConnectionException,
			FTPException {
		try {
			SocketFactory factory = new CustomSocketFactory(client);
			socket = factory.createSocket(host, port);
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream(), true);
		} catch (UnknownHostException exp) {
			throw new ConnectionException(exp.toString());
		} catch (NoRouteToHostException exp) {
			throw new ConnectionException(exp.toString());
		} catch (SocketException exp) {
			throw new ConnectionException(exp.toString());
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
		try {
			socket.setSoTimeout(client.getTimeout());
			socket.setKeepAlive(true);
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
	}

	/**
	 * Sends the given command <code>command</code>, to the remote host over
	 * this <code>ControlConnection</code>. All commands will be appended with
	 * telnet end of line (\r\n) characters before sending to the remote host.
	 * 
	 * @param command
	 *            Command to send to the remote host.
	 * @exception ConnectionException
	 *                if a network or IO error occurs while sending the command.
	 */
	public void sendCommand(String command) throws ConnectionException {
		try {
			writer.print(command + EOL);
			// writer.flush();
			if (command.startsWith("PASS ")) {
				fireCommandSent(new ControlConnectionEvent(client,
						"PASS **********"));
			} else {
				fireCommandSent(new ControlConnectionEvent(client, command));
			}
			if (writer.checkError()) {
				throw new IOException("Could not send command: " + command);
			}
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
	}

	// public void sendData(int b) throws ConnectionException {
	// try {
	// writer.write(b);
	// if (writer.checkError()) {
	// throw new IOException(
	// "An error occurred while sending the byte " + b);
	// }
	// }
	// catch (IOException exp) {
	// throw new ConnectionException(exp.toString());
	// }
	// }
	//
	// public void sendUrgentData(int b) throws ConnectionException {
	// try {
	// socket.sendUrgentData(b);
	// }
	// catch (IOException exp) {
	// throw new ConnectionException(exp.toString());
	// }
	// }
	/**
	 * reads a single FTP response from the remote host. If the reponse is a
	 * multi-line reponse, all lines will be read until the response is
	 * completely retrieved.
	 * 
	 * @return response from the remote host.
	 * @exception ConnectionException
	 *                if a network or IO error occurs while reading the
	 *                response.
	 */
	public synchronized String getReply() throws ConnectionException {
		String line = null;
		try {
			line = reader.readLine();
			if (line == null) {
				throw new IOException("Connection Dropped. ");
			}
			String replyCode = "000";
			replyCode = line.substring(0, 3);
			StringBuffer buffer = new StringBuffer();
			buffer.append(line);
			if (line.charAt(3) == '-') {
				do {
					buffer.append(EOL);
					line = reader.readLine();
					buffer.append(line);
				} while (!line.startsWith(replyCode + " "));
			}
			String reply = buffer.toString();
			fireReplyReceived(new ControlConnectionEvent(client, reply));
			return reply;
		} catch (StringIndexOutOfBoundsException exp) {
			line = "000 Invalid Response Received from your FTP server. "
					+ "The actual response is: [" + line + "]";
			fireReplyReceived(new ControlConnectionEvent(client, line));
			return line;
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
	}

	/**
	 * Executes the given command <code>command</code>. This method is similar
	 * to calling the <code>sendCommand</code> and <code>getReply</code> methods
	 * sequentially.
	 * 
	 * @param command
	 *            The command to be sent to the remote host.
	 * @return Reply from the remote host.
	 * @exception ConnectionException
	 *                if a network or IO error occurs.
	 */
	public String executeCommand(String command) throws ConnectionException {
		sendCommand(command);
		return getReply();
	}

	/**
	 * Closes this <code>ControlConnection</code> by closing the socket to the
	 * remote host and its associated strems.
	 * 
	 * @exception IOException
	 *                if a network or IO error occurs while closing this
	 *                <code>ControlConnection</code>.
	 */
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
		if (writer != null) {
			writer.close();
		}
		if (socket != null) {
			socket.close();
		}
		reader = null;
		writer = null;
		socket = null;
	}

	/**
	 * Returns the remote host's <code>InetAddress</code>.
	 * 
	 * @return <code>InetAddress</code> of the remote host.
	 */
	public InetAddress getRemoteAddress() {
		return socket.getInetAddress();
	}

	/**
	 * Returns the fully qualified domain name of the remote host.
	 * 
	 * @return Fully qualified domain name of the remote host.
	 */
	public String getRemoteHost() {
		return socket.getInetAddress().getHostName();
	}

	/**
	 * Returns the IP address of the remote host in xxx.xxx.xxx.xxx format.
	 * 
	 * @return IP address of the remote host.
	 */
	public String getRemoteIPAddress() {
		return socket.getInetAddress().getHostAddress();
	}

	/**
	 * Returns the remote port number to which this
	 * <code>ControlConnection</code> is connected to.
	 * 
	 * @return Remote port number to which this <code>ControlConnection</code>
	 *         is connected to.
	 */
	public int getRemotePort() {
		return socket.getPort();
	}

	/**
	 * Returns the <code>InetAddress</code> of the local host.
	 * 
	 * @return <code>InetAddress</code> of the local host.
	 */
	public InetAddress getLocalAddress() {
		return socket.getLocalAddress();
	}

	/**
	 * Returns the host name of the local host.
	 * 
	 * @return Host name of the local host.
	 */
	public String getLocalHost() {
		return socket.getLocalAddress().getHostName();
	}

	/**
	 * Returns the IP address of the local host.
	 * 
	 * @return IP address of the local host.
	 */
	public String getLocalIPAddress() {
		return socket.getLocalAddress().getHostAddress();
	}

	/**
	 * Retruns the local port number to which this <code>ControlConnection
	 * </code> is connected to.
	 * 
	 * @return Local port.
	 */
	public int getLocalPort() {
		return socket.getLocalPort();
	}

	/**
	 * Tells whether or not this <code>ControlConnection</code> is secured.
	 * 
	 * @return <code>true</code>, if this connection is secured.
	 *         <code>false</code>, otherwise.
	 */
	public boolean isSecured() {
		if (socket != null) {
			if (socket instanceof SSLSocket) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the SSLSession associated with this connection.
	 * 
	 * @return SSL Session associated with this connection. Returns null, if
	 *         this connection is not a secured connection.
	 */
	public SSLSession getSSLSession() {
		if (!isSecured()) {
			return null;
		}
		return ((SSLSocket) socket).getSession();
	}

	/**
	 * Used to notify registered listeners that a command was sent to the remote
	 * host. The command is wrapped in the <code>ControlConnectionEvent
	 * </code> object.
	 * 
	 * @param evt
	 *            <code>ControlConnectionEvent</code>.
	 */
	protected void fireCommandSent(ControlConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ControlConnectionListener.class) {
				((ControlConnectionListener) listeners[i + 1]).commandSent(evt);
			}
		}
	}

	/**
	 * Used to notify the registered listeners that a reply was received from
	 * the remote host.
	 * 
	 * @param evt
	 *            <code>ControlConnectionEvent</code>.
	 */
	protected void fireReplyReceived(ControlConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ControlConnectionListener.class) {
				((ControlConnectionListener) listeners[i + 1])
						.replyReceived(evt);
			}
		}
	}

	/**
	 * Prints a given string to the standard output.
	 * 
	 * @param message
	 *            The message to print.
	 */
	protected void stdout(String message) {
		System.out.println(message);
	}

	/**
	 * Prints the given string to standard error.
	 * 
	 * @param message
	 *            The message to print.
	 */
	protected void stderr(String message) {
		System.err.println(message);
	}
}