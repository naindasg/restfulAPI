package edu.leicester.co2103cw3.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import edu.leicester.co2103cw3.CustomErrorType;
import edu.leicester.co2103cw3.domain.Convenor;
import edu.leicester.co2103cw3.domain.Lecture;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.LectureRepository;
import edu.leicester.co2103cw3.repository.ModuleRepository;
@RequestMapping("/api")
@RestController
public class LectureController {
	
	public static final Logger logger = LoggerFactory.getLogger(LectureController.class);
	
	@Autowired
	private LectureRepository lrepo;
	
	@Autowired
	private ModuleRepository mrepo;

//=========================================================================== getLectureById=======================================================================================
	
	@RequestMapping(value = "/lecture/{lectureId}", method = RequestMethod.GET)
	public ResponseEntity<?> getLectureById(@PathVariable("lectureId") int lectureId) {
		logger.info("Fetching Lecture with id {}", lectureId);
		Lecture lecture = lrepo.findById(lectureId);
		if (lecture == null) {
			logger.error("Lecture with id {} not found", lectureId);
			return new ResponseEntity(new CustomErrorType("Lecture with Id " + lectureId + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Lecture>(lecture, HttpStatus.OK);	 
	}
	
	
// ===================================================================== listAllLectures============================================================================================
	
	
	@RequestMapping(value = "/lecture/", method = RequestMethod.GET)
	public ResponseEntity<List<Lecture>> listAllLectures() {
	  List<Lecture> lecture = lrepo.findAll();
	  if (lecture == null) {
	    return new ResponseEntity(HttpStatus.NOT_FOUND); // Or HttpStatus.NOT_FOUND if you prefer
	  }
	  logger.info("Finding all lectures");
	  return new ResponseEntity<List<Lecture>>(lecture, HttpStatus.OK);
	}
	
	
// ====================================================================== addLecture ====================================================================================================
	
	
	
	@RequestMapping(value = "/lecture/", method = RequestMethod.POST)
	public ResponseEntity<?> addLecture(@RequestBody Lecture lecture, UriComponentsBuilder ucBuilder) {
		logger.info("Creating lecture: {}", lecture);
		
		if (lrepo.existsById(lecture.getLectureId())) {
			logger.error("Unable to create. A lecture with Id {} already exists", lecture.getLectureId());
			return new ResponseEntity(new CustomErrorType("Unable to create. A lecture with  " + lecture.getLectureId() + " already exists."), HttpStatus.CONFLICT);
		}
		lrepo.save(lecture);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/lecture/{lectureId}").buildAndExpand(lecture.getLectureId()).toUri());
		//headers.setLocation(ucBuilder.path("/api/lecture/{lectureId}").buildAndExpand(new Object[] { 1, lecture.getLectureId()}).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
		}
	
		
	
// =========================================================== updateLecture ===========================================================================================
	
	
	
	@RequestMapping(value = "/lecture/{lectureId}", method = RequestMethod.PUT)
	public ResponseEntity<Lecture> updateLecture(@PathVariable("lectureId") int lectureId, @RequestBody Lecture lecture) {
		logger.info("Updating lecture with lectureId {}", lectureId);

		  Lecture currentLecture = lrepo.findById(lectureId);
		  
		  if (currentLecture == null) {
			    logger.error("Unable to update. Lecture with lectureId {} not found.", lectureId);
			    return new ResponseEntity(new CustomErrorType("Unable to update. Lecture with lectureId " + lectureId + " not found."),
			        HttpStatus.NOT_FOUND);
			  }
		  currentLecture.setLectureId(lecture.getLectureId());
		  currentLecture.setWeek(lecture.getWeek());
		  currentLecture.setTitle(lecture.getTitle());
		  currentLecture.setUrl(lecture.getUrl());
		  
		  lrepo.save(currentLecture);
		  return new ResponseEntity<Lecture>(currentLecture, HttpStatus.OK);
		}
	
	
	
	
// ============================================================= deleteLecture =========================================================================================
	
	@RequestMapping(value = "/lecture/{lectureId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLecture(@PathVariable("lectureId") int lectureId) {
		logger.info("Deleting Lecture with id {}", lectureId);
		Lecture lecture = lrepo.findById(lectureId);
		if (lecture == null) {
			logger.error("Unable to delete. A lecture with id {} not found", lectureId);
			return new ResponseEntity(new CustomErrorType("Unable to delete. A lecture with Id " + lectureId + " not found"), HttpStatus.NOT_FOUND);
		}
		
		lrepo.deleteById(lectureId);
		
		return new ResponseEntity<Lecture>(lecture, HttpStatus.OK);	
	}
	
// ================================================================ getLectureByModuleId ==================================================================================
	
	@RequestMapping(value = "/module/{moduleId}/lecture", method = RequestMethod.GET)
	public ResponseEntity<List<Lecture>> getLectureByModuleId(@PathVariable("moduleId") int moduleId) {
		logger.info("Getting lectures with with Module id {}", moduleId);
		Module module = mrepo.findById(moduleId);
		if (module == null) {
			logger.error("Module with id {} not found. Cannot return lectures", moduleId);
			return new ResponseEntity(new CustomErrorType("Module with Id " + moduleId + " not found. Cannot return Lectures"), HttpStatus.NOT_FOUND);
			
	}
		return new ResponseEntity<List<Lecture>>(module.getLecture(), HttpStatus.OK);
}
	
 // ========================================================================================================================================================================//
	

		
}
