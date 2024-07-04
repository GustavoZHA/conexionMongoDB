package product.Mapper;

import product.dto.BodyDTO;
import utils.ManagementFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyFileMapper {

    public static BodyDTO bodyFileDTOFromString(String input) {
        BodyDTO bodyDTO = new BodyDTO();

        String[] body = input.split(" ");

        bodyDTO.setMethod(body[0]);
        bodyDTO.setId(body[1]);

        if (body.length > 3) {
            bodyDTO.setName(body[2]);
            bodyDTO.setDescription(body[3]);
            bodyDTO.setPrice(body[4]);
            bodyDTO.setStock(body[5]);
        }

        bodyDTO.setStatus(body[body.length - 1]);

        return bodyDTO;
    }

    public static List<BodyDTO> ListBodyFileDTOFromFile(String path) throws Exception {
        List<String> rowFiles = ManagementFile.readTextFile(path);

        List<BodyDTO> list = new ArrayList<>();
        rowFiles.forEach(r -> {
            if (r != null) list.add(bodyFileDTOFromString(r));
        });
        return list;
    }

    public static Map<String, String> mapFromFile(String path) throws Exception {
        Map<String, String> mapValues = new HashMap<>();
        List<String> rowFiles = ManagementFile.readTextFile(path);

        rowFiles.forEach(r -> {
            String[] value = r.split("_");
            if (r != null & !r.contains("null")) {
                mapValues.put(value[0], value[1]);
            }
        });
        return mapValues;
    }
}
