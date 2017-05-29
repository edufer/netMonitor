package com.appgree.netmonitor;

import org.apache.log4j.Logger;

public class MonitorConfig {
	
	final static Logger LOGGER = Logger.getLogger(MonitorConfig.class);

	private String name;
	private int timeout;
	private String commandPath;
	private long refreshDelay;
	private int nPackets;
	private String address;

	public MonitorConfig(String name, long refreshDelay, int timeout, int nPackets, String address, String commandPath) {
		this.name = name;
		this.timeout = timeout;
		this.refreshDelay = refreshDelay;
		this.nPackets = nPackets;
		this.commandPath = commandPath;
		this.address = address;
		LOGGER.info(String.format("Loading monitor name=%s timeout=%d address=%s command=%s", name, timeout, address, commandPath));
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * @return the commandPath
	 */
	public String getCommandPath() {
		return commandPath;
	}

	/**
	 * @return the refreshDelay
	 */
	public long getRefreshDelay() {
		return refreshDelay;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the nPackets
	 */
	public int getNPackets() {
		return nPackets;
	}

}
