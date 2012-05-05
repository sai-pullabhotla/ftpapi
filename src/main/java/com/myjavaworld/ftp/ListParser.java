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
 * under the License
 */
package com.myjavaworld.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * A contract for classes that are used to parse the directory listing produced
 * by an FTP server. Since different FTP servers produce the listing in
 * different formats, this interface helps switch between List parsers.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface ListParser {

	/**
	 * Parses the directory listing returned by an FTP server into an array of
	 * RemoteFile objects.
	 * 
	 * @param parent
	 *            the directory whose contents are listed
	 * @param reader
	 *            the reader containing the data returned by the server
	 * @return the equivalent RemoteFile objects
	 * @throws ParseException
	 *             if any error occurs while parsing the directory listing.
	 * @throws IOException
	 *             propagated
	 */
	public RemoteFile[] parse(RemoteFile parent, BufferedReader reader)
			throws ParseException, IOException;

	/**
	 * Parses a line of output in to a <code>RemoteFile</code> object.
	 * 
	 * @param parent
	 *            Parent directory.
	 * @param rawData
	 *            a line of output from the LIST command.
	 * @return RemoteFile object.
	 * @exception ParseException
	 *                if the rawData can not be parsed by this ListParser.
	 */
	public RemoteFile parse(RemoteFile parent, String rawData)
			throws ParseException;

	/**
	 * Creates a <code>RemoteFile</code> object. The created object will be
	 * marked as a directory.
	 * 
	 * @param path
	 *            Absolute path name
	 * @return RemoteFile object.
	 */
	public RemoteFile createRemoteFile(String path);

	/**
	 * Creates a <code>RemoteFile</code> object.
	 * 
	 * @param path
	 *            Absolute path name
	 * @param dir
	 *            If this is <code>true</code>, the created object will be
	 *            marked as a directory. Otherwise, a file.
	 * @return RemoteFile object.
	 */
	public RemoteFile createRemoteFile(String path, boolean dir);

	/**
	 * Creates a <code>RemoteFile</code> object.
	 * 
	 * @param parent
	 *            Parent file
	 * @param name
	 *            Name of the file The created object will be marked as a
	 *            directory.
	 * @return RemoteFile object.
	 */
	public RemoteFile createRemoteFile(String parent, String name);

	/**
	 * Creates a <code>RemoteFile</code> object.
	 * 
	 * @param parent
	 *            Parent file
	 * @param name
	 *            Name of the file
	 * @param dir
	 *            If this is <code>true</code>, the created object will be
	 *            marked as a directory. Otherwise, a file.
	 * @return RemoteFile object.
	 */
	public RemoteFile createRemoteFile(String parent, String name, boolean dir);

	/**
	 * Creates a <code>RemoteFile</code> object
	 * 
	 * @param parent
	 *            Parent file
	 * @param name
	 *            Name of the file
	 * @return RemoteFile object.
	 */
	public RemoteFile createRemoteFile(RemoteFile parent, String name);

	/**
	 * Creates a <code>RemoteFile</code> object.
	 * 
	 * @param parent
	 *            Parent file
	 * @param name
	 *            Name of the file
	 * @param dir
	 *            , if this is <code>true</code>, the created object will be
	 *            marked as a directory. Otherwise a file.
	 * @return RemoteFile object.
	 */
	public RemoteFile createRemoteFile(RemoteFile parent, String name,
			boolean dir);

	/**
	 * Gets the name of this <code>ListParser</code>
	 * 
	 * @return Name of this parser.
	 */
	public String getName();

	/**
	 * Returns the description of this parser.
	 * 
	 * @return Description
	 */
	public String getDescription();

	/**
	 * Gets the vendor name of this parser.
	 * 
	 * @return Vendor name.
	 */
	public String getVendor();

	/**
	 * Gets the version of this parser.
	 * 
	 * @return Version number of this parser.
	 */
	public String getVersion();
}