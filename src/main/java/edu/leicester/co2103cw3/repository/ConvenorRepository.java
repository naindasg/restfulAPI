package edu.leicester.co2103cw3.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103cw3.domain.Convenor;


public interface ConvenorRepository extends CrudRepository<Convenor, Integer> {
	public Convenor findById(int convenorId);
	public List<Convenor> findAll();
	//public void create(Convenor convenor);
	//public void update(Convenor currentConvenor);
}
