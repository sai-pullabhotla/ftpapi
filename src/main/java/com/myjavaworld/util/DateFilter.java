/*
 * Copyright 2012 jMethods, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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