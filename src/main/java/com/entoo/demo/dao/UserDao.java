package com.entoo.demo.dao;

import com.entoo.demo.document.RiderAggregationResult;
import com.entoo.demo.document.User;
import com.entoo.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDao
{
    @Autowired
    private UserRepository repository;

    public Optional<User> findByPhone(User user)
    {
        return repository.findByPhone(user.getPhone());
    }

    public User signupUser(User user)
    {
        return repository.save(user);
    }

    public List<User> aggregationData(String username)
    {
        return repository.aggregationData(username);
    }
}
