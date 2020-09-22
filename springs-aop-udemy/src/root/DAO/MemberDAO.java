package root.DAO;

import org.springframework.stereotype.Repository;

@Repository
public class MemberDAO
{
	public boolean addMember(
	)
	{
		System.out.println(getClass() + " - Adding Member DB Stuff");
		return true;
	}
}
