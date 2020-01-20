/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

/**
 *
 * @author siya
 */
@Entity
@NamedQuery(name = Todo.FIND_TODO_BY_TASK, query = "select t from Todo t where t.user.Id = :id and t.task like :task")
@NamedQuery(name = Todo.FIND_TODO_BY_USER, query = "select t from Todo t where t.user.email = :email")
@NamedQuery(name = Todo.FIND_TODO_BY_ID, query = "select t from Todo t where t.Id = :id and t.user.email = :email")
public class Todo extends AbstractEntity {
    
    public static final String FIND_TODO_BY_TASK = "todo.findTodoByTask";
    public static final String FIND_TODO_BY_USER = "todo.findTodoByUser";
    public static final String FIND_TODO_BY_ID = "todo.findTodoById";
    
    @NotEmpty(message = "Task must be set")
    @Size(min=10, message="Task should not be less than 10")
    private String task;
    
    @NotNull(message = "Task must be set")
    @FutureOrPresent(message = "due date must be present or in future")
    @JsonbDateFormat(value = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dueDate;
    
    private boolean isCompleted;
    
    @ManyToOne()
    private User user;

    @FutureOrPresent(message = "due date must be present or in future")
    private LocalDate dateCompleted;
    
    @PastOrPresent(message = "date created must be in the past or present")
    private LocalDate dateCreated;


    @PrePersist
    private void init(){
        setDateCreated(LocalDate.now());
    }
    
    public String getTask() {
        return task;
    }

    
    public void setTask(String task) {
        this.task = task;
    }

    
    public LocalDate getDueDate() {
        return dueDate;
    }

    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    
    public boolean isIsCompleted() {
        return isCompleted;
    }

    
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    
    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    
    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
