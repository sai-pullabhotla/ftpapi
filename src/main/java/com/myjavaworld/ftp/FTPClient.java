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
import java.net.InetAddress;
import java.net.Proxy;
import java.text.ParseException;

import javax.net.ssl.SSLContext;
import javax.swing.event.EventListenerList;

import com.myjavaworld.util.Filter;

/**
 * A contract for <code>FTPClient</code> implementation classes.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface FTPClient {

	/**
	 * Sets the timeout for this <code>FTPClient</code> to the specified
	 * <code>timeout</code>. Timeout is the amount of time an FTPClient waits
	 * for a server's reponse. If the server does not respond with in the
	 * timeout period, Implementations should close the connection. The timeout
	 * must be set before calling the <code>connect</code> methods. If an FTP
	 * client is already connected to a remote host, the new time out may be
	 * ignored by implementation classes.
	 * 
	 * @param timeout
	 *            Timeout in milli seconds.
	 */
	public void setTimeout(int timeout);

	/**
	 * Returns the timeout setting of this FTP client.
	 * 
	 * @return Timeout of this <code>FTPClient</code>.
	 */
	public int getTimeout();

	/**
	 * Sets the buffer size to use for transferring data over the data
	 * connection.
	 * 
	 * @param bufferSize
	 *            Buffer size to use for transferring data over the data
	 *            connection.
	 */
	public void setBufferSize(int bufferSize);

	/**
	 * Returns the current buffer size being used by this <code>FTPClient</code>
	 * .
	 * 
	 * @return Current buffer size in use.
	 */
	public int getBufferSize();

	/**
	 * Sets the <code>ListParser</code> to the specified <code>listParser</code>
	 * .<code>ListParser</code> objects are used to parsre the contents of a
	 * remote directory in to <code>RemoteFile</code> objects.
	 * 
	 * @param listParser
	 *            The <code>ListParser</code> to use to parse the contents of a
	 *            remote directory.
	 */
	public void setListParser(ListParser listParser);

	/**
	 * Returns the current <code>ListParser</code> in use by this <code>
	 * FTPClient</code>.
	 * 
	 * @return <code>ListParser</code> in use.
	 */
	public ListParser getListParser();

	/**
	 * Sets the SSL usage of this client to the given value. The possible values
	 * are:
	 * <ul>
	 * <li>FTPConstants.USE_NO_SSL</li>
	 * <li>FTPConstants.USE_SSL_IF_AVAILABLE</li>
	 * <li>FTPConstants.USE_EXPLICIT_SSL</li>
	 * <li>FTPConstants.USE_IMPLICIT_SSL</li>
	 * </ul>
	 * 
	 * @param sslUsage
	 *            SSL usage parameter to set.
	 */
	public void setSSLUsage(int sslUsage);

	/**
	 * Returns the SSL usage of this FTPClient.
	 * 
	 * @return SSL usage of this FTPClient. Possible values are:
	 *         <ul>
	 *         <li>FTPConstants.USE_NO_SSL</li>
	 *         <li>FTPConstants.USE_SSL_IF_AVAILABLE</li>
	 *         <li>FTPConstants.USE_EXPLICIT_SSL</li>
	 *         <li>FTPConstants.USE_IMPLICIT_SSL</li>
	 *         </ul>
	 */
	public int getSSLUsage();

	/**
	 * Sets the SSL protocol to use when negotiating an Explicit SSL connection.
	 * The specified <code>protocol</code> string will be sent (to the FTP
	 * server) as a parameter to the <code>AUTH</code> command. Valid values are
	 * "SSL" and "TLS", however, this method does not restrict clients from
	 * using other values.
	 * 
	 * @param protocol
	 *            the security protocol to use when negotiating an Explicit SSL
	 *            connection.
	 */
	public void setExplicitSSLProtocol(String protocol);

	/**
	 * Returns the SSL protocol that is in use for negotiating Explicit SSL
	 * connections.
	 * 
	 * @return the SSL protocol that is in use for negotiating Explicit SSL
	 *         connections. If the
	 *         <code>setExplicitSSLProtocol(string protocol)</code> method is
	 *         not called, this method returns the default value "SSL".
	 */
	public String getExplicitSSLProtocol();

	/**
	 * Sets the SSLContext of this FTPClient to the given context.
	 * 
	 * @param context
	 *            SSLContext
	 */
	public void setSSLContext(SSLContext context);

	/**
	 * Gets the SSLContext of this FTPclient.
	 * 
	 * @return SSLContext of this FTPClient. May return null, if no context was
	 *         set.
	 */
	public SSLContext getSSLContext();

	/**
	 * Sets/unsets the flag to encrypt the data channel.
	 * 
	 * @param dataChannelUnencrypted
	 *            whether or not to encrypt the data channel.
	 */
	public void setDataChannelUnencrypted(boolean dataChannelUnencrypted);

	/**
	 * Tells whether or not the data channel encryption is ON.
	 * 
	 * @return <code>true</code>, if the data channel encryption os ON,
	 *         <code>false</code>, otherwise.
	 */
	public boolean isDataChannelUnencrypted();

	/**
	 * Tells whether this connection is secured.
	 * 
	 * @return <code>true</code>, if this connection is secured.
	 *         <code>false</code>, othherwise.
	 */
	public boolean isSecured();

	/**
	 * Connects to the specified remote host <code>host</code> on the default
	 * FTP port.
	 * 
	 * @param host
	 *            Host name or IP address of the remote host.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void connect(String host) throws FTPException, ConnectionException;

	/**
	 * Connects to the specfied remote host <code>host</code>, on the specified
	 * port number <code>port</code>.
	 * 
	 * @param host
	 *            Host name or IP address of the remote host.
	 * @param port
	 *            Port number to connect to.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void connect(String host, int port) throws FTPException,
			ConnectionException;

	/**
	 * Connects and logs in to the FTP host.
	 * 
	 * @param ftpHost
	 *            Remote FTP host to connect to.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void connect(FTPHost ftpHost) throws FTPException,
			ConnectionException;

	/**
	 * Checks whether or not this <code>FTPClient</code> is still connected to
	 * to the remote host.
	 * 
	 * @return <code>true</code>, if this <code>FTPClient</code> is still
	 *         connected to the remote host. Otherwise, <code>false</code>.
	 */
	public boolean isConnected();

	/**
	 * Logs in to the remote host with the specified user id and password.
	 * 
	 * @param user
	 *            User ID.
	 * @param password
	 *            Password.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void login(String user, String password) throws FTPException,
			ConnectionException;

	/**
	 * Logs in to the remote host with the specified user ID, pasword and
	 * account. Not all FTP servers need Account information.
	 * 
	 * @param user
	 *            User ID.
	 * @param password
	 *            Password.
	 * @param account
	 *            Account Name.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void login(String user, String password, String account)
			throws FTPException, ConnectionException;

	/**
	 * Checks wherher or not this client is logged in.
	 * 
	 * @return <code>true</code>, if this client is logged in to the remote
	 *         host. <code>false</code> otherwise.
	 */
	public boolean isLoggedIn();

	/**
	 * Sets the remote working directory to the specified directory
	 * <code>dir</code>. Clients must send a <code>PWD</code> command after
	 * sending the <code>CWD</code> command to make sure that the working
	 * directory has changed.
	 * 
	 * @param dir
	 *            New remote working directory to set.
	 * @return Working directory after the execution of this method.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public RemoteFile setWorkingDirectory(RemoteFile dir) throws FTPException,
			ConnectionException;

	/**
	 * Sets the remote working directory to the parent directory of the current
	 * working directory.
	 * 
	 * @return working directory after the execution of this method.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public RemoteFile setToParentDirectory() throws FTPException,
			ConnectionException;

	/**
	 * Returns the current working directory.
	 * 
	 * @return Current remote working directory.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public RemoteFile getWorkingDirectory() throws FTPException,
			ConnectionException;

	/**
	 * Sets the data representation type to the given <code>type</code>.
	 * 
	 * @param type
	 *            New data representation type to set. Possible types are -
	 *            <ul>
	 *            <li><code>FTPConstants.TYPE_ASCII</code></li>
	 *            <li><code>FTPConstants.TYPE_BINARY</code></li>
	 *            <li><code>FTPConstants.TYPE_IMAGE</code></li>
	 *            <li><code>FTPConstants.TYPE_EBCDIC</code></li>
	 *            <li><code>FTPConstants.TYPE_LOCAL</code></li>
	 *            </ul>
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void setType(int type) throws FTPException, ConnectionException;

	/**
	 * Returns the current data representation type in use.
	 * 
	 * @return current data representation type.
	 */
	public int getType();

	/**
	 * Sets the structure to the specified <code>structure</code>.
	 * 
	 * @param structure
	 *            New structure to set. Possible structure type are -
	 *            <ul>
	 *            <li><code>FTPConstants.STRUCTURE_FILE</code></li>
	 *            <li><code>FTPConstants.STRUCTURE_RECORD</code></li>
	 *            <li><code>FTPConstants.STRUCTURE_PAGE</code></li>
	 *            </ul>
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void setStructure(int structure) throws FTPException,
			ConnectionException;

	/**
	 * Returns the current structure in use.
	 * 
	 * @return current structure.
	 */
	public int getStructure();

	/**
	 * Sets the mode in which data is transferred to the given <code>mode</code>
	 * .
	 * 
	 * @param mode
	 *            The new mode to set. Possible modes are -
	 *            <ul>
	 *            <li><code>FTPConstants.MODE_STREAM</code></li>
	 *            <li><code>FTPConstants.MODE_BLOCK</code></li>
	 *            <li><code>FTPConstants.MODE_COMPRESSED</code></li>
	 *            </ul>
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void setMode(int mode) throws FTPException, ConnectionException;

	/**
	 * Returns the current mode in use.
	 * 
	 * @return Current mode in use.
	 */
	public int getMode();

	/**
	 * If <code>true</code>, marks that the data is to be transgerrred over a
	 * passive data connection.
	 * 
	 * @param passive
	 *            Value <code>true</code> marks that the data is to be
	 *            transferred over a passive data connection. Otherwise, over an
	 *            active data connection.
	 */
	public void setPassive(boolean passive);

	/**
	 * Tells whether or not the data connectons are opned in passive mode. This
	 * method will be consulted every time when there a need to open a data
	 * connection.
	 * 
	 * @return <code>true</code> if in passive mode. Otherwise,
	 *         <code>false</code>.
	 */
	public boolean isPassive();

	/**
	 * Creates the specified remote directory <code>dir</code> on the remote
	 * host.
	 * 
	 * @param dir
	 *            New directory to create.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void createDirectory(RemoteFile dir) throws FTPException,
			ConnectionException;

	/**
	 * Creates an empty file with the given name on the remote host.
	 * 
	 * @param file
	 *            Path and name of the file.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void createFile(RemoteFile file) throws FTPException,
			ConnectionException;

	/**
	 * Deletes the specified directory <code>dir</code> on the remote system.
	 * Most FTP servers would not allow the deletion of a directory unless it is
	 * empty. It is the responsibility of the clients to delete the children of
	 * the directory before attempting to delete a directory.
	 * 
	 * @param dir
	 *            Directory to delete.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void deleteDirectory(RemoteFile dir) throws FTPException,
			ConnectionException;

	/**
	 * Deletes the specified file, <code>file</code> from the remote system.
	 * 
	 * @param file
	 *            File to be deleted.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void deleteFile(RemoteFile file) throws FTPException,
			ConnectionException;

	/**
	 * Deletes the specified <code>path</code> from the remote system. If the
	 * <code>path</code> is a directory (as determined by <code>
	 * RemoteFile.isDirectory()</code>) , then <code>deleteDirectory()</code>
	 * method will be called with <code>path</code> as an argument. Otherwise,
	 * <code>deleteFile()</code> method will be called with <code>path</code> as
	 * an argument.
	 * 
	 * @param path
	 *            The file to delete.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void delete(RemoteFile path) throws FTPException,
			ConnectionException;

	/**
	 * Renames a file or directory, <code>from</code> to <code>to</code>.
	 * 
	 * @param from
	 *            The file to be renamed.
	 * @param to
	 *            New file name.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void rename(RemoteFile from, RemoteFile to) throws FTPException,
			ConnectionException;

	/**
	 * Sends a No-Operation command to the remote system.
	 * 
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void noop() throws FTPException, ConnectionException;

	/**
	 * Sends an <code>abort</code> command to the remote host.
	 * 
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void abort() throws FTPException, ConnectionException;

	/**
	 * Sends a reinitialize command to the remote host. Clients may need to
	 * login after executing this method.
	 * 
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void reinitialize() throws FTPException, ConnectionException;

	/**
	 * Sends a quit command to the remote system and closes the control
	 * connection and data connection associated with this
	 * <code>FTPClient</code>.
	 * 
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void disconnect() throws FTPException, ConnectionException;

	/**
	 * Closes the control and data connections associated with this
	 * <code>FTPClient</code>. This method should usually be called after a
	 * network error ocuurs in order to clean up the resources. Under normal
	 * conditions, the clients are recommended to call <code>disconnect</code>
	 * method.
	 */
	public void close();

	/**
	 * Allocates the specified number of bytes on the remote system.
	 * 
	 * @param bytes
	 *            Number of bytes to allocate for this <code>FTPClient</code>.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void allocate(long bytes) throws FTPException, ConnectionException;

	/**
	 * Sends a RESTART command to the remote host with the specified number of
	 * bytes.
	 * 
	 * @param bytes
	 *            number of bytes to skip in the Input/Output stream.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void restart(long bytes) throws FTPException, ConnectionException;

	/**
	 * Returns the remote host's information by executing the <code>SYST</code>
	 * command.
	 * 
	 * @return remote host's system information. The informaion returned will
	 *         vary from FTP server to server.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public String getSystemInfo() throws FTPException, ConnectionException;

	/**
	 * Executes the <code>HELP</code> command on the remote host and returns the
	 * response back.
	 * 
	 * @return Response to HELP command.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public String getHelp() throws FTPException, ConnectionException;

	/**
	 * Sends a <code>SITE</code> command with the specified parameter(s).
	 * 
	 * @param param
	 *            Parameters or the SITE command.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void setSiteParameter(String param) throws FTPException,
			ConnectionException;

	/**
	 * Sends a SMNT command with the specified path to mount.
	 * 
	 * @param path
	 *            New path structure to mount.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void mountStructure(String path) throws FTPException,
			ConnectionException;

	/**
	 * Executes the given command returns the response back.
	 * 
	 * @param command
	 *            Command to execute.
	 * @return Response to the command sent.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public String executeCommand(String command) throws FTPException,
			ConnectionException;

	/**
	 * Returns the Address of the remote host.
	 * 
	 * @return InetAddress of the remote host.
	 */
	public InetAddress getRemoteAddress();

	/**
	 * Returns the fully qaulified domain name of the remote host.
	 * 
	 * @return domain name or IP address of the remote host.
	 */
	public String getRemoteHost();

	/**
	 * Returns the IP address of the remote host in xxx.xxx.xxx.xxx format.
	 * 
	 * @return IP address of the remote host.
	 */
	public String getRemoteIPAddress();

	/**
	 * Returns the remote port number to which the ControlConnection is
	 * established.
	 * 
	 * @return port number.
	 */
	public int getRemotePort();

	/**
	 * Returns the InetAddress of the local host.
	 * 
	 * @return InetAddress of the local host.
	 */
	public InetAddress getLocalAddress();

	/**
	 * Returns the host name or IP address of the local host.
	 * 
	 * @return Host name or IP address of the local host.
	 */
	public String getLocalHost();

	/**
	 * Returns the IP address of the local host in xxx.xxx.xxx.xxx format.
	 * 
	 * @return IP address of the local host.
	 */
	public String getLocalIPAddress();

	/**
	 * Returns the local port number used by the control connection.
	 * 
	 * @return Local port number.
	 */
	public int getLocalPort();

	/**
	 * Retrieves the contents (children) of the current working directory.
	 * 
	 * @return Direct children of the current working directory.
	 * @exception FTPException
	 * @exception ParseException
	 * @exception ConnectionException
	 */
	public RemoteFile[] list() throws FTPException, ParseException,
			ConnectionException;

	/**
	 * Retrieves the children of the current working directory. The contents
	 * will be filtered using the given filter.
	 * 
	 * @param filter
	 *            the filter to use.
	 * @return direct children of the working directory.
	 * @throws FTPException
	 * @throws ParseException
	 * @throws ConnectionException
	 */
	public RemoteFile[] list(Filter filter) throws FTPException,
			ParseException, ConnectionException;

	/**
	 * Returns the direct children of the specified directory.
	 * 
	 * @param dir
	 *            directory whose children are to be retrieved
	 * @return Direct children of the specified directory <code>dir</code>.
	 * @exception FTPException
	 * @exception ParseException
	 * @exception ConnectionException
	 */
	public RemoteFile[] list(RemoteFile dir) throws FTPException,
			ParseException, ConnectionException;

	/**
	 * Returns the direct children of the specified directory.
	 * 
	 * @param dir
	 *            directory whose children are to be retrieved.
	 * @param filter
	 *            Fiter to apply.
	 * @return Direct children of the given directory.
	 * @throws FTPException
	 * @throws ParseException
	 * @throws ConnectionException
	 */
	public RemoteFile[] list(RemoteFile dir, Filter filter)
			throws FTPException, ParseException, ConnectionException;

	/**
	 * Copies the contents of the <code>source</code> to the local file
	 * <code>destination</code>.
	 * 
	 * @param source
	 *            Remote file to be copied or downloaded.
	 * @param destination
	 *            Local file to which the contents are to be copied.
	 * @param type
	 *            Data representation type to use for data transfer.
	 * @param append
	 *            Whether or not to open the <code>destination</code> file in
	 *            append mode.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void download(RemoteFile source, File destination, int type,
			boolean append) throws FTPException, ConnectionException;

	/**
	 * copies the contents of local file <code>source</code> to the specified
	 * remote file <code>destination</code>.
	 * 
	 * @param source
	 *            Local file to be copied.
	 * @param destination
	 *            Destination file on the remote system.
	 * @param type
	 *            Data representation type to use for data transfer.
	 * @param append
	 *            Whether or not the destination file is to be appended with the
	 *            contents of source file.
	 * @param skip
	 *            Number of bytes to skip.
	 * @exception FTPException
	 * @exception ConnectionException
	 */
	public void upload(File source, RemoteFile destination, int type,
			boolean append, long skip) throws FTPException, ConnectionException;

	/**
	 * Adds a listener that will be notified about the activities of
	 * <code>ControlConnection</code> associated with this <code>FTPClient
	 * </code>.
	 * 
	 * @param l
	 *            Listener to register.
	 */
	public void addControlConnectionListener(ControlConnectionListener l);

	/**
	 * Removes the given listener from the list of registered listenerrs.
	 * 
	 * @param l
	 *            Listner to remove from the registered listener list.
	 */
	public void removeControlConnectionListener(ControlConnectionListener l);

	/**
	 * Adds a DataConnection Listener to this <code>FTPClient</code>.
	 * 
	 * @param l
	 *            Listener to register.
	 */
	public void addDataConnectionListener(DataConnectionListener l);

	/**
	 * Removes the given data connection listener from the registered list of
	 * listeners.
	 * 
	 * @param l
	 *            Listener to unregister.
	 */
	public void removeDataConnectionListener(DataConnectionListener l);

	/**
	 * Returns the list of registered listeners.
	 * 
	 * @return List of registered listeners.
	 */
	public EventListenerList getListenerList();

	/**
	 * Adds the given FTPConnectionListener to the list of registered listeners.
	 * 
	 * @param l
	 *            Listener to register.
	 */
	public void addFTPConnectionListener(FTPConnectionListener l);

	/**
	 * Unregisters the given listener from the list of listeners.
	 * 
	 * @param l
	 *            Listener to unregister.
	 */
	public void removeFTPConnectionListener(FTPConnectionListener l);

	/**
	 * Sets or unsets the flag that determines whether or not to ignore the
	 * response (the IP address portion) of PASV command and substitute it with
	 * the original server IP address when making a PASSIVE data connection.
	 * This method has been added to support some FTP servers that return an
	 * unreachable IP address in response to the PASV command. This method can
	 * also be used to ensure that the data connections will always be made to
	 * the same server to which you are connected.
	 * 
	 * @param enable
	 *            <code>true</code> to enable the IP address substitution;
	 *            <code>false</code> to disable it.
	 * 
	 */
	public void setPassiveIPSubstitutionEnabled(boolean enable);

	/**
	 * Tells whether or not the substitution of passive IP address is enabled.
	 * 
	 * @return <code>true</code>, if the addressed retunred by the server in
	 *         response to a PASV command will be ignored and substituted with
	 *         the original server's IP address; <code>false</code>, otherewise.
	 * 
	 */
	public boolean isPassiveIPSubstitutionEnabled();

	/**
	 * Sets the proxy server to use when making the control/data connections.
	 * 
	 * @param proxy
	 *            the proxy server to use when making the control/data
	 *            connections. Supported proxy types are - SOCKS and DIRECT.
	 */
	public void setProxy(Proxy proxy);

	/**
	 * Returns the current proxy configuration, if any.
	 * 
	 * @return the current proxy configuration, if any. Returns
	 *         <code>null</code>, if there is no proxy set.
	 */
	public Proxy getProxy();
}