package product;

import mongoDB.ManagementDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.junit.jupiter.api.*;
import product.Mapper.BodyFileMapper;
import product.dto.BodyDTO;
import utils.ManagementFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

public class PersistentLocal {
    private Logger logger = LogManager.getLogger(ManagementDB.class);
    public static ManagementDB database;
    public HashSet<String> listId = new HashSet<>();

    @BeforeAll
    public static void connection() {
        String urlMongoConnection = "mongodb://localhost:27017";
        String dataBaseName = "microservices_demo-Local";
        database = new ManagementDB(urlMongoConnection, dataBaseName);
    }

    @AfterAll
    public static void clean() throws IOException {
        //Delete files
        String expectResultFile = "src/test/resources/expected_Request.txt";
        ManagementFile.deleteFile(expectResultFile);
        String expectBadRequest = "src/test/resources/expected_bad_request_Execution.txt";
        ManagementFile.deleteFile(expectBadRequest);

        //Clean collection
        database.getDataBase().getCollection("audit").drop();
        database.getDataBase().createCollection("audit");
    }

    @Test
    @DisplayName("Verify the POST, UPDATE, GETBy, DELETE and GETALL is persisting in DB")
    public void verifyHappyPathLocalCRUD() throws Exception {
        String pathExpectResult = "src/test/resources/expected_Request.txt";
        List<BodyDTO> expected = BodyFileMapper.ListBodyFileDTOFromFile(pathExpectResult);

        List<Document> records = database.getCollection("audit").find(or(
                eq("executionStatus", "SUCCESS"))
        ).into(new ArrayList<>());

        for (BodyDTO bodyDTO : expected) {
            for (Document record : records) {
                String id = record.get("_id").toString();
                switch (bodyDTO.getMethod()) {
                    case "POST-product:": {
                        String saveBody = record.get("result").toString();
                        if (saveBody.contains("name=" + bodyDTO.getName()) && record.get("methodName").equals("create") && !isSelected(id)) {
                            Assertions.assertTrue(saveBody.contains("id=" + bodyDTO.getId()), "The id is NOT MATCHED. The expected is" + bodyDTO.getId());
                            Assertions.assertTrue(saveBody.contains("description=" + bodyDTO.getDescription()), "The description is NOT MATCHED. The expected is" + bodyDTO.getDescription());
                            Assertions.assertTrue(saveBody.contains("price=" + bodyDTO.getPrice()), "The price is NOT MATCHED. The expected is" + bodyDTO.getPrice());
                            Assertions.assertTrue(saveBody.contains("stock=" + bodyDTO.getStock()), "The stock is NOT MATCHED. The expected is" + bodyDTO.getStock());
                            Assertions.assertEquals("product-service", record.get("serviceName"));
                            Assertions.assertEquals("ProductServiceImpl", record.get("className"));
                            Assertions.assertEquals("SUCCESS", record.get("executionStatus"));
                            Assertions.assertEquals("201", bodyDTO.getStatus());
                            listId.add(id);
                            logger.debug("POST: "+record.toJson());
                        }
                        break;
                    }
                    case "PUT-product:": {
                        String saveBody = record.get("result").toString();
                        if (saveBody.contains("name=" + bodyDTO.getName()) && record.get("methodName").equals("update") && !isSelected(id)) {
                            String idActual = ((ArrayList) (record.get("parameters"))).get(0).toString();
                            Assertions.assertEquals(idActual, bodyDTO.getId());
                            Assertions.assertTrue(saveBody.contains("id=" + bodyDTO.getId()), "The id is NOT MATCHED. The expected is" + bodyDTO.getId());
                            Assertions.assertTrue(saveBody.contains("description=" + bodyDTO.getDescription()), "The description is NOT MATCHED. The expected is" + bodyDTO.getDescription());
                            Assertions.assertTrue(saveBody.contains("description=" + bodyDTO.getDescription()), "The description is NOT MATCHED. The expected is" + bodyDTO.getDescription());
                            Assertions.assertTrue(saveBody.contains("price=" + bodyDTO.getPrice()), "The price is NOT MATCHED. The expected is" + bodyDTO.getPrice());
                            Assertions.assertTrue(saveBody.contains("stock=" + bodyDTO.getStock()), "The stock is NOT MATCHED. The expected is" + bodyDTO.getStock());
                            Assertions.assertEquals("product-service", record.get("serviceName"));
                            Assertions.assertEquals("ProductServiceImpl", record.get("className"));
                            Assertions.assertEquals("SUCCESS", record.get("executionStatus"));
                            Assertions.assertEquals("201", bodyDTO.getStatus());
                            listId.add(id);
                            logger.debug("PUT: "+record.toJson());
                        }
                        break;
                    }
                    case "DELETE-product:": {
                        if ("delete".equals(record.get("methodName")) && !isSelected(id)) {
                            String idActual = ((ArrayList) (record.get("parameters"))).get(0).toString();
                            if (idActual.equals(bodyDTO.getId())) {
                                Assertions.assertEquals("product-service", record.get("serviceName"));
                                Assertions.assertEquals("ProductServiceImpl", record.get("className"));
                                Assertions.assertEquals("SUCCESS", record.get("executionStatus"));
                                Assertions.assertEquals("No result", record.get("result"));
                                Assertions.assertEquals("200", bodyDTO.getStatus());
                                listId.add(id);
                                logger.debug("DELETE: "+record.toJson());
                            }
                        }
                        break;
                    }
                    case "GETby-product:": {
                        if ("getById".equals(record.get("methodName")) && !isSelected(id)) {
                            String idActual = ((ArrayList<?>) (record.get("parameters"))).get(0).toString();
                            if (idActual.equals(bodyDTO.getId())) {
                                Assertions.assertTrue(record.get("result").toString().contains("id=" + bodyDTO.getId()), "The id is NOT MATCHED. The expected is" + bodyDTO.getId());
                                Assertions.assertEquals("product-service", record.get("serviceName"));
                                Assertions.assertEquals("ProductServiceImpl", record.get("className"));
                                Assertions.assertEquals("SUCCESS", record.get("executionStatus"));
                                Assertions.assertEquals("200", bodyDTO.getStatus());
                                listId.add(id);
                                logger.debug("GETby: "+record.toJson());
                            }
                        }
                        break;
                    }
                    case "GETAll-product:": {
                        if ("getAll".equals(record.get("methodName")) && !isSelected(id)) {
                            Assertions.assertEquals(record.get("serviceName"), "product-service");
                            Assertions.assertEquals(record.get("className"), "ProductServiceImpl");
                            Assertions.assertEquals(record.get("executionStatus"), "SUCCESS");
                            Assertions.assertEquals("200", bodyDTO.getStatus());
                            listId.add(id);
                            logger.debug("GETAll: "+record.toJson());
                        }
                        break;
                    }
                    default: {
                        logger.error("Something is wrong may be: Problem in txt file, only the input valid are POST-product:, PUT-product: and DELETE-product:");
                    }
                }


            }
        }
        Assertions.assertEquals(expected.size(), listId.size(), "The number record not founded are: " + Math.abs(expected.size() - listId.size()));
    }

