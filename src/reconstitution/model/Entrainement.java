package reconstitution.model;

import java.util.ArrayList;
import java.util.List;

public class Entrainement extends Exercice {

    private boolean motIncomplet;
    private int minLettre;
    private boolean afficherRatio;
    private boolean afficherSolution;

    public Entrainement() {
        super();
    }

    public boolean isAfficherRatio() {
        return afficherRatio;
    }

    public void setAfficherRatio(boolean afficherRatio) {
        this.afficherRatio = afficherRatio;
    }

    public boolean isAfficherSolution() {
        return afficherSolution;
    }

    public void setAfficherSolution(boolean afficherSolution) {
        this.afficherSolution = afficherSolution;
    }

    @Override
    public List<Integer> proposer(String proposition) {
        List<Integer> containCorrect = new ArrayList<>();

        for (String prop : proposition.split("[ '-,.;:]")) {
            for (int i = 0; i < getMots().length; i++) {
                boolean isCorrect;

                if (!getMotsOccult()[i].matches(".*[" + getOccultation() + "]+")) {
                    continue;
                } else if (isCaseSensi()) {
                    isCorrect = getMots()[i].equals(prop);
                } else {
                    isCorrect = getMots()[i].equalsIgnoreCase(prop);
                }

                if (isCorrect) {
                    getMotsOccult()[i] = getMots()[i];
                    addNbNombreDecouv();
                    containCorrect.add(i);
                } else {
                    if (motIncomplet && prop.length() >= minLettre) {
                        if (getMots()[i].startsWith(prop)) {
                            String word = getMots()[i].substring(prop.length());
                            getMotsOccult()[i] = prop + word.replaceAll(".", getOccultation());
                            containCorrect.add(i);
                        }
                    }
                }
            }
        }

        return containCorrect;
    }

    public boolean isMotIncomplet() {
        return motIncomplet;
    }

    public void setMotIncomplet(boolean motIncomplet) {
        this.motIncomplet = motIncomplet;
    }

    public int getMinLettre() {
        return minLettre;
    }

    public void setMinLettre(int minLettre) {
        this.minLettre = minLettre;
    }
}
