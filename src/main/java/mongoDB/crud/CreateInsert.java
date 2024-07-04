package mongoDB.crud;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class CreateInsert {
    public MongoCollection<Document> collection;

    public CreateInsert(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void insertData(Document document) {
        collection.insertOne(document);
    }
}
