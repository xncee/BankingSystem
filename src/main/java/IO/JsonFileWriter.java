package IO;

import IO.JsonFileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonFileWriter {
    // ObjectNode jsonNode = newJsonNode();
    // jsonNode.put("key", "value");
    // jsonNode.put("key1", "value2");
    // ObjectNode newNode1 = newJsonNode();
    // newNode1.put("m1", new int[] {1, 2, 3});
    // ObjectNode newNode2 = newJsonNode();
    // newNode2.put("sub-key1", "saif");
    // newNode2.put("sub-key2", "ahmad");
    // newNode1.put("m2", newNode2);

    // jsonNode.put("key2", newNode1);
    File file;
    ObjectMapper mapper = new ObjectMapper();
    private ObjectNode node;

    public JsonFileWriter(String location) {
        file = new File(location);
        if (!file.exists()) {
            try {
                file.createNewFile();
                node = mapper.createObjectNode();
                mapper.writeValue(file, node);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            node = new JsonFileReader(location).getNode();
            //System.out.println(jsonNode.toString());
        }
//        try {
//            node = (ObjectNode) mapper.readTree(file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
    public ObjectNode getNode() {
        return node;
    }
    public static ObjectNode getNewNode() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createObjectNode();
    }

    public void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
