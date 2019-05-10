package sample;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnectionClient {

	private String ip;
	private int port; 
	
	public Client(String ip, int port, Consumer<Serializable> callback) {
		super(callback);
		this.ip = ip;
		this.port = port;
		//isServer = false;
	}

	@Override
	protected boolean isServer() {
	
		return false;
	}

	@Override
	protected String getIP() {
		// TODO Auto-generated method stub
		return this.ip;
	}

	@Override
	protected int getPort() {
		// TODO Auto-generated method stub
		return this.port;
	}
	public void setPort(int newPort)
	{
		this.port = newPort;
	}
	public void setIp(String newIP)
	{
		this.ip = newIP;
	}

}
