package com.github.IronButton.core;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;

/**
 * A implement of the Bukkit Plugin API
 * Provides access to the Server for handlers/processors 
 * @author circuitcoder
 */

public class MainPlugin extends JavaPlugin{
	private Server server;

	/**
	 * Initializes the HTTP Server and set the handler
	 */
	public MainPlugin() {
		super();
		//getLogger().info("Creating server instance...");
		server=new Server();
		
		HandlerCollection handlers=new HandlerCollection();
		handlers.addHandler(new com.github.IronButton.handler.ProcessCmd(this));
		
		server.setHandler(handlers);
	}
	
	/**
	 * Called when this plugin is enabled
	 * <p />
	 * Opens the server port (default: 8093)
	 */
	@Override
	public void onEnable() {
		getLogger().info("Enabling IronButton...");
		try {
			initHttpServer(8093);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Server unable to start", e);
		}
		getLogger().info("IronButton has been enabled");
	}
	
	/**
	 * Called when this plugin is disabled
	 * <p />
	 * Tries to stop the server
	 */
	 public void onDisable() {
		getLogger().info("Disabling IronButton...");
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			getLogger().log(Level.WARNING, "Server unable to stop", e);
		}
	 }
	
	 /**
	  * (Re)creates the connector and start the server
	  * @param port [int] (the TCP port that server listens to)
	  * @throws Exception
	  */
	private void initHttpServer(int port) throws Exception {
		if(!server.isStopped()) {
			getLogger().info("Server is started");
			return;
		}
		ServerConnector connector=new ServerConnector(server);
		connector.setPort(port);
		server.setConnectors(new Connector[] {connector});
		
		server.start();
		//server.join();
	}
}
