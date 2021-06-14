package reconstitution.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import reconstitution.Enseignant;
import reconstitution.utils.PopUp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Exercice implements Serializable {
    private String consigne;
    private String aide;
    private String occultation;
    private boolean caseSensi;
    private String[] textOccult;
    private String[] mots;
    private int nbNombreDecouv;
    private MediaExercice mediaExercice;

    public static Exercice Importer() {
        ObjectInputStream ois = null;

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Exercice", "*.exo"));
            fileChooser.setTitle("Choisir un exercice");
            File selectedFile = fileChooser.showOpenDialog(Enseignant.getStage());

            if (selectedFile == null) return null;

            try {
                final FileInputStream fichier = new FileInputStream(selectedFile.getPath());

                ois = new ObjectInputStream(fichier);
                final Object obj = ois.readObject();

                if (obj instanceof Evaluation) {
                    if (((Evaluation) obj).getEtudiant() != null)
                        new PopUp(Alert.AlertType.ERROR, "Fichier Examen", "Ce fichier est un examen déjà terminé et non un exercice.");
                } else if (obj instanceof Entrainement) {

                } else {
                    new PopUp(Alert.AlertType.ERROR, "Fichier inconnu", "Ce fichier ne peut être pris en charge par l'application.");
                }

                return (Exercice) obj;
            } catch (final java.io.IOException e) {
                new PopUp(Alert.AlertType.ERROR, "Fichier inconnu", "Ce fichier ne peut être pris en charge par l'application.", selectedFile.getAbsolutePath());
            } catch (final ClassNotFoundException e) {
                new PopUp(Alert.AlertType.ERROR, "Fichier inconnu", "Ce fichier est corrompu, il a été modifié ou il s'agit d'un encien exercice plus compatible.", selectedFile.getAbsolutePath());
            } finally {
                try {
                    if (ois != null) ois.close();
                } catch (final IOException ex) {
                    new PopUp(Alert.AlertType.ERROR, "Une erreur s'est produite", "L'application rencontre une erreur interne.");
                }
            }

        } catch (IllegalArgumentException iae) {
            new PopUp(Alert.AlertType.ERROR, "Aucun fichier", "Aucun fichier n'a été ouvert... Merci de réessayer.");
        }

        return null;
    }

    public static List<Evaluation> ImporterMult() {
        ObjectInputStream ois = null;
        List<Evaluation> exerciceList = new ArrayList<>();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Exercice", "*.exo"));
            fileChooser.setTitle("Choisir un exercice");
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(Enseignant.getStage());

            if (selectedFiles == null) return exerciceList;

            for (File file : selectedFiles) {
                if (file == null) return null;

                try {
                    final FileInputStream fichier = new FileInputStream(file.getPath());

                    ois = new ObjectInputStream(fichier);
                    final Object obj = ois.readObject();

                    if (obj instanceof Evaluation) {
                        Evaluation evaluation = (Evaluation) obj;
                        if (evaluation.getEtudiant() != null) exerciceList.add(evaluation);
                    } else if (obj instanceof Entrainement) {

                    } else {
                        new PopUp(Alert.AlertType.ERROR, "Une erreur s'est produite", "L'application rencontre une erreur interne.");
                    }

                } catch (final java.io.IOException e) {
                    new PopUp(Alert.AlertType.ERROR, "Fichier inconnu", "Ce fichier ne peut être pris en charge par l'application.", file.getAbsolutePath());
                } catch (final ClassNotFoundException e) {
                    new PopUp(Alert.AlertType.ERROR, "Fichier inconnu", "Ce fichier est corrompu, il a été modifié ou il s'agit d'un encien exercice plus compatible.", file.getAbsolutePath());
                } finally {
                    try {
                        if (ois != null) ois.close();
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            return exerciceList;
        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        }

        return null;
    }

    public boolean Exporter() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Exercice", "*.exo"));
        fileChooser.setTitle("Enregistrer un exercice");

        if (this instanceof Evaluation && ((Evaluation) this).getEtudiant() != null) {
            fileChooser.setInitialFileName(((Evaluation) this).getEtudiant().getPrenom() + "_" + ((Evaluation) this).getEtudiant().getNom() + "_exercice.exo");
        } else {
            fileChooser.setInitialFileName("exercice.exo");
        }

        File selectedFile = fileChooser.showSaveDialog(Enseignant.getStage());

        try {
            if (selectedFile != null) {
                ObjectOutputStream oos = null;

                try {
                    final FileOutputStream fichier = new FileOutputStream(selectedFile.getPath());
                    oos = new ObjectOutputStream(fichier);
                    oos.writeObject(this);
                    oos.flush();
                } catch (final java.io.IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (oos != null) {
                            oos.flush();
                            oos.close();
                        }
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        return false;
    }

    public String getConsigne() {
        return consigne;
    }

    public void setConsigne(String consigne) {
        this.consigne = consigne;
    }

    public void setTexte(String texte) {
        String txt = texte.trim()
                .replaceAll("[ ]*[.][ ]*", " . ")
                .replaceAll("[ ]*[?][ ]*", " ? ")
                .replaceAll("[ ]*[,][ ]*", " , ")
                .replaceAll("[ ]*[;][ ]*", " ; ")
                .replaceAll("[ ]*[\\[]][ ]*", " [ ")
                .replaceAll("[ ]*[\\]][ ]*", " ] ")
                .replaceAll("[ ]*[(][ ]*", " [ ")
                .replaceAll("[ ]*[)][ ]*", " ] ")
                .replaceAll("[ ]*[$][ ]*", " $ ")
                .replaceAll("[ ]*[£][ ]*", " £ ")
                .replaceAll("[ ]*[€][ ]*", " € ")
                .replaceAll("[ ]*['][ ]*", " ' ")
                .replaceAll("[ ]*[\"][ ]*", " \" ")
                .replaceAll("[ ]*[/][ ]*", " / ")
                .replaceAll("[ ]*[!][ ]*", " ! ")
                .replaceAll("[ ]*[-][ ]*", " - ");
        mots = txt.split(" ");
        textOccult = txt.replaceAll("[a-zA-Z0-9]", occultation).split(" ");
    }

    public String getAide() {
        return aide;
    }

    public void setAide(String aide) {
        this.aide = aide;
    }

    public String getOccultation() {
        return occultation;
    }

    public void setOccultation(String occultation) {
        this.occultation = occultation;
    }

    public boolean isCaseSensi() {
        return caseSensi;
    }

    public void setCaseSensi(boolean caseSensi) {
        this.caseSensi = caseSensi;
    }

    public String[] getTextOccult() {
        return textOccult;
    }

    public String getTextOccultJoined() {
        return String.join(" ", textOccult);
    }

    public ObservableList<Text> getTextOccultAsList() {
        ObservableList<Text> textOccultList = FXCollections.observableArrayList();
        AtomicInteger acc = new AtomicInteger();

        Arrays.asList(textOccult).forEach(el -> {
            if (acc.get() == 0 || acc.get() == textOccult.length - 1 || textOccult[textOccult.length < (acc.get() + 1) ? acc.get() + 1 : acc.get()].matches("[.?,.$£€!]")) {
                textOccultList.add(getTextTransformed(el + " "));
            } else if (textOccult[textOccult.length < (acc.get() + 1) ? acc.get() + 1 : acc.get()].matches("['-]")) {
                textOccultList.add(getTextTransformed(el));
            } else if (textOccult[textOccult.length < (acc.get() + 1) ? acc.get() + 1 : acc.get()].matches("[()\\[]\\/]")) {
                textOccultList.add(getTextTransformed(" " + el));
            } else {
                textOccultList.add(getTextTransformed(" " + el + " "));
            }
            acc.getAndIncrement();
        });

        return textOccultList;
    }

    private Text getTextTransformed(String str) {
        Text text = new Text(str);
        text.setStyle("-fx-font-size: 15.0;");

        return text;
    }

    public String[] getMotsOccult() {
        return textOccult;
    }

    public int getNbNombreDecouv() {
        return nbNombreDecouv;
    }

    public void addNbNombreDecouv() {
        this.nbNombreDecouv += 1;
    }

    public MediaExercice getMedia() {
        return mediaExercice;
    }

    public void setMedia(MediaExercice mediaExercice) {
        this.mediaExercice = mediaExercice;
    }

    public String[] getMots() {
        return mots;
    }

    public abstract boolean proposer(String proposition);
}
