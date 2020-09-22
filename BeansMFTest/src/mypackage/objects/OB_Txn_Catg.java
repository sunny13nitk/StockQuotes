package mypackage.objects;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.annotations.UserAware;
import modelframework.implementations.RootObject;
import mypackage.enums.Enums;

@Component("OB_Txn_Catg") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
@UserAware // User specific Bean
public class OB_Txn_Catg extends RootObject
{
	private String			Category;

	private Enums.Book_Type	Book_Type;

	private Date			DateCreate;

	/**
	 * @return the dateCreate
	 */
	public Date getDateCreate()
	{
		return DateCreate;
	}

	/**
	 * @param dateCreate
	 *             the dateCreate to set
	 */
	public void setDateCreate(Date dateCreate)
	{
		DateCreate = dateCreate;
	}

	public String getCategory()
	{
		return Category;
	}

	public void setCategory(String category)
	{
		Category = category;
	}

	public Enums.Book_Type getBook_Type()
	{
		return Book_Type;
	}

	public void setBook_Type(Enums.Book_Type book_Type)
	{
		Book_Type = book_Type;
	}

	public OB_Txn_Catg()
	{

	}

	public OB_Txn_Catg(String category, Enums.Book_Type book_Type, Date crDate)
	{
		Category = category;
		Book_Type = book_Type;
		DateCreate = crDate;
	}

	public OB_Txn_Catg(String category, int bk_type_val, Date crDate)
	{
		Category = category;

		for ( Enums.Book_Type BookType : Enums.Book_Type.values() )
		{
			if (BookType.getValue() == bk_type_val)
			{
				this.Book_Type = BookType;
			}
		}

		DateCreate = crDate;
	}
}
