package com.phoenix.demos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class ConnectDatabase extends TagSupport{
	
	private String database;
	private String driver;
	private String url;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
	@Override
	public int doStartTag() throws JspException {
		switch(database)
		{
		case "mysql":
				setDriver("com.mysql.jdbc.Driver");
				url="jdbc:mysql://localhost:3306/test";
				
		default:
				setDriver("Database not supported");
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(url,"root","root");
			pageContext.getSession().setAttribute("connection", con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return SKIP_BODY;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

}
