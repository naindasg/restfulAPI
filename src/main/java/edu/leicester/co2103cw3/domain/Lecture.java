package edu.leicester.co2103cw3.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Lecture {
	@Id
	private int lectureId;
	private int week;
	private String title;
	private String url;
	@ManyToOne
	private Module module;

	public int getLectureId() {
		return lectureId;
	}

	public void setLectureId(int lectureId) {
		this.lectureId = lectureId;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	

	@Override
	public String toString() {
		return "Lecture [lectureId=" + lectureId + ", week=" + week + ", title=" + title + ", url=" + url + ", module="
				+ module + "]";
	}

	//constructor 1
	public Lecture() {
		}
	
	//constructor 2
	public Lecture(int lectureId, int week, String title, String url) {
		super();
		this.lectureId = lectureId;
		this.week = week;
		this.title = title;
		this.url = url;
	}
	
	
	//constructor 3
	public Lecture(int lectureId, int week, String title, String url, Module module) {
		super();
		this.lectureId = lectureId;
		this.week = week;
		this.title = title;
		this.url = url;
		this.module = module;
	}
	
	
	
	
	
}
