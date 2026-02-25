package org.example.domain.valueobject;

public record FullName(String firstName, String lastName) {
    public FullName {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}

