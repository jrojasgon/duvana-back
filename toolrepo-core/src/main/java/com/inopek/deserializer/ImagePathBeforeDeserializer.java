package com.inopek.deserializer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;

@Transactional
public class ImagePathBeforeDeserializer extends AbstractPathDeserializer {
	
	@Value("${image.before.path}")
	private String path;

	@Override
	protected String getPath() {
		return path;
	}

}
