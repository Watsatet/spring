package com.entoo.demo.controller;

import java.util.Date;
import java.util.List;

import com.entoo.demo.document.User;
import com.entoo.demo.repository.PaginatorAndDetails;
import com.entoo.demo.response.ResponseStructure;
import com.entoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure> loginUser(@RequestBody User user) {
        return service.loginUser(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseStructure> signupUser(@RequestBody User user) {
        return service.signupUser(user);
    }

    @GetMapping("/changePass/{email}")
    public ResponseEntity<ResponseStructure<User>> changePass(@PathVariable String email, @RequestParam String oldPass, @RequestParam String newPass) {
        return service.changePass(email, oldPass, newPass);
    }

    @GetMapping("/users")
    public List<User> users() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Madhav"));
        return mongoTemplate.find(query, User.class);
    }

//    @GetMapping("/aggregationData")
//    public ResponseEntity<ResponseStructure> aggregationData(@RequestParam String email) {
//        return service.aggregationData(email);
//    }

    @GetMapping("/allAggregationData")
    public ResponseEntity<ResponseStructure> allAggregationData(@RequestParam int pageNo, int itemsPerPage) {
        return service.allAggregationData(pageNo, itemsPerPage);
    }

    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<ResponseStructure> deleteUser(@PathVariable(name = "email") String email) {
        return service.deleteByEmail(email);
    }
    @GetMapping("/getData")
    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getAggData(@RequestParam String search, @RequestParam int pageNo, @RequestParam int itemsPerPage) {
        return service.getAggData(search,pageNo,itemsPerPage);
    }
    @GetMapping("/getData1")
    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getAggeData(@RequestParam String search, @RequestParam int pageNo, @RequestParam int itemsPerPage) {
        return service.getAggeData(search,pageNo,itemsPerPage);
    }
    @GetMapping("/getDeductionData")
    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getDeductionData() {
        return service.getDeductionData();
    }
    @GetMapping("/getAlertData")
    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getAlertData(int pageNo, int itemsPerPage, @RequestParam String date) {
        return service.getAlertData(pageNo,itemsPerPage,date);
    }
}