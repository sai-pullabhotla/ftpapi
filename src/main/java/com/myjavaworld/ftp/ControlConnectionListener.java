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

import java.util.EventListener;

/**
 * A contract for implementations of <code>ControlConnectionListener</code>.
 * These listeners will be notified when a command is sent to the remote host
 * over the control connection or a reply is received from the remote host.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface ControlConnectionListener extends EventListener {

	/**
	 * Called by the <code>ControlConnection</code> when a command is sent to
	 * the remote host.
	 * 
	 * @param evt
	 *            <code>ControlConnectionEvent</code>.
	 */
	public void commandSent(ControlConnectionEvent evt);

	/**
	 * Called by the <code>ControlConnection</code> when a reply is received
	 * from the remote host.
	 * 
	 * @param evt
	 *            <code>ControlConnectionEvent</code>.
	 */
	public void replyReceived(ControlConnectionEvent evt);
}