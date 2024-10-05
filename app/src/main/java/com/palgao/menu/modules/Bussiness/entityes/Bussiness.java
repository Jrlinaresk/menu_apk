package com.palgao.menu.modules.Bussiness.entityes;

import androidx.room.Entity;

@Entity
public class Bussiness {
    private String id;
    private String name;
    private String description;
    private Location location;
    private Address address;
    private Horario horario;
    private String email;
    // agregar al bakend de aqui para abajo
    private String type;
    private String ImageLogoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return "todos"; // originalmente este valor baja del baja del bakend
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageLogoUrl() {
        return ImageLogoUrl;
    }

    public void setImageLogoUrl(String imageLogoUrl) {
        ImageLogoUrl = imageLogoUrl;
    }
}
