package com.bibliotheque.model;

/**
 * Entity representing a library member (membre)
 * Only attributes and method signatures (no implementation logic)
 */
public class Member {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Member() {}

    public Member(Integer id, String firstName, String lastName, String email, String phone) {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
