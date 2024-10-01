package BitsAndBucks;

import IO.JsonFileWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Data {
    JsonFileWriter accountsWriter = new JsonFileWriter("accounts.json");
    ObjectNode accountsNode = accountsWriter.getNode();
    JsonFileWriter transactionsWriter = new JsonFileWriter("transactions.json");
    ObjectNode transactionsNode = transactionsWriter.getNode();
}