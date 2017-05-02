package com.appgree.netmonitor;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

public class Monitor implements Runnable {

	final static Logger LOGGER = Logger.getLogger(Monitor.class);

	private MonitorConfig config;

	public Monitor(MonitorConfig config) throws IOException {
		this.config = config;
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		InetAddress inet;
		try {
			LOGGER.info(String.format("Monitoring ip on port %s", config.getAddress()));
			inet = InetAddress.getByName(config.getAddress());
		} catch (IOException e) {
			LOGGER.error(String.format("Error monitoring host %s", config.getAddress()), e);
			return;
		}

		for (;;) {
			try {
				boolean isReachable = inet.isReachable(config.getTimeout());
				if (!isReachable) {
					LOGGER.error(String.format("Unreachable node detected %s", config.getAddress()));
					executeCommand();
				}
				
				// waits for next cycle
				try {
					Thread.sleep(config.getRefreshDelay());
				} catch (InterruptedException e) {
				}
			} catch (Exception e) {
				LOGGER.error(String.format("Error processing monitoring cycle name = %s. Retrying in 5s...", config.getName()), e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}
			}
		}
	}

	private void executeCommand() throws IOException {
		String command = String.format("\"%s\" \"%s\" %s", config.getCommandPath(), config.getName(), config.getAddress());
		LOGGER.info("Executing command " + command);
		final long start = System.currentTimeMillis();
		Process p = Runtime.getRuntime().exec(command);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
		}
		final long end = System.currentTimeMillis();
		LOGGER.info(String.format("Monitor command %s took %dms", command, (end - start)));
	}
}
