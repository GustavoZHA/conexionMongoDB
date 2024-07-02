package mongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ManagementDB {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public ManagementDB(String urlMongoConnection, String database) {
        this.mongoClient = MongoClients.create(urlMongoConnection);
        this.mongoDatabase = mongoClient.getDatabase(database);
        System.out.println("Conexión establecida correctamente a la base de datos: " + mongoDatabase.getName());
    }

    public ManagementDB(MongoDatabase mongoDatabase, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoDatabase;
    }

    public MongoDatabase setAndGetDataBase(String databaseName) {
        this.mongoDatabase = mongoClient.getDatabase(databaseName);
        return mongoClient.getDatabase(databaseName);
    }

    public MongoDatabase getDataBase() {
        return mongoDatabase;
    }

    public MongoCollection<Document> getCollection(String collectionName){
        return mongoDatabase.getCollection(collectionName);
    }
}
