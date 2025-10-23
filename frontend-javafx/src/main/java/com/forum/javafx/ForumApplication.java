package com.forum.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.forum.javafx.view.MainView;

public class ForumApplication extends Application {

    private static final String APP_TITLE = "Discussion Forum";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        try {
            MainView mainView = new MainView();
            Scene scene = new Scene(mainView, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // Load CSS
            scene.getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm()
            );
            
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            
            // Set application icon (optional)
            try {
                primaryStage.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/icon.png"))
                );
            } catch (Exception e) {
                // Icon not found, continue without it
            }
            
            primaryStage.show();
            
            // Apply fade-in animation
            mainView.playEntranceAnimation();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
