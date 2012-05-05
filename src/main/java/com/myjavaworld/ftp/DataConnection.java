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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.SocketFactory;
import javax.swing.event.EventListenerList;

/**
 * A <code>DataConnection</code> object is used to transfer data over the data
 * connection in an FTP process. <code>FTPClient</code> object will initiate a
 * <code>DataConnection</code> based on the commands the send to the remote
 * host. For more details about data connection, refer to the FTP protocol
 * specification (RFC 959).
 */
public class DataConnection implements FTPConstants {

	/**
	 * A reference to the <code>FTPClient</code> that created this <code>
	 * DataConnection</code>.
	 */
	protected FTPClient client = null;
	/**
	 * A <code>ServerSocket</code> object used in active mode of data transfers.
	 */
	protected ServerSocket server = null;
	/**
	 * A Socket that represents a data connection.
	 */
	protected Socket socket = null;
	/**
	 * An InputStream object for reading binary data either from the remote host
	 * or from the local file system.
	 */
	protected InputStream in = null;
	/**
	 * An OutputStream object used to write binary data to the remote host or to
	 * the local file system.
	 */
	protected OutputStream out = null;
	/**
	 * A List of registered listeners that are interested in receiving
	 * notifications about the activities of this <code>DataConnection</code>.
	 */
	protected EventListenerList listenerList = null;
	/**
	 * A flag for aborting the data transfer.
	 */
	protected boolean abort = false;

	/**
	 * Constructs a <code>DataConnection</code> object.
	 * 
	 * @param client
	 *            <code>FTPClient</code> that created this data connection.
	 */
	public DataConnection(FTPClient client) {
		this.client = client;
		this.listenerList = client.getListenerList();
		this.abort = false;
	}

	/**
	 * Binds a server socket on the local host on a free port. This server
	 * socket is used for transmitting data in active mode.
	 * 
	 * @return the port number to which this server is bound to.
	 * @exception ConnectionException
	 *                If could not bind a server.
	 */
	public synchronized int bind() throws ConnectionException {
		try {
			server = new ServerSocket(0, 0, client.getLocalAddress());
			return server.getLocalPort();
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}
	}

	/**
	 * Listens for connections. This method blocks until a connection is made or
	 * a timeout occurs.
	 * 
	 * @exception ConnectionException
	 *                If a network or IO error occurs.
	 */
	public synchronized void accept() throws ConnectionException {
		try {
			server.setSoTimeout(client.getTimeout());
		} catch (SocketException exp) {
			// Let's ignore this.
		}
		try {
			socket = server.accept();
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
	 *            Internet address of the remote host.
	 * @param port
	 *            Port number to connect to.
	 * @exception ConnectionException
	 *                if a network or IO error occurs.
	 */
	public synchronized void connect(InetAddress address, int port)
			throws ConnectionException {
		try {
			SocketFactory factory = new CustomSocketFactory(client);
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

	/**
	 * Closes this data connection and open streams, if any.
	 */
	public void close() {
		FTPUtil.close(in);
		FTPUtil.close(out);
		FTPUtil.close(socket);
		FTPUtil.close(server);
	}

	/**
	 * Sets the abort flag to <code>true</code>.
	 */
	public void abort() {
		this.abort = true;
	}

	/**
	 * Checks to see if the abort flag was set.
	 * 
	 * @return <code>true</code>, if the abort request was made.
	 *         <code>false</code>, otherwise.
	 */
	public boolean isAborted() {
		return abort;
	}

	/**
	 * Parses the data received over this data connection to an array of
	 * <code>RemoteFile</code> objects.
	 * 
	 * @param dir
	 *            The remote directory for which the listing is being done.
	 * @return An array of <code>RemoteFile</code> objects which are supposedly
	 *         the children of the specified directory <code>dir</code>.
	 * @exception ConnectionException
	 *                If a network or IO error occurs.
	 * @exception ParseException
	 *                if The data can not be parsed to a <code>RemoteFile</code>
	 *                object.
	 */
	public RemoteFile[] list(RemoteFile dir) throws ConnectionException,
			ParseException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()), client.getBufferSize());
			ListParser parser = client.getListParser();
			return parser.parse(dir, reader);
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		} finally {
			FTPUtil.close(reader);
			close();
		}
	}

	/**
	 * Reads the stream over this data connection and saves it in the specified
	 * local file <code>destination</code>.
	 * 
	 * @param destination
	 *            The local file to which the contents of the stream are to be
	 *            stored.
	 * @param append
	 *            Whether or not to append the contents of the stream to the
	 *            local file.
	 * @exception IOException
	 *                if an IO error occurs.
	 */
	public void download(File destination, boolean append) throws IOException {
		abort = false;
		download(destination, append, (client.getType() == TYPE_ASCII));
	}

	/**
	 * Sends the contents of the specified local file <code>src</code> to the
	 * remote host.
	 * 
	 * @param source
	 *            The source for whose contents are to be sent to the remote
	 *            host.
	 * @param skip
	 *            If the value of this parameter is greater than zero, these
	 *            many bytes will be skipped before sending the data.
	 * @exception IOException
	 *                if an IO error occurs.
	 */
	public void upload(File source, long skip) throws IOException {
		abort = false;
		upload(source, skip, (client.getType() == TYPE_ASCII));
	}

