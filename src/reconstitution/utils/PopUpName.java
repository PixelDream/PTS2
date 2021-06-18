package reconstitution.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class PopUpName extends Dialog<Optional<Pair<String, String>>> {
    private final Optional<Optional<Pair<String, String>>> result;

    public PopUpName() {
        this.setTitle("Vos informations");
        this.setHeaderText("Quelle est votre identitée");

        ButtonType loginButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField prenom = new TextField();
        prenom.setPromptText("Prénom");
        TextField nom = new TextField();
        nom.setPromptText("Nom");

        grid.add(new Label("Prénom:"), 0, 0);
        grid.add(prenom, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nom, 1, 1);

        Node btnEnregistrer = this.getDialogPane().lookupButton(loginButtonType);
        btnEnregistrer.setDisable(true);

        prenom.textProperty().addListener((observable, oldValue, newValue) -> {
            btnEnregistrer.setDisable(newValue.trim().isEmpty());
        });

        this.getDialogPane().setContent(grid);
        Platform.runLater(prenom::requestFocus);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return Optional.of(new Pair<>(prenom.getText(), nom.getText()));
            }
            return Optional.empty();
        });

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().addAll(
                new Image(String.valueOf(getClass().getResource("/favicon/64x64.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/32x32.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/16x16.png")))
        );

        this.result = this.showAndWait();
    }

    public Optional<Pair<String, String>> getDataResult() {
        return result.get();
    }

}
