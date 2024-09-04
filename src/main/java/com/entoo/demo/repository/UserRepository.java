package com.entoo.demo.repository;

import com.entoo.demo.document.Result;
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


}

