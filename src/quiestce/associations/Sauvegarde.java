package quiestce.associations;

import java.util.ArrayList;

import org.json.simple.JSONArray;


public class Sauvegarde {
	
	private String nom;					//Nom du dossier .json de la sauvegarde de la partie.
	private ArrayList<String> bannis;	//La liste des ID des bannis.
	
	/**
	 * La classe Sauvegarde sert pour le moment uniquement a gerer la liste des
	 * personnages bannis.
	 * 
	 * Le @param nom est le nom du dossier .json contenant le reel sauvegarde physique sur
	 * disque dur. (Pour le moment la date a laquel est effectue la sauvegarde)
	 */
	public Sauvegarde(String nom) {
		this.nom = nom;
		this.bannis = new ArrayList<String>();
	}
	
	/**
	 * Prend la liste des ids @param bans qui ont ete clique pour les ajouter a la liste des personnages bannis (seulement l'id).
	 */
	public void ajoutBannis(ArrayList<String> bans) {
		this.bannis.addAll(bans);
	}
	
	public String getNom() {
		return this.nom;
	}

	/**
	 * Utile pour la methode sauvegarder de GameSolo.
	 * @return Un liste JSON des personnages actuellement bannis.
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getBannisJSON() {
		JSONArray bannis = new JSONArray();
		for(String banni : this.bannis) {
			bannis.add(banni);
		}
		return bannis;
	}
	
	/**
	 * @return la liste des bannis en String utile pour le controlleur.
	 */
	public ArrayList<String> getBannis() {
		return this.bannis;
	}

}
