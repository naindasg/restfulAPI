package edu.leicester.co2103cw3.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import edu.leicester.co2103cw3.CustomErrorType;
import edu.leicester.co2103cw3.domain.Convenor;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.ConvenorRepository;
@RequestMapping("/api")
@RestController
public class ConvenorController {
	
	public static final Logger logger = LoggerFactory.getLogger(ConvenorController.class);
	
	@Autowired
	private ConvenorRepository crepo;
	
// ========================================================= getConvenorById =============================================================================
	
	@RequestMapping(value = "/convenor/{convenorId}", method = RequestMethod.GET)
	public ResponseEntity<?> getConvenorById(@PathVariable("convenorId") int convenorId) {
		logger.info("Fetching Convenor with id {}", convenorId);
		Convenor convenor = crepo.findById(convenorId);
		if (convenor == null) {
			logger.error("Convenor with id {} not found", convenorId);
			return new ResponseEntity(new CustomErrorType("Convenor with Id " + convenorId + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);	
	}
	

	
// ============================================================ listAllConvenors ============================================================================
	
	@RequestMapping(value = "/convenor/", method = RequestMethod.GET)
	public ResponseEntity<List<Convenor>> listAllConvenors() {
	  List<Convenor> convenor = crepo.findAll();
	  if (convenor == null) {
	    return new ResponseEntity(HttpStatus.NOT_FOUND); // Or HttpStatus.NOT_FOUND if you prefer
	  }
	  logger.info("Finding all convenors");
	  return new ResponseEntity<List<Convenor>>(convenor, HttpStatus.OK);
	} 
	
// ============================================================ addConvenor ==================================================================================
	
	@RequestMapping(value = "/convenor/", method = RequestMethod.POST)
	public ResponseEntity<?> addConvenor(@RequestBody Convenor convenor, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Convenor: {}", convenor);
		
		if (crepo.existsById(convenor.getConvenorId())) {
			logger.error("Unable to create. A Convenor with Id {} already exists", convenor.getConvenorId());
			return new ResponseEntity(new CustomErrorType("Unable to create. A convenor with  " + convenor.getConvenorId() + " already exists."), HttpStatus.CONFLICT);
		}
		crepo.save(convenor);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/convenor/{convenorId}").buildAndExpand(convenor.getConvenorId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
// =============================================================== updateConvenor ===================================================================
	
	
	@RequestMapping(value = "/convenor/{convenorId}", method = RequestMethod.PUT)
	public ResponseEntity<Convenor> updateConvenor(@PathVariable("convenorId") int convenorId, @RequestBody Convenor convenor) {
		logger.info("Updating convenor with convenorId {}", convenorId);

		Convenor currentConvenor = crepo.findById(convenorId);
		  
		  if (currentConvenor == null) {
			    logger.error("Unable to update. Convenor with convenorId {} not found.", convenorId);
			    return new ResponseEntity(new CustomErrorType("Unable to update. Convenor with convenorId " + convenorId + " not found."),
			        HttpStatus.NOT_FOUND);
			  }
		  
		  currentConvenor.setConvenorId(convenor.getConvenorId());
		  currentConvenor.setName(convenor.getName());
		  currentConvenor.setOffice(convenor.getOffice());
		  currentConvenor.setPosition(convenor.getPosition());
		  
		  crepo.save(currentConvenor);
		  return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
	}
	
// ============================================================ deleteConvenor =======================================================================
	
	@RequestMapping(value = "/convenor/{convenorId}", method = RequestMethod.DELETE)
	public ResponseEntity<Convenor> deleteConvenor(@PathVariable("convenorId") int convenorId) {
		logger.info("Deleting convenor with id {}", convenorId);
		Convenor convenor = crepo.findById(convenorId);
		if (convenor == null) {
			logger.error("Unable to delete. A convenor with id {} not found", convenorId);
			return new ResponseEntity(new CustomErrorType("Unable to delete. A convenor with Id " + convenorId + " not found"), HttpStatus.NOT_FOUND);
		}
		
		crepo.deleteById(convenorId);
		
		return new ResponseEntity("Deleting convenor" + convenorId, HttpStatus.OK);	
	}
	


}
