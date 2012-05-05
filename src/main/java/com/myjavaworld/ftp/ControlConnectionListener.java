/*
 * This software is the confidential and proprietary information of the author,
 * Sai Pullabhotla. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement you
 * entered into with the author. 
 * 
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF 
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR 
 * NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY 
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR 
 * ITS DERIVATIVES.
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