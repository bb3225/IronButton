package com.github.IronButton.core;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class MainPlugin extends JavaPlugin{
	private Server server;

	public MainPlugin() {
		super();
		//getLogger().info("Creating server instance...");
		server=new Server();
		
		HandlerCollection handlers=new HandlerCollection();
		handlers.addHandler(new com.github.IronButton.handler.ProcessCmd(this));
		
		server.setHandler(handlers);
	}
	
	@Override
	public void onEnable() {
		getLogger().info("Enabling IronButton...");
		try {
			initHttpServer(80);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Server unable to start", e);
		}
		getLogger().info("IronButton has been enabled");
	}
	
	 public void onDisable() {
		getLogger().info("Disabling IronButton...");
		/*
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			getLogger().log(Level.WARNING, "Server unable to stop", e);
		}
		*/
	 }
	
	private void initHttpServer(int port) throws Exception {
		if(server.isStarted()) {
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
