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