	/**
	 * Stores the contents of the stream to the local file <code>destination
	 * </code> in BINARY format.
	 * 
	 * @param destination
	 *            Local file to which the contents read over the stream are to
	 *            be saved.
	 * @param append
	 *            If this flag is true, The local file will be opened in append
	 *            mode. Otherwise, The local file will be overwritten.
	 * @exception IOException
	 *                if an IO error occurs.
	 */
	private void download(File destination, boolean append, boolean ascii)
			throws IOException {
		long totalBytes = 0L;
		try {
			int bufferSize = client.getBufferSize();
			in = new BufferedInputStream(socket.getInputStream(), bufferSize);
			if (ascii) {
				in = new FromNetASCIIInputStream(in);
			}
			out = new BufferedOutputStream(new FileOutputStream(
					destination.getAbsolutePath(), append), bufferSize);
			byte[] bytes = new byte[bufferSize];
			int bytesRead = 0;
			fireDataTransferStarted(new DataConnectionEvent(client,
					DataConnectionEvent.RECEIVE, totalBytes));
			while ((bytesRead = in.read(bytes)) != -1) {
				if (abort) {
					fireDataTransferAborted(new DataConnectionEvent(this,
							DataConnectionEvent.RECEIVE, totalBytes));
					break;
				}
				out.write(bytes, 0, bytesRead);
				totalBytes += bytesRead;
				fireDataTransferProgress(new DataConnectionEvent(client,
						DataConnectionEvent.RECEIVE, totalBytes));
			}
		} finally {
			fireDataTransferFinished(new DataConnectionEvent(client,
					DataConnectionEvent.RECEIVE, totalBytes));
			close();
		}
	}

	/**
	 * Sends the contents of the specified local file <code>src</code> to the
	 * remote host over this data connection in BINARY format.
	 * 
	 * @param source
	 *            Local file whose contents are to be sent to the remote host.
	 * @param skip
	 *            number of bytes to skip in the local file.
	 * @exception IOException
	 *                if an IO error occurs.
	 */
	private void upload(File source, long skip, boolean ascii)
			throws IOException {
		long totalBytes = 0L;
		try {
			int bufferSize = client.getBufferSize();
			in = new BufferedInputStream(new FileInputStream(source),
					bufferSize);
			if (ascii) {
				in = new ToNetASCIIInputStream(in);
			}
			out = new BufferedOutputStream(socket.getOutputStream(), bufferSize);
			byte[] bytes = new byte[bufferSize];
			int bytesRead = 0;
			if (skip > 0) {
				in.skip(skip);
			}
			fireDataTransferStarted(new DataConnectionEvent(client,
					DataConnectionEvent.SEND, totalBytes));
			while ((bytesRead = in.read(bytes)) != -1) {
				if (abort) {
					fireDataTransferAborted(new DataConnectionEvent(this,
							DataConnectionEvent.RECEIVE, totalBytes));
					break;
				}
				out.write(bytes, 0, bytesRead);
				out.flush();
				totalBytes += bytesRead;
				fireDataTransferProgress(new DataConnectionEvent(client,
						DataConnectionEvent.SEND, totalBytes));
			}
		} finally {
			fireDataTransferFinished(new DataConnectionEvent(client,
					DataConnectionEvent.SEND, totalBytes));
			close();
		}
	}

	/**
	 * Notifies the registered listeners that the data transfer has started.
	 * 
	 * @param evt
	 *            <code>DataConnectionEvent</code>.
	 */
	protected void fireDataTransferStarted(DataConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DataConnectionListener.class) {
				((DataConnectionListener) listeners[i + 1])
						.dataTransferStarted(evt);
			}
		}
	}

	/**
	 * Notifies the registered listeners that the data transfer has finished.
	 * 
	 * @param evt
	 *            <code>DataConnectionEvent</code>.
	 */
	protected void fireDataTransferFinished(DataConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DataConnectionListener.class) {
				((DataConnectionListener) listeners[i + 1])
						.dataTransferFinished(evt);
			}
		}
	}

	/**
	 * Notifies the registered listeners that the data transfer has aborted.
	 * 
	 * @param evt
	 *            <code>DataConnectionEvent</code>.
	 */
	protected void fireDataTransferAborted(DataConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DataConnectionListener.class) {
				((DataConnectionListener) listeners[i + 1])
						.dataTransferAborted(evt);
			}
		}
	}

	/**
	 * Notifies the registered listeners that the data transfer is in progress.
	 * 
	 * @param evt
	 *            <code>DataConnectionEvent</code>.
	 */
	protected void fireDataTransferProgress(DataConnectionEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DataConnectionListener.class) {
				((DataConnectionListener) listeners[i + 1])
						.dataTransferProgress(evt);
			}
		}
	}

	/**
	 * Writes the given message to the standard output.
	 * 
	 * @param message
	 *            The message to be written.
	 */
	protected void stdout(String message) {
		System.out.println(message);
	}
}