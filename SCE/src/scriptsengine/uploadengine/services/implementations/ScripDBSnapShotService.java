package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.MessagesFormatter;
import modelframework.types.TY_NameValue;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.ScripDBSnaphot;
import scriptsengine.uploadengine.definitions.SheetDBSnapShot;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.IScripDBSnapShot;
import scriptsengine.uploadengine.services.interfaces.IScripDataService;
import scriptsengine.uploadengine.validations.implementations.ScripExistsService;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripDBSnapShotService implements IScripDBSnapShot
{

	@Autowired
	private ScripExistsService		scExSrv;

	@Autowired
	private IScripDataService		scDataSrv;

	@Autowired
	private ScripSheetMetadataService	shMdtSrv;

	@Autowired
	private MessagesFormatter		msgFormatter;

	private ScripDBSnaphot			scDBsnapshot;

	/**
	 * @return the scDBsnapshot
	 */
	@Override
	public ScripDBSnaphot getScDBsnapshot()
	{
		return scDBsnapshot;
	}

	@Override
	public ScripDBSnaphot getScripDBSnapshot(String scCode) throws EX_General
	{

		this.scDBsnapshot = new ScripDBSnaphot();

		if (scExSrv != null && scCode != null)
		{
			// 1 First get the Root Scrip Entity
			scDBsnapshot.setScRoot(genRootScrip(scCode));

			// 2. Process Individual Sheets
			processSheets();

		}

		return scDBsnapshot;
	}

	private void processSheets() throws EX_General
	{
		String relname = null;
		if (scDataSrv != null)
		{
			if (scDataSrv.getWBMetaData() != null)
			{
				for ( SheetMetadata shMdt : scDataSrv.getWBMetaData().getSheetsMetadata() )
				{
					if (!shMdt.getBasesheet())
					{
						// Single Cardinality Relations
						if (!shMdt.getCollection())
						{
							// If Required for Create: Data has to be maintained
							if (shMdt.getRequiredCreate())
							{
								SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), true, null);
								scDBsnapshot.getSheetsDBSS().add(shSnapshot);
							}
							// In case not required for Create - Get related entities and see if data maintained
							else
							{
								// Get the relation name
								if (this.shMdtSrv != null)
								{
									relname = this.shMdtSrv.getRelationNameforSheetName(shMdt.getSheetName());
									if (relname != null)
									{
										try
										{
											ArrayList<DependantObject> depObjs = scDBsnapshot.getScRoot().getRelatedEntities(relname);
											if (depObjs.size() > 0) // Dependant entities Exist
											{
												SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), true, null);
												scDBsnapshot.getSheetsDBSS().add(shSnapshot);
											}
											else // No depdendant Entities Maintained
											{
												SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), false, null);
												scDBsnapshot.getSheetsDBSS().add(shSnapshot);
											}

										}
										catch (EX_InvalidRelationException e)
										{
											EX_General egen = new EX_General("ERRRELSCRIP", new Object[]
											{ relname, scDBsnapshot.getScRoot().getscCode(), shMdt.getSheetName(), e.getMessage()
											});
											msgFormatter.generate_message_snippet(egen);
											throw egen;
										}
									}
								}
							}

						}
						else // Multiple Cardinality Relations
						{
							// Get Integer Keys for Sheet
							ArrayList<String> intKeys = this.shMdtSrv.getIntKeyfieldsforSheet(shMdt.getSheetName());
							if (intKeys != null)
							{
								if (intKeys.size() == 1)
								{
									int value = GetKeyMaxfromDB(intKeys.get(0),
									          this.shMdtSrv.getTableNameforSheetName(shMdt.getSheetName()));
									if (value > 0)
									{
										ArrayList<TY_NameValue> namevals = new ArrayList<TY_NameValue>();
										namevals.add(new TY_NameValue(intKeys.get(0), value));
										SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), true, namevals);
										scDBsnapshot.getSheetsDBSS().add(shSnapshot);
									}
									else
									{
										SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), false, null);
										scDBsnapshot.getSheetsDBSS().add(shSnapshot);
									}
								}
								else if (intKeys.size() > 1)
								{
									ArrayList<TY_NameValue> namevals = GetKeysMaxfromDB(intKeys,
									          shMdtSrv.getTableNameforSheetName(shMdt.getSheetName()));
									if (namevals != null)
									{
										SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), true, namevals);
										scDBsnapshot.getSheetsDBSS().add(shSnapshot);
									}
									else
									{
										SheetDBSnapShot shSnapshot = new SheetDBSnapShot(shMdt.getSheetName(), false, null);
										scDBsnapshot.getSheetsDBSS().add(shSnapshot);
									}
								}
							}

						}
					}
				}
			}
		}
	}

	private OB_Scrip_General genRootScrip(String scCode) throws EX_General

	{
		OB_Scrip_General scripRoot = null;
		if (scExSrv != null)
		{
			try
			{
				scripRoot = scExSrv.getScripRootbyCode(scCode);

			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException
			          | SecurityException | EX_InvalidObjectException | EX_NotRootException | SQLException | EX_NullParamsException
			          | EX_ParamCountMismatchException | EX_ParamInitializationException | EX_InvalidRelationException e)
			{
				EX_General egen = new EX_General("SCRIPEXISTERROR", new Object[]
				{ e.getMessage()
				});
				msgFormatter.generate_message_snippet(egen);
				throw egen;
			}
		}
		return scripRoot;
	}

	private int GetKeyMaxfromDB(String maxColname, String tableName) throws EX_General
	{
		int res = 0;
		String query = "Select MAX(" + maxColname + ") from " + tableName + " WHERE " + shMdtSrv.getBaseSheetKeyObjField() + " = ?";

		try
		{
			PreparedStatement stmt = FrameworkManager.getConnectionPool().getConnection().prepareStatement(query);
			if (stmt != null)
			{
				stmt.setString(1, this.scDBsnapshot.getScRoot().getscCode());
				ResultSet rs = stmt.executeQuery();
				if (rs.next())
				{

					res = rs.getInt(1);
				}
			}
		}
		catch (SQLException e)
		{
			EX_General egen = new EX_General("ERRSQLSTMT", new Object[]
			{ e.getMessage()
			});
			msgFormatter.generate_message_snippet(egen);
			throw egen;
		}

		return res;
	}

	private ArrayList<TY_NameValue> GetKeysMaxfromDB(ArrayList<String> keys, String tablename) throws EX_General
	{
		int res = 0;
		String query = null;
		PreparedStatement stmt = null;
		ArrayList<Integer> IntVals = new ArrayList<Integer>();
		ArrayList<TY_NameValue> nameVals = new ArrayList<TY_NameValue>();
		int itr = 0;

		for ( String string : keys )
		{

			if (itr > 0)
			{
				// split Query at "from"
				String[] splitq = query.split("(?<=from)");
				int arrsize = splitq.length;

				query = "SELECT MAX (" + string + ") FROM " + splitq[arrsize - 1] + " AND " + keys.get(itr - 1) + " = ?";

				try
				{
					stmt = FrameworkManager.getConnectionPool().getConnection().prepareStatement(query);
					if (stmt != null)
					{
						stmt.setString(1, this.scDBsnapshot.getScRoot().getscCode());
						for ( int x = 1; x <= IntVals.size(); x++ )
						{
							stmt.setInt(x + 1, IntVals.get(itr - 1));
						}
						ResultSet rs = stmt.executeQuery();
						if (rs.next())
						{
							res = rs.getInt(1);
							IntVals.add(res);
						}
					}
					stmt.close();
				}
				catch (SQLException e)
				{
					EX_General egen = new EX_General("ERRSQLSTMT", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}
			else
			{
				query = "Select MAX(" + string + ") from " + tablename + " WHERE " + shMdtSrv.getBaseSheetKeyObjField() + " = ?";

				try
				{
					stmt = FrameworkManager.getConnectionPool().getConnection().prepareStatement(query);
					if (stmt != null)
					{
						stmt.setString(1, this.scDBsnapshot.getScRoot().getscCode());
						ResultSet rs = stmt.executeQuery();
						if (rs.next())
						{
							res = rs.getInt(1);
							IntVals.add(res);
						}
					}
					stmt.close();
				}
				catch (SQLException e)
				{
					EX_General egen = new EX_General("ERRSQLSTMT", new Object[]
					{ e.getMessage()
					});
					msgFormatter.generate_message_snippet(egen);
					throw egen;
				}
			}

			itr++;

		}

		if (IntVals.size() > 0 && keys.size() > 0)
		{
			int i = 0;
			for ( Integer intVal : IntVals )
			{

				TY_NameValue nameval = new TY_NameValue(keys.get(i), intVal);
				nameVals.add(nameval);
				i++;
			}
		}

		return nameVals;

	}

}
