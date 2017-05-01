package com.appgree.netmonitor;

import java.io.IOException;
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
			try {
				LOGGER.info(String.format("Listening on port %d", config.getPort()));
				Socket clientSocket = serverSocket.accept();
				String clientIp = clientSocket.getRemoteSocketAddress().toString();
				LOGGER.error("Waking up listener " + config.getName() + " from ip " + clientIp);
				clientSocket.close();
				executeCommand(clientIp);
			} catch (IOException e) {
				LOGGER.error(String.format("Error processing listening cycle name = %s. Retrying in 5s...", config.getName()), e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}
			}
		}
	}

	private void executeCommand(String clientIp) throws IOException {
		String command = String.format("\"%s\" \"%s\" %s", config.getCommandPath(), config.getName(), clientIp);
		LOGGER.info("Executing command " + command);
		Process p = Runtime.getRuntime().exec(command);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
		}
	}
}
