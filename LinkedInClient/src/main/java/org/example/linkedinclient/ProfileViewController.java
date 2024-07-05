package org.example.linkedinclient;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.linkedinclient.FeedController.toStringArray;
import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class ProfileViewController {

    @FXML
    private Button changePicBtn;

    @FXML
    private FontAwesomeIconView closeBtn;

    @FXML
    private FontAwesomeIconView editEduBtn;

    @FXML
    private FontAwesomeIconView editSkillBtn;

    @FXML
    private FontAwesomeIconView editSumBtn;

    @FXML
    private VBox educationVBox;

    @FXML
    private Label emailLabel;

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
    private Button logOutBtn;

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
    @FXML
    public void initialize() throws IOException {
        profileImageView.setImage(new Image(downloadImg()));
        nameLabel.setText(LoggedInUser.getId());
        headlineLabel.setText(LoggedInUser.getCountry() + ", " + LoggedInUser.getCity());
        emailLabel.setText(LoggedInUser.getEmail());
        phoneLabel.setText(LoggedInUser.getPhoneNumber());
        ArrayList<User> followers = getFollowers();
        ArrayList<User> followings = getFollowings();
        followersCountLabel.setText(Integer.toString(followers.size()));
        followingsCountLabel.setText(Integer.toString(followings.size()));
        getBio();

    }

    @FXML
    void OnMouseClickedCloseBtn(MouseEvent event) throws IOException {
        HelloApplication helloApplication  = new HelloApplication();
        helloApplication.changeScene(3);
    }

    @FXML
    void OnMouseClickededitEduBtn(MouseEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScene(6);
    }

    @FXML
    void OnMouseClickededitSkillBtn(MouseEvent event) {

    }

    @FXML
    void OnMouseClickededitSumBtn(MouseEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScene(8);
    }

    @FXML
    void changePicClicked(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("pictures", "*.png"));
        List<File> f = fc.showOpenMultipleDialog(null);
        String res = "";
        if (f == null)
            return;
        for (int i = 0; i < f.size(); i++) {
            File file = f.get(i);
            res += file.getName();

            if (i != f.size() - 1)
                res += ", ";
        }
        if (f.get(0) != null) {
            copyFileToDirectory(f.get(0), "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets", "profPic" + LoggedInUser.getId() + ".png");
            profileImageView.setImage(new Image(downloadImg()));
        }

    }
    private void copyFileToDirectory(File file, String targetDirectory, String newName) throws IOException {
        Path targetPath = Path.of(targetDirectory, newName);
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
    private String downloadImg() {
        if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + LoggedInUser.getId() + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + LoggedInUser.getId() + ".png";
        return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\logo.png";
    }
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }


    @FXML
    void logOutClicked(ActionEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        LoggedInUser = null;
        helloApplication.changeScene(1);
    }

    public void OnMouseClickedShowFollowersBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScene(11);
    }

    public void OnMouseClickedShowFollowingsBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScene(12);
    }

    public void OnMouseClickedSumBtn(MouseEvent mouseEvent) {
    }

    public void OnMouseClickedEduBtn(MouseEvent mouseEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScene(6);
    }
    public static ArrayList<User> getFollowers() throws IOException {
        URL url = new URL("http://localhost:8000/follows/followers/" + LoggedInUser.getId());
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
        URL url = new URL("http://localhost:8000/follows/followings/" + LoggedInUser.getId());
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
        URL url = new URL("http://localhost:8000/bios/" + LoggedInUser.getId());
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
        helloApplication.changeScene(9);
    }
}
