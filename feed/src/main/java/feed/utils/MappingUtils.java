package feed.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feed.exception.ErrorCode;
import feed.exception.InternalException;
import org.springframework.http.HttpStatus;

public class MappingUtils {

    private final static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException ex) {
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST);
        }
    }
}
