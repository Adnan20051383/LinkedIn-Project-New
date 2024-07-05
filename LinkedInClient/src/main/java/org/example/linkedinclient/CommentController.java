package org.example.linkedinclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.example.linkedinclient.HelloApplication.LoggedInUser;

public class CommentController {


    @FXML
    private Button EventBtn;

    @FXML
    private Button articleBtn;

    @FXML
    private FontAwesomeIconView closeBtn;

    @FXML
    private Label commentCountLabel;

    @FXML
    private VBox commentsBox;

    @FXML
    private Button followButton;

    @FXML
    private Button likeButton;

    @FXML
    private Button mediaBtn;

    @FXML
    private VBox postBox;

    @FXML
    private Button postButton;

    @FXML
    private Label postContentLabel;

    @FXML
    private VBox postCreationVBox;

    @FXML
    private Label postHeaderLabel;

    @FXML
    private TextArea postTextArea;

    @FXML
    private Button shareButton;

    @FXML
    private Label timelineLabel;
    @FXML
    private Label posterIdLabel;
    @FXML
    private ImageView profPostImgView;
    static Post post;
    private Map<String, Button> followButtonsMap = new HashMap<>();
    private Map<String, Button> likeButtonsMap = new HashMap<>();
    private int commentCounts;

    @FXML
    void OnMouseClickedArticleBtn(MouseEvent event) {

    }

    @FXML
    void OnMouseClickedCloseBtn(MouseEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        postTextArea.setText("");
        helloApplication.changeScene(3);
    }

    @FXML
    void OnMouseClickedMediaBtn(MouseEvent event) {

    }

    @FXML
    void OnMouseClickedPostBtn(MouseEvent event) throws IOException {
        String userId = LoggedInUser.getId();
        Comment comment = new Comment(userId, postTextArea.getText(), post.getPostId());
        String res = saveComment(comment);
        comment.setPostId(res);
        addComment(comment , downloadProfCommentImg(comment.getPosterId()));
    }

