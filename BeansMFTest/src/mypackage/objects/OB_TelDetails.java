/**
 * 
 */
package mypackage.objects;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import modelframework.implementations.DependantObject;

/**
 * Telephone Details Model Class :Dependant Object
 *
 */
@Component("OB_TelDetails") // Initialize as bean in Context with name as Class Name
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Always create a new bean Instance
public class OB_TelDetails extends DependantObject
	{
		private int		Address_ID;
		private String	TelNumber;
		
		/**
		 * @return the telNumber
		 */
		public String getTelNumber()
			{
				return TelNumber;
			}
			
		/**
		 * @param telNumber
		 *           the telNumber to set
		 */
		public void setTelNumber(String telNumber)
			{
				TelNumber = telNumber;
			}
			
		/**
		 * @return the address_ID
		 */
		public int getAddress_ID()
			{
				return Address_ID;
			}
			
		/**
		 * @param address_ID
		 *           the address_ID to set
		 */
		public void setAddress_ID(int address_ID)
			{
				Address_ID = address_ID;
			}
			
		/**
		 * @param address_ID
		 * @param telNumber
		 */
		public OB_TelDetails(int address_ID, String telNumber)
			{
				
				Address_ID = address_ID;
				TelNumber = telNumber;
			}
			
		public OB_TelDetails()
			{
				
			}
			
	}
