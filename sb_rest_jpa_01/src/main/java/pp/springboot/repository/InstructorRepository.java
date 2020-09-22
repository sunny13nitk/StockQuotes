package pp.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pp.springboot.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Integer>
{
	
}
