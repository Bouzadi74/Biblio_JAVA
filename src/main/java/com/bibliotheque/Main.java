package com.bibliotheque;

import com.bibliotheque.controller.LivreController;
import com.bibliotheque.service.BibliothequeService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LivreView.fxml"));

        // Factory pour injecter le service métier
        loader.setControllerFactory(clazz -> {
            if (clazz == LivreController.class) {
                return new LivreController(new BibliothequeService());
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        primaryStage.setTitle("Gestion de Bibliothèque");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }
}