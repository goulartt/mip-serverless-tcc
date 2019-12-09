package com.amazonaws.lambda.field.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;

public class FieldRepository {

	public String getCurrentTime(Context context) {

		context.getLogger().log("Invoked JDBCSample.getCurrentTime");

		String currentTime = "unavailable";

		// Get time from DB server
		try {
			String url = "jdbc:mysql://mip.cnwecoykhrj7.us-east-1.rds.amazonaws.com:3306";
			String username = "admin";
			String password = "joaogoulart666";

			Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT NOW()");

			if (resultSet.next()) {
				currentTime = resultSet.getObject(1).toString();
			}

			context.getLogger().log("Successfully executed query.  Result: " + currentTime);

		} catch (Exception e) {
			e.printStackTrace();
			context.getLogger().log("Caught exception: " + e.getMessage());
		}

		return currentTime;
	}
}
