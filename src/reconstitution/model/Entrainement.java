package reconstitution.model;

public class Entrainement extends Exercice {

    private boolean motIncomplet;
    private int minLettre;
    private boolean afficherRatio;
    private boolean afficherSolution;

    public Entrainement() {
        super();
    }

    public void setMotIncomplet(boolean motIncomplet) {
        this.motIncomplet = motIncomplet;
    }

    public void setMinLettre(int minLettre) {
        this.minLettre = minLettre;
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
    public boolean proposer(String proposition) {
        boolean containCorrect = false;

        for (String prop : proposition.split("[ '-,.;:]")) {
            for (int i = 0; i < getMots().length; i++) {
                boolean isCorrect;

                if (!getMotsOccult()[i].matches("[" + getOccultation() + "]+")) {
                    continue;
                } else if (isCaseSensi()) {
                    isCorrect = getMots()[i].equals(prop);
                } else {
                    isCorrect = getMots()[i].equalsIgnoreCase(prop);
                }

                if (isCorrect) {
                    getMotsOccult()[i] = getMots()[i];
                    addNbNombreDecouv();
                    containCorrect = true;
                } else {
                    if (motIncomplet && prop.length() >= minLettre) {
                        if (getMots()[i].startsWith(prop)) {
                            getMotsOccult()[i] = getMots()[i];
                            addNbNombreDecouv();
                            containCorrect = true;
                        }
                    }
                }
            }
        }

        return containCorrect;
    }
}
