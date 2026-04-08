package com.example.Doctor_Microservices.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Doctor_Microservices.dao.DoctorDataRepo;
import com.example.Doctor_Microservices.dto.DoctorDto;
import com.example.Doctor_Microservices.model.DoctorDataModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
	
	@Autowired
	private DoctorDataRepo doctorDataRepo;
	
	
	
	
	@GetMapping("/list")
    public Map<String, Object> getDoctors() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream =
                new ClassPathResource("static/doctor.json").getInputStream();

        return mapper.readValue(inputStream, Map.class);
    }
	
	
	@PostMapping("/add-doctor")
	public Map<String, Object> addDoctor(@RequestBody DoctorDto doctor)throws Exception
	{
		 Map<String, Object> response = new HashMap<>();
	        Map<String, String> message = new HashMap<>();

	        System.out.println("Patient Data :::-> " + doctor);

	        if (doctor.getName() == null ||
	        	doctor.getExperienceYears() == 0 ||
	        	doctor.getEmail() == null ||
	        	doctor.getSpecialization() == null ||
	        	doctor.getPhone() == null || 
	        	doctor.getAvailability() == null)  {
	            
	        	message.put("message", "Doctor data is null");
	            response.put("error", message);
	            return response;
	        }
	        
	        DoctorDataModel doctorData = new DoctorDataModel();
	        
	        doctorData.setName(doctor.getName());
	        doctorData.setSpecialization(doctor.getSpecialization());
	        doctorData.setExperienceYears(doctor.getExperienceYears());
	        doctorData.setPhone(doctor.getPhone());
	        doctorData.setEmail(doctor.getEmail()); 
	        doctorData.setTime(doctor.getAvailability().getTime());
	        doctorData.setDays(doctor.getAvailability().getDays());
	        
	        doctorDataRepo.save(doctorData);
	        System.out.println("Doctor Data Set successfully:-> "+doctorData);
	        response.put("message", "Doctor added successfully");
	        
	        response.put("data", doctorData);

	        return response;
	}
}