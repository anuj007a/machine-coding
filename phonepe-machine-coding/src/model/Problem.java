package model;

import java.util.List;

public class Problem {
    private int id;
    private String description;
    private List<Tag> tags;
    private String difficultyLevel;
    private int score;
    private int numberOfSolvers;
    private double totalSolveTime; // Total time taken by all solvers

    public Problem(String description, List<Tag> tags, String difficultyLevel, int score) {
        this.description = description;
        this.tags = tags;
        this.difficultyLevel = difficultyLevel;
        this.score = score;
        this.numberOfSolvers = 0;
        this.totalSolveTime = 0.0;
    }

    public String getDescription() {
        return description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getScore() {
        return score;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfSolvers() {
        return numberOfSolvers;
    }

    public double getAverageSolveTime() {
        if (numberOfSolvers == 0) return 0;
        return totalSolveTime / numberOfSolvers;
    }

    public void addSolver(double timeTaken) {
        numberOfSolvers++;
        totalSolveTime += timeTaken;
    }
}
