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
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.myjavaworld.ftp;

import com.myjavaworld.util.DateFilter;
import com.myjavaworld.util.Filter;
import com.myjavaworld.util.RegexFilter;

/**
 * An implementation of <code>Filter</code> to filter files on the FTP host.
 * This filter supports the following:
 * <ul>
 * <li>Name based filtering using regular expressions</li>
 * <li>Filter based on the last modification date of the files</li>
 * </ul>
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 1.0
 */
public class RemoteFileFilter implements Filter {

	private RegexFilter regexFilter = null;
	private DateFilter dateFilter = null;
	private boolean exclusionFilter = false;

	/**
	 * Creates an instance of <code>RemoteFileFilter</code>.
	 * 
	 * @param regexFilter
	 * @param dateFilter
	 */
	public RemoteFileFilter(RegexFilter regexFilter, DateFilter dateFilter) {
		this(regexFilter, dateFilter, false);
	}

	public RemoteFileFilter(RegexFilter regexFilter, DateFilter dateFilter,
			boolean exclusionFilter) {
		setRegexFilter(regexFilter);
		setDateFilter(dateFilter);
		this.exclusionFilter = exclusionFilter;
	}

	/**
	 * Returns the date filter.
	 * 
	 * @return Returns the dateFilter.May return <code>null</code> if a date
	 *         filter is not set.
	 * 
	 */
	public DateFilter getDateFilter() {
		return dateFilter;
	}

	/**
	 * Sets the date filter to the given filter.
	 * 
	 * @param dateFilter
	 *            The dateFilter to set.
	 * 
	 */
	public void setDateFilter(DateFilter dateFilter) {
		this.dateFilter = dateFilter;
	}

	/**
	 * Returns the name filter.
	 * 
	 * @return Returns the regexFilter. May return null if a name filter is not
	 *         set.
	 */
	public RegexFilter getRegexFilter() {
		return regexFilter;
	}

	/**
	 * Sets the name filter to the given filter.
	 * 
	 * @param regexFilter
	 *            The regexFilter to set.
	 */
	public void setRegexFilter(RegexFilter regexFilter) {
		this.regexFilter = regexFilter;
	}

	public void setExclusionFilter(boolean exclusionFilter) {
		this.exclusionFilter = exclusionFilter;
	}

	public boolean isExclusionFilter() {
		return exclusionFilter;
	}

	public boolean accept(Object obj) {
		RemoteFile rf = (RemoteFile) obj;
		if (rf.isFile()) {
			if (regexFilter != null) {
				if (!regexFilter.accept(rf.getName())) {
					return exclusionFilter ? true : false;
				}
			}
			if (dateFilter != null) {
				if (!dateFilter.accept(new Long(rf.getLastModified()))) {
					return exclusionFilter ? true : false;
				}
			}
			return exclusionFilter ? false : true;
		}
		return true;
	}
}