package edu.TaylorThurlow;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class musicPlayer extends Application
{

	private static musicPlayer instance;
	private Stage mainStage;
	private WindowMainController mainController;
	private MediaPlayer mainPlayer;
	private ObservableList<FileMP3> list = FXCollections.observableArrayList();
	private ObservableList<Playlist> playlists = FXCollections.observableArrayList();
	private Boolean firstPlay = true;

	@Override
	public void start(Stage stage) throws Exception
	{
		URL loc = getClass().getResource("WindowMain.fxml");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loc);
		Parent root = (Parent) loader.load(loc.openStream());
		Scene scene = new Scene(root);
		mainStage = stage;
		mainController = loader.getController();
		stage.setScene(scene);
		stage.show();
		populatePlaylistsList();
	}

	public static void main(String[] args)
	{
		launch(args);
	}

	public void updateList()
	{
		System.out.println("DEBUG: updateList called.");
		mainController.setMainTableData(list);
	}

	public void loadFolderDialog()
	{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Pick a folder to import");
		File inFile = chooser.showDialog(mainStage);
		if (inFile != null)
		{
			String path = inFile.getAbsolutePath();
			System.out.println("DEBUG: Loading folder: " + path);
			list.removeAll(list);
			listFilesInFolder(inFile);
		}
		updateList();
	}

	public void listFilesInFolder(File inFile)
	{
		for (File entry : inFile.listFiles())
		{
			String entryExt = "";
			String entryPath = entry.getAbsolutePath();
			int i = entryPath.lastIndexOf('.');
			int j = Math.max(entryPath.lastIndexOf('/'), entryPath.lastIndexOf('\\'));
			if (i > j)
			{
				entryExt = entryPath.substring(i + 1);
			}
			if (entry.isDirectory())
			{
				listFilesInFolder(entry);
			} else if (entryExt.equals("mp3"))
			{
				try
				{
					FileMP3 toAdd = new FileMP3(entry.getAbsolutePath());
				} catch (Exception e)
				{
					showErrorDialog(e.getMessage());
				}
			}
		}
	}

	public void addMp3ToList(FileMP3 toAdd)
	{
		list.add(toAdd);
		System.out.println("DEBUG: added to list: " + toAdd.toString());
	}

	public void playFile(FileMP3 file) throws Exception
	{
		firstPlay = false;
		File audioFile = new File(file.getPath());
		Media media = new Media(audioFile.toURI().toURL().toString());
		mainPlayer = new MediaPlayer(media);
		mainPlayer.play();
	}

	public void playFileAtTime(FileMP3 file, Duration time) throws Exception
	{
		File audioFile = new File(file.getPath());
		Media media = new Media(audioFile.toURI().toURL().toString());
		mainPlayer = new MediaPlayer(media);
		mainPlayer.setStartTime(time);
	}

	public void playPause()
	{
		if (!firstPlay)
		{
			if (mainPlayer.getStatus() == MediaPlayer.Status.PAUSED)
			{
				mainPlayer.play();
			} else if (mainPlayer.getStatus() == MediaPlayer.Status.PLAYING)
			{
				mainPlayer.pause();
			}
		}
	}

	public void savePlaylist()
	{
		System.out.println("DEBUG: Save playlist started.");
		ArrayList<String> paths = new ArrayList<String>();
		Playlist newPlaylist = new Playlist();

		for (FileMP3 entry : list)
		{
			System.out.println("DEBUG: Adding to paths arraylist: " + entry.getPath());
			paths.add(entry.getPath());
		}

		for (String entry : paths)
		{
			newPlaylist.addSong(entry);
		}

		PlaylistDBHelper helper = new PlaylistDBHelper("playlists");
		if (!helper.dbExists())
		{
			System.out.print("DEBUG: DB doesn't exist, creating.. ");
			try
			{
				helper.createDatabase();
				System.out.println("Created.");
			} catch (Exception e)
			{
				e.printStackTrace();
				showErrorDialog(e.getMessage());
			}
		}

		try
		{
			System.out.print("DEBUG: Connecting to helper.. ");
			helper.connect();
			System.out.println("Connected.");
			helper.create(newPlaylist);
		} catch (Exception e)
		{
			e.printStackTrace();
			showErrorDialog(e.getMessage());
		}
	}

	public void stopFile() throws Exception
	{
		if (!firstPlay)
		{
			mainPlayer.stop();
		}
	}

	public void populatePlaylistsList()
	{
		PlaylistDBHelper helper = new PlaylistDBHelper("playlists");
		if (!helper.dbExists())
		{
			System.out.print("DEBUG: DB doesn't exist, creating.. ");
			try
			{
				helper.createDatabase();
				System.out.println("Created.");
			} catch (Exception e)
			{
				e.printStackTrace();
				showErrorDialog(e.getMessage());
			}
		}

		try
		{
			System.out.print("DEBUG: Connecting to helper.. ");
			helper.connect();
			System.out.println("Connected.");

			playlists.removeAll(playlists);
			ArrayList<Playlist> tempList = helper.read();

			for (Playlist entry : tempList)
			{
				System.out.println("DEBUG: Adding to tempList: " + entry.getName() + " - "
						+ entry.getPathsSerialized());
				playlists.add(entry);
			}

			mainController.setPlaylistListData(playlists);

		} catch (Exception e)
		{
			e.printStackTrace();
			showErrorDialog(e.getMessage());
		}
	}

	public void displayPlaylist(Playlist toDisplay)
	{
		list.removeAll(list);
		String[] paths = toDisplay.getArray();
		for (String entry : paths)
		{
			try
			{
				File toTest = new File(entry);
				if (toTest.exists())
				{
					FileMP3 newFile = new FileMP3(entry);
				} else
				{
					FileMP3 newFile = new FileMP3("NOTFOUNDERROR");
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				showErrorDialog(e.getMessage());
			}
		}
		updateList();
	}

	public void showErrorDialog(String message)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(musicPlayer.class.getResource("WindowErrorDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Error");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			WindowErrorDialogController controller = loader.getController();
			controller.setMessage(message);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (Exception e)
		{
			// Cant show error dialog when theres an error showing the dialog. Heh.
			e.printStackTrace();
		}
	}

	public musicPlayer()
	{
		instance = this;
	}

	public static musicPlayer getInstance()
	{
		return instance;
	}

	public ObservableList<FileMP3> getList()
	{
		return list;
	}

	public Stage getMainStage()
	{
		return mainStage;
	}

	public WindowMainController getMainController()
	{
		return mainController;
	}

	public MediaPlayer getMainPlayer()
	{
		return mainPlayer;
	}
}
