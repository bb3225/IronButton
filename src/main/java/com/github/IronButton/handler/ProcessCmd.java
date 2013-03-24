package com.github.IronButton.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Represents a handler that is used for process command from remote
 * @author circuitcoder
 * @see com.github.IronButton.core.MainPlugin
 */
public class ProcessCmd extends AbstractHandler{
	
	/**
	 * The plugin the object belongs to<br />
	 * Assigned when the object is created
	 */
	private JavaPlugin plugin;
	public ProcessCmd(JavaPlugin thePlugin) {
		plugin=thePlugin;
	}

	/**
	 * Tries to process the command according to the request
	 */
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//TODO: move to parent class
		//plugin.getLogger().info("Got request");
		String[] querys=request.getQueryString().split("&");
		//plugin.getLogger().info("Argument count: "+querys.length);
		HashMap<String,String> queryMap=new HashMap<String,String>();
		String[] pair=new String[2];
		for(int i=0;i<querys.length;i++) {
			pair=querys[i].split("=");
			queryMap.put(pair[0], pair[1]);
			plugin.getLogger().info(pair[0]+'\t'+pair[1]+'\n');
		}
		
		final PrintWriter output=response.getWriter();
		
		if(!queryMap.containsKey("method")) {
			output.println("#!NoMethod");
			output.flush();
			output.close();
		}
		
		if(queryMap.get("method")=="processCommand") return;
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html;charset=utf-8");
		String cmd=queryMap.get("command");
		if(cmd==null) {
			output.println("#!NullCommand");
			output.flush();
			output.close();
			return;
		}
		plugin.getLogger().info("Running from Remote: "+cmd);
		//TODO: FIXME
		/*
		plugin.getLogger().addHandler(new Handler() {
			@Override
			public void close() throws SecurityException {			}
			@Override
			public void flush() {			}
			@Override
			public void publish(LogRecord log) {
				if(output.checkError()){
					plugin.getLogger().removeHandler(this);
					return;
				}
				//TODO: add time
				output.println("["+log.getLevel()+"] "
						+"["+log.getLoggerName()+"] "+log.getMessage());
			}
			
		});
		*/
		
		boolean cmdFound=plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
		if(!cmdFound) output.println("#!CommandNotFound");
		else output.println("#!ProcessSuccess");
		output.flush();
		output.close();
	}

}
