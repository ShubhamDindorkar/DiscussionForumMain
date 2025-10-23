package com.forum.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.forum.javafx.view.LoginView;
import com.forum.javafx.view.DashboardView;
import com.forum.javafx.service.AuthService;

public class ForumApplication extends Application {

    private static final String APP_TITLE = "Discussion Forum";
    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 850;
    private static final int MIN_WIDTH = 1100;
    private static final int MIN_HEIGHT = 700;
    
    private Stage primaryStage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        try {
            // Start with login view
            LoginView loginView = new LoginView();
            loginView.setOnLoginSuccess(this::showDashboard);
            
            scene = new Scene(loginView, WINDOW_WIDTH, WINDOW_HEIGHT);
            
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showDashboard() {
        DashboardView dashboard = new DashboardView();
        dashboard.setOnLogout(this::showLogin);
        scene.setRoot(dashboard);
    }
    
    private void showLogin() {
        AuthService.getInstance().logout();
        LoginView loginView = new LoginView();
        loginView.setOnLoginSuccess(this::showDashboard);
        scene.setRoot(loginView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
