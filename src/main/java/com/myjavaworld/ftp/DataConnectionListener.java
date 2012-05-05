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