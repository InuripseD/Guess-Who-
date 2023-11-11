package application;
//
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import quiestce.associations.Personnage;
import quiestce.generateur.PreGrille;

public class GenerateurController implements Initializable {
	@FXML
	ListView<String> listViewArguments;
	@FXML
	ListView<String> listViewAttributs;
	@FXML
	AnchorPane inputArguments;
	@FXML
	AnchorPane inputAttributs;
	@FXML
	TextField textFieldArg;
	@FXML
	TextField textFieldAttr;
	@FXML
	Button addBtnArg;
	@FXML
	Button addBtnAttr;
	@FXML
	Button addArgHisto;
	@FXML
	ListView<String> histoArguments;
	@FXML
	GridPane GridPaneGenerateur;
	@FXML
	AnchorPane anchorPanePerso;
	@FXML
	TextField nomGrille;
	@FXML
	Label status;
	
	Stage stage;
	
	public static final int NBCOLONNES = 6;
	public static final int TAILLEIMAGE = 100;
	public static final int ECARTIMAGE = 25;
	private int ligne=1;
	private int col=0;
	PreGrille pregrille;
	ArrayList<ImageView> ImageViews = new ArrayList<ImageView>();
	
	private Button ButtonSelected;
	
	public void multipleFileChooser(ActionEvent event) {
		FileChooser fc= new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files","*png"));
		List<File> f = fc.showOpenMultipleDialog(null);
		if(f!=null) {
		anchorPanePerso.setPrefHeight(anchorPanePerso.getPrefHeight()+Math.ceil(f.size()/6)*(TAILLEIMAGE+ECARTIMAGE)		+ECARTIMAGE+TAILLEIMAGE);
		for(File file : f) {
			try {
				//int nbLigne = (int)Math.ceil(game.getGrille().getTaille()/NBCOLONNES);
				boolean tf = false;
				for(Personnage p :this.pregrille.getPersos()) {
					if(p.getPhoto().equals(file.getName())) {
						tf = true;
					}
				}
				if(!tf) {
					BufferedImage bufferedImage = ImageIO.read(file);
					Image image = SwingFXUtils.toFXImage(bufferedImage, null);
					ImageView view = new ImageView();
					view.setFitHeight(TAILLEIMAGE*0.9);
					view.setFitWidth(TAILLEIMAGE*0.9);
					view.setImage(image);
					view.setId(file.getName());
					ImageViews.add(view);
					Button button = new Button();
					button.setId(""+this.pregrille.getCompteur());
					button.setOnAction(even -> setButton((Button) even.getSource()));
					button.setGraphic(view);
					button.setStyle("-fx-background-color:none");
					this.pregrille.ajouterPerso(file.getName(),""+pregrille.getCompteur());
				
					for(String item : listViewAttributs.getItems()){
						if(!pregrille.getPersoUnique(button.getId()).getCaracteristique().containsKey(item)) {
							ArrayList<String> args = new ArrayList<String>();
							this.pregrille.getPersoUnique(button.getId()).setCaracteristique(item, args);;
						}
					}
				
					GridPaneGenerateur.add(button, col, ligne);
					status.setText("");
				
					col++;
					if(col>NBCOLONNES-1) {
						col=0;
						ligne++;
					}
				}
				else {
					status.setText("Ce personnage existe deja !");
				}
		} catch (IOException e) {
				e.printStackTrace();
				}
			}
		}
	}
	
	public void storeImages() {
		for(ImageView iv : ImageViews ) {
			Image ImageToSave = iv.getImage();
			
			File file = new File("../qui-est-ce/Grilles/"+this.pregrille.getNom()+"/"+iv.getId()+"");
			
			try {
				if(iv.getId().contains(".png")) {
					ImageIO.write(SwingFXUtils.fromFXImage(ImageToSave, null), "png", file);
				}else {
					ImageIO.write(SwingFXUtils.fromFXImage(ImageToSave, null), "PNG", file);
				}
			}catch(IOException e) {
				
			}
		}
	}
	
	public void setButton(Button button) {
		if(ButtonSelected!=null) {
			this.ButtonSelected.setStyle("-fx-background-color:none");
		}
		
		this.ButtonSelected = button;
		button.setStyle("-fx-background-color:yellow");
		
		if(ButtonSelected!=null) {
			this.listViewArguments.getItems().clear();
			this.histoArguments.getItems().clear();
			ArrayList<String> argsTotaux = new ArrayList<String>();
         	argsTotaux = pregrille.getArgsList(listViewAttributs.getSelectionModel().getSelectedItem());
             
             if(ButtonSelected==null) {
             	histoArguments.getItems().addAll(argsTotaux);
             }
             
             if(ButtonSelected!=null) {
             	ArrayList<String> argsDuPerso = new ArrayList<String>();
             	argsDuPerso = pregrille.getPersoUnique(ButtonSelected.getId()).getCaracteristique().get(listViewAttributs.getSelectionModel().getSelectedItem());
             	if(argsDuPerso!=null) {
             		listViewArguments.getItems().addAll(argsDuPerso);             	
             		//argsTotaux.removeAll(argsDuPerso);
             	}
             	if(argsTotaux!=null) {
             		histoArguments.getItems().addAll(argsTotaux);
             	}
             }
		}	
	}
	
