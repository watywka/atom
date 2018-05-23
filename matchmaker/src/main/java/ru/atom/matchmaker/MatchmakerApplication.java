package ru.atom.matchmaker;

import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MatchmakerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchmakerApplication.class, args);
	}

}
