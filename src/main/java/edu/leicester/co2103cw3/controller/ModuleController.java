package edu.leicester.co2103cw3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.leicester.co2103cw3.domain.Lecture;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.ConvenorRepository;
import edu.leicester.co2103cw3.repository.LectureRepository;
import edu.leicester.co2103cw3.repository.ModuleRepository;
@RequestMapping("/api")
@RestController
public class ModuleController {
	
	public static final Logger logger = LoggerFactory.getLogger(ModuleController.class);
	
	@Autowired
	private ModuleRepository mrepo;
	
	@Autowired 
	private ConvenorRepository crepo;
	
	@Autowired
	private LectureRepository lrepo;
	
	
// =============================================================== getModuleById ===========================================================================================
	
	@RequestMapping(value = "/module/{moduleId}", method = RequestMethod.GET)
	public ResponseEntity<?> getModuleById(@PathVariable("moduleId") int moduleId) {
		logger.info("Fetching Module with id {}", moduleId);
		Module module = mrepo.findById(moduleId);
		if (module == null) {
			logger.error("Module with id {} not found", moduleId);
			return new ResponseEntity(new CustomErrorType("Module with Id " + moduleId + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Module>(module, HttpStatus.OK);	
	}
	
// =============================================================== listAllModules ==========================================================================================
	
	
	@RequestMapping(value = "/module/", method = RequestMethod.GET)
	public ResponseEntity<List<Module>> listAllModules() {
	  List<Module> module = mrepo.findAll();
	  if (module == null) {
	    return new ResponseEntity(HttpStatus.NOT_FOUND); // Or HttpStatus.NOT_FOUND if you prefer
	  }
	  logger.info("Finding all modules");
	  return new ResponseEntity<List<Module>>(module, HttpStatus.OK);
	}
	
// ============================================================== addModule ==============================================================================================
	
	@RequestMapping(value = "/module/", method = RequestMethod.POST)
	public ResponseEntity<?> addModule(@RequestBody Module module, UriComponentsBuilder ucBuilder) {
		logger.info("Creating module: {}", module);
		
		if (mrepo.existsById(module.getModuleId())) {
			logger.error("Unable to create. A module with Id {} already exists", module.getModuleId());
			return new ResponseEntity(new CustomErrorType("Unable to create. A module with  " + module.getModuleId() + " already exists."), HttpStatus.CONFLICT);
		} 
		mrepo.save(module);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/module/{moduleId}").buildAndExpand(module.getModuleId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
// =============================================================== updateModule ===========================================================================================
	
	@RequestMapping(value = "/module/{moduleId}", method = RequestMethod.PUT)
	public ResponseEntity<Module> updateModule(@PathVariable("moduleId") int moduleId, @RequestBody Module module) {
		logger.info("Updating module with ModuleId {}", module.getModuleId());

		  Module currentModule = mrepo.findById(moduleId);
		  
		  if (currentModule == null) {
			    logger.error("Unable to update. Module with ModuleId {} not found.", moduleId);
			    return new ResponseEntity(new CustomErrorType("Unable to update. Module with moduleId " + moduleId + " not found."),
			        HttpStatus.NOT_FOUND);
			  }
		  currentModule.setModuleId(module.getModuleId());
		  currentModule.setTitle(module.getTitle());
		  currentModule.setSemester(module.getSemester()); 
		  currentModule.setCode(module.getCode());
		  currentModule.setCore(module.isCore());
		   
		  mrepo.save(currentModule);
		  return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
		}
	
// ============================================================== deleteModule ==============================================================================================
	
	@RequestMapping(value = "/module/{moduleId}", method = RequestMethod.DELETE)
	public ResponseEntity<Module> deleteModule(@PathVariable("moduleId") int moduleId) {
		logger.info("Deleting Module with id {}", moduleId);
		Module module = mrepo.findById(moduleId);
		if (module == null) {
			logger.error("Unable to delete. A module with id {} not found", moduleId);
			return new ResponseEntity(new CustomErrorType("Unable to delete. A Module with Id " + moduleId + " not found"), HttpStatus.NOT_FOUND);
		}
		
		mrepo.deleteById(moduleId);
		
		return new ResponseEntity("Deleting module" + moduleId, HttpStatus.OK);	
	}
	
// =========================================================== getModuleByConvenorId =====================================================================
	
		@RequestMapping(value = "/convenor/{convenorId}/module", method = RequestMethod.GET)
		public ResponseEntity<List<Module>> getModuleByConvenorId(@PathVariable("convenorId") int convenorId) {
			logger.info("Getting modules from Convenor id {}", convenorId);
			Convenor convenor = crepo.findById(convenorId);
			if (convenor == null) {
				logger.error("Convenor with id {} not found. Cannot return modules", convenorId);
				return new ResponseEntity(new CustomErrorType("Convenor with Id " + convenorId + " not found. Cannot return modules"), HttpStatus.NOT_FOUND);
				
		}
			return new ResponseEntity<List<Module>>(convenor.getModule(), HttpStatus.OK); //////////////////
		
}
// ===========================================================================================================================================================
		
	
		
		
		
		


}
