package quiestce.associations;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Grille {
	
	protected JSONArray grille;	   //La liste de tout les personnage sous forme de JSONArray ou chaque personnage est sous forme de JSONObject.
	protected String nom;	   //Le nom du fichier dans lequel est situe le .json et le nom du .json ex:"grillesimple.json", le nom est grillesimple.
	protected int taille;			   //Le nombre de personnage que contient la grille a partir de 1. (Le dernier perso a pour id 37 alors il a 38 perso car la premiere id est 0)
	protected HashMap<String,ArrayList<String>> AttributsEtArguments = new HashMap<String,ArrayList<String>>(); //La liste de tout les differents attributs present dans la grille pour chaque personnage, et pour chaque attributs sa liste d'arguments.
	
	public Grille(String filename, boolean b) {
		this.nom = filename;
	}
	
	/**
	 * Prend en @param filename le nom de la grille que notre partie utilise pour pouvoir generer une grille en fonction.
	 * Le nom de celle-ci sera le nom de la grille utilise sans le '.json', puis on va mettre la liste des persos de ce .json
	 * s'il existe dans un JSONArray.
	 * La taille est le nombre de perso qu'il y a dans la JSONArray.
	 * Puis on va mettre en memoire la liste de chaque attribut avec ces arguments.
	 */
	public Grille(String filename) {
		this.nom=filename;//On met un nom a la Grille.
		this.setJSONArrayPerso();//On met en memoire la liste des Personnage dans une JSONArray.
		this.taille = grille.size();//On prend la taille de la la JSONArray.
		this.setAttributsEtArguments();//On met en memoire la liste de chaque attribut avec ces arguments.
		
	}
	
	/*------------------------LES GETTERS/SETTERS----------------------------*/
	
	/**
	 * @return le nom du fichier .json et donc le nom de cette grille. 
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * !!! Cette methode n'est pas suppose etre utile !!!
	 * @return le JSONArray de tout les personnages du fichier ".json".
	 */
	public JSONArray getGrille() {
		return this.grille;
	}
	
	/**
	 * @return le nombre de personnage present dans le JSONArray de personnages.
	 */
	public int getTaille() {
		return this.taille;
	}
	
	/**
	 * Utile pour la methode setCaracteristique() de la classe personnage.
	 * @param id pour connaitre quel personnage sous forme de JSONObject on doit @return.
	 */
	public JSONObject getPerso(String id) {
		return (JSONObject) this.grille.get(Integer.parseInt(id));
	}
	
	/**
	 * Utile pour afficher la liste des attributs avec sa liste d'arguments.
	 * @return simplement l'hashmap avec pour chaque attribut sa liste d'arguments.
	 */
	public HashMap<String,ArrayList<String>> getAttsEtArgs() {
		return this.AttributsEtArguments;
	}
	
	public void setAttsEtArgs(String att,ArrayList<String> args) {
		this.AttributsEtArguments.put(att, args);
	}
	
	public void removeAtt(String att) {
		this.AttributsEtArguments.remove(att);
	}
	
	/*-------Utilitaire mise en memoire d'info sur les personnages de la grille------*/
	
	/**
	 * Utile lors de l'affichage des personnages, car on a besoin d'une photo et
	 * de l'id de pour pouvoir gerer les bans etcs...
	 * Cette methode @return la liste des nom des images de chaques perso avec son id dans une ArrayList.
	 */
	public HashMap<String,String> getImage() {
		HashMap<String,String> imageLinks = new LinkedHashMap<String,String>();//init l'arraylist.
		for(Object perso : this.getGrille()) {//Pour chaque perso de la grille.
			imageLinks.put(""+((JSONObject)perso).get("id"),(String)((JSONObject)perso).get("photo"));//On prend l'argument de photo pour le persoEnCours et on ajoute cette argument/url a l'arraylist AVEC son ID !
		}
		return imageLinks;//On retourne l'arraylist d'url des photos/id.
	}
	
	/**
	 * Utile lors de la creation d'une grille pour une implementation de IGame.
	 * Initialise l'hashmap d'attributs et d'arguments de cette grille.
	 */
	protected void setAttributsEtArguments() {
		for(String att : this.getAttList()) {
			this.AttributsEtArguments.put(att, this.getArgsList(att));
		}
	}
	
	/**
	 * @return la liste des attribut possible dans cette grille sauf ceux de base (id et photo)
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getAttList() {
		ArrayList<String> attList = new ArrayList<String>();//init arraylist
		JSONObject persoEncours = (JSONObject)this.grille.get(0);//prend le premier perso car tout les perso ont les meme attributs.
		attList.addAll(persoEncours.keySet());//Ajoute tous les attributs.
		attList.remove("id");//retire id 
		attList.remove("photo");//retire photo
		System.out.println("Liste attribut : "+attList.toString());
		return attList;//return la liste des attributs.
	}
	
	/**
	 * Prend un attribut en @param (att) 
	 * et @return tout les arguments possible pour cet attribut en 
	 * allant voir dans tout les persos.
	 */
	private ArrayList<String> getArgsList(String att) {
		ArrayList<String> argsList = new ArrayList<String>();//init l'arraylist
		for(Object perso : this.grille) {//Va allait sur tous les perso de la grille actuelle.
			if(((JSONObject)perso).get(att) instanceof JSONArray) {//Regarde si c'est une liste d'argument ou un simple argument
				for(Object arg : (JSONArray)((JSONObject)perso).get(att)) {//Pointeur qui va d'argument en argument de cet attribut dans le ca ou il y en a plusierus.
					if(!argsList.contains(""+arg)) {//verifie qu'il n'y ait pas deja.
						argsList.add(""+arg);//On ajoute les arguments de la liste d'arguments un par un.
					}
				}
			}else {
				if(!argsList.contains(""+((JSONObject)perso).get(att))) {//verifie qu'il n'y ait pas deja.
					argsList.add(""+((JSONObject)perso).get(att));//On ajoute l'argument.
				}
			}
		}
		System.out.println(argsList.toString());
		return argsList;//retourne la liste des arguments sans doublons.
	}

	/**
	 * Utile lors de la creation d'un grille.
	 * Cette methode va aller chercher le .json dans le dossier Grilles et l'analyser
	 * pour mettre la liste des persos dans une JSONArray.
	 */
	protected void setJSONArrayPerso() {
		JSONParser parser = new JSONParser();//Creation d'un analyseur.
		JSONObject grilleDePerso = new JSONObject();//Creation d'un JSONObject
		try {
			grilleDePerso = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Grilles/"+this.getNom()+"/"+this.getNom()+".json"));//On essaie d'ouvrir et d'analyser le .json
		} catch (IOException | ParseException e) {
			grilleDePerso = null;
			System.out.println("Grille non trouvé !");
		}
		this.grille = (JSONArray)grilleDePerso.get("personnages");//On prend tous les personnages du .json pour les mettre dans une JSONArray.
	}
}
