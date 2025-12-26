package com.bibliotheque;

import com.bibliotheque.controller.LivreController;
import com.bibliotheque.service.BookServiceImpl;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_book.fxml"));
        // simple controller factory to inject service instances
        loader.setControllerFactory(clazz -> {
            if (clazz == LivreController.class) {
                return new LivreController(new BookServiceImpl());
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        primaryStage.setTitle("Bibliotheque - Ajouter un Livre");
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
    }
}