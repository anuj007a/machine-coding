package com.wraith.factory;


import com.wraith.repository.ProblemRepository;
import com.wraith.repository.UserRepository;
import com.wraith.service.ProblemService;
import com.wraith.service.impl.ProblemServiceImpl;

public class ServiceFactory {
    private static ProblemRepository problemRepository = new ProblemRepository();
    private static UserRepository userRepository = new UserRepository();

    public static ProblemService getProblemService() {
        return new ProblemServiceImpl(problemRepository, userRepository);
    }
}