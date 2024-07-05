package org.example.linkedinclient;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.example.linkedinclient.FeedController.toStringArray;
import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class UserProfilePageController {
    @FXML
    private FontAwesomeIconView closeBtn;

    @FXML
    private VBox educationVBox;

    @FXML
    private Label emailLabel;

    @FXML
    private Button followBtn;

    @FXML
    private Label followersCountLabel;

    @FXML
    private Label followersLabel;

    @FXML
    private Label followingsCountLabel;

    @FXML
    private Label followingsLabel;

    @FXML
    private Label headlineLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private VBox skillsVBox;

    @FXML
    private TextArea summaryTextArea;

    static User user;
    static int lastScene;

    @FXML
    public void initialize() throws IOException {
        profileImageView.setImage(new Image(downloadImg()));
        nameLabel.setText(user.getId());
        headlineLabel.setText(user.getCountry() + ", " + user.getCity());
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhoneNumber());
        ArrayList<User> followers = getFollowers();
        ArrayList<User> followings = getFollowings();
        followersCountLabel.setText(Integer.toString(followers.size()));
        followingsCountLabel.setText(Integer.toString(followings.size()));
        if (isFollowing(LoggedInUser.getId(), user.getId())) {
            followBtn.setText("followed");
            followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-opacity: 0.7;"));
            followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;"));
            followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        }
        getBio();

    }

    @FXML
    void OnMouseClickedCloseBtn(MouseEvent event) throws IOException {
        HelloApplication helloApplication  = new HelloApplication();
        helloApplication.changeScene(3);
    }

    private String downloadImg() {
        if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + user.getId() + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + user.getId() + ".png";
        return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\logo.png";
    }
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public void OnMouseClickedShowFollowersBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        UserFollowersViewController.user = user;
        helloApplication.changeScene(14);
    }

    public void OnMouseClickedShowFollowingsBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        UserFollowingsViewController.user = user;
        helloApplication.changeScene(15);
    }

    public void OnMouseClickedSumBtn(MouseEvent mouseEvent) {
    }

    public void OnMouseClickedEduBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        UserEducationViewController.user = user;
        UserEducationViewController.lastScene = 13;
        helloApplication.changeScene(16);
    }
    public static ArrayList<User> getFollowers() throws IOException {
        URL url = new URL("http://localhost:8000/follows/followers/" + user.getId());
        String response;
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        StringBuffer response1 = new StringBuffer();
        while ((inputline = in.readLine()) != null) {
            response1.append(inputline);
        }
        in.close();
        response = response1.toString();
        JSONArray jsonObject = new JSONArray(response);
        String[] users = toStringArray(jsonObject);
        ArrayList<User> users1 = new ArrayList<>();
        for (String t: users) {
            JSONObject obj = new JSONObject(t);
            User user = getUser(obj.getString("follower"));
            users1.add(user);
        }
        return users1;
    }
    public static User getUser(String userId) throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/users/" + userId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        responseCode = con.getResponseCode();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline1;
        StringBuffer response2 = new StringBuffer();
        while ((inputline1 = in1.readLine()) != null) {
            response2.append(inputline1);
        }
        in1.close();
        response = response2.toString();
        JSONObject obj = new JSONObject(response);
        User user = new User(userId, obj.getString("firstName"), obj.getString("lastName"), obj.getString("additionalName"), obj.getString("country"), obj.getString("city"), obj.getString("email"), obj.getString("password"), obj.getString("phoneNumber"), obj.getInt("followers"), obj.getInt("followings"));
        return user;
    }
    public static ArrayList<User> getFollowings() throws IOException {
        URL url = new URL("http://localhost:8000/follows/followings/" + user.getId());
        String response;
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        StringBuffer response1 = new StringBuffer();
        while ((inputline = in.readLine()) != null) {
            response1.append(inputline);
        }
        in.close();
        response = response1.toString();
        JSONArray jsonObject = new JSONArray(response);
        String[] users = toStringArray(jsonObject);
        ArrayList<User> users1 = new ArrayList<>();
        for (String t: users) {
            JSONObject obj = new JSONObject(t);
            User user = getUser(obj.getString("followed"));
            users1.add(user);
        }
        return users1;
    }
    private void getBio() throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/bios/" + user.getId());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        responseCode = con.getResponseCode();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline1;
        StringBuffer response2 = new StringBuffer();
        while ((inputline1 = in1.readLine()) != null) {
            response2.append(inputline1);
        }
        in1.close();
        response = response2.toString();
        if (!response.equals("NO BIO!")) {
            JSONObject obj = new JSONObject(response);
            summaryTextArea.setText(obj.getString("bioText"));
        }
        else
            summaryTextArea.setText("Add A Summary For YourSelf");


    }
    public void OnMouseClickedSkillBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        UserSkillViewController.lastScene = 13;
        UserSkillViewController.user = user;
        helloApplication.changeScene(17);
    }

    public void followBtnClicked(ActionEvent actionEvent) throws IOException {
        if (!isFollowing(LoggedInUser.getId(), user.getId())) {
            URL url = new URL("http://localhost:8000/follows/" + LoggedInUser.getId() + "/" + user.getId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", HelloApplication.token);
            con.setRequestMethod("POST");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response1 = new StringBuffer();
            while ((inputline = in.readLine()) != null) {
                response1.append(inputline);
            }
            in.close();
            String response = response1.toString();

            if (response.equals("successful!")) {
                followBtn.setText("followed");
                followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-opacity: 0.7;"));
                followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;"));
                followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
                followersCountLabel.setText(Integer.toString(Integer.parseInt(followersCountLabel.getText()) + 1));
            }
            else {
                System.out.println(response);
            }
        }
        else {
            URL url = new URL("http://localhost:8000/follows/" + LoggedInUser.getId() + "/" + user.getId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", HelloApplication.token);
            con.setRequestMethod("DELETE");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response1 = new StringBuffer();
            while ((inputline = in.readLine()) != null) {
                response1.append(inputline);
            }
            in.close();
            String response = response1.toString();

            if (response.equals("successful!")) {
                followBtn.setText("follow");
                followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
                followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-opacity: 0.7;"));
                followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px;"));
                followersCountLabel.setText(Integer.toString(Integer.parseInt(followersCountLabel.getText()) - 1));
            }
            else {
                System.out.println(response);
            }
        }
    }
    private ArrayList<Follow> getAllFollowings(String userId) throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/follows/followings/" + userId);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        StringBuffer response1 = new StringBuffer();
        while ((inputline = in.readLine()) != null) {
            response1.append(inputline);
        }
        in.close();
        response = response1.toString();
        JSONArray jsonObject = new JSONArray(response);
        String[] follows = toStringArray(jsonObject);
        ArrayList <Follow> followings = new ArrayList<>();
        for (String t: follows) {
            JSONObject obj = new JSONObject(t);
            Follow l = new Follow(obj.getString("follower"), obj.getString("followed"));
            followings.add(l);
        }
        return followings;
    }
    private boolean isFollowing(String follower, String followed) throws IOException {
        ArrayList<Follow> followings = getAllFollowings(follower);
        for (Follow t: followings) {
            if (t.getFollowed().equals(followed) && t.getFollower().equals(follower))
                return true;
        }
        return false;
    }

}
