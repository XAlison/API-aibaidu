package org.singleton.aibaidu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ocr")
public class Config {
	
	public String clientId;
	
	public String clientSecret;
	
	public String upladfile;
	
	

	public String getUpladfile() {
		return upladfile;
	}

	public void setUpladfile(String upladfile) {
		this.upladfile = upladfile;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	
	
}
