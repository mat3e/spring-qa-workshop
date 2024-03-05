package io.github.mat3e.downloads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.Clock;

@SpringBootApplication
public class DownloadsApplication {

	@Bean
	Clock clock() {
		return Clock.systemUTC();
	}

	@Bean
	DataSource dataSource() {
		var result = new DriverManagerDataSource("jdbc:h2:file:./filedb;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
		result.setDriverClassName("org.h2.Driver");
		return result;
	}

	public static void main(String[] args) {
		SpringApplication.run(DownloadsApplication.class, args);
	}

}
