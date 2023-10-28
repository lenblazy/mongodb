package com.lenibonje.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDbClient {

    private static MongoClient mongoClient;

    public static synchronized MongoClient getMongoClient(String connStr) {
        if (mongoClient == null) {
            ConnectionString connectionString = new ConnectionString(connStr);

            MongoClientSettings settings = MongoClientSettings
                    .builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi
                            .builder()
                            .version(ServerApiVersion.V1)
                            .build()
                    )
                    .build();

            mongoClient = MongoClients.create(settings);
        }
        return mongoClient;
    }

}
