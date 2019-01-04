package data.localDataBase;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Messages;

import jdk.nashorn.api.tree.ForInLoopTree;
import jdk.nashorn.api.tree.ForLoopTree;
import logic.ContactList;
import logic.MyInfo;
import logic.TextMessages;

public class DataTransfer {

	/**
	 * method that is not in use 
	 * creates contact list where user can store own contacts
	 * insert contacts , name ,last name and ip address
	 * 
	 * @param connection
	 * @param contact    list
	 * @param users      current ipAddress
	 * @return true if contact was inserted
	 */
	public static boolean insertContact(Connection conn, ContactList contact, String ipAddress) {

		String rep = ipAddress.replace('.', '_');

		String contacts = "contacts_" + rep;
		try {
			String sql = "Insert into " + contacts + "  (name, last_name, ip_address) values(?, ?, ? ) ";

			if (checkIp(contact, ipAddress).equals(contact.getIpAddress())) {
				System.out.println("there is contact stored by this IP address");

			} else {
				System.out.println(contact.getIpAddress());
				PreparedStatement pstm = conn.prepareStatement(sql);

				pstm.setString(1, contact.getName());
				pstm.setString(2, contact.getLastName());
				pstm.setString(3, contact.getIpAddress());

				pstm.executeUpdate();
				System.out.println("contact inserted ! ");
				return true;

			}

		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		return false;

	}
	/**
	 * send message to specific contact
	 * @param connection
	 * @param sender ip, reciever ip, text message, sent date
	 * @return true if sent
	 */

	public static boolean sendMessage(Connection conn, MyInfo myInfo, TextMessages message) {

		String messageBox = "message_box";
		try {
			String sql = "Insert into " + messageBox + "  (from_ip, to_ip, text_message,sent_date) values(?, ?, ? ,?) ";

			PreparedStatement pstm = conn.prepareStatement(sql);
			// TEST TEST
//				pstm.setString(1, message.getRecieverIp());
//				pstm.setString(2, message.getSenderIp());
			pstm.setString(1, message.getSenderIp());
			pstm.setString(2, message.getRecieverIp());
			pstm.setString(3, message.getTextMessage().substring(0, message.getTextMessage().length() - 1));
			pstm.setString(4, message.getSentDate());

			pstm.executeUpdate();

			return true;

		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		return false;

	}
/**
 * send message in group chat
 * @param conn
 * @param myInfo
 * @param message
 * @return true if message waas inserted into database table
 */
	public static boolean sendMessageLive(Connection conn, MyInfo myInfo, TextMessages message) {

		String messageBox = "message_box";
		try {
			String sql = "Insert into " + messageBox + "  (from_ip, to_ip, text_message,sent_date) values(?, ?, ? ,?) ";

			PreparedStatement pstm = conn.prepareStatement(sql);

			pstm.setString(1, message.getSenderIp());
			pstm.setString(2, message.getRecieverIp());
			pstm.setString(3, message.getTextMessage());
			pstm.setString(4, message.getSentDate());

			pstm.executeUpdate();

			return true;

		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		return false;

	}

	/**
	 * check if there is alredy that ip in use
	 * 
	 * @param contact list
	 * @param users   current ipAddress
	 * @return string of ip addres that is stored in database
	 */
	public static String checkIp(ContactList list, String ipAddress) {
		String ip = "";
		try {
			String rep = ipAddress.replace('.', '_');
			String contacts = "contacts_" + rep;

			String query = "SELECT ip_address FROM " + contacts + " where ip_address = '" + list.getIpAddress() + "';";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						ip = rs.getString("ip_address");
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}
		System.out.println("ovo je ip ad ||" + ip + "||");
		return ip;
	}

	/**
	 * create contact list for contact storage
	 * 
	 * @param ipAddress
	 */
	public static void createMyContacts(String ipAddress) {
		try {

			Statement st = ConnectToDB.getConnection().createStatement();
			String url = "jdbc:mariadb://192.168.0.12/message_app";
			String rep = ipAddress.replace('.', '_');
			String contacts = "contacts_" + rep;
			// SQL statement for creating a new table
			String sql = "CREATE TABLE IF NOT EXISTS `" + contacts + "` (\n" + "	ID int unsigned AUTO_INCREMENT,\n"
					+ "	name varchar(255) NOT NULL,\n" + " last_name varchar(255) NOT NULL,\n"
					+ " ip_address varchar(255) NOT NULL,\n" + " PRIMARY KEY  (ID)\n" + ");";
			st.execute(sql);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * users info , store in database info_db
	 * 
	 * @param connection to db
	 * @param users      info
	 * @return true if user is registered
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public static boolean insertInfoIntoServerAddressList(Connection conn, MyInfo myInfo)
			throws SocketException, UnknownHostException {

		try {
			if (checkIpForUserUpdate(myInfo).equals(myInfo.getIpAddress())) {
				System.out.println("there is contact stored by this IP address");
			} else {
				String sql = "Insert into info_db(name, last_name, ip_address) values(?,?,?)";
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setString(1, myInfo.getName());
				pstm.setString(2, myInfo.getLastName());
				pstm.setString(3, myInfo.getIpAddress());

				pstm.executeUpdate();
				return true;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}

		return false;
	}

	/**
	 * check if there is alredy that ip in use
	 * 
	 * @param contact list
	 * @param users   current ipAddress
	 * @return string of ip addres that is stored in database
	 */
	public static String checkIpForUserUpdate(MyInfo infos) {
		String ip = "";
		try {

			String info = "info_db";

			String query = "SELECT ip_address FROM " + info + " where ip_address = '" + infos.getIpAddress() + "' ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						ip = rs.getString("ip_address");
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

		return ip;
	}

	/**
	 * changes empty row of seen message when message was seen by reciever.
	 * 
	 * @param connection
	 * @param message    info
	 * @return true if date was updated
	 * @throws Exception
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public static boolean sentMessageCheck(Connection conn, TextMessages message)
			throws SocketException, UnknownHostException {

		String db = "message_box";
		try {
			String sql = "Update " + db + " set seen_date = ?  where to_ip=? AND seen_date IS NULL";

			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(2, message.getRecieverIp());
			pstm.setString(1, message.getSeenDate());

//				System.out.print(message.getRecieverIp() +"  "+message.getSeenDate() );
			if (pstm.executeUpdate() > 1) {

				return true;
			}

			pstm.executeUpdate();

		} catch (SQLException sqle) {

			sqle.getMessage();
		}
		return false;
	}

	/**
	 * read message from another user
	 * 
	 * @param infos
	 */
	public static void readMessage(MyInfo infos) {

		try {
			String info = "message_box";

			String query = "SELECT text_message , sent_date, from_ip  FROM " + info + " where to_ip = '"
					+ infos.getIpAddress() + "' ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {

					while (rs.next()) {

						String mf = rs.getString("from_ip");
						String m = rs.getString("text_message");
						String mi = rs.getString("sent_date");

						System.out.println(findNameInTableMessageBox(mf) + "> " + m + " @" + mi);

					}

					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

	}

	/**
	 * read message from another user
	 * 
	 * @param infos
	 */
	public static String readMessageLive() {
		TextMessages tm = new TextMessages();
	
		String seen = "";
		try {
			String info = "message_box";

			String query = "SELECT text_message , sent_date ,from_ip,seen_date  FROM " + info
					+ " where to_ip = 'all' AND seen_date IS NULL ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {

					while (rs.next()) {

						String mf = rs.getString("from_ip");
						String m = rs.getString("text_message");
						String mi = rs.getString("sent_date");
						seen = rs.getString("seen_date");

						if (seen == null) {
							tm.setRecieverIp("all");
							tm.getSeenDate();
							DataTransfer.sentMessageCheck(data.localDataBase.ConnectToDB.getConnection(), tm);
							
							System.out.println(findNameInTableMessageBox(mf) + "> " + m + "         @" + mi);
						   
						}

					}

					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

		return seen;
	}

	/**
	 * method only for READMESSAGE method
	 * 
	 * @param ip
	 * @return
	 */
	public static String findNameInTableMessageBox(String ip) {
		String name = "";
		try {

			String info = "info_db";

			String query = "SELECT name FROM " + info + " where ip_address = '" + ip + "' ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						name = rs.getString("name");
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

		return name;
	}

	/**
	 * finds name in table info_db
	 * 
	 * @param infos
	 * @return name
	 */
	public static String findName(MyInfo infos) {
		String name = "";
		try {

			String info = "info_db";

			String query = "SELECT name FROM " + info + " where ip_address = '" + infos.getIpAddress() + "' ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						name = rs.getString("name");
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

		return name;
	}

	/**
	 *  get id from table 
	 * @param id
	 * @return id
	 */
	public static String selectId(String id) {
		String name = "";
		try {

			String info = "info_db";

			String query = "SELECT ip_address FROM " + info + " where id = '" + id + "' ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						name = rs.getString("ip_address");
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

		return name;
	}

	/**
	 *  return name from table 
	 * @param id
	 * @return name
	 */
	public static String selectNamefromId(String id) {
		String name = "";
		try {

			String info = "info_db";

			String query = "SELECT name FROM " + info + " where id = '" + id + "' ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						name = rs.getString("name");
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

		return name;
	}

	/**
	 * print all the names from table 
	 */
	public void selectAllNames() {
		String name = "";
		String id = "";

		try {

			String info = "info_db";

			String query = "SELECT id,name FROM " + info + " ;";
			PreparedStatement st = ConnectToDB.getConnection().prepareStatement(query);
			boolean isRes = st.execute();
			do {
				try (ResultSet rs = st.getResultSet()) {
					while (rs.next()) {
						id = rs.getString("id");
						name = rs.getString("name");

						System.out.println(id + "  " + name);
					}
					isRes = st.getMoreResults();

				}
			} while (isRes);
		} catch (Exception e) {
			e.getMessage();
		}

	}

}
