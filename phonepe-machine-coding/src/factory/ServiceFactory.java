package factory;

import repository.ProblemRepository;
import repository.UserRepository;
import service.ProblemService;
import service.impl.ProblemServiceImpl;

public class ServiceFactory {
    private static ProblemRepository problemRepository = new ProblemRepository();
    private static UserRepository userRepository = new UserRepository();

    public static ProblemService getProblemService() {
        return new ProblemServiceImpl(problemRepository, userRepository);
    }
}