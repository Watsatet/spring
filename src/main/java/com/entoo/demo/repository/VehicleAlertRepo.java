package com.entoo.demo.repository;

import com.entoo.demo.Model.Report;
import com.entoo.demo.document.Alert;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleAlertRepo extends MongoRepository<Alert, String> {

    @Aggregation(pipeline = {
            " {\n" +
                    "    $match:\n" +
                    "      {\n" +
                    "        vehicle_registration_number: {\n" +
                    "          $regex: \"\"\n" +
                    "        },\n" +
                    "        created_at: {\n" +
                    "          $gt: ISODate(\n" +
                    "            \"2024-09-11T11:45:34.822+00:00\"\n" +
                    "          )\n" +
                    "        }\n" +
                    "      }\n" +
                    "  },",
            "  {\n" +
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
                    "  },\n",
            "  {\n" +
                    "    $group: {\n" +
                    "      _id: \"$_id.vehicle_registration_number\",\n" +
                    "      data: {\n" +
                    "        $push: \"$$ROOT\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  },\n",
            "  {\n" +
                    "    $project: {\n" +
                    "      _id:0,\n" +
                    "      vehicleNo:\"$_id\",\n" +
                    "      data: {\n" +
                    "        $map: {\n" +
                    "          input: \"$data\",\n" +
                    "          as: \"val\",\n" +
                    "          in: {\n" +
                    "            alertName: \"$$val._id.alert_name\",\n" +
                    "            count: \"$$val.count\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n"
    })
    List<Report> getAlertData();
}
