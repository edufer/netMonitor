package com.appgree.netmonitor;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class NetMonitor {
	
	final static Logger LOGGER = Logger.getLogger(NetMonitor.class);
	
	private Configuration configuration;
	
	private static String VERSION = "1.2.0";
	
	private ArrayList<Listener> listeners = new ArrayList<>();
	private ArrayList<Monitor> monitors = new ArrayList<>();

	public static void main(String[] args) {
		
		if (args.length != 1) {
			final String format = String.format("Version %s%sPlease enter the configuration file path", VERSION, "%s");
			System.out.println(String.format(format, "\n"));
			LOGGER.error(String.format(format, " "));
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
		startMonitors();
	}

	private void startMonitors() throws IOException {
		ArrayList<MonitorConfig> MonitorsConfig = configuration.getMonitors();
		for (MonitorConfig monitorConfig: MonitorsConfig) {
			Monitor monitor = new Monitor(monitorConfig);
			monitors.add(monitor);
		}
	}

	private void startListeners() throws IOException {
		ArrayList<ListenerConfig> listenersConfig = configuration.getListeners();
		for (ListenerConfig listenerConfig: listenersConfig) {
			Listener listener = new Listener(listenerConfig);
			listeners.add(listener);
		}
	}
}
