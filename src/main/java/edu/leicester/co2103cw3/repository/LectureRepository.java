package edu.leicester.co2103cw3.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103cw3.domain.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Integer> {
	public Lecture findById(int lectureId);
	public List<Lecture> findAll();
//	public void updateLecture(Lecture currentLecture);
	//public void create(Lecture lectures);
	//public void update(Lecture lectures);

}
