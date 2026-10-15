package com.example.service;

import com.example.model.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class ReservationService {

    private static final Logger LOGGER = Logger.getLogger(ReservationService.class.getName());
    private final EntityManager emm;

    public ReservationService(EntityManager emm) {
        this.emm = emm;
    }

    public void createReservation(Reservation reservation) {
        EntityTransaction tx = emm.getTransaction();
        boolean ownTx = false;
        if (!tx.isActive()) {
            tx.begin();
            ownTx = true;
        }

        try {
            emm.persist(reservation);
            if (ownTx) {
                tx.commit();
            }
            if (reservation.getUtilisateur() != null) {
                LOGGER.info("Création de la réservation réussie pour utilisateur : " + reservation.getUtilisateur().getEmaill());
            }
        } catch (Exception e) {
            if (ownTx && tx.isActive()) {
                tx.rollback();
            }
            LOGGER.severe("Erreur lors de la création de la réservation : " + e.getMessage());
            throw e;
        }
    }

    public List<Reservation> findReservationsBySalleAndDateJPQL(String nomSalle, LocalDateTime date) {
        String jpql = "SELECT r FROM Reservation r WHERE r.salle.nomm = :nomSalle AND r.dateeDebut > :date";
        return emm.createQuery(jpql, Reservation.class)
                .setParameter("nomSalle", nomSalle)
                .setParameter("date", date)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    public List<Reservation> findReservationsBySalleAndDateCriteria(String nomSalle, LocalDateTime date) {
        CriteriaBuilder cb = emm.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);

        Predicate predicate = cb.and(
                cb.equal(root.get("salle").get("nom"), nomSalle),
                cb.greaterThan(root.get("dateDebut"), date)
        );

        cq.select(root).where(predicate);
        return emm.createQuery(cq)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    public void testOptimisticLocking(Long reservationId) {
        EntityTransaction tx = emm.getTransaction();
        try {
            tx.begin();
            Reservation res = emm.find(Reservation.class, reservationId);
            if (res != null) {
                res.setDateeFin(res.getDateeFin().plusHours(1));
            }
            tx.commit();
        } catch (OptimisticLockException ole) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Conflit détecté, veuillez réessayer.");
        }
    }
}
