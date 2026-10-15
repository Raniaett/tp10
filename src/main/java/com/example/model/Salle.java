package com.example.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salles")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Salle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomm;

    private int capacitee;

    private String descriptionn;

    @Version
    private int versionn;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Reservation> reservations = new ArrayList<>();

    public Salle() {
    }

    public Salle(String nomm, int capacitee) {
        this.nomm = nomm;
        this.capacitee = capacitee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomm() {
        return nomm;
    }

    public void setNomm(String nomm) {
        this.nomm = nomm;
    }

    public int getCapacitee() {
        return capacitee;
    }

    public void setCapacitee(int capacitee) {
        this.capacitee = capacitee;
    }

    public String getDescriptionn() {
        return descriptionn;
    }

    public void setDescriptionn(String description) {
        this.descriptionn = descriptionn;
    }

    public int getVersionn() {
        return versionn;
    }

    public void setVersionn(int versionn) {
        this.versionn = versionn;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void ajouterReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setSalle(this);
    }

    public void retirerReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setSalle(null);
    }
}
