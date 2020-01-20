/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.service;

import academy.learnprogramming.entity.Todo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import academy.learnprogramming.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * @author siya
 */
@Stateless
public class QueryService {

    @Inject
    EntityManager entityManager;

    @Inject
    SecurityUtil securityUtil;

    @Context
    private SecurityContext securityContext;

    public User findUserByEmail(String email) {

        List<User> userList = entityManager.createNamedQuery(User.FIND_USER_BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();

        if(!userList.isEmpty()){
            return userList.get(0);
        }

        return null;
    }

    public List countUserByEmail(String email) {
        return entityManager.createNativeQuery("select count(id) from TodoUser where exists (select id from TodoUser where email = ?)")
                .setParameter(1, email)
                .getResultList();
    }

    public boolean authenticateUser(String email, String password) {
        User user = findUserByEmail(email);

        if (user == null) {
            return false;
        }

        return securityUtil.passwordMatch(user.getPassword(), user.getSalt(), password);
    }

    public Todo findTodoById(Long id) {
        List<Todo> resultsList = entityManager.createNamedQuery(Todo.FIND_TODO_BY_ID, Todo.class)
                .setParameter("id", id)
                .setParameter("email", securityContext.getUserPrincipal().getName())
                .getResultList();

        if (resultsList.isEmpty()) {
            return null;
        }

        return resultsList.get(0);
    }

    public Collection getAllTodos(String email) {
        return entityManager.createNamedQuery(Todo.FIND_TODO_BY_USER, Todo.class)
                .setParameter("email", email)
                .getResultList();
    }

    public Collection getAllTodosByTask(String task) {
        return entityManager.createNamedQuery(Todo.FIND_TODO_BY_TASK, Todo.class)
                .setParameter("email", securityContext.getUserPrincipal().getName())
                .setParameter("task", "%" + task + "%")
                .getResultList();
    }
}
