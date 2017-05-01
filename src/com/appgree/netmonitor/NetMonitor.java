package com.appgree.netmonitor;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class NetMonitor {
	
	final static Logger LOGGER = Logger.getLogger(NetMonitor.class);
	
	private Configuration configuration;
	
	private ArrayList<Listener> listeners = new ArrayList<>();

	public static void main(String[] args) {
		
		if (args.length != 1) {
			LOGGER.error("Invalid number of arguments. Please enter the configuration file path");
			System.exit(1);
			return;
		}
		
		try {
			new NetMonitor().start(args[0]);
		} catch (Exception e) {
			LOGGER.error("Exception executing initialization", e);
			System.exit(2);
		}
	}

	private void start(String configFilePath) throws Exception {
		LOGGER.info("Starting NetMonitor");
		configuration = new Configuration(configFilePath);
		startListeners();
	}

	private void startListeners() throws IOException {
		ArrayList<ListenerConfig> listenersConfig = configuration.getListeners();
		for (ListenerConfig listenerConfig: listenersConfig) {
			Listener listener = new Listener(listenerConfig);
			listeners.add(listener);
		}
	}
}
