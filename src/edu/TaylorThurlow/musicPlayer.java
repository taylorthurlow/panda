package edu.TaylorThurlow;

import java.io.File;
import java.net.URL;
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
	}

	public static void main(String[] args)
	{
		launch(args);
	}

	public void updateList()
	{
		System.out.println("updateList called, here's the list:");
		System.out.println(list);
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
			System.out.println("Loading folder: " + path);
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
		System.out.println("added to list: " + toAdd.toString());
	}

	public void playFile(FileMP3 file) throws Exception
	{
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

		if (mainPlayer.getStatus() == MediaPlayer.Status.PAUSED)
		{
			mainPlayer.play();
		} else if (mainPlayer.getStatus() == MediaPlayer.Status.PLAYING)
		{
			mainPlayer.pause();
		}

	}

	public void stopFile()
	{
		try
		{
			mainPlayer.stop();
		} catch (Exception e)
		{
			showErrorDialog(e.getMessage());
		}

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
}
