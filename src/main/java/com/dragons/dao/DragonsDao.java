package com.dragons.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.dragons.models.Dragon;

public class DragonsDao {
	private Connection connection;

	private void loadProperties() {
		try {
			InputStream inputStream = Dragon.class.getClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			properties.load(inputStream);

			String url = properties.getProperty("hibernate.connection.url");
			String user = properties.getProperty("hibernate.connection.username");
			String password = properties.getProperty("hibernate.connection.password");
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createTable() {
		String query = "CREATE TABLE IF NOT EXISTS dragon (id INT PRIMARY KEY, name VARCHAR(255), location VARCHAR(255))";
		try {
			loadProperties();
			Statement statement = connection.createStatement();
			statement.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(Dragon dragon) {
		String query = "INSERT INTO dragon (id, name, location) VALUES (?, ?, ?)";
		try {
			loadProperties();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, dragon.getId());
			statement.setString(2, dragon.getName());
			statement.setString(3, dragon.getLocation());
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLocation(int id, String location) {
		String query = "UPDATE dragon SET location=? WHERE id=?;";
		try {
			loadProperties();

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, location);
			statement.setInt(2, id);

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated == 0) {
				System.out.println("Dragon not found with id: " + id);
			} else {
				System.out.println("Dragon location updated successfully.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public Dragon findById(int id) {
		String query = "SELECT * FROM dragon WHERE id = ?";
		Dragon dragon = null;
		try {
			loadProperties();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				dragon = new Dragon(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getString("location"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dragon;
	}

	public List<Dragon> findAll() {
		String query = "SELECT * FROM dragon";
		List<Dragon> dragonsList = new ArrayList<>();
		try {
			loadProperties();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Dragon dragon = new Dragon(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getString("location"));
				dragonsList.add(dragon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dragonsList;
	}

	public void update(Dragon dragon) {
		String query = "UPDATE dragon SET name=?, location=? WHERE id=?;";
		try {
			loadProperties();

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, dragon.getName());
			statement.setString(2, dragon.getLocation());
			statement.setInt(3, dragon.getId());

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated == 0) {
				System.out.println("Dragon not found with id: " + dragon.getId());
			} else {
				System.out.println("Dragon updated successfully.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void delete(int id) {
		String query = "DELETE FROM dragon WHERE id=?;";

		try {
			loadProperties();

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted == 0) {
				System.out.println("Dragon not found with id: " + id);
			} else {
				System.out.println("Dragon deleted successfully.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
