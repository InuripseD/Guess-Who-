package quiestce.associations;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Personnage {

    private String id;			//Le numero du personnage (id) dans le .json (On utilise le String car c'est plus simple)
    private Grille grille;		//La grille a laquel ce personnage appartient
    private HashMap<String,ArrayList<String>> caracteristiques= new HashMap<String,ArrayList<String>>();	//Pour chaque attributs que possede ce personnage sa liste d'arguments.
    
    public Personnage(String id) {
    	this.id = id;
    }
    
    /**
     * L'appel au constructeur de Personnage a lieu lors de la creation d'un game :
     * 			-Pour creer le personnage cache.
     * 			-Pour creer la liste des personnage de la grille.
     * En theorie l'apelle au constructeur n'a lieu que dans les classes implements by IGame.
     * 
     * @param id Permet d'obtenir le personnage du JSONArray de la grille a laquel il appartient.
     * @param grille Permet de savoir dans quel grille ce personnage appartient.
     */
    public Personnage(String id, Grille grille) {
        this.grille = grille;					//On assigne une grille a notre personnage.
    	this.id=id;								//On lui remet son ID.
    	this.setCaracteristiques();				//On remplie sa liste d'attributs/arguments.
    }
    
    /*--------------------------GETTERS AND SETTERS--------------------------*/
    
    /**
     * @return juste l'id du personnage.
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return juste l'hashmap contenant pour chaque attributs les arguments du personnage.
     */
    public HashMap<String,ArrayList<String>> getCaracteristique(){
    	return this.caracteristiques;
    }
    
    /**
     * (Peut etre) Utile si on veut juste la photo d'un personnage.
     * @return l'adresse url/nom de la photo du personnage en question.
     */
    public String getPhoto() {
    	return this.caracteristiques.get("photo").get(0);
    }
    
    /*--------------------------Utilitaire pour personnage--------------------------*/
    
    /**
     * Cette methode est appele lorsque l'on souhaite savoir si un tel personnage possede
     * des tels arguments pour de tels attributs.
     * 
     * @param attsEtArgs qui est une HashMap de type <Attribut,ArrayList<Argument>>
     * 			C'est en quelque sort la question qui est pose : 
     * 				ex:"Le perso a-t-il pour cet attribut des tels arguments ?".
     *  
     * @param andORor est le constructeur de la question, car une question peut etre simple,
     * 			c'est a dire on demande pour un seul attribut s'il possede un seul argument.
     * 			Ou on peut demander si pour plusieurs attributs, il possede un tel ou de tels arguments.
     * 			Quand anORor est :
     * 					-true : alors ou veut possede la question avec des "OU", c'est a dire :
     * 								ex:"Notre perso au moins cet argument pour cet attribut @OU
     * 									au moins cet argument pour cet attribut etcs...".
     * 							C'est pour quoi @possede devient false car au moindre att/arg true
     * 							on change @possede de false => true et on @return true !
     * 					-false : alors ou veut possede la question avec des "ET", c'est a dire :
     * 								ex:"Notre perso a obligatoirement cet argument pour cet attribut @ET
     * 									a obligatoirement cet argument pour cet attribut etcs...".
     * 							C'est pour quoi @possede devient true car au moindre att/arg false
     * 							on change @possede de true => false et on @return false !
     * 					-cas avec une seul question : que ce soit @ET ou @OU la reponse 
     * 							@return true ou false correctment. (voir cours logique)
     * 
     * @return voir selon parametre !
     */
    public boolean verifierArguments(HashMap<String, ArrayList<String>> attsEtArgs, Boolean andORor) {
    	boolean possede = !andORor;//On initialise la valeur boolean de retour.
		for(Object att : attsEtArgs.keySet()) {//Pour chaque attribut de la question.
			for(Object arg : ((ArrayList<String>)(attsEtArgs.get(att)))) {//Et pour chaque argument pour cette attribut dans la question.
				if(andORor) {//si on travail sur le ou alors on prend premiere maniere sinon deuxieme.
					possede =  possede || this.getCaracteristique().get(att).contains((String)arg);//On verifie que au moins un argument que l'on demande et contenue dans la liste d'arguments d'au moins un attribut pour le personnage demande.
				}else {
					possede =  possede && this.getCaracteristique().get(att).contains((String)arg);//On verifie que tout les arguments que l'on demande soient contenuent dans la liste d'argument de chaque attribut demande pour le personnage demande.
				}
			}
		}
		return possede;//Voir @return ! 
    }

    /**
     * Methode appele lors de la construction du personnage.
     * Elle va chercher dans la grille a laquelle le personnage appartient,
     * ses attributs et ses arguments pour les mettre dans sa liste de caracteristiques.
     */
    private void setCaracteristiques() {
    	JSONObject personnage = this.grille.getPerso(this.getId());//On demande le JSONObject correspondant a l'id du personnage que l'on cree dans la grille a laquelle il appartient.
    	for(Object attribut : personnage.keySet()){//Pointeur qui va de clef en clef/attributs en attributs dans le perso choisi.
			ArrayList<String> arguments = new ArrayList<String>();//Cree la liste des arguments qu'il n'y en ait un ou plusieur on met dans une arraylist!
			if(personnage.get(attribut) instanceof JSONArray) {	//On verifie si c'est une list d'agument ou un simple argument, car sinon cause des erreurs.
				for(Object arg : (JSONArray)personnage.get(attribut)) {//Pointeur qui va d'argument en argument de cet attribut. (il y a souvent qu'un argument)
					arguments.add(""+arg);//On ajoute les arguments de la liste d'arguments un par un.
				}
			}else {
				arguments.add(""+personnage.get(attribut));//On ajoute le seul argument a larraylist.
			}
			this.caracteristiques.put((String)attribut, arguments);//On met dans l'hashmap des caracteristiques, l'attribut avec son Arraylist d'argument.
		}

	}
    
    public void setCaracteristique(String att, ArrayList<String> caracteristique) {
        this.caracteristiques.put(att, caracteristique);
    }
    
    public void removeAtt(String attmoins) {
        this.caracteristiques.remove(attmoins);
    }
    
    public void removeArg(String att, String argmoins) {
        this.caracteristiques.get(att).remove(argmoins);
    }

}
