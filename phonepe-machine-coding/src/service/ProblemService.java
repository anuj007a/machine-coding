package service;

import model.Problem;
import model.User;
import java.util.List;
import java.util.Map;

public interface ProblemService {
    void addProblem(Problem problem);
    void addUser(User user);
    List<Problem> fetchProblems(Map<String, List<String>> filters, String sortBy);
    List<Problem> solveProblem(String username, Problem problem, double timeTaken);
    List<Problem> fetchSolvedProblems(String username); // User can fetch problems they have solved
    List<Problem> recommendProblems(String username); // Fetch recommended problems based on user's history
    List<User> displayLeaderboardUsers(int topN);
    List<Map.Entry<String, Integer>> displayLeaderboardDepartments(int topN);
    Problem getProblemStats(int problemId); // Get number of solvers and average solve time
}
