package org.example.linkedinclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stg;
    private static Scene Login;
    public static User LoggedInUser;
    public static String token;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        stg = stage;
        stage.setResizable(false);
        Login = new Scene((new FXMLLoader(HelloApplication.class.getResource("login.fxml"))).load(), 700, 500);
        stage.setTitle("LinkedIn");
        Image icon = new Image("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\img\\logo.png");
        stage.getIcons().add(icon);
        stg.setScene(Login);
        stage.show();
    }
    public void changeScene(int x) throws IOException {
        switch (x) {
            case 1:
                stg.setScene(Login);
                break;
            case 2:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("signIn.fxml"))).load()));
                break;
            case 3:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("LinkedInFeed.fxml"))).load(), 1000, 600));
                break;
            case 4:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("commentSection.fxml"))).load(), 1000, 800));
                break;
            case 5:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("profileView.fxml"))).load(), 800, 1000));
                break;
            case 6:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("educationsView.fxml"))).load(), 600, 1000));
                break;
            case 7:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("educationComponent.fxml"))).load(), 600, 1000));
                break;
            case 8:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("sumPage.fxml"))).load(), 600, 500));
                break;
            case 9:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("skillPage.fxml"))).load(), 432, 602));
                break;
            case 10:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("skillComponent.fxml"))).load(), 400, 300));
                break;
            case 11:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("followersPage.fxml"))).load(), 600, 700));
                break;
            case 12:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("followingsPage.fxml"))).load(), 600, 700));
                break;
            case 13:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("UserProfilePageView.fxml"))).load(), 800, 1000));
                break;
            case 14:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("userFollowersView.fxml"))).load(), 600, 700));
                break;
            case 15:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("userFollowingsView.fxml"))).load(), 600, 700));
                break;
            case 16:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("userEducationView.fxml"))).load(), 600, 1000));
                break;
            case 17:
                stg.setScene(new Scene((new FXMLLoader(HelloApplication.class.getResource("userSkillView.fxml"))).load(), 432, 602));
                break;


        }

    }

    public static void main(String[] args) {
        launch();
    }
}