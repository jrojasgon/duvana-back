package com.inopek.deserializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


@Transactional
public class ImageDaoDeserializer extends JsonDeserializer<Blob> {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * create a blob from image base 64
	 */
	@Override
	public Blob deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException, JsonProcessingException {
		String encodedImage = jsonParser.getText();
		if (!StringUtils.isEmpty(encodedImage)) {
			byte[] decodedByte = Base64.decodeBase64(encodedImage);
			Blob blob = sessionFactory.getCurrentSession().getLobHelper().createBlob(decodedByte);
			return blob;
		}
		return null;
	}
	
}
