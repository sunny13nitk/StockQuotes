/**
 * 
 */
package modelframework.definitions;

/**
 * Primary Key class definition
 *
 */
public class PrimaryKey
	{
		
		public String	objField;
		
		public String	tableField;
		
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
		 * @return the tableField
		 */
		public String getTableField()
			{
				return tableField;
			}
			
		/**
		 * @param tableField
		 *            the tableField to set
		 */
		public void setTableField(String tableField)
			{
				this.tableField = tableField;
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
		 * @param tableField
		 * @param value
		 */
		public PrimaryKey(String objField, String tableField, Object value)
			{
				super();
				this.objField = objField;
				this.tableField = tableField;
				this.value = value;
			}
			
		/**
		 * 
		 */
		public PrimaryKey()
			{
				super();
				
			}
			
	}
