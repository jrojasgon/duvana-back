package com.inopek.deserializer;

import org.springframework.beans.factory.annotation.Value;

public class ImagePathAfterDeserializer extends AbstractPathDeserializer {
	
	@Value("${image.after.path}")
	private String path;

	@Override
	protected String getPath() {
		return path;
	}
}
