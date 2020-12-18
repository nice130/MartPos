package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.Form;

public class Authentication {
	protected HttpServletRequest req;
	protected Form form;
	protected HttpSession session;
	
	protected Authentication() {
		
	}
}
