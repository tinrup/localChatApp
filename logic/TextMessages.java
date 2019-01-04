package logic;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * sends, recieves text messages
 * @author tin
 *
 */
public class TextMessages {


	private String textMessage;
	private String recieverIp;
	private String senderIp;
	private boolean messageRead;
	private String sentDate;	
	private String seenDate;
	
	
	
	

	public String getTextMessage() {
		
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	public String getRecieverIp() {
		return recieverIp;
	}

	public void setRecieverIp(String recieverIp) {
		this.recieverIp = recieverIp;
	}

	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public boolean isMessageRead() {
		return messageRead;
	}

	public void setMessageRead(boolean messageRead) {
		this.messageRead = messageRead;
	}

	
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public void setSeenDate(String seenDate) {
		this.seenDate = seenDate;
	}
/**
 * 
 * @return message sent date
 */
	public String getSentDate() {
		String sentDate = LocalDateTime.now().getDayOfMonth() +"." +LocalDateTime.now().getMonthValue()+"."
				+LocalDateTime.now().getYear()+"-"+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute();
		return sentDate;
	}

	/**
	 * 
	 * @return mesage seen date
	 */
	public String getSeenDate() {
		String seenDate = LocalDateTime.now().getDayOfMonth() +"." +LocalDateTime.now().getMonthValue()+"."
				+LocalDateTime.now().getYear()+"-"+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute();
		return seenDate;
	}


	
	

}
