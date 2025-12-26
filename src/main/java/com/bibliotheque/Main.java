package com.bibliotheque;

import com.bibliotheque.controller.BookController;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        // controller factory injects BibliothequeService into LivreController
        loader.setControllerFactory(clazz -> {
            try {
                if (clazz == com.bibliotheque.controller.LivreController.class) {
                    com.bibliotheque.infra.DatabaseConnection db = com.bibliotheque.infra.DatabaseConnection.getInstance();
                    com.bibliotheque.dao.LivreDAO livreDAO = new com.bibliotheque.dao.LivreDAOImpl(db);
                    com.bibliotheque.service.BibliothequeService svc = new com.bibliotheque.service.BibliothequeServiceImpl(livreDAO);
                    return clazz.getDeclaredConstructor(com.bibliotheque.service.BibliothequeService.class).newInstance(svc);
                }
                if (clazz == BookController.class) {
                    return new BookController(new BookServiceImpl());
                }
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        primaryStage.setTitle("Bibliotheque - Home");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
}