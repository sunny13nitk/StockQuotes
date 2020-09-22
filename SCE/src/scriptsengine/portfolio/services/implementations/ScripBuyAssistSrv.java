package scriptsengine.portfolio.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidObjectException;
import modelframework.exceptions.EX_InvalidRelationException;
import modelframework.exceptions.EX_NotRootException;
import modelframework.exceptions.EX_NullParamsException;
import modelframework.exceptions.EX_ParamCountMismatchException;
import modelframework.exceptions.EX_ParamInitializationException;
import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.portfolio.services.interfaces.IPortfolioManager;
import scriptsengine.portfolio.services.interfaces.IScripBuyAssistSrv;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.validations.implementations.ScripExistsService;

/**
 * 
 * Scrips Purchase Assist Service
 */

@Service("ScripBuyAssistSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripBuyAssistSrv implements IScripBuyAssistSrv
{

	/**************************** AutoWIRED BEANS **********************************/
	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private IPortfolioManager	pFMMgr;		// Session Scoped Portfolio Manager Bean

	@Autowired
	private ScripExistsService	scExSrv;		// Scrip Exists Service

	/**************************** AutoWIRED BEANS **********************************/

	/**************************** My Properties **********************************/

	private OB_Scrip_General		scRoot;

	/**************************** My Properties **********************************/

	/**
	 * Console Application Service Initialization
	 * 
	 * @throws EX_General
	 */
	@Override
	public void InitializeinConsole() throws EX_General
	{

		// Portfolio Manager is Bound
		if (pFMMgr != null)
		{
			for ( int i = 0; i <= 10; i++ )
			{
				System.out.println();
			}
			// At least need to have one demat Account to perform any stock transaction

			if (pFMMgr.getMyDematACs() != null)
			{

				if (pFMMgr.getMyDematACs().size() > 0)
				{
					Scanner scanner = new Scanner(System.in);

					// Welcome Message
					GeneralMessage msgGen = new GeneralMessage("WELCOME", null);
					String wlcmMsg = msgFormatter.generate_message_snippet(msgGen);
					System.out.println(wlcmMsg);

					// Get a Fine scrip Code
					while (scRoot == null)
					{

						String scPattern = getScripCode(scanner);
						if (scPattern != null)
						{
							if (scExSrv != null)
							{
								try
								{

									this.scRoot = scExSrv.getScripRootbyDescStartingwith(scPattern);
									if (scRoot != null)
									{
										if (scRoot.getscCode() != null)
										{
											generateScInfo(scanner);
										}
									}
									else
									{

										msgGen = new GeneralMessage("SCNOTEXISTS", new Object[]
										{ scPattern
										});
										String Msg = msgFormatter.generate_message_snippet(msgGen);
										System.out.print(Msg + "/n");
									}
								}
								catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException
								          | NoSuchMethodException | SecurityException | EX_InvalidObjectException | EX_NotRootException
								          | SQLException | EX_NullParamsException | EX_ParamCountMismatchException
								          | EX_ParamInitializationException | EX_InvalidRelationException e)
								{
									EX_General egen = new EX_General("SRVERROR", new Object[]
									{ e.getMessage()
									});
									String Msg = msgFormatter.generate_message_snippet(egen);
									System.out.print(Msg);
									throw egen;
								}
							}
						}

					}

					// Proceed with next steps

				}
			}
			else
			{
				GeneralMessage msgGen = new GeneralMessage("NODEMATERROR", null);
				String Msg = msgFormatter.generate_message_snippet(msgGen);
				EX_General egen = new EX_General("SRVERROR", new Object[]
				{ Msg
				});
				System.out.print(Msg);
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}

	}

	// ************************** PRIVATE METHODS ***********************************************

	/**
	 * -------------------------------------
	 * Get Scrip Code from Prompt
	 * 
	 * @param scanner
	 * @return - Scrip Code
	 *         ---------------------------------------
	 */
	private String getScripCode(Scanner scanner)
	{
		GeneralMessage msgGen = null;
		String scPattern = null;
		if (scanner != null)
		{
			msgGen = new GeneralMessage("SCPROMPT", null);
			String Msg = msgFormatter.generate_message_snippet(msgGen);
			System.out.print(Msg);
			scPattern = scanner.nextLine();
			checkForExit(scanner, scPattern);
		}

		return scPattern;

	}

	/**
	 * Check for User Input in case of Exit ----------------
	 * 
	 * @param scanner
	 * @param UserResponse
	 *             - User Promptt Response-----------
	 */
	private void checkForExit(Scanner scanner, Object UserResponse)
	{
		if (UserResponse.toString().equals("Q"))
		{
			scanner.close();
			System.out.println("Exit!");
			System.exit(0);
		}

	}

	/**
	 * ---------------------------------
	 * Generate Scrip Information
	 * 
	 * @param scanner
	 *             --------------------------------
	 */
	private void generateScInfo(Scanner scanner)
	{
		if (scRoot.getscCode() != null)
		{
			GeneralMessage msgGen = new GeneralMessage("SCINFO", new Object[]
			{ scRoot.getscCode(), scRoot.getscSector()
			});
			String Msg = msgFormatter.generate_message_snippet(msgGen);
			System.out.print(Msg);
		}

	}
}
