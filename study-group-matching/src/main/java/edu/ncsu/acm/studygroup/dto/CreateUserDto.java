package edu.ncsu.acm.studygroup.dto;

/**
 * Carries the information needed to create a new user.
 */
public class CreateUserDto {

    /** User's full name. */
    private String name;

    /** User's email address. */
    private String email;

    /** User's NC State identifier. */
    private String ncsuId;

    /** User's academic major. */
    private String major;

    /** User's academic year. */
    private String year;
    
    /** User's bio info*/
    private String bio;
    
    /** User's profile picture represented as a String*/
    private String profilePicture;

    /** Returns the user's name. */
    public String getName() {
        return name;
    }

    /** Sets the user's name. */
    public void setName(final String name) {
        this.name = name;
    }

    /** Returns the user's email. */
    public String getEmail() {
        return email;
    }

    /** Sets the user's email. */
    public void setEmail(final String email) {
        this.email = email;
    }

    /** Returns the user's NC State ID. */
    public String getNcsuId() {
        return ncsuId;
    }

    /** Sets the user's NC State ID. */
    public void setNcsuId(final String ncsuId) {
        this.ncsuId = ncsuId;
    }

    /** Returns the user's major. */
    public String getMajor() {
        return major;
    }

    /** Sets the user's major. */
    public void setMajor(final String major) {
        this.major = major;
    }

    /** Returns the user's academic year. */
    public String getYear() {
        return year;
    }

    /** Sets the user's academic year. */
    public void setYear(final String year) {
        this.year = year;
    }
    
    /**
     * Returns the user's bio
     * @return bio the user's bio
     */
	public String getBio() {
		return bio;
	}

	/**
	 * Sets the user's bio
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * @return the profilePicture
	 */
	public String getProfilePicture() {
		return profilePicture;
	}

	/**
	 * @param profilePicture the profilePicture to set
	 */
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
}