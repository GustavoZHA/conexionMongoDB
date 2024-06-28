package localConexion;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mongoDB.crud.ReadData;
import org.bson.Document;

public class MongoConexion {
    public static void main(String[] args) {
        // Conectar a MongoDB (asegúrate de tener MongoDB corriendo localmente en el puerto por defecto 27017)
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Seleccionar una base de datos
            MongoDatabase database = mongoClient.getDatabase("mi_basededatos");
            database.createCollection("collectionName");

            System.out.println("Conexión establecida correctamente a la base de datos: " + database.getName());
            MongoCollection<Document> collection = database.getCollection("test00");
//            InsertData insertData = new InsertData(collection);
//
//            Document document = new Document("firstName", "Peter")
//                    .append("lastName", "user02")
//                    .append("age", 38)
//                    .append("occupation", "Test")
//                    .append("skills", Arrays.asList("JMeter", "Selenium", "Rest-Assured"))
//                    .append("adress", new Document("city", "London")
//                            .append("street", "saint")
//                            .append("house", 49));
//
//            insertData.insertData(document);

            ReadData readData = new ReadData(collection);
            readData.displayedData();

        } catch (MongoException e) {
            System.err.println("Error al conectar a MongoDB: " + e.getMessage());
        }
    }
}
