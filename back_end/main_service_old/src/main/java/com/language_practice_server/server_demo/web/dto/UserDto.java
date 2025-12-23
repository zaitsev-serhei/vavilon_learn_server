package com.language_practice_server.server_demo.web.dto;

public class UserDto {
    private Long id;
    //@NotNull, @Email, @Size - valid annotations.
    //@NotBlank - valid annotation
    private String userName;
    private String password;
    private String email;
    //@Valid - valid annotation
    private PersonDto personDto;

    public UserDto() {
    }

    public UserDto(Long id, String userName, String password, String email, PersonDto personDto) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.personDto = personDto;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PersonDto getPersonDto() {
        return personDto;
    }

    public void setPersonDto(PersonDto personDto) {
        this.personDto = personDto;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", personDto=" + personDto +
                '}';
    }
}
