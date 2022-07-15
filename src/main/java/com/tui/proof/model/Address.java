package com.tui.proof.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
public class Address extends BaseEntity {
    @NotBlank
    @Size(min = 3, max = 255)
    private String street;
    @Size(max = 10)
    private String postcode;
    @NotBlank
    @Size(min = 2, max = 32)
    private String city;
    @NotBlank
    @Size(min = 2, max = 32)
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
