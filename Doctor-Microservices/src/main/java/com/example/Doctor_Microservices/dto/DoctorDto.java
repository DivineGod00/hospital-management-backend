package com.example.Doctor_Microservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor

public class DoctorDto {

   	private Long doctorId;
    private String name;
    private String specialization;
    private int experienceYears;
    private String phone;
    private String email;
    private AvailabilityDTO availability;
}
