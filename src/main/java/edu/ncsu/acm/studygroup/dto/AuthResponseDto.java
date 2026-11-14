package edu.ncsu.acm.studygroup.dto;

/**
 * Information returned after successful authentication.
 */
public class AuthResponseDto {

    private Long userId;
    private String name;
    private String email;

    public AuthResponseDto(final Long userId,
                           final String name,
                           final String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}