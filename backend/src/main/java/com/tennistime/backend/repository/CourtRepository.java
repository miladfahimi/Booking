package com.tennistime.backend.repository;

import com.tennistime.backend.model.Court;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
public class CourtRepository {

    private final JpaCourtRepository jpaCourtRepository;

    @Autowired
    public CourtRepository(JpaCourtRepository jpaCourtRepository) {
        this.jpaCourtRepository = jpaCourtRepository;
    }

    public List<Court> findAll() {
        return jpaCourtRepository.findAll();
    }

    public Court save(Court court) {
        return jpaCourtRepository.save(court);
    }

    @PostConstruct
    public void initializeData() {
        jpaCourtRepository.save(new Court("Court 1"));
        jpaCourtRepository.save(new Court("Court 2"));
    }
}
