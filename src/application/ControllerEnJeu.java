package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import quiestce.associations.Personnage;
import quiestce.games.GameSolo;

public class ControllerEnJeu implements Initializable{
	
	/*Un anchorpane qui accueil un #GridePanePersonnage de photos et qui est dans un scrollpane*/
	@FXML
	AnchorPane AnchorPhotoPersonnage;
	/*Pour le moment ce label existe pour retourner la reponse a la question car pas encore de chat.*/
	@FXML
	Label LabelReponse;
	@FXML
	ListView<String> ListViewAttribut;
	@FXML
	ListView<String> ListViewArgument;
	/*La gridpane accueillant les boutons de photos des personnages.*/
	@FXML
	GridPane GridePanePersonnage;
	@FXML
	Button ButtonVerification;
	@FXML
	Button ButtonBan;
	/*Activer ou desactiver le mode cheat via une checkBox*/
	@FXML
	CheckBox CheckBoxTriche;
	@FXML
	Button ButtonAnalyse;
	@FXML
	ChoiceBox<String> ChoiceBoxConstructeur;
	@FXML
	CheckBox CheckBoxFacile;
	
	/*Les elements necessaire au chat !*/
	@FXML
	AnchorPane ChatBox;
	@FXML
	Label LabelChat;
	@FXML
	ScrollPane TexteScroll;
	
	GameSolo game;
	ArrayList<String> preBannis = new ArrayList<String>();
	ArrayList<Button> preBannisButton = new ArrayList<Button>();
	ArrayList<Button> Buttons = new ArrayList<Button>();
	
	HashMap<String,ArrayList<String>> questionEnCours = new LinkedHashMap<String,ArrayList<String>>();
	
	private static int chatline = 0;
	protected Stage stage;
	protected Scene scene;
	
	public static final int NBCOLONNES = 6;
	public static final int TAILLEIMAGE = 100;
	public static final int ECARTIMAGE = 25;
	public static final String[] CONSTRUCTEURLOGIQUES = {"ET","OU"};
	
