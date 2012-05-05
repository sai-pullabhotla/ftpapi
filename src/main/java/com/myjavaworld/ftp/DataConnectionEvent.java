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

import java.util.EventObject;

/**
 * An event generated by data connections to notify the status of data transfer.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class DataConnectionEvent extends EventObject {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7736555651463625070L;
	/**
	 * A constant to indicate that data is being sent to the remote host.
	 */
	public static final int SEND = 1;
	/**
	 * A constant to indicate that data is being received from the remote host.
	 */
	public static final int RECEIVE = 2;
	/**
	 * The type of this event.
	 */
	private int id = 0;
	/**
	 * Number of bytes trabsferred so far.
	 */
	private long bytesTransferred = 0L;

	/**
	 * Constructs a <code>DataConnectionEvent</code>.
	 * 
	 * @param source
	 *            The object that generated this event.
	 * @param id
	 *            Type of this event. Possible types are -
	 *            <ol>
	 *            <li><code>DataConnectionEvent.SEND</code></li>
	 *            <li><code>DataConnectionEvent.RECEIVE</code></li>
	 *            </ol>
	 * @param bytesTransferred
	 *            Number of bytes transferred so far.
	 */
	public DataConnectionEvent(Object source, int id, long bytesTransferred) {
		super(source);
		this.id = id;
		this.bytesTransferred = bytesTransferred;
	}

	/**
	 * Returns the ID ot type of this event.
	 * 
	 * @return Type of this event.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the number of bytes transferred.
	 * 
	 * @return number of bytes transferred.
	 */
	public long getBytesTransferred() {
		return bytesTransferred;
	}

	/**
	 * String rpresentation of this event.
	 * 
	 * @return String representation of this event.
	 */
	@Override
	public String toString() {
		return "Type: " + id + " Bytes Transferred: " + bytesTransferred;
	}
}