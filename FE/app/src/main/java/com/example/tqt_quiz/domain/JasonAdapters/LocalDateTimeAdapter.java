package com.example.tqt_quiz.domain.JasonAdapters;

import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime>
{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat);

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String value = in.nextString();
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(value, formatter);
    }
}
