package mypackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import modelframework.JAXB.implementations.Models_JAXB;
import modelframework.constants.Framework_Constants;
import modelframework.definitions.EntityMetadata;
import modelframework.enums.system.modelEnums.entityMode;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IPropertyAwareMessage;
import modelframework.interfaces.IQueryService;
import modelframework.managers.ModelObjectFactory;
import modelframework.managers.QueryManager;
import modelframework.types.TY_Filter;
import modelframework.types.TY_NameValue;
import modelframework.usermanager.exceptions.EX_DuplicateUserException;
import modelframework.usermanager.exceptions.EX_InvalidLogonException;
import modelframework.usermanager.exceptions.EX_UserCreationException;
import modelframework.usermanager.exceptions.EX_UserLogonException;
import modelframework.usermanager.managers.UserManager;
import modelframework.usermanager.models.SimpleUser;
import modelframework.usermanager.services.CreateUserService;
import modelframework.usermanager.services.LogonService;
import modelframework.utilities.EncryptionUtility;
import mypackage.Interfaces.IExecutable;
import mypackage.definitions.ExecutionBeanName;
import mypackage.enums.Enums.Account_Type;
import mypackage.enums.Enums.Book_Type;
import mypackage.objects.OB_Account;
import mypackage.objects.OB_Address;
import mypackage.objects.OB_TelDetails;
import mypackage.objects.OB_Txn_Catg;
import scriptsengine.constants.PriceProjectionFactorsConstants;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.managers.StatisticsManager;
import scriptsengine.pojos.OB_Scrip_BalSheet;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.pojos.OB_Scrip_QuartersData;
import scriptsengine.portfolio.definitions.TY_QtyPriceDate;
import scriptsengine.portfolio.definitions.TY_ScripBuy;
import scriptsengine.portfolio.definitions.TY_ScripBuySummary;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.pojos.OB_Positions_Item;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.portfolio.services.interfaces.IScripBuyAssistSrv;
import scriptsengine.pricingengine.JAXB.definitions.PathsJAXBPP;
import scriptsengine.pricingengine.JAXB.implementations.PriceProjectionConfig_JAXB;
import scriptsengine.pricingengine.definitions.TY_AvgPE;
import scriptsengine.pricingengine.definitions.TY_Last_NP_FVR;
import scriptsengine.pricingengine.definitions.TY_NPSD;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.definitions.TY_PricesReturn;
import scriptsengine.pricingengine.reports.definitions.TY_PriceWatchItem;
import scriptsengine.pricingengine.reports.definitions.TY_ReportPPS_Default;
import scriptsengine.pricingengine.reports.interfaces.IPriceWatchService;
import scriptsengine.pricingengine.services.interfaces.ISA_AvgPEService;
import scriptsengine.pricingengine.services.interfaces.ISA_ENPRService;
import scriptsengine.pricingengine.services.interfaces.ISA_LastNettProfit_FVR_Service;
import scriptsengine.pricingengine.services.interfaces.ISA_NPDService;
import scriptsengine.pricingengine.services.interfaces.ISA_ScripPriceProjectionService;
import scriptsengine.reportsengine.JAXB.definitions.PathsJAXBRE;
import scriptsengine.reportsengine.JAXB.implementations.RepXLS_Srv_MappingConfig_JAXB;
import scriptsengine.reportsengine.interfaces.IXLSReportAware;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.reportsengine.repDS.definitions.TY_ScRoot_AttrContainers;
import scriptsengine.reportsengine.repDS.interfaces.IReportDataSource;
import scriptsengine.sectors.services.implementations.SectorsService;
import scriptsengine.simulations.sell.definitions.TY_Qty_Price;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI;
import scriptsengine.simulations.sell.definitions.TY_SellProposalI_Summary;
import scriptsengine.simulations.sell.definitions.TY_Sell_Proposal;
import scriptsengine.simulations.sell.definitions.TY_Sell_Quote;
import scriptsengine.simulations.sell.definitions.TY_TaxProjections;
import scriptsengine.simulations.sell.interfaces.IScripSellSimulation;
import scriptsengine.statistics.JAXB.definitions.PathsJAXBSS;
import scriptsengine.statistics.JAXB.implementations.SSConfig_JAXB;
import scriptsengine.statistics.JAXB.interfaces.IStatsSrvConfigMetadata;
import scriptsengine.statistics.definitions.StDevHead;
import scriptsengine.statistics.definitions.StDevItem;
import scriptsengine.statistics.definitions.StDevResult;
import scriptsengine.statistics.definitions.TY_AttribSingleVal;
import scriptsengine.statistics.definitions.TY_Sector_AttrContainers;
import scriptsengine.statistics.exceptions.EX_StdDeviation;
import scriptsengine.statistics.interfaces.IMShareScripAnalytics;
import scriptsengine.statistics.interfaces.IScripAnalysisSrv;
import scriptsengine.statistics.interfaces.IScripHealthSrv;
import scriptsengine.statistics.interfaces.IStDev;
import scriptsengine.taxation.JAXB.definitions.PathsJAXBTE;
import scriptsengine.taxation.JAXB.implementations.Obj_ITaxableSrv_MappingConfig_JAXB;
import scriptsengine.uploadengine.JAXB.definitions.PathsJAXB;
import scriptsengine.uploadengine.JAXB.implementations.Workbook_JAXB;
import scriptsengine.uploadengine.definitions.ScripDBSnaphot;
import scriptsengine.uploadengine.definitions.ScripDataContainer;
import scriptsengine.uploadengine.definitions.SheetDBSnapShot;
import scriptsengine.uploadengine.definitions.SheetSplitAttrs;
import scriptsengine.uploadengine.managers.ScripDataManager;
import scriptsengine.uploadengine.services.interfaces.IScripDBSnapShot;
import scriptsengine.uploadengine.services.interfaces.IScripDataContainerSrv;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;
import scriptsengine.uploadengine.updateIdentifiers.services.interfaces.IScripMassUpdateSrv;
import scriptsengine.uploadengineSC.Metadata.JAXB.definitions.PathsJAXBSM;
import scriptsengine.uploadengineSC.Metadata.JAXB.implementations.SCWBMetadata_JAXB;
import scriptsengine.uploadengineSC.Metadata.definitions.SCSheetMetadata;
import scriptsengine.uploadengineSC.Metadata.services.interfaces.ISCWBMetadataSrv;
import scriptsengine.utilities.dateStringUtil.JAXB.definitions.PathsJAXBMO;
import scriptsengine.utilities.dateStringUtil.JAXB.definitions.monthsConfig;
import scriptsengine.utilities.dateStringUtil.JAXB.implementations.monthsConfig_JAXB;
import scriptsengine.utilities.types.PenultimateQYear;

public class Test
{
	
	public static void main(
	        String[] args
	)
	{
		try
		{
			test_writeup();
			// test_genrateSMMdt_Xml();
			// test_Models_JAXB();
			// test_SellScripSimulation();
			// test_SellScrip();
			// test_buyScripwoCtxtRefresh();
			// testversion2();
			// test_buyScrip();
			// test_scBuyAssist();
			// test_console();
			// test_dateEntity();
			// test_PortfolioSrv();
			// test_mShareAnalytics();
			// test_SAS();
			// test_SASConsole();
			// test_RepDS();
			// test_ScripHealthSrv();
			// test_generateSSMdt_Xml();
			// test_MassUpdate_ReadyforUpdate();
			// test_MassUpdateTmpl();
			// test_scDBSnapShot();
			// test_Detail_PriceWatchXLS();
			// test_PricingSrvStats();
			// test_PricingSrvInd();
			// test_PriceWatchXLS();
			// test_PriceWatchScrip();
			// test_PriceWatch();
			// test_PricingSrv();
			// test_scripCache();
			// test_splitAwareflds();
			// test_scCrTemplate();
			// test_scUpdTemplate();
			// test_scripissue();
			// testRelQueryScrip();
			// test_scripupdate();
			// test_scripcreate();
			// test_logon_user();
			
			// test_secDL();
			// test_secUpdate();
			
			// testRelQueryFiltercache();
			
			// test_genratePPMdt_Xml();
			
			// test_genrateREMdt_Xml();
			
			// test_generateSSMdt_Xml();
			// test_genrateTEMdt_Xml();
			
			// test_DSMdt_mShareAttr();
			
			// test_genrateMOMdt_Xml();
		}
		
		catch (Throwable Ex)
		{
			if (Ex.getMessage() != null)
			{
				System.out.println(Ex.getMessage());
			}
			
		}
	}
	
	public static void test_LogOnUser(
		) throws Exception, Exception
	{
		// @SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
		
		// RootObject root1 = RootObject.Create_RootObject("OB_Txn_Catg");
		
		// Get User Logon Bean
		
		LogonService UserLogon = (LogonService) context.getBean("userLogon");
		if (UserLogon != null)
		{
			UserLogon.setUsername("SYSADMIN");
			UserLogon.Validate_Logon("iamfine_123");
			UserManager usermanager = (UserManager) context.getBean("userManager");
			if (usermanager != null)
			{
				System.out.println("WElcome - " + usermanager.Get_LoggedUser());
			}
		}
	}
	
	public static void test_Models_JAXB(
				)
	{
		ApplicationContext appCtxt = null;
		try
		{
			appCtxt = test_logon_user();
			if (appCtxt != null)
			{
				
				Models_JAXB modelJaxB = new Models_JAXB(appCtxt.getBean(Framework_Constants.class));
				if (modelJaxB != null)
				{
					// Generate XSD
					modelJaxB.Generate_XSD();
					
					// Generate XML
					modelJaxB.Generate_XML();
					
					// Unmarshal Operation
					modelJaxB.Load_XML_to_Objects();
				}
			}
			
		} catch (Exception e)
		{
			
		}
	}
	
