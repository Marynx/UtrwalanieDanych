package Domain;

import java.sql.Date;

public class Director {
    
    private int id;
    private String firstName;
    private String lastName;
    private String city;
    private Date yob;
    
    public Director(int id, String firstName, String lastName, String city, Date yob) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.yob = yob;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public Date getYob() {
        return yob;
    }
    
    public void setYob(Date yob) {
        this.yob = yob;
    }
    
    @Override
    public String toString() {
        return "Director{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", city='" + city + '\'' +
               ", yob=" + yob +
               '}';
    }
    
}
