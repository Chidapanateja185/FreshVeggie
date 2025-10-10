package com.Auth.Web;

public class ResponseStatus {
	
	public static final Status REQUEST_SUCCESS = new Status(200, "Request_Success");
	public static final Status BAD_REQUEST = new Status(401, "Bad_Request");
	public static final Status MISSING_FEILDS = new Status(1001, "Missing Feilds");
	public static final Status EMAIL_ALREADY_EXISTS = new Status(1002, "Email Already Exists please Login");
	public static final Status VERIFICATION_LINK_EXPERIED = new Status(1003, "Verification link expired. Please register again.");
	public static final Status PASSWORD_NOT_VALIED = new Status(1004, "PassWord Validation");
	public static final Status PASSWORD_NOT_MATCHES = new Status(1005, "Password does not matches with conform passwrod");
}
