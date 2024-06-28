package mongoDB.crud;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


public class ReadData {
    public MongoCollection<Document> collection;

    public ReadData(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void displayedData() {
        int i = 0;
        for (Document cur : collection.find()) {
            System.out.println(cur.toJson());
        }
    }

    public FindIterable<Document> getLastData() {
        return collection.find();
    }

    public List<Document> convertToListDocument() {
        List<Document> documents = new ArrayList<>();
        FindIterable<Document> documentList = collection.find();

        for (Document document : documentList) {
            documents.add(document);
        }
        return documents;
    }
}
