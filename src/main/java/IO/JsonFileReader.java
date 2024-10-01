package IO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {
    // C:\Users\xncee\OneDrive\Desktop\HowToUseJson
    File file;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode node;

    public JsonFileReader(String location) {
        file = new File(location);
        try {
            //ObjectNode newNode = objectMapper.createObjectNode();
            node = (ObjectNode) objectMapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(node.toPrettyString());
    }

    public ObjectNode getNode() {
        return node;
    }
}
