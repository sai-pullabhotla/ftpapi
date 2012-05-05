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

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An implementation of <code>ListParser</code> interface used to parse the
 * directory listing of FTP servers, which produce the output in MS-DOS format.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class DosListParser implements ListParser {

	private final SimpleDateFormat dosDateFormat = new SimpleDateFormat(
			"MM-dd-yy hh:mma", Locale.US);

	public RemoteFile[] parse(RemoteFile parent, BufferedReader reader)
			throws ParseException, IOException {

		RemoteFile[] list = new RemoteFile[0];
		List<RemoteFile> l = new ArrayList<RemoteFile>(50);
		String line = null;
		while ((line = reader.readLine()) != null) {
			// stdout(line);
			if (line.trim().length() > 0) {
				l.add(parse(parent, line));
			}
		}
		list = new RemoteFile[l.size()];
		list = l.toArray(list);
		return list;
	}

	public RemoteFile parse(RemoteFile parent, String rawData)
			throws ParseException {

		try {
			// Date
			int spaceIndex = rawData.indexOf(' ');
			String date = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Time
			spaceIndex = rawData.indexOf(' ');
			String time = rawData.substring(0, spaceIndex);
			rawData = rawData.substring(spaceIndex).trim();

			// Directory or Size
			spaceIndex = rawData.indexOf(' ');
			String temp = rawData.substring(0, spaceIndex);
			boolean dir = false;
			long size = 0L;
			if (temp.equals("<DIR>")) {
				dir = true;
			} else {
				size = Long.parseLong(temp);
			}
			rawData = rawData.substring(spaceIndex).trim();

			// Name
			String name = rawData;

			return new DefaultRemoteFile(parent.getPath(), name, dir, size,
					parseDate(date + " " + time), "");
		} catch (Exception exp) {
			throw new ParseException(exp.toString(), 0);
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
		return "MS-DOS List Parser";
	}

	public String getDescription() {
		return "Parses MS-DOS style listing. ";
	}

	public String getVendor() {
		return "MyJavaWorld.com";
	}

	public String getVersion() {
		return "1.0";
	}

	private long parseDate(String date) throws ParseException {
		return dosDateFormat.parse(date).getTime();
	}
}