package com.Auth.Web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response {
	
	private Status status;
	private String message;
	private Object data;
	
	public Response(Status status) {
		this.status = status;
	}
	
	public Response(Status status, Object data) {
		this.status = status;
		this.data = data;
	}

}
