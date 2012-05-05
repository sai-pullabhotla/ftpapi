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

/**
 * A Utility class that defines useful constants that are used in FTP process.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public interface FTPConstants {

	/**
	 * A constant that represents a Telnet End Of Line (EOL). All commands sent
	 * over the control connection must end with this character or sequence of
	 * characters.
	 */
	public static final String EOL = "\r\n";
	/**
	 * A constant to represent the default FTP port. Default FTP port is 21.
	 */
	public static final int DEFAULT_PORT = 21;
	/**
	 * A constant that represents default time out. Default time out is 5
	 * minutes.
	 */
	public static final int DEFAULT_TIMEOUT = 5 * 60 * 1000;
	/**
	 * A constant to represent default buffer size to use while sending and
	 * receiving data. The default value is 16 KB. i.e. 16 * 1024 bytes.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 64 * 1024;
	/**
	 * A constant to represent ASCII representation type.
	 */
	public static final int TYPE_ASCII = 1;
	/**
	 * A constant to represent BINARY representation type.
	 */
	public static final int TYPE_BINARY = 2;
	/**
	 * A constant to represent EBCDIC representation type.
	 */
	public static final int TYPE_EBCDIC = 3;
	/**
	 * A constant to represent local representation type.
	 */
	public static final int TYPE_LOCAL = 4;
	/**
	 * A constant to represent IMAGE representation type. This is an alias for
	 * <code>TYPE_BINARY</code>.
	 */
	public static final int TYPE_IMAGE = TYPE_BINARY;
	/**
	 * Default type. Default is set to <code>TYPE_BINARY</code>.
	 */
	public static final int DEFAULT_TYPE = TYPE_BINARY;
	/**
	 * A constant for representing FILE structure.
	 */
	public static final int STRUCTURE_FILE = 1;
	/**
	 * A constant to represent RECORD structure.
	 */
	public static final int STRUCTURE_RECORD = 2;
	/**
	 * A constant to represent PAGE structure.
	 */
	public static final int STRUCTURE_PAGE = 3;
	/**
	 * Default structure. Value is <code>STRUCTURE_FILE</code>.
	 */
	public static final int DEFAULT_STRUCTURE = STRUCTURE_FILE;
	/**
	 * A constant to represent STREAM mode.
	 */
	public static final int MODE_STREAM = 1;
	/**
	 * A constant to represent BLOCK mode.
	 */
	public static final int MODE_BLOCK = 2;
	/**
	 * A Constant to represent COMPRESSED mode.
	 */
	public static final int MODE_COMPRESSED = 3;
	/**
	 * Default mode. Value is <code>MODE_STREAM</code>.
	 */
	public static final int DEFAULT_MODE = MODE_STREAM;
	/**
	 * An SSL usage constant to represent do not use SSL.
	 */
	public static final int USE_NO_SSL = 0;
	/**
	 * An SSL usage constant to represent use SSL if available.
	 */
	public static final int USE_SSL_IF_AVAILABLE = 1;
	/**
	 * An SSL usage constant to represent use implicit SSL.
	 */
	public static final int USE_IMPLICIT_SSL = 2;
	/**
	 * An SSL usage constant to represent use explicit SSL.
	 */
	public static final int USE_EXPLICIT_SSL = 3;
	/**
	 * Default port number to connect to when making an implicit SSL connection.
	 */
	public static final int DEFAULT_IMPLICIT_SSL_PORT = 990;
}