package com.lenibonje.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoIterable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyEndpoint {

    @GetMapping("/data")
    public List<String> retrieveData(HttpServletRequest request, HttpServletResponse response){
        List<String> data = new ArrayList<>();
        MongoClient mongoClient = MongoDbClient.getMongoClient();
        MongoIterable<String> strings = mongoClient.listDatabaseNames();
        for(String string : strings){
            data.add(string);
        }
        return data;
    }

}
