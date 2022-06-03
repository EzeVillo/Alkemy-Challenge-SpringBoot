package com.alkemy.challenge;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.alkemy.challenge.Entities.Character;
import com.alkemy.challenge.Entities.Genre;
import com.alkemy.challenge.Entities.Movie;
import com.alkemy.challenge.Entities.User;
import com.alkemy.challenge.Entities.UserRole;
import com.alkemy.challenge.Repositories.CharacterRepository;
import com.alkemy.challenge.Repositories.GenreRepository;
import com.alkemy.challenge.Repositories.MovieRepository;
import com.alkemy.challenge.Repositories.UserRepository;
import com.alkemy.challenge.Services.Interfaces.ImageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(MovieRepository movieRepository, GenreRepository genreRepository,
			CharacterRepository characterRepository, UserRepository userRepository,
			final PasswordEncoder passwordEncoder, ImageService imageService) {
		return args -> {

			// Creacion de genero
			Genre genre = new Genre();
			genre.setName("Accion");
			genreRepository.save(genre);

			// Creacion de personajes
			List<Character> characters = Arrays.asList(
					Character.builder()
							.age(53)
							.history(
									"Iron Man es un superhéroe que aparece en los cómics estadounidenses publicados por Marvel Comics. El personaje fue cocreado por el escritor y editor Stan Lee, desarrollado por el guionista Larry Lieber y diseñado por los artistas Don Heck y Jack Kirby.")
							.name("Iron Man")
							.weight(102)
							.build(),
					Character.builder()
							.age(1500)
							.history(
									"Thor es un personaje importante en el Universo Cinematográfico Marvel, siendo el único Vengador que no es de la Tierra. Thor empuña el martillo de guerra místico Mjolnir, que controla el clima, pero él también tiene fuerza, durabilidad y agilidad similares a las de un dios.")
							.name("Thor")
							.weight(98)
							.build(),
					Character.builder()
							.age(39)
							.history(
									"El Capitán América, cuyo nombre real es Steven Grant Rogers, es un superhéroe ficticio que aparece en los cómics estadounidenses publicados por Marvel Comics.")
							.name("Capitán América")
							.weight(109)
							.build(),
					Character.builder()
							.age(90)
							.history(
									"La primera y más conocida Viuda Negra, es una agente rusa entrenada como espía, artista marcial y francotiradora, y equipada con un arsenal de armas de alta tecnología, que incluye un par de armas energéticas montadas en la muñeca")
							.name("Black Widow")
							.weight(59)
							.build(),
					Character.builder()
							.age(1000)
							.history(
									"Thanos es un megavillano ficticio que aparece en los cómics estadounidenses publicados por Marvel Comics. Creado por el escritor y artista Jim Starlin, el personaje apareció por primera vez en The Invincible Iron Man # 55")
							.name("Thanos")
							.weight(448)
							.build());
			characterRepository.saveAll(characters);

			// Creacion de peliculas
			List<Movie> movies = Arrays.asList(
					Movie.builder()
							.score(5)
							.creationDate(LocalDate.of(2018, 4, 26))
							.genre(genre)
							.title("Avengers: Infinity War")
							.build(),
					Movie.builder()
							.score(4)
							.creationDate(LocalDate.of(2008, 4, 30))
							.genre(genre)
							.title("Iron Man: el hombre de hierro")
							.build(),
					Movie.builder()
							.score(4)
							.creationDate(LocalDate.of(2011, 4, 28))
							.genre(genre)
							.title("Thor")
							.build(),
					Movie.builder()
							.score(3)
							.creationDate(LocalDate.of(2011, 7, 28))
							.genre(genre)
							.title("Capitán América: el primer vengador")
							.build(),
					Movie.builder()
							.score(3)
							.creationDate(LocalDate.of(2021, 7, 9))
							.genre(genre)
							.title("Black Widow")
							.build());

			movies.get(0).getCharacters().addAll(characters);
			movies.get(1).getCharacters().add(characters.get(0));
			movies.get(2).getCharacters().add(characters.get(1));
			movies.get(3).getCharacters().add(characters.get(2));
			movies.get(4).getCharacters().add(characters.get(3));

			movieRepository.saveAll(movies);

			// Creacion de usuario administrador
			userRepository.save(
					User.builder()
							.email("MailDePrueba@gmail.com")
							.password(passwordEncoder.encode("123"))
							.roles(new HashSet<>(Arrays.asList(UserRole.ADMIN)))
							.username("ADMIN")
							.build());

			// Resetear imagenes
			imageService.deleteAll();
			imageService.init();
		};
	}

}
