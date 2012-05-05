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