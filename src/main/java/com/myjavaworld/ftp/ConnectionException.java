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