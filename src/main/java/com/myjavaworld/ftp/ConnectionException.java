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
 * A General exception class to indicate connection failures. All classes in
 * this package must throw this exception as a replacement for SocketException
 * and IOException when reading data from or writing data to the remote host.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class ConnectionException extends Exception {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 2025718852007883661L;

	/**
	 * Constructs a <code>ConnectionException</code> object with no detailed
	 * message.
	 */
	public ConnectionException() {
		super();
	}

	/**
	 * Constructs a <code>ConnectionException</code> object with the specified
	 * detailed message.
	 * 
	 * @param message
	 *            reason string.
	 */
	public ConnectionException(String message) {
		super(message);
	}
}