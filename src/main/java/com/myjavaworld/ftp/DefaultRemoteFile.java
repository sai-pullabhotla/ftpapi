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

import java.util.Hashtable;

/**
 * The default implementation of <code>RemoteFile</code> interface.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class DefaultRemoteFile implements RemoteFile, java.io.Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 924667277483346705L;
	/**
	 * File separator
	 */
	private static final String SEPARATOR = "/";
	/**
	 * File separator
	 */
	private static final char SEPARATOR_CHAR = '/';
	/**
	 * Absolute Path of the file.
	 */
	private String path = null;
	/**
	 * Name of the file
	 */
	private String name = null;
	/**
	 * Extension of the file.
	 */
	private String extension = null;
	/**
	 * Size of the file.
	 */
	private long size = 0L;
	/**
	 * last modified date and time of the file.
	 */
	private long lastModified = 0L;
	/**
	 * Whether or not this is a directory.
	 */
	private boolean dir = true;
	/**
	 * File attributes
	 */
	private String attributes = null;
	private int linkCount = 0;
	private String owner = null;
	private String group = null;

	/**
	 * Constructs a <code>DefaultRemoteFile</code> object. The result object
	 * will be marked as directory.
	 * 
	 * @param path
	 *            Absolute path.
	 */
	public DefaultRemoteFile(String path) {
		this(path, true);
	}

	/**
	 * Constructs a <code>DefaultRemoteFile</code> object.
	 * 
	 * @param path
	 *            Absolute path
	 * @param dir
	 *            Whether or not this object is a directory.
	 */
	public DefaultRemoteFile(String path, boolean dir) {
		if (path == null) {
			throw new NullPointerException();
		}
		this.path = path;
		this.dir = dir;
		this.attributes = "";
		this.name = computeName();
		this.extension = computeExtension();
	}

	/**
	 * Constructs a <code>DefaultRemoteFile</code> object.
	 * 
	 * @param parent
	 *            parent file.
	 * @param name
	 *            File name
	 */
	public DefaultRemoteFile(String parent, String name) {
		this(parent, name, true);
	}

	/**
	 * Constructs a <code>DefaultRemoteFile</code> object.
	 * 
	 * @param parent
	 *            Parent file
	 * @param name
	 *            File name
	 * @param dir
	 *            Whther or not this is a directory
	 */
	public DefaultRemoteFile(String parent, String name, boolean dir) {
		this(parent, name, dir, 0L, 0L, "");
	}

	/**
	 * Constructs a <code>DefaultRemoteFile</code> object.
	 * 
	 * @param parent
	 *            Parent file
	 * @param name
	 *            File name
	 * @param dir
	 *            Whether or not this is a directory
	 * @param size
	 *            Size of this file in bytes
	 * @param lastModified
	 *            Date and time this file is last modified
	 * @param attributes
	 *            Attributes of this file
	 */
	public DefaultRemoteFile(String parent, String name, boolean dir,
			long size, long lastModified, String attributes) {

		// if (name == null)
		// throw new NullPointerException();
		// if (parent != null) {
		// if (parent.endsWith(SEPARATOR))
		// path = parent + name;
		// else
		// path = parent + SEPARATOR + name;
		// }
		// else {
		// path = name;
		// }
		// this.dir = dir;
		// this.size = size;
		// this.lastModified = lastModified;
		// this.attributes = attributes;
		// this.name = computeName();
		// this.extension = computeExtension();

		this(parent, name, dir, size, lastModified, attributes, 0, "", "");
	}

	/**
	 * Creates an instance of <code>DefaultRemoteFile</code>.
	 * 
	 * @param parent
	 *            parent file.
	 * @param name
	 *            name of this file.
	 * @param dir
	 *            whether this file is a directory or not.
	 * @param size
	 *            Size of this file.
	 * @param lastModified
	 *            Last modified date
	 * @param attributes
	 *            Attributes or permissions.
	 * @param linkCount
	 *            symbolic link count
	 * @param owner
	 *            Owner of this file.
	 * @param group
	 *            Group to which the owner belongs to.
	 */
	public DefaultRemoteFile(String parent, String name, boolean dir,
			long size, long lastModified, String attributes, int linkCount,
			String owner, String group) {

		if (name == null) {
			throw new NullPointerException();
		}
		if (parent != null) {
			if (parent.endsWith(SEPARATOR)) {
				path = parent + name;
			} else {
				path = parent + SEPARATOR + name;
			}
		} else {
			path = name;
		}
		this.dir = dir;
		this.size = size;
		this.lastModified = lastModified;
		this.attributes = attributes;
		this.linkCount = linkCount;
		this.owner = owner;
		this.group = group;
		this.name = computeName();
		this.extension = computeExtension();
	}

	public String getPath() {
		return path;
	}

	public String getNormalizedPath() {
		if (isLink()) {
			int index = path.indexOf(" -> ");
			if (index < 0) {
				return path;
			}
			return path.substring(0, index);
		}
		return path;
	}

	public String getName() {
		return name;
	}

	public String getNormalizedName() {
		if (isLink()) {
			int index = name.indexOf(" -> ");
			if (index < 0) {
				return name;
			}
			return name.substring(0, index);
		}
		return name;
	}

	public String getExtension() {
		return extension;
	}

	public String getType() {
		if (extension == null) {
			return "Directory";
		}
		if (isLink()) {
			return "Link";
		}
		if (extension.length() == 0) {
			return "File";
		}
		return extension + " File";
	}

	public long getSize() {
		return size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public boolean isDirectory() {
		return dir;
	}

	public boolean isFile() {
		return !dir;
	}

	public boolean isLink() {
		try {
			return attributes.charAt(0) == 'l';
		} catch (Exception exp) {
			return false;
		}
	}

	public String getAttributes() {
		return attributes;
	}

	public int getLinkCount() {
		return linkCount;
	}

	public String getOwner() {
		return owner;
	}

	public String getGroup() {
		return group;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return path;
	}

	public Hashtable getOtherProperties() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DefaultRemoteFile)) {
			return false;
		}
		DefaultRemoteFile that = (DefaultRemoteFile) obj;
		return this.path.equals(that.path);
	}

	private String computeName() {
		if (isLink()) {
			int linkIndex = path.indexOf(" -> ");
			String beforeLink = path.substring(0, linkIndex);
			String afterLink = path.substring(linkIndex + 4);
			int index = beforeLink.lastIndexOf(SEPARATOR_CHAR);
			if (index < 0 || beforeLink.endsWith(SEPARATOR)) {
				return beforeLink + " -> " + afterLink;
			}
			return beforeLink.substring(index + 1) + " -> " + afterLink;
		}
		int index = path.lastIndexOf(SEPARATOR_CHAR);
		if (index < 0 || path.endsWith(SEPARATOR)) {
			return path;
		}
		return path.substring(index + 1);
	}

	private String computeExtension() {
		if (dir) {
			return null;
		}
		int index = name.lastIndexOf('.');
		if (index < 0 || name.endsWith(".")) {
			return "";
		}
		return name.substring(index + 1).toUpperCase();
	}
}