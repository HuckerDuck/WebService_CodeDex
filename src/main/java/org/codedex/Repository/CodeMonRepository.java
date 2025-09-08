package org.codedex.Repository;

import org.codedex.Model.CodeMon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CodeMonRepository extends MongoRepository<CodeMon, String> {
    Optional<CodeMon> findById(String id);
}
