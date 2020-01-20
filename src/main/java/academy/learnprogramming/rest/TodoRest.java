/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.rest;

import academy.learnprogramming.entity.Todo;
import academy.learnprogramming.service.QueryService;
import academy.learnprogramming.service.TodoService;

import java.util.Collection;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author siya
 */
@Path("todo")
@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
@Auth
public class TodoRest {

    @Inject
    TodoService todoService;

    @Inject
    QueryService queryService;

    @Context
    private SecurityContext securityContext;

    @Path("new")
    @POST
    public Response createTodo(Todo todo) {
        todoService.createTodo(todo, this.securityContext.getUserPrincipal().getName());

        return Response.ok(todo).build();
    }

    @Path("update")
    @PUT
    public Response updateTodo(Todo todo) {
        todoService.updateTodo(todo);
        return Response.ok(todo).build();
    }

    @GET
    @Path("{id}")
    public Response getTodo(@PathParam("id") Long id) {
        Todo todo = queryService.findTodoById(id);
        return Response.ok(todo).build();
    }

    @GET
    @Path("list")
    public Response getTodos() {
        Collection<Todo> todos = queryService.getAllTodos(this.securityContext.getUserPrincipal().getName());

        return Response.ok(todos).build();
    }

    @Path("status")
    @POST
    public Response markAsCompete(@QueryParam("id") Long id) {
        Todo todo = queryService.findTodoById(id);
        todo.setIsCompleted(true);

        Todo updateTodo = todoService.updateTodo(todo);
        return Response.ok(updateTodo).build();
    }
}
