package com.Auth.Web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Status {
	
	private int code;
	private String message;
	
	public Status(Status status) {
		super();
		this.code = status.code;
		this.message = status.message;
	}
}
