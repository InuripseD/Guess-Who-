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

import quiestce.associations.Grille;
import quiestce.associations.Personnage;
import quiestce.associations.Sauvegarde;

public class GameSolo2 extends GameSolo {
	
	private Personnage persoCache2;

	public GameSolo2(boolean droitCheatMode, String grille) {
		super(droitCheatMode, grille);
		this.grille = new Grille(grille);
		this.droitCheatMode = droitCheatMode;
		this.setListePerso();
		this.cacherPersonnages();
		this.save = new Sauvegarde("2c"+LocalDate.now()+"at"+LocalDateTime.now().getHour()+"h"+LocalDateTime.now().getMinute()+"m"+LocalDateTime.now().getSecond()+"s");
	}
		
	public GameSolo2(String sauvegardeOUpas) {
		super(sauvegardeOUpas);
		this.chargerSauvegarde(sauvegardeOUpas);
	}

	public Personnage getPersonnage2(){
		return this.persoCache2;
	}

	private void cacherPersonnages() {
		Random random = new Random();//On creer un Randomiser.
		int idAleatoire = random.nextInt(this.grille.getTaille());
		int idAleatoire2 = random.nextInt(this.grille.getTaille());//On prend un nombre aleatoire entre 0 et la taille-1 de la liste des personnages.
		while(idAleatoire==idAleatoire2){
			idAleatoire2 = random.nextInt(this.grille.getTaille());
		}
		persoCache2 = this.listePersonnage.get(idAleatoire2);
		persoCache = this.listePersonnage.get(idAleatoire);
	}
	
	public boolean questionPersosCaches(HashMap<String, ArrayList<String>> attsEtArgs, Boolean andorOr, String constructeur) {
		boolean test = true;
		boolean rep1 = this.persoCache.verifierArguments(attsEtArgs, andorOr);
		boolean rep2 = this.persoCache2.verifierArguments(attsEtArgs, andorOr);
		
		if(constructeur=="Les 2") {
			test = rep1 && rep2;
		}else if(constructeur=="1 des 2") {
			test = rep1 || rep2;
		}else{
			test = !rep1 && !rep2;
		}
		
		return test;
	}
	
	@SuppressWarnings("unchecked")
	public void sauvegarder() {
		JSONObject sauvegarde = new JSONObject();
		sauvegarde.put("nom",this.save.getNom());
		sauvegarde.put("etat", this.statut.toString());
		sauvegarde.put("grille", this.grille.getNom());
		sauvegarde.put("persocache", this.persoCache.getId());
		sauvegarde.put("bannis", this.save.getBannisJSON());
		sauvegarde.put("persocache2", this.persoCache2.getId());
		try {
			File file= new File("../qui-est-ce/Sauvegardes/"+this.save.getNom()+".json");
			file.delete();
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Sauvegardes/"+this.save.getNom()+".json");
			fileWriter.write(sauvegarde.toJSONString());
			fileWriter.close();
		}catch(IOException e) {
			
		}
	}
	
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
		this.persoCache2 = this.listePersonnage.get(Integer.parseInt((String)loadSave.get("persocache2")));
		this.persoCache = this.listePersonnage.get(Integer.parseInt((String)loadSave.get("persocache")));
		nomSauvegarde = nomSauvegarde.substring(0, nomSauvegarde.length() - 5);
		this.save = new Sauvegarde(nomSauvegarde);
		save.ajoutBannis(listBannis);
		System.out.println(save.getNom());	
	}
	
}
