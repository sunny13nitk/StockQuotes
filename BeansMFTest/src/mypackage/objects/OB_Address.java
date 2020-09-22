/**
 * 
 */
package mypackage.objects;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

/**
 * 
 * Address Model Class :Dependant Object
 */
@Component("OB_Address") //Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) //Always create a new bean Instance
public class OB_Address extends DependantObject
	{
		private String	AC_Num;
		private String	Street1;
		private String	Street2;
		private String	City;
		private String	State;
		private String	Country;
		private String	Postal_Code;
		private String	Notes;
		
		/**
		 * @return the aC_Num
		 */
		public String getAC_Num()
			{
				return AC_Num;
			}
			
		/**
		 * @param aC_Num
		 *            the aC_Num to set
		 */
		public void setAC_Num(String aC_Num)
			{
				AC_Num = aC_Num;
			}
			
		/**
		 * @return the street1
		 */
		public String getStreet1()
			{
				return Street1;
			}
			
		/**
		 * @param street1
		 *            the street1 to set
		 */
		public void setStreet1(String street1)
			{
				Street1 = street1;
			}
			
		/**
		 * @return the street2
		 */
		public String getStreet2()
			{
				return Street2;
			}
			
		/**
		 * @param street2
		 *            the street2 to set
		 */
		public void setStreet2(String street2)
			{
				Street2 = street2;
			}
			
		/**
		 * @return the city
		 */
		public String getCity()
			{
				return City;
			}
			
		/**
		 * @param city
		 *            the city to set
		 */
		public void setCity(String city)
			{
				City = city;
			}
			
		/**
		 * @return the state
		 */
		public String getState()
			{
				return State;
			}
			
		/**
		 * @param state
		 *            the state to set
		 */
		public void setState(String state)
			{
				State = state;
			}
			
		/**
		 * @return the country
		 */
		public String getCountry()
			{
				return Country;
			}
			
		/**
		 * @param country
		 *            the country to set
		 */
		public void setCountry(String country)
			{
				Country = country;
			}
			
		/**
		 * @return the postal_Code
		 */
		public String getPostal_Code()
			{
				return Postal_Code;
			}
			
		/**
		 * @param postal_Code
		 *            the postal_Code to set
		 */
		public void setPostal_Code(String postal_Code)
			{
				Postal_Code = postal_Code;
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
		 *            the notes to set
		 */
		public void setNotes(String notes)
			{
				Notes = notes;
			}
			
		/**
		 * @param aC_Num
		 * @param street1
		 * @param street2
		 * @param city
		 * @param state
		 * @param country
		 * @param postal_Code
		 * @param notes
		 */
		public OB_Address(String aC_Num, String street1, String street2, String city, String state, String country,
		        String postal_Code, String notes)
			{
				super();
				AC_Num = aC_Num;
				Street1 = street1;
				Street2 = street2;
				City = city;
				State = state;
				Country = country;
				Postal_Code = postal_Code;
				Notes = notes;
			}
			
		/**
		 * 
		 */
		public OB_Address()
			{
				
			}
			
	}
