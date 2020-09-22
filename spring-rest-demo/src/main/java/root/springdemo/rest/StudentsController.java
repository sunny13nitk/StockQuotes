package root.springdemo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import root.springdemo.Exceptions.StudentException;
import root.springdemo.entity.Student;

@RestController
@RequestMapping(
    "/api"
)
public class StudentsController
{
	
	private List<Student> students;
	
	@PostConstruct
	public void loadData(
	)
	{
		this.students = new ArrayList<Student>();
		students.add(new Student("Sunny", "Bhardwaj"));
		students.add(new Student("Vishal", "Chugh"));
		students.add(new Student("Vibhor", "Gupta"));
		students.add(new Student("Sandeep", "Gupta"));
	}
	
	@GetMapping(
	    "/students"
	)
	public List<Student> getStudents(
	)
	{
		return this.students;
	}
	
	@GetMapping(
	    "/students/{studentId}"
	)
	public Student getStudentbyId(
	        @PathVariable int studentId
	)
	{
		
		if (studentId < 0 || studentId > students.size())
		{
			throw new StudentException("Student id not found - " + studentId);
		}
		return students.get(studentId);
	}
	
}
