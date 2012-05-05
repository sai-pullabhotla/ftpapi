/*
 * Copyright 2012 jMethods, Inc. 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * 
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License. 
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