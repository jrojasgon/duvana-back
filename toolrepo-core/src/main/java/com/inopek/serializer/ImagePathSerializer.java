package com.inopek.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ImagePathSerializer extends JsonSerializer<String> {
	
	private static final Logger	LOGGER	= LoggerFactory.getLogger(ImagePathSerializer.class);


	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		File file = new File(value);
		try (FileInputStream imageInFile = new FileInputStream(file)) {
			// Reading a Image file from file system
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);
			gen.writeString(Base64.getEncoder().encodeToString(imageData));
		} catch (FileNotFoundException e) {
			LOGGER.error("Image not found" + e);
		} catch (IOException ioe) {
			LOGGER.error("Exception while reading the Image " + ioe);
		}
	}

}
