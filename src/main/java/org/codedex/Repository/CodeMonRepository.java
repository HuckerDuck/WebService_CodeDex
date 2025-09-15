package org.codedex.Repository;

import com.mongodb.client.MongoCollection;
import org.codedex.Model.CodeMon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CodeMonRepository extends MongoRepository<CodeMon, String> {
    Optional<CodeMon> findById(String id);
    List<CodeMon> findByType(String type);
    List<CodeMon> findByName(String name);
    List<CodeMon> findByCodeMonGeneration(String codeMonGeneration);
}
