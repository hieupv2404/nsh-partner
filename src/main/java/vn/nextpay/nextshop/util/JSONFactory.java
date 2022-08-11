package vn.nextpay.nextshop.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class JSONFactory {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(JSONFactory.class);

    public static String toString(Object o) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return o.toString();
        }
    }

    public static Object toObject(String obj, Class<?> tClass) throws IOException {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            return objectMapper.readValue(obj, tClass);
        } catch (IOException e) {
            throw e;
        }
    }
}
