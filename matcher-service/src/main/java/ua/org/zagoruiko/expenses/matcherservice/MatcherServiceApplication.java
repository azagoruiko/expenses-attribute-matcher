package ua.org.zagoruiko.expenses.matcherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatcherServiceApplication {

	public static void main(String[] args) {
		System.out.println(System.getProperty("MYSQL_URL"));
		SpringApplication.run(MatcherServiceApplication.class, args);
	}

}
