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
package com.myjavaworld.ftp;

import java.util.Hashtable;

/**
 * Objects of this class represent a file on the remote FTP server.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface RemoteFile {

	/**
	 * Returns the name of this remote file.
	 * 
	 * @return Name of this file.
	 */
	public String getName();

	/**
	 * Returns the normalized name of this remote file.
	 * 
	 * @return normalized name.
	 */
	public String getNormalizedName();

	/**
	 * Returns the full path of this remote file.
	 * 
	 * @return Path of this remote file.
	 */
	public String getPath();

	/**
	 * Returns the normalized path.
	 * 
	 * @return normalized path.
	 */
	public String getNormalizedPath();

	/**
	 * Returns the extension of this file. Extensions are used to determine the
	 * type of file when a file is to be transferred from/to the remote system.
	 * 
	 * @return extension of this file.
	 */
	public String getExtension();

	/**
	 * Returns the type of this file.
	 * 
	 * @return Type of this file.
	 */
	public String getType();

	/**
	 * Returns the size of this file in bytes.
	 * 
	 * @return size of this file in bytes.
	 */
	public long getSize();

	/**
	 * Returns the last modified date of this file in milli seconds.
	 * 
	 * @return Last modified date of this file. If the last modified date is
	 *         unknown a value of -1L may be returned.
	 */
	public long getLastModified();

	/**
	 * Tells whether this file is a directory or not.
	 * 
	 * @return <code>true</code>, if this file is a directory.
	 *         <code>false</code> otherwise.
	 */
	public boolean isDirectory();

	/**
	 * Tells whether this file is a data file or not.
	 * 
	 * @return <code>true</code>, if this file is a data file.
	 *         <code>false</code> otherwise.
	 */
	public boolean isFile();

	/**
	 * Checkes to see if this file is symbolic link.
	 * 
	 * @return <code>true</code>, if this file is a symbolic link.
	 *         <code>false</code>, otherwsie.
	 */
	public boolean isLink();

	/**
	 * Returns the attributes of this file in standard unix format.
	 * 
	 * @return attributes of this file. May return blank if the attributes are
	 *         unknown.
	 */
	public String getAttributes();

	/**
	 * Returns additional properties of this file if any.
	 * 
	 * @return additional properties. May return null if there are no additional
	 *         properties.
	 */
	public Hashtable getOtherProperties();

	/**
	 * Returns the number of links that point to this file.
	 * 
	 * @return link count
	 */
	public int getLinkCount();

	/**
	 * Returns the owner of this file.
	 * 
	 * @return Owner.
	 */
	public String getOwner();

	/**
	 * Returns the group name to which the owner of this file belongs to.
	 * 
	 * @return group name.
	 */
	public String getGroup();

	/**
	 * Sets the attributes on this file to the given attributes.
	 * 
	 * @param attributes
	 *            new attributes to set.
	 */
	public void setAttributes(String attributes);
}