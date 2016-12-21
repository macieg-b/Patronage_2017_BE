package bartlomiejczyk.maciej.controllers;


import bartlomiejczyk.maciej.domain.Actor;
import bartlomiejczyk.maciej.domain.Movie;
import bartlomiejczyk.maciej.repositories.ActorRepository;
import bartlomiejczyk.maciej.repositories.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MovieRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Movie> movieList = new ArrayList<>();

    private List<Actor> actorList = new ArrayList<>();

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        movieRepository.deleteAllInBatch();
        actorRepository.deleteAllInBatch();

        movieList.add(movieRepository.save(new Movie("Listy do M.")));
        movieList.add(movieRepository.save(new Movie("Lord of the rings")));
        movieList.add(movieRepository.save(new Movie("Hobbit")));
        movieList.add(movieRepository.save((new Movie("Game of thrones"))));

        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))), "Ian McKellen")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(2)))), "Orlando Bloom")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(1), movieList.get(3)))), "Sean Bean")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(0)))), "Tomasz Karolak")));
        actorList.add(actorRepository.save(new Actor(new HashSet<Movie>(new ArrayList<>(Arrays.asList(movieList.get(0)))), "Agnieszka Dygant")));
    }

    @Test
    public void movieNotFound() throws Exception {
        mockMvc.perform(get("/movies/999")
                .content(this.json(new Actor()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleMovie() throws Exception {
        mockMvc.perform(get("/movies/"+ movieList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.title", is(movieList.get(0).title)));
    }

    @Test
    public void readMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(movieList.get(0).title)))
                .andExpect(jsonPath("$[1].id", is(movieList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(movieList.get(1).title)))
                .andExpect(jsonPath("$[2].id", is(movieList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].title", is(movieList.get(2).title)))
                .andExpect(jsonPath("$[3].id", is(movieList.get(3).getId().intValue())))
                .andExpect(jsonPath("$[3].title", is(movieList.get(3).title)));
    }

    @Test
    public void createMovie() throws Exception {
        String movieJson = json(new Movie("Title", Integer.toUnsignedLong(2)));

        this.mockMvc.perform(post("/movies")
                .contentType(contentType)
                .content(movieJson))
                .andExpect(status().isCreated());
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
