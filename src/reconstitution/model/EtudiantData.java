package reconstitution.model;

import java.io.Serializable;

public class EtudiantData implements Serializable {
    private final String nom;
    private final String prenom;

    public EtudiantData(String prenom, String nom) {
        this.prenom = prenom;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
