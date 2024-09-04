package com.entoo.demo.repository;

import com.entoo.demo.document.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmpRepo extends MongoRepository<Employee,String>
{
}