	public void afficheInputArguments(ActionEvent event) {
		if(inputArguments.isVisible()) {
			inputArguments.setVisible(false);
			textFieldArg.clear();
		}else {
			inputArguments.setVisible(true);
		}
	}
	
	private void afficherEtGererAttributsArgs() {
        listViewAttributs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                listViewArguments.getItems().clear();
                histoArguments.getItems().clear();
                
                ArrayList<String> argsTotaux = new ArrayList<String>();
            	argsTotaux = pregrille.getArgsList(listViewAttributs.getSelectionModel().getSelectedItem());
                
                if(ButtonSelected==null) {
                	histoArguments.getItems().addAll(argsTotaux);
                }
                
                if(ButtonSelected!=null) {
                	ArrayList<String> argsDuPerso = new ArrayList<String>();
                	argsDuPerso = pregrille.getPersoUnique(ButtonSelected.getId()).getCaracteristique().get(listViewAttributs.getSelectionModel().getSelectedItem());
                	listViewArguments.getItems().addAll(argsDuPerso);
                	
                	//argsTotaux.removeAll(argsDuPerso);
                	
                	histoArguments.getItems().addAll(argsTotaux);
                }
                
            }    
        });    
    }
	
	public void afficheInputAttributs(ActionEvent event) {
		if(inputAttributs.isVisible()) {
			inputAttributs.setVisible(false);
			textFieldAttr.clear();
		}else {
			inputAttributs.setVisible(true);
			
		}
	}	
	public void ajoutAttribut(ActionEvent event) {
		status.setText("");
		String Attr= textFieldAttr.getText();
		if(Attr!=""&&!listViewAttributs.getItems().contains(Attr)) {
			listViewAttributs.getItems().add(Attr);
			textFieldAttr.clear();
			this.pregrille.addAttributs(Attr);
		}else {
			textFieldAttr.clear();
			if(this.pregrille.getAttList().contains(Attr)) {
				status.setText("Cet attribut existe deja !");
			}
			if(Attr=="") {
            	status.setText("Veuillez saisir un argument !");
            }
		}
	}
	
	public void ajoutArgument(ActionEvent event) {
        String Arg = textFieldArg.getText();
        if(Arg!=""&&listViewAttributs.getSelectionModel().getSelectedItem()!=null&&!this.pregrille.getArgsList(listViewAttributs.getSelectionModel().getSelectedItem()).contains(Arg)) {
            histoArguments.getItems().add(Arg);
            textFieldArg.clear();
            this.pregrille.addArgument(listViewAttributs.getSelectionModel().getSelectedItem(), Arg);
            status.setText("");
        }else {
            textFieldArg.clear();
            if(Arg=="") {
            	status.setText("Veuillez saisir un argument !");
            }
            if(listViewAttributs.getSelectionModel().getSelectedItem()==null) {
            	status.setText("Veuillez selectionner un attribut specifique !");
            }
            if(this.pregrille.getArgsList(listViewAttributs.getSelectionModel().getSelectedItem()).contains(Arg)) {
            	status.setText("Cet argument existe deja !");
            }
            System.out.println("Argument pas ajoutÃ© !\nPas d'attribut select !");
        }
    }
	
	public void supprAttribut(ActionEvent event) {
		status.setText("");
		if(listViewAttributs.getSelectionModel().getSelectedItem()!=null) {
            for(Personnage p: this.pregrille.getPersos()) {
                p.removeAtt(listViewAttributs.getSelectionModel().getSelectedItem());
            }
            this.pregrille.deleteAttribut(listViewAttributs.getSelectionModel().getSelectedItem());
        }
		
		final int selectedIdx = listViewAttributs.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
          String itemToRemove = listViewAttributs.getSelectionModel().getSelectedItem();
 
          final int newSelectedIdx =
            (selectedIdx == listViewAttributs.getItems().size() - 1)
               ? selectedIdx - 1
               : selectedIdx;
 
          listViewAttributs.getItems().remove(selectedIdx);
          listViewAttributs.getSelectionModel().select(newSelectedIdx);
        }
      }
	
	public void supprArgument(ActionEvent event) {
		status.setText("");
        final int selectedIdx = listViewArguments.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
          String itemToRemove = listViewArguments.getSelectionModel().getSelectedItem();
 
          final int newSelectedIdx =
            (selectedIdx == listViewArguments.getItems().size() - 1)
               ? selectedIdx - 1
               : selectedIdx;
 
          listViewArguments.getItems().remove(selectedIdx);
          listViewArguments.getSelectionModel().select(newSelectedIdx);
          ArrayList<String> argSelect = new ArrayList<String>();
          argSelect.addAll(pregrille.getPersoUnique(ButtonSelected.getId()).getCaracteristique().get(listViewAttributs.getSelectionModel().getSelectedItem()));
          argSelect.remove(itemToRemove);
          this.pregrille.getPersoUnique(ButtonSelected.getId()).removeAtt(listViewAttributs.getSelectionModel().getSelectedItem());
          this.pregrille.getPersoUnique(ButtonSelected.getId()).setCaracteristique(listViewAttributs.getSelectionModel().getSelectedItem(), argSelect);
        }
        
      }

	  public void supprArgHisto(ActionEvent event) {
		status.setText("");
		boolean tf = false;
		for(Personnage p : this.pregrille.getPersos()) {
			ArrayList<String> argperso = new ArrayList<String>();
			argperso.addAll(p.getCaracteristique().get(listViewAttributs.getSelectionModel().getSelectedItem()));
			if(argperso.contains(histoArguments.getSelectionModel().getSelectedItem())) {
				tf=true;
			}
		}
			if(!tf) {
				final int selectedIdx = histoArguments.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {
					String itemToRemove = histoArguments.getSelectionModel().getSelectedItem();
 
					final int newSelectedIdx =
							(selectedIdx == histoArguments.getItems().size() - 1)
							? selectedIdx - 1
									: selectedIdx;
 
					histoArguments.getItems().remove(selectedIdx);
					histoArguments.getSelectionModel().select(newSelectedIdx);
					this.pregrille.deleteArgument(listViewAttributs.getSelectionModel().getSelectedItem(), listViewArguments.getSelectionModel().getSelectedItem());
					status.setText("");
				}
			}else {
				status.setText("Un personnage possede cet argument !");
			}
	}
	
	public void ajoutArgFromHisto(ActionEvent event) {
        if(histoArguments.getSelectionModel().getSelectedItem()!=null&&ButtonSelected!=null&&!listViewArguments.getItems().contains(histoArguments.getSelectionModel().getSelectedItem())) {
            status.setText("");
        	ArrayList<String> argSelect = new ArrayList<String>();
            argSelect.add(histoArguments.getSelectionModel().getSelectedItem());
            argSelect.addAll(pregrille.getPersoUnique(ButtonSelected.getId()).getCaracteristique().get(listViewAttributs.getSelectionModel().getSelectedItem()));
            this.pregrille.getPersoUnique(ButtonSelected.getId()).setCaracteristique(listViewAttributs.getSelectionModel().getSelectedItem(), argSelect);
            listViewArguments.getItems().add(histoArguments.getSelectionModel().getSelectedItem());
            //histoArguments.getItems().remove(histoArguments.getSelectionModel().getSelectedItem());
        }else {
            if(listViewArguments.getItems().toString().contains(""+histoArguments.getSelectionModel().getSelectedItem())) {
            	status.setText("Cet argument a deja ete ajoute !");
            }
            if(histoArguments.getSelectionModel().getSelectedItem()==null) {
            	status.setText("Veuillez selectionnez l'argument avant de l'ajouter !");
            }
            if(ButtonSelected==null) {
            	status.setText("Veuillez preciser quel personnage !");
            }
            if(histoArguments.getSelectionModel().getSelectedItem()==null) {
            	status.setText("Aucun argument n'est selectionne !");
            }
        }
    }
	
	public void genererGrille(ActionEvent event) {
		if(nomGrille.getText()!=null&&nomGrille.getText()!=""&&checkSiOnGenere()) {
			if(this.pregrille.getNom()!=null){
				File file= new File("../qui-est-ce/Grilles/"+this.pregrille.getNom());file.delete();
			}
			//File file= new File("../qui-est-ce/Grilles/"+this.pregrille.getNom());file.delete();
			this.pregrille.setNom(nomGrille.getText());
			File file= new File("../qui-est-ce/Grilles/"+this.pregrille.getNom());file.delete();
			File grilleFolder = new File("../qui-est-ce/Grilles/"+this.pregrille.getNom());
			grilleFolder.mkdir();
			this.storeImages();
			this.pregrille.creerJSON();
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			stage.close();
		}else {
			if(!checkSiOnGenere()) {
				status.setText("Probleme d'arguments !");
			}else {
				status.setText("Veuillez donner un nom a votre Grille !");
			}
		}
	}

	public void Sauvegarder(ActionEvent event) {
        if(nomGrille.getText()!=null&&nomGrille.getText()!="") {
            this.pregrille.setNom(nomGrille.getText()+"SAVE");
            File file= new File("../qui-est-ce/Grilles/"+this.pregrille.getNom());file.delete();
            File grilleFolder = new File("../qui-est-ce/Grilles/"+this.pregrille.getNom());
            grilleFolder.mkdir();
            this.storeImages();
            this.pregrille.creerJSON();
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }else {
        	status.setText("Veuillez donner un nom a votre Grille !");
            System.out.println("pas sauvegarde ! ");
        }
    }

	public boolean checkSiOnGenere() {
        boolean test = true;
        boolean test2 = true;
        int count = 0;
        for(Personnage perso : this.pregrille.getPersos()) {
            for(Personnage perso2 : this.pregrille.getPersos()) {
            	count = 0;
            	if(perso!=perso2) {
            		for(String att : this.pregrille.getAttList()) {
            			if(att!="id"&&att!="photo") {
            				if(perso.getCaracteristique().get(att).containsAll(perso2.getCaracteristique().get(att))&&perso2.getCaracteristique().get(att).containsAll(perso.getCaracteristique().get(att))) {
            					count++;
            					System.out.println(count);
            				}
            			}
            		}
            	}
                if(count==this.pregrille.getAttList().size()) {
                	test2 = false;
                }
            }
            if(!test2) {
                test=false;
            }
            
        }
        
        System.out.println(this.pregrille.getAttList().size());
        
        for(Personnage perso : this.pregrille.getPersos()) {
            for(String att : this.pregrille.getAttList()) {
                if(perso.getCaracteristique().get(att).size()==0) {
                    test=false;
                }
            }
        }
        
        return test;
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		JSONParser parser = new JSONParser();//Creation d'un analyseur.
		JSONObject settings = new JSONObject();//Creation d'un JSONObject
		try {
			settings = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Parametres/parametres.json"));//On essaie d'ouvrir et d'analyser le .json
		} catch (IOException | ParseException e) {
			settings = null;
			System.out.println("settings non trouve !");
		}
		String grilleGenerateur = (String)settings.get("grilleGenerateur");//On prend tous les personnages du .json pour les mettre dans une JSONArray.
		
		if(grilleGenerateur==""||grilleGenerateur==null) {
			inputAttributs.setVisible(false);
			inputArguments.setVisible(false);
			afficherEtGererAttributsArgs();
			this.pregrille = new PreGrille("grille"+PreGrille.TOTAL, false);
			//this.pregrille.setNom(grilleGenerateur);
		}else {
			String s = settings.get("grilleGenerateur").toString();
			if(s.contains("SAVE")) {
				nomGrille.setText(s.substring(0,s.length() - 4));
			}else {
				nomGrille.setText(s);
			}
			inputAttributs.setVisible(false);
			inputArguments.setVisible(false);
			afficherEtGererAttributsArgs();
			this.pregrille = new PreGrille(grilleGenerateur);
			this.pregrille.setNom(grilleGenerateur);
			listViewAttributs.getItems().addAll(pregrille.getAttList());
			
			int nbLigne = (int)Math.ceil(this.pregrille.getTaille()/NBCOLONNES);//On demande le nombre de perso puis on divise 6 cela sera le nombre de ligne.
			anchorPanePerso.setPrefHeight(anchorPanePerso.getPrefHeight()+Math.ceil(nbLigne)*(TAILLEIMAGE+ECARTIMAGE)+ECARTIMAGE+TAILLEIMAGE);
			HashMap<String,String> idEtLink = this.pregrille.getImage();//On va chercher l'hashmap AVec id et liens des photos.
			for(String imageID : idEtLink.keySet()) {//Pour chaque Id / image	
				System.out.println(idEtLink.get(imageID).toString());
				File file = new File("../qui-est-ce/Grilles/"+pregrille.getNom()+"/"+idEtLink.get(imageID));
				Image image = new Image(file.toURI().toString());
				System.out.println(file.exists());
				ImageView view = new ImageView();
				view.setFitHeight(TAILLEIMAGE*0.9);
				view.setFitWidth(TAILLEIMAGE*0.9);
				view.setImage(image);
				view.setId(idEtLink.get(imageID).toString());
				ImageViews.add(view);
				Button button = new Button();
				button.setId(imageID);//On fabrique image plus imageView et on met dans Button.
				button.setOnAction(event -> setButton((Button) event.getSource()));//Chaque Boutton va enclencher la methode checkID().
				button.setGraphic(view);
				this.pregrille.compteur++;
				button.setStyle("-fx-background-color:none");
				GridPaneGenerateur.add(button, col, ligne);//on ajoute le button a la gridpane
				col++;
				if(col>NBCOLONNES-1) {//on retour a la ligne quand on a mit 6 elements.
					col=0;
					ligne++;
				}
			}
			
		}
		
	}

}
