package modelframework.exposed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import modelframework.JAXB.definitions.models.Model;
import modelframework.JAXB.definitions.objschemas.ObjectSchemas;
import modelframework.JAXB.implementations.Models_JAXB;
import modelframework.JAXB.implementations.ObjectSchemas_JAXB;
import modelframework.constants.Framework_Constants;
import modelframework.exceptions.EX_ModelInitialize;
import modelframework.exceptions.EX_ModelLoadException;
import modelframework.exceptions.EX_SchemaLoadException;
import modelframework.implementations.Connection_Pool;
import modelframework.implementations.MessagesFormatter;
import modelframework.implementations.Object_Info_Model;
import modelframework.usermanager.managers.UserManager;

/*********************************************************************************
 * System Class - Main Class to hold System Information & Metadata - DO NOT CHANGE
 *********************************************************************************/

public class FrameworkManager

{

	/**********************************************************
	 * Attributes Section
	 *********************************************************/
	// Framework Constants -- Initialized through Spring bean configuration

	private Framework_Constants		Constants;

	@Autowired
	// User Manager Session Bean
	private UserManager				UserManager;

	@Autowired
	// Messages Formatter Service Bean
	private MessagesFormatter		MessageFormatter;

	// Connection Pool
	private static Connection_Pool	ConnectionPool;

	// Model Loaded depending on Default Model Name paramter in Target
	// properties file
	private static Model			ModelLoaded;

	// Objects Information Factory
	private static Object_Info_Model	ObjectsInfoFactory;

	// Objects Schemas Factory
	private static ObjectSchemas		ObjectSchemaFactory;

	// Logger Object
	private static Logger			G_Logger;

	/******************************* Getter & Setters *********************************************/

	public Framework_Constants getConstants()
	{
		return Constants;
	}

	public void setConstants(Framework_Constants constants)
	{
		Constants = constants;
	}

	public static Connection_Pool getConnectionPool()
	{
		return ConnectionPool;
	}

	public static Model getModelLoaded()
	{
		return ModelLoaded;
	}

	public static Object_Info_Model getObjectsInfoFactory()
	{
		return ObjectsInfoFactory;
	}

	public static ObjectSchemas getObjectSchemaFactory()
	{
		return ObjectSchemaFactory;
	}

	public static Logger getLogger()
	{
		return G_Logger;
	}

	public MessagesFormatter getMessageFormatter()
	{
		return MessageFormatter;
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager()
	{
		return UserManager;
	}

	/**********************************************************
	 * Constructor
	 *********************************************************/

	// Instantiate Framework Manager Using Constans and Data Source beans
	// configured in Target Program Properties
	public FrameworkManager(Framework_Constants constants, DataSource ds)
	{

		// First Instantiate the Constants //
		this.Constants = constants;

		if (ds != null)
		{
			ConnectionPool = new Connection_Pool(ds);
		}
		// Do not initialize framework here. With the connection pool initialized
		// First the user will be validated and on successful validation event
		// the Framework will be initialized andUser prop will be set in Framework Manager
	}

	/*************************************************************************************************
	 * Initilaize Framework - Only to be initilaized once the User is validated - To Be called in user
	 * Validation event
	 * 
	 * @throws Exception
	 *************************************************************************************************/

	// Initialize Framework
	public void Initialize_Framework() throws Exception
	{
		if (Constants != null)
		{

			try
			{
				// First Initialize the Logger for Session
				Initialize_Logger();

				// Create a new Connection Object to Start
				if (ConnectionPool.getConnection() != null)
				{
					// Metadata Initialization for Framework

					ObjectSchemas_JAXB Obj_JAXB = new ObjectSchemas_JAXB(Constants);
					if (Obj_JAXB != null)
					{

						try
						{
							ObjectSchemaFactory = Obj_JAXB.Get_ObjectSchemas();
						}
						catch (EX_SchemaLoadException Ex)
						{
							this.getMessageFormatter().generate_message_snippet(Ex);
						}
					}

					Models_JAXB Modelfactory = new Models_JAXB(Constants);
					if (Modelfactory != null)
					{
						try
						{
							ModelLoaded = (Model) Modelfactory.Load_Model_by_Name(Constants.getModel_default());
							if (ModelLoaded != null)
							{
								ObjectsInfoFactory = new Object_Info_Model(true);
							}
						}
						catch (EX_ModelLoadException Ex)
						{
							this.getMessageFormatter().generate_message_snippet(Ex);
						}
					}
				}
			}
			catch (Exception e)
			{

				EX_ModelInitialize Ex = new EX_ModelInitialize(e.getMessage());
				this.MessageFormatter.generate_message_snippet(Ex);
			}
		}
	}

	// Initialize the Logging Object for Sessionfor Logged in User
	private void Initialize_Logger() throws Exception
	{
		if (this.UserManager != null)
		{
			if (this.UserManager.Get_LoggedUser() != null)
			{
				FileHandler fh = null;
				// create log file string
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Calendar cal = Calendar.getInstance();
				String date_time = (dateFormat.format(cal.getTime()));
				String fname_path = Constants.getLogdir();
				String filepath = fname_path + "./" + this.UserManager.Get_LoggedUser() + "_" + date_time + ".log";

				fh = new FileHandler(filepath);

				FrameworkManager.G_Logger = Logger.getLogger("Logger");
				G_Logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				G_Logger.setUseParentHandlers(false);
			}
		}
	}

}
