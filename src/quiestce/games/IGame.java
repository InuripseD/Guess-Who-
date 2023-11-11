package quiestce.games;

import java.util.ArrayList;
import java.util.HashMap;

import quiestce.associations.Personnage;

public interface IGame {
	
	/**
	 * Cette fonction est utilisé dans toutes les classes de jeux !
	 * Elle prend en parametre la liste d'attribut et d'argument selectionné par
	 * le joueur puis ouvre le JSON pour voir si il(persocaché) possede cet/ces argument
	 * sur cet/ces attribut et @return vrai si le perso caché possede cet 
	 * caracteristique et faux sinon.
	 * Elle est executé lorsque le joueur appuie sur le boutton valider
	 * et affiche le resultat à l'écrans pour que le joueur puisse
	 * barrer des perso en fonction de la réponse.
	 */
	/**
	 * On va verifier que l'argument de l'attribut que l'on a demande et bien identique a celui de notre persocache.
	 * Pour cela on prend la clef/attribut que l'on souhaite verifier grace au 3 premieres ligne att1 est l'attribut,
	 * puis on @return le resultat de la comparaison de l'argument de notre persoCache et de celui demande.
	 */
	boolean verifArgument(HashMap<String, ArrayList<String>> attsEtArgs, Boolean andorOr);
	/**
	 * Permet lors du click pour activer le mode cheat d'activer celui ci,
	 * cela impactera les actions qui seront réalisé lors de la validation 
	 * de la focntion obtenirInfo() @return dans le chat "mode cheat activé" !
	 */
	public abstract void changerModeTriche();

	/**
	 * Cette fonction est appelé lorsque la partie est terminé !
	 * Elle met l'etat actuel de la partie en 'terminé'.
	 */
	public abstract void finirLaPartie();

	/**
	 * Elle prend aléatoirement un personnage dans le fichier JSON que l'on souhaite
	 * utiliser.
	 * @return l'id du personnage que l'utilisateur va devoir trouver.
	 */
	public abstract Personnage cacherPersonnage();

	void sauvegarder();

	/**
	 * Prend en @param bans la liste des persos qui sont coches sur la GUI et les ajoutes les ids a la liste
	 * des persos bannis dans notre save.
	 */
	void bannir(ArrayList<String> bans);

	
	

}
