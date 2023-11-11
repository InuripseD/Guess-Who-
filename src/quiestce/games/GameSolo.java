package quiestce.games;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import quiestce.associations.Grille;
import quiestce.associations.Personnage;
import quiestce.associations.Sauvegarde;

public class GameSolo implements IGame{
	
	private boolean cheatMode=false; //Activer ou desactiver le mode triche !
	protected boolean droitCheatMode; //Choisir si ou pourra utiliser le cheat mode ou non durant la partie !
	//private int nbQuestionPose=0; //S'incremente a chaque question pose, pour poser une limite ou pour savoir en combien de question on a gagnÃƒÂ© !
	protected Statut statut=Statut.enCours; //Est-ce que la partie est finit ou en cours, utile pour l'historique !
	protected Grille grille;//Une partie est lance avec une grille prÃƒÂ©cise et ne pourra etre relance qu'avec cette mÃƒÂªme grille.
	protected Personnage persoCache; //Le personnage que l'on doit trouver !
	protected Sauvegarde save;//On met la list des bannis dedans mais que l'id.
	protected ArrayList<Personnage> listePersonnage = new ArrayList<Personnage>();//La liste des persos
	
	/*------------------------------CONSTRUCTORS-----------------------------*/
	
	/**
	 * On a besoin d'un constructeur pour pouvoir gÃ©nÃ©rer les Game Ã  partir d'une sauvegarde!
	 * Donc ce constructeur est appelÃ© quand la personne veut jouer sur une partie sauvegarde!
	 */
	public GameSolo(String save) {
		this.chargerSauvegarde(save);
	}
	
	/**
	 * On creer une partie en lui associant une grille et un personnage cache.
	 * On dit si la partie autorisera le mode triche ou pas avec @param droitCheatMode. 
	 * Le nom de la grille est mentionne lors de la creation @param grille.
	 * Puis on met en memoire une sauvegarde virtuel et on setup la list
	 * de tous les persos de la grille car on a besoin d'une list de perso.
	 */
	public GameSolo(boolean droitCheatMode, String grille) {
		this.grille = new Grille(grille);
		this.droitCheatMode = droitCheatMode;
		this.setListePerso();
		this.persoCache = this.cacherPersonnage();
		this.save = new Sauvegarde(LocalDate.now()+"at"+LocalDateTime.now().getHour()+"h"+LocalDateTime.now().getMinute()+"m"+LocalDateTime.now().getSecond()+"s");
	}
	
	/*-------------------------GETTERS/SETTERS---------------------*/
	
	/**
	 * utile pour savoir si on met la personne en mode triche apres sa demande !
	 * @return (true) si la personne peut activer le mode triche et (false) sinon.
	 */
	public boolean getDroitCheatMode() {
		return this.droitCheatMode;
	}
	
	/**
	 * Utile pour savoir si le mode triche est actif ou pas.
	 * @return Si le mode triche est actuellement active (true) et (false) sinon !
	 */
	public boolean getCheatMode() {
		return this.cheatMode;
	}
	
	/**
	 * Active ou desactive le mode triche a l'aide de @param activeOUdesactive !
	 * Private car il n'y a que la methode changerModeTriche() qui peut changer le mode triche.
	 */
	private void setCheatMode(boolean activeOUdesactive) {
		this.cheatMode = activeOUdesactive;
	}
	
	/**
	 * @return simplement la grille qui est utilise par la partie.
	 */
	public Grille getGrille() {
		return this.grille;
	}
	
	/**
	 * @return simplement le personnage qui est cache dans la partie.
	 */
	public Personnage getPersonnage() {
		return this.persoCache;
	}
	
	/**
	 * @return simplement le personnage qui est cache dans la partie.
	 */
	public Sauvegarde getSauvegarde() {
		return this.save;
	}
	
	/**
	 * Methode Utile lors de la creation d'une game.
	 * Elle sert simplement a mettre en memoire (Dans l'arraylist dedie a enregistrer les perso)
	 */
	protected void setListePerso() {
		for(int i = 0 ; i < this.grille.getTaille() ; i++) {//Pour i de 0 a la taille de la liste des perso du fichier .json.
			this.listePersonnage.add(new Personnage(""+i,this.getGrille()));//On cree un nouveau perso pour chaque id de la liste.
		}
	}
	
	/**
	 * Utile pour obtenir des infos sur tous les persos.
	 * @return le liste des personnages y compris le personnage cache de la partie.
	 */
	public ArrayList<Personnage> getListePerso(){
		return this.listePersonnage;
	}
	
	/**
	 * Utile pour ecrire dans la sauvegarde.
	 * Utile pour savoir si notre joueur peut continuer a poser des questions.
	 * @return simplement si la partie est finit ou en cours.
	 */
	public Statut getStatut() {
		return this.statut;
	}
	
