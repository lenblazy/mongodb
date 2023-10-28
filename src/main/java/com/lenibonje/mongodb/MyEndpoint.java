package com.lenibonje.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MyEndpoint {

    private MongoClient mongoClient;

    public MyEndpoint(AppConfig config) {
        mongoClient = MongoDbClient.getMongoClient(config.connection());
    }

    @GetMapping("/dbs")
    public List<String> retrieveData() {
        return mongoClient.listDatabaseNames().into(new ArrayList<String>());
    }

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        MongoCollection<Document> collection = getDocumentMongoCollection();
        Document document = new Document("_id", new ObjectId())
                .append("first_name", student.getFirstName())
                .append("last_name", student.getLastName())
                .append("age", student.getAge())
                .append("gpa", student.getGpa())
                .append("full_time", student.isFullTime());

        InsertOneResult result = collection.insertOne(document);
        BsonValue id = result.getInsertedId();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{student_id}")
                .buildAndExpand(id.asObjectId().getValue()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudent() {
        MongoCollection<Document> collection = getDocumentMongoCollection();
        ArrayList<Student> students = new ArrayList<Student>();

        ObjectMapper mapper = new ObjectMapper();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                students.add(mapper.readValue(cursor.next().toJson(), Student.class));
            }
        } catch (JsonProcessingException e) {
            return (ResponseEntity<List<Student>>) ResponseEntity.internalServerError();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{student_id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("student_id") String id) {
        MongoCollection<Document> collection = getDocumentMongoCollection();
        ObjectId oId = new ObjectId(id);
        Document document = collection.find(Filters.eq("_id", oId)).first();
        try {
            if (document == null) {
                throw new Exception("Student not found");
            }
            ObjectMapper mapper = new ObjectMapper();
            Student student = mapper.readValue(document.toJson(), Student.class);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return (ResponseEntity<Student>) ResponseEntity.internalServerError();
        }

    }

    private MongoCollection<Document> getDocumentMongoCollection() {
        MongoDatabase database = mongoClient.getDatabase("school");
        return database.getCollection("students");
    }

}
