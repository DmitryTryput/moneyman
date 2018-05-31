package by.tryput.moneyman.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


public class CustomTaskSerializer extends StdSerializer<Task> {

    public CustomTaskSerializer() {
        this(null);
    }

    public CustomTaskSerializer(Class<Task> t) {
        super(t);
    }

    @Override
    public void serialize(Task task, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", task.getName());
        jsonGenerator.writeEndObject();
    }
}
