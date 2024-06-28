package mongoDB.crud;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class InsertData {
    public MongoCollection<Document> collection;

    public InsertData(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void insertData(Document document) {
        collection.insertOne(document);
    }
}
