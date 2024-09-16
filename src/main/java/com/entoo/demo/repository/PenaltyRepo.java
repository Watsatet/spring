package com.entoo.demo.repository;

import com.entoo.demo.Model.Deduction;
import com.entoo.demo.document.Penalty;
import com.entoo.demo.document.Result;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PenaltyRepo extends MongoRepository<Penalty,String>
{
    @Aggregation(pipeline = {
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
            " {\n" +
                    "    $match: {\n" +
                    "      $or: [\n" +
                    "        {\n" +
                    "          riderName: {\n" +
                    "            $regex: ?0,\n" +
                    "            $options: \"i\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n" ,
                    "{\n" +
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
                            "        { $skip: ?1 },\n" +
                            "        { $limit: ?2 }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  },\n" ,
                            "  {\n" +
                            "    $unwind: {\n" +
                            "      path: \"$countFacet\",\n" +
                            "      preserveNullAndEmptyArrays: true\n" +
                            "    }\n" +
                            "  }"
    })
    Result getAggData(String search, int skip, int limit);

    @Aggregation(pipeline = {
                    "  {\n" +
                    "    $lookup: {\n" +
                    "      from: \"configurations\",\n" +
                    "      localField: \"status\",\n" +
                    "      foreignField: \"_id\",\n" +
                    "      as: \"configurationsresult\"\n" +
                    "    }\n" +
                    "  },\n" ,
                    "  {\n" +
                    "    $unwind: {\n" +
                    "      path: \"$configurationsresult\",\n" +
                    "      preserveNullAndEmptyArrays: true\n" +
                    "    }\n" +
                    "  },\n" ,
                    "  {\n" +
                    "    $facet: {\n" +
                    "      totalPenaltyAmount: [\n" +
                    "        {\n" +
                    "          $group: {\n" +
                    "            _id: \"$configurationsresult.key\",\n" +
                    "            totalPenaltyAmount: {\n" +
                    "              $sum: \"$totalPenaltyAmount\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      sum: [\n" +
                    "        {\n" +
                    "          $group: {\n" +
                    "            _id: \"\",\n" +
                    "            totalPenaltyAmount: {\n" +
                    "              $sum: \"$totalPenaltyAmount\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n" ,
                    "  {\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$sum\"\n" +
                    "      }\n" +
                    "  },\n" ,
                    "  {\n" +
                    "    $unwind:\n" +
                    "      {\n" +
                    "        path: \"$totalPenaltyAmount\"\n" +
                    "      }\n" +
                    "  },\n" ,
                    "  {\n" +
                    "    $project: {\n" +
                    "      status: \"$totalPenaltyAmount._id\",\n" +
                    "      totalPenaltyAmount:\n" +
                    "        \"$totalPenaltyAmount.totalPenaltyAmount\",\n" +
                    "      percentage: {\n" +
                    "        $multiply: [\n" +
                    "          {\n" +
                    "            $divide: [\n" +
                    "              \"$totalPenaltyAmount.totalPenaltyAmount\",\n" +
                    "              \"$sum.totalPenaltyAmount\"\n" +
                    "            ]\n" +
                    "          },\n" +
                    "          100\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n"
    })
    List<Deduction> getData();
}
