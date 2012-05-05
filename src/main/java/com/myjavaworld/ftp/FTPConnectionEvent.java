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
 * An event object to represent FTPconnection stats.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class FTPConnectionEvent extends java.util.EventObject {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -168620515406409910L;

	/**
	 * Message describing this event
	 */
	private String message = null;

	/**
	 * Creates an instance of <code>FTPConnectionEvent</code>.
	 * 
	 * @param source
	 *            Object that produced this event.
	 * @param message
	 *            Message string describing the event.
	 */
	public FTPConnectionEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	/**
	 * Returns the message string that describes this connection event.
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
}