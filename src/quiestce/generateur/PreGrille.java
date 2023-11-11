package quiestce.generateur;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import quiestce.associations.Grille;
import quiestce.associations.Personnage;

public class PreGrille extends Grille{
	
	public static int TOTAL = 0;
	public int compteur = 0;
	private ArrayList<Personnage> persos = new ArrayList<Personnage>();
	
	public PreGrille(String filename) {
		super(filename);
		this.setJSONArrayPerso();//On met en memoire la liste des Personnage dans une JSONArray.
		this.taille = grille.size();//On prend la taille de la la JSONArray.
		this.setAttributsEtArguments();
		this.setListePerso();
	}
	
	public PreGrille(String nom, boolean b) {
		super(nom, b);	
	}
	
	private void setListePerso() {
		for(int i = 0 ; i < this.getTaille() ; i++) {//Pour i de 0 a la taille de la liste des perso du fichier .json.
			this.persos.add(new Personnage(""+i,this));//On cree un nouveau perso pour chaque id de la liste.
		}
	}
	
	public Personnage getPersoUnique(String id) {
		return this.persos.get(Integer.parseInt(id));
	}
	
	public ArrayList<Personnage> getPersos(){
		return this.persos;
	}
	
	public void ajouterPerso(String photo, String id) {
		this.persos.add(new Personnage(id));
		ArrayList<String> photoacutel = new ArrayList<String>();
		photoacutel.add(photo);
		this.persos.get(compteur).setCaracteristique("photo", photoacutel);
		ArrayList<String> idacutel = new ArrayList<String>();
		idacutel.add(id);
		this.persos.get(compteur).setCaracteristique("id", idacutel);
		this.compteur+=1;
	}
	
	public int getCompteur() {
		return this.compteur;
	}
	
	public void addAttributs(String att) {
		if(!this.getAttList().contains(att)) {
			ArrayList<String> args = new ArrayList<String>();
			this.setAttsEtArgs(att, args);
			for(Personnage p : persos) {
				ArrayList<String> argperso = new ArrayList<String>();
				p.setCaracteristique(att, argperso);
			}
		}
	}

	public void deleteAttribut(String att) {
		if(this.getAttList().contains(att)) {
			this.removeAtt(att);
		}
	}
	
	public void addArgument(String att, String arg) {
		if(!this.getArgsList(att).contains(arg)) {
			ArrayList<String> argperso = new ArrayList<String>();
			argperso.addAll(this.getArgsList(att));
			argperso.add(arg);
			this.setAttsEtArgs(att,argperso);
		}
	}
	
	public void deleteArgument(String att, String arg) {
		this.AttributsEtArguments.get(att).remove(arg);
	}
	
	public void updatePerso(String id, String att, ArrayList<String> args) {
		int idint = Integer.parseInt(id);
		this.persos.get(idint).setCaracteristique(att, args);
	}
	
	public ArrayList<String> getAttList(){
		ArrayList<String> atts = new ArrayList<>();
		for(String att : this.getAttsEtArgs().keySet()) {
			atts.add(att);
		}
		return atts;
	}
	
	public ArrayList<String> getArgsList(String att){
		return this.getAttsEtArgs().get(att);
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getPersosJSON() {
		JSONArray JSONPerso = new JSONArray();
		for(Personnage perso : this.persos) {
			JSONObject persoObject = new JSONObject();
			for(String att : perso.getCaracteristique().keySet()) {
				JSONArray argsArray = new JSONArray();
				if(perso.getCaracteristique().get(att).size()==1) {
					String argUnique = perso.getCaracteristique().get(att).get(0);
					persoObject.put(att, argUnique);
				}else {
					for(String arg : perso.getCaracteristique().get(att)) {
						argsArray.add(arg);
					}
					persoObject.put(att, argsArray);
				}
			}
			JSONPerso.add(persoObject);
		}
		return JSONPerso;
	}
	
	@SuppressWarnings("unchecked")
	public void creerJSON() {
		JSONObject personnages = new JSONObject();
		personnages.put("personnages", this.getPersosJSON());
		try {
			FileWriter fileWriter = new FileWriter("../qui-est-ce/Grilles/"+this.getNom()+"/"+this.getNom()+".json");
			fileWriter.write(personnages.toJSONString());
			fileWriter.close();
		}catch(IOException e) {
			
		}
	}

	public void setNom(String text) {
		this.nom = text;
	}
	
}
