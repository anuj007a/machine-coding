package main.java.com.todo.taskmanager.Utils;

import java.util.UUID;

public class IdGenerator {

    public String getId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
