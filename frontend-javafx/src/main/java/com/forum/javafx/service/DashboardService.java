package com.forum.javafx.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 * Simple data provider for the Dashboard. All fields are alterable so UI can reflect user input later.
 */
public class DashboardService {
    private static final DashboardService INSTANCE = new DashboardService();

    // High-level metrics
    public int totalTasksDone = 43;
    public int projectsStopped = 2;
    public int projectsTotal = 32;
    public int projectsInProgress = 2;
    public int projectsCompleted = 25;

    // Weekly process series (category axis: days)
    public final ObservableList<String> weekDays = FXCollections.observableArrayList("Mon","Tue","Wed","Thu","Fri","Sat");
    public final XYChart.Series<String, Number> workSeries = new XYChart.Series<>();
    public final XYChart.Series<String, Number> meditationSeries = new XYChart.Series<>();

    // Month progress (0..1)
    public double monthProgress = 0.30; // 30%
    public double workProgress = 1.20;  // 120% compared to last month
    public double meditationProgress = 0.80;
    public double projectsProgress = 0.95;

    // Events / Tasks / Last projects (strings for simplicity)
    public final ObservableList<String> monthEvents = FXCollections.observableArrayList(
            "Mid Sems", "PSS Assignment", "BEE Test", "End Sems"
    );

    public final ObservableList<String> tasksInProcess = FXCollections.observableArrayList(
            "Meet HR For Project", "Boss Appointment"
    );

    public final ObservableList<String> lastProjects = FXCollections.observableArrayList(
            "New Schedule", "Anime UI design", "Creative UI design"
    );

    private DashboardService() {
        workSeries.setName("Work");
        meditationSeries.setName("Meditation");
        // Sample values (alterable later)
        int[] workVals = {20, 35, 28, 40, 22, 30};
        int[] medVals  = {15, 10, 18, 12, 16, 14};
        for (int i = 0; i < weekDays.size(); i++) {
            workSeries.getData().add(new XYChart.Data<>(weekDays.get(i), workVals[i]));
            meditationSeries.getData().add(new XYChart.Data<>(weekDays.get(i), medVals[i]));
        }
    }

    public static DashboardService getInstance() { return INSTANCE; }
}
