package main;

import java.sql.*;


public class ConnectionHandler implements AutoCloseable{
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	private int rowsAffected;
	private static String url = "jdbc:mysql://localhost/BANKING";
	private static String user = "root";
	private static String password = "";


	
	public ConnectionHandler(String query) throws SQLException {
		con = DriverManager.getConnection(url, user, password);
		stmt = con.createStatement();
		rowsAffected = -1;
		if(stmt.execute(query)) {
			rs = stmt.getResultSet();
		} else {
			rowsAffected = stmt.getUpdateCount();
		}
	}

	public boolean execute(String query) throws SQLException {
		if(rs != null) {
			rs.close();
			rs = null;
		}
		rowsAffected = -1;
		if(stmt.execute(query)) {
			rs = stmt.getResultSet();
			return false;
		} else {
			rowsAffected = stmt.getUpdateCount();
			return true;
		}
	}
	
	public int getRowsAffected() {
		return rowsAffected;
	}
	
	public int getInt(int colNo) throws SQLException {
		return rs.getInt(colNo);
	}
	
	public int getInt(String colName) throws SQLException {
		return rs.getInt(colName);
	}
	
	public String getString(int colNo) throws SQLException {
		return rs.getString(colNo);
	}
	
	public String getString(String colName) throws SQLException {
		return rs.getString(colName);
	}
	
	public Date getDate(int colNo) throws SQLException {
		return rs.getDate(colNo);
	}
	
	public Date getDate(String colName) throws SQLException {
		return rs.getDate(colName);
	}
	
	public Double getDouble(int colNo) throws SQLException {
		return rs.getDouble(colNo);
	}
	
	public Double getDouble(String colName) throws SQLException {
		return rs.getDouble(colName);
	}
	
	public Object getObject(int colNo) throws SQLException {
		return rs.getObject(colNo);
	}
	
	public Object getObject(String colName) throws SQLException {
		return rs.getObject(colName);
	}
	
	public long getLong(int colNo) throws SQLException {
		return rs.getLong(colNo);
	}
	
	public long getLong(String colName) throws SQLException {
		return rs.getLong(colName);
	}
	
	
	public boolean next() throws SQLException {
		return rs.next();
	}
	
	
	@Override
	public void close() throws SQLException {
		if(rs != null) {
			rs.close();
		}
		stmt.close();
		con.close();
	}

}
