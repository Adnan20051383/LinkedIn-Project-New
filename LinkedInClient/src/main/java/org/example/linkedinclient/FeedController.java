package org.example.linkedinclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class FeedController {

    @FXML
    private Button EventBtn;

    @FXML
    private Button articleBtn;

    @FXML
    private VBox feedVBox;

    @FXML
    private Button jobsBtn;

    @FXML
    private Button meBtn;

    @FXML
    private Button mediaBtn;

    @FXML
    private Button msgBtn;

    @FXML
    private Button networkBtn;

    @FXML
    private Button notifBtn;

    @FXML
    private Button postButton;

    @FXML
    private VBox postCreationVBox;

    @FXML
    private Label userNameLabel;

    @FXML
    private TextArea postTextArea;
    @FXML
    private ImageView profileImgView;
    List <File> pictures = new ArrayList<>();
    private Map<String, Button> likeButtonsMap = new HashMap<>();
    private Map<String, Button> followButtonsMap = new HashMap<>();



    @FXML
    void OnMouseClickedArticleBtn(MouseEvent event) {

    }

    @FXML
    void OnMouseClickedPostBtn(MouseEvent event) throws IOException {
        String userId = LoggedInUser.getId();
        Post post = new Post(userId, postTextArea.getText());
        if (!(pictures.isEmpty()) ) {
            for (int i = 0; i < pictures.size(); i++)
                post.getMediaPaths().add("a");
        }

        String res = savePost(post);
        post.setPostId(res);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        String formattedDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        if (!pictures.isEmpty())
            savePicture(post.getPostId());
        addPostToFeed(post ,post.getContent(), formattedDateTime, downloadProfPostImg(post.getPosterId()));
        postTextArea.setText("");
        mediaBtn.setText("Media");

    }
    public void savePicture(String postId) throws IOException {
        copyFileToDirectory(pictures.get(0), "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets", "postPic" + postId + ".png");
    }



    @FXML
    void OnMouseClickedMediaBtn(MouseEvent event) {
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
        pictures = f;
        mediaBtn.setText(res);
    }
    private void copyFileToDirectory(File file, String targetDirectory, String newName) throws IOException {
        Path targetPath = Path.of(targetDirectory, newName);
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
    public String savePost(Post post) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8000/posts/" +  post.getPosterId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", HelloApplication.token);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(post);

            byte[] postDataBytes = json.getBytes();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) > 0; )
                sb.append((char) c);
            response = sb.toString();
            return response;
        }
        catch (ConnectException e) {
            return "Connection failed";
        }
    }

    @FXML
    void onMouseClickedTextArea(MouseEvent event) {
        postButton.setVisible(true);
    }

    @FXML
    public void initialize() throws IOException {
        userNameLabel.setText(LoggedInUser.getId());
        profileImgView.setImage(new Image(downloadProfImg()));
        URL url = new URL("http://localhost:8000/posts");
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
        String[] posts = toStringArray(jsonObject);
        for (String t : posts) {
            JSONObject obj = new JSONObject(t);
            Post post = new Post(obj.getString("posterId"), obj.getString("content"));
            post.setPostId(obj.getString("postId"));
            post.setLikesNumber(obj.getInt("likesNumber"));
            post.setCommentsNumber(obj.getInt("commentsNumber"));
            post.setTimeStamp(new Date(obj.getLong("timeStamp")));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            addPostToFeed(post ,post.getContent(), simpleDateFormat.format(new Date(obj.getLong("timeStamp"))), downloadProfPostImg(post.getPosterId()));
            if (IsLike(LoggedInUser.getId(), post.getPostId())) {
                Button likeButton = likeButtonsMap.get(post.getPostId());
                likeButton.setText(Integer.toString(post.getLikesNumber()));
                likeButton.setStyle("-fx-background-color: #ccc;");
            }
            if (isFollowing(LoggedInUser.getId(), post.getPosterId())) {
                Button followBtn = followButtonsMap.get(post.getPostId());
                followBtn.setText("followed");
                followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
                followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
                FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                followIcon.getStyleClass().add("followIcon");
                followBtn.setGraphic(followIcon);
                followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            }
        }
    }

    public static String downloadProfPostImg(String userId) {
        if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + userId + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + userId + ".png";
        return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\logo.png";
    }
    public static String downloadProfImg() {
        if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + LoggedInUser.getId() + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + LoggedInUser.getId() + ".png";
        return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\logo.png";
    }
    public void addPostToFeed(Post post, String postContent, String postDate, String profileImagePath) throws IOException {
        VBox postBox = new VBox(10);
        postBox.getStyleClass().add("post-box");
        postBox.setPadding(new Insets(10));
        postBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Profile Image
        Image profileImage = new Image(profileImagePath);
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        Circle clip = new Circle(25, 25, 25);
        profileImageView.setClip(clip);

        Label postHeader = new Label(post.getPosterId());
        postHeader.setCursor(Cursor.HAND);
        postHeader.setOnMouseClicked(e -> {
            try {
                handleUserLink(getUser(post.getPosterId()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        postHeader.getStyleClass().add("post-header");

        // Follow Button
        Button followButton = new Button("Follow");
        FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
        followIcon.getStyleClass().add("followIcon");
        followButton.setGraphic(followIcon);
        followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
        followButton.setOnMouseEntered(event -> followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-opacity: 0.7;"));
        followButton.setOnMouseExited(event -> followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;"));
        followButton.setCursor(javafx.scene.Cursor.HAND);
        followButtonsMap.put(post.getPostId(), followButton);
        followButton.setOnAction(event -> {
            try {
                handleFollow(post, followButton);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HBox postHeaderBox = new HBox(10, profileImageView, postHeader);
        postHeaderBox.setAlignment(Pos.CENTER_LEFT);

        // Header with Follow Button
        HBox headerWithFollow = new HBox(postHeaderBox, followButton);
        headerWithFollow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(postHeaderBox, Priority.ALWAYS);
        headerWithFollow.setAlignment(Pos.CENTER_RIGHT);

        Label postLabel = new Label(postContent);
        postLabel.setWrapText(true); // Ensure text wraps within the label

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(postLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(100);
        scrollPane.getStyleClass().add("scroll-pane-label");

        // Timeline
        Label timelineLabel = new Label("Posted on: " + postDate);
        timelineLabel.getStyleClass().add("timeline-label");

        HBox postActions = new HBox(10);
        postActions.setAlignment(Pos.CENTER);

        Button likeButton = new Button("Like");
        FontAwesomeIconView thumb = new FontAwesomeIconView(FontAwesomeIcon.THUMBS_UP);
        thumb.getStyleClass().add("thumbIcon");
        likeButton.setGraphic(thumb);
        likeButton.setCursor(javafx.scene.Cursor.HAND);
        likeButton.getStyleClass().addAll("post-box-btn", "likeBtn");
        likeButtonsMap.put(post.getPostId(), likeButton);

        Button commentButton = new Button("Comment");
        FontAwesomeIconView comment = new FontAwesomeIconView(FontAwesomeIcon.COMMENT);
        comment.getStyleClass().add("commentIcon");
        commentButton.setGraphic(comment);
        commentButton.setCursor(javafx.scene.Cursor.HAND);
        commentButton.getStyleClass().addAll("post-box-btn", "cmtBtn");

        Button shareButton = new Button("Share");
        FontAwesomeIconView share = new FontAwesomeIconView(FontAwesomeIcon.SHARE);
        share.getStyleClass().add("shareIcon");
        shareButton.setGraphic(share);
        shareButton.setCursor(javafx.scene.Cursor.HAND);
        shareButton.getStyleClass().addAll("post-box-btn", "shareBtn");

        // Add action listeners using lambdas
        likeButton.setOnAction(event -> handleLike(post, likeButton));
        commentButton.setOnAction(event -> {
            try {
                handleComment(post);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        shareButton.setOnAction(event -> handleShare(post));

        postActions.getChildren().addAll(likeButton, commentButton, shareButton);
        // Hidden ImageView
        // Hidden Image View
        ImageView hiddenImageView = new ImageView();
        hiddenImageView.setFitWidth(200);
        hiddenImageView.setPreserveRatio(true);
        hiddenImageView.setVisible(false);

        // To center the hidden ImageView
        StackPane imagePane = new StackPane(hiddenImageView);
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setVisible(false); // Initially hidden

        VBox postContentBox = new VBox(5, headerWithFollow, scrollPane, imagePane, timelineLabel, postActions);
        if (downloadImg(post) != null) {
            hiddenImageView.setVisible(true);
            imagePane.setVisible(true);
            Image img = new Image(downloadImg(post));
            hiddenImageView.setImage(img);
        }
        postBox.getChildren().add(postContentBox);

        feedVBox.getChildren().add(1, postBox);
    }
    public void handleUserLink(User user) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        if (!LoggedInUser.getId().equals(user.getId())) {
            UserProfilePageController.user = user;
            UserProfilePageController.lastScene = 3;
            helloApplication.changeScene(13);
        }
        else
            helloApplication.changeScene(5);

    }
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    private String downloadImg(Post post) {
         if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\postPic" + post.getPostId() + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\postPic" + post.getPostId() + ".png";
        return null;
    }


    // Example handlers for like, comment, and share actions
    private void handleLike(Post post, Button likeButton) {
        try {
            if (!IsLike(LoggedInUser.getId(), post.getPostId())) {
                URL url = new URL("http://localhost:8000/likes/" + LoggedInUser.getId() + "/" + post.getPostId());
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

                if (response.equals("successful")) {
                    post.addLike();
                    likeButton.setText(Integer.toString(post.getLikesNumber()));
                    likeButton.setStyle("-fx-background-color: #ccc;");
                }
                else {
                    System.out.println(response);
                }
            }
            else {
                URL url = new URL("http://localhost:8000/likes/" + LoggedInUser.getId() + "/" + post.getPostId());
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

                if (response.equals("successful")) {
                    post.takeLike();
                    likeButton.setText(Integer.toString(post.getLikesNumber()));
                    likeButton.setStyle("-fx-background-color: transparent;");
                }
                else {
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean IsLike(String userId, String postId) throws IOException {
        ArrayList <Like> likes = getAllLikes();
        for (Like l: likes) {
            if (l.getLiker().equals(userId) && l.getLiked().equals(postId))
                return true;
        }
        return false;
    }

    public ArrayList <Like> getAllLikes() throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/likes");
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
        ArrayList <Like> likes = new ArrayList<>();
        for (String t: users) {
            JSONObject obj = new JSONObject(t);
            Like l = new Like(obj.getString("liker"), obj.getString("liked"));
            likes.add(l);
        }
        return likes;
    }

    private void handleFollow(Post post, Button followBtn) throws IOException {
        if (!isFollowing(LoggedInUser.getId(), post.getPosterId())) {
            URL url = new URL("http://localhost:8000/follows/" + LoggedInUser.getId() + "/" + post.getPosterId());
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
                followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
                followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
                FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                followIcon.getStyleClass().add("followIcon");
                followBtn.setGraphic(followIcon);
                followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                changeOtherFollowBtns(post);
            }
            else {
                System.out.println(response);
            }
        }
        else {
            URL url = new URL("http://localhost:8000/follows/" + LoggedInUser.getId() + "/" + post.getPosterId());
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
                FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
                followIcon.getStyleClass().add("followIcon");
                followBtn.setGraphic(followIcon);
                followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
                followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-opacity: 0.7;"));
                followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;"));
                changeOtherFollowBtns(post);
            }
            else {
                System.out.println(response);
            }
        }

    }
    private void changeOtherFollowBtns(Post post) throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/posts");
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
        String[] posts1 = toStringArray(jsonObject);
        for (String t : posts1) {
            JSONObject obj = new JSONObject(t);
            if (post.getPosterId().equals(obj.getString("posterId"))) {
                Button followBtn = followButtonsMap.get(obj.getString("postId"));
                if (isFollowing(LoggedInUser.getId(), post.getPosterId())) {
                    followBtn.setText("followed");
                    followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
                    followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
                    FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                    followIcon.getStyleClass().add("followIcon");
                    followBtn.setGraphic(followIcon);
                    followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                }
                else {
                    followBtn.setText("follow");
                    FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
                    followIcon.getStyleClass().add("followIcon");
                    followBtn.setGraphic(followIcon);
                    followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
                    followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-opacity: 0.7;"));
                    followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;"));
                }
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

    private void handleComment(Post post) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        postTextArea.setText("");
        CommentController.post = post;
        helloApplication.changeScene(4);
    }

    private void handleShare(Post post) {
        // Handle share action
        System.out.println("Post shared: " + post.getPostId());
    }

    public static String[] toStringArray(JSONArray array) {
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }

    public void meBtnClicked(ActionEvent actionEvent) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        postTextArea.setText("");
        helloApplication.changeScene(5);
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
}

