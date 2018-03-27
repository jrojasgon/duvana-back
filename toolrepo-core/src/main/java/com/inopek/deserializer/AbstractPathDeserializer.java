package com.inopek.deserializer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class AbstractPathDeserializer extends JsonDeserializer<String> {
	
	private static final String RESIZED_PREFIX = "-resized";
	private static final String IMAGE_EXTENSION = ".png";
	
	protected abstract String getPath();
	
	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String encodedImage = jsonParser.getValueAsString();
		if (StringUtils.isNotEmpty(encodedImage)) {
			byte[] decodedByte = Base64.decodeBase64(encodedImage);
			final String formatedDate = LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ISO_DATE);
			final String fileName = formatedDate + UUID.randomUUID().toString();
			final File dir = new File(getPath(), formatedDate);
			dir.mkdir();
			
			final Path destinationFile = Paths.get(dir.getPath(), fileName + IMAGE_EXTENSION);
			// write original image file
			final Path originalPath = Files.write(destinationFile, decodedByte);
			
	        BufferedImage image = ImageIO.read(originalPath.toFile());
	        BufferedImage resized = resize(image, 80, 240);
	        File output = new File(dir.getPath().toString() + "\\" + fileName + RESIZED_PREFIX + IMAGE_EXTENSION);
	        // write resized image file
	        ImageIO.write(resized, "png", output);
			return destinationFile.toString();
		}
		return null;
	}
	
	private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
