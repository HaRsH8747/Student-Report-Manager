package application;

import java.sql.*;

public class MysqlModel {
	Connection connection;
	PreparedStatement preparedStatement = null;
	ResultSet resultset = null;
	public MysqlModel()
	{
		connection = MysqlConnection.Connector();
		if(connection == null) System.exit(0);
	}
	
	public boolean isDbConnected() {
		try {
			return !connection.isClosed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isAdmin(String user,String pass) throws SQLException {
		String query = "select * from admin_details where username = ? and password = ?";
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);
			resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}finally {
			preparedStatement.close();
			resultset.close();
		}
	}
	
	public boolean isTeacher(String user,String pass) throws SQLException {
		String query = "select * from teacher_details where username = ? and password = ?";	
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, pass);
			resultset = preparedStatement.executeQuery();
			if(resultset.next()) 
			{
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}finally {
			preparedStatement.close();
			resultset.close();
		}
	}
}
