package services.auth;

import java.sql.*;

import beans.Employee;

public class DataAccessObject extends controller.DataAccessObject {	
	public DataAccessObject() {
		dbConnect();
	}

}
