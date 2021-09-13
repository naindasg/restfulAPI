package edu.leicester;


import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.leicester.co2103cw3.controller.ConvenorController;
import edu.leicester.co2103cw3.controller.LectureController;
import edu.leicester.co2103cw3.controller.ModuleController;
import edu.leicester.co2103cw3.domain.Convenor;
import edu.leicester.co2103cw3.domain.Convenor.Position;
import edu.leicester.co2103cw3.domain.Lecture;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.ConvenorRepository;
import edu.leicester.co2103cw3.repository.LectureRepository;
import edu.leicester.co2103cw3.repository.ModuleRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CO2103Tests {
	
	private static final Position Lecturer = null;

	@Autowired
    private MockMvc mockMvcModule;
	
	@Autowired
	private MockMvc mockMvcLecture;
	
	@Autowired
	private MockMvc mockMvcConvenor; 
	
	@Mock
	private ModuleRepository mrepo;
	
	@Mock
	private LectureRepository lrepo;
	
	@Mock
	private ConvenorRepository crepo;
	
	@InjectMocks
	private ModuleController moduleController;
	
	@InjectMocks
	private LectureController lectureController;
	
	@InjectMocks
	private ConvenorController convenorController;
	
	@Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvcModule = MockMvcBuilders
                .standaloneSetup(moduleController) 
                .addFilters(new CORSFilter())
                .build();
	} 
	
	@Before
    public void init2(){
        MockitoAnnotations.initMocks(this);
        mockMvcLecture = MockMvcBuilders
                .standaloneSetup(lectureController) 
                .addFilters(new CORSFilter())
                .build(); 
	}
	
	@Before
    public void init3(){
        MockitoAnnotations.initMocks(this);
        mockMvcConvenor = MockMvcBuilders
                .standaloneSetup(convenorController) 
                .addFilters(new CORSFilter())
                .build();
	} 
	
	
	public static final ObjectMapper om = new ObjectMapper();

	
