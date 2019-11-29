package com.phoenix.demos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DataStore extends TagSupport {
	
	private String table;
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int doStartTag()
	{		
		HttpServletRequest req=(HttpServletRequest)pageContext.getRequest();
		Map<String,String[]> entries= req.getParameterMap();
		String createTable = createTable(entries) ;
		String insert= insertQuery(entries);	
		executeQuery(createTable);
		executeQuery(insert);
		return EVAL_PAGE;
	}

	
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		Connection con=(Connection) pageContext.getSession().getAttribute("connection");
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	public void executeQuery(String query){
		try {
			System.out.println(query);
			Connection con=(Connection) pageContext.getSession().getAttribute("connection");
			Statement stmt=con.createStatement();
			int updateRowCount = stmt.executeUpdate(query);
			if(updateRowCount != 0)
				pageContext.getOut().write("Successfully written in database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String createTable(Map<String, String[]> enteries) {
		int counter=1;
		String tableQuery = "CREATE TABLE IF NOT EXISTS  " + table + " ( " ;
		for(Map.Entry entry : enteries.entrySet())
		{
			int n = enteries.size();
			if(counter<enteries.size()-1)
			{
			tableQuery+=entry.getKey() + " varchar(255), ";
			}
			else {
				if(counter!= enteries.size())
					tableQuery+=entry.getKey() + " varchar(255)); ";
			}		
			counter++;
		}
	return tableQuery;
}
	
	
	
	public String insertQuery(Map<String, String[]> enteries) {
		String query="insert into ";
				query+= table;
				query+=" values(";
				try {
					int counter=1;
					for(Map.Entry entry:enteries.entrySet())
					{;
						String name=(String) entry.getKey();
						String[] values=(String[]) entry.getValue();
						System.out.println(values[0]);
						if(counter<enteries.size()-1)
						{
							query+= ("\'"+values[0]+"\',");
						}
						else
						if(counter!=enteries.size())
						{
							query+="\'"+values[0]+"\'";
						}
						counter++;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println(e1.getMessage());
				}
				query+=")";		
		return query;
	}
}
