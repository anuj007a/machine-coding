package service.impl;

import model.Problem;
import model.Tag;
import model.User;
import repository.ProblemRepository;
import repository.UserRepository;
import service.ProblemService;

import java.util.*;
import java.util.stream.Collectors;

public class ProblemServiceImpl implements ProblemService {
    private ProblemRepository problemRepository;
    private UserRepository userRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository, UserRepository userRepository) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addProblem(Problem problem) {
        problemRepository.addProblem(problem);
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }

    @Override
    public List<Problem> fetchProblems(Map<String, List<String>> filters, String sortBy) {
        return problemRepository.getAllProblems().stream()
                .filter(problem -> filters == null || filters.keySet().stream()
                        .allMatch(key -> problemMatchesFilters(problem, key, filters.get(key))))
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }

    @Override
    public List<Problem> solveProblem(String username, Problem problem, double timeTaken) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.solveProblem(problem, timeTaken);
        problem.addSolver(timeTaken); // Make sure to add solver data to problem
        return recommendProblems(username);
    }

    @Override
    public List<Problem> recommendProblems(String username) {
        User user = userRepository.getUserByUsername(username);
        Set<Tag> tags = user.getSolvedProblems().stream()
                .flatMap(p -> p.getTags().stream())
                .collect(Collectors.toSet());
        return problemRepository.getAllProblems().stream()
                .filter(p -> p.getTags().stream().anyMatch(tags::contains) && !user.getSolvedProblems().contains(p))
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public List<Problem> fetchSolvedProblems(String username) {
        User user = userRepository.getUserByUsername(username);
        return new ArrayList<>(user.getSolvedProblems());
    }

    @Override
    public Problem getProblemStats(int problemId) {
        return problemRepository.getProblemById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
    }

    @Override
    public List<User> displayLeaderboardUsers(int topN) {
        return userRepository.getAllUsers().stream()
                .sorted(Comparator.comparingInt(User::getTotalScore).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map.Entry<String, Integer>> displayLeaderboardDepartments(int topN) {
        Map<String, Integer> departmentScores = new HashMap<>();
        userRepository.getAllUsers().forEach(user ->
                departmentScores.merge(user.getDepartment(), user.getTotalScore(), Integer::sum));
        return departmentScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .collect(Collectors.toList());
    }


    private boolean problemMatchesFilters(Problem problem, String key, List<String> values) {
        switch (key.toLowerCase()) {
            case "difficulty":
                return values.contains(problem.getDifficultyLevel());
            case "tag":
                return problem.getTags().stream()
                        .map(Enum::toString)
                        .anyMatch(values::contains);
            default:
                return true;
        }
    }

    private Comparator<Problem> getComparator(String sortBy) {
        switch (sortBy) {
            case "score":
                return Comparator.comparingInt(Problem::getScore).reversed();
            default:
                return Comparator.comparing(Problem::getDescription);
        }
    }
}
