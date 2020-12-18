package controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import beans.Employee;


public class DataAccessObject {
	protected Connection connect;
	protected PreparedStatement pstmt;
	protected ResultSet rs;
	protected Statement stmt;

	public DataAccessObject() {

	}

	public void dbConnect() {
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource)init.lookup("java:comp/env/POSPROJECT");
			try {
				connect = ds.getConnection();
			} catch(SQLException e) {
				e.printStackTrace();
				try {
					if(!connect.isClosed()) {connect.close();}
				}catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch(NamingException e) {
			e.printStackTrace();
		}
	}

	public void dbClose() {
		try {
			if(!connect.isClosed()) connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setTransaction(boolean state) {
		try {
			connect.setAutoCommit(state);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void setTransactionResult(boolean state) {
		try {

			if(state) connect.commit();
			else connect.rollback(); 
		}catch (SQLException e) {e.printStackTrace();}

	}

	public boolean isCheck(int value) {
		return (value == 1)? true: false;
	}

	public void getEmpInfo(Employee emp) {
		String query = "SELECT JOCODE AS JOB,NAME,TO_CHAR(LASTLOGDATE,'YYYY-MM-DD HH24:MI:SS')AS LASTLOGDATE FROM ECLIPSE_DBA.USERDATE WHERE CODE = ?";

		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, emp.getEmCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				emp.setEmName(rs.getNString("NAME"));
				emp.setLastAccessTime(rs.getNString("LASTLOGDATE"));
				emp.setJobCode(rs.getNString("JOB"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isEmployee(Employee emp) {
		boolean result = false;
		String query = "SELECT COUNT(*) AS CNT FROM EM WHERE EM_CODE = '"+emp.getEmCode()+"'";

		try {
			stmt = connect.createStatement();
			rs = stmt.executeQuery(query);

			while(rs.next()) {
				result = isCheck(rs.getInt("CNT"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	} 

	public boolean isAuth(Employee emp) {
		boolean result = false;
		String query = "SELECT COUNT(*) AS CNT FROM EM WHERE EM_CODE = ? AND EM_PWD = ?";
		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setString(1, emp.getEmCode());
			pstmt.setString(2, emp.getEmPwd());

			rs = pstmt.executeQuery();

			while(rs.next()) {
				result = isCheck(rs.getInt("CNT"));
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	public boolean insEmpLog(Employee emp) {
		boolean result = false;
		String query ="INSERT INTO AC(AC_EMCODE, AC_DATE, AC_STATE) VALUES(?,DEFAULT,?)";

		try {
			pstmt = connect.prepareStatement(query);
			pstmt.setNString(1, emp.getEmCode());
			pstmt.setInt(2, emp.getLogType());
			result = (pstmt.executeUpdate()==1)? true:false;

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

}
