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

import java.io.Serializable;

/**
 * A class that encapsulates the information of an FTP server. Objects of this
 * class are used by <code>FTPClient</code> for connecting/login to the FTP
 * server.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class FTPHost implements Serializable {

	private static final long serialVersionUID = -2673296497106527529L;
	private String hostName = null;
	private int port = 0;
	private String userName = null;
	private String password = null;
	private String account = null;

	/**
	 * Constructs an <code>FTPHost</code> object.
	 */
	public FTPHost() {
		this("", FTPConstants.DEFAULT_PORT, "", "", "");
	}

	/**
	 * Constructs an <code>FTPHost</code> object.
	 * 
	 * @param hostName
	 *            host name or IP address
	 * @param userName
	 *            user name for login
	 * @param password
	 *            Password for login
	 */
	public FTPHost(String hostName, String userName, String password) {

		this(hostName, FTPConstants.DEFAULT_PORT, userName, password, "");
	}

	/**
	 * Constructs an <code>FTPHost</code> object.
	 * 
	 * @param hostName
	 *            host name or IP address
	 * @param userName
	 *            user name for login
	 * @param password
	 *            Password for login
	 * @param account
	 *            Account name for login
	 */
	public FTPHost(String hostName, String userName, String password,
			String account) {

		this(hostName, FTPConstants.DEFAULT_PORT, userName, password, account);
	}

	/**
	 * Constructs an <code>FTPHost</code> object.
	 * 
	 * @param hostName
	 *            host name or IP address port Port number to connect to
	 * @param port
	 *            Port number
	 * @param userName
	 *            user name for login
	 * @param password
	 *            Password for login
	 * @param account
	 *            Account name for login
	 */
	public FTPHost(String hostName, int port, String userName, String password,
			String account) {

		setHostName(hostName);
		setPort(port);
		setUserName(userName);
		setPassword(password);
		setAccount(account);
	}

	/**
	 * Sets the host name to the given <code>hostName</code>.
	 * 
	 * @param hostName
	 *            Host name or IP address
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Returns the host name
	 * 
	 * @return Host name
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Sets the port number to the given port.
	 * 
	 * @param port
	 *            Port number of this FTPHost.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Returns the port number.
	 * 
	 * @return Port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the user name to the given <code>userName</code>.
	 * 
	 * @param userName
	 *            user name for login.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * returns the user name.
	 * 
	 * @return User name.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the password to the given <code>password</code>.
	 * 
	 * @param password
	 *            Password for login.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the password
	 * 
	 * @return Password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the account to the given <code>account</code>.
	 * 
	 * @param account
	 *            Account name for login
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Returns the account name.
	 * 
	 * @return Account name.
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Returns a string representation of this <code>FTPHost</code>.
	 * 
	 * @return String representation.
	 */
	@Override
	public String toString() {
		return hostName;
	}
}