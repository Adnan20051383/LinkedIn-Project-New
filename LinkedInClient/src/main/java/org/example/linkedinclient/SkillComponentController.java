package org.example.linkedinclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class SkillComponentController {
    @FXML
    private TextField skillField;

    @FXML
    private Label wrongLabel;

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        skillField.setText("");
        helloApplication.changeScene(9);
    }
    private void saveSkill() throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/skills/" + LoggedInUser.getId());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("JWT", HelloApplication.token);
        Skill skill = new Skill(LoggedInUser.getId(), 1, skillField.getText());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(skill);

        byte[] postDataBytes = json.getBytes();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(postDataBytes);

        try (Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) > 0; ) {
                sb.append((char) c);
            }
            response = sb.toString();
        }

        if (response.equals("successful")) {
            HelloApplication helloApplication = new HelloApplication();
            skillField.setText("");
            helloApplication.changeScene(9);
        } else {
            System.out.println("Server response: " + response);
            wrongLabel.setVisible(true);
            wrongLabel.setText(response);
        }
    }

    public void handleSave(ActionEvent actionEvent) throws IOException {
        if (skillField.getText().isEmpty()) {
            wrongLabel.setText("PLEASE WRITE SOMETHING!!!");
            wrongLabel.setVisible(true);
        }
        else
            saveSkill();
    }
}