    private String saveComment(Comment comment) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8000/comments/" +  LoggedInUser.getId() + "/" + post.getPostId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", HelloApplication.token);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(comment);

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

    public void initialize() throws IOException {
        profPostImgView.setImage(new Image(downloadProfPostImg()));
        posterIdLabel.setText("    " + post.getPosterId());
        postContentLabel.setText(post.getContent());
        if (isFollowing(LoggedInUser.getId(), post.getPosterId())) {
            followButton.setText("followed");
            followButton.setOnMouseEntered(event -> followButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
            followButton.setOnMouseExited(event -> followButton.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
            FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
            followIcon.getStyleClass().add("followIcon");
            followButton.setGraphic(followIcon);
            followButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        }
        if (IsLike(LoggedInUser.getId(), post.getPostId())) {
            likeButton.setText(Integer.toString(post.getLikesNumber()));
            likeButton.setStyle("-fx-background-color: #ccc;");
        }
        URL url = new URL("http://localhost:8000/comments/" + post.getPostId());
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
        String[] comments = toStringArray(jsonObject);
        commentCounts = 0;
        for (String t : comments) {
            JSONObject obj = new JSONObject(t);
            Comment comment = new Comment(obj.getString("posterId"), obj.getString("content"), post.getPostId());
            comment.setPostId(obj.getString("postId"));
            comment.setLikesNumber(obj.getInt("likesNumber"));
            comment.setCommentsNumber(obj.getInt("commentsNumber"));
            comment.setTimeStamp(new Date(obj.getLong("timeStamp")));
            addComment(comment , downloadProfCommentImg(comment.getPosterId()));
            if (IsLike(LoggedInUser.getId(), comment.getPostId())) {
                Button likeButton = likeButtonsMap.get(comment.getPostId());
                likeButton.setText(Integer.toString(comment.getLikesNumber()));
                likeButton.setStyle("-fx-background-color: #ccc;");
            }
            if (isFollowing(LoggedInUser.getId(), comment.getPosterId())) {
                Button followBtn = followButtonsMap.get(comment.getPostId());
                followBtn.setText("followed");
                followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
                followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
                FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                followIcon.getStyleClass().add("followIcon");
                followBtn.setGraphic(followIcon);
                followBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            }
        }
        commentCountLabel.setText(commentCounts + " Comments");


    }
    private String downloadProfCommentImg(String userId) {
        if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + userId + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + userId + ".png";
        return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\logo.png";
    }
    private String downloadProfPostImg() {
        if (fileExists("C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + post.getPosterId() + ".png"))
            return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\profPic" + post.getPosterId() + ".png";
        return "C:\\Users\\3500-\\Desktop\\LinkedIn Project\\LinkedInClient\\src\\main\\resources\\org\\example\\linkedinclient\\assets\\logo.png";
    }
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
    private void updateCommentCount() {
        commentCountLabel.setText(++commentCounts + " Comments");
    }

    private void addComment(Comment comment,  String profileImagePath) {
        updateCommentCount();
        VBox commentBox = new VBox(10);
        commentBox.getStyleClass().add("post-box");
        commentBox.setPadding(new Insets(10));
        commentBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");

// Profile Image
        Image profileImage = new Image(profileImagePath);
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        Circle clip = new Circle(25, 25, 25);
        profileImageView.setClip(clip);

        Label commentHeader = new Label(comment.getPosterId());
        commentHeader.getStyleClass().add("post-header");

// Follow Button
        Button followBtn = new Button("Follow");
        FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
        followIcon.getStyleClass().add("followIcon");
        followBtn.setGraphic(followIcon);
        followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
        followBtn.setOnMouseEntered(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-opacity: 0.7;"));
        followBtn.setOnMouseExited(event -> followBtn.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;"));
        followBtn.setCursor(javafx.scene.Cursor.HAND);
        followButtonsMap.put(comment.getPostId(), followBtn);
        followBtn.setOnAction(event -> {
            try {
                handleFollow(comment, followBtn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

// Header with Profile Image, Name, and Follow Button
        HBox headerBox = new HBox(10, profileImageView, commentHeader, followBtn);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(commentHeader, Priority.ALWAYS);

// Comment Content
        Label commentLabel = new Label(comment.getContent());
        commentLabel.setWrapText(true); // Ensure text wraps within the label

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(commentLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(100);
        scrollPane.getStyleClass().add("scroll-pane-label");

// Timeline
        Label timelineLabel = new Label("Posted on: " + comment.getTimeStamp());
        timelineLabel.getStyleClass().add("timeline-label");

// Action Buttons
        HBox commentActions = new HBox(10);
        commentActions.setAlignment(Pos.CENTER);

        Button likeButton = new Button("Like");
        FontAwesomeIconView thumb = new FontAwesomeIconView(FontAwesomeIcon.THUMBS_UP);
        thumb.getStyleClass().add("thumbIcon");
        likeButton.setGraphic(thumb);
        likeButton.setCursor(javafx.scene.Cursor.HAND);
        likeButton.getStyleClass().addAll("post-box-btn", "likeBtn");
        likeButtonsMap.put(comment.getPostId(), likeButton);
        likeButton.setOnAction(event -> {
            handleLike(comment, likeButton);
        });

        Button shareButton = new Button("Share");
        FontAwesomeIconView share = new FontAwesomeIconView(FontAwesomeIcon.SHARE);
        share.getStyleClass().add("shareIcon");
        shareButton.setGraphic(share);
        shareButton.setCursor(javafx.scene.Cursor.HAND);
        shareButton.getStyleClass().addAll("post-box-btn", "shareBtn");

// Add action listeners using lambdas
        likeButton.setOnAction(event -> handleLike(comment, likeButton));
        shareButton.setOnAction(event -> handleShare(post));

        commentActions.getChildren().addAll(likeButton, shareButton);

        VBox commentContentBox = new VBox(5, headerBox, scrollPane, timelineLabel, commentActions);
        commentBox.getChildren().add(commentContentBox);

// Add commentBox to commentsBox at the appropriate index
        commentsBox.getChildren().add(0, commentBox);

    }

    public void OnMouseClickedLikeBtn(MouseEvent mouseEvent) {
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

    public void OnMouseClickedShareBtn(MouseEvent mouseEvent) {
    }

    public void OnMouseEnteredShareBtn(MouseEvent mouseEvent) {
        shareButton.setStyle("-fx-background-color: #ccc");
    }

    public void OnMouseEnteredLikeBtn(MouseEvent mouseEvent) {
        likeButton.setStyle("-fx-background-color: #ccc");
    }

    public void OnMouseExitedLikeBtn(MouseEvent mouseEvent) throws IOException {
        if (!IsLike(LoggedInUser.getId(), post.getPostId()))
            likeButton.setStyle("-fx-background-color: transparent");
    }

    public void OnMouseExitedShareBtn(MouseEvent mouseEvent) {
        shareButton.setStyle("-fx-background-color: transparent");
    }

    public void OnMouseEnteredFollowBtn(MouseEvent mouseEvent) {

        followButton.setStyle("-fx-opacity: 0.7; -fx-background-color: #0598ff; -fx-text-fill: white;");
    }

    public void OnMouseExitedFollowBtn(MouseEvent mouseEvent) {
        followButton.setStyle("-fx-opacity: 1; -fx-background-color: #0598ff; -fx-text-fill: white;");
    }



    // Example handlers for like, comment, and share actions
    private void handleLike(Comment comment, Button likeButton) {
        try {
            if (!IsLike(LoggedInUser.getId(), comment.getPostId())) {
                URL url = new URL("http://localhost:8000/likes/" + LoggedInUser.getId() + "/" + comment.getPostId());
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
                    comment.addLike();
                    likeButton.setText(Integer.toString(comment.getLikesNumber()));
                    likeButton.setStyle("-fx-background-color: #ccc;");
                }
                else {
                    System.out.println(response);
                }
            }
            else {
                URL url = new URL("http://localhost:8000/likes/" + LoggedInUser.getId() + "/" + comment.getPostId());
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
                    comment.takeLike();
                    likeButton.setText(Integer.toString(comment.getLikesNumber()));
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
        ArrayList<Like> likes = getAllLikes();
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

    private void handleFollow(Comment comment, Button followBtn) throws IOException {
        if (!isFollowing(LoggedInUser.getId(), comment.getPosterId())) {
            URL url = new URL("http://localhost:8000/follows/" + LoggedInUser.getId() + "/" + comment.getPosterId());
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
                changeOtherFollowBtns(comment);
                if (post.getPosterId().equals(comment.getPosterId())) {
                    followButton.setText("followed");
                    followButton.setOnMouseEntered(event -> followButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
                    followButton.setOnMouseExited(event -> followButton.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
                    FontAwesomeIconView followPostIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                    followPostIcon.getStyleClass().add("followIcon");
                    followButton.setGraphic(followIcon);
                    followButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                }
            }
            else {
                System.out.println(response);
            }
        }
        else {
            URL url = new URL("http://localhost:8000/follows/" + LoggedInUser.getId() + "/" + comment.getPosterId());
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
                changeOtherFollowBtns(comment);
                if (post.getPosterId().equals(comment.getPosterId())) {
                    followButton.setText("follow");
                    FontAwesomeIconView followPostIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
                    followPostIcon.getStyleClass().add("followIcon");
                    followButton.setGraphic(followPostIcon);
                    followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
                    followButton.setOnMouseEntered(event -> followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-opacity: 0.7;"));
                    followButton.setOnMouseExited(event -> followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;"));
                }
            }
            else {
                System.out.println(response);
            }
        }

    }
    private void changeOtherFollowBtns(Comment comment) throws IOException {
        String response;
        URL url = new URL("http://localhost:8000/comments/" + post.getPostId());
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
            if (comment.getPosterId().equals(obj.getString("posterId"))) {
                System.out.println(obj.getString("postId"));
                Button followBtn = followButtonsMap.get(obj.getString("postId"));
                if (isFollowing(LoggedInUser.getId(), comment.getPosterId())) {
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

    public void OnMouseClickedPostFollowBtn(MouseEvent mouseEvent) throws IOException {
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
                followButton.setText("followed");
                followButton.setOnMouseEntered(event -> followButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-opacity: 0.7;"));
                followButton.setOnMouseExited(event -> followButton.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
                FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                followIcon.getStyleClass().add("followIcon");
                followButton.setGraphic(followIcon);
                followButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                Comment com = new Comment();
                com.setPosterId(post.getPosterId());
                changeOtherFollowBtns(com);
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
                followButton.setText("follow");
                FontAwesomeIconView followIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
                followIcon.getStyleClass().add("followIcon");
                followButton.setGraphic(followIcon);
                followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;");
                followButton.setOnMouseEntered(event -> followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white; -fx-opacity: 0.7;"));
                followButton.setOnMouseExited(event -> followButton.setStyle("-fx-background-color: #0598ff; -fx-text-fill: white;"));
                Comment com = new Comment();
                com.setPosterId(post.getPosterId());
                changeOtherFollowBtns(com);
            }
            else {
                System.out.println(response);
            }
        }

    }
}