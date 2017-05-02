package com.appgree.netmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Listener implements Runnable {

	final static Logger LOGGER = Logger.getLogger(Listener.class);

	private ListenerConfig config;

	private ServerSocket serverSocket;

	public Listener(ListenerConfig config) throws IOException {
		this.config = config;
		Thread thread = new Thread(this);
		serverSocket = new ServerSocket(config.getPort());
		thread.start();
	}

	@Override
	public void run() {
		for (;;) {
			Socket clientSocket = null;
			try {
				LOGGER.info(String.format("Listening on port %d", config.getPort()));
				clientSocket = serverSocket.accept();
				String clientIp = clientSocket.getRemoteSocketAddress().toString();
				
				LOGGER.error("Waking up listener " + config.getName() + " from ip " + clientIp);
				String inputData = readInput(clientSocket, clientIp);
				
				closeSocket(clientSocket);
				clientSocket = null;
				
				executeCommand(clientIp, inputData);
			} catch (IOException e) {
				LOGGER.error(String.format("Error processing listening cycle name = %s. Retrying in 5s...", config.getName()), e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}
			}
			finally {
				closeSocket(clientSocket);
				clientSocket = null;
			}
		}
	}

	private void closeSocket(Socket clientSocket) {
		if (clientSocket == null) {
			return;
		}
		try {
			clientSocket.close();
		} catch (IOException e) {}
	}

	private String readInput(Socket clientSocket, String clientIp) throws IOException {
		InputStream input = clientSocket.getInputStream();
		StringBuffer buffer = new StringBuffer();
		if (input == null) {
			LOGGER.error("Listener " + config.getName() + " from ip " + clientIp + " no valid input stream");
			return "";
		}
		byte inputBytes[] = new byte[256];
		for (;;) {
			final int readed = input.read(inputBytes, 0, inputBytes.length);
			if (readed < 0) {
				break;
			}
			buffer.append(new String(inputBytes, 0, readed));
		}
		return buffer.toString();
	}

	private void executeCommand(String clientIp, String inputData) throws IOException {
		String command = String.format("%s \"%s\" %s \"%s\"", config.getCommandPath(), config.getName(), clientIp, inputData);
		LOGGER.info("Executing command " + command);
		final long start = System.currentTimeMillis();
		Process p = Runtime.getRuntime().exec(command);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
		}
		final long end = System.currentTimeMillis();
		LOGGER.info(String.format("Listener command %s took %dms", command, (end - start)));
	}
}
