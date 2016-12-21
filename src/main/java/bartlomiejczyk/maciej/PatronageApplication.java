package bartlomiejczyk.maciej;

import bartlomiejczyk.maciej.domain.Actor;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class PatronageApplication {
	public static void main(String[] args) {SpringApplication.run(PatronageApplication.class, args);}

}