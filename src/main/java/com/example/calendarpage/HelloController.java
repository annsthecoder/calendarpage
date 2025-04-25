package com.example.calendarpage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelloController {

    // === FXML UI Elements ===
    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private GridPane weekGrid;
    @FXML private GridPane dayGrid;

    @FXML private VBox weekView;
    @FXML private VBox dayView;

    @FXML private ToggleGroup viewToggleGroup;
    @FXML private RadioButton dayRadio;
    @FXML private RadioButton weekRadio;
    @FXML private RadioButton monthRadio;

    // === State ===
    private LocalDate currentDate = LocalDate.now();

    // === Initialization ===
    @FXML
    public void initialize() {
        // Set up toggle group
        viewToggleGroup = new ToggleGroup();
        dayRadio.setToggleGroup(viewToggleGroup);
        weekRadio.setToggleGroup(viewToggleGroup);
        monthRadio.setToggleGroup(viewToggleGroup);
        monthRadio.setSelected(true); // Default view

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
        populateDayView();
    }

    @FXML
    private void onWeekView() {
        dayView.setVisible(false);
        dayView.setManaged(false);
        weekView.setVisible(true);
        weekView.setManaged(true);
    }

    // === Calendar Rendering ===
    private void updateCalendar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentDate.format(formatter));
        calendarGrid.getChildren().clear();

        // Add weekday headers
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
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

        int startCol = firstDay.getDayOfWeek().getValue() % 7; // Make Monday = 0
        int row = 1;
        int col = startCol;

        for (int day = 1; day <= lastDay.getDayOfMonth(); day++) {
            Label label = new Label(String.valueOf(day));
            calendarGrid.add(label, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void renderWeekView() {
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        int row = 1;

        for (int col = 0; col < 7; col++) {
            LocalDate day = startOfWeek.plusDays(col);
            Label dayLabel = new Label(day.getDayOfMonth() + " (" + day.getDayOfWeek().name().substring(0, 3) + ")");
            calendarGrid.add(dayLabel, col, row);

            for (int i = 0; i < 24; i++) {
                String hour = (i < 12) ? String.format("%02d AM", i == 0 ? 12 : i) : String.format("%02d PM", i == 12 ? 12 : i - 12);
                Label hourLabel = new Label(hour);
                calendarGrid.add(hourLabel, col, i + 2);
            }
        }
    }

    private void renderDayView() {
        // Optional placeholder for label
        Label label = new Label(currentDate.getDayOfWeek().name() + " " + currentDate.getDayOfMonth());
        calendarGrid.add(label, 0, 1);
    }

    private void populateDayView() {
        dayGrid.getChildren().clear();

        for (int hour = 0; hour < 24; hour++) {
            Label timeLabel = new Label(String.format("%02d:00", hour));
            timeLabel.setStyle("-fx-font-weight: bold;");
            Label eventSlot = new Label(); // Empty cell for now

            eventSlot.setPrefHeight(40);
            eventSlot.setStyle("-fx-border-color: #ccc;");

            dayGrid.add(timeLabel, 0, hour); // Time
            dayGrid.add(eventSlot, 1, hour); // Event cell
        }
    }
}
