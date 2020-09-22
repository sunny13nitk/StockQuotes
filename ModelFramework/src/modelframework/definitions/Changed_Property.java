package modelframework.definitions;

/**
 * Class for Maintaining the Properties Names and Values that are changed in an Object from last Refreshed It
 * is part of Entity MetaData for an Object. Will be Initialized at time of Object Creation/Refreshed to/from
 * DBase
 */
public class Changed_Property
	{
		public String	fieldName;
		public String	dataType;
		public Object	value;
		
		/**
		 * @return the fieldName
		 */
		public String getFieldName()
			{
				return fieldName;
			}
			
		/**
		 * @param fieldName
		 *            the fieldName to set
		 */
		public void setFieldName(String fieldName)
			{
				this.fieldName = fieldName;
			}
			
		/**
		 * @return the dataType
		 */
		public String getDataType()
			{
				return dataType;
			}
			
		/**
		 * @param dataType
		 *            the dataType to set
		 */
		public void setDataType(String dataType)
			{
				this.dataType = dataType;
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
		 * @param fieldName
		 * @param dataType
		 * @param value
		 */
		public Changed_Property(String fieldName, String dataType, Object value)
			{
				super();
				this.fieldName = fieldName;
				this.dataType = dataType;
				this.value = value;
			}
			
		/**
		 * 
		 */
		public Changed_Property()
			{
				super();
				
			}
			
	}
