package com.entoo.demo;

import com.entoo.demo.dao.UserDao;
import com.entoo.demo.document.Count;
import com.entoo.demo.document.Result;
import com.entoo.demo.document.User;
import com.entoo.demo.repository.UserRepository;
import com.entoo.demo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @MockBean
    private UserDao dao;
    @MockBean
    private UserRepository repository;
    @MockBean
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserService userService;

    @Test
    public void loginPass() {
        User user = new User();
        user.setPhone("1234567890");
        user.setPassword("123");
        User user1 = new User();
        user1.setPassword("123");
        Mockito.when(repository.findByPhone(user.getPhone())).thenReturn(Optional.of(user1));
        var res = userService.loginUser(user);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void loginPasswordFail() {
        User user = new User();
        user.setPhone("12345678");
        user.setPassword("14423");

        User user1 = new User();
        user1.setPassword("123");

        Mockito.when(repository.findByPhone(user.getPhone())).thenReturn(Optional.of(user1));
        var res = userService.loginUser(user);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void loginFail() {
        User user = new User();
        user.setPhone("1234567890");
        user.setPassword("123");
        Mockito.when(repository.findByPhone(user.getPhone())).thenReturn(Optional.empty());
        var res = userService.loginUser(user);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void loginException() {
        User user = new User();
        Mockito.when(repository.findByPhone(user.getPhone())).thenReturn(null);
        var res = userService.loginUser(user);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, res.getStatusCode());
    }

    @Test
    public void signupPass() {
        User user = new User();
        user.setName("m");
        user.setEmail("m@123");
        user.setPhone("65432333");
        user.setPassword("123456789");
        Mockito.when(repository.findByPhone(user.getPhone())).thenReturn(Optional.empty());
        var res = userService.signupUser(user);
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());
    }

    @Test
    public void signupFail() {
        User user = new User();
        user.setName("m");
        user.setEmail("m@123");
        user.setPhone("12345678");
        user.setPassword("123456789");
        User user1 = new User();
        user1.setName("m");
        user1.setEmail("m@123");
        user1.setPhone("1234534567");
        user1.setPassword("123456789");
        Mockito.when(repository.findByPhone(user.getPhone())).thenReturn(Optional.of(user1));
        var res = userService.signupUser(user);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }
    Result getResultInstance(){
        User user = new User();
        user.setName("m");
        user.setEmail("m@123");
        user.setPhone("12345678");
        user.setPassword("123456789");
        User user1 = new User();
        user1.setName("m");
        user1.setEmail("m@123");
        user1.setPhone("1234534567");
        user1.setPassword("123456789");
        List<User> l= new ArrayList<>();
        l.add(user);
        l.add(user1);
        Count count = new Count();
        count.setCount(13);
        Result result = new Result();
        result.setProjectionFacet(l);
        result.setCountFacet(count);
        return result;
    }
    @Test
    public void allAggregationData() {
        int itemsPerPage= 5;
        int pageNo = 1;
        int limit = 5;
        int skip =  0;
        Mockito.when(repository.findAll(limit, skip)).thenReturn(getResultInstance());
        var res = userService.allAggregationData(pageNo,itemsPerPage);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }
}
