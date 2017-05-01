package com.appgree.netmonitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The configuration class
 *
 */
public class Configuration {
	
	final static Logger LOGGER = Logger.getLogger(Configuration.class);
	
	private ArrayList<ListenerConfig> listeners = new ArrayList<>();
	
	/**
	 * The configuration constructor
	 * @param configPath The configuration file path
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Configuration(String configPath) throws Exception {
		
		LOGGER.info("Loading configuration");
		
		File xmlFile = new File(configPath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList listeners = doc.getElementsByTagName("listener");
		
		LOGGER.info("Loading " + listeners.getLength() + " listeners");
		
		for (int i=0; i<listeners.getLength(); ++i) {
			Node listener = listeners.item(i);
			NodeList entries = listener.getChildNodes();
			
			String name = null;
			int port = -1;
			String commandPath = null;
			for (int j=0; j<entries.getLength(); ++j) {
				Node entry = entries.item(j);
				String nodeName = entry.getNodeName();
				if (nodeName.equals("name")) {
					name = entry.getTextContent();
				}
				else if (nodeName.equals("port")) {
					port = Integer.valueOf(entry.getTextContent());
				}
				else if (nodeName.equals("command")) {
					commandPath = entry.getTextContent();
				}
			}
			if (port == -1) {
				throw new Exception("Missing listener port number entry in configuration");
			}
			if (name == null) {
				throw new Exception("Missing listener name entry in configuration");
			}
			if (commandPath == null) {
				throw new Exception("Missing listener commandPath entry in configuration");
			}
			
			addListener(name, port, commandPath);
		}
		
	}

	private void addListener(String name, int port, String commandPath) {
		listeners.add(new ListenerConfig(name, port, commandPath));
	}

	/**
	 * @return the listeners
	 */
	public ArrayList<ListenerConfig> getListeners() {
		return listeners;
	}
}
