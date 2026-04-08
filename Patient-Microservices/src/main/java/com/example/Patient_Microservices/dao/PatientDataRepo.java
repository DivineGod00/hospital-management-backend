package com.example.Patient_Microservices.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Patient_Microservices.model.PatientDataModel;

@Repository
public interface PatientDataRepo extends JpaRepository<PatientDataModel,Long>{

	Optional<PatientDataModel> findById(Long patient_id);
}
