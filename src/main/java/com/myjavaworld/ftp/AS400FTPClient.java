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
 * An FTP client implementation that can be used with AS/400 systems.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class AS400FTPClient extends DefaultFTPClient {

	/**
	 * Creates a new instance of AS400FTPClient
	 */
	public AS400FTPClient() {
		super();
	}

	@Override
	public void login(String user, String password, String account)
			throws FTPException, ConnectionException {
		super.login(user, password, account);
		executeCommand("SITE LISTFMT 1");
		executeCommand("SITE NAMEFMT 1");
	}
}