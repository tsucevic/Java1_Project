/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.User;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author Tin
 */
public class SqlRepository implements Repository {

    private static final String ID_ACTOR = "IDActor";
    private static final String ID_DIRECTOR = "IDDirector";
    private static final String PERSON_ID = "PersonID";
    private static final String FULL_NAME = "FullName";
    private static final String ALTERNATE_NAME = "AlternateName";

    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";
    private static final String ORIGINAL_TITLE = "OriginalTitle";
    private static final String DESCRIPTION_HTML = "DescriptionHTML";
    private static final String LENGTH = "Length";
    private static final String GENRE = "Genre";
    private static final String POSTER_LINK = "PosterLink";
    private static final String TRAILER_LINK = "TrailerLink";
    private static final String LINK = "Link";
    private static final String GUID = "GUID";
    private static final String STARTS_PLAYING = "StartsPlaying";

    private static final String ID_USER = "IDUser";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String ROLE = "Person";
    private static final String ACCESS_LEVEL = "AccessLevel";

    private static final String PROC_CREATE_MOVIE = "{ CALL proc_create_movie  (?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String PROC_READ_MOVIE = "{ CALL proc_read_movie  (?) }";
    private static final String PROC_READ_MOVIES = "{ CALL proc_read_movies }";
    private static final String PROC_UPDATE_MOVIE = "{ CALL proc_update_movie  (?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String PROC_DELETE_MOVIE = "{ CALL proc_delete_movie (?) }";

    private static final String PROC_CREATE_ACTOR = "{ CALL proc_create_actor  (?,?) }";
    private static final String PROC_READ_ACTOR = "{ CALL proc_read_actor (?) }";
    private static final String PROC_READ_ACTORS = "{ CALL proc_read_actors }";
    private static final String PROC_UPDATE_ACTOR = "{ CALL proc_update_actor  (?,?,?) }";
    private static final String PROC_DELETE_ACTOR = "{ CALL proc_delete_actor  (?) }";

    private static final String PROC_CREATE_DIRECTOR = "{ CALL proc_create_director  (?,?) }";
    private static final String PROC_READ_DIRECTOR = "{ CALL proc_read_director  (?) }";
    private static final String PROC_READ_DIRECTORS = "{ CALL proc_read_directors }";
    private static final String PROC_UPDATE_DIRECTOR = "{ CALL proc_update_director  (?,?,?) }";
    private static final String PROC_DELETE_DIRECTOR = "{ CALL proc_delete_director (?) }";

    private static final String PROC_CREATE_USER = "{ CALL proc_create_user  (?,?,?) }";
    private static final String PROC_GET_USER = "{ CALL proc_get_user  (?) }";

    private static final String PROC_CREATE_MOVIE_ACTOR = "{ CALL proc_create_movie_actor  (?,?) }";
    private static final String PROC_READ_MOVIE_ACTOR = "{ CALL proc_read_movie_actor (?) }";
    private static final String PROC_DELETE_MOVIE_ACTOR = "{ CALL proc_delete_movie_actor  (?,?) }";

    private static final String PROC_CREATE_MOVIE_DIRECTOR = "{ CALL proc_create_movie_director  (?,?) }";
    private static final String PROC_READ_MOVIE_DIRECTOR = "{ CALL proc_read_movie_director  (?) }";
    private static final String PROC_DELETE_MOVIE_DIRECTOR = "{ CALL proc_delete_movie_director  (?,?) }";

    private static final String PROC_RESET_TO_DEFAULT = "{ CALL proc_reset_to_default }";

