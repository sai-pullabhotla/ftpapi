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

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * An utility class
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class FTPUtil {

	/**
	 * Codes for data representation types.
	 */
	public static final String[] TYPES = { "A", "I", "E" };
	/**
	 * Names for data representation types.
	 */
	public static final String[] TYPE_NAMES = { "ASCII", "Binary", "EBCDIC" };
	/**
	 * Codes for structures.
	 */
	public static final String[] STRUCTURES = { "F", "R", "P" };
	/**
	 * Codes for modes.
	 */
	public static final String[] MODES = { "S", "B", "C" };

	/**
	 * Returns the type code for a given type.
	 * 
	 * @param type
	 *            type constant as defined in FTPConstants.
	 * @return type code as defined in FTP spec.
	 */
	public static String getType(int type) {
		return TYPES[type - 1];
	}

	/**
	 * Returns the structure code.
	 * 
	 * @param structure
	 *            structure constant as defined in FTPConstants.
	 * @return structure code as defined in FTP spec.
	 */
	public static String getStructure(int structure) {
		return STRUCTURES[structure - 1];
	}

	/**
	 * Returns the mode code.
	 * 
	 * @param mode
	 *            mode constant as defined in FTPConstants.
	 * @return mode code as defined in FTP spec.
	 */
	public static String getMode(int mode) {
		return MODES[mode - 1];
	}

	/**
	 * Extracts the path from the given string.
	 * 
	 * @param reply
	 * @return Path
	 */
	public static String parsePath(String reply) {
		int index1 = reply.indexOf('\"');
		int index2 = reply.lastIndexOf('\"');
		if (index1 < 0 || index2 < 0 || index1 == index2) {
			return null;
		}
		String path = reply.substring(index1 + 1, index2);
		return path;
	}

	/**
	 * Extracts the host address from the reply of a PASV command.
	 * 
	 * @param reply
	 *            Reply of the PASV command
	 * @return Address
	 */
	public static String parseAddress(String reply) {
		String[] tokens = parsePassiveCommandReply(reply);
		return tokens[0] + "." + tokens[1] + "." + tokens[2] + "." + tokens[3];
	}

	/**
	 * @param reply
	 * @return
	 */
	public static int parsePort(String reply) {
		String[] tokens = parsePassiveCommandReply(reply);
		int port = (Integer.parseInt(tokens[4]) << 8)
				+ Integer.parseInt(tokens[5]);
		return port;
	}

	private static String[] parsePassiveCommandReply(String reply) {
		int index1 = reply.indexOf("(");
		int index2 = reply.indexOf(")", index1 + 1);
		if (index1 < 0 || index2 < 0) {
			return null;
		}
		reply = reply.substring(index1 + 1, index2);
		return reply.split(",");
	}

	/**
	 * @param ip
	 * @param port
	 * @return
	 */
	public static String getPortCommand(String ip, int port) {
		String command = "PORT " + ip.replace('.', ',') + "," + (port >> 8)
				+ "," + (port & 0x000000FF);
		return command;
	}

	/**
	 * @param attributes
	 * @return
	 */
	public static String parseAttributes(String attributes) {
		int ownerPermissions = 0;
		ownerPermissions += attributes.charAt(0) == 'r' ? 4 : 0;
		ownerPermissions += attributes.charAt(1) == 'w' ? 2 : 0;
		ownerPermissions += attributes.charAt(2) == 'x' ? 1 : 0;

		int groupPermissions = 0;
		groupPermissions += attributes.charAt(3) == 'r' ? 4 : 0;
		groupPermissions += attributes.charAt(4) == 'w' ? 2 : 0;
		groupPermissions += attributes.charAt(5) == 'x' ? 1 : 0;

		int publicPermissions = 0;
		publicPermissions += attributes.charAt(6) == 'r' ? 4 : 0;
		publicPermissions += attributes.charAt(7) == 'w' ? 2 : 0;
		publicPermissions += attributes.charAt(8) == 'x' ? 1 : 0;

		return String.valueOf(ownerPermissions)
				+ String.valueOf(groupPermissions)
				+ String.valueOf(publicPermissions);
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ex) {
				// Do nothing
			}
		}
	}

	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException ex) {
				// Do nothing
			}
		}
	}

	public static void close(ServerSocket serverSocket) {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException ex) {
				// Do nothing
			}
		}
	}
}