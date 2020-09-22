package scriptsengine.uploadengineSC.scripSheetServices.implementations;

import java.lang.reflect.InvocationTargetException;
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
import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import modelframework.types.TY_NameValue;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.definitions.BaseSheetKey;
import scriptsengine.uploadengineSC.Metadata.services.implementations.SCWBMetadataSrv;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCExistsDB_Srv;

@Service("SCExistsDB_Srv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SCExistsDB_Srv implements ISCExistsDB_Srv
{
	@Autowired
	private MessagesFormatter	msgFormatter;

	@Autowired
	private SCWBMetadataSrv		scMdtSrv;

	private String				rootObjName;
	private final String		descField	= "SCName";

	@Override
	public boolean Is_ScripExisting_DB(String scCode) throws EX_General
	{
		boolean scExists = false;

		if (scCode != null)
		{
			if (this.Get_ScripExisting_DB(scCode) != null)
			{
				scExists = true;
			}
		}

		return scExists;
	}

	@Override
	public EN_SC_General Get_ScripExisting_DB(String scCode) throws EX_General
	{
		EN_SC_General scRoot = null;

		if (scCode != null)
		{
			scRoot = this.Get_scRoot(scCode);
		}

		return scRoot;
	}

	@Override
	public boolean Is_ScripExisting_DB_DescSW(String scDesc) throws EX_General
	{
		boolean scExists = false;

		if (scDesc != null)
		{
			if (this.Get_ScripExisting_DB_DescSW(scDesc) != null)
			{
				scExists = true;
			}
		}

		return scExists;
	}

	@Override
	public EN_SC_General Get_ScripExisting_DB_DescSW(String scDesc) throws EX_General
	{
		EN_SC_General scRoot = null;

		if (scDesc != null)
		{
			scRoot = this.Get_scRoot_Desc(scDesc);
		}

		return scRoot;
	}

	/**
	 * ---------------- PRIVATE METHODS --------------------------------
	 */

	/**
	 * Actual Query to Get Scrip Root from Scrip Code
	 * 
	 * @param scCode
	 * @return
	 * @throws EX_General
	 */
	private EN_SC_General Get_scRoot(String scCode) throws EX_General
	{
		EN_SC_General scRoot = null;

		if (scCode != null && scMdtSrv != null)
		{
			BaseSheetKey bshKey = scMdtSrv.getBaseSheetKey();
			if (bshKey != null)
			{
				this.rootObjName = scMdtSrv.getMetadataforSheet(bshKey.getBaseSheetName()).getBobjName();
				if (rootObjName != null)
				{
					try
					{
						IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(rootObjName);
						if (qs != null)
						{
							ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

							params.add(new TY_NameValue(bshKey.getKeyFieldName(), scCode));
							String condn = " " + bshKey.getKeyFieldName() + " = ? ";

							ArrayList<EN_SC_General> scrips = qs.executeQuery(condn, params);
							if (scrips.size() > 0)
							{
								scRoot = scrips.get(0);
							}

						}
					}
					catch (EX_InvalidObjectException | EX_NotRootException | SQLException | IllegalAccessException | IllegalArgumentException
					          | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException
					          | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
					          | EX_InvalidRelationException e)
					{
						EX_General egen = new EX_General("INVALIDROOT", new Object[]
						{ rootObjName, e.getMessage(), scCode
						});
						msgFormatter.generate_message_snippet(egen);
						throw egen;
					}
				}
			}
		}

		return scRoot;
	}

	/**
	 * Get Scrip root Object by Description Starting With
	 * 
	 * @param scDesc
	 *             - Scrip Description Starting With
	 * @return - Scrip Root
	 * @throws EX_General
	 */
	private EN_SC_General Get_scRoot_Desc(String scDesc) throws EX_General
	{
		EN_SC_General scRoot = null;

		if (scDesc != null && scMdtSrv != null)
		{
			BaseSheetKey bshKey = scMdtSrv.getBaseSheetKey();
			if (bshKey != null)
			{
				this.rootObjName = scMdtSrv.getMetadataforSheet(bshKey.getBaseSheetName()).getBobjName();
				if (rootObjName != null)
				{
					try
					{
						IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(rootObjName);
						if (qs != null)
						{
							ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();
							String pattern = scDesc + "%";

							params.add(new TY_NameValue(descField, pattern));
							String condn = " " + descField + " LIKE  ? ";

							ArrayList<EN_SC_General> scrips = qs.executeQuery(condn, params);
							if (scrips.size() > 0)
							{
								scRoot = scrips.get(0);
							}

						}
					}
					catch (EX_InvalidObjectException | EX_NotRootException | SQLException | IllegalAccessException | IllegalArgumentException
					          | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException
					          | EX_NullParamsException | EX_ParamCountMismatchException | EX_ParamInitializationException
					          | EX_InvalidRelationException e)
					{
						EX_General egen = new EX_General("INVALIDROOT", new Object[]
						{ rootObjName, e.getMessage(), scDesc
						});
						msgFormatter.generate_message_snippet(egen);
						throw egen;
					}
				}
			}
		}

		return scRoot;

	}

}