    @Test
    @DisplayName("Verify BATH Request is persisting in DB")
    public void verifyBathRequestIsRegisterLocalCRUD() throws Exception {
        String pathExpectResult = "src/test/resources/expected_bad_request_Execution.txt";
        Map<String, String> expected = BodyFileMapper.mapFromFile(pathExpectResult);

        List<Document> records = database.getCollection("audit").find(or(
                eq("executionStatus", "VALIDATION_FAILED"),
                eq("executionStatus", "EXCEPTION"))
        ).into(new ArrayList<>());

        int nameSizeErrorCount = 0;
        int emptyErrorCount = 0;
        int negativePriceCount = 0;
        int negativeStockCount = 0;
        int searchByIDErrorCount = 0;

        for (Document record : records) {
            String exceptionMessage = record.get("exceptionMessage").toString();
            if ("VALIDATION_FAILED".equals(record.get("executionStatus"))) {
                if (exceptionMessage.contains("{product.stock.min}")) {
                    negativeStockCount++;
                }
                if (exceptionMessage.contains("{product.name.size}")) {
                    nameSizeErrorCount++;
                }
                if (exceptionMessage.contains("{product.price.min}")) {
                    negativePriceCount++;
                }
                if (exceptionMessage.contains("BeanPropertyBindingResult: 4 errors")) {
                    emptyErrorCount++;
                }
                Assertions.assertEquals("product-service", record.get("serviceName"));
                Assertions.assertEquals("ProductController", record.get("className"));
                Assertions.assertEquals("create", record.get("methodName"));
                Assertions.assertEquals("400 BAD_REQUEST", record.get("result"));
                Assertions.assertEquals("MethodArgumentNotValidException", record.get("exceptionName"));
            }
            if ("EXCEPTION".equals(record.get("executionStatus"))) {
                Assertions.assertEquals("product-service", record.get("serviceName"));
                Assertions.assertEquals("ProductServiceImpl", record.get("className"));
                Assertions.assertEquals("getById", record.get("methodName"));
                Assertions.assertEquals("No result", record.get("result"));
                Assertions.assertEquals("EntityNotFoundException", record.get("exceptionName"));
                Assertions.assertEquals("Product with id 0 not found", record.get("exceptionMessage"));
                searchByIDErrorCount++;
            }
        }

        int nExpectedNameSize = Integer.parseInt(expected.get("POST-WRONG-NameSizeMin")) + Integer.parseInt(expected.get("POST-WRONG-NameSizeMax"));
        Assertions.assertEquals(nExpectedNameSize, nameSizeErrorCount,
                "The number record not founded for POST-WRONG-NameSizeMin are: " + Math.abs(nExpectedNameSize - nameSizeErrorCount));
        Assertions.assertEquals(Integer.parseInt(expected.get("POST-WRONG-EmptyBody")), emptyErrorCount,
                "The number record not founded for POST-WRONG-EmptyBody are: " + Math.abs(Integer.parseInt(expected.get("POST-WRONG-EmptyBody")) - emptyErrorCount));
        Assertions.assertEquals(Integer.parseInt(expected.get("POST-WRONG-NegativeStock")), negativeStockCount,
                "The number record not founded for POST-WRONG-NegativeStock are: " + Math.abs(Integer.parseInt(expected.get("POST-WRONG-NegativeStock")) - negativeStockCount));
        Assertions.assertEquals(Integer.parseInt(expected.get("POST-WRONG-NegativePrice")), negativePriceCount,
                "The number record not founded for POST-WRONG-NegativePrice are: " + Math.abs(Integer.parseInt(expected.get("POST-WRONG-NegativePrice")) - negativePriceCount));
        Assertions.assertEquals(Integer.parseInt(expected.get("GETBy-WRONG-Id0")), searchByIDErrorCount,
                "The number record not founded for POST-WRONG-Id0 are: " + Math.abs(Integer.parseInt(expected.get("GETBy-WRONG-Id0")) - searchByIDErrorCount));

        int totalFound = nameSizeErrorCount + emptyErrorCount + negativeStockCount + negativePriceCount + searchByIDErrorCount;
        Assertions.assertEquals(totalFound, records.size());
    }

    public boolean isSelected(String id) {
        return listId.stream().findFirst().equals(id);
    }
}
