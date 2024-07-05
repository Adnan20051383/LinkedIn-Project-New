package org.example.linkedinclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class SignInController {

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField PassWord;

    @FXML
    private Button SignInBtn;

    @FXML
    private MenuItem USA;

    @FXML
    private TextField additionalName;

    @FXML
    private MenuItem canada;

    @FXML
    private TextField city;

    @FXML
    private SplitMenuButton countryChosser;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private MenuItem iran;

    @FXML
    private TextField lastName;

    @FXML
    private TextField number;

    @FXML
    private PasswordField rePassWord;

    @FXML
    private TextField userName;

    @FXML
    private Label wrongLable;

    private void clear() {
        rePassWord.setText("");
        PassWord.setText("");
        userName.setText("");
        additionalName.setText("");
        firstName.setText("");
        lastName.setText("");
        wrongLable.setText("");
        email.setText("");
        number.setText("");
        countryChosser.setText("Choose Your Country");
        city.setText("");
    }

    @FXML
    void LoginBtnClicked(MouseEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        clear();
        helloApplication.changeScene(1);
    }

    @FXML
    void SignInBtnClicked(MouseEvent event) throws IOException {
        boolean a1 = firstName.getText().length() == 0;
        boolean a2 = lastName.getText().length() == 0;
        boolean a3 = userName.getText().length() == 0;
        boolean a4 = PassWord.getText().length() == 0;
        boolean a5 = email.getText().length() == 0;
        boolean a6 = number.getText().length() == 0;
        boolean a7 = countryChosser.getText().equals("Choose Your Country");
        boolean a8 = rePassWord.getText().length() == 0;
        if (a1 || a2 || a3 || a4 || a5 || a6 || a7 || a8)
            wrongLable.setText("PLEASE ENTER ALL FIELDS!");
        else if (!isValidEmailAddress(email.getText()))
            wrongLable.setText("NOT A VALID EMAIL!");
        else if (!PassWord.getText().equals(rePassWord.getText()))
            wrongLable.setText("PASSWORDS DO NOT MATCH!");
        else {
            URL url = new URL("http://localhost:8000/users");
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
            boolean Email_existed = false;
            for (String t: users) {
                JSONObject obj = new JSONObject(t);
                User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("additionalName"), obj.getString("country"), obj.getString("city"), obj.getString("email"), obj.getString("password"), obj.getString("phoneNumber") , 0, 0);
                if (user.getEmail().equals(email.getText()) && email.getText().length() != 0)
                    Email_existed = true;
                if (user.getPhoneNumber().equals(number.getText()) && number.getText().length() != 0)
                    Email_existed = true;
            }
            if (Email_existed) {
                wrongLable.setText("Email or Phone existed");
            }
            else {

                url = new URL("http://localhost:8000/users/" + userName.getText());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                responseCode = con.getResponseCode();
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response1 = new StringBuffer();
                while ((inputline = in.readLine()) != null) {
                    response1.append(inputline);
                }
                in.close();
                response = response1.toString();

                if (!response.equals("No User")) {
                    wrongLable.setText("Username exist");
                }
                else {
                    url = new URL("http://localhost:8000/users");
                    con = (HttpURLConnection) url.openConnection();
                    User user = new User(userName.getText(), firstName.getText(), lastName.getText(), additionalName.getText(), countryChosser.getText(), city.getText(), email.getText(), PassWord.getText(), number.getText(), 0 , 0);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(user);
                    byte[] postDataBytes = json.getBytes();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.getOutputStream().write(postDataBytes);

                    Reader in1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = in1.read()) > 0; )
                        sb.append((char) c);
                    response = sb.toString();
                    if (response.equals("successful!")) {
                        clear();
                        HelloApplication helloApplication = new HelloApplication();
                        helloApplication.changeScene(1);

                    } else
                        wrongLable.setText("Server error");

                }
            }
        }

    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(emailAddress);
        return m.matches();
    }
    public static String[] toStringArray(JSONArray array) {
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }
    @FXML
    void USAClicked(ActionEvent event) {
        countryChosser.setText("USA");

    }

    @FXML
    void canadaClicked(ActionEvent event) {
        countryChosser.setText("Canada");
    }

    @FXML
    void iranClicked(ActionEvent event) {
        countryChosser.setText("Iran");
    }

    @FXML
    void onMouseEnteredLogin(MouseEvent event) {
        LoginBtn.setStyle("-fx-background-color: #222527;");
    }

    @FXML
    void onMouseEnteredSignIn(MouseEvent event) {
        SignInBtn.setStyle("-fx-background-color: #007fd3;");
    }

    @FXML
    void onMouseExitedLogin(MouseEvent event) {
        LoginBtn.setStyle("-fx-background-color: #000000;");
    }

    @FXML
    void onMouseExitedSignIn(MouseEvent event) {
        SignInBtn.setStyle("-fx-background-color: #0598ff;");
    }

}
