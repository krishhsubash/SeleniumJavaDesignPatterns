package facade.file;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * JSON File Handler - Subsystem component for Facade pattern
 * Handles JSON file read/write operations using Jackson library
 */
public class JSONFileHandler {

    private final ObjectMapper objectMapper;

    public JSONFileHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Read JSON file and deserialize to object
     *
     * @param filePath Path to JSON file
     * @param valueType Class type to deserialize to
     * @param <T> Type parameter
     * @return Deserialized object
     * @throws IOException if file operation fails
     */
    public <T> T readJSON(String filePath, Class<T> valueType) throws IOException {
        File file = new File(filePath);
        T result = objectMapper.readValue(file, valueType);

        System.out.println("✓ JSON file read successfully: " + filePath);
        System.out.println("  Deserialized to: " + valueType.getSimpleName());
        return result;
    }

    /**
     * Read JSON file as Map
     *
     * @param filePath Path to JSON file
     * @return Map representation of JSON
     * @throws IOException if file operation fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> readJSONAsMap(String filePath) throws IOException {
        File file = new File(filePath);
        Map<String, Object> result = objectMapper.readValue(file, Map.class);

        System.out.println("✓ JSON file read as Map: " + filePath);
        System.out.println("  Keys found: " + result.size());
        return result;
    }

    /**
     * Write object to JSON file
     *
     * @param filePath Path to JSON file
     * @param data Object to serialize
     * @throws IOException if file operation fails
     */
    public void writeJSON(String filePath, Object data) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, data);

        System.out.println("✓ JSON file written successfully: " + filePath);
        System.out.println("  Object serialized: " + data.getClass().getSimpleName());
    }

    /**
     * Convert object to JSON string
     *
     * @param data Object to serialize
     * @return JSON string
     * @throws IOException if serialization fails
     */
    public String toJSONString(Object data) throws IOException {
        String json = objectMapper.writeValueAsString(data);
        System.out.println("✓ Object converted to JSON string");
        return json;
    }

    /**
     * Parse JSON string to object
     *
     * @param jsonString JSON string
     * @param valueType Class type to deserialize to
     * @param <T> Type parameter
     * @return Deserialized object
     * @throws IOException if parsing fails
     */
    public <T> T fromJSONString(String jsonString, Class<T> valueType) throws IOException {
        T result = objectMapper.readValue(jsonString, valueType);
        System.out.println("✓ JSON string parsed to: " + valueType.getSimpleName());
        return result;
    }
}

