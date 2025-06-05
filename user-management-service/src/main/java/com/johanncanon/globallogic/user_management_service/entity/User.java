package com.johanncanon.globallogic.user_management_service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.johanncanon.globallogic.user_management_service.config.Utils;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;
    private String password;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime created;
    @Transient
    private String formattedCreated;

    private String lastLogin;
    private String token;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
        this.formattedCreated = Utils.formatDateTime(this.created);
    }

    public User() {
        this.id = Utils.generateRandomUUID();
        this.lastLogin = Utils.formatDateTime(LocalDateTime.now());
    }

    public User(
            String name, String password, String email,
            List<Phone> phones, LocalDateTime created,
            String token, boolean isActive) {

        this.id = Utils.generateRandomUUID();
        this.name = name;
        this.password = password;
        this.email = email;
        this.phones = phones;
        this.created = created;
        this.lastLogin = Utils.formatDateTime(LocalDateTime.now());
        this.token = token;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pasword) {
        this.password = pasword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getFormattedCreated() {
        return formattedCreated != null ? formattedCreated : Utils.formatDateTime(created);
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    // Bidirectional relationship with phone
    public void addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setUser(this);
    }

    public void removePhone(Phone phone) {
        this.phones.remove(phone);
        phone.setUser(null);
    }

}
