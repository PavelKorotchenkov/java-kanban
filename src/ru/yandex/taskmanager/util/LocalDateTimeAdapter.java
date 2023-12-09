package ru.yandex.taskmanager.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Override
	public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
		if (localDateTime == null) {
			jsonWriter.nullValue();
		} else {
			jsonWriter.value(dateTimeFormatter.format(localDateTime));
		}
	}

	@Override
	public LocalDateTime read(JsonReader jsonReader) throws IOException {
		if (jsonReader.peek() == JsonToken.NULL) {
			jsonReader.nextNull();
			return null;
		} else {
			String dateString = jsonReader.nextString();
			return LocalDateTime.parse(dateString, dateTimeFormatter);
		}
	}
}
