# localChatApp

LocalChatApp project is based on sending messages from one computer(IP address) to Centos7 Server where database is located, and from server to another computer with different IP address.
Executing the program, each user is stored in server database by their Ip address. 
User can view contact list, read messages, send messages to specific contacts or enter the group chat with all users.
Sending messages is acceptable only if the Server has a recipient ip address.

Add MariaDb connector to your project library ( https://mariadb.com/kb/en/library/about-mariadb-connector-j/ ) .

When creating database on Centos7 (MariaDb), database must have a host called '%' to recieve data from all users.
( CREATE USER 'name'@'%' IDENTIFIED BY 'somePassword';
  GRANT ALL PRIVILEGES ON *.* TO 'name'@'%'; )

Connecting to Centos database can cause some problems, you need to disable the server firewall first. 
( systemctl stop firewalld )

_______________________________________________________

centos simple database : 
CREATE DATABASE message_app;

tables :
CREATE TABLE info_db
(
  id              INT unsigned NOT NULL AUTO_INCREMENT, 
  name            VARCHAR(150) NOT NULL,
  last_name       VARCHAR(150) NOT NULL,
  ip_address      VARCHAR(150) NOT NULL,
  PRIMARY KEY     (id)
);

CREATE TABLE message_box
(
  id                 INT unsigned NOT NULL AUTO_INCREMENT, 
  from_ip            VARCHAR(150) NOT NULL,
  to_ip              VARCHAR(150) NOT NULL,
  text_message       VARCHAR(10000) NOT NULL,
  sent_date          VARCHAR(150) NOT NULL,
  seen_date          VARCHAR(150) NULL,
  PRIMARY KEY        (id)
);




