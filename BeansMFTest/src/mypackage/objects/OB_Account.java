/**
 * 
 */
package mypackage.objects;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.annotations.AfterCommitHandler;
import modelframework.annotations.BeforeCommitValidator;
import modelframework.annotations.BeforeSaveHandler;
import modelframework.annotations.BeforeSaveValidator;
import modelframework.annotations.UserAware;
import modelframework.implementations.RootObject;
import mypackage.enums.Enums;
import mypackage.enums.Enums.Account_Type;
import mypackage.enums.Enums.Book_Type;

/**
 * Account Model Class - Root Object
 *
 */
@Component("OB_Account") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always Create a New Instance
@UserAware // User specific Bean
// Always create a new bean instance
public class OB_Account extends RootObject
{
	private String				AC_Num;
	private Enums.Account_Type	AC_Type;
	private String				Cust_ID;
	private String				Owner;
	private String				Bank_Name;
	private String				Nominee;
	private double				Current_Balance;
	private Enums.Book_Type		Credit_Book;
	private Enums.Book_Type		Debit_Book;
	private String				Notes;
	private String				IFSC_Code;
	private Boolean			Active;

	/**
	 * @return the aC_Num
	 */
	public String getAC_Num()
	{
		return AC_Num;
	}

	/**
	 * @param aC_Num
	 *             the aC_Num to set
	 */
	public void setAC_Num(String aC_Num)
	{
		AC_Num = aC_Num;
	}

	/**
	 * @return the aC_Type
	 */
	public Enums.Account_Type getAC_Type()
	{
		return AC_Type;
	}

	/**
	 * @param aC_Type
	 *             the aC_Type to set
	 */
	public void setAC_Type(Enums.Account_Type aC_Type)
	{
		AC_Type = aC_Type;
	}

	/**
	 * @return the cust_ID
	 */
	public String getCust_ID()
	{
		return Cust_ID;
	}

	/**
	 * @param cust_ID
	 *             the cust_ID to set
	 */
	public void setCust_ID(String cust_ID)
	{
		Cust_ID = cust_ID;
	}

	/**
	 * @return the owner
	 */
	public String getOwner()
	{
		return Owner;
	}

	/**
	 * @param owner
	 *             the owner to set
	 */
	public void setOwner(String owner)
	{
		Owner = owner;
	}

	/**
	 * @return the bank_Name
	 */
	public String getBank_Name()
	{
		return Bank_Name;
	}

	/**
	 * @param bank_Name
	 *             the bank_Name to set
	 */
	public void setBank_Name(String bank_Name)
	{
		Bank_Name = bank_Name;
	}

	/**
	 * @return the nominee
	 */
	public String getNominee()
	{
		return Nominee;
	}

	/**
	 * @param nominee
	 *             the nominee to set
	 */
	public void setNominee(String nominee)
	{
		Nominee = nominee;
	}

	/**
	 * @return the current_Balance
	 */
	public double getCurrent_Balance()
	{
		return Current_Balance;
	}

	/**
	 * @param current_Balance
	 *             the current_Balance to set
	 */
	public void setCurrent_Balance(Double current_Balance)
	{
		Current_Balance = current_Balance;
	}

	/**
	 * @return the credit_Book
	 */
	public Enums.Book_Type getCredit_Book()
	{
		return Credit_Book;
	}

	/**
	 * @param credit_Book
	 *             the credit_Book to set
	 */
	public void setCredit_Book(Enums.Book_Type credit_Book)
	{
		Credit_Book = credit_Book;
	}

	/**
	 * @return the debit_Book
	 */
	public Enums.Book_Type getDebit_Book()
	{
		return Debit_Book;
	}

	/**
	 * @param debit_Book
	 *             the debit_Book to set
	 */
	public void setDebit_Book(Enums.Book_Type debit_Book)
	{
		Debit_Book = debit_Book;
	}

	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return Notes;
	}

	/**
	 * @param notes
	 *             the notes to set
	 */
	public void setNotes(String notes)
	{
		Notes = notes;
	}

	/**
	 * @return the iFSC_Code
	 */
	public String getIFSC_Code()
	{
		return IFSC_Code;
	}

	/**
	 * @param iFSC_Code
	 *             the iFSC_Code to set
	 */
	public void setIFSC_Code(String iFSC_Code)
	{
		IFSC_Code = iFSC_Code;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive()
	{
		return Active;
	}

	/**
	 * @param active
	 *             the active to set
	 */
	public void setActive(Boolean active)
	{
		Active = active;
	}

	public OB_Account(String aC_Num, Account_Type aC_Type, String cust_ID, String owner, String bank_Name, String nominee, double current_Balance,
	          Book_Type credit_Book, Book_Type debit_Book, String notes, String iFSC_Code, Boolean active)
	{
		super();
		AC_Num = aC_Num;
		AC_Type = aC_Type;
		Cust_ID = cust_ID;
		Owner = owner;
		Bank_Name = bank_Name;
		Nominee = nominee;
		Current_Balance = current_Balance;
		Credit_Book = credit_Book;
		Debit_Book = debit_Book;
		Notes = notes;
		IFSC_Code = iFSC_Code;
		Active = active;
	}

	/**
	 * 
	 */
	public OB_Account()
	{

	}

	@BeforeSaveValidator
	public boolean validateBeforeSave()
	{
		boolean error = false;
		// If Account Does not have AC Type or is not active
		if (this.getAC_Type() == null || this.getActive() == false)
		{
			error = true;
		}
		return error;
	}

	@BeforeSaveHandler
	public void convertAmttoUSD()
	{
		if (this.getCurrent_Balance() > 0)
		{
			this.setCurrent_Balance((this.getCurrent_Balance() / 60));
		}
	}

	@BeforeCommitValidator
	public boolean beforeCommit()
	{
		boolean commit = true;
		if (this.getCurrent_Balance() < 5)
		{
			commit = false;
		}
		return commit;
	}

	@AfterCommitHandler
	public void successful()
	{
		System.out.println("Account saved successfully -  " + this.getAC_Num());
	}

}