package Projekt.appServer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "broker")
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "broker_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "company", nullable = false, length = 20)
    private String company;

    @Column(name = "password_hash", nullable = false, length = 20)
    private byte[] passwordHash;

    @Column(name = "password_salt", nullable = false, length = 32)
    private byte[] passwordSalt;

    public Broker(String username, String company, byte[] passwordSalt, byte[] passwordHash) {
        this.username = username;
        this.company = company;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
    }

    public Broker() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public byte[] getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt)
    {
        this.passwordSalt = passwordSalt;
    }



}