	public static void encrypt_test(
				)
	{
		try
		{
			
			// Encrypt Here
			Map<String, String> Key_Value = EncryptionUtility.Encrypt("Encrypt me");
			if (Key_Value != null)
			{
				Set      keys = Key_Value.keySet();
				Iterator itr  = keys.iterator();
				
				String key;
				String value;
				while (itr.hasNext())
				{
					key   = (String) itr.next();
					value = Key_Value.get(key);
					System.out.println("________________Encryption Starts ________________________");
					System.out.println(" Key : " + key + " - " + "Encrypted Text - " + value);
				}
				
			}
			
			// Decrypt Here
			if (Key_Value != null)
			{
				String decrypted_text = EncryptionUtility.Decrypt(Key_Value);
				System.out.println("________________Decryption Starts________________________");
				System.out.println(decrypted_text);
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_messagebased_Ex(
		)
	{
		ApplicationContext        context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
		EX_DuplicateUserException Ex      = new EX_DuplicateUserException("Sunny");
		
		MessagesFormatter msgformatter = context.getBean("messagesFormatter", MessagesFormatter.class);
		msgformatter.setMessage_snippet(Ex);
		String text = msgformatter.generate_formatted_message();
		System.out.println(text);
	}
	
	public static void test_datetime(
		) throws Exception, Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar   cal        = Calendar.getInstance();
		String     date_time  = (dateFormat.format(cal.getTime()));
		System.out.println(date_time);
		
		String filepath = "C:\\ModelLogs" + "./" + "Sunny" + "_" + date_time + ".log";
		
		FileHandler     fh        = new FileHandler(filepath);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
		
	}
	
	public static ApplicationContext Initialize_Context(
		)
	{
		ApplicationContext context = null;
		
		context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
		
		return context;
	}
	
	public static void test_create_user(
			) throws SQLException, EX_DuplicateUserException, EX_UserCreationException
	{
		ApplicationContext context     = Initialize_Context();
		CreateUserService  userService = context.getBean("createUserService", CreateUserService.class);
		if (userService != null)
		{
			SimpleUser newuser = new SimpleUser("", "");
			userService.Initialize_Creation(newuser);
			userService.CreateUser();
			if (userService.IS_Created() == true)
			{
				System.out.println("User " + userService.getUser().getUserName() + " Created Successfully!");
			}
		}
		
	}
	
	public static ApplicationContext test_logon_user(
			) throws SQLException
	{
		ApplicationContext context      = Initialize_Context();
		LogonService       logonService = context.getBean("logonService", LogonService.class);
		if (logonService != null)
		{
			logonService.setUsername("SYSADMIN");
			
			try
			{
				logonService.Validate_Logon("Iamfine_123");
				if (logonService.IS_Authenticated() == true)
				{
					System.out.println("User Authenticated!! Ready for Logon event!");
					
				}
			} catch (EX_InvalidLogonException | EX_UserLogonException Ex)
			{
				MessagesFormatter msgformatter = context.getBean("messagesFormatter", MessagesFormatter.class);
				msgformatter.setMessage_snippet(Ex);
				String msg = msgformatter.generate_formatted_message();
				System.out.println(msg);
			}
		}
		
		return context;
		
	}
	
	public static String read_MFexception(
	        Exception Ex
			)
	{
		String Message = null;
		
		if (Ex instanceof IPropertyAwareMessage)
		{
			@SuppressWarnings(
			    "resource"
			)
			ApplicationContext context      = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			MessagesFormatter  msgformatter = context.getBean("messagesFormatter", MessagesFormatter.class);
			Message = msgformatter.generate_message_snippet((IPropertyAwareMessage) Ex);
		}
		
		return Message;
	}
	
	public static void testSimpleQuery(
				)
	{
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params.add(new TY_NameValue("Active", true));
				String condn = " Owner = ? AND Active = ?";
				
				ArrayList<OB_Account> accountsDirect = sq.executeQuery(condn, params);
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					
					ArrayList<mypackage.objects.OB_Address> addreses = ob_Account
					        .getRelatedEntities("Account_Address_Rel");
					
					if (addreses != null)
					{
						System.out.println("          Address details for Account - " + ob_Account.getAC_Num());
						
						for (OB_Address ob_Address : addreses)
						{
							int i = 1;
							System.out.println("	  Address ID :  " + i + " City - " + ob_Address.getCity());
							ArrayList<OB_TelDetails> telephones = ob_Address.getRelatedEntities("Address_Tel_Rel");
							System.out.println("		Telephone Details for Address - " + ob_Address.getCity());
							for (OB_TelDetails ob_TelDetails : telephones)
							{
								System.out.println("			" + ob_TelDetails.getTelNumber());
							}
							i++;
						}
						
					}
					
				}
				ArrayList<TY_NameValue> params2 = new ArrayList<TY_NameValue>();
				
				params2.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params2.add(new TY_NameValue("Active", true));
				String                condn2          = " Owner = ? AND Active = ?";
				ArrayList<OB_Account> accountsDirect2 = sq.executeQuery(condn2, params2);
				for (OB_Account ob_Account : accountsDirect2)
				{
					ArrayList<EntityMetadata> mdt_coll = ob_Account.getEntityManager().getMetadataColl();
					mdt_coll.size();
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
				}
				
			}
			
			IQueryService sq2 = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq2 != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params.add(new TY_NameValue("Active", true));
				String                condn2          = " Owner = ? AND Active = ?";
				ArrayList<OB_Account> accountsDirect2 = sq2.executeQuery(condn2, params);
				for (OB_Account ob_Account : accountsDirect2)
				{
					ArrayList<EntityMetadata> mdt_coll = ob_Account.getEntityManager().getMetadataColl();
					mdt_coll.size();
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
				}
			}
			/*
			 * OB_Account Account1 = ModelObjectFactory.createObjectbyName("OB_Account"); if (Account1 != null) {
			 * Account1.setAC_Num("4"); Account1.setAC_Type(Account_Type.Savings); Account1.setActive(true);
			 * Account1.setBank_Name("TESTBANK"); Account1.setCredit_Book(Book_Type.Home_Kalka);
			 * Account1.setCurrent_Balance(50000.98); Account1.setCust_ID("11111111");
			 * Account1.setDebit_Book(Book_Type.Expense); Account1.setIFSC_Code("TEST2344"); Account1.setOwner(
			 * "Sunny Bhardwaj"); Account1.setNominee("Mahie"); Account1.setNotes("Test Dummy Account2"); }
			 * Account1.Save();
			 */
			
			IQueryService sq3 = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq3 != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params.add(new TY_NameValue("Active", true));
				String                condn2          = " Owner = ? AND Active = ?";
				ArrayList<OB_Account> accountsDirect2 = sq3.executeQuery(condn2, params);
				for (OB_Account ob_Account : accountsDirect2)
				{
					ArrayList<EntityMetadata> mdt_coll = ob_Account.getEntityManager().getMetadataColl();
					mdt_coll.size();
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
				}
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_LogOnGuestUser(
			) throws Exception, Exception
	{
		ApplicationContext context      = Initialize_Context();
		LogonService       logonService = context.getBean("logonService", LogonService.class);
		if (logonService != null)
		{
			logonService.setUsername("SYSGUEST");
			
			try
			{
				logonService.Validate_Logon("welcome123");
				if (logonService.IS_Authenticated() == true)
				{
					System.out.println("User Authenticated!! Ready for Logon event!");
					
				}
			} catch (EX_InvalidLogonException | EX_UserLogonException Ex)
			{
				MessagesFormatter msgformatter = context.getBean("messagesFormatter", MessagesFormatter.class);
				msgformatter.setMessage_snippet(Ex);
				String msg = msgformatter.generate_formatted_message();
				System.out.println(msg);
			}
		}
	}
	
