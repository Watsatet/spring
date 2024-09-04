package com.entoo.demo.controller;

import java.util.List;

import com.entoo.demo.document.User;
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
}
