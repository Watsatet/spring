package com.entoo.demo.repository;

import com.entoo.demo.document.Result;
import com.entoo.demo.document.RiderAggregationResult;
import com.entoo.demo.document.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Aggregation(pipeline = {
            "{\n" +
                    "    $facet: {\n" +
                    "      countFacet: [\n" +
                    "        {\n" +
                    "          $count: 'count' " +
                    "        }\n" +
                    "      ],\n" +
                    "      projectionFacet: [\n" +
                    "{\n" +
                    "          $lookup: {\n" +
                    "            from: \"employee\",\n" +
                    "            localField: \"gender\",\n" +
                    "            foreignField: \"_id\",\n" +
                    "            as: \"userDetails\"\n" +
                    "          }\n" +
                    "        }," +
                    "        {\n" +
                    "          $unwind: {\n" +
                    "            path: \"$userDetails\",\n" +
                    "            preserveNullAndEmptyArrays: true\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          $project: {\n" +
                    "            name: 1,\n" +
                    "            email: 1,\n" +
                    "            gender: \"$userDetails.gender\",\n" +
                    "            dob: 1,\n" +
                    "            phone: 1,\n" +
                    "            password: 1,\n" +
                    "            favourite_fruit: 1\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          $skip: ?1\n" +
                    "        },\n" +
                    "        {\n" +
                    "          $limit: ?0\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n",
            "  {\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$countFacet\",\n" +
                    "        preserveNullAndEmptyArrays: true\n" +
                    "      }\n" +
                    "  }"
    })
    Result findAll(int limit, int skip);

    Optional<User> findByPhone(String phone);

    @Aggregation(pipeline = {
            "{ $match: { 'email': ?0 } }",
            "{\n" +
                    "    $lookup:\n" +
                    "      {\n" +
                    "        from: \"employee\",\n" +
                    "        localField: \"gender\",\n" +
                    "        foreignField: \"_id\",\n" +
                    "        as: \"userDetails\"\n" +
                    "      }\n" +
                    "  }",
            "{\n" +
                    "    $unwind: {\n" +
                    "      path: \"$userDetails\",\n" +
                    "      preserveNullAndEmptyArrays: true\n" +
                    "    }\n" +
                    "  }",
            "{\n" +
                    "    $project: {\n" +
                    "      name: 1,\n" +
                    "      email: 1,\n" +
                    "      gender: \"$userDetails.gender\",\n" +
                    "      dob: 1,\n" +
                    "      phone: 1,\n" +
                    "      password: 1 \n" +
                    "      favourite_fruit:1\n" +
                    "    }\n" +
                    "  }"
    })
    List<User> aggregationData(String email);

    User findByEmail(String email);

    @Aggregation(pipeline = {
            "{\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$penaltyCategories\",\n" +
                    "        preserveNullAndEmptyArrays: true\n" +
                    "      }\n" +
                    "  },",
            "{\n" +
                    "    $lookup:\n" +
                    "      {\n" +
                    "        from: \"hub\",\n" +
                    "        localField: \"hubId\",\n" +
                    "        foreignField: \"_id\",\n" +
                    "        as: \"result1\"\n" +
                    "      }\n" +
                    "  },",
            "{\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$result1\",\n" +
                    "        preserveNullAndEmptyArrays: true\n" +
                    "      }\n" +
                    "  },",
            "{\n" +
                    "    $lookup:\n" +
                    "      {\n" +
                    "        from: \"users\",\n" +
                    "        localField: \"riderId\",\n" +
                    "        foreignField: \"_id\",\n" +
                    "        as: \"result2\"\n" +
                    "      }\n" +
                    "  },\n",
            "  {\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$result2\",\n" +
                    "        preserveNullAndEmptyArrays: true\n" +
                    "      }\n" +
                    "  },\n",
            "  {\n" +
                    "    $lookup:\n" +
                    "      {\n" +
                    "        from: \"city_state_lat_lng\",\n" +
                    "        localField: \"city\",\n" +
                    "        foreignField: \"geoname_id\",\n" +
                    "        as: \"result3\"\n" +
                    "      }\n" +
                    "  },\n",
            "  {\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$result3\",\n" +
                    "        preserveNullAndEmptyArrays: true\n" +
                    "      }\n" +
                    "  },\n",
            "  {\n" +
                    "    $lookup:\n" +
                    "      {\n" +
                    "        from: \"configurations\",\n" +
                    "        localField: \"status\",\n" +
                    "        foreignField: \"_id\",\n" +
                    "        as: \"result4\"\n" +
                    "      }\n" +
                    "  },\n",
            "  {\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$result4\",\n" +
                    "        preserveNullAndEmptyArrays: true\n" +
                    "      }\n" +
                    "  },\n",
            "  {\n" +
                    "    $project: {\n" +
                    "      _id: 0,\n" +
                    "      deductionId: \"$penaltyId\",\n" +
                    "      deductionCategory: \"$penaltyCategories\",\n" +
                    "      riderName: \"$riderName\",\n" +
                    "      clientName: \"$clientName\",\n" +
                    "      hubName: \"$result1.name\",\n" +
                    "      riderId: \"$lead_code\",\n" +
                    "      city: \"$result3.city_name\",\n" +
                    "      riderCustomerId: \"$riderCustomerId\",\n" +
                    "      createdDate: {\n" +
                    "        $dateToString: {\n" +
                    "          format: \"%d-%m-%Y,%H:%M\",\n" +
                    "          date: \"$createdAt\",\n" +
                    "          timezone: \"Asia/Kolkata\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      modifiedDate: {\n" +
                    "        $dateToString: {\n" +
                    "          format: \"%d-%m-%Y,%H:%M\",\n" +
                    "          date: \"$modifiedAt\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      mobile: \"$result2.mobile\",\n" +
                    "      status: \"$result4.value\",\n" +
                    "      totalCollectedAmount: {\n" +
                    "        $sum: \"$totalCollectedAmount\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }"
    })
    List<RiderAggregationResult> getAggData();
}

