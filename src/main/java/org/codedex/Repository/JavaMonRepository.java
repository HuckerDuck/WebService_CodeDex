package org.codedex.Repository;

import org.codedex.Model.JavaMon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JavaMonRepository extends MongoRepository<JavaMon, String> {
    Optional<JavaMon> findById(String id);
}
