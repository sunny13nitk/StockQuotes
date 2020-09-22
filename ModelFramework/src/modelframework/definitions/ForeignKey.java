/**
 * 
 */
package modelframework.definitions;

/**
 * Foreign Key class definition
 *
 */
public class ForeignKey
	{
		public String	objField;
		public Object	value;
		
		/**
		 * @return the objField
		 */
		public String getObjField()
			{
				return objField;
			}
			
		/**
		 * @param objField
		 *            the objField to set
		 */
		public void setObjField(String objField)
			{
				this.objField = objField;
			}
			
		/**
		 * @return the value
		 */
		public Object getValue()
			{
				return value;
			}
			
		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(Object value)
			{
				this.value = value;
			}
			
		/**
		 * @param objField
		 * @param value
		 */
		public ForeignKey(String objField, Object value)
			{
				super();
				this.objField = objField;
				this.value = value;
			}
			
		/**
		 * 
		 */
		public ForeignKey()
			{
				super();
				
			}
			
	}
