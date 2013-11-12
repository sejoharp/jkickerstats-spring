package jkickerstats.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class MongoDbSettings {
	private String host;
	private int port;
	private String dbname;
	private String user;
	private String password;

	public String getPassword() {
		return password;
	}

	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public int getPort() {
		return port;
	}

	public String getDbname() {
		return dbname;
	}
}
