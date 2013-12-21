package edu.TaylorThurlow;

import java.util.ArrayList;

public class Playlist
{

	private ArrayList<String> paths = new ArrayList<String>();
	private int totalSongs;
	private String name;
	private int id;

	public Playlist()
	{
		this.totalSongs = 0;
	}

	public void addSong(String path)
	{
		paths.add(path);
		totalSongs = paths.size();
	}

	public void removeSong(String path)
	{
		for (String entry : paths)
		{
			if (entry.equals(path))
			{
				paths.remove(entry);
				totalSongs = paths.size();
			}
		}
	}

	public void removeAllSongs()
	{
		paths.removeAll(paths);
		totalSongs = paths.size();
	}

	public ArrayList<String> getPaths()
	{
		return paths;
	}

	public String getPathsSerialized()
	{
		String serialized = "";
		for (String entry : paths)
		{
			serialized += entry;
			serialized += "|";
		}
		return serialized;
	}

	public void setPaths(ArrayList<String> paths)
	{
		this.paths = paths;
	}

	public void setPaths(String serialized)
	{
		String[] split = serialized.split("\\|");
		System.out.println("DEBUG: split serialized array: " + split.toString());
		this.removeAllSongs();
		for (String entry : split)
		{
			this.addSong(entry);
		}
	}

	public String[] getArray()
	{
		String[] array = (String[]) paths.toArray();
		return array;
	}

	public int getSize()
	{
		return totalSongs;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
}
