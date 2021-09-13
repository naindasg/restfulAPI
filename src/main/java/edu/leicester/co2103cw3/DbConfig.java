package edu.leicester.co2103cw3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;

/**
 * Creates a connection to the DB.
 * 
 * If you want to use it remotely (not on a lab computer) set
 * <code>REMOTE = true</code> and create file <tt>CO2103.mysql.properties</tt>
 * in your home directory with contents:
 * 
 * <pre>
 * username=abc123
 * dbPassword=asdaskld  # the one from ~/.my.cnf 
 * linuxPassword=54646454  # the password you use in the lab
 * </pre>
 * 
 * If you want to use SSH, make sure to include the gradle dependency
 * 
 * <pre>
 * compile 'com.jcraft:jsch:0.1.55'
 * </pre>
 */
@Configuration
public class DbConfig {

	final boolean REMOTE = false;
	final String REMOTE_HOST = "mysql.mcscw3.le.ac.uk"; 

//	public static Session session;

	@Bean
	public DriverManagerDataSource dataSource() {
		String path = System.getProperty("user.home") + "/CO2103.mysql.properties";
		if (Files.isReadable(Paths.get(path))) {
			Properties mysqlProps = new Properties();
			try {
				mysqlProps.load(new FileInputStream(path));
			} catch (Exception e) {
				e.printStackTrace();
			}

			String dbPassword = mysqlProps.getProperty("dbPassword");
//			String linuxPassword = mysqlProps.getProperty("linuxPassword");
			String user = mysqlProps.getProperty("username");

			try {
				Files.write(Paths.get("gradle/db.conf"), (System.currentTimeMillis() +  ": DbConfig used from " + path + "\n").getBytes(),
						StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			} catch (IOException e) {
			}

			String host = REMOTE ? "127.0.0.1" : REMOTE_HOST;
			int port = REMOTE ? 3307 : 3306;
//			if (REMOTE && (session == null || !session.isConnected())) {
//				JSch.setConfig("StrictHostKeyChecking", "no");
//				JSch jsch = new JSch();
//				try {
//					session = jsch.getSession(user, "xanthus.mcscw3.le.ac.uk", 22);
//					session.setPassword(linuxPassword);
//					session.connect();
//					session.setPortForwardingL(3307, REMOTE_HOST, 3306);
//				} catch (JSchException e) {
//					e.printStackTrace();
//					session.disconnect();
//				}
//			}

			DriverManagerDataSource ds = new DriverManagerDataSource();
			ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
			ds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + user);
			ds.setUsername(user);
			ds.setPassword(dbPassword);
			return ds;
		} else {
			try {
				Files.write(Paths.get("gradle/db.conf"), (System.currentTimeMillis() +  ": No DbConfig in " + path + "\n").getBytes(),
						StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			} catch (IOException e) {
			}
			throw new RuntimeException("Place your username and passwords in file " + path);
		}
	}
}