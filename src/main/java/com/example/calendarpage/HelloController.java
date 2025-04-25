package com.example.calendarpage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HelloController {
    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;
    @FXML private ToggleGroup viewToggleGroup;  // Declared here, initialized in initialize() method
    @FXML private RadioButton dayRadio;
    @FXML private RadioButton weekRadio;
    @FXML private RadioButton monthRadio;

    private LocalDate currentDate;

    public HelloController() {
        currentDate = LocalDate.now();
    }

    @FXML
    public void initialize() {
        // Ensure ToggleGroup is properly initialized here
        viewToggleGroup = new ToggleGroup();  // Initialize ToggleGroup

        dayRadio.setToggleGroup(viewToggleGroup);  // Bind ToggleGroup to RadioButton
        weekRadio.setToggleGroup(viewToggleGroup);
        monthRadio.setToggleGroup(viewToggleGroup);

        // Optionally set default selection
        monthRadio.setSelected(true);

        // Add a listener to handle toggle changes
        viewToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                RadioButton selected = (RadioButton) newVal;
                String view = selected.getText();
                System.out.println("Selected view: " + view);
                updateCalendar(); // Updates calendar view based on selection
            }
        });

        updateCalendar();
    }

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

    private void updateCalendar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentDate.format(formatter));
        calendarGrid.getChildren().clear();

        // Re-add day labels (Mon to Sun)
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            calendarGrid.add(dayLabel, i, 0);
        }

        RadioButton selectedView = (RadioButton) viewToggleGroup.getSelectedToggle();
        String view = selectedView != null ? selectedView.getText() : "Month";

        switch (view) {
            case "Day":
                renderDayView();
                break;
            case "Week":
                renderWeekView();
                break;
            default:
                renderMonthView();
        }
    }

    private void renderMonthView() {
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int row = 1;
        int col = dayOfWeek - 1;

        for (int day = 1; day <= lastDayOfMonth.getDayOfMonth(); day++) {
            Label dayLabel = new Label(String.valueOf(day));
            calendarGrid.add(dayLabel, col, row);

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
        }
    }

    private void renderDayView() {
        Label label = new Label(currentDate.getDayOfWeek().name() + " " + currentDate.getDayOfMonth());
        calendarGrid.add(label, 0, 1);
    }
}
