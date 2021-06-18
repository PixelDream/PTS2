package reconstitution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Enseignant extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void changeScene(String fxml) throws IOException {
        Stage primaryStage = stage;
        primaryStage.getIcons().addAll(
                new Image(String.valueOf(Enseignant.class.getResource("/favicon/64x64.png"))),
                new Image(String.valueOf(Enseignant.class.getResource("/favicon/32x32.png"))),
                new Image(String.valueOf(Enseignant.class.getResource("/favicon/16x16.png")))
        );
        primaryStage.setMinWidth(1080);
        primaryStage.setMinHeight(720);
        primaryStage.setMaximized(true);

        Parent root = FXMLLoader.load(Enseignant.class.getResource(fxml));
        try {
            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            primaryStage.setScene(new Scene(root));
        }
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        changeScene("/enseignantAccueil.fxml");
    }


}
