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