package Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonTest1 {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // ------------------------------------------------------
        Evil evil = new Evil();
        evil.name = "HACKER";
        evil.age = 1;
        String json = objectMapper.writeValueAsString(evil);
        System.out.println(json);

        Evil newEvil = objectMapper.readValue(json, Evil.class);
        System.out.println(newEvil);
    }
}
