package product;

import com.mongodb.client.*;
import mongoDB.ManagementDB;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Persistent {
//    public static ManagementDB database;

//    @BeforeEach
//    public void connection() {
//        String connectionString = "mongodb://admin:pass@10.240.74.12:8081/microservices_demo";
//
//        MongoClient mongoClient = MongoClients.create(connectionString);
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("microservices_demo");
//        database = new ManagementDB(mongoDatabase, mongoClient);
//    }

    public static ManagementDB database;
    public int numberRecords = 0;
    public List<String> listId = new ArrayList<>();

//    @BeforeAll
//    public static void connection() {
//        String urlMongoConnection = "mongodb://localhost:27017";
//        String dataBaseName = "mi_basededatos";
//        database = new ManagementDB(urlMongoConnection, dataBaseName);
//    }

    @Test
    @DisplayName("Verificar the POST, UPDATE, GETBy, DELETE and GETALL is persisting in ")
    public void verifyHappyPath() throws Exception {
        String urlMongoConnection = "mongodb://localhost:27017";
        String dataBaseName = "mi_basededatos";
        database = new ManagementDB(urlMongoConnection, dataBaseName);
        System.out.println("test - EXECUTED");
    }

    @Test
   public void verifyHappyPath1() throws Exception {
        // Define la URL de conexión a MongoDB
        String connectionString = "mongodb://admin:pass@10.240.74.12:8081";

        // Crea la conexión utilizando la URL de conexión
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            // Selecciona la base de datos
            MongoDatabase database = mongoClient.getDatabase("microservices_demo");

            System.out.println("Conexión establecida correctamente a la base de datos: " + database.getName());

            database.createCollection("test00");
            MongoCollection<Document> mongoCollection = database.getCollection("audit");

        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB: " + e.getMessage());
        }

    }
}
