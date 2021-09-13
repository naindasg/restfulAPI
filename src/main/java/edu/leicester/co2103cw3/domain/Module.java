package edu.leicester.co2103cw3.domain;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import edu.leicester.co2103cw3.repository.ModuleRepository;

@Entity
public class Module {
	@Id
	private int moduleId;
	private String code;
	private String title;
	private int semester;
	private boolean core;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn
	private List<Lecture> lecture;
	@ManyToOne
	private Convenor convenor;
	

	public int getModuleId() {
		return moduleId;
	}


	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public int getSemester() {
		return semester;
	}


	public void setSemester(int semester) {
		this.semester = semester;
	}


	public boolean isCore() {
		return core;
	}


	public void setCore(boolean core) {
		this.core = core;
	}


	public List<Lecture> getLecture() {
		return lecture;
	}


	public void setLecture(List<Lecture> lecture) {
		this.lecture = lecture;
	}


	public Convenor getConvenor() {
		return convenor;
	}


	public void setConvenor(Convenor convenor) {
		this.convenor = convenor;
	}



	@Override
	public String toString() {
		return "Module [moduleId=" + moduleId + ", code=" + code + ", title=" + title + ", semester=" + semester
				+ ", core=" + core + ", lecture=" + lecture + "]";
	}
	
	
	
	//Constructor number 1
	public Module() {
	}
	
	
	//Second constructor created to avoid adding lectures for simplification
	public Module(int moduleId, String code, String title, int semester, boolean core) {
		super();
		this.moduleId = moduleId;
		this.code = code;
		this.title = title;
		this.semester = semester;
		this.core = core;
		this.lecture = lecture;
	}
	
	
	
	//Constructor used for adding lectures 
	public Module(int moduleId, String code, String title, int semester, boolean core, List<Lecture> lecture) {
		super();
		this.moduleId = moduleId;
		this.code = code;
		this.title = title;
		this.semester = semester;
		this.core = core;
		this.lecture = lecture;
	}
	
	public Object thenReturn(List<Lecture> lecture2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	


}
	
	

	
	


