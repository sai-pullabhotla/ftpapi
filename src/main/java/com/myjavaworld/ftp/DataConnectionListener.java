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

import java.util.EventListener;

/**
 * A contract for implementations who are interested in receiving notifications
 * about the progress of data transfers.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface DataConnectionListener extends EventListener {

	/**
	 * Indicates a new data transfer has started.
	 * 
	 * @param evt
	 *            <code>DataTransferEvent</code>.
	 */
	public void dataTransferStarted(DataConnectionEvent evt);

	/**
	 * Indicates a data transfer has finished.
	 * 
	 * @param evt
	 *            <code>DataTransferEvent</code>.
	 */
	public void dataTransferFinished(DataConnectionEvent evt);

	/**
	 * Indicates that a data transfer is still in progress.
	 * 
	 * @param evt
	 *            <code>DataTransferEvent</code>.
	 */
	public void dataTransferProgress(DataConnectionEvent evt);

	/**
	 * Indicates that a data transfer was aborted by the user.
	 * 
	 * @param evt
	 *            <code>DataTransferEvent</code>.
	 */
	public void dataTransferAborted(DataConnectionEvent evt);

	/**
	 * Indicates that the data transfer could not be completed because of some
	 * error.
	 * 
	 * @param evt
	 *            <code>DataTransferEvent</code>.
	 */
	public void dataTransferError(DataConnectionEvent evt);
}