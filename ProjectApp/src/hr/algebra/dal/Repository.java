/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Tin
 */
public interface Repository {
    int createActor(Person actor) throws Exception;
    void createActors(List<Person> actors) throws Exception;    
    void updateActor(int id, Person data) throws Exception;
    void deleteActor(int id) throws Exception;
    Optional<Person> selectActor(int id) throws Exception;
    List<Person> selectActors() throws Exception;
    
    int createDirector(Person director) throws Exception;
    void createDirectors(List<Person> directors) throws Exception;    
    void updateDirector(int id, Person data) throws Exception;
    void deleteDirector(int id) throws Exception;
    Optional<Person> selectDirector(int id) throws Exception;
    List<Person> selectDirectors() throws Exception;
    
    int createMovie(Movie movie) throws Exception;
    void createMovies(List<Movie> movies) throws Exception;    
    void updateMovie(int id, Movie data) throws Exception;
    void deleteMovie(int id) throws Exception;
    Optional<Movie> selectMovie(int id) throws Exception;
    List<Movie> selectMovies() throws Exception;
    List<Movie> selectMoviesFull() throws Exception;
    
    int createUser(User newUser) throws Exception;
    Optional<User> selectUser(String username) throws Exception;
    
    void createMovieActor(int MovieID, int ActorID) throws Exception;
    void createMovieActors(int MovieID, List<Person> actors) throws Exception;    
    void deleteMovieActor(int MovieID, int ActorID) throws Exception;
    List<Person> selectMovieActors(int MovieID) throws Exception;
    
    void createMovieDirector(int MovieID, int DirectorID) throws Exception;
    void createMovieDirectors(int MovieID, List<Person> directors) throws Exception;    
    void deleteMovieDirector(int MovieID, int DirectorID) throws Exception;
    List<Person> selectMovieDirectors(int MovieID) throws Exception;

    void clearAllData(User user) throws Exception;
    void clearAllData() throws Exception;
}
