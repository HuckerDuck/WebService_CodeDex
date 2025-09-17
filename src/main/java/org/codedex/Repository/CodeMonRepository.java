package org.codedex.Repository;
import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonTyps;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.domain.Pageable;

import com.mongodb.client.MongoCollection;
import org.codedex.Model.CodeMon;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CodeMonRepository extends MongoRepository<CodeMon, String> {
    Optional<CodeMon> findById(String id);
    List<CodeMon> findByType(CodeMonTyps type);
    List<CodeMon> findByName(String name);

    List<CodeMon> findByCodeMonGeneration(String codeMonGeneration);

    @Aggregation
    List<CodeMon> findByTypeAndCodeMonGeneration(CodeMonTyps type, Integer codeMonGeneration);


    @Aggregation(pipeline = {
            "{'$match': {'createdAt': {'$gte': ?0}}}"
    })
    List<CodeMon> findAllWithCreatedAfter(Date createdAfter);

    @Aggregation(pipeline ={
            "{'$match': {'$createdAt' : {'$lde':?0}}}"
    })
    List<CodeMon> findAllWithCreatedBefore(Date createdBefore);


    // Icke-paginerad


    // Paginerad
    Page<CodeMon> findByCodeMonGeneration(Integer codeMonGeneration, Pageable pageable);


}
