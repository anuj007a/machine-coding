package repository;

import model.Problem;

import java.util.*;

public class ProblemRepository {
    private Map<Integer, Problem> problems = new HashMap<>();
    private int currentId = 1; // Simple mechanism to assign IDs to problems

    public void addProblem(Problem problem) {
        problem.setId(currentId);  // Set the problem's ID
        problems.put(currentId, problem);
        currentId++;
    }

    public Optional<Problem> getProblemById(int problemId) {
        return Optional.ofNullable(problems.get(problemId));
    }

    public List<Problem> getAllProblems() {
        return new ArrayList<>(problems.values());
    }
}
