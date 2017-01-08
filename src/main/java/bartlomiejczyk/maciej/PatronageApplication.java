package bartlomiejczyk.maciej;

import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
public class PatronageApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatronageApplication.class, args);
    }

    @Bean
    CommandLineRunner init(MovieRepository movieRepository) {
        return (args) -> {
            String[] moviesTitle = new String[]{
                    "Movie one",
                    "Movie two",
                    "Movie three",
                    "Movie four",
                    "Movie five",
                    "Movie six",
                    "Movie seven",
                    "Movie eight",
                    "Movie nine",
                    "Movie ten"
            };
            String[] categories = new String[]{
                    "new",
                    "best",
                    "others"
            };
            Arrays.asList(categories).forEach(
                    category -> Arrays.asList(moviesTitle).forEach(
                            title -> movieRepository.save(new Movie(title, category))
                    )
            );
        };
    }
}
