package com.entoo.demo.service;

import com.entoo.demo.Model.AlertData;
import com.entoo.demo.Model.AlertWithPagination;
import com.entoo.demo.Model.Report;
import com.entoo.demo.document.Count;
import com.entoo.demo.document.Result;
import com.entoo.demo.document.User;
import com.entoo.demo.repository.PenaltyRepo;
import com.entoo.demo.repository.UserRepository;
import com.entoo.demo.repository.VehicleAlertRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @MockBean
    private UserRepository repository;
    @MockBean
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserService userService;
    @MockBean
    private PenaltyRepo penaltyRepo;
    @MockBean
    private VehicleAlertRepo repos;

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

    Result getResultInstance() {
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
        List<User> l = new ArrayList<>();
        l.add(user);
        l.add(user1);
        Count count = new Count();
        count.setCount(13);
        Result result = new Result();
//        result.setProjectionFacet(l);
        result.setCountFacet(List.of(count));
        return result;
    }

    @Test
    public void allAggregationData() {
        int itemsPerPage = 5;
        int pageNo = 1;
        int limit = 5;
        int skip = 0;
        Mockito.when(repository.findAll(limit, skip)).thenReturn(getResultInstance());
        var res = userService.allAggregationData(pageNo, itemsPerPage);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void changePass1() {
//      acceptable found
        User user = new User();
        user.setEmail("manjeet79@gmail.com");
        user.setPhone("6230162697");
        user.setPassword("Manjeet@123");

        String email = "manjeet79@gmail.com";
        String oldPass = "Manjeet@123";
        String newPass = "Madhav@123";
        Mockito.when(repository.findByEmail(email)).thenReturn(user);
        var res = userService.changePass(email, oldPass, newPass);
        Assertions.assertEquals(HttpStatus.ACCEPTED, res.getStatusCode());
    }

    @Test
    public void changePass1_1() {
//      not implemented found
        User user = new User();
        user.setEmail("manjeet79@gmail.com");
        user.setPhone("6230162697");
        user.setPassword("Manjeet@123");

        String email = "manjeet79@gmail.com";
        String oldPass = "Manjeet@123";
        String newPass = "Manjeet@123";
        Mockito.when(repository.findByEmail(email)).thenReturn(user);
        var res = userService.changePass(email, oldPass, newPass);
        Assertions.assertEquals(HttpStatus.NOT_IMPLEMENTED, res.getStatusCode());
    }

    @Test
    public void changePass1_2() {
//      not acceptable found
        User user = new User();
        user.setEmail("manjeet79@gmail.com");
        user.setPhone("6230162697");
        user.setPassword("Manjeet@123");

        String email = "manjeet79@gmail.com";
        String oldPass = "Manjee@123";
        String newPass = "Manjeet@123";
        Mockito.when(repository.findByEmail(email)).thenReturn(user);
        var res = userService.changePass(email, oldPass, newPass);
        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, res.getStatusCode());
    }

    @Test
    public void changePass2() {
//        Not found
        String email = "manjeet79@gmail.com";
        String oldPass = "Manjeet@123";
        String newPass = "Madhav@123";
        Mockito.when(repository.findByEmail(email)).thenReturn(null);
        var res = userService.changePass(email, oldPass, newPass);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void getAlertData() {
        int pageNo = 1;
        int itemsPerPage = 2;
        String date = "2024-09-12";
        var aggregationResultsMock = mock(AggregationResults.class);
        when(aggregationResultsMock.getMappedResults()).thenReturn(getAggregationResult());
        Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class), Mockito.eq("vehicle_alerts"), Mockito.eq(Result.class));
        var res = userService.getAlertData(pageNo, itemsPerPage, date);
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }
    @Test
    public void getAlertWithoutData()
    {
        int pageNo = 1;
        int itemsPerPage = 2;
        String date = "2024-09-12";
        var aggregationResultsMock = mock(AggregationResults.class);
        when(aggregationResultsMock.getMappedResults()).thenReturn(new ArrayList<>());
        Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class), Mockito.eq("vehicle_alerts"), Mockito.eq(Result.class));
        var res = userService.getAlertData(pageNo, itemsPerPage, date);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
    @Test
    public void getAlertDataException() {
        int pageNo = 0;
        int itemsPerPage = 0;
        String date = "2024-09-12";
        var aggregationResultsMock = mock(AggregationResults.class);
        when(aggregationResultsMock.getMappedResults()).thenReturn(null);
        Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class), Mockito.eq("vehicle_alerts"), Mockito.eq(Result.class));
        var res = userService.getAlertData(pageNo, itemsPerPage, date);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, res.getStatusCode());
    }

    private ArrayList<Result> getAggregationResult() {
        ArrayList<Result> resultList = new ArrayList<>();
        ArrayList<Report> reportList = new ArrayList<>();
        ArrayList<AlertData> alertData = new ArrayList<>();

        Report report1 = new Report();
        report1.setVehicleNo("KA01JS2916");

        AlertData al1 = new AlertData();
        al1.setAlertName("theft");
        al1.setCount(11);
        alertData.add(al1);

        report1.setData(alertData);

        reportList.add(report1);
        Result result = new Result();
        result.setProjectionFacet(reportList);
        Count count = new Count();
        count.setCount(10);
        result.setCountFacet(List.of(count));
        resultList.add(result);
        return resultList;
    }
}
