package com.entoo.demo.service;

import com.entoo.demo.CustomAggregationOperation;
import com.entoo.demo.document.Result;
import com.entoo.demo.document.User;
import com.entoo.demo.pagination.PageStructure;
import com.entoo.demo.repository.PaginatorAndDetails;
import com.entoo.demo.repository.PenaltyRepo;
import com.entoo.demo.repository.UserRepository;
import com.entoo.demo.repository.VehicleAlertRepo;
import com.entoo.demo.response.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PenaltyRepo repo;
    @Autowired
    private VehicleAlertRepo repos;

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
                    rs.setBody("enter correct password..");
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
            rs.setBody("do signup..");
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
                rs.setBody(repository.save(user));
            }

            return new ResponseEntity<>(rs, HttpStatus.CREATED);
        } else {
            rs.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            rs.setMessage("User already exists");
            rs.setBody(null);
            return new ResponseEntity<>(rs, HttpStatus.BAD_REQUEST);
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
        var rs = new ResponseStructure<>();
        PaginatorAndDetails pd = new PaginatorAndDetails();

        rs.setHttpStatus(HttpStatus.OK.value());
        rs.setMessage("fetched all data.......");
        int skip = (pageNo - 1) * itemsPerPage;
        Result all;
        try {
            all = repository.findAll(itemsPerPage, skip);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        pd.setDetails(all.getProjectionFacet());
        PageStructure ps = new PageStructure();
        ps.setItemsPerPage(itemsPerPage);
        ps.setPageNo(pageNo);
        ps.setTotalItems(all.getCountFacet().get(0).getCount());
        if (ps.getTotalItems() == itemsPerPage)
            ps.setTotalPages(ps.getTotalItems() / itemsPerPage);
        else ps.setTotalPages((ps.getTotalItems() / itemsPerPage) + 1);
        pd.setPaginator(ps);
        rs.setBody(pd);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<User>> changePass(String email, String oldPass, String newPass) {
        User optional = repository.findByEmail(email);
        ResponseStructure<User> rs = new ResponseStructure<>();
        if (optional != null) {
            if (optional.getPassword().equals(oldPass)) {
                if (newPass.equals(oldPass)) {
                    rs.setHttpStatus(HttpStatus.NOT_IMPLEMENTED.value());
                    rs.setMessage("new pass should be differ from new pass");
                    rs.setBody(null);
                    return new ResponseEntity<>(rs, HttpStatus.NOT_IMPLEMENTED);
                } else {
                    optional.setPassword(newPass);
                    rs.setHttpStatus(HttpStatus.ACCEPTED.value());
                    rs.setMessage("password changed successfully...");
                    rs.setBody(repository.save(optional));
                    return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
                }
            } else {
                rs.setHttpStatus(HttpStatus.NOT_ACCEPTABLE.value());
                rs.setMessage("old password is not correct...");
                rs.setBody(null);
                return new ResponseEntity<>(rs, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            rs.setHttpStatus(HttpStatus.NOT_FOUND.value());
            rs.setMessage("user not found");
            rs.setBody(null);
            return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseStructure> deleteByEmail(String email) {
        ResponseStructure rs = new ResponseStructure<>();
        try {
            User user = repository.findByEmail(email);
            if (user != null) {
                repository.deleteById(user.getId());
                rs.setHttpStatus(HttpStatus.OK.value());
                rs.setBody(user);
                rs.setMessage("user deleted successfully.....");
                return new ResponseEntity<>(rs, HttpStatus.OK);
            } else {
                rs.setHttpStatus(HttpStatus.NOT_FOUND.value());
                rs.setBody(null);
                rs.setMessage("USER NOT-FOUND.....");
                return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            rs.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            rs.setMessage("Found multiple users with same entity....");
            return new ResponseEntity<>(rs, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getAggData(String search, int pageNo, int itemsPerPage) {
        ResponseStructure<PaginatorAndDetails> rs = new ResponseStructure<>();
        int skip = (pageNo - 1) * itemsPerPage;
        Result all = repo.getAggData(search, skip, itemsPerPage);

        PaginatorAndDetails pd = new PaginatorAndDetails();
        PageStructure ps = new PageStructure();

        ps.setTotalItems(all.getCountFacet().get(0).getCount());
        ps.setTotalPages((all.getCountFacet().get(0).getCount()) / itemsPerPage);
        ps.setPageNo(pageNo);
        ps.setItemsPerPage(itemsPerPage);
        pd.setDetails(all.getProjectionFacet());
        pd.setPaginator(ps);
        rs.setBody(pd);
        rs.setMessage("aggregatedData");
        rs.setHttpStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getAggeData(String search, int pageNo, int itemsPerPage) {
        int skip = (pageNo - 1) * itemsPerPage;
        var lookup1 = "{\n" +
                "    $lookup: {\n" +
                "      from: \"hub\",\n" +
                "      localField: \"hubId\",\n" +
                "      foreignField: \"_id\",\n" +
                "      as: \"result1\"\n" +
                "    }\n" +
                "  }";

        var unwind1 = "{\n" +
                "    $unwind: {\n" +
                "      path: \"$result1\",\n" +
                "      preserveNullAndEmptyArrays: true\n" +
                "    }\n" +
                "  }";

        var lookup2 = " {\n" +
                "    $lookup: {\n" +
                "      from: \"users\",\n" +
                "      localField: \"riderId\",\n" +
                "      foreignField: \"_id\",\n" +
                "      as: \"result2\"\n" +
                "    }\n" +
                "  }";
        var unwind2 = "  {\n" +
                "    $unwind: {\n" +
                "      path: \"$result2\",\n" +
                "      preserveNullAndEmptyArrays: true\n" +
                "    }\n" +
                "  }";
        var lookup3 = "  {\n" +
                "    $lookup: {\n" +
                "      from: \"city_state_lat_lng\",\n" +
                "      localField: \"city\",\n" +
                "      foreignField: \"geoname_id\",\n" +
                "      as: \"result3\"\n" +
                "    }\n" +
                "  }";
        var unwind3 = "  {\n" +
                "    $unwind: {\n" +
                "      path: \"$result3\",\n" +
                "      preserveNullAndEmptyArrays: true\n" +
                "    }\n" +
                "  }";
        var lookup4 = "  {\n" +
                "    $lookup: {\n" +
                "      from: \"configurations\",\n" +
                "      localField: \"status\",\n" +
                "      foreignField: \"_id\",\n" +
                "      as: \"result4\"\n" +
                "    }\n" +
                "  }";

        var unwind4 = "  {\n" +
                "    $unwind: {\n" +
                "      path: \"$result4\",\n" +
                "      preserveNullAndEmptyArrays: true\n" +
                "    }\n" +
                "  }";

        var match = "{\n" +
                "    $match: {\n" +
                "      $or: [\n" +
                "        {\n" +
                "          riderName: {\n" +
                "            $regex: \"" + search + "\" ,\n" +
                "            $options: \"i\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }";
        var facet = "{\n" +
                "    $facet: {\n" +
                "      countFacet: [\n" +
                "        {\n" +
                "          $count: \"count\"\n" +
                "        }\n" +
                "      ],\n" +
                "      projectionFacet: [\n" +
                "        {\n" +
                "          $project: {\n" +
                "            _id: 0,\n" +
                "            deductionId: \"$penaltyId\",\n" +
                "            deductionCategory:\n" +
                "              \"$penaltyCategories\",\n" +
                "            riderName: \"$riderName\",\n" +
                "            clientName: \"$clientName\",\n" +
                "            hubName: \"$result1.name\",\n" +
                "            riderId: \"$lead_code\",\n" +
                "            city: \"$result3.city_name\",\n" +
                "            riderCustomerId: \"$riderCustomerId\",\n" +
                "            createdDate: {\n" +
                "              $dateToString: {\n" +
                "                format: \"%d-%m-%Y,%H:%M\",\n" +
                "                date: \"$createdAt\",\n" +
                "                timezone: \"Asia/Kolkata\"\n" +
                "              }\n" +
                "            },\n" +
                "            modifiedDate: {\n" +
                "              $dateToString: {\n" +
                "                format: \"%d-%m-%Y,%H:%M\",\n" +
                "                date: \"$modifiedAt\"\n" +
                "              }\n" +
                "            },\n" +
                "            mobile: \"$result2.mobile\",\n" +
                "            status: \"$result4.value\",\n" +
                "            totalCollectedAmount: {\n" +
                "              $sum: \"$totalCollectedAmount\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        { $skip: " + skip + " },\n" +
                "        { $limit: " + itemsPerPage + " }\n" +
                "      ]\n" +
                "    }\n" +
                "  }";
        var unwind5 = " {\n" +
                "    $unwind: {\n" +
                "      path: \"$countFacet\",\n" +
                "      preserveNullAndEmptyArrays: true\n" +
                "    }\n" +
                "  }";

        List<String> list = List.of(lookup1, unwind1, lookup2, unwind2, lookup3, unwind3, lookup4, unwind4, match, facet, unwind5);
        var agg = new ArrayList<CustomAggregationOperation>();
        for (String l : list) {
            agg.add(new CustomAggregationOperation(l));
        }
        Aggregation aggg = Aggregation.newAggregation(agg);
        var penaltyDetails = mongoTemplate.aggregate(aggg, "penalty_details", Result.class);
        Result all = penaltyDetails.getMappedResults().get(0);

        ResponseStructure<PaginatorAndDetails> rs = new ResponseStructure<>();
        PaginatorAndDetails pd = new PaginatorAndDetails();
        PageStructure ps = new PageStructure();

        ps.setTotalItems(all.getCountFacet().get(0).getCount());
        ps.setTotalPages((all.getCountFacet().get(0).getCount()) / itemsPerPage);
        ps.setPageNo(pageNo);
        ps.setItemsPerPage(itemsPerPage);
        pd.setDetails(all.getProjectionFacet());
        pd.setPaginator(ps);
        rs.setBody(pd);
        rs.setMessage("aggregatedData");
        rs.setHttpStatus(HttpStatus.OK.value());
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getDeductionData() {
        ResponseStructure rs = new ResponseStructure<>();
        rs.setHttpStatus(HttpStatus.OK.value());
        rs.setMessage("aggregatedData");
        rs.setBody(repo.getData());
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    public ResponseEntity<ResponseStructure<PaginatorAndDetails>> getAlertData(int pageNo, int itemsPerPage, String date) {
        ResponseStructure rs = new ResponseStructure<>();
        try {
            int skip = (pageNo - 1) * itemsPerPage;
            String from = date + "T00:00:00Z";
            String to = date + "T23:59:59Z";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            Date ff = dateFormat.parse(from);
            Date tt = dateFormat.parse(to);

            Instant instant1 = ff.toInstant();
            Instant instant2 = tt.toInstant();

            ZonedDateTime f = instant1.atZone(ZoneOffset.UTC);
            ZonedDateTime t = instant2.atZone(ZoneOffset.UTC);


            var match = "{\n" +
                    "    $match:\n" +
                    "      {\n" +
                    "        vehicle_registration_number: {\n" +
                    "          $regex: \"\"\n" +
                    "        },\n" +
                    "   created_at: {\n" +
                    "    $gte: ISODate(\"" + f + "\"),\n" +
                    "    $lte: ISODate(\"" + t + "\")\n" +
                    "  }" +
                    "      }\n" +
                    "  }";
            var group1 = "{\n" +
                    "    $group: {\n" +
                    "      _id: {\n" +
                    "        vehicle_registration_number:\n" +
                    "          \"$vehicle_registration_number\",\n" +
                    "        alert_name: \"$alert_name\"\n" +
                    "      },\n" +
                    "      count: {\n" +
                    "        $count: {}\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }";
            var group2 = "{\n" +
                    "    $group: {\n" +
                    "      _id: \"$_id.vehicle_registration_number\",\n" +
                    "      data: {\n" +
                    "        $push: \"$$ROOT\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }";
            var facet = "{\n" +
                    "    $facet: {\n" +
                    "      countFacet: [\n" +
                    "        {\n" +
                    "          $count: \"count\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      projectionFacet: [\n" +
                    "        {\n" +
                    "          $project: {\n" +
                    "            _id: 0,\n" +
                    "            vehicleNo: \"$_id\",\n" +
                    "            data: {\n" +
                    "              $map: {\n" +
                    "                input: \"$data\",\n" +
                    "                as: \"val\",\n" +
                    "                in: {\n" +
                    "                  alertName:\n" +
                    "                    \"$$val._id.alert_name\",\n" +
                    "                  count: \"$$val.count\"\n" +
                    "                }\n" +
                    "              }\n" +
                    "            }\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          $skip: " + skip + " " +
                    "        },\n" +
                    "        { $limit: " + itemsPerPage + " }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  }";

            List<String> list = List.of(match, group1, group2, facet);
            var agg = new ArrayList<AggregationOperation>();
            for (var l : list) {
                agg.add(new CustomAggregationOperation(l));
            }
            var aggregation = Aggregation.newAggregation(agg);
            var data = mongoTemplate.aggregate(aggregation, "vehicle_alerts", Result.class);

            if (!data.getMappedResults().isEmpty()) {
                Result all = data.getMappedResults().get(0);
                if (!all.getCountFacet().isEmpty()) {
                    PaginatorAndDetails pw = new PaginatorAndDetails();
                    PageStructure ps = new PageStructure();

                    ps.setPageNo(pageNo);
                    ps.setItemsPerPage(itemsPerPage);
                    ps.setTotalItems(all.getCountFacet().get(0).getCount());
                    if (all.getCountFacet().get(0).getCount() >= itemsPerPage)
                        ps.setTotalPages(all.getCountFacet().get(0).getCount() / itemsPerPage);
                    else ps.setTotalPages((all.getCountFacet().get(0).getCount() / itemsPerPage) + 1);
                    pw.setPaginator(ps);
                    pw.setDetails(all.getProjectionFacet());
                    rs.setBody(pw);

                    rs.setMessage("got the data");
                    rs.setHttpStatus(HttpStatus.OK.value());
                    return new ResponseEntity<>(rs, HttpStatus.OK);
                } else {
                    rs.setBody(List.of());
                    rs.setMessage("no data found");
                    rs.setHttpStatus(HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
                }
            }
            else return new ResponseEntity<>(rs, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            rs.setBody(List.of());
            rs.setMessage("unprocessable");
            rs.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return new ResponseEntity<>(rs, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
