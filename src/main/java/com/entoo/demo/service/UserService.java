package com.entoo.demo.service;

import com.entoo.demo.dao.UserDao;
import com.entoo.demo.document.Result;
import com.entoo.demo.document.User;
import com.entoo.demo.pagination.PageStructure;
import com.entoo.demo.repository.PaginatorAndDetails;
import com.entoo.demo.repository.UserRepository;
import com.entoo.demo.response.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserDao dao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository repository;

    public ResponseEntity<ResponseStructure> loginUser(User user) {
        ResponseStructure rs = new ResponseStructure();
        try {
            Optional<User> optional = repository.findByPhone(user.getPhone());
            if (optional.isPresent()) {
                if (user.getPassword().equals(optional.get().getPassword())) {
                    rs.setHttpStatus(HttpStatus.OK.value());
                    rs.setMessage("Login successfully....");

                    return new ResponseEntity<ResponseStructure>(rs, HttpStatus.OK);
                } else {
                    rs.setHttpStatus(HttpStatus.BAD_REQUEST.value());
                    rs.setMessage("password incorrect....");
//                    rs.setBody("enter correct password..");
                    return new ResponseEntity<ResponseStructure>(rs, HttpStatus.BAD_REQUEST);
                }
            } else {
                rs.setHttpStatus(HttpStatus.NOT_FOUND.value());
                rs.setMessage("user not found....");
//                rs.setBody("do signup..");
                return new ResponseEntity<ResponseStructure>(rs, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            rs.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            rs.setMessage("user not found....");
//            rs.setBody("do signup..");
            return new ResponseEntity<ResponseStructure>(rs, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<ResponseStructure> signupUser(User user) {
        Optional<User> optional = repository.findByPhone(user.getPhone());
        ResponseStructure rs = new ResponseStructure();
        if (optional.isEmpty()) {
            if (user.getPassword().matches("^.{8,15}$")) {
                rs.setHttpStatus(HttpStatus.CREATED.value());
                rs.setMessage("User created successfully...");
//                rs.setBody(dao.signupUser(user));
                dao.signupUser(user);
            }

            return new ResponseEntity<ResponseStructure>(rs, HttpStatus.CREATED);
        } else {
            rs.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            rs.setMessage("User already exists");
            rs.setBody(null);
            return new ResponseEntity<ResponseStructure>(rs, HttpStatus.BAD_REQUEST);
        }
    }

//    public ResponseEntity<ResponseStructure> aggregationData(String email) {
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.unwind("penaltyCategories", true),
//                Aggregation.lookup("hub", "hubId", "_id", "result1"),
//                Aggregation.unwind("result1", true),
//                Aggregation.lookup("users", "riderId", "_id", "result2"),
//                Aggregation.unwind("result2", true),
//                Aggregation.lookup("city_state_lat_lng", "city", "geoname_id", "result3"),
//                Aggregation.unwind("result3", true),
//                Aggregation.lookup("configurations", "status", "_id", "result4"),
//                Aggregation.unwind("result4", true),
//                Aggregation.project()
//                        .and("penaltyId").as("deductionId")
//                        .and("penaltyCategories").as("deductionCategory")
//                        .and("riderName").as("riderName")
//                        .and("clientName").as("clientName")
//                        .and("result1.name").as("hubName")
//                        .and("lead_code").as("riderId")
//                        .and("result3.city_name").as("city")
//                        .and("riderCustomerId").as("riderCustomerId")
//                        .and("result2.mobile").as("mobile")
//                        .and("result4.value").as("status")
//                        .and("totalCollectedAmount").as("totalCollectedAmount")
//                        .and("createdAt").as("createdDate")
//
//        );
//        AggregationResults<RiderAggregationResult> penaltyDetails = mongoTemplate.aggregate(aggregation, "penalty_details", RiderAggregationResult.class);
//        rs.setBody(penaltyDetails.getMappedResults());
//        ResponseStructure rs = new ResponseStructure();
//        rs.setHttpStatus(HttpStatus.OK.value());
//        rs.setMessage("alright");
////        rs.setBody(repository.aggregationData(email));
//        return new ResponseEntity<ResponseStructure>(rs, HttpStatus.OK);
//    }

    public ResponseEntity<ResponseStructure> allAggregationData(int pageNo, int itemsPerPage) {
        ResponseStructure rs = new ResponseStructure();
        PaginatorAndDetails pd = new PaginatorAndDetails();

        rs.setHttpStatus(HttpStatus.OK.value());
        rs.setMessage("fetched all data.......");
        int skip = (pageNo - 1) * itemsPerPage;
        int limit = itemsPerPage;
        Result all;
        try {
             all = repository.findAll(limit, skip);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        pd.setDetails(all.getProjectionFacet());
        PageStructure ps = new PageStructure();
        ps.setItemsPerPage(itemsPerPage);
        ps.setPageNo(pageNo);
        ps.setTotalItems(all.getCountFacet().getCount());
        if (ps.getTotalItems() == itemsPerPage)
            ps.setTotalPages(ps.getTotalItems() / itemsPerPage);
        else ps.setTotalPages((ps.getTotalItems() / itemsPerPage) + 1);
        pd.setPaginator(ps);
        rs.setBody(pd);
        return new ResponseEntity<ResponseStructure>(rs, HttpStatus.OK);
    }
}
