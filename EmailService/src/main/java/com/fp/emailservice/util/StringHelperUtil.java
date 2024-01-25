package com.fp.emailservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Collection of static methods that manipulate string
 */
public final class StringHelperUtil {


    private StringHelperUtil() {}
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * converts a string input from string to json using Jackson
     * @param input String to be converted JSON
     * @return JSON view of the string
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String stringToJSON(String input) throws JsonProcessingException {
        return objectMapper.writeValueAsString(input);
    }
}
