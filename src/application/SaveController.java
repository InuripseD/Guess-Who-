package application;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SaveController implements Initializable {
	
	@FXML
	ListView<String> ListViewSauvegardes;
	@FXML
	ImageView retour;
	private Parent root;
	private Scene scene;
	private Stage stage;
	
	
	public String[] getSauvegardesFromFolder() {
		String[] nomSauvegarde = new String[50];
		File directoryPath = new File("../qui-est-ce/Sauvegardes/");
		nomSauvegarde = directoryPath.list();
		return nomSauvegarde;
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		ListViewSauvegardes.getItems().addAll(getSauvegardesFromFolder());

		Image imgretour = new Image(getClass().getResourceAsStream("/resources/images/retour.png"));
		retour.setImage(imgretour);
	}
	
	public void lancerSauvegarde(ActionEvent event) {
		try {
			System.out.println("1");
			validerSauvegarde();
			System.out.println("2");
			String nomSave = this.ListViewSauvegardes.getSelectionModel().getSelectedItem();
			System.out.println(nomSave);
			if(nomSave.contains("2c")) {
				Parent root = FXMLLoader.load(getClass().getResource("SampleInGame2.fxml"));
				stage=(Stage)((Node)event.getSource()).getScene().getWindow();
				scene= new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Partie solo 2");
				stage.show();
			}else {
				Parent root = FXMLLoader.load(getClass().getResource("SampleInGame.fxml"));
				stage=(Stage)((Node)event.getSource()).getScene().getWindow();
				scene= new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Partie solo");
				stage.show();
			}
		}catch(Exception ex) {
			System.out.println("error");
		}

	}
	
	@SuppressWarnings("unchecked")
	public void validerSauvegarde() {
		JSONParser parser = new JSONParser();
		JSONObject settings = new JSONObject();
		try {
			settings = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException er) {
			settings = null;
			System.out.println("Parametres non trouv !");
		}
		settings.replace("partie", ListViewSauvegardes.getSelectionModel().getSelectedItem());
		try {
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Parametres/parametres.json");
			fileWriter.write(settings.toJSONString());
			fileWriter.close();
		}catch(IOException er) {
			
		}
	}

	public void retourToMenu(ActionEvent event) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu");
		stage.show();
	}
	
}
