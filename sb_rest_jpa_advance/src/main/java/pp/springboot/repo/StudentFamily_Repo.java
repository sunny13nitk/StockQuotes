package pp.springboot.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import pp.springboot.entity.Student_Family;

@RepositoryRestResource(path = "familyInfo")
public interface StudentFamily_Repo extends JpaRepository<Student_Family, Integer>
{
	public List<Student_Family> findAllByStudentEmailIgnoreCase(
	        @Param("studentEmail") String studentEmail
	);
	
}
