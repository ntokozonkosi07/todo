package academy.learnprogramming.service;

import academy.learnprogramming.entity.Todo;
import academy.learnprogramming.entity.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TodoServiceTest {

    @Inject
    private User user;
    
    @Inject
    private TodoService todoService;   
    Logger logger;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "hello-todo.war")
                .addPackage(Todo.class.getPackage())
                .addPackage(TodoService.class.getPackage())
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {
        
        logger = Logger.getLogger(TodoServiceTest.class.getName());
        
        user.setEmail("bla@bla.com");
        user.setFullName("Donald Trump");
        user.setPassword("jDTrumpf@41");
        
        todoService.saveUser(user);
        logger.log(Level.INFO, "----------------------------------------------------------------------------");
    }

    @After
    public void tearDown() throws Exception {
        logger.log(Level.INFO, "----------------------------------------------------------------------------");
    }
    
    @Test
    public void SaveUser(){
        assertNotNull(user.getId());
        logger.log(Level.INFO, user.getId().toString());
        assertNotEquals("the user password is not the same as hashed",user.getPassword(), "jDTrumpf@41");
        logger.log(Level.INFO, user.getPassword());
    }
}
