package edu.TaylorThurlow;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PlaylistDBHelper
{

	private String dbName;
	private Connection dbConn = null;

	public PlaylistDBHelper(String dbName)
	{
		this.dbName = dbName;
	}

	public void connect() throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			Class.forName("org.sqlite.JDBC");
			dbConn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		}
	}

	public boolean dbExists()
	{
		File f = new File(dbName);
		return f.exists();
	}

	public void createDatabase() throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			System.out.println("DEBUG: dbConn == null, connecting");
			connect();
		}

		Statement statement = dbConn.createStatement();
		statement.setQueryTimeout(30);
		String sql = "CREATE TABLE \"playlists\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE , \"name\" VARCHAR NOT NULL DEFAULT newplaylist, \"paths\" VARCHAR)";

		statement.executeUpdate(sql);
	}

	public void create(Playlist playlist) throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			connect();
		}

		Statement statement = dbConn.createStatement();
		statement.setQueryTimeout(30);

		String sql = "INSERT INTO playlists (paths) VALUES (";
		String[] array = playlist.getArray();
		String storedPath = "";

		for (String entry : array)
		{
			storedPath += entry;
			storedPath += "|";
		}

		sql += "\"" + storedPath + "\")";

		statement.executeUpdate(sql);

		ResultSet rs = statement.getGeneratedKeys();

		long id = -1;
		
		if (rs.next())
		{
			id = rs.getLong(1);
			System.out.println("DEBUG: Setting id to: " + id);
		}

		playlist.setId(id);
		musicPlayer.getInstance().populatePlaylistsList();

	}

	public ArrayList<Playlist> read() throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			connect();
		}

		Statement statement = dbConn.createStatement();
		statement.setQueryTimeout(30);

		String sql = "SELECT * FROM playlists";

		ResultSet rs = statement.executeQuery(sql);

		ArrayList<Playlist> list = new ArrayList<>();

		while (rs.next())
		{
			Playlist p = new Playlist();
			p.setName(rs.getString("name"));
			p.setPaths(rs.getString("paths"));
			p.setId(rs.getInt("id"));
			list.add(p);
		}

		return list;
	}

	public void update(Playlist p) throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			connect();
		}

		Statement statement = dbConn.createStatement();
		statement.setQueryTimeout(30);

		String sql = "UPDATE playlists SET name=\"" + p.getName() + "\"," + "paths=\""
				+ p.getPathsSerialized() + "\"" + " WHERE id = " + p.getId();
		statement.executeUpdate(sql);
	}

	public void delete(Playlist p) throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			connect();
		}

		Statement statement = dbConn.createStatement();
		statement.setQueryTimeout(30);

		String sql = "DELETE FROM playlists WHERE id = " + p.getId();
		statement.executeUpdate(sql);
	}
}
