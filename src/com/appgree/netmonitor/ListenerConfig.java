package com.appgree.netmonitor;

import org.apache.log4j.Logger;

public class ListenerConfig {
	
	final static Logger LOGGER = Logger.getLogger(ListenerConfig.class);

	private String name;
	private int port;
	private String commandPath;
	public ListenerConfig(String name, int port, String commandPath) {
		this.name = name;
		this.port = port;
		this.commandPath = commandPath;
		LOGGER.info(String.format("Loading listener name=%s port=%d command=%s", name, port, commandPath));
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @return the commandPath
	 */
	public String getCommandPath() {
		return commandPath;
	}

}
