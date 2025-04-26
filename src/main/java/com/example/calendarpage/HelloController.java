package com.example.calendarpage;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {

    // === FXML UI Elements ===
    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private GridPane weekGrid;
    @FXML private GridPane dayGrid;
    @FXML private ImageView logoImage;

    @FXML private VBox weekView;
    @FXML private VBox dayView;

    @FXML private VBox aiSidebar;
    @FXML
    private SplitPane splitPane;

    @FXML
    private VBox mainContent;


    @FXML
    private void toggleAISidebar() {
        boolean isVisible = aiSidebar.isVisible();
        aiSidebar.setVisible(!isVisible);
        aiSidebar.setManaged(!isVisible);

        if (!isVisible) {
            splitPane.setDividerPositions(0.6); // adjust this as you like
        } else {
            splitPane.setDividerPositions(1.0);
        }
    }

//    @FXML
//    private void toggleAISidebar() {
//        // Check current visibility state
//        boolean currentlyVisible = aiSidebar.isVisible();
//
//        // Toggle the visibility and manage state
//        aiSidebar.setVisible(!currentlyVisible);
//        aiSidebar.setManaged(!currentlyVisible);
//
//        // Update the SplitPane divider position
//        if (!currentlyVisible) {
//            // Show the AI sidebar fully
//            splitPane.setDividerPositions(0.75); // Adjust this value to suit your layout
//        } else {
//            // Hide the AI sidebar
//            splitPane.setDividerPositions(1.0); // Fully collapse the sidebar
//        }
//    }

    @FXML private ToggleGroup viewToggleGroup;
    @FXML private RadioButton dayRadio;
    @FXML private RadioButton weekRadio;
    @FXML private RadioButton monthRadio;

    // === State ===
    private LocalDate currentDate = LocalDate.now();

    // === Initialization ===
    @FXML
    public void initialize() {
        // Load logo image
        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        logoImage.setImage(logo);

        // Set up toggle group
        viewToggleGroup = new ToggleGroup();
        dayRadio.setToggleGroup(viewToggleGroup);
        weekRadio.setToggleGroup(viewToggleGroup);
        monthRadio.setToggleGroup(viewToggleGroup);
        monthRadio.setSelected(true); // Default view

//        // Bind hgap and vgap to window size
//        calendarGrid.hgapProperty().bind(
//                Bindings.createDoubleBinding(() -> Math.max(calendarGrid.getWidth() / 50, 10), calendarGrid.widthProperty())
//        );
//        calendarGrid.vgapProperty().bind(
//                Bindings.createDoubleBinding(() -> Math.max(calendarGrid.getHeight() / 50, 10), calendarGrid.heightProperty())
//        );

        // Listen for view changes
        viewToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateCalendar();
            }
        });

        updateCalendar();
    }

    // === Navigation Buttons ===
    @FXML
    private void onPreviousMonth() {
        currentDate = currentDate.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void onNextMonth() {
        currentDate = currentDate.plusMonths(1);
        updateCalendar();
    }

    // === View Handlers ===
    @FXML
    private void onDayView(ActionEvent event) {
        weekView.setVisible(false);
        weekView.setManaged(false);
        dayView.setVisible(true);
        dayView.setManaged(true);
    }

    @FXML
    private void onWeekView() {
        dayView.setVisible(false);
        dayView.setManaged(false);
        weekView.setVisible(true);
        weekView.setManaged(true);
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

    // === Calendar Rendering hi harpi===
    private void updateCalendar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentDate.format(formatter));
        calendarGrid.getChildren().clear();

        // Add weekday headers
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            GridPane.setVgrow(dayLabel, Priority.ALWAYS);

            // Allow font size to change according to window size
            dayLabel.styleProperty().bind(
                    Bindings.createStringBinding(() -> {
                        double size = calendarGrid.getWidth() / 50;
                        return String.format(
                                "-fx-font-size: %.2fpx; -fx-font-weight: bold; -fx-text-fill: #444; -fx-border-color: #ccc; -fx-border-width: 0.5px; -fx-border-style: solid; -fx-alignment: center; -fx-background-color: #f2f2f2;",
                                size
                        );
                    }, calendarGrid.widthProperty())
            );

            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            GridPane.setVgrow(dayLabel, Priority.ALWAYS);
            calendarGrid.add(dayLabel, i, 0);
        }

        RadioButton selectedView = (RadioButton) viewToggleGroup.getSelectedToggle();
        String view = selectedView != null ? selectedView.getText() : "Month";

        switch (view) {
            case "Day": renderDayView(); break;
            case "Week": renderWeekView(); break;
            default: renderMonthView(); break;
        }
    }

    private void renderMonthView() {
        LocalDate firstDay = currentDate.withDayOfMonth(1);
        LocalDate lastDay = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        int startCol = (firstDay.getDayOfWeek().getValue() + 6) % 7; // Monday = 0, Sunday = 6
        int row = 1;
        int col = startCol;

        for (int day = 1; day <= lastDay.getDayOfMonth(); day++) {
            Label label = new Label(String.valueOf(day));
            label.setAlignment(Pos.CENTER);
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            // bind the font size to the grid cell width
            label.styleProperty().bind(
                    Bindings.createStringBinding(() -> {
                        double size = calendarGrid.getWidth() / 40;
                        return String.format(
                                "-fx-font-size: %.2fpx; -fx-text-fill: black; -fx-border-color: #ccc; -fx-border-width: 0.5px; -fx-border-style: solid; -fx-alignment: center;",
                                size
                        );
                    }, calendarGrid.widthProperty())
            );

            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            calendarGrid.add(label, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void renderWeekView() {
        calendarGrid.getChildren().clear(); // Clear previous content

        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);

        // Add day headers (Monday, Tuesday, etc.)
        for (int col = 1; col <= 7; col++) {
            LocalDate day = startOfWeek.plusDays(col - 1);
            Label dayLabel = new Label(day.getDayOfMonth() + " (" + day.getDayOfWeek().name().substring(0, 3) + ")");
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #eee; -fx-border-color: #ccc; -fx-border-width: 0.5px;");
            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            GridPane.setVgrow(dayLabel, Priority.ALWAYS);
            calendarGrid.add(dayLabel, col, 0);
        }

        // Add hour labels on the side (00:00 to 23:00)
        for (int row = 1; row <= 24; row++) {
            Label hourLabel = new Label(String.format("%02d:00", row - 1));
            hourLabel.setAlignment(Pos.CENTER_RIGHT);
            hourLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            hourLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
            GridPane.setHgrow(hourLabel, Priority.ALWAYS);
            GridPane.setVgrow(hourLabel, Priority.ALWAYS);
            calendarGrid.add(hourLabel, 0, row);
        }

        // Add cells for each hour of each day
        for (int col = 1; col <= 7; col++) {
            for (int row = 1; row <= 24; row++) {
                Label cell = new Label();
                cell.setAlignment(Pos.CENTER);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                // Bind font size to the width of the grid
                cell.styleProperty().bind(
                        Bindings.createStringBinding(() -> {
                            double size = calendarGrid.getWidth() / 50;
                            return String.format(
                                    "-fx-font-size: %.2fpx; -fx-border-color: #ccc; -fx-border-width: 0.5px; -fx-border-style: solid; -fx-alignment: center;",
                                    size
                            );
                        }, calendarGrid.widthProperty())
                );

                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);
                calendarGrid.add(cell, col, row);
            }
        }
    }

    private void renderDayView() {
        dayGrid.getChildren().clear();

        for (int hour = 0; hour < 24; hour++) {
            // Create the time label (left side)
            Label timeLabel = new Label(String.format("%02d:00", hour));
            timeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            timeLabel.setPrefHeight(40);
            timeLabel.setMaxWidth(Double.MAX_VALUE);
            timeLabel.setAlignment(Pos.CENTER_RIGHT);

            // Create the event slot (right side)
            Label eventSlot = new Label(); // Empty for now
            eventSlot.setPrefHeight(40);
            eventSlot.setMaxWidth(Double.MAX_VALUE);
            eventSlot.setStyle("-fx-border-color: #ccc; -fx-border-width: 0 0 1px 0;"); // Bottom line only
            eventSlot.setAlignment(Pos.CENTER_LEFT);

            // Add both to the grid
            dayGrid.add(timeLabel, 0, hour); // Column 0 = Time
            dayGrid.add(eventSlot, 1, hour); // Column 1 = Event

            // These are now optional, because column constraints handle the stretching
            // GridPane.setHgrow(timeLabel, Priority.ALWAYS);
            GridPane.setHgrow(eventSlot, Priority.ALWAYS);
        }
    }
//    private void populateDayView() {
//        dayGrid.getChildren().clear();
//
//        for (int hour = 0; hour < 24; hour++) {
//            Label timeLabel = new Label(String.format("%02d:00", hour));
//            timeLabel.setStyle("-fx-font-weight: bold;");
//            Label eventSlot = new Label(); // Empty cell for now
//
//            eventSlot.setPrefHeight(40);
//            eventSlot.setStyle("-fx-border-color: #ccc;");
//
//            dayGrid.add(timeLabel, 0, hour); // Time
//            dayGrid.add(eventSlot, 1, hour); // Event cell
//        }
//    }
}
