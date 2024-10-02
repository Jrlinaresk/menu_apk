package com.palgao.menu.modules.Bussiness.entityes;

import androidx.room.Entity;

@Entity
public class Address {
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
    private String zip;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Devuelve la dirección completa como un texto,
     * ignorando los valores null.
     */
    public String fullAddress() {
        StringBuilder addressBuilder = new StringBuilder();

        // Añade la calle y el número si están presentes
        if (street != null && !street.isEmpty()) {
            addressBuilder.append(street);
        }
        if (number != null && !number.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(number);
        }

        // Añade la ciudad si está presente
        if (city != null && !city.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(city);
        }

        // Añade el estado si está presente
        if (state != null && !state.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(state);
        }

        // Añade el país si está presente
        if (country != null && !country.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(country);
        }

        // Añade el código postal si está presente
        if (zip != null && !zip.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(zip);
        }

        // Retorna la dirección completa
        return addressBuilder.toString();
    }
}