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

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * The default implementation of <code>ListParser</code> interface. This parser
 * parses standard UNIX style listing produced by the FTP servers and converts
 * them to <code>RemoteFile</code> objects.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class DefaultListParser implements ListParser {

	/**
	 * We need to substitute this if a file's modified date does not contain the
	 * year information.
	 */
	private static final int CURRENT_YEAR = Calendar.getInstance().get(
			Calendar.YEAR);
	/**
	 * Assume all UNIX FTP servers output the last modified date of a file in
	 * MMM dd yyyy hh:mm" format.
	 */
	private final SimpleDateFormat unixDateFormat = new SimpleDateFormat(
			"MMM dd yyyy hh:mm", Locale.US);
	/**
	 * Number of milli seconds in a year
	 */
	private static final long MILLIS_IN_YEAR;

	static {
		GregorianCalendar gc = new GregorianCalendar();
		if (gc.isLeapYear(CURRENT_YEAR)) {
			MILLIS_IN_YEAR = 366L * 24L * 60L * 60L * 1000L;
		} else {
			MILLIS_IN_YEAR = 365L * 24L * 60L * 60L * 1000L;
		}
	}

	public RemoteFile[] parse(RemoteFile parent, BufferedReader reader)
			throws ParseException, IOException {

		RemoteFile[] list = new RemoteFile[0];
		List<RemoteFile> l = new ArrayList<RemoteFile>(50);
		String line = null;
		// Some FTP servers return the first line something like
		// "total xxxx bytes. ". Lets ignore the fist line if it starts
		// with the word "total", because the UNIX listing must start with
		// the file attributes.

		line = reader.readLine();
		// System.out.println(line);
		if (line != null && line.trim().length() > 0) {
			if (!line.trim().toLowerCase().startsWith("total")) {
				l.add(parse(parent, line));
			}
		}
		// Parse all other lines after the first line until the
		// end of the stream is reached or a ParseException is thrown.
		while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			if (line.trim().length() > 0) {
				l.add(parse(parent, line));
			}
		}
		list = new RemoteFile[l.size()];
		list = l.toArray(list);
		return list;
	}

	public RemoteFile parse(RemoteFile parent, String line)
			throws ParseException {
		String rawData = line;
		try {
			// Attributes & isDirectory
			int spaceIndex = rawData.indexOf(' ');
			String attributes = rawData.substring(0, spaceIndex);
			boolean dir = attributes.charAt(0) == 'd';
			// if the length of this field is more than 10, we might have a
			// server that is returning advanced file attributes such as ACLs.
			// If the length of this filed is more than 11, thn we in addition
			// to the advanced attributes, the number of links are more than 99.
			int linkCount = 0;
			boolean gotLinkCount = false;
			if (attributes.length() > 10) {
				if (attributes.length() == 11) {
					// we don't have to anything.
				} else {
					// Check the character at 11th position
					char ch = attributes.charAt(10);
					if (Character.isDigit(ch)) {
						linkCount = Integer.parseInt(attributes.substring(10));
						attributes = attributes.substring(0, 10);
					} else {
						linkCount = Integer.parseInt(attributes.substring(11));
						attributes = attributes.substring(0, 11);
					}
					gotLinkCount = true;
				}
			}

			rawData = rawData.substring(spaceIndex).trim();
			if (!gotLinkCount) {
				// No. of Symbolic links
				spaceIndex = rawData.indexOf(' ');
				linkCount = Integer.parseInt(rawData.substring(0, spaceIndex));
				rawData = rawData.substring(spaceIndex).trim();
			}

			// Owner
			spaceIndex = rawData.indexOf(' ');
			String owner = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Group
			spaceIndex = rawData.indexOf(' ');
			String group = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Size
			spaceIndex = rawData.indexOf(' ');
			long size = Long.parseLong(rawData.substring(0, spaceIndex));
			rawData = rawData.substring(spaceIndex).trim();

			// Month
			spaceIndex = rawData.indexOf(' ');
			String month = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Date
			spaceIndex = rawData.indexOf(' ');
			String date = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Year
			spaceIndex = rawData.indexOf(' ');
			String year = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Name
			String name = rawData;

			return new DefaultRemoteFile(parent.getPath(), name, dir, size,
					parseDate(month, date, year), attributes, linkCount, owner,
					group);
		} catch (Exception exp) {
			// System.err.println("*** Parsing Exception ***");
			throw new ParseException(line, 0);
		}
	}

	public RemoteFile createRemoteFile(String path) {
		return createRemoteFile(path, true);
	}

	public RemoteFile createRemoteFile(String path, boolean dir) {
		return new DefaultRemoteFile(path, dir);
	}

	public RemoteFile createRemoteFile(String parent, String name) {
		return createRemoteFile(parent, name, true);
	}

	public RemoteFile createRemoteFile(String parent, String name, boolean dir) {
		return new DefaultRemoteFile(parent, name, dir);
	}

	public RemoteFile createRemoteFile(RemoteFile parent, String name) {
		return createRemoteFile(parent.getPath(), name, true);
	}

	public RemoteFile createRemoteFile(RemoteFile parent, String name,
			boolean dir) {
		return createRemoteFile(parent.getPath(), name, dir);
	}

	public String getName() {
		return "Default List Parser";
	}

	public String getDescription() {
		return "Parses standard UNIX style listing. ";
	}

	public String getVendor() {
		return "MyJavaWorld.com";
	}

	public String getVersion() {
		return "1.0";
	}

	private long parseDate(String month, String date, String year)
			throws ParseException {
		long millis = 0L;
		if (year.indexOf(':') >= 0) {
			millis = unixDateFormat.parse(
					month + " " + date + " " + CURRENT_YEAR + " " + year)
					.getTime();
			if (millis - System.currentTimeMillis() > 0) {
				millis -= MILLIS_IN_YEAR;
			}
		} else {
			millis = unixDateFormat.parse(
					month + " " + date + " " + year + " 00:00").getTime();
		}
		return millis;
	}

	public static void main(String[] args) throws ParseException {
		DefaultListParser parser = new DefaultListParser();
		RemoteFile parent = parser.createRemoteFile("/");
		RemoteFile file = parser
				.parse(parent,
						"drwxrwxrwx+ 329    root  analysts  9216  Sep 8 18:00   backup");
		System.out.println(file.getName() + "\t" + file.getLinkCount() + "\t"
				+ file.getGroup() + "\t" + file.getOwner() + "\t"
				+ file.getSize() + "\t" + new Date(file.getLastModified())
				+ "\t" + file.getAttributes());
	}
}