package application;

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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import quiestce.games.GameSolo;
import javafx.fxml.Initializable;

public class SampleController implements Initializable{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	RadioButton radioButton1 = new RadioButton("1 personnage cachÃ©");
	@FXML
	RadioButton radioButton2 = new RadioButton("2 personnages cachÃ©s");
	@FXML
	ToggleGroup sousmodes1 = new ToggleGroup();
	@FXML
	RadioButton modeSolo = new RadioButton();
	@FXML
	RadioButton mode1v1 = new RadioButton();
	@FXML
	RadioButton modeReseau = new RadioButton();
	@FXML
	ToggleGroup mode = new ToggleGroup();
	@FXML
	Label description = new Label();
	@FXML
	ImageView imgsolo;
	@FXML
	ImageView img1v1;
	@FXML
	ImageView imgreseau;
	@FXML
	ImageView imgretour;
	@FXML
	Label status;
	//GameSolo game;
	public static final int TAILLEIMAGE = 100;
	
	public void displayDescription(ActionEvent event) {
		if(modeSolo.isSelected()) {
			//modeSolo.setStyle("-fx-background-color: transparent, yellow");
			modeSolo.setStyle("-fx-background-insets: 120, 12");
			//modeSolo.setStyle("-fx-background-color: yellow");
			mode1v1.setStyle("-fx-background-color:none");
			modeReseau.setStyle("-fx-background-color:none");
			description.setText("Jouer en solo");
		}else if(mode1v1.isSelected()) {
			description.setText("Jouer contre votre ami (1v1)");
			mode1v1.setStyle("-fx-background-insets: 120, 12");
			modeSolo.setStyle("-fx-background-color:none");
			modeReseau.setStyle("-fx-background-color:none");
		}else if(modeReseau.isSelected()) {
			description.setText("Jouer en ligne (Reseau)");
			modeReseau.setStyle("-fx-background-insets: 120, 12");
			modeSolo.setStyle("-fx-background-color:none");
			mode1v1.setStyle("-fx-background-color:none");
		}
	}
	
	public static String obtenirGrille() {
		JSONParser parser = new JSONParser();
		JSONObject grilleDePerso = new JSONObject();
		try {
			grilleDePerso = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException e) {
			grilleDePerso = null;
			System.out.println("Parametres non trouvÃ© !");
		}
		return (String)grilleDePerso.get("grille");
	}
	
	@SuppressWarnings("unchecked")
	public void validerSauvegarde() {
		JSONParser parser = new JSONParser();
		JSONObject settings = new JSONObject();
		try {
			settings = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException er) {
			settings = null;
			System.out.println("Parametres non trouvÃ© !");
		}
		settings.replace("partie", null);
		try {
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Parametres/parametres.json");
			fileWriter.write(settings.toJSONString());
			fileWriter.close();
		}catch(IOException er) {
			
		}
	}
	
	public void creationPartie(ActionEvent event) throws IOException {
		validerSauvegarde();
		//radioButton1.setSelected(true);
		//radioButton1.isSelected();
		//MODE SOLO
		
		if(modeSolo.isSelected() && radioButton1.isSelected()) {
			//game = new GameSolo(true,this.obtenirGrille());
			JSONParser parser = new JSONParser();
			JSONObject grilleDePerso = new JSONObject();
			try {
				grilleDePerso = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
			} catch (IOException | ParseException er) {
				grilleDePerso = null;
				System.out.println("Grille non trouvee !");
			}
			if((String)grilleDePerso.get("grille")!=null) {
				Parent root = FXMLLoader.load(getClass().getResource("SampleInGame.fxml"));
				stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			
				scene= new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Partie solo");
				stage.setX(50.0);
				stage.setY(50.0);
				stage.setMinWidth(1200.0);
				stage.setMinHeight(700.0);
				stage.show();
				status.setText("");
			}else {
				status.setText("Veuillez selectionner une grille avant !");
			}
		}
		
		if(modeSolo.isSelected() && radioButton2.isSelected()) {
			
			status.setText("");
			Parent root = FXMLLoader.load(getClass().getResource("SampleInGame2.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Partie solo 2");
			stage.setX(50.0);
			stage.setY(50.0);
			stage.setMinWidth(1200.0);
			stage.setMinHeight(700.0);
			stage.show();
		}
		//MODE 1V1
		if(mode1v1.isSelected() && radioButton1.isSelected()) {
			
			Parent root = FXMLLoader.load(getClass().getResource("Partie.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Partie locale");
			stage.show();
			status.setText("");
		}
		
		if(mode1v1.isSelected() && radioButton2.isSelected()) {
			
			Parent root = FXMLLoader.load(getClass().getResource("Partie.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Partie locale");
			stage.show();
			status.setText("");
		}
		///MODE RESEAU
		if(modeReseau.isSelected() && radioButton1.isSelected()) {
			
			Parent root = FXMLLoader.load(getClass().getResource("Partie.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Partie en ligne");
			stage.show();
			status.setText("");
		}
		
		if(modeReseau.isSelected() && radioButton1.isSelected()) {
			
			Parent root = FXMLLoader.load(getClass().getResource("Partie.fxml"));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene= new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Partie en ligne");
			stage.show();
			status.setText("");
		}
		if((modeSolo.isSelected()||mode1v1.isSelected()||modeReseau.isSelected())&&sousmodes1.getSelectedToggle()==null){
			status.setText("Veuillez selectionner un parametre de jeu !");
		}
		if((radioButton1.isSelected()||radioButton2.isSelected())&&mode.getSelectedToggle()==null) {
			status.setText("Veuillez selectionner un mode de jeu !");
		}
	}
	
	public void switchToMenu(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene= new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu");
		stage.show();
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		status.setText("");
		Image imageSolo = new Image(getClass().getResourceAsStream("/resources/images/solo.png"));
		ImageView view1 = new ImageView();
		view1.setFitHeight(TAILLEIMAGE*0.9);
		view1.setFitWidth(TAILLEIMAGE*0.9);
		view1.setImage(imageSolo);
		modeSolo.getStyleClass().remove("radio-button");
		modeSolo.setStyle("-fx-background-color:none");
		modeSolo.getStyleClass().add("toggle-button");
		modeSolo.setGraphic(view1);
		
		Image image1v1 = new Image(getClass().getResourceAsStream("/resources/images/1v1.png"));
		ImageView view2 = new ImageView();
		view2.setFitHeight(TAILLEIMAGE*0.9);
		view2.setFitWidth(TAILLEIMAGE*0.9);
		view2.setImage(image1v1);
		mode1v1.getStyleClass().remove("radio-button");
		mode1v1.setStyle("-fx-background-color:none");
		mode1v1.getStyleClass().add("toggle-button");
		mode1v1.setGraphic(view2);
		
		Image imagereseau = new Image(getClass().getResourceAsStream("/resources/images/reseau.png"));
		ImageView view3 = new ImageView();
		view3.setFitHeight(TAILLEIMAGE*0.9);
		view3.setFitWidth(TAILLEIMAGE*0.9);
		view3.setImage(imagereseau);
		modeReseau.getStyleClass().remove("radio-button");
		modeReseau.setStyle("-fx-background-color:none");
		modeReseau.getStyleClass().add("toggle-button");
		modeReseau.setGraphic(view3);
		
		Image imageretour = new Image(getClass().getResourceAsStream("/resources/images/retour.png"));
		imgretour.setImage(imageretour);
		
		
	}
}
