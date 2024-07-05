package org.example.linkedinclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LogInController {

    @FXML
    private PasswordField PassWord;

    @FXML
    private Hyperlink forgetPassLink;

    @FXML
    private Button logInBtn;

    @FXML
    private Button signInBtn;

    @FXML
    private TextField userName;

    @FXML
    private Label wrong;
    private void clear() {
        PassWord.setText("");
        userName.setText("");
        wrong.setText("");
    }
    @FXML
    void onMouseEnteredLogin(MouseEvent event) {
        logInBtn.setStyle("-fx-background-color: #007fd3;");
    }

    @FXML
    void onMouseEnteredSignIn(MouseEvent event) {
        signInBtn.setStyle("-fx-background-color: #222527;");
    }

    @FXML
    void onMouseExitedLogin(MouseEvent event) {
        logInBtn.setStyle("-fx-background-color: #0598ff;");
    }

    @FXML
    void onMouseExitedSignIn(MouseEvent event) {
        signInBtn.setStyle("-fx-background-color: #000000;");
    }

    @FXML
    void loginBtnClicked(MouseEvent event) throws IOException {
        if (userName.getText().length() == 0 || PassWord.getText().length() == 0) {
            wrong.setText("PLEASE ENTER ALL OF THE FIELDS!");
        }
        else {
            URL url = new URL("http://localhost:8000/sessions/" + userName.getText() + "/" + PassWord.getText());
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
            String response = response1.toString();
            HelloApplication.token = con.getHeaderField("JWT");
            if (response.equals("userID or PassWord is incorrect")) {
                wrong.setText("Username or Password is incorrect!");
            }
            else {
                HelloApplication m = new HelloApplication();
                url = new URL("http://localhost:8000/users/" + userName.getText());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                responseCode = con.getResponseCode();
                BufferedReader in1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline1;
                StringBuffer response2 = new StringBuffer();
                while ((inputline1 = in1.readLine()) != null) {
                    response2.append(inputline1);
                }
                in.close();
                response = response2.toString();
                JSONObject obj = new JSONObject(response);
                if (!obj.getString("id").equals(userName.getText()))
                    wrong.setText("Username or Password is incorrect!");
                else {
                    User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("additionalName"),  obj.getString("country"), obj.getString("city"), obj.getString("email"), obj.getString("password"), obj.getString("phoneNumber"), obj.getInt("followers"), obj.getInt("followings"));
                    HelloApplication.LoggedInUser = user;
                    clear();
                    m.changeScene(3);
                }
            }
        }

    }

    @FXML
    void signInClicked(ActionEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        clear();
        helloApplication.changeScene(2);
    }

}
