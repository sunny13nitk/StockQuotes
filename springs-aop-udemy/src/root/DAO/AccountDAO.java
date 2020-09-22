package root.DAO;

import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO
{
	public void addAccount(
	)
	{
		System.out.println(getClass() + ": Doing Db Work - Adding an Account");
	}
}
