package com.example.Patient_Microservices.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.Patient_Microservices.dao.PatientDataRepo;
import com.example.Patient_Microservices.dto.ContactDTO;
import com.example.Patient_Microservices.dto.PatientDto;
import com.example.Patient_Microservices.dto.PatientResponse;
import com.example.Patient_Microservices.model.PatientDataModel;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/patient")
public class PatientController {
	
	private static final Logger log = LoggerFactory.getLogger(PatientController.class);

	@Value("${hospital.name}")
	public String hospital;
	
	@Value("${server.port}")
	private String port;
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private PatientDataRepo patientDataRepo;
	
    @GetMapping("/list")
    public PatientResponse getPatients() throws Exception {

    	log.info("Request handled by instance on port: {}", port);

    	System.out.println("hospital : - "+hospital);
        InputStream inputStream =
                new ClassPathResource("static/patient.json").getInputStream();
        PatientResponse response = objectMapper.readValue(inputStream, PatientResponse.class);
        response.setHospital(hospital);
        System.out.println(response);
        return response;
    }

    @PostMapping("/detail-patient")
    public Map<String , Object> patientDetail(@RequestParam int patientId) throws Exception
    {
    	Map<String , Object> map = new HashMap<>();
    	
    	PatientDataModel patientDto =  patientDataRepo.findById((long) patientId)
    				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient Not found"));
    	
    	
    	ContactDTO contact = new ContactDTO();
    	contact.setAddress(patientDto.getAddress());
    	contact.setPhone(patientDto.getMobile());
    	
    	PatientDto filteredData = new PatientDto();
    	filteredData.setPatientId(patientDto.getPatient_id());
    	filteredData.setName(patientDto.getName());
    	filteredData.setAge(patientDto.getAge());
    	filteredData.setGender(patientDto.getGender());
    	filteredData.setDisease(patientDto.getDisease());
    	filteredData.setAssignedDoctorId(patientDto.getAssignedDoctorId());
    	filteredData.setAdmissionDate(patientDto.getAdmissionDate());
    	filteredData.setContact(contact);

    	
    	System.out.println(filteredData);
    	map.put("patientDetail", filteredData);
    	return map;
    }
    
    
    @PostMapping("/add-patient")
    public Map<String, Object> addPatient(@RequestBody PatientDto patient) throws Exception {

        Map<String, Object> response = new HashMap<>();
        Map<String, String> message = new HashMap<>();

        System.out.println("Patient Data :::-> " + patient);

        if (patient.getName() == null ||
        	    patient.getAge() == 0 ||
        	    patient.getGender() == null ||
        	    patient.getDisease() == null ||
        	    patient.getAssignedDoctorId() == 0 ||
        	    patient.getAdmissionDate() == null )  {
            message.put("message", "Patient data is null");
            response.put("error", message);
            return response;
        }
        
        PatientDataModel patientData = new PatientDataModel();
        
        patientData.setName(patient.getName());
        patientData.setAge(patient.getAge());
        patientData.setGender(patient.getGender());
        patientData.setDisease(patient.getDisease());
        patientData.setAssignedDoctorId(patient.getAssignedDoctorId());
        patientData.setAdmissionDate(patient.getAdmissionDate());
        patientData.setMobile(patient.getContact().getPhone());
        patientData.setAddress(patient.getContact().getAddress());
        
        patientDataRepo.save(patientData);
        System.out.println("Patient Data Set successfully:-> "+patientData);
        response.put("message", "Patient added successfully");
        
        response.put("data", patient);

        return response;
    }
}