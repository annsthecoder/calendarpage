package com.example.calendarpage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SettingsController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView profileImage;
    @FXML private Button uploadImageButton;
    @FXML private Button changeEmailButton;
    @FXML private Button changePasswordButton;
    @FXML private Button homeButton;
    @FXML private Button settingsButton;
    @FXML private Button friendsButton;
    @FXML private Label dateLabel;
    @FXML private VBox aiSidebar;
    @FXML private GridPane miniDayView;
    @FXML private SplitPane splitPane;
    @FXML private ImageView logoImage;
    private final LocalDate currentDate = LocalDate.now();

    @FXML
    private void initialize() {
        // Load logo image
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        logoImage.setImage(logo);
        // Prefill dummy data
        emailField.setText("example@domain.com");
        passwordField.setText("********");

        // Set sidebar open like calendar page
        splitPane.setDividerPositions(0.75);

        applySidebarButtonStyle();
        updateDateLabel();
        renderMiniDayView();
    }

    private void applySidebarButtonStyle() {
        if (homeButton != null)
            homeButton.setStyle("-fx-text-fill: #1A1A1A; -fx-background-color: #CCCCFF; -fx-font-size: 20px; -fx-background-radius: 12px; -fx-font-weight: bold;");
        if (settingsButton != null)
            settingsButton.setStyle("-fx-text-fill: #1A1A1A; -fx-background-color: #D8B9FF; -fx-font-size: 20px; -fx-background-radius: 12px; -fx-font-weight: bold;");
        if (friendsButton != null)
            friendsButton.setStyle("-fx-text-fill: #1A1A1A; -fx-background-color: #CCCCFF; -fx-font-size: 20px; -fx-background-radius: 12px; -fx-font-weight: bold;");
    }

    private void updateDateLabel() {
        if (dateLabel != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
            dateLabel.setText(currentDate.format(formatter));
        }
    }

    private void renderMiniDayView() {
        if (miniDayView == null) return;

        miniDayView.getChildren().clear();
        miniDayView.getRowConstraints().clear();

        // Add a label for the current day and month (no year)
        Label dateLabel = new Label(currentDate.format(DateTimeFormatter.ofPattern("d MMM")));
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 32px; -fx-text-fill: #2e014f;");
        dateLabel.setMaxWidth(Double.MAX_VALUE);
        dateLabel.setAlignment(Pos.TOP_LEFT);
        miniDayView.add(dateLabel, 0, 0, 2, 1);

        // Spacer
        RowConstraints spacer = new RowConstraints();
        spacer.setMinHeight(30);
        miniDayView.getRowConstraints().add(spacer);

        for (int hour = 0; hour < 24; hour++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(30);
            miniDayView.getRowConstraints().add(rowConstraints);

            Label timeLabel = new Label(String.format("%02d:00", hour));
            timeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
            timeLabel.setMaxWidth(Double.MAX_VALUE);
            timeLabel.setAlignment(Pos.CENTER_RIGHT);

            Label eventSlot = new Label();
            eventSlot.setStyle("-fx-border-color: #bbb; -fx-border-width: 0 0 1px 0;");
            eventSlot.setMaxWidth(Double.MAX_VALUE);
            eventSlot.setAlignment(Pos.CENTER_LEFT);

            miniDayView.add(timeLabel, 0, hour + 1);
            miniDayView.add(eventSlot, 1, hour + 1);
            GridPane.setHgrow(eventSlot, Priority.ALWAYS);
        }
    }
    @FXML
    private void onLogoHover() {
        logoImage.setScaleX(1.2);
        logoImage.setScaleY(1.2);
    }

    @FXML
    private void onLogoExit() {
        logoImage.setScaleX(1.0);
        logoImage.setScaleY(1.0);
    }
    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            profileImage.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    private void handleChangeEmail() {
        String newEmail = emailField.getText();
        if (!newEmail.isEmpty() && newEmail.contains("@")) {
            System.out.println("Email changed to: " + newEmail);
        } else {
            System.out.println("Invalid email entered.");
        }
    }

    @FXML
    private void handleChangePassword() {
        String newPassword = passwordField.getText();
        if (newPassword.length() >= 6) {
            System.out.println("Password changed.");
        } else {
            System.out.println("Password too short.");
        }
    }

    @FXML
    private void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/calendarpage/hello-view.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("The Ultimate Calendar");
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToFriends() {
        System.out.println("Navigating to Friends Page (to be implemented).");
    }
}
