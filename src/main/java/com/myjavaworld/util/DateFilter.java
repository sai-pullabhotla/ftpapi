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
package com.myjavaworld.util;

import java.util.Date;

/**
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 1.0
 */
public class DateFilter implements Filter {

	private Date startDate = null;
	private Date endDate = null;
	private long startMillis = 0L;
	private long endMillis = 0L;

	/**
	 * Creates an instance of <code>DateFilter</code>.
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public DateFilter(Date startDate, Date endDate) {
		if (startDate == null && endDate == null) {
			throw new IllegalArgumentException();
		}
		setStartDate(startDate);
		setEndDate(endDate);
	}

	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		if (endDate != null) {
			this.endMillis = endDate.getTime();
		}
	}

	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		if (startDate != null) {
			this.startMillis = startDate.getTime();
		}
	}

	public boolean accept(Object obj) {
		long millis = ((Long) obj).longValue();
		if (startDate == null) {
			return millis <= endMillis;
		}
		if (endDate == null) {
			return startMillis <= millis;
		}
		return startMillis <= millis && millis <= endMillis;
	}
}