package com.task.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.task.model.EventLog;

@Repository
public interface EventLogRepository extends MongoRepository<EventLog, String> {

}
