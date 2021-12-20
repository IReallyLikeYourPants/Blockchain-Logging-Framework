package blf.blockchains.algorand;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

// Transform json to csv file
public class json_To_Csv {
    public static void createCsv(String bcqlFile) {
        try {
            JsonNode jsonTree = new ObjectMapper().readTree(new File(bcqlFile + "/output.json"));
            Builder csvSchemaBuilder = CsvSchema.builder();
            JsonNode firstObject = jsonTree.elements().next();
            firstObject.fieldNames().forEachRemaining(fieldName -> { csvSchemaBuilder.addColumn(fieldName); });
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(JsonNode.class).with(csvSchema).writeValue(new File(bcqlFile + "/output.csv"), jsonTree);
        } catch (Exception e) {}
    }
}
