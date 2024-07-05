package org.example.linkedinclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class sumPageController {
    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TextArea summaryTextArea;
    public void handleCancel(ActionEvent actionEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        summaryTextArea.setText("");
        helloApplication.changeScene(5);
    }
    private void saveBio() throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/bios");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("JWT", HelloApplication.token);
        Bio bio = new Bio(LoggedInUser.getId(), summaryTextArea.getText(), "");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(bio);

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

        if (response.equals("successful!")) {
            HelloApplication helloApplication = new HelloApplication();
            helloApplication.changeScene(5);
        } else {
            System.out.println("Server response: " + response);
        }
    }

    public void handleSave(ActionEvent actionEvent) throws IOException {
        saveBio();
    }
}
