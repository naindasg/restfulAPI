package edu.leicester.co2103cw3.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Convenor {
	@Id
	private int convenorId;
	private String name;
	private String office;
	private Position position;
	@OneToMany
	private List<Module> module;
	
	
	public int getConvenorId() {
		return convenorId;
	}
	public void setConvenorId(int convenorId) {
		this.convenorId = convenorId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public enum Position {
		GTA, Lecturer, Professor
	}
	
	public List<Module> getModule() {
		return module;
	}
	public void setModule(List<Module> module) {
		this.module = module;
	}
	
	
	
	@Override
	public String toString() {
		return "Convenor [convenorId=" + convenorId + ", name=" + name + ", office=" + office + ", position=" + position
				+ ", module=" + module + "]";
	}
	public Convenor(int convenorId, String name, String office, Position position) {
		super();
		this.convenorId = convenorId;
		this.name = name;
		this.office = office;
		this.position = position;
	}
	
	public Convenor() {
		// TODO Auto-generated constructor stub
	}
	
	public Convenor(int convenorId, String name, String office, Position position, List<Module> module) {
		super();
		this.convenorId = convenorId;
		this.name = name;
		this.office = office;
		this.position = position;
		this.module = module;
	}
	
	
	
	
	
	
}
