package mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Connection {
    public static MongoDatabase normal(String url, String databaseName) {
        MongoClient mongoClient = MongoClients.create(url);

        return mongoClient.getDatabase(databaseName);
    }
}
