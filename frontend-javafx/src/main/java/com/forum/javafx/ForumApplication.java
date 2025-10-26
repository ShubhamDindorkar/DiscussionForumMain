package com.forum.javafx;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.forum.javafx.view.LoginView;
import com.forum.javafx.view.DashboardView;
import com.forum.javafx.service.AuthService;

public class ForumApplication extends Application {

    private static final String APP_TITLE = "Discussion Forum";
    // Default fallback size; we'll expand to the primary screen's visual bounds or maximize.
    private static final int WINDOW_WIDTH = 1366;
    private static final int WINDOW_HEIGHT = 768;
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

            // Fit the app to the default screen resolution
            try {
                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(bounds.getWidth());
                primaryStage.setHeight(bounds.getHeight());
                // Also maximize to ensure full-screen fit across platforms
                primaryStage.setMaximized(true);
            } catch (Exception ignore) {
                // If Screen info isn't available, the fallback WINDOW_WIDTH/HEIGHT will be used
            }
            
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
