package root.user.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import root.busslogic.entity.FundLine;
import root.busslogic.entity.Portfolio;

@Entity
@Table(
        /*
         * Since user is std. jdbc authentication table name, using same table name needs to be masked using ` `
         */
        name = "`user`"
)
public class User
{
	
	@Id
	@GeneratedValue(
	        strategy = GenerationType.IDENTITY
	)
	private int id;
	
	@Column(
	        name = "username"
	)
	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	@ManyToMany(
	        fetch = FetchType.EAGER, cascade = CascadeType.ALL
	)
	@JoinTable(
	        name = "users_roles", joinColumns = @JoinColumn(
	                name = "user_id"
	        ), inverseJoinColumns = @JoinColumn(
	                name = "role_id"
	        )
	)
	private List<Role> roles;
	
	@OneToMany(
	        cascade = CascadeType.ALL, fetch = FetchType.LAZY
	)
	@JoinColumn(
	        name = "uid"
	)
	private List<Portfolio> portfolios;
	
	@OneToMany(
	        cascade = CascadeType.ALL, fetch = FetchType.LAZY
	)
	@JoinColumn(
	        name = "uid"
	)
	private List<FundLine> fundLines;
	
	public List<FundLine> getFundLines(
	)
	{
		return fundLines;
	}
	
	public void setFundLines(
	        List<FundLine> fundLines
	)
	{
		this.fundLines = fundLines;
	}
	
	public List<Portfolio> getPortfolios(
	)
	{
		return portfolios;
	}
	
	public void setPortfolios(
	        List<Portfolio> portfolios
	)
	{
		this.portfolios = portfolios;
	}
	
	public List<Role> getRoles(
	)
	{
		return roles;
	}
	
	public void setRoles(
	        List<Role> roles
	)
	{
		this.roles = roles;
	}
	
	public int getId(
	)
	{
		return id;
	}
	
	public void setId(
	        int id
	)
	{
		this.id = id;
	}
	
	public String getUserName(
	)
	{
		return userName;
	}
	
	public void setUserName(
	        String userName
	)
	{
		this.userName = userName;
	}
	
	public String getPassword(
	)
	{
		return password;
	}
	
	public void setPassword(
	        String password
	)
	{
		this.password = password;
	}
	
	public String getFirstName(
	)
	{
		return firstName;
	}
	
	public void setFirstName(
	        String firstName
	)
	{
		this.firstName = firstName;
	}
	
	public String getLastName(
	)
	{
		return lastName;
	}
	
	public void setLastName(
	        String lastName
	)
	{
		this.lastName = lastName;
	}
	
	public String getEmail(
	)
	{
		return email;
	}
	
	public void setEmail(
	        String email
	)
	{
		this.email = email;
	}
	
	public User(
	        String userName, String firstName, String lastName, String email
	)
	{
		super();
		
		this.userName  = userName;
		this.password  = new String();
		this.firstName = firstName;
		this.lastName  = lastName;
		this.email     = email;
	}
	
	public User(
	)
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * UTILITY METHODS - HIBERNATE RELATIONS OR OTHERS
	 */
	
}
