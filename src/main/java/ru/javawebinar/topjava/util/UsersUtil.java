package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UsersUtil {

    Set<Role> roleSet = new HashSet<>();

    public static final List<User> users = Arrays.asList(
            new User(null, "Alex", "email0@gmail.com", "qwerty0", 2000,
                    true,new HashSet<Role>(){{add(Role.ADMIN);}}),
            new User(null, "Marty", "email1@gmail.com", "qwerty1", 1500,
                             true,new HashSet<Role>(){{add(Role.USER);}}),
            new User(null, "Melman", "email2@gmail.com", "qwerty2", 1000,
                    true,new HashSet<Role>(){{add(Role.USER);}}),
            new User(null, "Glory", "email3@gmail.com", "qwerty3", 5000,
                             true,new HashSet<Role>(){{add(Role.USER);}})
    );


}
