package com.example.Doctor_Microservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Doctor_Microservices.model.DoctorDataModel;

@Repository
public interface DoctorDataRepo extends JpaRepository<DoctorDataModel, Long>{
	
}
