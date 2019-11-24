package Domain;

import java.sql.Date;
import java.util.Objects;

public class Movie {
    
    private int id;
    private String title;
    private int lengthInMinutes;
    private Date releaseDate;
    private int director;
    
    public Movie(){};
    
    public Movie( String title, int lengthInMinutes, Date releaseDate, int directorId) {
        this.title = title;
        this.lengthInMinutes = lengthInMinutes;
        this.releaseDate = releaseDate;
        this.director = directorId;
    }
    
    public Movie(int id, String title, int lengthInMinutes, Date releaseDate, int directorId) {
        this.id = id;
        this.title = title;
        this.lengthInMinutes = lengthInMinutes;
        this.releaseDate = releaseDate;
        this.director = directorId;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getLengthInMinutes() {
        return lengthInMinutes;
    }
    
    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }
    
    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public int getDirector() {
        return director;
    }
    
    public void setDirector(int director) {
        this.director = director;
    }
    
    @Override
    public String toString() {
        return "Movie{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", lengthInMinutes=" + lengthInMinutes +
               ", releaseDate=" + releaseDate +
               ", directorId=" + director +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Movie movie = (Movie) o;
        return id == movie.id &&
               lengthInMinutes == movie.lengthInMinutes &&
               director == movie.director &&
               Objects.equals(title, movie.title) &&
               Objects.equals(releaseDate, movie.releaseDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, title, lengthInMinutes, releaseDate, director);
    }
    
}
