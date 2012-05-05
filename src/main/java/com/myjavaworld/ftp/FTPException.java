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

/**
 * An exception thrown to indicate the command sent was not understood by the
 * remote host or no action is taken because of some reason. Usually, this
 * exception is thrown whenever the server sends a response with a response code
 * starting with 5XX or 4XX. All responses starting with a reponse code of 5XX
 * are permenant negative completion replies and that start with a response code
 * of 4XX are transient negative completion replies.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class FTPException extends Exception {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -8911832324569098314L;
	/**
	 * The error code or response code.
	 */
	private String errorCode = null;

	/**
	 * Constructs an <CODE>FTPException</code> object with "000" as the error
	 * code and no descriptive message.
	 */
	public FTPException() {
		this("000 FTPException");
	}

	/**
	 * Constructs an <code>FTPException</code> object with the reply received
	 * from the remote FTP host. The reply will be parsed to find the reply code
	 * and message.
	 * 
	 * @param reply
	 *            a permenant or transient negative completion reply from the
	 *            remote host.
	 */
	public FTPException(String reply) {
		super(reply.length() >= 4 ? reply.substring(4) : reply);
		this.errorCode = reply.substring(0, 3);
	}

	/**
	 * Returns the error or reply code.
	 * 
	 * @return Error or reply code.
	 */
	public String getErrorCode() {
		return errorCode;
	}
}