package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;

public class SettingsController implements Initializable {
	
	@FXML
	ImageView iGrille;
	
	@FXML
	ImageView imgsettings;

	public void initialize(URL arg0, ResourceBundle arg1) {
		Image imageSettings = new Image(getClass().getResourceAsStream("/resources/images/settings.png"));
		imgsettings.setImage(imageSettings);
		
		//Image iconeVolume = new Image(getClass().getResourceAsStream("/resources/images/sound.png"));
		//iVolume.setImage(iconeVolume);
		
		Image iconeGrille = new Image(getClass().getResourceAsStream("/resources/images/grille.png"));
		iGrille.setImage(iconeGrille);
		
		
	}
}
