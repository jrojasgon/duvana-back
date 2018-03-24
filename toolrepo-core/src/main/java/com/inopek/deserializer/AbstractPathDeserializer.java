package com.inopek.deserializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class AbstractPathDeserializer extends JsonDeserializer<String> {
	
	protected abstract String getPath();
	
	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String encodedImage = jsonParser.getValueAsString();
		if (StringUtils.isNotEmpty(encodedImage)) {
			byte[] decodedByte = Base64.decodeBase64(encodedImage);
			final String formatedDate = LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ISO_DATE);
			final String fileName = formatedDate + UUID.randomUUID().toString() + ".png";
			final File dir = new File(getPath(), formatedDate);
			final Path destinationFile = Paths.get(dir.getPath(), fileName);
			Files.write(destinationFile, decodedByte);
			return destinationFile.toString();
		}
		return null;
	}
}
