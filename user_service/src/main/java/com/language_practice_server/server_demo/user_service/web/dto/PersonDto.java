package com.language_practice_server.server_demo.user_service.web.dto;

public class PersonDto {
    private Long id;
    private String email;

    public PersonDto() {
    }

    public PersonDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
