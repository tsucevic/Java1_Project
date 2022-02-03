/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tin
 */
@XmlRootElement(name = "Movies")
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {
    
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_DATE;
    
    private int _idMovie;
    private String title;
    private String originalTitle;
    private String descriptionHtml;
    private int length;
    private String genre;
    private String posterLink;
    private String trailerLink;
    private String link;
    private String guid;
    private LocalDate startsPlaying;
    
    @XmlElementWrapper
    @XmlElement(name = "actor")
    private List<Person> actors;


    @XmlElementWrapper
    @XmlElement(name = "director")
    private List<Person> directors;
    
    public Movie() {
    }

    public Movie(String title, String originalTitle, String descriptionHtml, 
            int length, String genre, String posterLink, String trailerLink, String link, String guid, LocalDate startsPlaying) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.descriptionHtml = descriptionHtml;
        this.length = length;
        this.genre = genre;
        this.posterLink = posterLink;
        this.trailerLink = trailerLink;
        this.link = link;
        this.guid = guid;
        this.startsPlaying = startsPlaying;
    }

    public Movie(int _idMovie, String title, String originalTitle, String descriptionHtml, 
            int length, String genre, String posterLink, String trailerLink, String link, String guid, LocalDate startsPlaying) {
        this(title, originalTitle, descriptionHtml, length, genre, posterLink, trailerLink, link, guid, startsPlaying);
        this._idMovie = _idMovie;
    }
    
    public List<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public int getIdMovie() {
        return _idMovie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public LocalDate getStartsPlaying() {
        return startsPlaying;
    }

    public void setStartsPlaying(LocalDate startsPlaying) {
        this.startsPlaying = startsPlaying;
    }
    
    public List<Person> getActors() {
        return actors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" + "idMovie=" + _idMovie + ", title=" + title + ", originalTitle=" + originalTitle + ", descriptionHtml=" + descriptionHtml + ", length=" + length + ", genre=" + genre + ", posterLink=" + posterLink + ", trailerLink=" + trailerLink + ", link=" + link + ", guid=" + guid + ", startsPlaying=" + startsPlaying + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this._idMovie;
        hash = 47 * hash + Objects.hashCode(this.originalTitle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this._idMovie != other._idMovie) {
            return false;
        }
        return true;
    }
    
    

    
}
