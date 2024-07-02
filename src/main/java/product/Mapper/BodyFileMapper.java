package product.Mapper;

import product.dto.BodyFileDTO;
import utils.ReaderFile;

import java.util.ArrayList;
import java.util.List;

public class BodyFileMapper {

    public static BodyFileDTO bodyFileDTOFromString(String input) {
        BodyFileDTO bodyFileDTO = new BodyFileDTO();

        String[] body = input.split(" ");

        bodyFileDTO.setMethod(body[0]);
        bodyFileDTO.setId(body[1]);

        if (body.length > 3) {
            bodyFileDTO.setName(body[2]);
            bodyFileDTO.setDescription(body[3]);
            bodyFileDTO.setPrice(body[4]);
            bodyFileDTO.setStock(body[5]);
        }

        bodyFileDTO.setStatus(body[body.length - 1]);

        return bodyFileDTO;
    }

    public static List<BodyFileDTO> ListBodyFileDTOFromFile(String path) throws Exception {
        List<String> rowFiles = ReaderFile.readTextFile(path);

        List<BodyFileDTO> list = new ArrayList<>();
        rowFiles.forEach(r -> {
            if (r != null) list.add(bodyFileDTOFromString(r));
        });
        return list;
    }
}
