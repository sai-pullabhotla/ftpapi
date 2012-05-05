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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 1.0
 */
public class RegexFilter implements Filter {

	private Pattern pattern = null;
	private Matcher matcher = null;

	/**
	 * Creates an instance of <code>RegexFilter</code>.
	 * 
	 * @param pattern
	 *            The regular expression or pattern.
	 * @param flags
	 *            Additional flags to apply to the pattern.
	 */
	public RegexFilter(String pattern, int flags) {
		setPattern(pattern, flags);
	}

	/**
	 * Sets the pattern to be used by this filter to the given pattern. The
	 * given string will be compiled to <code>java.util.Pattern</code> and a
	 * call to the <code>setPattern(Pattern pattern)</code> is made.
	 * 
	 * @param pattern
	 *            The pattern to be used by this filter.
	 * @param flags
	 *            Additional flags to apply to the pattern.
	 */
	public void setPattern(String pattern, int flags) {
		setPattern(Pattern.compile(pattern, flags));
	}

	/**
	 * Sets the pattern to be used by this filter to the given pattern.
	 * 
	 * @param pattern
	 *            The pattern to be used by this filter.
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
		matcher = this.pattern.matcher("");
	}

	/**
	 * Returns the current pattern being used by this filter.
	 * 
	 * @return the pattern in use.
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * Checks to see if the given value can pass through this filter. This
	 * method only accepts <code>java.lang.String</code>.
	 * 
	 * @return <code>true</code> if the given value contains the pattern as set
	 *         by the setPattern() method. <code>false</code> otherwise.
	 *         <code>ClassCastException</code> will thrown if the given vlue is
	 *         not an instance <code>java.lang.String</code>.
	 */
	public boolean accept(Object value) {
		String input = (String) value;
		matcher.reset(input);
		return matcher.matches();
	}
}