    @Override
    public int createActor(Person actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return createActor(actor, con);
        }
    }

    public int createActor(Person actor, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_CREATE_ACTOR)) {

            stmt.setString(1, actor.getFullName());
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(2);
        }
    }

    @Override
    public void createActors(List<Person> actors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_ACTOR)) {

            for (Person actor : actors) {
                stmt.setString(1, actor.getFullName());
                stmt.registerOutParameter(2, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    public void createActors(List<Person> actors, int movieID, Connection con) throws Exception {
        List<Integer> actorIDs = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(PROC_CREATE_ACTOR)) {
            for (Person actor : actors) {
                stmt.setString(1, actor.getFullName());
                stmt.registerOutParameter(2, Types.INTEGER);

                stmt.executeUpdate();
                actorIDs.add(stmt.getInt(2));
            }
        }
        try (CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE_ACTOR)) {

            for (Integer actorID : actorIDs) {
                stmt.setInt(1, movieID);
                stmt.setInt(2, actorID);
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateActor(int id, Person data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            updateActor(id, data, con);
        }
    }

    public void updateActor(int id, Person data, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_UPDATE_ACTOR)) {

            stmt.setInt(1, data.getIdInRole());
            stmt.setString(2, data.getFullName());
            stmt.setString(3, data.getAlternateName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            deleteActor(id, con);
        }
    }

    public void deleteActor(int id, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_DELETE_ACTOR)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> selectActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return selectActor(id, con);
        }
    }

    public Optional<Person> selectActor(int id, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_READ_ACTOR)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt(ID_ACTOR),
                            rs.getInt(PERSON_ID),
                            rs.getString(FULL_NAME),
                            rs.getString(ALTERNATE_NAME)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> selectActors() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return selectActors(con);
        }
    }

    public List<Person> selectActors(Connection con) throws Exception {
        List<Person> actors = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(PROC_READ_ACTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                actors.add(new Person(
                        rs.getInt(ID_ACTOR),
                        rs.getInt(PERSON_ID),
                        rs.getString(FULL_NAME),
                        rs.getString(ALTERNATE_NAME)));
            }
        }
        return actors;
    }

    @Override
    public int createDirector(Person director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return createDirector(director, con);
        }
    }

    public int createDirector(Person director, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_CREATE_DIRECTOR)) {

            stmt.setString(1, director.getFullName());
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(2);
        }
    }

    @Override
    public void createDirectors(List<Person> directors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_DIRECTOR)) {

            for (Person director : directors) {
                stmt.setString(1, director.getFullName());
                stmt.registerOutParameter(2, Types.INTEGER);

                stmt.executeUpdate();
            }
        }
    }

    public void createDirectors(List<Person> directors, int movieID, Connection con) throws Exception {
        List<Integer> directorIDs = new ArrayList<>();

        try (CallableStatement stmt = con.prepareCall(PROC_CREATE_DIRECTOR)) {
            for (Person director : directors) {
                stmt.setString(1, director.getFullName());
                stmt.registerOutParameter(2, Types.INTEGER);

                stmt.executeUpdate();
                directorIDs.add(stmt.getInt(2));
            }
        }

        try (CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE_DIRECTOR)) {

            for (Integer directorID : directorIDs) {
                stmt.setInt(1, movieID);
                stmt.setInt(2, directorID);

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateDirector(int id, Person data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            updateDirector(id, data, con);
        }
    }

    public void updateDirector(int id, Person data, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_UPDATE_DIRECTOR)) {

            stmt.setInt(1, data.getIdInRole());
            stmt.setString(2, data.getFullName());
            stmt.setString(3, data.getAlternateName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            deleteDirector(id, con);
        }
    }

    public void deleteDirector(int id, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_DELETE_DIRECTOR)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> selectDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return selectDirector(id, con);
        }
    }

    public Optional<Person> selectDirector(int id, Connection con) throws Exception {
        try (CallableStatement stmt = con.prepareCall(PROC_READ_DIRECTOR)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt(ID_DIRECTOR),
                            rs.getInt(PERSON_ID),
                            rs.getString(FULL_NAME),
                            rs.getString(ALTERNATE_NAME)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> selectDirectors() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return selectDirectors(con);
        }
    }

    public List<Person> selectDirectors(Connection con) throws Exception {
        List<Person> directors = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(PROC_READ_DIRECTORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                directors.add(new Person(
                        rs.getInt(ID_DIRECTOR),
                        rs.getInt(PERSON_ID),
                        rs.getString(FULL_NAME),
                        rs.getString(ALTERNATE_NAME)));
            }
        }
        return directors;
    }

    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE)) {

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getOriginalTitle());
            stmt.setString(3, movie.getDescriptionHtml());
            stmt.setInt(4, movie.getLength());
            stmt.setString(5, movie.getGenre());
            stmt.setString(6, movie.getPosterLink());
            stmt.setString(7, movie.getTrailerLink());
            stmt.setString(8, movie.getLink());
            stmt.setString(9, movie.getGuid());
            stmt.setDate(10, Date.valueOf(movie.getStartsPlaying()));
            stmt.registerOutParameter(11, Types.INTEGER);

            stmt.executeUpdate();

            int movieID = stmt.getInt(11);
            createActors(movie.getActors(), movieID, con);
            createDirectors(movie.getDirectors(), movieID, con);

            return movieID;
        }
    }

    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE)) {

            for (Movie movie : movies) {

                stmt.setString(1, movie.getTitle());
                stmt.setString(2, movie.getOriginalTitle());
                stmt.setString(3, movie.getDescriptionHtml());
                stmt.setInt(4, movie.getLength());
                stmt.setString(5, movie.getGenre());
                stmt.setString(6, movie.getPosterLink());
                stmt.setString(7, movie.getTrailerLink());
                stmt.setString(8, movie.getLink());
                stmt.setString(9, movie.getGuid());
                stmt.setDate(10, Date.valueOf(movie.getStartsPlaying()));
                stmt.registerOutParameter(11, Types.INTEGER);

                stmt.executeUpdate();

                int movieID = stmt.getInt(11);
                createActors(movie.getActors(), movieID, con);
                createDirectors(movie.getDirectors(), movieID, con);
            }
        }
    }

    @Override
    public void updateMovie(int id, Movie data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_UPDATE_MOVIE)) {

            stmt.setInt(1, id);
            stmt.setString(2, data.getTitle());
            stmt.setString(3, data.getOriginalTitle());
            stmt.setString(4, data.getDescriptionHtml());
            stmt.setInt(5, data.getLength());
            stmt.setString(6, data.getGenre());
            stmt.setString(7, data.getPosterLink());
            stmt.setString(8, data.getTrailerLink());
            stmt.setString(9, data.getLink());
            stmt.setString(10, data.getGuid());
            stmt.setDate(11, Date.valueOf(data.getStartsPlaying()));

            stmt.executeUpdate();

            for (Person actor : data.getActors()) {
                updateActor(actor.getIdInRole(), actor, con);
            }
            for (Person director : data.getDirectors()) {
                updateDirector(director.getIdInRole(), director, con);
            }
        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        Optional<Movie> potentialMovie = selectMovie(id);
        if (potentialMovie.isPresent()) {

            try (Connection con = dataSource.getConnection()) {

                for (Person actor : potentialMovie.get().getActors()) {
                    deleteMovieActor(id, actor.getIdInRole());
                }
                for (Person director : potentialMovie.get().getDirectors()) {
                    deleteMovieDirector(id, director.getIdInRole());
                }

                CallableStatement stmt = con.prepareCall(PROC_DELETE_MOVIE);
                stmt.setInt(1, id);

                stmt.executeUpdate();
            }
            deletePosterIfPresent(potentialMovie.get());
        }
    }

    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_READ_MOVIE)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie newMovie = new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(ORIGINAL_TITLE),
                            rs.getString(DESCRIPTION_HTML),
                            rs.getInt(LENGTH),
                            rs.getString(GENRE),
                            rs.getString(POSTER_LINK),
                            rs.getString(TRAILER_LINK),
                            rs.getString(LINK),
                            rs.getString(GUID),
                            rs.getDate(STARTS_PLAYING).toLocalDate());

                    return Optional.of(newMovie);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_READ_MOVIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie newMovie = new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        rs.getString(ORIGINAL_TITLE),
                        rs.getString(DESCRIPTION_HTML),
                        rs.getInt(LENGTH),
                        rs.getString(GENRE),
                        rs.getString(POSTER_LINK),
                        rs.getString(TRAILER_LINK),
                        rs.getString(LINK),
                        rs.getString(GUID),
                        rs.getDate(STARTS_PLAYING).toLocalDate());

                movies.add(newMovie);
            }
        }
        return movies;
    }

    @Override
    public List<Movie> selectMoviesFull() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_READ_MOVIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie newMovie = new Movie(
                        rs.getInt(ID_MOVIE),
                        rs.getString(TITLE),
                        rs.getString(ORIGINAL_TITLE),
                        rs.getString(DESCRIPTION_HTML),
                        rs.getInt(LENGTH),
                        rs.getString(GENRE),
                        rs.getString(POSTER_LINK),
                        rs.getString(TRAILER_LINK),
                        rs.getString(LINK),
                        rs.getString(GUID),
                        rs.getDate(STARTS_PLAYING).toLocalDate());

                newMovie.setActors(selectMovieActors(newMovie.getIdMovie(), con));
                newMovie.setDirectors(selectMovieDirectors(newMovie.getIdMovie(), con));

                movies.add(newMovie);
            }
        }
        return movies;
    }

    @Override
    public int createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_USER)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }

    @Override
    public Optional<User> selectUser(String username) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_GET_USER)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getString(ROLE),
                            rs.getInt(ACCESS_LEVEL))
                    );
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void createMovieActor(int movieID, int actorID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE_ACTOR)) {

            stmt.setInt(1, movieID);
            stmt.setInt(2, actorID);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createMovieActors(int movieID, List<Person> actors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE_ACTOR)) {

            for (Person actor : actors) {
                stmt.setInt(1, movieID);
                stmt.setInt(2, actor.getIdInRole());

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void deleteMovieActor(int movieID, int actorID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_DELETE_MOVIE_ACTOR)) {

            stmt.setInt(1, movieID);
            stmt.setInt(2, actorID);

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Person> selectMovieActors(int movieID) throws Exception {
        List<Person> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return selectMovieActors(movieID, con);
        }
    }

    public List<Person> selectMovieActors(int movieID, Connection con) throws Exception {
        List<Person> actors = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(PROC_READ_MOVIE_ACTOR)) {

            stmt.setInt(1, movieID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Person(
                            rs.getInt(ID_ACTOR),
                            rs.getInt(PERSON_ID),
                            rs.getString(FULL_NAME),
                            rs.getString(ALTERNATE_NAME)));
                }
            }
            return actors;
        }
    }

    @Override
    public void createMovieDirector(int movieID, int directorID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE_DIRECTOR)) {

            stmt.setInt(1, movieID);
            stmt.setInt(2, directorID);

            stmt.executeUpdate();
        }
    }

    @Override
    public void createMovieDirectors(int movieID, List<Person> directors) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_CREATE_MOVIE_DIRECTOR)) {

            for (Person director : directors) {
                stmt.setInt(1, movieID);
                stmt.setInt(2, director.getIdInRole());

                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void deleteMovieDirector(int movieID, int directorID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_DELETE_MOVIE_DIRECTOR)) {

            stmt.setInt(1, movieID);
            stmt.setInt(2, directorID);

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Person> selectMovieDirectors(int movieID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection()) {
            return selectMovieDirectors(movieID, con);
        }
    }

    public List<Person> selectMovieDirectors(int movieID, Connection con) throws Exception {
        List<Person> directors = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(PROC_READ_MOVIE_DIRECTOR)) {

            stmt.setInt(1, movieID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    directors.add(new Person(
                            rs.getInt(ID_DIRECTOR),
                            rs.getInt(PERSON_ID),
                            rs.getString(FULL_NAME),
                            rs.getString(ALTERNATE_NAME)));
                }
            }
            return directors;
        }
    }

    @Override
    public void clearAllData(User user) throws Exception {
        if (user.getAccessLevel() == 100) {
            FileUtils.clearDirectory(new File("assets"), true);

            DataSource dataSource = DataSourceSingleton.getInstance();
            try (Connection con = dataSource.getConnection();
                    CallableStatement stmt = con.prepareCall(PROC_RESET_TO_DEFAULT)) {
                stmt.executeUpdate();
            }
        }
    }
    public void clearAllData() throws Exception {
        FileUtils.clearDirectory(new File("assets"), true);

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(PROC_RESET_TO_DEFAULT)) {
            stmt.executeUpdate();
        }
    }

    private void deletePosterIfPresent(Movie movie) {
        File poster = new File(movie.getPosterLink());
        try {
            poster.delete();
        } catch (Exception e) {
        }
    }
}