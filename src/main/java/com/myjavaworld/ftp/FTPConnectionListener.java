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

/**
 * A contract for implementations of <code>FTPConnectionListener</code>s. These
 * listeners will be notified when a connection is opened or closed.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface FTPConnectionListener extends java.util.EventListener {

	/**
	 * Called when a connection is opened.
	 * 
	 * @param evt
	 *            connection event.
	 */
	public void connectionOpened(FTPConnectionEvent evt);

	/**
	 * Called when the connection is closed.
	 * 
	 * @param evt
	 *            connection event.
	 */
	public void connectionClosed(FTPConnectionEvent evt);
}