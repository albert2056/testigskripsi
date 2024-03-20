package com.project.repository;

import com.project.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
  User findByIdAndIsDeleted(Integer id, Integer isDeleted);
  User findByEmailAndIsDeleted(String email, Integer isDeleted);
  List<User> findByIsDeleted(Integer isDeleted);
}
