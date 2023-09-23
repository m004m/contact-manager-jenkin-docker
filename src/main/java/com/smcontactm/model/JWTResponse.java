package com.smcontactm.model;

public class JWTResponse {
	
	private String jwtToken;
	
	private String statusMsg;

	public JWTResponse() {
		super();
	}

	public JWTResponse(String jwtToken, String statusMsg) {
		super();
		this.jwtToken = jwtToken;
		this.statusMsg = statusMsg;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	

}
