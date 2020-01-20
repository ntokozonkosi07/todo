/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.service;

import academy.learnprogramming.entity.Todo;
import academy.learnprogramming.entity.User;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Seeraj
 */
@Stateless
public class TodoService {

    @Inject
    EntityManager entityManager;

    @Inject
    QueryService queryService;

    @Inject
    SecurityUtil securityUtil;

    private String email;

    @Context
    private SecurityContext securityContext;

    public User saveUser(User user) {
//        BigInteger count = (BigInteger)queryService.countUserByEmail(user.getEmail()).get(0);

        if (user.getId() == null) {
            Map<String, String> credMap = securityUtil.hashPassword(user.getPassword());

            user.setPassword(credMap.get(SecurityUtil.HASHED_PASSWORD_KEY));
            user.setSalt(credMap.get(SecurityUtil.SALT_KEY));

            entityManager.persist(user);
            credMap.clear();
        }

        return user;
    }

    public Todo createTodo(Todo todo, String email) {
        User user = queryService.findUserByEmail(email);

        if (user != null) {
            todo.setUser(user);
            entityManager.persist(todo);
        }

        return todo;
    }

    public Todo updateTodo(Todo todo) {
        entityManager.merge(todo);
        return todo;
    }

    public Todo findToDoById(Long id) {
        return entityManager.find(Todo.class, id);
    }

    public List<Todo> getTodos() {
        return entityManager.createQuery("SELECT t from Todo t", Todo.class).getResultList();
    }
}
