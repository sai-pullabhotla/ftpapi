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

/**
 * An FTP client implementation that can be used with AS/400 systems.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class AS400FTPClient extends DefaultFTPClient {

	/**
	 * Creates a new instance of AS400FTPClient
	 */
	public AS400FTPClient() {
		super();
	}

	@Override
	public void login(String user, String password, String account)
			throws FTPException, ConnectionException {
		super.login(user, password, account);
		executeCommand("SITE LISTFMT 1");
		executeCommand("SITE NAMEFMT 1");
	}
}