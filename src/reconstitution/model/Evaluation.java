package reconstitution.model;

public class Evaluation extends Exercice {

    private boolean limite;
    private int limiteTemps;
    private String finTemps;
    private EtudiantData etudiant;

    public Evaluation() {
        super();
    }

    public int getLimiteTemps() {
        return limiteTemps;
    }

    public void setLimiteTemps(int limiteTemps) {
        this.limiteTemps = limiteTemps;
    }

    public String getFinTemps() {
        return finTemps;
    }

    public void setFinTemps(String finTemps) {
        this.finTemps = finTemps;
    }

    public boolean isLimite() {
        return limite;
    }

    public void setLimite(boolean limite) {
        this.limite = limite;
    }

    public EtudiantData getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantData etudiant) {
        this.etudiant = etudiant;
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
                }
            }
        }

        return containCorrect;
    }
}