// ================================================== Module tests ========================================================================//
	
	
	/**
	 * curl -X POST "http://localhost:8080/api/module/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"moduleId\": 1, \"code\": \"CO2103\", \"title\": \"Software Architecture\", \"semester\": 0, \"core\": false, \"lecture\": [ { \"lectureId\": 1, \"week\": 0, \"title\": \"Unit Testing\", \"url\": \"https://le.ac.uk/\", \"Module\": {} } ]}"
	 */
	@Test
	public void testAddModule() throws Exception {
		List<Lecture> lecture = Arrays.asList(new Lecture(1, 1, "SA", "www.leicester.co.uk"), new Lecture(2, 2, "CO", "www.leicester.co.uk"));
		Module modules = new Module(1, "SA", "CO", 1, true, lecture);
		when(mrepo.existsById(1)).thenReturn(false);
		mockMvcModule.perform(post("/api/module/").content(om.writeValueAsString(modules))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
		verify(mrepo, times(1)).existsById(1);
		
	}
	
	/**
	 * curl -X POST "http://localhost:8080/api/module/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"moduleId\": 1, \"code\": \"CO2103\", \"title\": \"Software Architecture\", \"semester\": 0, \"core\": false, \"lecture\": [ { \"lectureId\": 1, \"week\": 0, \"title\": \"Unit Testing\", \"url\": \"https://le.ac.uk/\", \"Module\": {} } ]}"
	 * 
	 */
	@Test
	public void testAddModuleFail() throws Exception {
		Module modules = new Module(1, "SA", "CO", 1, true);
		when(mrepo.existsById(modules.getModuleId())).thenReturn(true);
		mockMvcModule.perform(post("/api/module/").content(om.writeValueAsString(modules))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict());
		verify(mrepo, times(1)).existsById(1);
		verifyNoMoreInteractions(mrepo);
		
	}
	
	
	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/module/" -H "accept: application/json"
	 */
	@Test
	public void testGetAllModules() throws Exception {
		List<Module> modules = Arrays.asList(new Module(1, "SA", "CO", 1, true), new Module(2, "SA", "CO", 2, true));
		when(mrepo.findAll()).thenReturn(modules); //when findAll method has been executed, do Return(modules) instead. 
		mockMvcModule.perform(get("/api/module/")) 
		.andExpect(status().isOk()) // HTTP response 
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))  //Expect the content to be JSON 
		.andExpect(jsonPath("$", hasSize(2))) // List will contain 2 elements 
		.andExpect(jsonPath("$[0].moduleId", is(1)))
		.andExpect(jsonPath("$[0].code", is("SA")))
		.andExpect(jsonPath("$[0].title", is("CO")))
		.andExpect(jsonPath("$[0].semester", is(1)))
		.andExpect(jsonPath("$[0].core", is(true)))
		.andExpect(jsonPath("$[1].moduleId", is(2)))
		.andExpect(jsonPath("$[1].code", is("SA")))
		.andExpect(jsonPath("$[1].title", is("CO")))
		.andExpect(jsonPath("$[1].semester", is(2))) 
		.andExpect(jsonPath("$[1].core", is(true)));
		verify(mrepo, times(1)).findAll(); //Ensure that the findAll() method has only been executed ONCE in the class mrepo
		verifyNoMoreInteractions(mrepo);
		}
	
	
	@Test
	public void testGetAllModulesFail() throws Exception {
		when(mrepo.findAll()).thenReturn(null);
		mockMvcModule.perform(get("/api/module/")).andExpect(status().isNotFound());
	}
	
	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/module/1" -H "accept: application/json"
	 */
	@Test
	public void testGetModuleById() throws Exception {
		Module modules = new Module(1, "SA", "CO", 1, true);
		when(mrepo.findById(modules.getModuleId())).thenReturn(modules);
		mockMvcModule.perform(get("/api/module/{moduleId}", 1))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.moduleId", is(1)))
		.andExpect(jsonPath("$.code", is("SA")))
		.andExpect(jsonPath("$.title", is("CO")))
		.andExpect(jsonPath("$.semester", is(1)))
		.andExpect(jsonPath("$.core", is(true)));
		verify(mrepo, times(1)).findById(1);
		verifyNoMoreInteractions(mrepo);
		
	} 
	
	/**
	 * curl -X GET "http://localhost:8080/api/module/5" -H "accept: application/json"
	 */
	@Test
	public void testGetModuleByIdFail() throws Exception {
		when(mrepo.findById(1)).thenReturn(null);
		mockMvcModule.perform(get("/api/module/{moduleId}", 1))
		.andExpect(status().isNotFound());
		verify(mrepo, times(1)).findById(1);
		verifyNoMoreInteractions(mrepo);
		
	} 
	
	/**
	 * 
	 * curl -X PUT "http://localhost:8080/api/module/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"moduleId\": 1, \"code\": \"CO2103\", \"title\": \"New Architecture\", \"semester\": 0, \"core\": false, \"lecture\": [ { \"lectureId\": 1, \"week\": 0, \"title\": \"BRAND NEW Testing\", \"url\": \"https://le.ac.uk/\", \"Module\": {} } ]}"
	 */
	@Test
	public void testUpdateModule() throws Exception {
		Module currentModule = new Module(1, "SA", "CO", 1, true);
		when(mrepo.findById(currentModule.getModuleId())).thenReturn(currentModule);
		mockMvcModule.perform(put("/api/module/{moduleId}", currentModule.getModuleId())
		.content(om.writeValueAsString(currentModule))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)) 
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		verify(mrepo, times(1)).findById(currentModule.getModuleId());
	}
	
		
	
	/**
	 * curl -X PUT "http://localhost:8080/api/module/5" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"moduleId\": 1, \"code\": \"CO2103\", \"title\": \"New Architecture\", \"semester\": 0, \"core\": false, \"lecture\": [ { \"lectureId\": 1, \"week\": 0, \"title\": \"BRAND NEW Testing\", \"url\": \"https://le.ac.uk/\", \"Module\": {} } ]}"
	 */
	@Test
	public void testUpdateModuleFail() throws Exception {
		Module modules =  new Module(1, "SA", "CO", 1, true);
		when(mrepo.findById(modules.getModuleId())).thenReturn(null);
		mockMvcModule.perform(put("/api/module/{moduleId}", modules.getModuleId())
		.content(om.writeValueAsString(modules))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(mrepo, times(1)).findById(modules.getModuleId());
		verifyNoMoreInteractions(mrepo);
	}	
	
	/**
	 * 
	 * curl -X DELETE "http://localhost:8080/api/module/1" -H "accept: application/json"
	 */
	@Test
	public void testDeleteModule() throws Exception {
		Module modules = new Module(1, "SA", "CO", 1, true);
		when(mrepo.findById(modules.getModuleId())).thenReturn(modules);
		doNothing().when(mrepo).deleteById(modules.getModuleId());
		mockMvcModule.perform(delete("/api/module/{moduleId}", modules.getModuleId()))
		.andExpect(status().isOk());
		verify(mrepo, times(1)).findById(modules.getModuleId());
		verify(mrepo, times(1)).deleteById(modules.getModuleId());
		verifyNoMoreInteractions(mrepo);	
	}
	
	/**
	 * curl -X DELETE "http://localhost:8080/api/module/20" -H "accept: application/json"
	 */
	
	@Test
	public void testDeleteModuleFail() throws Exception {
		Module modules = new Module(1, "SA", "CO", 1, true);
		when(mrepo.findById(modules.getModuleId())).thenReturn(null);
		mockMvcModule.perform(delete("/api/module/{moduleId}", modules.getModuleId()))
		.andExpect(status().isNotFound());
		verify(mrepo, times(1)).findById(modules.getModuleId());
		verifyNoMoreInteractions(mrepo);
	}
	
	/**
	 * curl -X POST "http://localhost:8080/api/convenor/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"convenorId\": 1, \"name\": \"Jose Rojas\", \"position\": \"GTA\", \"office\": \"F27\"}"
	 * curl -X GET "http://localhost:8080/api/convenor/1/module" -H "accept: application/json"
	 */
	@Test
	public void testGetModuleByConvenorId() throws Exception {
		List<Module> modules = Arrays.asList(new Module(1, "SA", "CO", 1, true), new Module(2, "SA", "CO", 2, true));
		Convenor convenors =  new Convenor(1, "Mr. Smith", "F11", Lecturer);
		convenors.setModule(modules);
		when(crepo.findById(1)).thenReturn(convenors);
		mockMvcModule.perform(get("/api/convenor/1/module"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].moduleId", is(1)))
		.andExpect(jsonPath("$[0].code", is("SA")))
		.andExpect(jsonPath("$[0].title", is("CO")))
		.andExpect(jsonPath("$[0].semester", is(1)))
		.andExpect(jsonPath("$[0].core", is(true)))
		.andExpect(jsonPath("$[1].moduleId", is(2)))
		.andExpect(jsonPath("$[1].code", is("SA")))
		.andExpect(jsonPath("$[1].title", is("CO")))
		.andExpect(jsonPath("$[1].semester", is(2)))
		.andExpect(jsonPath("$[1].core", is(true)));
		
	}
	
	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/convenor/5/module" -H "accept: application/json"
	 */
	@Test
	public void testGetModuleByConvenorIdFail() throws Exception {
		when(crepo.findById(1)).thenReturn(null);
		mockMvcModule.perform(get("/api/convenor/{convenorId}/module", 1))
		.andExpect(status().isNotFound());
	}
	
	
	
	// ================================================== Lecture tests ========================================================================//
	
	
	
	/**
	 * curl -X POST "http://localhost:8080/api/lecture/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"lectureId\": 1, \"week\": 0, \"title\": \"Unit Testing\", \"url\": \"https://le.ac.uk/\"}"
	 */
	@Test
	public void testAddLecture() throws Exception {
		Module modules = new Module(1, "SA", "CO", 1, true);
		Lecture lecture = new Lecture(1, 1, "SA", "www.SA.co.uk", modules);
		when(lrepo.findById(lecture.getLectureId())).thenReturn(lecture);
		mockMvcLecture.perform(post("/api/lecture/").content(om.writeValueAsString(lecture))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
		verify(lrepo, times(1)).existsById(1);
	}
	
	
	
	/**
	 * curl -X POST "http://localhost:8080/api/lecture/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"lectureId\": 1, \"week\": 0, \"title\": \"Unit Testing\", \"url\": \"https://le.ac.uk/\"}"
	 * 
	 */
	@Test
	public void testAddLectureFail() throws Exception {
		Lecture lecture = new Lecture(1, 1, "SA", "www.SA.co.uk");
		when(lrepo.existsById(lecture.getLectureId())).thenReturn(true);
		mockMvcLecture.perform(post("/api/lecture/").content(om.writeValueAsString(lecture))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict());
		verify(lrepo, times(1)).existsById(1);
		verifyNoMoreInteractions(lrepo);
	}
	
	/**
	 *
	 * curl -X GET "http://localhost:8080/api/lecture/" -H "accept: application/json"
	 */
	@Test
	public void testGetAllLectures() throws Exception {
		List<Lecture> lectures = Arrays.asList(new Lecture(1, 1, "SA", "www.SA.co.uk"), new Lecture(2, 2, "CA", "www.CA.co.uk"));
		when(lrepo.findAll()).thenReturn(lectures);
		mockMvcLecture.perform(get("/api/lecture/"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2))) 
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$[0].lectureId", is(1)))
		.andExpect(jsonPath("$[0].week", is(1)))
		.andExpect(jsonPath("$[0].title", is("SA")))
		.andExpect(jsonPath("$[0].url", is("www.SA.co.uk")))
		.andExpect(jsonPath("$[1].lectureId", is(2)))
		.andExpect(jsonPath("$[1].week", is(2)))
		.andExpect(jsonPath("$[1].title", is("CA")))
		.andExpect(jsonPath("$[1].url", is("www.CA.co.uk")));
		verify(lrepo, times(1)).findAll();
		verifyNoMoreInteractions(lrepo);
	}
	
	@Test
	public void testGetAllLecturesFail() throws Exception {
		when(lrepo.findAll()).thenReturn(null);
		mockMvcLecture.perform(get("/api/lecture/")).andExpect(status().isNotFound());
	}
	
	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/lecture/1" -H "accept: application/json"
	 */
	@Test
	public void testGetLectureById() throws Exception {
		Lecture lectures = new Lecture(1, 1, "SA", "www.SA.co.uk");
		when(lrepo.findById(1)).thenReturn(lectures);
		mockMvcLecture.perform(get("/api/lecture/{lectureId}", 1))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.lectureId", is(1)))
		.andExpect(jsonPath("$.week", is(1)))
		.andExpect(jsonPath("$.title", is("SA")))
		.andExpect(jsonPath("$.url", is("www.SA.co.uk")));
		verify(lrepo, times(1)).findById(1);
		verifyNoMoreInteractions(lrepo);
		
	} 
	
	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/lecture/5" -H "accept: application/json"
	 */
	@Test
	public void testGetLectureByIdFail() throws Exception {
		when(lrepo.findById(1)).thenReturn(null);
		mockMvcLecture.perform(get("/api/lecture/{lectureId}", 1))
		.andExpect(status().isNotFound());
		verify(lrepo, times(1)).findById(1);
		verifyNoMoreInteractions(lrepo);
	}
	
	
	/**
	 * curl -X PUT "http://localhost:8080/api/lecture/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"lectureId\": 1, \"week\": 0, \"title\": \"NEW Testing\", \"url\": \"https://le.ac.uk/\"}"
	 *
	 */
	@Test
	public void testUpdateLecture() throws Exception {
		Lecture currentLecture = new Lecture(1, 1, "SA", "www.SA.co.uk");
		when(lrepo.findById(currentLecture.getLectureId())).thenReturn(currentLecture);
		mockMvcLecture.perform(put("/api/lecture/{lectureId}", currentLecture.getLectureId())
		.content(om.writeValueAsString(currentLecture))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		verify(lrepo, times(1)).findById(currentLecture.getLectureId());
		
	
	}
	
	/**
	 * curl -X PUT "http://localhost:8080/api/lecture/5" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"lectureId\": 1, \"week\": 0, \"title\": \"NEW Testing\", \"url\": \"https://le.ac.uk/\"}"
	 * 
	 */
	@Test
	public void testUpdateLectureFail() throws Exception {
		Lecture lectures = new Lecture(1, 1, "SA", "www.SA.co.uk");
		when(lrepo.findById(lectures.getLectureId())).thenReturn(null);
		mockMvcLecture.perform(put("/api/lecture/{lectureId}", lectures.getLectureId())
		.content(om.writeValueAsString(lectures))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(lrepo, times(1)).findById(lectures.getLectureId());
		verifyNoMoreInteractions(lrepo);
	
	}	 
	
	/**
	 * 
	 * curl -X DELETE "http://localhost:8080/api/lecture/1" -H "accept: application/json"
	 */
	@Test
	public void testDeleteLecture() throws Exception {
		Lecture lectures =  new Lecture(1, 1, "SA", "www.SA.co.uk");
		when(lrepo.findById(lectures.getLectureId())).thenReturn(lectures);
		doNothing().when(lrepo).deleteById(lectures.getLectureId());
		mockMvcLecture.perform(delete("/api/lecture/{lectureId}", lectures.getLectureId()))
		.andExpect(status().isOk());
		verify(lrepo, times(1)).findById(lectures.getLectureId());
		verify(lrepo, times(1)).deleteById(lectures.getLectureId());
		verifyNoMoreInteractions(lrepo); 	
	}
	
	/**
	 * 
	 * curl -X DELETE "http://localhost:8080/api/lecture/10" -H "accept: application/json"
	 */
	@Test
	public void testDeleteLectureFail() throws Exception {
		Lecture lectures =  new Lecture(1, 1, "SA", "www.SA.co.uk");
		when(lrepo.findById(lectures.getLectureId())).thenReturn(null);
		mockMvcLecture.perform(delete("/api/lecture/{lectureId}", lectures.getLectureId()))
		.andExpect(status().isNotFound());
		verify(lrepo, times(1)).findById(lectures.getLectureId());
		verifyNoMoreInteractions(lrepo);
	}
	
	/**
	 * curl -X POST "http://localhost:8080/api/module/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"moduleId\": 1, \"code\": \"CO2103\", \"title\": \"Software Architecture\", \"semester\": 0, \"core\": false, \"lecture\": [ { \"lectureId\": 1, \"week\": 0, \"title\": \"Unit Testing\", \"url\": \"https://le.ac.uk/\", \"Module\": {} } ]}"
	 * curl -X GET "http://localhost:8080/api/module/1/lecture" -H "accept: application/json"
	 */
	@Test
	public void testGetLectureByModuleId() throws Exception {
		List<Lecture> lecture = Arrays.asList(new Lecture(1, 1, "SA", "www.SA.co.uk"), new Lecture(2, 2, "CA", "www.CA.co.uk"));
		Module module = new Module(1, "SA", "CO", 1, true, lecture);
		when(mrepo.findById(1)).thenReturn(module);
		mockMvcLecture.perform(get("/api/module/1/lecture"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].lectureId", is(1)))
		.andExpect(jsonPath("$[0].week", is(1)))
		.andExpect(jsonPath("$[0].title", is("SA")))
		.andExpect(jsonPath("$[0].url", is("www.SA.co.uk")))
		.andExpect(jsonPath("$[1].lectureId", is(2)))
		.andExpect(jsonPath("$[1].week", is(2)))
		.andExpect(jsonPath("$[1].title", is("CA")))
		.andExpect(jsonPath("$[1].url", is("www.CA.co.uk")));
		
	}
	
	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/module/5/lecture" -H "accept: application/json"
	 */
	@Test
	public void testGetLectureByModuleIdFail() throws Exception {
		when(mrepo.findById(1)).thenReturn(null);
		mockMvcLecture.perform(get("/api/module/{moduleId}/lecture", 1))
		.andExpect(status().isNotFound());
	}
	
	
	
	// ================================================== Convenor tests ========================================================================//
	
	
	
	/**
	 * DELETE CONVENOR FIRST
	 * curl -X DELETE "http://localhost:8080/api/convenor/1" -H "accept: application/json"
	 * curl -X POST "http://localhost:8080/api/convenor/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"convenorId\": 1, \"name\": \"Jose Rojas\", \"position\": \"GTA\", \"office\": \"F27\"}"
	 */
	@Test
	public void testAddConvenor() throws Exception {
		Convenor convenor = new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.findById(convenor.getConvenorId())).thenReturn(convenor);
		mockMvcConvenor.perform(post("/api/convenor/").content(om.writeValueAsString(convenor))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
		verify(crepo, times(1)).existsById(1);
	}
	
	
	/**
	 * 
	 * curl -X POST "http://localhost:8080/api/convenor/" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"convenorId\": 1, \"name\": \"Jose Rojas\", \"position\": \"GTA\", \"office\": \"F27\"}"
	 */
	@Test
	public void testAddConvenoreFail() throws Exception {
		Convenor convenor = new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.existsById(convenor.getConvenorId())).thenReturn(true);
		mockMvcConvenor.perform(post("/api/convenor/").content(om.writeValueAsString(convenor))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict());
		verify(crepo, times(1)).existsById(1);
		verifyNoMoreInteractions(crepo);
	}
	

	/**
	 * 
	 * curl -X GET "http://localhost:8080/api/convenor/" -H "accept: application/json"
	 */
	@Test
	public void testGetAllConvenors() throws Exception {
		List<Convenor> convenors = Arrays.asList(new Convenor(1, "Mr. Smith", "F11", Lecturer), new Convenor(2, "Mr. Lord", "F12", Lecturer));
		when(crepo.findAll()).thenReturn(convenors);
		mockMvcConvenor.perform(get("/api/convenor/"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(2))) 
		.andExpect(jsonPath("$[0].convenorId", is(1)))
		.andExpect(jsonPath("$[0].name", is("Mr. Smith")))
		.andExpect(jsonPath("$[0].office", is("F11")))
		.andExpect(jsonPath("$[0].position", is(Lecturer)))
		.andExpect(jsonPath("$[1].convenorId", is(2)))
		.andExpect(jsonPath("$[1].name", is("Mr. Lord")))
		.andExpect(jsonPath("$[1].office", is("F12")))
		.andExpect(jsonPath("$[1].position", is(Lecturer)));
		verify(crepo, times(1)).findAll();
		verifyNoMoreInteractions(crepo);
		}
	
	@Test
	public void testGetAllConvenorsFail() throws Exception {
		when(crepo.findAll()).thenReturn(null);
		mockMvcConvenor.perform(get("/api/convenor/")).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X GET "http://localhost:8080/api/convenor/1" -H "accept: application/json"
	 */
	@Test
	public void testGetConvenorById() throws Exception {
		Convenor convenor = new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.findById(1)).thenReturn(convenor);
		mockMvcConvenor.perform(get("/api/convenor/{convenorId}", 1))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.convenorId", is(1)))
		.andExpect(jsonPath("$.name", is("Mr. Smith")))
		.andExpect(jsonPath("$.office", is("F11")))
		.andExpect(jsonPath("$.position", is(Lecturer)));
		verify(crepo, times(1)).findById(1);
		verifyNoMoreInteractions(crepo);
		
	} 
	
	/**
	 * curl -X GET "http://localhost:8080/api/convenor/5" -H "accept: application/json"
	 */
	@Test
	public void testGetConvenorByIdFail() throws Exception {
		when(crepo.findById(1)).thenReturn(null);
		mockMvcConvenor.perform(get("/api/convenor/{convenorId}", 1))
		.andExpect(status().isNotFound());
		verify(crepo, times(1)).findById(1);
		verifyNoMoreInteractions(crepo);
		
	}
	
	/**
	 * 
	 * curl -X PUT "http://localhost:8080/api/convenor/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"convenorId\": 1, \"name\": \"Jose LastNameNotKnown\", \"position\": \"GTA\", \"office\": \"F27\"}"
	 */
	@Test
	public void testUpdateConvenor() throws Exception {
		Convenor currentConvenor = new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.findById(currentConvenor.getConvenorId())).thenReturn(currentConvenor);
		mockMvcConvenor.perform(put("/api/convenor/{convenorId}", currentConvenor.getConvenorId())
		.content(om.writeValueAsString(currentConvenor))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		verify(crepo, times(1)).findById(currentConvenor.getConvenorId());
		
	}
	
	
	

	/**
	 * 
	 * curl -X PUT "http://localhost:8080/api/convenor/5" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"convenorId\": 1, \"name\": \"Jose LastNameNotKnown\", \"position\": \"GTA\", \"office\": \"F27\"}"
	 */
	@Test
	public void testUpdateConvenorFail() throws Exception {
		Convenor convenor = new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.findById(convenor.getConvenorId())).thenReturn(null);
		mockMvcConvenor.perform(put("/api/convenor/{convenorId}", convenor.getConvenorId())
		.content(om.writeValueAsString(convenor))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
		verify(crepo, times(1)).findById(convenor.getConvenorId());
		verifyNoMoreInteractions(crepo);
	}	
	
	/**
	 * 
	 * curl -X DELETE "http://localhost:8080/api/convenor/1" -H "accept: application/json"
	 */
	@Test
	public void testDeleteConvenor() throws Exception {
		Convenor convenors =  new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.findById(convenors.getConvenorId())).thenReturn(convenors);
		doNothing().when(crepo).deleteById(convenors.getConvenorId());
		mockMvcConvenor.perform(delete("/api/convenor/{convenorId}", convenors.getConvenorId()))
		.andExpect(status().isOk());
		verify(crepo, times(1)).findById(convenors.getConvenorId());
		verify(crepo, times(1)).deleteById(convenors.getConvenorId());
		verifyNoMoreInteractions(crepo); 
		
	}
	
	/**
	 * curl -X DELETE "http://localhost:8080/api/convenor/20" -H "accept: application/json"
	 */
	@Test
	public void testDeleteConvenorFail() throws Exception {
		Convenor convenor =  new Convenor(1, "Mr. Smith", "F11", Lecturer);
		when(crepo.findById(convenor.getConvenorId())).thenReturn(null);
		mockMvcConvenor.perform(delete("/api/convenor/{convenorId}", convenor.getConvenorId())).andExpect(status().isNotFound());
		verify(crepo, times(1)).findById(convenor.getConvenorId());
		verifyNoMoreInteractions(crepo);
	}
	
}

	


