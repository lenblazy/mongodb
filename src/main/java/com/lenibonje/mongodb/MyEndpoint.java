package com.lenibonje.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.InsertOneResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyEndpoint {

    @GetMapping("/dbs")
    public List<String> retrieveData(){
        return MongoDbClient.getMongoClient().listDatabaseNames().into(new ArrayList<>());
    }

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student){
        MongoDatabase database = MongoDbClient.getMongoClient().getDatabase("school");
        MongoCollection<Document> students = database.getCollection("students");
        Document document = new Document("_id", new ObjectId())
                .append("first_name", student.getFirstName())
                .append("last_name", student.getLastName())
                .append("age", student.getAge())
                .append("gpa", student.getGpa())
                .append("full_time", student.isFullTime());

        InsertOneResult result = students.insertOne(document);
        BsonValue id = result.getInsertedId();
        student.setId(id.toString());

        return ResponseEntity.created(null).build();
    }

}
