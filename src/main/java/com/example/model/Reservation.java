package com.example.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private LocalDateTime dateeDebut;

    @Column(nullable = false)
    private LocalDateTime dateeFin;

    @Version
    private int versionn;

    public Reservation() {
    }

    public Reservation(Salle salle, Utilisateur utilisateur, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.salle = salle;
        this.utilisateur = utilisateur;
        this.dateeDebut = dateeDebut;
        this.dateeFin = dateeFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDateTime getDateeDebut() {
        return dateeDebut;
    }

    public void setDateeDebut(LocalDateTime dateeDebut) {
        this.dateeDebut = dateeDebut;
    }

    public LocalDateTime getDateeFin() {
        return dateeFin;
    }

    public void setDateeFin(LocalDateTime dateeFin) {
        this.dateeFin = dateeFin;
    }

    public int getVersionn() {
        return versionn;
    }

    public void setVersionn(int versionn) {
        this.versionn = versionn;
    }
}
