package edu.leicester.co2103cw3.repository;

import java.util.List;


import org.springframework.data.repository.CrudRepository;
import edu.leicester.co2103cw3.domain.Module;

public interface ModuleRepository extends CrudRepository<Module, Integer> {
	public Module findById(int moduleId);
	public List<Module> findAll();
	//public void save(Matcher<Module> any);
//	public void updateModule(Module currentModule);

	

}
