package driver;

import model.Problem;
import model.Tag;
import model.User;
import service.ProblemService;
import factory.ServiceFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ProblemService problemService = ServiceFactory.getProblemService();

        // Adding a comprehensive list of problems
        problemService.addProblem(new Problem("Solve 2+2", Arrays.asList(Tag.MATH), "easy", 10));
        problemService.addProblem(new Problem("Simplify: 133 – 19 × 2 + 15", Arrays.asList(Tag.MATH), "easy", 10));
        problemService.addProblem(new Problem("Simplify the numerical expression: [36 ÷ (-9)] ÷ [(-24) ÷ 6]", Arrays.asList(Tag.MATH), "medium", 50));
        problemService.addProblem(new Problem("If z1 = 2 + 8i and z2 = 1 – i, then find |z1/z2|.", Arrays.asList(Tag.MATH), "hard", 100));
        problemService.addProblem(new Problem("Implement LinkedList", Arrays.asList(Tag.DATA_STRUCTURE), "medium", 50));
        problemService.addProblem(new Problem("Optimize QuickSort", Arrays.asList(Tag.ALGORITHM), "hard", 100));
        problemService.addProblem(new Problem("Binary Search", Arrays.asList(Tag.ALGORITHM), "easy", 20));
        problemService.addProblem(new Problem("Fibonacci Sequence", Arrays.asList(Tag.MATH), "medium", 40));
        problemService.addProblem(new Problem("Graph Traversal", Arrays.asList(Tag.DATA_STRUCTURE), "hard", 80));
        problemService.addProblem(new Problem("Selection Sort", Arrays.asList(Tag.ALGORITHM), "medium", 50));
        problemService.addProblem(new Problem("Linear Search", Arrays.asList(Tag.ALGORITHM), "easy", 10));

        // Adding users
        User user1 = new User("rahul_1", "Rahul", "Engineering");
        User user2 = new User("akash_1", "Akash", "Science");
        User user3 = new User("amit_1", "Amit", "Engineering");
        problemService.addUser(user1);
        problemService.addUser(user2);
        problemService.addUser(user3);

        // Sample operation for each user
        User[] users = {user1, user2, user3};
        for (User user : users) {

            System.out.println("==================================> "+ user.getName() + " <==================================");
            // Filtering problems for the user
            Map<String, List<String>> filters = new HashMap<>();
            filters.put("difficulty", Arrays.asList("easy", "medium"));

            // filter and print the problems as per above filter
            List<Problem> filteredProblems = problemService.fetchProblems(filters, "score");
            System.out.println("Filtered Problems for " + user.getName() + ":");
            filteredProblems.forEach(p -> System.out.println(p.getDescription() + " - Score: " + p.getScore()));
//3
            // Solving the filtered problems and getting recommendations
            filteredProblems.forEach(p -> {
                List<Problem> recommendations = problemService.solveProblem(user.getUsername(), p, Math.random() * 20 + 5);
                System.out.println("Recommendations for " + user.getName() + " after solving " + p.getDescription() + ":");
                recommendations.forEach(rec -> System.out.println(rec.getDescription() + " - Score: " + rec.getScore()));
            });

            System.out.println("==================================> "+ user.getName() + " <==================================");



        }
        // Leaderboard display for users
        System.out.println("Leaderboard for users:");
        List<User> leaderboardUsers = problemService.displayLeaderboardUsers(3);
        leaderboardUsers.forEach(usr -> System.out.println(usr.getName() + " - Total Score: " + usr.getTotalScore()));

        // Leaderboard display for departments
        System.out.println("Leaderboard for departments:");
        List<Map.Entry<String, Integer>> leaderboardDepts = problemService.displayLeaderboardDepartments(3);
        leaderboardDepts.forEach(dept -> System.out.println(dept.getKey() + " Department - Total Score: " + dept.getValue()));

        try {
            Problem problemStats = problemService.getProblemStats(1); // Get stats for problem with ID 1
            System.out.println("Problem: " + problemStats.getDescription());
            System.out.println("Solvers: " + problemStats.getNumberOfSolvers());
            System.out.println("Average Time Taken: " + problemStats.getAverageSolveTime() + " minutes");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
