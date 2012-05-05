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
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * This class represents a Control Connection as specified in the FTP protocol
 * specification. For more details refer to RFC 959.
 * 
 * @author Sai Pullabhotla, psai [at] jMethods [dot] com
 * @version 2.0
 */
public class ExplicitSSLControlConnection extends ControlConnection implements
		FTPConstants {

	/**
	 * Creates an instance of <code>ExplicitSSLControlConnection</code>.
	 * 
	 * @param client
	 */
	public ExplicitSSLControlConnection(FTPClient client) {
		super(client);
	}

	/**
	 * Connects to the specified remote host on the specified port number.
	 * 
	 * @param host
	 *            Host name or IP address of the remote host.
	 * @param port
	 *            Port number to connect to.
	 * @exception ConnectionException
	 *                If unable to connect to the specified host.
	 */
	@Override
	public void connect(String host, int port) throws ConnectionException,
			FTPException {
		super.connect(host, port);
		if (client.getSSLUsage() == FTPConstants.USE_EXPLICIT_SSL) {
			sslConnect(host, port);
		} else if (client.getSSLUsage() == FTPConstants.USE_SSL_IF_AVAILABLE) {
			sslConnectIfAvailable(host, port);
		}
	}

	private void sslConnect(String host, int port) throws ConnectionException,
			FTPException {

		String reply = executeCommand("AUTH " + client.getExplicitSSLProtocol());
		if (reply.charAt(0) != '2') {
			throw new FTPException(reply);
		}

		SSLContext ctx = client.getSSLContext();
		SSLSocketFactory factory = ctx.getSocketFactory();
		try {
			socket = factory.createSocket(socket, host, port, true);
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream(), true);
			((SSLSocket) socket).startHandshake();
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}

		reply = executeCommand("PBSZ 0");
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
		reply = executeCommand("PROT "
				+ (client.isDataChannelUnencrypted() ? "C" : "P"));
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
	}

	private void sslConnectIfAvailable(String host, int port)
			throws ConnectionException, FTPException {

		String reply = executeCommand("AUTH " + client.getExplicitSSLProtocol());
		if (reply.charAt(0) != '2') {
			client.setDataChannelUnencrypted(true);
			return;
		}

		SSLContext ctx = client.getSSLContext();
		SSLSocketFactory factory = ctx.getSocketFactory();
		try {
			socket = factory.createSocket(socket, host, port, true);
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream(), true);
			((SSLSocket) socket).startHandshake();
		} catch (IOException exp) {
			throw new ConnectionException(exp.toString());
		}

		reply = executeCommand("PBSZ 0");
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
		reply = executeCommand("PROT "
				+ (client.isDataChannelUnencrypted() ? "C" : "P"));
		if (reply.charAt(0) == '5' || reply.charAt(0) == '4') {
			throw new FTPException(reply);
		}
	}
}