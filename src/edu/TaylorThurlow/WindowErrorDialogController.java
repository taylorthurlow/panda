package edu.TaylorThurlow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WindowErrorDialogController implements Initializable
{

	private Stage dialogStage;

	@FXML
	private Button okButton;

	@FXML
	private Label errorLabel;

	@FXML
	private void okButton()
	{
		System.out.println("DEBUG: Error dialog triggered.");
		dialogStage.close();
	}

	public void setDialogStage(Stage dialogStage)
	{
		this.dialogStage = dialogStage;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		
	}
	
	public void setMessage(String message) {
		errorLabel.setText(message);
	}

}
