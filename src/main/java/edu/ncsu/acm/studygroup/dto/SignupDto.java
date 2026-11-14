package edu.ncsu.acm.studygroup.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data received when a user creates an account.
 */
public class SignupDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private String ncsuId;

    @NotBlank
    @Size(min = 6)
    private String password;

    public SignupDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getNcsuId() {
        return ncsuId;
    }

    public void setNcsuId(final String ncsuId) {
        this.ncsuId = ncsuId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}