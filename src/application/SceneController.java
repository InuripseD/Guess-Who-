package application;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
//L
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;

public class SceneController implements Initializable {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	//private Event iView;
	
	@FXML 
	Image iView;
	@FXML
	ImageView imgsolo;
	@FXML
	ImageView imgsettings;
	@FXML
	ImageView imgGenerateur;
	@FXML
	Pane settingsPane;
	@FXML
	ImageView iVolume;
	@FXML
	ImageView iGrille;
	@FXML
	ImageView exitGrillePannel;
	@FXML
	ImageView exitGenerateurPannel;
	@FXML
	AnchorPane Configuration;
	@FXML
	AnchorPane ConfigGenerateur;
	@FXML
	ChoiceBox<String> ChoiceBoxGrille;
	@FXML
	ChoiceBox<String> ChoiceBoxGenerateur;
	@FXML
	Label status;
	
	public void openSettings(ActionEvent e) {
		Configuration.setStyle("-fx-background-color:d0b715");
		if(Configuration.isVisible()) {
			ChoiceBoxGrille.getItems().clear();
			Configuration.setVisible(false);
		}else {
			Configuration.setVisible(true);
			ChoiceBoxGrille.getItems().addAll(getGrilleOPFromFolder());
		}
		//ChoiceBoxGrille.getItems().addAll(getGrilleFromFolder());
	}
	
	public void closeSettings(ActionEvent e) {
		ChoiceBoxGrille.getItems().clear();
		status.setText("");
		Configuration.setVisible(false);
	}
	
	public void openSettingsGenerateur(ActionEvent e) {
		ConfigGenerateur.setStyle("-fx-background-color: #324B5B");
		if(ConfigGenerateur.isVisible()) {
			ChoiceBoxGenerateur.getItems().clear();
			status.setText("");
			ConfigGenerateur.setVisible(false);
		}else {
			ConfigGenerateur.setVisible(true);
			ChoiceBoxGenerateur.getItems().addAll(getGrilleFromFolder());
		}
	}
	
	public void closeSettingsGenerateur(ActionEvent e) {
		ChoiceBoxGenerateur.getItems().clear();
		status.setText("");
		ConfigGenerateur.setVisible(false);
	}
	
	
	@SuppressWarnings("unchecked")
	public void validerGrille(ActionEvent e) {
		JSONParser parser = new JSONParser();
		JSONObject grilleDePerso = new JSONObject();
		try {
			grilleDePerso = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException er) {
			grilleDePerso = null;
			System.out.println("Parametres non trouvÃƒÂ© !");
		}
		grilleDePerso.replace("grille", (String)ChoiceBoxGrille.getValue());
		try {
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Parametres/parametres.json");
			fileWriter.write(grilleDePerso.toJSONString());
			fileWriter.close();
			closeSettings(e);
		}catch(IOException er) {
			
		}
	}

	public ArrayList<String> getGrilleOPFromFolder(){
		ArrayList<String> bonnesGrilles = new ArrayList<String>();
		for(String grille : getGrilleFromFolder()){
			if(!grille.contains("SAVE")){
				bonnesGrilles.add(grille);
			}
		}
		return bonnesGrilles;
	}
	
	public String[] getGrilleFromFolder(){
		String[] nomGrilles = new String[50];
		File directoryPath = new File("../qui-est-ce/Grilles/");
		nomGrilles = directoryPath.list();
		return nomGrilles;
	}
	
	public void switchToChoixMode(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ChoixMode.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Choix de la partie");
		stage.show();
	}
	
	public void switchToMenu(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu");
		stage.show();
	}
	
	@FXML
	public void closeWindow(ActionEvent event) throws IOException{
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}	

	public void afficheSettings() {
		BackgroundFill backgroundFill =
	        new BackgroundFill(
	                Color.valueOf("#000000"),
	                new CornerRadii(10),
	                new Insets(10)
	                );
		Background background =
	        new Background(backgroundFill);
		settingsPane.setBackground(background);
	}

	public void switchToSettings(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Parametres");
		stage.show();
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		Image imageSettings = new Image(getClass().getResourceAsStream("/resources/images/settings2.png"));
		imgsettings.setImage(imageSettings);
		
		Image imageGenerateur = new Image(getClass().getResourceAsStream("/resources/images/grille3.png"));
		imgGenerateur.setImage(imageGenerateur);
		
		Image imageExitGenerateurPannel = new Image(getClass().getResourceAsStream("/resources/images/croix2.png"));
		exitGrillePannel.setImage(imageExitGenerateurPannel);

		Image imageExitGrillePannel = new Image(getClass().getResourceAsStream("/resources/images/croix.png"));
		exitGenerateurPannel.setImage(imageExitGrillePannel);
	}

	
	public void switchToSauvegardes(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Saves.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Sauvegardes");
		stage.show();
		//ListViewSauvegardes.getItems().addAll(getSauvegardesFromFolder());
	}
	
	@SuppressWarnings("unchecked")
	public void switchToGenerateur(ActionEvent event) throws IOException {
		if(ChoiceBoxGenerateur.getValue()!=null) {
			//closeSettingsGenerateur(event);
			JSONParser parser = new JSONParser();
			JSONObject grilleGenerateur = new JSONObject();
			try {
				grilleGenerateur = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
			} catch (IOException | ParseException er) {
				grilleGenerateur = null;
				System.out.println("Grille non trouvÃƒÂ©e !");
			}
			grilleGenerateur.replace("grilleGenerateur", (String)ChoiceBoxGenerateur.getValue());
			try {
				FileWriter fileWriter = new FileWriter("../qui-est-ce/Parametres/parametres.json");
				fileWriter.write(grilleGenerateur.toJSONString());
				fileWriter.close();
			}catch(IOException er) {
			
			}
			Stage stage= new Stage();
			FXMLLoader fxmlLoader= new FXMLLoader();
			Pane root= fxmlLoader.load(getClass().getResource("Generateur.fxml").openStream());
			stage.setScene(new Scene(root));
			Image icone = new Image("/resources/images/icone.png");
			stage.getIcons().add(icone);
			stage.setTitle("Generateur de Grille");
			stage.showAndWait();
			closeSettingsGenerateur(event);
		}else {
			status.setText("Aucune grille a modifier n'est selectionnee !");
		}
	}
	
	public void switchToNouveauGenerateur(ActionEvent event) throws IOException {
		closeSettingsGenerateur(event);
		JSONParser parser = new JSONParser();
		JSONObject grilleGenerateur = new JSONObject();
		try {
			grilleGenerateur = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException er) {
			grilleGenerateur = null;
			System.out.println("Grille non trouvÃƒÂ©e !");
		}
		grilleGenerateur.replace("grilleGenerateur", null);
		try {
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Parametres/parametres.json");
			fileWriter.write(grilleGenerateur.toJSONString());
			fileWriter.close();
		}catch(IOException er) {
			
		}
		Stage stage= new Stage();
		FXMLLoader fxmlLoader= new FXMLLoader();
		Pane root= fxmlLoader.load(getClass().getResource("Generateur.fxml").openStream());
		stage.setScene(new Scene(root));
		Image icone = new Image("/resources/images/icone.png");
		stage.getIcons().add(icone);
		stage.setTitle("Generateur de Grille");
		stage.setResizable(false);
		stage.showAndWait();
	}
	
}
