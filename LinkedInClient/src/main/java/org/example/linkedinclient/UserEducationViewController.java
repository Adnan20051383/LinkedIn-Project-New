package org.example.linkedinclient;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import static org.example.linkedinclient.FeedController.toStringArray;
import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class UserEducationViewController {
    @FXML
    private FontAwesomeIconView addEducationBtn;

    @FXML
    private FontAwesomeIconView closeBtn;

    @FXML
    private FontAwesomeIconView editEducationBtn;

    @FXML
    private ImageView educationImgView;

    @FXML
    private VBox rootVBox;

    static User user;
    static int lastScene;

    @FXML
    void OnMouseClickedCloseBtn(MouseEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScene(lastScene);
    }

    @FXML
    void OnMouseClickedIntsLabel(MouseEvent event) {

    }

    @FXML
    public void initialize() throws IOException, ParseException {
        ArrayList<Education> educations = getEducations();
        for (Education education : educations) {
            addEducation(education.getInstitutionName(), education.getMajor(), education.getStartDate());
        }
    }

    private ArrayList<Education> getEducations() throws IOException, ParseException {
        URL url = new URL("http://localhost:8000/educations/" + user.getId());
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
        String[] educations = toStringArray(jsonObject);
        ArrayList<Education> educations1 = new ArrayList<>();
        for (String t : educations) {
            JSONObject obj = new JSONObject(t);
            Education education = new Education(user.getId(), obj.getString("institutionName"), obj.getString("major"), obj.getString("startDate"), obj.getString("endDate"), 0, "", "");
            educations1.add(education);
        }
        return educations1;
    }


    private void addEducation(String institution, String major, String date) throws IOException {
        HBox newEducationBox = createEducationBox(institution, major, date);
        rootVBox.getChildren().add(1, newEducationBox);
    }

    private HBox createEducationBox(String institution, String major, String date) {
        // Creating a new HBox
        HBox educationBox = new HBox();
        educationBox.setAlignment(javafx.geometry.Pos.CENTER);
        educationBox.setStyle("-fx-background-color: #f0f2f0;");

        // Creating the ImageView
        ImageView imageView = new ImageView(new Image("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\img\\logo.png"));
        imageView.setFitHeight(98);
        imageView.setFitWidth(69);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        // Creating the VBox for labels
        VBox labelsVBox = new VBox();
        labelsVBox.setStyle("-fx-background-color: transparent;  -fx-border-width: 0 0 0 0;");

        // Institution label
        Label institutionLabel = new Label(institution);
        institutionLabel.setCursor(Cursor.HAND);
        institutionLabel.setFont(new javafx.scene.text.Font("Arial Bold", 18));

        // Major label
        Label majorLabel = new Label(major);
        majorLabel.setFont(new javafx.scene.text.Font("Arial", 14));

        // Date label
        Label dateLabel = new Label(date);
        dateLabel.setTextFill(javafx.scene.paint.Color.web("#929292"));
        dateLabel.setFont(new javafx.scene.text.Font("Arial", 14));

        labelsVBox.getChildren().addAll(institutionLabel, majorLabel, dateLabel);

        // Creating the Region for spacing
        Region region = new Region();
        region.setPrefHeight(Region.USE_COMPUTED_SIZE);
        region.setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(region, javafx.scene.layout.Priority.ALWAYS);


        // Adding children to the HBox
        educationBox.getChildren().addAll(imageView, labelsVBox, region);
        return educationBox;
    }

    public void OnMouseClickedEditEduBtn(MouseEvent mouseEvent) {
    }
}
