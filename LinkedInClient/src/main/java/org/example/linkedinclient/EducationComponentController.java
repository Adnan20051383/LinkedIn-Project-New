package org.example.linkedinclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class EducationComponentController {
    @FXML
    private TextField degreeField;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField fieldOfStudyField;

    @FXML
    private TextField institutionField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Label wrongLable;
    public void handleCancel(ActionEvent actionEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        clear();
        helloApplication.changeScene(6);
    }
    private void clear() {
        degreeField.setText("");
        fieldOfStudyField.setText("");
        institutionField.setText("");
    }

    public void handleSave(ActionEvent actionEvent) throws IOException, ParseException {
        if (degreeField.getText().isEmpty() || fieldOfStudyField.getText().isEmpty() || institutionField.getText().isEmpty() || endDatePicker.getValue() == null || startDatePicker.getValue() == null)
            wrongLable.setText("PLEASE FILL ALL OF THE FIELDS!");
        else {
            try {
                String response;
                URL url = new URL("http://localhost:8000/educations/" +  LoggedInUser.getId());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("JWT", HelloApplication.token);
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String startDate1 = startDate.format(formatter);
                String endDate1 = endDate.format(formatter);
                Education education = new Education(LoggedInUser.getId(), institutionField.getText(),degreeField.getText() + "," + fieldOfStudyField.getText(), startDate1, endDate1, 0, "", "");
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(education);

                byte[] postDataBytes = json.getBytes();

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) > 0; )
                    sb.append((char) c);
                response = sb.toString();
                if (response.equals("successful!")) {
                    HelloApplication helloApplication = new HelloApplication();
                    clear();
                    helloApplication.changeScene(6);
                }
                else {
                    System.out.println(response);
                }
            }
            catch (ConnectException e) {
                e.printStackTrace();
            }
        }

    }
}