	/*---------------Utilitaire de la classe GameSolo---------------*/
	
	/**
	 * Cette fonction est appele lorsque l'utilisateur clique sur le bouton 
	 * pour activer le mode triche. Tout dabord elle teste si cette partie de jeu
	 * autorise l'activation du mode de triche, puis si c'est le cas active
	 * le mode cheat s'il n'est pas active ou le desactive s'il est actif ! 
	 */
	@Override
	public void changerModeTriche() {
		if(this.getDroitCheatMode()) {
			this.setCheatMode(!this.getCheatMode());
		}
	}

	/**
	 * Utile lorsque l'on souhaite savoir si le personnage cache de notre partie possede
	 * les attributs et arguments de la question.
	 * 
	 * @return (voir methode unpersonnage.verifierArguments())
	 * @param attsEtArgs La question.
	 * @param anorOr Le mode/constructeur a utiliser.
	 */
	@Override
	public boolean verifArgument(HashMap<String, ArrayList<String>> attsEtArgs, Boolean andorOr) {
		return this.persoCache.verifierArguments(attsEtArgs, andorOr);//retourne si true ou false.
	}
	
	/**
	 * Prend en @param bans la liste des persos qui sont coches(les prebannis) sur la GUI et ajoutes les ids a la liste
	 * des persos bannis dans notre save.
	 */
	@Override
	public void bannir(ArrayList<String> bans) {
		this.save.ajoutBannis(bans);
	}
	
	/**
	 * Creer un nombre aleatoire entre 0 et l'id max des personnage de notre .json,
	 * puis @return un personnage base sur cette id dans notre grille.
	 */
	@Override
	public Personnage cacherPersonnage() {
		Random random = new Random();//On creer un Randomiser.
		int idAleatoire = random.nextInt(this.grille.getTaille());//On prend un nombre aleatoire entre 0 et la taille-1 de la liste des personnages.
		//return new Personnage(""+idAleatoire,this.grille);//retourne le perso.
		return this.listePersonnage.get(idAleatoire);
	}
	
	/**
	 * Cette fonction est appele lorsque la partie est termine (1 ou 0 personnage restant dans la liste des non bannis) !
	 * Elle met l'etat actuel de la partie en 'finie'.
	 */
	@Override
	public void finirLaPartie() {
		this.statut = Statut.finie;
	}
	
	/**
	 * Creer une fichier JSON dans le fichiers des sauvegarde si la sauvegarde n'existe pas.
	 */
	@Override
	public void sauvegarder() {
		JSONObject sauvegarde = new JSONObject();
		sauvegarde.put("nom",this.save.getNom());
		sauvegarde.put("etat", this.statut.toString());
		sauvegarde.put("grille", this.grille.getNom());
		sauvegarde.put("persocache", this.persoCache.getId());
		sauvegarde.put("bannis", this.save.getBannisJSON());
		try {
			File file= new File("../qui-est-ce/Sauvegardes/"+this.save.getNom()+".json");
			file.delete();
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Sauvegardes/"+this.save.getNom()+".json");
			fileWriter.write(sauvegarde.toJSONString());
			fileWriter.close();
		}catch(IOException e) {
			
		}
	}
	
	/**
	 * Methode qui va chercher dans le fichier json de sauvegarde selectionne toutes les 
	 * infos de la game pour les remettre en place.
	 */
	private void chargerSauvegarde(String nomSauvegarde) {
		JSONParser parser = new JSONParser();
		JSONObject loadSave = new JSONObject();
		try {
			loadSave = (JSONObject)parser.parse(new FileReader("../qui-est-ce/Sauvegardes/"+nomSauvegarde));
		} catch (Exception e) {
			loadSave = null;
			System.out.println("Sauvegarde non trouvée !");
		}
		JSONArray bannisList=(JSONArray)loadSave.get("bannis");
		ArrayList<String> listBannis = new ArrayList<String>();
		for(Object id : (JSONArray)bannisList){
	           listBannis.add((String)id);
		}
		String partiee = (String) loadSave.get("grille");
		//partiee = partiee.substring(0, partiee.length());
		this.grille = new Grille(partiee);
		this.droitCheatMode = true;
		this.setListePerso();
		this.persoCache = this.listePersonnage.get(Integer.parseInt((String)loadSave.get("persocache")));
		nomSauvegarde = nomSauvegarde.substring(0, nomSauvegarde.length() - 5);
		this.save = new Sauvegarde(nomSauvegarde);
		save.ajoutBannis(listBannis);
		System.out.println(save.getNom());	
	}
}
