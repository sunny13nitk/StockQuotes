package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.types.TY_Filter;
import modelframework.types.TY_NameValue;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.definitions.SheetEntFilter;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.services.interfaces.IDBQueryFilter;

/**
 * Data Fetcher Srvice - to get data from DB
 *
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DBQueryFilterService implements IDBQueryFilter
{
	@Autowired
	private ScripSheetMetadataService	scMdtSrv;

	private String					relName;

	private String					condn;

	private ArrayList<TY_NameValue>	params;

	@SuppressWarnings("rawtypes")
	private SheetEntities			shEntities;

	private TY_Filter				filter;

	private boolean				nofilter;

	private ArrayList<String>		keys;

	private final String			conOR			= " OR ";

	private final String			conAND			= " AND ";

	private final String			conbracketStart	= " ( ";

	private final String			conbracketEnd		= " ) ";

	private final String			coneqQ			= " = ?";

	// Prepare the filter
	private void prepareQuery() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		condn = "";
		if (scMdtSrv != null)
		{
			if (shEntities != null)
			{
				if (shEntities.getSheetEntityList() != null)
				{
					if (shEntities.getSheetEntityList().size() > 0)
					{
						// First get the keys for Sheet from Sheet Metadata
						this.keys = scMdtSrv.getKeyfieldsforSheet(shEntities.getSheetName());
						// No key so related entities cannot be fetched with a filter
						if (keys.size() == 0)
						{
							nofilter = true;
						}
						// Keys Exists - Filter will be created
						else
						{
							params = new ArrayList<TY_NameValue>();
							Object_Info depobjinfo = FrameworkManager.getObjectsInfoFactory()
							          .Get_ObjectInfo_byClass(shEntities.getSheetEntityList().get(0).getClass());

							// Looping through sheet Entities Entities List
							for ( int i = 1; i <= shEntities.getSheetEntityList().size(); i++ )
							{
								// e.g. " City = ? OR City = ? OR City = ?"
								if (keys.size() == 1)
								{
									if (i >= 2 && i <= shEntities.getSheetEntityList().size())
									{
										condn += conOR;
									}
									condn += keys.get(0) + coneqQ;

									Method mGetter = depobjinfo.Get_Getter_for_FieldName(keys.get(0));
									if (mGetter != null)
									{
										// Get the value for the parameter from POJO
										Object value = mGetter.invoke(shEntities.getSheetEntityList().get(i - 1), new Object[] {});
										if (value != null)
										{
											params.add(new TY_NameValue(keys.get(0), value));
										}
									}

								}
								else if (keys.size() > 1)
								{
									// Looping through Keys
									for ( int x = 1; x <= keys.size(); x++ )
									{
										if (x == 1)
										{
											condn += conbracketStart;
										}
										if (x >= 2 && x <= keys.size())
										{
											condn += conAND;
										}
										condn += keys.get(x - 1) + coneqQ;

										if (x == keys.size())
										{
											condn += conbracketEnd;
										}

										Method mGetter = depobjinfo.Get_Getter_for_FieldName(keys.get(x - 1));
										if (mGetter != null)
										{
											// Get the value for the parameter from POJO
											Object value = mGetter.invoke(shEntities.getSheetEntityList().get(i - 1), new Object[] {});
											if (value != null)
											{
												params.add(new TY_NameValue(keys.get(x - 1), value));
											}
										}

									}

									if (i < shEntities.getSheetEntityList().size())
									{
										// Add an OR on complete pass of Keys once untill penultimate element
										// in sheet entity List
										condn += conOR;
									}

								}
							}

						}

					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SheetEntFilter getEntFilterforSheetEntities(OB_Scrip_General scRoot, SheetEntities shEntities)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		SheetEntFilter shEntFilter = null;
		if (scRoot != null && shEntities != null)
		{
			if (scMdtSrv != null)
			{
				this.relName = scMdtSrv.getRelationNameforSheetName(shEntities.getSheetName());
				if (relName != null)
				{
					this.shEntities = shEntities;
					prepareQuery();

					// No Filter
					if (this.nofilter)
					{
						shEntFilter = new SheetEntFilter(shEntities.getSheetName(), relName, null, keys);
					}
					else // Filter Exists
					{

						this.filter = new TY_Filter(condn, params);
						shEntFilter = new SheetEntFilter(shEntities.getSheetName(), relName, filter, keys);
					}
				}

			}
		}

		return shEntFilter;
	}

}
