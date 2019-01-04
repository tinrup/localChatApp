package logic;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * user information 
 * sets ip addres for user 
 * stores it inside info_db table
 * @author tin
 *
 */
public class MyInfo {
	
	
	private String name;
	private String lastName;
	private String ipAddress;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * 
	 * @return iv4 address
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public  String getIpAddress() throws SocketException, UnknownHostException {
		String ipv4;
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  ipv4 = socket.getLocalAddress().getHostAddress();
				
			}
		return ipv4;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
	

}