	public void initGame(String sauvegardeOUpas) {
		if(sauvegardeOUpas==null || sauvegardeOUpas=="") {
			this.game = new GameSolo(true,obtenirGrille());
		}else {
			this.game = new GameSolo(sauvegardeOUpas);
			preBannis.addAll(this.game.getSauvegarde().getBannis());
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.initGame(obtenirPartie());
		System.out.println("Game lance !");
		afficherButtons();
		afficherPersonnages();
		afficherEtGererAttributsArgs();
		for(Button preban : Buttons) {
			if(preBannis.contains(preban.getId())) {
				preBannisButton.add(preban);
			}
		}
		if(!preBannisButton.isEmpty()) {
			for(Button b : preBannisButton) {
				b.setStyle("-fx-background-color:red");
				b.setDisable(true);
			}
			preBannisButton.clear();
			preBannis.clear();
		}
		//this.ban(null);
		this.ChatBox.setStyle("-fx-background-color: #324B5B");
	}
	
	public static String obtenirPartie() {
		JSONParser parser = new JSONParser();
		JSONObject partie = new JSONObject();
		try {
			partie = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException e) {
			partie = null;
			System.out.println("Partie non trouve !");
		}
		return (String)partie.get("partie");
	}
	
	public static String obtenirGrille() {
		JSONParser parser = new JSONParser();
		JSONObject grilleDePerso = new JSONObject();
		try {
			grilleDePerso = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));
		} catch (IOException | ParseException e) {
			grilleDePerso = null;
			System.out.println("Parametres non trouv !");
		}
		return (String)grilleDePerso.get("grille");
	}
	
	/**
	 * Methode qui va afficher les Noms des attributs et des arguments
	 * dans les ListViews destine a ca.
	 * Et gere la changement d'argumetns dans la listview argumetns lorsque 
	 * un attribut est clique dans la listview attrbiut.
	 */
	private void afficherEtGererAttributsArgs() {
		ListViewAttribut.getItems().addAll(game.getGrille().getAttsEtArgs().keySet());
		ListViewAttribut.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				ListViewArgument.getItems().clear();
				ListViewArgument.getItems().addAll(game.getGrille().getAttsEtArgs().get((String)ListViewAttribut.getSelectionModel().getSelectedItem()));
			}	
		});	
	}

	/**
	 * Cette methode est utilise lors de l'initialisation, c'est a dire lors ce que l'on pass sur le fenetre 
	 *  "en jeu", elle va aller mettre toutes les photos des personnages dans des buttons,
	 *  et mettre ces boutons dans la gridpane pour un affichage simple.
	 */
	private void afficherPersonnages() {
		int nbLigne = (int)Math.ceil(game.getGrille().getTaille()/NBCOLONNES);//On demande le nombre de perso puis on divise 6 cela sera le nombre de ligne.
		AnchorPhotoPersonnage.setPrefHeight(nbLigne*(TAILLEIMAGE+ECARTIMAGE)+ECARTIMAGE);//La taille du scrolleur est defini en fonction du nombre de personnages.
		HashMap<String,String> idEtLink = this.game.getGrille().getImage();//On va chercher l'hashmap AVec id et liens des photos.
		int i = 0, j = 0;//Variable necesssaire au bon placement des elements dans la gridpane.
		for(String imageID : idEtLink.keySet()) {//Pour chaque Id / image	
			System.out.println(idEtLink.get(imageID).toString());
			File file = new File("../qui-est-ce/Grilles/"+game.getGrille().getNom()+"/"+idEtLink.get(imageID));
			Image image = new Image(file.toURI().toString());
			System.out.println(file.exists());
			ImageView view = new ImageView();
			view.setFitHeight(TAILLEIMAGE*0.9);
			view.setFitWidth(TAILLEIMAGE*0.9);
			view.setImage(image);
			Button button = new Button();
			button.setId(imageID);//On fabrique image plus imageView et on met dans Button.
			button.setOnAction(event -> checkId((Button) event.getSource()));//Chaque Boutton va enclencher la methode checkID().
			button.setGraphic(view);
			button.setStyle("-fx-background-color:none");
			Buttons.add(button);//on stock tous les buttons dans une arraylist de buttons.
			GridePanePersonnage.add(button, j, i);//on ajoute le button a la gridpane
			j++;
			if(j>NBCOLONNES-1) {//on retour a la ligne quand on a mit 6 elements.
				j=0;
				i++;
			}
		}
	}

	/**
	 * Methode appelee lors du chargement de la partie pour l'affichage et le remplissage des boutons et choiceBox etx...
	 */
	private void afficherButtons() {
		/**Image verifButton = new Image("/resources/images/lockbutton.png");//On met le lien de l'image du Bouton dans l'image.
		ImageView verifButtonView = new ImageView();//On creer l'imageView qui contient l'image.
		verifButtonView.setFitHeight(40);//On definit les dimensions de l'image.
		verifButtonView.setFitWidth(150);
		verifButtonView.setImage(verifButton);//On met l'image dans l'imageView
		ButtonVerification.setGraphic(verifButtonView);//On met l'image view dans le Bouton.
		ButtonVerification.setStyle("-fx-background-color:none");//On rend le Bouton transparent pour ne voir que l'image.
		
		Image banButton = new Image("/resources/images/banbutton.png");
		ImageView banButtonView = new ImageView();
		banButtonView.setFitHeight(40);
		banButtonView.setFitWidth(150);
		banButtonView.setImage(banButton);
		ButtonBan.setGraphic(banButtonView);
		ButtonBan.setStyle("-fx-background-color:none");**/
		
		if(this.game.getDroitCheatMode()) {//Si on a autorise l'activation du cheat mode dans cette partie alors =>
			CheckBoxTriche.setVisible(true);//on met la checBox pour active la triche visible.
			ButtonAnalyse.setDisable(true);
		}else {//sinon =>
			CheckBoxTriche.setVisible(false);//On la met invisible/pas.
			ButtonAnalyse.setVisible(false);
		}
		
		ChoiceBoxConstructeur.getItems().addAll(CONSTRUCTEURLOGIQUES);//On met les constructeur "ET" et "OU" dans la choiceBox.
		ChoiceBoxConstructeur.setValue("OU");
		
		appendText(LabelChat, "C'est Partie!");
		appendText(LabelChat, "Trouvez le Personnage!");
		appendText(LabelChat, " ");
		appendText(LabelChat, " ");
		appendText(LabelChat, " ");
		appendText(LabelChat, " ");
		appendText(LabelChat, " ");
		appendText(LabelChat, " ");
		
	}
	
	//methode pour mettre du texte dans la chat box. A mettre en place
	public void appendText(Label lab,String newText) {
		lab.setMinHeight(lab.getMinHeight()+18.5);
        lab.setText(lab.getText() + "\n" +newText);
        TexteScroll.setVvalue(1);
    }

	/**
	 * @param e quand on clique sur la checkbox pour activer le mode cheat on lance cette fonction qui change le mode de jeu
	 * et ecrit si possible dans le chat (a mettre en place) que on est en mode cheat ou pas.
	 */
	public void activeTriche(ActionEvent e) {
		this.game.changerModeTriche();
		System.out.println(this.game.getCheatMode());
		ButtonAnalyse.setDisable(!this.game.getCheatMode());
		if(this.game.getCheatMode()) {
			appendText(LabelChat,"Cheat Mode actif !");
		}else {
			appendText(LabelChat,"Cheat Mode desactive !");
		}
	}
	
	public void donnerQuestion() {
		appendText(LabelChat,"Vous voulez savoir si le personnage ");
		appendText(LabelChat,"possede les caracteristiques suivantes :");
		for(String att : this.questionEnCours.keySet()) {
			//appendText(LabelChat,""+att+" : ");
			for(String arg : this.questionEnCours.get(att)) {
				appendText(LabelChat,"-"+arg);
			}
		}
		//appendText(LabelChat,"Avec le constructeur : "+ ChoiceBoxConstructeur.getValue());
	}
	
	public void analyser(ActionEvent e) {
		try {
			preBannis.clear();
			preBannisButton.clear();
			ajouterQuestion(e);
			int nbPerso = 0;
			boolean constructeur;
			if(ChoiceBoxConstructeur.getValue()=="OU") {
				constructeur = true;
			}else {
				constructeur = false;
			}
			System.out.println(questionEnCours.toString());
			for(Personnage perso : this.game.getListePerso()) {
				if(perso.getId()!=this.game.getPersonnage().getId()&&(!this.game.getSauvegarde().getBannis().contains(perso.getId()))) {
					if(this.game.verifArgument(questionEnCours,constructeur)&&perso.verifierArguments(questionEnCours, constructeur) || !this.game.verifArgument(questionEnCours,constructeur)&&!perso.verifierArguments(questionEnCours, constructeur)) {
					//rien car notre personnage possede/ne possede pas et l'autre perso possede/ne possede pas aussi donc on ompte pas.
					}else {
						nbPerso++;
						preBannisButton.add(Buttons.get(Integer.parseInt(perso.getId())));
						preBannis.add((String)perso.getId());
					}
				}
			}
			appendText(LabelChat,"Cette question elimine : "+nbPerso+" caracteres!");
			System.out.println(nbPerso);
			questionEnCours.clear();
		}catch(Exception ex){
			appendText(LabelChat,"Veuillez choisir :");
			appendText(LabelChat,"-Un attribut");
			appendText(LabelChat,"-Un argument");
		}
	}
	
	public boolean verifierQuestion() {
		boolean constructeur;
		if(ChoiceBoxConstructeur.getValue()=="OU") {
			constructeur = true;
		}else {
			constructeur = false;
		}
		return this.game.verifArgument(questionEnCours,ChoiceBoxConstructeur.getValue()=="OU");
	}
	
	public void verifierArgumentPersoCache(ActionEvent e) {
		preBannis.clear();
		preBannisButton.clear();
		ajouterQuestion(e);
		if(ListViewArgument.getSelectionModel().getSelectedItem()!=null) {
			if(verifierQuestion()) {
				appendText(LabelChat,"Le personnage repond a :");
				if(ChoiceBoxConstructeur.getValue()=="OU") {
					appendText(LabelChat," au moins un critere!");
				}else {
					appendText(LabelChat," tous les criteres!");
				}
				LabelReponse.setText("VRAI");
			}else {
				appendText(LabelChat,"Le personnage ne repond :");
				if(ChoiceBoxConstructeur.getValue()=="OU") {
					appendText(LabelChat," a aucun des criteres!");
				}else {
					appendText(LabelChat," pas a au moins un critere!");
				}
				LabelReponse.setText("FAUX");
			}
			
			boolean constructeur;
			if(ChoiceBoxConstructeur.getValue()=="OU") {
				constructeur = true;
			}else {
				constructeur = false;
			}
			
			if(CheckBoxFacile.isSelected()) {
				for(Personnage perso : this.game.getListePerso()) {
					if(perso.getId()!=this.game.getPersonnage().getId()&&(!this.game.getSauvegarde().getBannis().contains(perso.getId()))) {
						if(this.game.verifArgument(questionEnCours,constructeur)&&perso.verifierArguments(questionEnCours, constructeur) || !this.game.verifArgument(questionEnCours,constructeur)&&!perso.verifierArguments(questionEnCours, constructeur)) {
						//rien car notre personnage possede/ne possede pas et l'autre perso possede/ne possede pas aussi donc on ompte pas.
						}else {
							preBannisButton.add(Buttons.get(Integer.parseInt(perso.getId())));
							preBannis.add((String)perso.getId());
						}
					}
				}
				ban(e);
			}
			questionEnCours.clear();
			appendText(LabelChat," ");
		}
	}
	
	public void activerFacile(ActionEvent e) {
		appendText(LabelChat,"Changement mode !");
	}
	
	/**
	 * Quand n'import quel @param button est clique on regarde s'il est deja dans la list des
	 * prebannis au quel cas on l'enleve de celle-ci, s'il n'y est pas alors on l'ajoute.
	 */
	public void checkId(Button button) {
		if(preBannisButton.contains(button)) {
			button.setStyle("-fx-background-color:none");
			preBannis.remove(button.getId());
			preBannisButton.remove(button);
		}else{
			button.setStyle("-fx-background-color:yellow");
			preBannis.add(button.getId());
			preBannisButton.add(button);
			System.out.println(preBannis.toString());
		}
	}
	
	/**
	 * lors du clic sur le bouton BAN @param e on va ajouter les prebannis a la liste des bannis
	 * et on va le montrer visuelement et desactiver le bouton, puis on vide la list des prebannis.
	 */
	public void ban(ActionEvent e) {
		try {
		if(!preBannisButton.isEmpty()) {
		for(Button b : preBannisButton) {
			b.setStyle("-fx-background-color:red");
			b.setDisable(true);
		}
		preBannisButton.clear();
		this.game.getSauvegarde().ajoutBannis(preBannis);
		System.out.println(this.game.getSauvegarde().getBannisJSON().toString());
		preBannis.clear();
		if(this.game.getSauvegarde().getBannis().size()>=this.game.getGrille().getTaille()-1) {
			if(!this.game.getSauvegarde().getBannis().contains(this.game.getPersonnage().getId())) {
				System.out.println("Vous etes le meilleur ! ");
				appendText(LabelChat,"Vous etes le meilleur ! ");
				this.game.finirLaPartie();
			}else {
				System.out.println("Vous avez perdu ! ");
				appendText(LabelChat,"Vous avez perdu ! ");
				this.game.finirLaPartie();
			}
		}
		}else {
			appendText(LabelChat,"Pas de personnage  bannir!");
		}
		}catch(Exception ex) {
			appendText(LabelChat,"Pas de personnage  bannir!");
		}
	}
	
	public void ajouterQuestion(ActionEvent e) {
		try {
			if(ListViewArgument.getSelectionModel().getSelectedItem()!=null) {
			String attSelect = ListViewAttribut.getSelectionModel().getSelectedItem();
			if(questionEnCours.containsKey(attSelect)) {
				ArrayList<String> argsActu = questionEnCours.get(attSelect);
				argsActu.add(ListViewArgument.getSelectionModel().getSelectedItem());
				questionEnCours.put(attSelect, argsActu);
			}else {
				ArrayList<String> argSelect = new ArrayList<String>();
				argSelect.add(ListViewArgument.getSelectionModel().getSelectedItem());
				questionEnCours.put(attSelect, argSelect);
			}
			System.out.println(questionEnCours.toString());
			donnerQuestion();appendText(LabelChat," ");
			}else {
				appendText(LabelChat,"Veuillez choisir :");
				appendText(LabelChat,"-Un attribut");
				appendText(LabelChat,"-Un argument");
			}
		} catch(Exception ex) {
			appendText(LabelChat,"Veuillez choisir :");
			appendText(LabelChat,"-Un attribut");
			appendText(LabelChat,"-Un argument");
			System.out.print("Question pas ajout !");
		}

	}
	
	public void save(ActionEvent event) {
        this.game.sauvegarder();
        appendText(LabelChat,"Partie sauvegarde au nom :");
        appendText(LabelChat,this.game.getSauvegarde().getNom());
        appendText(LabelChat,"Date de creation de cette partie!");
    }

    public void saveAndQuit(ActionEvent event) throws IOException {
        save(event);
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Menu");
        stage.show();
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Menu");
		stage.setMinWidth(600.0);
        stage.setMinHeight(400.0);
        stage.show();
    }
}
