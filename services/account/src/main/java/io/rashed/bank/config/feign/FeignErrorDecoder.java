package io.rashed.bank.config.feign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.rashed.bank.common.exception.customer.CustomerNotFoundException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMsg = getErrorMessage(response);
        return switch (response.status()) {
            case 400 ->
                    new BadRequestException(errorMsg);
            case 404 ->
                    new CustomerNotFoundException(errorMsg);
            case 500 ->
                    new InternalServerErrorException(errorMsg);
            default -> new Exception("Generic error");
        };
    }

    private String getErrorMessage(Response response) {
        Response.Body body = response.body();
        if (body == null) {
            return "No error message available.";
        }
        try {
            // Read response body as string
            String responseBody = StreamUtils.copyToString(body.asInputStream(), StandardCharsets.UTF_8);

            // Use Jackson to parse JSON and extract "message"
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            return jsonNode.has("message") ? jsonNode.get("message").asText() : "No message provided.";

        } catch (IOException exception) {
            return "Failed to read error message.";
        }
    }
}

