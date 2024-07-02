package product;

import mongoDB.ManagementDB;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import product.Mapper.BodyFileMapper;
import product.dto.BodyFileDTO;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

public class PersistentLocal {
    public static ManagementDB database;
    public int numberRecords = 0;
    public List<String> listId = new ArrayList<>();

    @BeforeAll
    public static void connection() {
        String urlMongoConnection = "mongodb://localhost:27017";
        String dataBaseName = "mi_basededatos";
        database = new ManagementDB(urlMongoConnection, dataBaseName);
    }

    @Test
    @DisplayName("Verificar the POST, UPDATE, GETBy, DELETE and GETALL is persisting in DB")
    public void verifyHappyPathLocal() throws Exception {
        String pathExpectResult = "src/test/resources/result.txt";
        List<BodyFileDTO> expected = BodyFileMapper.ListBodyFileDTOFromFile(pathExpectResult);


        List<Document> records= database.getCollection("audit").find(or(
                eq("executionStatus","SUCCESS"))
        ).into(new ArrayList<>());

        for (BodyFileDTO bodyFileDTO : expected) {
            for (Document record : records) {
                if("POST-product:".equals(bodyFileDTO.getMethod()) || "PUT-product:".equals(bodyFileDTO.getMethod())) {
                    String saveBody = record.get("result").toString();
                    if(saveBody.contains("name=" + bodyFileDTO.getName())) {

                        listId.add(record.get("_id").toString());
                        break;
                    }
                }
            }
        }
    }


    @Test
    @DisplayName("Verify the POST, UPDATE, GETBy, DELETE and GETALL is persisting in DB")
    public void verifyHappyPathLocalCRUD() throws Exception {
        String pathExpectResult = "src/test/resources/result.txt";
        List<BodyFileDTO> expected = BodyFileMapper.ListBodyFileDTOFromFile(pathExpectResult);

        List<Document> records= database.getCollection("audit").find(or(
                eq("executionStatus","SUCCESS"))
        ).into(new ArrayList<>());

        for (BodyFileDTO bodyFileDTO : expected) {
            for (Document record : records) {
                String id = record.get("_id").toString();
                switch (bodyFileDTO.getMethod()) {
                    case "POST-product:" : {
                        String saveBody = record.get("result").toString();
                        if(saveBody.contains("name=" + bodyFileDTO.getName()) && record.get("methodName").equals("create") && !isSelected(id)) {
                            Assertions.assertTrue(saveBody.contains("id=" + bodyFileDTO.getId()), "The id is NOT MATCHED. The expected is" +  bodyFileDTO.getId());
                            Assertions.assertTrue(saveBody.contains("description=" + bodyFileDTO.getDescription()), "The description is NOT MATCHED. The expected is" +  bodyFileDTO.getDescription());
                            Assertions.assertTrue(saveBody.contains("price=" + bodyFileDTO.getPrice()), "The price is NOT MATCHED. The expected is" +  bodyFileDTO.getPrice());
                            Assertions.assertTrue(saveBody.contains("stock=" + bodyFileDTO.getStock()), "The stock is NOT MATCHED. The expected is" +  bodyFileDTO.getStock());
                            Assertions.assertEquals(record.get("serviceName"),"product-service");
                            Assertions.assertEquals(record.get("executionStatus"),"SUCCESS");
                            Assertions.assertEquals(record.get("className"),"ProductServiceImpl");
                            Assertions.assertEquals(bodyFileDTO.getStatus(),"201");
                            listId.add(id);
                        }
                        break;
                    }
                    case "PUT-product:" : {
                        String saveBody = record.get("result").toString();
                        if(saveBody.contains("name=" + bodyFileDTO.getName()) && record.get("methodName").equals("update") && !isSelected(id)) {
                            String idActual = ((ArrayList) (record.get("parameters"))).get(0).toString();
                            Assertions.assertEquals(idActual, bodyFileDTO.getId());
                            Assertions.assertTrue(saveBody.contains("id=" + bodyFileDTO.getId()), "The id is NOT MATCHED. The expected is" +  bodyFileDTO.getId());
                            Assertions.assertTrue(saveBody.contains("description=" + bodyFileDTO.getDescription()), "The description is NOT MATCHED. The expected is" +  bodyFileDTO.getDescription());
                            Assertions.assertTrue(saveBody.contains("description=" + bodyFileDTO.getDescription()), "The description is NOT MATCHED. The expected is" +  bodyFileDTO.getDescription());
                            Assertions.assertTrue(saveBody.contains("price=" + bodyFileDTO.getPrice()), "The price is NOT MATCHED. The expected is" +  bodyFileDTO.getPrice());
                            Assertions.assertTrue(saveBody.contains("stock=" + bodyFileDTO.getStock()), "The stock is NOT MATCHED. The expected is" +  bodyFileDTO.getStock());
                            Assertions.assertEquals(record.get("serviceName"),"product-service");
                            Assertions.assertEquals(record.get("executionStatus"),"SUCCESS");
                            Assertions.assertEquals(record.get("className"),"ProductServiceImpl");
                            Assertions.assertEquals(bodyFileDTO.getStatus(),"201");

                            listId.add(id);
                        }
                        break;
                    }
                    case "DELETE-product:": {
                        if("delete".equals(record.get("methodName")) && !isSelected(id)) {
                            String idActual = ((ArrayList) (record.get("parameters"))).get(0).toString();
                            if (idActual.equals(bodyFileDTO.getId())) {
                                Assertions.assertEquals(record.get("serviceName"), "product-service");
                                Assertions.assertEquals(record.get("className"), "ProductServiceImpl");
                                Assertions.assertEquals(record.get("executionStatus"), "SUCCESS");
                                Assertions.assertEquals(record.get("result"), "No result");
                                Assertions.assertEquals(bodyFileDTO.getStatus(), "200");
                                listId.add(id);
                            }
                        }
                        break;
                    }
                    default: {
                        System.out.println("Something is wrong may be: Problem in txt file, only the input valid are POST-product:, PUT-product: and DELETE-product:");
                    }
                }
            }
        }
        Assertions.assertEquals(expected.size(), listId.size(), "The number record not founded are: " + Math.abs(expected.size() - listId.size()));
    }

    public boolean isSelected(String id) {
        return listId.stream().findFirst().equals(id);
    }
}
