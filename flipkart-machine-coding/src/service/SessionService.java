package service;

public interface SessionService {
    public void login(String userId) throws Exception;
    public void logout(String userId) throws Exception;
}
