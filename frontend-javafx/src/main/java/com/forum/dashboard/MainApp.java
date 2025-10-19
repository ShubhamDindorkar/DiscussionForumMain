package com.forum.dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        MainApp.primaryStage = primaryStage;
        // Start with student sign-in screen by default
        showStudentSignIn();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    // Show Student Sign-In
    public static void showStudentSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/student_signin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(MainApp.class.getResource("/style.css").toExternalForm());
            primaryStage.setTitle("Discussion Forum - Student Sign In");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Show Admin Sign-In
    public static void showAdminSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/admin_signin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(MainApp.class.getResource("/style.css").toExternalForm());
            primaryStage.setTitle("Discussion Forum - Admin Sign In");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Show Student Dashboard (after successful student login)
    public static void showStudentDashboard(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/student_dashboard.fxml"));
            Parent root = loader.load();
            
            // Pass username to controller
            StudentDashboardController controller = loader.getController();
            controller.setUsername(username);
            
            Scene scene = new Scene(root, 1400, 900);
            scene.getStylesheets().add(MainApp.class.getResource("/style.css").toExternalForm());
            primaryStage.setTitle("Discussion Forum - Student Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Show Admin Dashboard (after successful admin login)
    public static void showAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/admin_dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1400, 900);
            scene.getStylesheets().add(MainApp.class.getResource("/style.css").toExternalForm());
            primaryStage.setTitle("Discussion Forum - Admin Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Legacy method for backward compatibility
    @Deprecated
    public static void showMainDashboard() throws IOException {
        showAdminDashboard();
    }
}
