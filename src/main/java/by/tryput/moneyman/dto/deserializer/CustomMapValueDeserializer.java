package by.tryput.moneyman.dto.deserializer;

import by.tryput.moneyman.dto.ValueMap;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class CustomMapValueDeserializer extends KeyDeserializer {

    @Override
    public ValueMap deserializeKey(
            String key,
            DeserializationContext ctxt) {
        return new ValueMap(key);
    }
}
