package com.language_practice_server.server_demo.domain.model;

import com.language_practice_server.server_demo.common.enums.Role;

import java.util.Objects;

public class User {
    private Long id;
    private String userName;
    private Role role;
    private String email;
    private boolean active;
    private boolean locked;
    private boolean credentialsExpired;
    private Person person;

    //For Spring / JSON / Mapper
    public User() {
    }

    //For when the object is already created (comes from DB)
    public User(Long id, String userName,String email, Role role, boolean locked, boolean credentialsExpired, Person person) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.active = true; //Always active, when created.
        this.locked = locked;
        this.credentialsExpired = credentialsExpired;
        this.person = person;
    }

    //For creating new user.
    public User(String userName, Role role, Person person) {
        //Long id, this.id = id; - created by DB.
        this.userName = userName;
        this.role = role;
        this.person = person;
    }

    public boolean canLogin() {
        return active && !locked && !credentialsExpired;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(role);
    }

    public boolean isTeacher() {
        return Role.TEACHER.equals(role);
    }

    public boolean isStudent() {
        return Role.STUDENT.equals(role);
    }

    public boolean isGuest() {
        return Role.GUEST.equals(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", role=" + role +
                ", active=" + active +
                ", locked=" + locked +
                ", credentialsExpired=" + credentialsExpired +
                ", person=" + person +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
