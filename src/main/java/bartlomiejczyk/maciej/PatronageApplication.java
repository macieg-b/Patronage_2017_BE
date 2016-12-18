package bartlomiejczyk.maciej;

import bartlomiejczyk.maciej.models.Actor;
import bartlomiejczyk.maciej.models.Movie;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class PatronageApplication {
	public static void main(String[] args) {SpringApplication.run(PatronageApplication.class, args);}

	@Bean
	CommandLineRunner init (MovieRepository movieRepository, ActorRepository actorRepository){
		return (evt) -> Arrays.asList("Titanic,GoT,HouseOfCards".split(","))
				.forEach(
						a ->{
							Movie movie = movieRepository.save(new Movie(a, "http://localhost:8080/" + a));
							actorRepository.save(new Actor(movie, "Johny Deep"));
							actorRepository.save(new Actor(movie, "James Bond"));
						}
				);
	}
}