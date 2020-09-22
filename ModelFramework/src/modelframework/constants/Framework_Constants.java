package modelframework.constants;

public class Framework_Constants
	{
		// DB Specific constants
		private String	url;
		private String	driver;
		
		// Logging File Directory
		private String	logdir;
		private String	logsuffix;
		
		// Jaxable Constants
		private String	loc_model_schema;
		private String	loc_model_xml;
		
		private String	loc_model_xml_gen;
		
		// Default Model to Load
		private String	model_default;
		
		// Jaxable Constants for Object Schemas Information
		private String	loc_objschemas_schema;
		private String	loc_objschemas_xml;
		
		private String	loc_objschemas_xml_gen;
		
		public String getLoc_objschemas_schema()
			{
				return loc_objschemas_schema;
			}
			
		public String getLoc_objschemas_xml()
			{
				return loc_objschemas_xml;
			}
			
		public String getLoc_objschemas_xml_gen()
			{
				return loc_objschemas_xml_gen;
			}
			
		public String getLoc_model_xml_gen()
			{
				return loc_model_xml_gen;
			}
			
		public String getUrl()
			{
				return url;
			}
			
		public String getDriver()
			{
				return driver;
			}
			
		public String getLogdir()
			{
				return logdir;
			}
			
		public String getLogsuffix()
			{
				return logsuffix;
			}
			
		public String getLoc_model_schema()
			{
				return loc_model_schema;
			}
			
		public String getLoc_model_xml()
			{
				return loc_model_xml;
			}
			
		public String getModel_default()
			{
				return model_default;
			}
			
		public Framework_Constants(String url, String driver, String logdir, String logsuffix, String loc_model_schema,
		        String loc_model_xml, String loc_model_xml_gen, String model_default, String loc_objschemas_schema,
		        String loc_objschemas_xml, String loc_objschemas_xml_gen)
			{
				
				this.url = url;
				this.driver = driver;
				this.logdir = logdir;
				this.logsuffix = logsuffix;
				this.loc_model_schema = loc_model_schema;
				this.loc_model_xml = loc_model_xml;
				this.loc_model_xml_gen = loc_model_xml_gen;
				this.model_default = model_default;
				this.loc_objschemas_schema = loc_objschemas_schema;
				this.loc_objschemas_xml = loc_objschemas_xml;
				this.loc_objschemas_xml_gen = loc_objschemas_xml_gen;
			}
			
		public Framework_Constants()
			{
				
			}
			
	}
