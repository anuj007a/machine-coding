package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String name;
    private String department;
    private List<Problem> solvedProblems;

    public User(String username, String name, String department) {
        this.username = username;
        this.name = name;
        this.department = department;
        this.solvedProblems = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public List<Problem> getSolvedProblems() {
        return solvedProblems;
    }

    public void solveProblem(Problem problem, double timeTaken) {
        solvedProblems.add(problem);
        problem.addSolver(timeTaken);
    }

    public int getTotalScore() {
        return solvedProblems.stream().mapToInt(Problem::getScore).sum();
    }
}
