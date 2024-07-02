import org.junit.jupiter.api.Test;
import utils.ReaderFile;

public class UniTest {
    @Test
    public void readerFileTest() throws Exception {
        String path = "src/test/resources/result.txt";
        ReaderFile.readTextFile(path);
        ReaderFile.readTextFileShow(path);
    }
}
