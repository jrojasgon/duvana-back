package com.inopek;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@EnableAutoConfiguration
@PropertySource("file:/home/ec2-user/duvana-back/application.properties")
public class WebApplication {

	public static void main(String[] args) {
		//System.setProperty("Dspring.config.location","file:C:\\DEV\\duvana\\application.properties");
		new SpringApplicationBuilder().bannerMode(Banner.Mode.CONSOLE)
				.sources(CoreApplication.class, WebApplication.class).run(args);
	}

}