	public static void testversion2(
				)
	{
		try
		{
			test_logon_user();
			
			IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Positions_Header");
			if (qs != null)
			{
				ArrayList<OB_Positions_Header> positionsH = qs.executeQuery();
			}
			
			OB_Account Account1 = ModelObjectFactory.createObjectbyName("OB_Account");
			if (Account1 != null)
			{
				Account1.setAC_Num("User AC1");
				Account1.setAC_Type(Account_Type.Savings);
				Account1.setActive(true);
				Account1.setBank_Name("TESTBANK");
				Account1.setCredit_Book(Book_Type.Home_Kalka);
				Account1.setCurrent_Balance(80000.98);
				Account1.setCust_ID("76766686");
				Account1.setDebit_Book(Book_Type.Expense);
				Account1.setIFSC_Code("TEST2344");
				Account1.setOwner("Sunny Bhardwaj");
				Account1.setNominee("Mahie");
				Account1.setNotes("Big Key account");
			}
			
			OB_Address Address_Ac1 = (OB_Address) Account1.Create_RelatedEntity("Account_Address_Rel");
			if (Address_Ac1 != null)
			{
				Address_Ac1.setAC_Num(Account1.getAC_Num());
				// shouldbe handled by framework
				Address_Ac1.setCity("Ghaziabad");
				Address_Ac1.setCountry("INDIA");
				Address_Ac1.setNotes("Address 1");
				Address_Ac1.setPostal_Code("200100");
				Address_Ac1.setState("Uttar Pradesh");
				Address_Ac1.setStreet1("Mathura");
				
				OB_TelDetails Tel1_Ac1 = (OB_TelDetails) Address_Ac1.Create_RelatedEntity("Address_Tel_Rel");
				if (Tel1_Ac1 != null)
				{
					Tel1_Ac1.setTelNumber("999999945");
					// Address_ID to be set implicitly by system while
					// saving
				}
				OB_TelDetails Tel1_Ac2 = (OB_TelDetails) Address_Ac1.Create_RelatedEntity("Address_Tel_Rel");
				if (Tel1_Ac2 != null)
				{
					Tel1_Ac2.setTelNumber("0120676693");
					// Address_ID to be set implicitly by system while
					// saving
				}
			}
			
			OB_Account Account2 = ModelObjectFactory.createObjectbyClass(OB_Account.class);
			if (Account2 != null)
			{
				
				Account2.setAC_Num("USER AC 2");
				Account2.setAC_Type(Account_Type.Savings);
				Account2.setActive(true);
				Account2.setBank_Name("TESTING");
				Account2.setCredit_Book(Book_Type.Home_Kalka);
				Account2.setCurrent_Balance(50000.98);
				Account2.setCust_ID("76766686");
				Account2.setDebit_Book(Book_Type.Expense);
				Account2.setIFSC_Code("TEST2344");
				Account2.setOwner("Sunny Bhardwaj");
				Account2.setNominee("Mahie");
				Account2.setNotes("Test big key account2");
			}
			
			OB_Address addr2 = (OB_Address) Account2.Create_RelatedEntity("Account_Address_Rel");
			if (addr2 != null)
			{
				addr2.setCity("Kalka");
			}
			
			OB_TelDetails tel_addr2 = (OB_TelDetails) addr2.Create_RelatedEntity("Address_Tel_Rel");
			if (tel_addr2 != null)
			{
				tel_addr2.setTelNumber("01724456789");
			}
			
			Account1.Save();
			Account2.Save();
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testversion2guest(
				)
	{
		try
		{
			test_LogOnGuestUser();
			
			OB_Account Account1 = ModelObjectFactory.createObjectbyName("OB_Account");
			if (Account1 != null)
			{
				Account1.setAC_Num("346575334");
				Account1.setAC_Type(Account_Type.Savings);
				Account1.setActive(true);
				Account1.setBank_Name("TESTBANK");
				Account1.setCredit_Book(Book_Type.Home_Kalka);
				Account1.setCurrent_Balance(50000.98);
				Account1.setCust_ID("76766686");
				Account1.setDebit_Book(Book_Type.Expense);
				Account1.setIFSC_Code("TEST2344");
				Account1.setOwner("Sunny Bhardwaj");
				Account1.setNominee("Mahie");
				Account1.setNotes("Test Guest Account2");
			}
			
			OB_Address Address_Ac1 = (OB_Address) Account1.Create_RelatedEntity("Account_Address_Rel");
			if (Address_Ac1 != null)
			{
				// Address_Ac1.setAC_Num(Account1.getAC_Num());
				// shouldbe handled by framework
				Address_Ac1.setCity("Ghaziabad");
				Address_Ac1.setCountry("INDIA");
				Address_Ac1.setNotes("Address 1");
				Address_Ac1.setPostal_Code("200100");
				Address_Ac1.setState("Uttar Pradesh");
				Address_Ac1.setStreet1("Mathura");
				
				OB_TelDetails Tel1_Ac1 = (OB_TelDetails) Address_Ac1.Create_RelatedEntity("Address_Tel_Rel");
				if (Tel1_Ac1 != null)
				{
					Tel1_Ac1.setTelNumber("454545555");
					// Address_ID to be set implicitly by system while
					// saving
				}
				OB_TelDetails Tel1_Ac2 = (OB_TelDetails) Address_Ac1.Create_RelatedEntity("Address_Tel_Rel");
				if (Tel1_Ac2 != null)
				{
					Tel1_Ac2.setTelNumber("015655533");
					// Address_ID to be set implicitly by system while
					// saving
				}
			}
			
			OB_Account Account2 = ModelObjectFactory.createObjectbyClass(OB_Account.class);
			if (Account2 != null)
			{
				
				Account2.setAC_Num("2323232323");
				Account2.setAC_Type(Account_Type.Savings);
				Account2.setActive(true);
				Account2.setBank_Name("TESTING");
				Account2.setCredit_Book(Book_Type.Home_Kalka);
				Account2.setCurrent_Balance(50000.98);
				Account2.setCust_ID("76766686");
				Account2.setDebit_Book(Book_Type.Expense);
				Account2.setIFSC_Code("TEST2344");
				Account2.setOwner("Sunny Bhardwaj");
				Account2.setNominee("Mahie");
				Account2.setNotes("Test another Guest Account");
			}
			
			OB_Address addr2 = (OB_Address) Account2.Create_RelatedEntity("Account_Address_Rel");
			if (addr2 != null)
			{
				addr2.setCity("Kalka");
			}
			
			OB_TelDetails tel_addr2 = (OB_TelDetails) addr2.Create_RelatedEntity("Address_Tel_Rel");
			if (tel_addr2 != null)
			{
				tel_addr2.setTelNumber("01733456789");
			}
			
			Account1.Save();
			Account2.Save();
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testRelQueryFilter(
				)
	{
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params.add(new TY_NameValue("Active", true));
				String condn = " Owner = ? AND Active = ?";
				
				ArrayList<OB_Account> accountsDirect = sq.executeQuery(condn, params);
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					
					TY_Filter filter = new TY_Filter();
					filter.setWhereCondn(" City = ? OR City = ? OR City = ?");
					ArrayList<TY_NameValue> params2 = new ArrayList<TY_NameValue>();
					params2.add(new TY_NameValue("City", "Ghaziabad"));
					params2.add(new TY_NameValue("City", "Kalka"));
					params2.add(new TY_NameValue("City", "Gurgaon"));
					filter.setFilterParams(params2);
					
					ArrayList<OB_Address> addreses = ob_Account.getRelatedEntitieswithFilter("Account_Address_Rel",
					        filter);
					
					if (addreses != null)
					{
						System.out.println("          Address details for Account - " + ob_Account.getAC_Num());
						
						for (OB_Address ob_Address : addreses)
						{
							int i = 1;
							System.out.println("	  Address ID :  " + i + " City - " + ob_Address.getCity());
							
							TY_Filter filter2 = new TY_Filter();
							filter2.setWhereCondn("TelNumber LIKE ? ");
							ArrayList<TY_NameValue> params3 = new ArrayList<TY_NameValue>();
							params3.add(new TY_NameValue("TelNumber", "9%"));
							filter2.setFilterParams(params3);
							
							ArrayList<OB_TelDetails> telephones = ob_Address
							        .getRelatedEntitieswithFilter("Address_Tel_Rel", filter2);
							System.out.println("		Telephone Details for Address - " + ob_Address.getCity());
							for (OB_TelDetails ob_TelDetails : telephones)
							{
								System.out.println("			" + ob_TelDetails.getTelNumber());
							}
							i++;
						}
						
					}
					
				}
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testRelQueryFiltercache(
				)
	{
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params.add(new TY_NameValue("Active", true));
				String condn = " Owner = ? AND Active = ?";
				
				ArrayList<OB_Account> accountsDirect = sq.executeQuery(condn, params);
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					
					TY_Filter filter = new TY_Filter();
					filter.setWhereCondn(" City = ? OR City = ? OR City = ?");
					ArrayList<TY_NameValue> params2 = new ArrayList<TY_NameValue>();
					params2.add(new TY_NameValue("City", "Ghaziabad"));
					params2.add(new TY_NameValue("City", "Kalka"));
					params2.add(new TY_NameValue("City", "Gurgaon"));
					filter.setFilterParams(params2);
					
					ArrayList<OB_Address> addreses = ob_Account.getRelatedEntitieswithFilter("Account_Address_Rel",
					        filter);
					
					if (addreses != null)
					{
						System.out.println("          Address details for Account - " + ob_Account.getAC_Num());
						
						for (OB_Address ob_Address : addreses)
						{
							int i = 1;
							System.out.println("	  Address ID :  " + i + " City - " + ob_Address.getCity());
							
							TY_Filter filter2 = new TY_Filter();
							filter2.setWhereCondn("TelNumber LIKE ? ");
							ArrayList<TY_NameValue> params3 = new ArrayList<TY_NameValue>();
							params3.add(new TY_NameValue("TelNumber", "9%"));
							filter2.setFilterParams(params3);
							
							ArrayList<OB_TelDetails> telephones = ob_Address
							        .getRelatedEntitieswithFilter("Address_Tel_Rel", filter2);
							System.out.println("		Telephone Details for Address - " + ob_Address.getCity());
							for (OB_TelDetails ob_TelDetails : telephones)
							{
								System.out.println("			" + ob_TelDetails.getTelNumber());
							}
							i++;
						}
						
					}
					
				}
			}
			
			IQueryService sq2 = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Owner", "Sunny Bhardwaj"));
				params.add(new TY_NameValue("Active", true));
				String condn = " Owner = ? AND Active = ?";
				
				ArrayList<OB_Account> accountsDirect = sq2.executeQuery(condn, params);
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					
					TY_Filter filter = new TY_Filter();
					filter.setWhereCondn(" City = ? OR City = ? OR City = ?");
					ArrayList<TY_NameValue> params2 = new ArrayList<TY_NameValue>();
					params2.add(new TY_NameValue("City", "Ghaziabad"));
					params2.add(new TY_NameValue("City", "Kalka"));
					params2.add(new TY_NameValue("City", "Gurgaon"));
					filter.setFilterParams(params2);
					
					ArrayList<OB_Address> addreses = ob_Account.getRelatedEntitieswithFilter("Account_Address_Rel",
					        filter);
					
					if (addreses != null)
					{
						System.out.println("          Address details for Account - " + ob_Account.getAC_Num());
						
						for (OB_Address ob_Address : addreses)
						{
							int i = 1;
							System.out.println("	  Address ID :  " + i + " City - " + ob_Address.getCity());
							
							TY_Filter filter2 = new TY_Filter();
							filter2.setWhereCondn("TelNumber LIKE ? ");
							ArrayList<TY_NameValue> params3 = new ArrayList<TY_NameValue>();
							params3.add(new TY_NameValue("TelNumber", "9%"));
							filter2.setFilterParams(params3);
							
							ArrayList<OB_TelDetails> telephones = ob_Address
							        .getRelatedEntitieswithFilter("Address_Tel_Rel", filter2);
							System.out.println("		Telephone Details for Address - " + ob_Address.getCity());
							for (OB_TelDetails ob_TelDetails : telephones)
							{
								System.out.println("			" + ob_TelDetails.getTelNumber());
							}
							i++;
						}
						
					}
					
				}
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testRelQueryScrip(
				)
	{
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Scrip_General");
			if (sq != null)
			{
				
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("scCode", "MINDAIND"));
				
				String condn = " scCode = ?";
				
				ArrayList<OB_Scrip_General> scGeneral = sq.executeQuery(condn, params);
				for (OB_Scrip_General ob_Scrip : scGeneral)
				{
					
					System.out.println(ob_Scrip.getscCode() + "-" + ob_Scrip.getmCap());
					
					TY_Filter filter = new TY_Filter();
					filter.setWhereCondn(" year = ? OR year = ? OR year = ?");
					ArrayList<TY_NameValue> params2 = new ArrayList<TY_NameValue>();
					params2.add(new TY_NameValue("year", 2013));
					params2.add(new TY_NameValue("year", 2014));
					params2.add(new TY_NameValue("year", 2015));
					filter.setFilterParams(params2);
					
					ArrayList<OB_Scrip_BalSheet> balsheetEnt = ob_Scrip
					        .getRelatedEntitieswithFilter("OB_Scrip_BalSheet_Rel", filter);
					
					if (balsheetEnt != null)
					{
						System.out.println("          BalanceSheet Data for Scrip - " + ob_Scrip.getscCode());
						
						for (OB_Scrip_BalSheet balsheet : balsheetEnt)
						{
							int i = 1;
							System.out.println("	  Year :  " + balsheet.getyear() + " netWorth - "
							        + (balsheet.getnetWorth()) + " Cr.");
							
						}
					}
					
					TY_Filter filter2 = new TY_Filter();
					filter2.setWhereCondn(
					        "(Year = ? AND Quarter = ?) OR (Year = ? AND Quarter = ?) OR (Year = ? AND Quarter = ?) OR (Year = ? AND Quarter = ?)");
					ArrayList<TY_NameValue> params3 = new ArrayList<TY_NameValue>();
					params3.add(new TY_NameValue("Year", 2013));
					params3.add(new TY_NameValue("Quarter", 1));
					params3.add(new TY_NameValue("Year", 2014));
					params3.add(new TY_NameValue("Quarter", 1));
					params3.add(new TY_NameValue("Year", 2015));
					params3.add(new TY_NameValue("Quarter", 1));
					params3.add(new TY_NameValue("Year", 2017));
					params3.add(new TY_NameValue("Quarter", 1));
					filter2.setFilterParams(params3);
					
					ArrayList<OB_Scrip_QuartersData> quarters = ob_Scrip
					        .getRelatedEntitieswithFilter("OB_Scrip_QuartersData_Rel", filter2);
					System.out.println("		Quarters Data for Scrip - " + ob_Scrip.getscCode());
					for (OB_Scrip_QuartersData quarter : quarters)
					{
						System.out.println("			" + quarter.getYear() + " - " + quarter.getQuarter()
						        + " Nett. profit - Rs." + quarter.getNettProfit() + " Cr.");
					}
					
				}
				
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testAllRoot(
				)
	{
		
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				
				ArrayList<OB_Account> accountsDirect = sq.executeQuery();
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					
				}
			}
			
			IQueryService sq2 = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq2 != null)
			{
				
				ArrayList<OB_Account> accountsDirect = sq2.executeQuery();
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static void populate2RootCache(
				)
	{
		
	}
	
	public static void testlock(
				)
	{
		
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				
				ArrayList<OB_Account> accountsDirect = sq.executeQuery();
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					ArrayList<OB_Address> addreses = ob_Account.getRelatedEntities("Account_Address_Rel");
					
					if (addreses != null)
					{
						System.out.println("          Address details for Account - " + ob_Account.getAC_Num());
						
						for (OB_Address ob_Address : addreses)
						{
							int i = 1;
							System.out.println("	  Address ID :  " + i + " City - " + ob_Address.getCity());
							ArrayList<OB_TelDetails> telephones = ob_Address.getRelatedEntities("Address_Tel_Rel");
							System.out.println("		Telephone Details for Address - " + ob_Address.getCity());
							for (OB_TelDetails ob_TelDetails : telephones)
							{
								System.out.println("			" + ob_TelDetails.getTelNumber());
							}
							i++;
						}
						
					}
					
				}
				// Only lock the first account and all its dependent entities
				if (accountsDirect.get(0).lock())
				{
					
					// Test begin
					ArrayList<EntityMetadata> root_entMdtColl = accountsDirect.get(0).getEntityManager()
					        .getMetadataColl();
					if (root_entMdtColl != null)
					{
						System.out.println("Total Entities -" + root_entMdtColl.size());
						
						System.out.println(
						        "Locked Entities -" + root_entMdtColl.stream()
						                .filter(x -> x.getEntityMode() == entityMode.LOCKED).count());
					}
					
					accountsDirect.get(0).switchtoChangeMode();
					if (root_entMdtColl != null)
					{
						System.out.println("changeable Entities -"
						        + root_entMdtColl.stream().filter(x -> x.getEntityMode() == entityMode.CHANGE).count());
					}
					
					// Test end
				}
				
			}
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static void test_changemode(
				)
	{
		try
		{
			test_logon_user();
			
			/**
			 * Change All the Accounts where City is Kalka such that their descriptions become Kalka Accounts, increase
			 * current balance by 10% and their cities change to kalka
			 */
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				// Get All accounts
				ArrayList<OB_Account> Accounts = sq.executeQuery();
				if (Accounts != null)
				{
					if (Accounts.size() > 0)
					{
						// Get the addresses if City = Kalka
						String                  whereCondn = "City =  ? OR City =  ?";
						ArrayList<TY_NameValue> params     = new ArrayList<TY_NameValue>();
						TY_NameValue            param_city = new TY_NameValue("City", "kalka");
						params.add(param_city);
						TY_NameValue param_city2 = new TY_NameValue("City", "Kalka");
						params.add(param_city2);
						
						TY_Filter filter = new TY_Filter(whereCondn, params);
						
						for (OB_Account account : Accounts)
						{
							ArrayList<OB_Address> addresses = account
							        .getRelatedEntitieswithFilter("Account_Address_Rel", filter);
							if (addresses.size() > 0)
							{
								account.lock();
								account.switchtoChangeMode();
								double cur_bal = account.getCurrent_Balance();
								cur_bal = cur_bal * 1.1;
								account.setNotes("Kalka Accounts");
								account.setCurrent_Balance(cur_bal);
								
								for (OB_Address address : addresses)
								{
									address.switchtoChangeMode();
									address.setCity("kalka changed");
								}
								
								account.Save();
							}
						}
						
						// Test begin
						ArrayList<EntityMetadata> root_entMdtColl = Accounts.get(0).getEntityManager()
						        .getMetadataColl();
						if (root_entMdtColl != null)
						{
							System.out.println("Total Entities -" + root_entMdtColl.size());
							
							System.out.println("Locked Entities -" + root_entMdtColl.stream()
							        .filter(x -> x.getEntityMode() == entityMode.LOCKED).count());
						}
						
						if (root_entMdtColl != null)
						{
							System.out.println("changeable Entities -" + root_entMdtColl.stream()
							        .filter(x -> x.getEntityMode() == entityMode.CHANGE).count());
						}
						// Test end
						
					}
				}
			}
			
		}
		
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
	}
	
