package edu.TaylorThurlow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WindowMainController implements Initializable
{

	/**
	 * Buttons
	 */
	@FXML
	private Button buttonAddFolder;

	@FXML
	private Button buttonAddFile;

	@FXML
	private Button buttonPlayPause;

	@FXML
	private Button buttonStop;

	@FXML
	private Button buttonSavePlaylist;

	@FXML
	private Button buttonLoadPlaylist;

	/**
	 * Tables and lists
	 */
	private ObservableList<FileMP3> data;

	@FXML
	private ListView<Playlist> playlistList = new ListView<Playlist>();

	@FXML
	private TableView<FileMP3> mainTable = new TableView<FileMP3>();

	@FXML
	private TableColumn<FileMP3, String> artistCol;

	@FXML
	private TableColumn<FileMP3, String> albumCol;

	@FXML
	private TableColumn<FileMP3, String> titleCol;

	@FXML
	private TableColumn<FileMP3, Integer> trackCol;

	@FXML
	private TableColumn<FileMP3, String> yearCol;

	@FXML
	private TableColumn<FileMP3, String> genreCol;

	@FXML
	private TableColumn<FileMP3, String> filePathCol;

	/**
	 * Handle button presses
	 */
	@FXML
	private void buttonAddFolder()
	{
		System.out.println("DEBUG: Add Folder button pressed.");
		musicPlayer.getInstance().loadFolderDialog();
	}

	@FXML
	private void buttonAddFile()
	{
		System.out.println("DEBUG: Add File button pressed.");

	}

	@FXML
	private void buttonPlayPause()
	{
		System.out.println("DEBUG: Play / Pause button pressed.");
		musicPlayer.getInstance().playPause();
	}

	@FXML
	private void buttonStop()
	{
		System.out.println("DEBUG: Stop button pressed.");
		musicPlayer.getInstance().stopFile();
	}

	@FXML
	private void buttonSavePlaylist()
	{
		System.out.println("DEBUG: Save playlist button pressed.");
		musicPlayer.getInstance().savePlaylist();
	}

	@FXML
	private void buttonLoadPlaylist()
	{
		System.out.println("DEBUG: Load playlist button pressed.");
	}

	/**
	 * Artwork View
	 */
	@FXML
	ImageView artwork = new ImageView();

	/**
	 * Tables handled here
	 */
	public void setMainTableData(ObservableList<FileMP3> list)
	{
		artistCol.setCellValueFactory(new PropertyValueFactory<FileMP3, String>("artist"));
		albumCol.setCellValueFactory(new PropertyValueFactory<FileMP3, String>("album"));
		titleCol.setCellValueFactory(new PropertyValueFactory<FileMP3, String>("title"));
		trackCol.setCellValueFactory(new PropertyValueFactory<FileMP3, Integer>("track"));
		yearCol.setCellValueFactory(new PropertyValueFactory<FileMP3, String>("year"));
		genreCol.setCellValueFactory(new PropertyValueFactory<FileMP3, String>("genre"));
		filePathCol.setCellValueFactory(new PropertyValueFactory<FileMP3, String>("path"));
		mainTable.setItems(list);
	}

	/**
	 * Lists handled here
	 */
	public void setPlaylistListData(ObservableList<Playlist> list)
	{
		System.out.println("DEBUG: setPlaylistListData(list) called. 'list' contains:");
		System.out.println("DEBUG: " + list.toString());
		playlistList.setItems(list);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		mainTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				System.out.println("DEBUG: Main list row selected.");
				FileMP3 selectedFile = (FileMP3) newValue;
				Image art = selectedFile.getArtwork();
				artwork.setImage(art);

				try
				{
					musicPlayer.getInstance().stopFile();
					musicPlayer.getInstance().playFile(selectedFile);
				} catch (Exception ex)
				{
					ex.printStackTrace();
					musicPlayer.getInstance().showErrorDialog(ex.getMessage());
				}
			}
		});

		playlistList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
		{
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue)
			{
				System.out.println("DEBUG: Playlist list row selected.");
				Playlist selectedPlaylist = (Playlist) newValue;
				musicPlayer.getInstance().displayPlaylist(selectedPlaylist);
			}
		});
	}

}
