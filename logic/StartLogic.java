package logic;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import com.mysql.jdbc.Messages;

import data.localDataBase.ConnectToDB;
import data.localDataBase.DataTransfer;
import logic.ContactList;
import logic.MyInfo;
import logic.TextMessages;

public class StartLogic {

	public void run() {
		Scanner sc = new Scanner(System.in);
		String input;

		MyInfo myInfo = new MyInfo();

		TextMessages textMessages = new TextMessages();
		ContactList contactList = new ContactList();
		DataTransfer dataTransfer = new DataTransfer();

		do {

			try {
				if (dataTransfer.checkIpForUserUpdate(myInfo).equals(myInfo.getIpAddress())) {
					System.out.println("Welcome to MessagesApp!");
					System.out.println("welcome " + dataTransfer.findName(myInfo) + " !");
				} else {
					try {
						System.out.println(
								" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
						System.out.println("Welcome to MessagesApp!");
						System.out.println("for firstTimers :");
						System.out.println(
								"to list names, type 'list' . to send message, type 'send' . enter 'g' to have group chat ");
						System.out.println(" and read messages with 'read'");
						System.out.println("to end message use ';' where ever you like");
						System.out.println(
								" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
						myInfo.getIpAddress();
						System.out.println("your name : ");
						input = sc.nextLine();
						myInfo.setName(input);
						System.out.println("your last name : ");
						input = sc.nextLine();
						myInfo.setLastName(input);

						dataTransfer.insertInfoIntoServerAddressList(data.localDataBase.ConnectToDB.getConnection(),
								myInfo);
						System.out.println("hello " + dataTransfer.findName(myInfo) + " !");

					} catch (Exception e) {
						System.out.println("exception in transfering info into info_db ");
						e.getMessage();
					}
				}
			} catch (SocketException | UnknownHostException e1) {

				e1.getMessage();
			}

			do {
				System.out.println("list , read , send , g ");

				input = sc.nextLine();
				switch (input) {
				case "list":
					dataTransfer.selectAllNames();
					break;
				case "read":
					dataTransfer.readMessage(myInfo);
					try {
						textMessages.setRecieverIp(myInfo.getIpAddress());

					} catch (SocketException | UnknownHostException e1) {
						e1.getMessage();
					}
					try {
						dataTransfer.sentMessageCheck(data.localDataBase.ConnectToDB.getConnection(), textMessages);
					} catch (Exception e1) {
						e1.getMessage();
					}

					System.out.println();
					break;
				case "send":
					String message = "";
					System.out.println("enter id : ");
					input = sc.nextLine();
					System.out.println("to " + dataTransfer.selectNamefromId(input));
					if (dataTransfer.selectNamefromId(input) == "") {
						System.out.println("there is no such id as " + input);
						break;
					}
					do {
						System.out.print("> ");
						message = message + " " + sc.nextLine();

					} while (!message.substring(message.length() - 1).endsWith(";"));
					textMessages.setTextMessage(message);
					textMessages.setRecieverIp(dataTransfer.selectId(input));
					try {
						textMessages.setSenderIp(myInfo.getIpAddress());
					} catch (SocketException | UnknownHostException e1) {
						e1.getMessage();
					}

					try {
						dataTransfer.sendMessage(data.localDataBase.ConnectToDB.getConnection(), myInfo, textMessages);
						System.out.println("message sent ");
					} catch (Exception e) {
						e.getMessage();
					}
					break;
				case "g":
					do {
						System.out.print("Me>");
						input = sc.nextLine();
						if (input.equals(";")) {
							break;
						}

						if (input.equals("")) {
							dataTransfer.readMessageLive();
							try {
								textMessages.setRecieverIp(myInfo.getIpAddress());
							} catch (SocketException | UnknownHostException e) {
								e.getMessage();
							}
							try {
								dataTransfer.sentMessageCheck(data.localDataBase.ConnectToDB.getConnection(),
										textMessages);
							} catch (Exception e) {
								e.getMessage();
							}

						} else if (input.contains(input)) {

							textMessages.setTextMessage(input);
							try {
								textMessages.setSenderIp(myInfo.getIpAddress());
							} catch (SocketException | UnknownHostException e) {
								e.getMessage();
							}
							textMessages.setRecieverIp("all");
							try {
								dataTransfer.sendMessageLive(data.localDataBase.ConnectToDB.getConnection(), myInfo,
										textMessages);
							} catch (Exception e) {
								e.getMessage();
							}

						}
					} while (true);

				}
				System.out.println("____________________");
			} while (!input.equals("exit"));

			System.out.println("you sure y/n?");

			input = sc.nextLine();

		} while (!input.equals("y"));
		System.out.println("bye bye");
	}

}
