package com.example.Doctor_Microservices.model;

import java.util.List;

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
@Table(name="doctor_record")
public class DoctorDataModel {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long doctorId;
	
	@Column(name="name")
    private String name;
    
	@Column(name="specialization")
	private String specialization;
    
	@Column(name="experienceYears")
	private int experienceYears;
    
	@Column(name="phone")
	private String phone;
	
	@Column(name="email")
    private String email;
	
	@Column(name="days",  columnDefinition = "text[]")
    private List<String> days;

	@Column(name="time")
	private String time;
}
