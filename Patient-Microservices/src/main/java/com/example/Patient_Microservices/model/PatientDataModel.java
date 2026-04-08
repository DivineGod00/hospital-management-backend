package com.example.Patient_Microservices.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="patient_record")
public class PatientDataModel {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patient_id;
	
	@Column(name="name")
    private String name;
	
	@Column(name="age")
    private int age;
	
	@Column(name="gender")
    private String gender;
	
	@Column(name="disease")
    private String disease;
	
	@Column(name="doctorid")
    private int assignedDoctorId;
	
	@Column(name="admit_date")
    private LocalDate admissionDate;
	
	@Column(name="mobile_no")
    private String mobile;
	
	@Column(name="address")
    private String address; 
}