	public static void testdelete(
				)
	{
		
		try
		{
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				String                  whereCondition = "AC_Num = ?";
				TY_NameValue            param          = new TY_NameValue("AC_Num", "2222222222");
				ArrayList<TY_NameValue> params         = new ArrayList<TY_NameValue>();
				params.add(param);
				sq.executeQuery(whereCondition, params);
				ArrayList<OB_Account> accountsDirect = sq.executeQuery();
				for (OB_Account ob_Account : accountsDirect)
				{
					
					System.out.println(ob_Account.getAC_Num() + "-" + ob_Account.getCurrent_Balance());
					ArrayList<OB_Address> addreses = ob_Account.getRelatedEntities("Account_Address_Rel");
					
					if (addreses != null)
					{
						System.out.println("          Address details for Account - " + ob_Account.getAC_Num());
						
						for (OB_Address ob_Address : addreses)
						{
							int i = 1;
							System.out.println("	  Address ID :  " + i + " City - " + ob_Address.getCity());
							ArrayList<OB_TelDetails> telephones = ob_Address.getRelatedEntities("Address_Tel_Rel");
							System.out.println("		Telephone Details for Address - " + ob_Address.getCity());
							for (OB_TelDetails ob_TelDetails : telephones)
							{
								System.out.println("			" + ob_TelDetails.getTelNumber());
							}
							i++;
						}
						
					}
					
				}
				// Only lock the first account and all its dependent entities
				if (accountsDirect.get(0).lock())
				{
					
					// Test begin
					ArrayList<EntityMetadata> root_entMdtColl = accountsDirect.get(0).getEntityManager()
					        .getMetadataColl();
					if (root_entMdtColl != null)
					{
						System.out.println("Total Entities -" + root_entMdtColl.size());
						
						System.out.println(
						        "Locked Entities -" + root_entMdtColl.stream()
						                .filter(x -> x.getEntityMode() == entityMode.LOCKED).count());
					}
					
					accountsDirect.get(0).delete();
					accountsDirect.get(0).Save();
					if (root_entMdtColl != null)
					{
						System.out.println("Total Entities -" + root_entMdtColl.size());
						System.out.println("changeable Entities -"
						        + root_entMdtColl.stream().filter(x -> x.getEntityMode() == entityMode.CHANGE).count());
					}
					
					// Test end
				}
				
			}
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static void test_deletedependant(
				)
	{
		try
		{
			test_logon_user();
			
			/**
			 * Change All the Accounts where City is Kalka such that their descriptions become Kalka Accounts, increase
			 * current balance by 10% and their cities change to kalka
			 */
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Account");
			if (sq != null)
			{
				// Get All accounts
				ArrayList<OB_Account> Accounts = sq.executeQuery();
				if (Accounts != null)
				{
					if (Accounts.size() > 0)
					{
						// Get the addresses if City = Kalka
						String                  whereCondn = "Street1 LIKE  ? ";
						ArrayList<TY_NameValue> params     = new ArrayList<TY_NameValue>();
						TY_NameValue            param_city = new TY_NameValue("Street1", "Test Street1");
						params.add(param_city);
						
						TY_Filter filter = new TY_Filter(whereCondn, params);
						
						for (OB_Account account : Accounts)
						{
							ArrayList<OB_Address> addresses = account
							        .getRelatedEntitieswithFilter("Account_Address_Rel", filter);
							if (addresses.size() > 0)
							{
								account.lock();
								for (OB_Address address : addresses)
								{
									address.delete();
								}
								
								account.Save();
							}
							if (account.lock())
							{
								System.out.println("Locked again after save");
							}
						}
						
						// Test begin
						ArrayList<EntityMetadata> root_entMdtColl = Accounts.get(0).getEntityManager()
						        .getMetadataColl();
						if (root_entMdtColl != null)
						{
							System.out.println("Total Entities -" + root_entMdtColl.size());
							
							System.out.println("Locked Entities -" + root_entMdtColl.stream()
							        .filter(x -> x.getEntityMode() == entityMode.LOCKED).count());
						}
						
						if (root_entMdtColl != null)
						{
							System.out.println("changeable Entities -" + root_entMdtColl.stream()
							        .filter(x -> x.getEntityMode() == entityMode.CHANGE).count());
						}
						// Test end
						
					}
				}
			}
			
		}
		
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
	}
	
	public static void test_StdDeviation(
				)
	        throws FileNotFoundException, EX_StdDeviation, IOException
	{
		try
		{
			test_logon_user();
			IStDev StdDeviationService = StatisticsManager.getStDeviationService(null);
			if (StdDeviationService != null)
			{
				// 2015
				StDevHead devHead_2015 = new StDevHead("2015");
				
				// 2015-Items
				StDevItem devitem_2015_1  = new StDevItem("Jan-2015", 169);
				StDevItem devitem_2015_2  = new StDevItem("Feb-2015", 146);
				StDevItem devitem_2015_3  = new StDevItem("Mar-2015", 103);
				StDevItem devitem_2015_4  = new StDevItem("Apr-2015", 102);
				StDevItem devitem_2015_5  = new StDevItem("May-2015", 112);
				StDevItem devitem_2015_6  = new StDevItem("June-2015", 105);
				StDevItem devitem_2015_7  = new StDevItem("July-2015", 102);
				StDevItem devitem_2015_8  = new StDevItem("Aug-2015", 106);
				StDevItem devitem_2015_9  = new StDevItem("Sep-2015", 110);
				StDevItem devitem_2015_10 = new StDevItem("Oct-2015", 112);
				StDevItem devitem_2015_11 = new StDevItem("Nov-2015", 114);
				StDevItem devitem_2015_12 = new StDevItem("Dec-2015", 116);
				
				// Add 2015 items
				devHead_2015.addStdevItem(devitem_2015_1);
				devHead_2015.addStdevItem(devitem_2015_2);
				devHead_2015.addStdevItem(devitem_2015_3);
				devHead_2015.addStdevItem(devitem_2015_4);
				devHead_2015.addStdevItem(devitem_2015_5);
				devHead_2015.addStdevItem(devitem_2015_6);
				devHead_2015.addStdevItem(devitem_2015_7);
				devHead_2015.addStdevItem(devitem_2015_8);
				devHead_2015.addStdevItem(devitem_2015_9);
				devHead_2015.addStdevItem(devitem_2015_10);
				devHead_2015.addStdevItem(devitem_2015_11);
				devHead_2015.addStdevItem(devitem_2015_12);
				
				// 2014
				StDevHead devHead_2014 = new StDevHead("2014");
				
				// 2014-Items
				StDevItem devitem_2014_1 = new StDevItem("Jan-2014", 290);
				StDevItem devitem_2014_2 = new StDevItem("Feb-2014", 239);
				StDevItem devitem_2014_3 = new StDevItem("Mar-2014", 236);
				StDevItem devitem_2014_4 = new StDevItem("Apr-2014", 233);
				StDevItem devitem_2014_5 = new StDevItem("May-2014", 225);
				StDevItem devitem_2014_6 = new StDevItem("June-2014", 202);
				StDevItem devitem_2014_7 = new StDevItem("July-2014", 186);
				StDevItem devitem_2014_8 = new StDevItem("Aug-2014", 166);
				StDevItem devitem_2014_9 = new StDevItem("Sep-2014", 169);
				
				// Add 2014 items
				devHead_2014.addStdevItem(devitem_2014_1);
				devHead_2014.addStdevItem(devitem_2014_2);
				devHead_2014.addStdevItem(devitem_2014_3);
				devHead_2014.addStdevItem(devitem_2014_4);
				devHead_2014.addStdevItem(devitem_2014_5);
				devHead_2014.addStdevItem(devitem_2014_6);
				devHead_2014.addStdevItem(devitem_2014_7);
				devHead_2014.addStdevItem(devitem_2014_8);
				devHead_2014.addStdevItem(devitem_2014_9);
				
				// Process Std. Deviation
				StdDeviationService.getDeviation().addStdevHeader(devHead_2015);
				StdDeviationService.getDeviation().addStdevHeader(devHead_2014);
				StdDeviationService.process();
				
				// Get the results
				ArrayList<StDevResult> dev_summ = StdDeviationService.getResults();
				for (StDevResult result : dev_summ)
				{
					System.out.println(result.getTitle() + "        " + result.getRsd() + "%" + "         "
					        + result.getSpread_high());
				}
				
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test_StdDeviationXlsx(
			) throws FileNotFoundException, EX_StdDeviation, IOException
	{
		try
		{
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			test_logon_user();
			
			File myFile = new File("C://Minda.xlsx");
			
			FileInputStream fis = new FileInputStream(myFile);
			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
			
			// IStDev StdDeviationService =
			// StatisticsManager.getStDeviationService("C://Minda.xlsx");
			IStDev StdDeviationService = StatisticsManager.getStDeviationServiceforWBSheet(myWorkBook, "Prices");
			if (StdDeviationService != null)
			{
				
				// StdDeviationService.initializefromXlsx("C://Minda.xlsx");
				StdDeviationService.process();
				
				if (StdDeviationService.getDeviation() != null)
				{
					for (StDevHead devHead : StdDeviationService.getDeviation().getDeviationHeads())
					{
						System.out.println(devHead.getTitle());
						System.out.println(
						        "Mean" + "\t" + "Standard Deviation " + "\t" + "Deviation Avg. " + "\t" + "RSD " + "\t"
						                + "Spread high ");
						
						System.out.println(
						        df.format(devHead.getMean()) + "\t" + df.format(devHead.getStandard_deviation()) + "\t"
						                + df.format(devHead.getDeviation_average()) + "\t" + df.format(devHead.getRsd())
						                + "\t" + df.format(devHead.getSpread_high()));
						
						System.out.println("Title" + "\t" + "Value1" + "\t" + "Value 2" + "\t" + "Average" + "\t"
						        + "Deviation" + "\t" + "Deviation Sq.");
						for (StDevItem devitem : devHead.getItems())
						{
							
							System.out.println(devitem.getItem_title() + "\t" + df.format(devitem.getVal1()) + "\t"
							        + df.format(devitem.getVal2()) + "\t" + df.format(devitem.getAverage()) + "\t"
							        + df.format(devitem.getDeviation()) + "\t" + df.format(devitem.getDeviation_sq()));
						}
					}
				}
				
				// Get the results
				ArrayList<StDevResult> dev_summ = StdDeviationService.getResults();
				for (StDevResult result : dev_summ)
				{
					System.out.println(result.getTitle() + "        " + df.format(result.getRsd()) + "%" + "         "
					        + df.format(result.getSpread_high()));
				}
				
			}
			
		}
		
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_genrateWBMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXB          xmlpathBean = context.getBean(PathsJAXB.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_WB_xml_GEN() != null && xmlpathBean.getJaxPath_WB_xsd() != null)
				{
					Workbook_JAXB wbJAXB = new Workbook_JAXB(xmlpathBean);
					wbJAXB.Generate_XML();
					wbJAXB.Generate_XSD();
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_genratePPMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXBPP        xmlpathBean = context.getBean(PathsJAXBPP.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_PP_xml_GEN() != null && xmlpathBean.getJaxPath_PP_xsd() != null)
				{
					PriceProjectionConfig_JAXB ppJAXB = new PriceProjectionConfig_JAXB(xmlpathBean);
					ppJAXB.Generate_XML();
					ppJAXB.Generate_XSD();
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_genrateMOMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXBMO        xmlpathBean = context.getBean(PathsJAXBMO.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_MO_xml_GEN() != null && xmlpathBean.getJaxPath_MO_xsd() != null)
				{
					monthsConfig_JAXB moJAXB = new monthsConfig_JAXB(xmlpathBean);
					
					// moJAXB.Generate_XML();
					// moJAXB.Generate_XSD();
					
					ArrayList<monthsConfig> months = moJAXB.Load_XML_to_Objects();
					if (months != null)
					{
						
					}
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_genrateSMMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXBSM        xmlpathBean = context.getBean(PathsJAXBSM.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_SM_xml_GEN() != null && xmlpathBean.getJaxPath_SM_xsd() != null)
				{
					SCWBMetadata_JAXB wbJAXB = new SCWBMetadata_JAXB(xmlpathBean);
					
					wbJAXB.Generate_XML();
					wbJAXB.Generate_XSD();
					
				}
			}
			
			ISCWBMetadataSrv wmMdtSrv = context.getBean(ISCWBMetadataSrv.class);
			if (wmMdtSrv != null)
			{
				SCSheetMetadata shtMDt = wmMdtSrv.getMetadataforSheet("Analysis");
				if (shtMDt != null)
				{
					System.out.println(shtMDt.getSheetName() + "-" + shtMDt.getHeadScanConfig().getRowScanType());
				}
			}
			
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_genrateREMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXBRE        xmlpathBean = context.getBean(PathsJAXBRE.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_RE_xml_GEN() != null && xmlpathBean.getJaxPath_RE_xsd() != null)
				{
					RepXLS_Srv_MappingConfig_JAXB reJAXB = new RepXLS_Srv_MappingConfig_JAXB(xmlpathBean);
					reJAXB.Generate_XML();
					reJAXB.Generate_XSD();
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_genrateTEMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXBTE        xmlpathBean = context.getBean(PathsJAXBTE.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_TE_xml_GEN() != null && xmlpathBean.getJaxPath_TE_xsd() != null)
				{
					Obj_ITaxableSrv_MappingConfig_JAXB teJAXB = new Obj_ITaxableSrv_MappingConfig_JAXB(xmlpathBean);
					teJAXB.Generate_XML();
					teJAXB.Generate_XSD();
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_generateSSMdt_Xml(
				)
	{
		try
		{
			// test_logon_user();
			
			ApplicationContext context     = Initialize_Context();
			PathsJAXBSS        xmlpathBean = context.getBean(PathsJAXBSS.class);
			if (xmlpathBean != null)
			{
				if (xmlpathBean.getJaxPath_SS_xml_GEN() != null && xmlpathBean.getJaxPath_SS_xsd() != null)
				{
					SSConfig_JAXB ssJAXB = new SSConfig_JAXB(xmlpathBean);
					ssJAXB.Generate_XML();
					ssJAXB.Generate_XSD();
				}
			}
		}
		
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public static void test_scripcreate(
			)
	{
		try
		{
			
			test_logon_user();
			
			IScripDataService scCrSrv = ScripDataManager.getScripDataService();
			if (scCrSrv != null)
			{
				scCrSrv.createScripfromXls_WB("C://Sunny Drives//SCE Files//Upload//SterlingTools.xlsx");
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_scripupdate(
			)
	{
		try
		{
			
			test_logon_user();
			
			IScripDataService scUpdSrv = ScripDataManager.getScripDataService();
			if (scUpdSrv != null)
			{
				scUpdSrv.updateScripfromXls_WB("C://Sunny Drives//SCE Files//MassUpdate//UpdateReady//AksharChem.xlsx");
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_secDL(
				)
	{
		try
		{
			
			test_logon_user();
			String             filepath = "C://Sunny Drives/SCE Files//Download//";
			ApplicationContext context  = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				SectorsService secSrv = context.getBean(SectorsService.class);
				if (secSrv != null)
				{
					secSrv.downloadSectors(filepath);
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_secUpdate(
				)
	{
		try
		{
			
			test_logon_user();
			String             filepath = "C://Sunny Drives//SCE Files//Download//SectorsList.xlsx";
			ApplicationContext context  = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				SectorsService secSrv = context.getBean(SectorsService.class);
				if (secSrv != null)
				{
					secSrv.createUpdateSectors(filepath);
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_scripissue(
				)
	{
		try
		{
			
			test_logon_user();
			
			IQueryService sq = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Scrip_General");
			if (sq != null)
			{
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("scCode", "MINDAIND"));
				ArrayList<OB_Scrip_General> scrips = sq.executeQuery("scCode = ?", params);
				if (scrips != null)
				{
					OB_Scrip_General scRoot = scrips.get(0);
					if (scRoot != null)
					{
						scRoot.lock();
						scRoot.switchtoChangeMode();
						scRoot.setmCap(3500.56);
						
						OB_Scrip_QuartersData q1 = (OB_Scrip_QuartersData) scRoot
						        .Create_RelatedEntity("OB_Scrip_QuartersData_Rel");
						q1.setNettProfit(500);
						q1.setYear(2013);
						q1.setQuarter(1);
						q1.setNettSales(1000);
						
					}
					scRoot.Save();
				}
				
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_scCrTemplate(
			)
	{
		try
		{
			
			test_logon_user();
			IScripDataService scDataSrv = ScripDataManager.getScripDataService();
			if (scDataSrv != null)
			{
				scDataSrv.generateScripCreateTemplate("C://Sunny Drives//SCE Files//Templates//");
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_scUpdTemplate(
			)
	{
		try
		{
			
			test_logon_user();
			IScripDataService scDataSrv = ScripDataManager.getScripDataService();
			if (scDataSrv != null)
			{
				scDataSrv.generateScripUpdateTemplatebyScripDesc("C://SCTemplates//", "Ajanta");
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_splitAwareflds(
				)
	{
		try
		{
			
			test_logon_user();
			IScripDataService scDataSrv = ScripDataManager.getScripDataService();
			if (scDataSrv != null)
			{
				IScripSheetMetadata scMdtSrv = scDataSrv.getContext().getBean(IScripSheetMetadata.class);
				if (scMdtSrv != null)
				{
					ArrayList<SheetSplitAttrs> splitawareAttr = scMdtSrv.getSplitAwareSheetAttrs();
					for (SheetSplitAttrs sheetSplitAttrs : splitawareAttr)
					{
						System.out.println(
						        sheetSplitAttrs.getSheetName() + "-----" + sheetSplitAttrs.getRelationName() + "-----");
						for (String sttr : sheetSplitAttrs.getSplitAttrs())
						{
							System.out.println(sttr);
						}
					}
					
				}
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings(
				    "unchecked"
				)
	public static void test_scripCache(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripDataContainerSrv scDataSrv = context.getBean(IScripDataContainerSrv.class);
				if (scDataSrv != null)
				{
					ScripDataContainer sccont1 = scDataSrv.getScripDetailsfromDB(" MINDAIND");
				}
				IScripDataContainerSrv scDataSrv2 = context.getBean(IScripDataContainerSrv.class);
				if (scDataSrv2 != null)
				{
					ScripDataContainer sccont2 = scDataSrv2.getScripDetailsfromDB("EICHERMOT");
					
					if (sccont2 != null)
					{
						ArrayList<OB_Scrip_BalSheet> balsheetColl = sccont2.getEntitiesforSheet("BalanceSheet")
						        .getSheetEntityList();
						if (balsheetColl != null)
						{
							for (OB_Scrip_BalSheet ob_Scrip_BalSheet : balsheetColl)
							{
								System.out.println(
								        ob_Scrip_BalSheet.getavgPrice() + " _____" + ob_Scrip_BalSheet.getyear());
							}
						}
					}
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test_PricingSrvModules(
			)
	{
		try
		{
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			TY_AvgPE       avgPE = new TY_AvgPE();
			TY_NPSD        npd   = new TY_NPSD();
			TY_Last_NP_FVR fvr   = new TY_Last_NP_FVR();
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				ISA_ENPRService                enprSrv  = context.getBean(ISA_ENPRService.class);
				ISA_AvgPEService               avgPESrv = context.getBean(ISA_AvgPEService.class);
				ISA_NPDService                 npdSrv   = context.getBean(ISA_NPDService.class);
				ISA_LastNettProfit_FVR_Service fvrSrv   = context.getBean(ISA_LastNettProfit_FVR_Service.class);
				
				if (enprSrv != null)
				{
					double avgENPRMinda = enprSrv.getAvgENPRforScripCode("MINDAIND");
					System.out.println("Average ENPR for MINDA : " + df.format(avgENPRMinda));
					
					avgPE = avgPESrv.getPERatiosforScripCode("MINDAIND");
					System.out.println("Average PE: " + df.format(avgPE.getAvgPE()));
					System.out.println("Minimum PE: " + df.format(avgPE.getMinPE()));
					System.out.println("Maximum PE: " + df.format(avgPE.getMaxPE()));
					
					if (npdSrv != null)
					{
						npd = npdSrv.getNettProfitDeltaforScripCode("MINDAIND");
						System.out.println("Nett. Profit Delta for MINDAIND");
						System.out.println("Last Year Current Quarter Profit Rs. Cr.: "
						        + df.format(npd.getNettProfit_Penultimate()));
						System.out.println("Current Year Current Quarter Profit Rs. Cr.: "
						        + df.format(npd.getNettProfit_Current()));
						System.out.println("Profit Delta in % : " + df.format(npd.getNPD()) + "%");
					}
					
					if (fvrSrv != null)
					{
						fvr = fvrSrv.getLastNettProfit_FVRforScripCode("MINDAIND");
						System.out.println(
						        "Last Nett. Profit For MINDA : Rs. Cr. ->  " + df.format(fvr.getLastNettProfit()));
						System.out.println("Face Value Ratio for MINDA : " + df.format(fvr.getFVR()));
					}
					
					double avgENPREicher = enprSrv.getAvgENPRforScripCode("EICHERMOT");
					System.out.println("Average ENPR for EICHER : " + df.format(avgENPREicher));
					avgPE = avgPESrv.getPERatiosforScripCode("EICHERMOT");
					System.out.println("Average PE: " + df.format(avgPE.getAvgPE()));
					System.out.println("Minimum PE: " + df.format(avgPE.getMinPE()));
					System.out.println("Maximum PE: " + df.format(avgPE.getMaxPE()));
					
					if (npdSrv != null)
					{
						npd = npdSrv.getNettProfitDeltaforScripCode("EICHERMOT");
						System.out.println("Nett. Profit Delta for EICHERMOT");
						System.out.println("Last Year Current Quarter Profit Rs. Cr.: "
						        + df.format(npd.getNettProfit_Penultimate()));
						System.out.println("Current Year Current Quarter Profit Rs. Cr.: "
						        + df.format(npd.getNettProfit_Current()));
						System.out.println("Profit Delta in % : " + df.format(npd.getNPD()) + "%");
					}
					
					if (fvrSrv != null)
					{
						fvr = fvrSrv.getLastNettProfit_FVRforScripCode("EICHERMOT");
						System.out.println(
						        "Last Nett. Profit For EICHER : Rs. Cr. ->  " + df.format(fvr.getLastNettProfit()));
						System.out.println("Face Value Ratio for EICHER : " + df.format(fvr.getFVR()));
					}
					
					double avgENPRMinda2 = enprSrv.getAvgENPRforScripCode("MINDAIND");
					System.out.println("Average ENPR for MINDA : " + df.format(avgENPRMinda2));
					avgPE = avgPESrv.getPERatiosforScripCode("MINDAIND");
					System.out.println("Average PE: " + df.format(avgPE.getAvgPE()));
					System.out.println("Minimum PE: " + df.format(avgPE.getMinPE()));
					System.out.println("Maximum PE: " + df.format(avgPE.getMaxPE()));
					if (npdSrv != null)
					{
						npd = npdSrv.getNettProfitDeltaforScripCode("MINDAIND");
						System.out.println("Nett. Profit Delta for MINDAIND");
						System.out.println("Last Year Current Quarter Profit Rs. Cr.: "
						        + df.format(npd.getNettProfit_Penultimate()));
						System.out.println("Current Year Current Quarter Profit Rs. Cr.: "
						        + df.format(npd.getNettProfit_Current()));
						System.out.println("Profit Delta in % : " + df.format(npd.getNPD()) + "%");
					}
					
					if (fvrSrv != null)
					{
						fvr = fvrSrv.getLastNettProfit_FVRforScripCode("MINDAIND");
						System.out.println(
						        "Last Nett. Profit For MINDA : Rs. Cr. ->  " + df.format(fvr.getLastNettProfit()));
						System.out.println("Face Value Ratio for MINDA : " + df.format(fvr.getFVR()));
					}
					
				}
				
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test_PricingSrv(
				)
	{
		try
		{
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			test_logon_user();
			
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				ISA_ScripPriceProjectionService ppSrv = context.getBean(ISA_ScripPriceProjectionService.class);
				if (ppSrv != null)
				{
					TY_PFactor pfactor = new TY_PFactor();
					pfactor.setFactorType(PriceProjectionFactorsConstants.External);
					
					TY_PFactorDetail pfactorDetail = new TY_PFactorDetail("Demonitization",
					        scriptsengine.enums.SCEenums.direction.DECREASE, 5);
					pfactor.getFactoritems().add(pfactorDetail);
					
					TY_PFactorDetail pfactorDetail2 = new TY_PFactorDetail("Export Boom",
					        scriptsengine.enums.SCEenums.direction.INCREASE, 7);
					pfactor.getFactoritems().add(pfactorDetail2);
					
					ppSrv.addFactorforPriceCalculation(pfactor);
					
					ArrayList<TY_PricesReturn> pRet = ppSrv.getPriceProjectionsforScrip("MINDAIND");
					if (pRet != null)
					{
						System.out.println(
						        "------------- Price Projections for " + ppSrv.getScCode() + "------------------");
						
						for (TY_PricesReturn ty_PricesReturn : pRet)
						{
							System.out.println("Projection Statergy ------ " + ty_PricesReturn.getPriceType());
							
							System.out.println("Average PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getAvgPE_PP()));
							System.out.println("Max PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMaxPE_PP()));
							System.out.println("Min PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMinPE_PP()));
						}
						
					}
				}
				
				ISA_ScripPriceProjectionService ppSrv1 = context.getBean(ISA_ScripPriceProjectionService.class);
				if (ppSrv1 != null)
				{
					ArrayList<TY_PricesReturn> pRet = ppSrv1.getPriceProjectionsforScrip("EICHERMOT");
					if (pRet != null)
					{
						System.out.println(
						        "------------- Price Projections for " + ppSrv1.getScCode() + "------------------");
						
						for (TY_PricesReturn ty_PricesReturn : pRet)
						{
							System.out.println("Projection Statergy ------ " + ty_PricesReturn.getPriceType());
							
							System.out.println("Average PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getAvgPE_PP()));
							System.out.println("Max PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMaxPE_PP()));
							System.out.println("Min PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMinPE_PP()));
						}
					}
				}
				
				ISA_ScripPriceProjectionService ppSrv2 = context.getBean(ISA_ScripPriceProjectionService.class);
				if (ppSrv1 != null)
				{
					ArrayList<TY_PricesReturn> pRet = ppSrv2.getPriceProjectionsforScrip_byDescription("DataMatics");
					if (pRet != null)
					{
						System.out.println(
						        "------------- Price Projections for " + ppSrv2.getScCode() + "------------------");
						
						for (TY_PricesReturn ty_PricesReturn : pRet)
						{
							System.out.println("Projection Statergy ------ " + ty_PricesReturn.getPriceType());
							
							System.out.println("Average PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getAvgPE_PP()));
							System.out.println("Max PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMaxPE_PP()));
							System.out.println("Min PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMinPE_PP()));
						}
					}
				}
				
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_PricingSrvStats(
				)
	{
		try
		{
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			test_logon_user();
			
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				ISA_ScripPriceProjectionService ppSrv = context.getBean(ISA_ScripPriceProjectionService.class);
				if (ppSrv != null)
				{
					/*
					 * TY_PFactor pfactor = new TY_PFactor(); pfactor.setFactorType(PriceProjectionFactorsConstants.
					 * External); TY_PFactorDetail pfactorDetail = new TY_PFactorDetail("Demonitization",
					 * scriptsengine.enums.SCEenums.direction.DECREASE, 5); pfactor.getFactoritems().add(pfactorDetail);
					 * TY_PFactorDetail pfactorDetail2 = new TY_PFactorDetail( "Export Boom",
					 * scriptsengine.enums.SCEenums.direction.INCREASE, 7);
					 * pfactor.getFactoritems().add(pfactorDetail2); ppSrv.addFactorforPriceCalculation(pfactor);
					 */
					
					ArrayList<TY_PricesReturn> pRet = ppSrv.getPriceProjectionsforScrip_byDescription("AksharChem");
					if (pRet != null)
					{
						System.out.println(
						        "------------- Price Projections for " + ppSrv.getScCode() + "------------------");
						
						for (TY_PricesReturn ty_PricesReturn : pRet)
						{
							System.out.println("Projection Statergy ------ " + ty_PricesReturn.getPriceType());
							
							System.out.println("Average PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getAvgPE_PP()));
							System.out.println("Max PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMaxPE_PP()));
							System.out.println("Min PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMinPE_PP()));
						}
						
						TY_ReportPPS_Default defStats = ppSrv.getDefPstats();
						
					}
					
					if (ppSrv instanceof IXLSReportAware)
					{
						((IXLSReportAware) ppSrv).generateXLSReport("C://Sunny Drives//SCE Files//Reports//");
						
					}
					
				}
				
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_PriceWatch(
					)
	{
		try
		{
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IPriceWatchService pwSrv = context.getBean(IPriceWatchService.class);
				if (pwSrv != null)
				{
					pwSrv.computeandShowProjectedPrices_Console();
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_PriceWatchScrip(
					)
	{
		try
		{
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IPriceWatchService pwSrv = context.getBean(IPriceWatchService.class);
				if (pwSrv != null)
				{
					TY_PriceWatchItem pwItem = pwSrv.computeProjectedPriceforScripdesc("Transport");
					if (pwItem != null)
					{
						System.out.println(pwItem);
					}
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_PriceWatchXLS(
					)
	{
		try
		{
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IPriceWatchService pwSrv = context.getBean(IPriceWatchService.class);
				if (pwSrv != null)
				{
					pwSrv.computeandDLProjectedPrices_XLS("C://Sunny Drives//SCE Files//PriceWatch//");
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_Detail_PriceWatchXLS(
					)
	{
		try
		{
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IPriceWatchService pwSrv = context.getBean(IPriceWatchService.class);
				if (pwSrv != null)
				{
					pwSrv.generateDetailedReport_XLS("C://Sunny Drives//SCE Files//PriceWatch//");
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_scDBSnapShot(
					)
	{
		try
		{
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripDBSnapShot scSSSrv = context.getBean(IScripDBSnapShot.class);
				if (scSSSrv != null)
				{
					ScripDBSnaphot scSS = scSSSrv.getScripDBSnapshot("MINDAIND");
					if (scSS != null)
					{
						if (scSS.getSheetsDBSS() != null)
						{
							if (scSS.getSheetsDBSS().size() > 0)
							{
								for (SheetDBSnapShot sheet : scSS.getSheetsDBSS())
								{
									if (sheet.getKeyVals() != null)
									{
										if (sheet.getKeyVals().size() > 0)
										{
											System.out.println(sheet.getSheetName());
											for (TY_NameValue nameval : sheet.getKeyVals())
											{
												System.out.println("           " + nameval.Name + " - "
												        + nameval.Value.toString());
											}
										}
										
									}
									
									PenultimateQYear pqYear = new PenultimateQYear();
									System.out.println(pqYear.getYear() + " - " + pqYear.getQuarter());
									
								}
								
							}
						}
					}
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_PricingSrvInd(
				)
	{
		try
		{
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			test_logon_user();
			
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				ISA_ScripPriceProjectionService ppSrv = context.getBean(ISA_ScripPriceProjectionService.class);
				if (ppSrv != null)
				{
					TY_PFactor pfactor = new TY_PFactor();
					pfactor.setFactorType(PriceProjectionFactorsConstants.External);
					
					TY_PFactorDetail pfactorDetail = new TY_PFactorDetail("Demonitization",
					        scriptsengine.enums.SCEenums.direction.DECREASE, 5);
					pfactor.getFactoritems().add(pfactorDetail);
					
					TY_PFactorDetail pfactorDetail2 = new TY_PFactorDetail("Export Boom",
					        scriptsengine.enums.SCEenums.direction.INCREASE, 7);
					pfactor.getFactoritems().add(pfactorDetail2);
					
					// Add Raw Material Factor
					TY_PFactor pfactorRawM = new TY_PFactor();
					pfactorRawM.setFactorType(PriceProjectionFactorsConstants.RawMaterials);
					pfactorRawM.getFactoritems()
					        .add(new TY_PFactorDetail("Steel", scriptsengine.enums.SCEenums.direction.INCREASE, 5));
					pfactorRawM.getFactoritems().add(
					        new TY_PFactorDetail("Carbon Fibre", scriptsengine.enums.SCEenums.direction.INCREASE, 8));
					pfactorRawM.getFactoritems()
					        .add(new TY_PFactorDetail("Plastics", scriptsengine.enums.SCEenums.direction.INCREASE, 4));
					
					// Sales Factor - NPM
					TY_PFactor pfactorSalesF = new TY_PFactor();
					pfactorSalesF.setFactorType(PriceProjectionFactorsConstants.SalesForecast);
					pfactorSalesF.getFactoritems().add(new TY_PFactorDetail("European Sales Decrease",
					        scriptsengine.enums.SCEenums.direction.INCREASE, 25));
					
					ppSrv.addFactorforPriceCalculation(pfactor);
					ppSrv.addFactorforPriceCalculation(pfactorRawM);
					ppSrv.addFactorforPriceCalculation(pfactorSalesF);
					
					ArrayList<TY_PricesReturn> pRet = ppSrv.getPriceProjectionsforScrip("MRF");
					if (pRet != null)
					{
						System.out.println(
						        "------------- Price Projections for " + ppSrv.getScCode() + "------------------");
						
						for (TY_PricesReturn ty_PricesReturn : pRet)
						{
							System.out.println("Projection Statergy ------ " + ty_PricesReturn.getPriceType());
							
							System.out.println("Average PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getAvgPE_PP()));
							System.out.println("Max PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMaxPE_PP()));
							System.out.println("Min PE Price : Rs. "
							        + df.format(ty_PricesReturn.getProjectedPrices().getMinPE_PP()));
						}
						
					}
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_MassUpdateTmpl(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripMassUpdateSrv scMassUpdSrv = context.getBean(IScripMassUpdateSrv.class);
				if (scMassUpdSrv != null)
				{
					scMassUpdSrv.generateTemplatesforScripsPendingUpdate(
					        "C://Sunny Drives//SCE Files//MassUpdate//PendingUpdate//");
				}
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_MassUpdate_ReadyforUpdate(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripMassUpdateSrv scMassUpdSrv = context.getBean(IScripMassUpdateSrv.class);
				if (scMassUpdSrv != null)
				{
					scMassUpdSrv.updateScripsfromFilePath("C://Sunny Drives//SCE Files//MassUpdate//UpdateReady//");
				}
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_ScripHealthSrv(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripHealthSrv scHlthSrv = context.getBean(IScripHealthSrv.class);
				if (scHlthSrv != null)
				{
					scHlthSrv.getScripHealthforScrip("MINDAIND");
				}
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_RepDS(
				)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				
				IReportDataSource scdeltaSRv = (IReportDataSource) context.getBean("DS_ScripsAnalysis");
				if (scdeltaSRv != null)
				{
					TY_ScRoot_AttrContainers scattrCon = scdeltaSRv.generateReportDataSourceforScripCode("ASHOKLEY");
					if (scattrCon != null)
					{
						System.out.println(scattrCon.getScRoot().getmCap());
					}
					
					scdeltaSRv.generateReportDataSourceforScripCode("EICHERMOT");
					TY_ScRoot_AttrContainers scattrCon2 = scdeltaSRv.generateReportDataSourceforScripCode("MINDAIND");
					if (scattrCon != null)
					{
						System.out.println(scattrCon2.getScRoot().getscCode());
					}
					
					TY_ScRoot_AttrContainers scattrCon3 = scdeltaSRv.generateReportDataSourceforScripCode("ASHOKLEY");
					if (scattrCon != null)
					{
						System.out.println(scattrCon3.getScRoot().getmCap());
					}
					
				}
				
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_SASConsole(
			)
	{
		try
		{
			String        hFormat = "%40s%40s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s";
			String        dFormat = "%40s%40s%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f";
			DecimalFormat df      = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				
				IScripAnalysisSrv scASrv = context.getBean(IScripAnalysisSrv.class);
				if (scASrv != null)
				{
					scASrv.populateDataforAllScrips();
					ArrayList<TY_ScRoot_AttrContainers> SCRootAttrConts = scASrv.getScRootAttContainersList();
					if (SCRootAttrConts != null)
					{
						// Print the headers first
						
						TY_ScRoot_AttrContainers SCRootAttrContentH = SCRootAttrConts.get(0);
						if (SCRootAttrContentH != null)
						{
							System.out.println();
							System.out.print("Scrip Code" + "|" + "Sector" + "|");
							for (TY_Attr_Container attrCont : SCRootAttrContentH.getAttrContainers())
							{
								System.out.print(attrCont.getAttrLabel() + "|");
							}
							for (TY_AttribSingleVal singleval : SCRootAttrContentH.getSingleValContainers())
							{
								System.out.print(singleval.getAttrLabel() + "|");
							}
						}
						
						// Now print the Data
						for (TY_ScRoot_AttrContainers SCRootAttrContent : SCRootAttrConts)
						{
							System.out.println();
							System.out.println();
							System.out.print(
							        SCRootAttrContent.getScRoot().getscCode() + "|"
							                + SCRootAttrContent.getScRoot().getscSector() + "|");
							for (TY_Attr_Container attrCont : SCRootAttrContent.getAttrContainers())
							{
								System.out.print(df.format(attrCont.getDeltaAvg()) + "|");
							}
							for (TY_AttribSingleVal singleval : SCRootAttrContent.getSingleValContainers())
							{
								System.out.print(df.format(singleval.getValue()) + "|");
							}
						}
					}
				}
				
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_SAS(
			)
	{
		try
		{
			String        hFormat = "%40s%40s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s%20s";
			String        dFormat = "%40s%40s%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f%20.2f";
			DecimalFormat df      = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				
				IScripAnalysisSrv scASrv = context.getBean(IScripAnalysisSrv.class);
				if (scASrv != null)
				{
					
					/*
					 * ArrayList<String> sectors = new ArrayList<String>(); sectors.add("AUTO ANCILLARIES");
					 * sectors.add( "CERAMICS & GRANITE"); scASrv.populateDataforSectors(sectors); if (scASrv instanceof
					 * IXLSReportAware) { ((IXLSReportAware) scASrv).generateXLSReport(
					 * "C://Sunny Drives//SCE Files//ScripAnalysis//" ); }
					 */
					scASrv.populateDataforAllScrips();
					
					if (scASrv instanceof IXLSReportAware)
					{
						((IXLSReportAware) scASrv).generateXLSReport("C://Sunny Drives//SCE Files//ScripAnalysis//");
					}
					
				}
			}
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_DSMdt_mShareAttr(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IStatsSrvConfigMetadata DSMdt = context.getBean(IStatsSrvConfigMetadata.class);
				if (DSMdt != null)
				{
					ArrayList<String> mShareAttrs = DSMdt.getmShareRelevant_AttrNames();
					if (mShareAttrs != null)
					{
						mShareAttrs.forEach(x -> System.out.println(x));
					}
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_mShareAnalytics(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripAnalysisSrv scASrv = context.getBean(IScripAnalysisSrv.class);
				if (scASrv != null)
				{
					scASrv.populateDataforSector("AUTO ANCILLARIES");
					
					IMShareScripAnalytics mshareSrv = context.getBean(IMShareScripAnalytics.class);
					if (mshareSrv != null)
					{
						TY_Sector_AttrContainers secAttrCont = mshareSrv
						        .generateMarketShareAnalytics(scASrv.getScRootAttContainersList());
						if (secAttrCont != null)
						{
							System.out.println("Sector Loaded");
						}
					}
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_PortfolioSrv(
					)
	{
		try
		{
			
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IPortfolioManager PFMSrv = context.getBean(IPortfolioManager.class);
				if (PFMSrv != null)
				{
					PFMSrv.getMyDematACs().forEach(x -> System.out.println(x));
					((ClassPathXmlApplicationContext) context).close();
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_dateEntity(
				)
	{
		try
		{
			test_logon_user();
			
			/*
			 * OB_Txn_Catg txnCatg = ModelObjectFactory.createObjectbyName("OB_Txn_Catg"); if (txnCatg != null) {
			 * txnCatg.setCategory("Test Category"); txnCatg.setBook_Type(Book_Type.None); Calendar cal =
			 * Calendar.getInstance(); Date crdate = cal.getTime(); txnCatg.setDateCreate(crdate); txnCatg.Save(); }
			 */
			
			IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Txn_Catg");
			if (qs != null)
			{
				ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
				
				params.add(new TY_NameValue("Category", "Test"));
				// params.add(new TY_NameValue("Active", true));
				String condn = " Category = ? ";
				
				ArrayList<OB_Txn_Catg> txncatgs = qs.executeQuery(condn, params);
				if (txncatgs != null)
				{
					OB_Txn_Catg catgEnt1 = txncatgs.get(0);
					catgEnt1.lock();
					catgEnt1.switchtoChangeMode();
					catgEnt1.setCategory("Test Category");
					catgEnt1.Save();
					
					// txncatgs.forEach(x -> System.out.println(x.getCategory()
					// + " " + x.getBook_Type() + " " +
					// x.getDateCreate()));
				}
				
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_console(
				)
	{
		try
		{
			test_logon_user();
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("Enter 'Q' anytime at prompt to exit the program!");
			
			while (true)
			{
				System.out.print("Enter something : ");
				String input = scanner.nextLine();
				
				if ("Q".equals(input))
				{
					
					scanner.close();
					System.out.println("Exit!");
					System.exit(0);
					
				}
				
				System.out.println("input : " + input);
				System.out.println("-----------\n");
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test_scBuyAssist(
					)
	{
		try
		{
			test_logon_user();
			ApplicationContext context = new ClassPathXmlApplicationContext("config/startup/Startup.xml");
			if (context != null)
			{
				IScripBuyAssistSrv scBuyAssistSrv = context.getBean(IScripBuyAssistSrv.class);
				if (scBuyAssistSrv != null)
				{
					scBuyAssistSrv.InitializeinConsole();
				}
			}
			
		}
		
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test_buyScrip(
				)
	{
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		try
		{
			
			ApplicationContext context = test_logon_user();
			
			/*
			 * IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname("OB_Positions_Header"); if (qs !=
			 * null) { ArrayList<OB_Positions_Header> positionsH = qs.executeQuery(); }
			 */
			
			if (context != null)
			{
				IPortfolioManager PFMSrv = context.getBean(IPortfolioManager.class);
				if (PFMSrv != null)
				{
					PFMSrv.loadMyPortfolio();
					
					TY_ScripBuy scSellCont = new TY_ScripBuy();
					
					scSellCont.setDematAC(PFMSrv.getMyDematACs().get(0).getAcNum());
					scSellCont.setScDesc("KPR");
					
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					
					TY_QtyPriceDate item1 = new TY_QtyPriceDate(110, 791.58, dateFormat.parse("12/09/2017"));
					scSellCont.getBuyItems().add(item1);
					
					/*
					 * TY_QtyPriceDate item2 = new TY_QtyPriceDate(10, 939.72, dateFormat.parse("10/07/2017"));
					 * scSellCont.getBuyItems().add(item2);
					 */
					
					/*
					 * TY_QtyPriceDate item3 = new TY_QtyPriceDate(25, 543.25, dateFormat.parse("10/07/2017"));
					 * scSellCont.getBuyItems().add(item3); TY_QtyPriceDate item4 = new TY_QtyPriceDate(8, 564.25,
					 * dateFormat.parse("11/07/2017")); scSellCont.getBuyItems().add(item4);
					 */
					
					TY_ScripBuySummary buySumm = PFMSrv.buyScrip(scSellCont, false);
					
					PFMSrv.getScripPositions()
					        .forEach(x -> System.out.println(x.getScPosHeader().getScCode() + "  Holdings - "
					                + x.getScPosHeader().getCurrHolding() + " @ " + x.getScPosHeader().getPPU()));
					
					if (buySumm != null)
					{
						if (buySumm.getBeforeBuy() != null)
						{
							System.out.println("---------------------Before Purchase Snapshot -------------------");
							System.out.println("Holdings : " + buySumm.getBeforeBuy().getHoldings());
							System.out.println("Free Shares : " + buySumm.getBeforeBuy().getFreeShares());
							System.out.println("Price/Unit : Rs. " + df.format(buySumm.getBeforeBuy().getPpu()));
							System.out.println("Total Investments in Scrip : Rs. "
							        + df.format(buySumm.getBeforeBuy().getInvestments()));
							System.out.println("Scrip Contribution to Portfolio : "
							        + df.format(buySumm.getBeforeBuy().getScPFContr()) + " %");
							System.out.println("Scrip's Sector's Contribution to Portfolio : "
							        + df.format(buySumm.getBeforeBuy().getSecPFContr()) + " %");
						}
						
						if (buySumm.getAfterBuy() != null)
						{
							System.out.println("---------------------After Purchase Snapshot -------------------");
							System.out.println("Holdings : " + buySumm.getAfterBuy().getHoldings());
							System.out.println("Free Shares : " + buySumm.getAfterBuy().getFreeShares());
							System.out.println("Price/Unit : Rs. " + df.format(buySumm.getAfterBuy().getPpu()));
							System.out.println("Total Investments in Scrip : Rs. "
							        + df.format(buySumm.getAfterBuy().getInvestments()));
							System.out.println("Scrip Contribution to Portfolio : "
							        + df.format(buySumm.getAfterBuy().getScPFContr()) + " %");
							System.out.println("Scrip's Sector's Contribution to Portfolio : "
							        + df.format(buySumm.getAfterBuy().getSecPFContr()) + " %");
						}
					}
					
					((ClassPathXmlApplicationContext) context).close();
					
				}
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_buyScripwoCtxtRefresh(
				)
	{
		try
		{
			
			test_logon_user();
			
			/**
			 * Now Start Entity Creation Process
			 */
			
			OB_Positions_Header posHeader = ModelObjectFactory
			        .createObjectbyClass(new OB_Positions_Header().getClass());
			if (posHeader != null)
			{
				/**
				 * Position model for Update in Portfolio Manager
				 */
				
				posHeader.setScCode("UJAAS");
				posHeader.setCurrHolding(10000);
				posHeader.setCurrInvestment(430000);
				posHeader.setPPU(36.30);
				posHeader.setAmntRealized(0);
				posHeader.setDividendEarnings(0);
				posHeader.setFreeHolding(0);
				
				/*
				 * Create Relation - Position Items
				 */
				// for ( TY_QtyPriceDate qtypriceDate :
				// scripPurchaseDetails.getBuyItems() )
				
				OB_Positions_Item posItem = (OB_Positions_Item) posHeader
				        .Create_RelatedEntity("OB_Positions_Items_Rel");
				if (posItem != null)
				{
					posItem.setDematAcNum("ZERODHA");
					posItem.setPPUTxn(36.30);
					posItem.setQtyTxn(10000);
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					posItem.setTxnDate(dateFormat.parse("24/03/2017"));
					posItem.setTxnType(SCEenums.txnType.BUY);
					posItem.setETQ(0);
					
				}
				
				/*
				 * Commit the Txn
				 */
				if (posHeader.Save())
				{
					System.out.println("Save successful!!");
				}
				
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_SellScrip(
					)
	{
		try
		{
			
			ApplicationContext context = test_logon_user();
			
			if (context != null)
			{
				IPortfolioManager pfMgr = context.getBean(IPortfolioManager.class);
				if (pfMgr != null)
				{
					
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date       sellDate   = dateFormat.parse("12/09/2017");
					
					TY_Sell_Quote sellQuote = new TY_Sell_Quote();
					// sellQuote.setScCode("VTL");
					sellQuote.setScDesc("Vardhman Textiles");
					
					ArrayList<TY_Qty_Price> qty_PriceItems = new ArrayList<TY_Qty_Price>();
					
					TY_Qty_Price Q1 = new TY_Qty_Price(80, 1172.72);
					
					// TY_Qty_Price Q2 = new TY_Qty_Price(15, 916);
					
					qty_PriceItems.add(Q1);
					// qty_PriceItems.add(Q2);
					
					sellQuote.getSellItems().addAll(qty_PriceItems);
					
					sellQuote.setSellDate(sellDate);
					
					TY_Sell_Proposal proposal = pfMgr.sellScrip(sellQuote, scripSellMode.PandL);
					
					if (proposal != null)
					{
						helperDisplaySellproposal(sellQuote, proposal);
					}
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test_SellScripSimulation(
					)
	{
		try
		{
			
			ApplicationContext context = test_logon_user();
			
			if (context != null)
			{
				IScripSellSimulation sellSimSrv = context.getBean(IScripSellSimulation.class);
				if (sellSimSrv != null)
				{
					
					TY_Sell_Proposal proposal = null;
					
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date       sellDate   = dateFormat.parse("08/07/2017");
					
					TY_Sell_Quote sellQuote = new TY_Sell_Quote();
					sellQuote.setScDesc("Ujaas");
					
					ArrayList<TY_Qty_Price> qty_PriceItems = new ArrayList<TY_Qty_Price>();
					
					TY_Qty_Price Q1 = new TY_Qty_Price(1000, 27.5);
					
					// TY_Qty_Price Q2 = new TY_Qty_Price(15, 916);
					
					qty_PriceItems.add(Q1);
					// qty_PriceItems.add(Q2);
					
					sellQuote.getSellItems().addAll(qty_PriceItems);
					
					sellQuote.setSellDate(sellDate);
					
					proposal = sellSimSrv.generateProposalforSellQuote(sellQuote);
					if (proposal != null)
					{
						helperDisplaySellproposal(sellQuote, proposal);
					}
					
					/*
					 * IPortfolioManager pfMgr = context.getBean(IPortfolioManager.class); if (pfMgr != null) {
					 * pfMgr.sellScrip(proposal, scripSellMode.FreeShares); }
					 */
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void helperDisplaySellproposal(
	        TY_Sell_Quote sellQuote, TY_Sell_Proposal sellProposal
			)
	{
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		if (sellProposal != null && sellQuote != null)
		{
			System.out.println(
			        "_________________________________________________________________________________________________________________________________________________________________________________________");
			
			System.out.println("SELL Proposal for Scrip -  " + sellQuote.getScCode());
			System.out.println("Sell Quotes : AS Below");
			int Qc = 1;
			for (TY_Qty_Price quoteItem : sellQuote.getSellItems())
			{
				System.out.println("# " + Qc + " | Sell Qty - " + quoteItem.getQty() + " | Price/Unit - "
				        + df.format(quoteItem.getPrice()));
				Qc++;
			}
			
			if (sellProposal.getSellProposalHeader() != null)
			{
				if (sellProposal.getSellProposalHeader().getPandLPerspective() != null)
				{
					System.out.println();
					System.out.println(
					        "___________________________________________________________________________________________________________________________________________");
					
					System.out.println(
					        "                               P & L PERSPECTIVE                                                                          ");
					System.out.println();
					System.out.println("Nett. Realization : Rs. " + df
					        .format(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalRealization()));
					System.out.println("------------------------------------------------------------");
					
					System.out.println("Transaction Amount : Rs. " + df
					        .format(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTxnAmount()));
					
					System.out.println("Tax Liability : Rs. " + df
					        .format(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalTaxliability()));
					
					System.out.println(
					        "Fee and Brokerage : Rs. " + df
					                .format(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalFees()));
					
					System.out.println("Opportunity Cost : Rs. "
					        + df.format(sellProposal.getSellProposalHeader().getPandLPerspective().getTotalOppCost()));
					
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
				}
				
				if (sellProposal.getSellProposalHeader().getFreeSharesPespective() != null)
				{
					System.out.println();
					System.out.println(
					        "_______________________________________________________________________________________________________________________________________________");
					System.out.println(
					        "                                              FREE SHARES PERSPECTIVE                                                              ");
					System.out.println();
					System.out.println(
					        "# FRee Shares Eligible "
					                + sellProposal.getSellProposalHeader().getFreeSharesPespective().getNumFreeShares()
					                + "                 Tax/Opp. Cost Reinvested : " + sellProposal
					                        .getSellProposalHeader().getFreeSharesPespective().isReinvestTaxOpp());
					System.out.println(
					        "-----------------------------------------------------------------------------------");
					System.out.println("# Shares to Sell at Quote Price  : "
					        + sellProposal.getSellProposalHeader().getFreeSharesPespective().getNumActualSale());
					
					System.out.println("Realization Spill : Rs. " + df.format(
					        sellProposal.getSellProposalHeader().getFreeSharesPespective().getRealizationSpill()));
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					
				} else
				{
					System.out.println("No Free shares could be computed for the Quote!!");
				}
				
				if (sellProposal.getSellProposalHeader().getPricesRealizationPerspective() != null)
				{
					System.out.println();
					System.out.println(
					        "_______________________________________________________________________________________________________________________________________________");
					System.out.println(
					        "                                              PRICES & REALIZATION PERSPECTIVE                                                              ");
					System.out.println();
					
					System.out.println("Actual Buy Price/Unit :  Rs. " + df.format(sellProposal.getSellProposalHeader()
					        .getPricesRealizationPerspective().getPrices().getActualBuyPrice()));
					
					System.out.println("Actual Sell Price/Unit:  Rs. " + df.format(sellProposal.getSellProposalHeader()
					        .getPricesRealizationPerspective().getPrices().getActualSellPrice()));
					
					System.out.println();
					String hFormat = "%30s%30s%30s%30s";
					String dFormat = "%30s%30.2f%30.2f%30.2f";
					
					System.out.println("------------RECOMMENDATIONS-------------------");
					System.out.format(hFormat, "PE Type", "Buy Price", "Avg. Price", "Sell Price");
					System.out.println();
					
					System.out.format(dFormat, "Adjusted",
					        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
					                .getAvgPEPrices().getMinPE_PP(),
					        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
					                .getAvgPEPrices().getAvgPE_PP(),
					        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
					                .getAvgPEPrices().getMaxPE_PP());
					
					System.out.println();
					if (
						    sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
						            .isPEDifferent()
						)
						{
						System.out.format(dFormat, "Average",
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
						                .getAdjPEPrices().getMinPE_PP(),
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
						                .getAdjPEPrices().getAvgPE_PP(),
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
						                .getAdjPEPrices().getMaxPE_PP());
					}
					
					if (
						    sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
						            .getActualBuyPrice() > 0
						)
						{
						System.out.println();
						System.out.println();
						System.out.println("--------------REALIZATIONS---------------");
						System.out.println("Actual BUY to SELL (%) : " + df.format(sellProposal.getSellProposalHeader()
						        .getPricesRealizationPerspective().getRatios().getActualBuytoSellRealization()));
						System.out.println("-----------------------------------------");
						System.out.println();
						hFormat = "%30s%40s%40s";
						dFormat = "%30s%40.2f%40.2f";
						System.out.format(hFormat, "PE Type", "Buy to Avg(%)", "Buy to Max(%)");
						System.out.println();
						
						System.out.format(dFormat, "Adjusted",
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getRatios()
						                .getAvgPERealizationRatios().getActualBuytoAvgPrice(),
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getRatios()
						                .getAvgPERealizationRatios().getActualBuytoMaxPrice());
						
						if (
							    sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
							            .isPEDifferent()
							)
							{
							System.out.println();
							System.out.format(dFormat, "Average",
							        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getRatios()
							                .getAdjPERealizationRatios().getActualBuytoAvgPrice(),
							        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getRatios()
							                .getAdjPERealizationRatios().getActualBuytoMaxPrice());
						}
						
						System.out.println();
						System.out.println();
						System.out.println("------------RELATIVE REALZATIONS--------------");
						
						System.out.println();
						
						System.out.format(hFormat, "PE Type", "Actual to Buy/Avg Realization(%) ",
						        "Actual to Buy/Max Realization(%)");
						System.out.println();
						
						System.out.format(dFormat, "Adjusted",
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getRealizations()
						                .getAvgPERelativeRealizations().getRelRealizationAvgPrice(),
						        sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getRealizations()
						                .getAvgPERelativeRealizations().getRelRealizationMaxPrice());
						if (
							    sellProposal.getSellProposalHeader().getPricesRealizationPerspective().getPrices()
							            .isPEDifferent()
							)
							{
							System.out.println();
							System.out.format(dFormat, "Average",
							        sellProposal.getSellProposalHeader().getPricesRealizationPerspective()
							                .getRealizations().getAdjPERelativeRealizations()
							                .getRelRealizationAvgPrice(),
							        sellProposal.getSellProposalHeader().getPricesRealizationPerspective()
							                .getRealizations().getAdjPERelativeRealizations()
							                .getRelRealizationMaxPrice());
						}
					}
					
				}
				
				if (sellProposal.getTaxPerspective() != null && sellProposal.getTaxPerspective().getTaxSsavH() != null)
				{
					
					System.out.println();
					String hFormat = "%20s%60s%60s";
					String dFormat = "%20d%60.2f%60d";
					
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					System.out.println(
					        "                                               TAX PERSPECTIVE                                                              ");
					
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					
					System.out.println("You can save INR "
					        + df.format(sellProposal.getTaxPerspective().getTaxSsavH().getProbTaxSavings())
					        + " in taxes amounting to "
					        + sellProposal.getTaxPerspective().getTaxSsavH().getProbNumFreeShares()
					        + " free Shares by not selling for a max of "
					        + sellProposal.getTaxPerspective().getTaxSsavH().getNumDays() + " days.");
					
					System.out.println();
					System.out.println("DETAILED TAX BREAK-UP");
					System.out.format(hFormat, "#Days to Hold", "Projected Tax Savings", "Projected # Free Shares");
					System.out.println();
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					
					for (TY_TaxProjections taxItem : sellProposal.getTaxPerspective().getTaxSavI())
					{
						System.out.format(dFormat, taxItem.getNumDays(), taxItem.getProbTaxSavings(),
						        taxItem.getProbNumFreeShares());
						System.out.println();
					}
				}
				
				if (sellProposal.getSellProposalSummary() != null)
				{
					
					System.out.println();
					String hFormat = "%20s%20s%20s%20s%20s%20s%20s";
					String dFormat = "%20s%20d%20.2f%20.2f%20.2f%20.2f%20.2f";
					
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					System.out.println(
					        "                                               SELL PROPOSAL SUMMARY                                                              ");
					
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					System.out.format(hFormat, "Demat A/C", "Sell Qty", "Txn. Amount", "Tax Incurred", "Fee Incurred",
					        "Opp. Cost", "Realization");
					System.out.println();
					System.out.println(
					        "______________________________________________________________________________________________________________________________________________");
					
					for (TY_SellProposalI_Summary sumItem : sellProposal.getSellProposalSummary())
					{
						System.out.format(dFormat, sumItem.getDematAC(), sumItem.getSellQty(), sumItem.getTxnAmount(),
						        sumItem.getTaxIncurred(), sumItem.getFeeIncurred(), sumItem.getOppCost(),
						        sumItem.getRealization());
						System.out.println();
					}
				}
				
				if (sellProposal.getSellProposalItems() != null)
				{
					System.out.println();
					
					String hFormat = "%20s%20s%20s%20s%20s%20s%20s";
					String dFormat = "%20s%20d%20.2f%20.2f%20.2f%20.2f%20.2f";
					
					System.out.println(
					        "_______________________________________________________________________________________________________________________________________________");
					
					System.out.println(
					        "                                         SELL PROPOSAL ITEMS - DETAILS                                                                   ");
					System.out.println(
					        "_______________________________________________________________________________________________________________________________________________");
					System.out.format(hFormat, "Demat A/C", "Sell Qty", "Txn. Amount", "Tax Incurred", "Fee Incurred",
					        "Opp. Cost", "Realization");
					System.out.println();
					System.out.println(
					        "_______________________________________________________________________________________________________________________________________________");
					
					for (TY_SellProposalI Item : sellProposal.getSellProposalItems())
					{
						System.out.format(dFormat, Item.getDematAC(), Item.getSellQty(), Item.getTxnAmount(),
						        Item.getTaxIncurred(), Item.getFeeIncurred(), Item.getOppCost(), Item.getRealization());
						System.out.println();
					}
				}
				
			}
			
		}
	}
	
	public static ApplicationContext test_dryrun(
	) throws SQLException
	{
		ApplicationContext context      = Initialize_Context();
		LogonService       logonService = context.getBean("logonService", LogonService.class);
		if (logonService != null)
		{
			logonService.setUsername("DRYRUN");
			
			try
			{
				logonService.Validate_Logon("DRYRUN");
				if (logonService.IS_Authenticated() == true)
				{
					System.out.println("User Authenticated!! Ready for Logon event!");
					
				}
			} catch (EX_InvalidLogonException | EX_UserLogonException Ex)
			{
				MessagesFormatter msgformatter = context.getBean("messagesFormatter", MessagesFormatter.class);
				msgformatter.setMessage_snippet(Ex);
				String msg = msgformatter.generate_formatted_message();
				System.out.println(msg);
			}
		}
		
		return context;
		
	}
	
	public static void test_writeup()
	{
		try
		{

			ApplicationContext context = test_dryrun();
			
			
			//ApplicationContext context = test_logon_user();
			if (context != null)
			{
				IExecutable executionBean = null;
				try
				{
					executionBean = context.getBean(IExecutable.class);
				}
				catch (NoUniqueBeanDefinitionException e)
				{
					try
					{
						ExecutionBeanName exeBeanName = context.getBean(ExecutionBeanName.class);
						if (exeBeanName != null)
						{
							executionBean = (IExecutable) context.getBean(exeBeanName.getExeBeanName());
						}

					}
					catch (Exception ex)
					{

					}

				}

				if (executionBean != null)
				{
					executionBean.execute(context);
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
