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
<<<<<<< HEAD
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LivreView.fxml"));

        // Factory pour injecter le service métier
        loader.setControllerFactory(clazz -> {
            if (clazz == LivreController.class) {
                return new LivreController(new BibliothequeService());
            }
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        // controller factory injects BibliothequeService into LivreController
        loader.setControllerFactory(clazz -> {
>>>>>>> e014484e0ecce728e18711c7d7edda1ec5b547bb
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
<<<<<<< HEAD
        primaryStage.setTitle("Gestion de Bibliothèque");
        primaryStage.setScene(new Scene(root, 900, 600));
=======
        primaryStage.setTitle("Bibliotheque - Home");
        primaryStage.setScene(new Scene(root, 1000, 600));
>>>>>>> e014484e0ecce728e18711c7d7edda1ec5b547bb
        primaryStage.show();
    }
}