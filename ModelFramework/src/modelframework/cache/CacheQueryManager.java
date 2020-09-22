package modelframework.cache;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import modelframework.definitions.KeyEntity;
import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.RootObject;
import modelframework.interfaces.IQueryService;
import modelframework.interfaces.IRelQueryService;
import modelframework.interfaces.ISimpleQueryService;
import modelframework.services.RelationsQueryService;
import modelframework.services.SimpleQueryService;

@Service("CacheQueryManager")
public class CacheQueryManager
{

	@Autowired
	private FrameworkManager		frameworkManager;
	private IQueryService		querySrv;
	private ISimpleQueryService	simpleQ;
	private IRelQueryService		relQ;

	/**
	 * @return the simpleQ
	 */
	public ISimpleQueryService getSimpleQ()
	{
		return simpleQ;
	}

	/**
	 * @return the relQ
	 */
	public IRelQueryService getRelQ()
	{
		return relQ;
	}

	public void initialize(IQueryService qs)

	{
		if (qs != null)
		{
			if (qs instanceof SimpleQueryService)
			{
				this.simpleQ = (ISimpleQueryService) qs;
				this.relQ = null;
			}

			else if (qs instanceof RelationsQueryService)
			{
				this.simpleQ = null;
				this.relQ = (IRelQueryService) qs;

			}

		}
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked"
	})
	@Cacheable(value = "searchResults", keyGenerator = "QuerykeyGenerator")
	public <T> ArrayList<T> getResultEntities() throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	          InstantiationException, NoSuchMethodException, SecurityException
	{
		ArrayList<T> result = new ArrayList<T>();
		System.out.println("Actually Hitting DB");
		ResultSet rs = prepare_executeQuery();
		if (rs != null)
		{
			while (rs.next())
			{
				if (simpleQ != null)
				{
					if (simpleQ.getRootObjInfo() != null)
					{
						Class obj_class = simpleQ.getRootObjInfo().getCurr_Obj_Class();
						if (obj_class != null)
						{
							RootObject test_instance = (RootObject) obj_class.newInstance();
							if (test_instance != null && test_instance instanceof RootObject)
							{
								simpleQ.syncronizeProperties(test_instance, rs);
								result.add((T) test_instance);
							}
						}
					}
				}
			}

			rs.close();
		}
		return result;
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes", "static-access"
	})
	@Cacheable(value = "searchResults", keyGenerator = "QuerykeyGenerator")
	public <T> ArrayList<KeyEntity<T>> getResults() throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	          InstantiationException, NoSuchMethodException, SecurityException
	{
		ArrayList<KeyEntity<T>> result = new ArrayList<KeyEntity<T>>();
		System.out.println("Actually Hitting DB");
		String pkeyType = null;
		ResultSet rs = prepare_executeQuery();
		if (rs != null)
		{
			while (rs.next())
			{
				KeyEntity<T> keyEntity = new KeyEntity<T>();

				if (simpleQ != null)
				{
					if (simpleQ.getRootObjInfo() != null)
					{
						Class obj_class = simpleQ.getRootObjInfo().getCurr_Obj_Class();
						if (obj_class != null)
						{
							RootObject test_instance = (RootObject) obj_class.newInstance();
							if (test_instance != null && test_instance instanceof RootObject)
							{

								/**
								 * First get the Primary Key from Result Set
								 */

								if (simpleQ.getRootObjInfo().getRoot_Metadata().getAutokey() == false
								          && simpleQ.getRootObjInfo().getRoot_Metadata().getKeyObjField() != null)
								{
									pkeyType = "String";
								}
								else if (simpleQ.getRootObjInfo().getRoot_Metadata().getAutokey() == true
								          && simpleQ.getRootObjInfo().getRoot_Metadata().getKeyTableField() != null)
								{
									pkeyType = "Integer";
								}

								/*
								 * Substitute key in Keyentity
								 */
								try
								{
									if (pkeyType == "String")
									{
										keyEntity.setKey(rs.getString(this.simpleQ.getRootObjInfo().getPKey_Name()));
									}
									else if (pkeyType == "Integer")
									{
										keyEntity.setKey(rs.getInt(this.simpleQ.getRootObjInfo().getPKey_Name()));
									}
								}
								catch (Exception e)
								{
									// Do Nothing
								}

								/**
								 * Create POJO from ResultSet
								 */
								simpleQ.syncronizeProperties(test_instance, rs);

								/*
								 * Substitute POJO in Keyentity
								 */
								keyEntity.setPojoEntity((T) test_instance);
								/*
								 * Push to Result Finally
								 */
								result.add(keyEntity);
							}
						}
					}
				}

				else if (relQ != null)
				{
					if (relQ.getRelDetails() != null)
					{
						Object_Info depobjInfo = frameworkManager.getObjectsInfoFactory()
						          .Get_ObjectInfo_byName(relQ.getRelDetails().getDepobjname());
						if (depobjInfo != null)
						{
							Class obj_class = depobjInfo.getCurr_Obj_Class();
							if (obj_class != null)
							{
								DependantObject test_instance = (DependantObject) obj_class.newInstance();
								if (test_instance != null && test_instance instanceof DependantObject)
								{

									/**
									 * First get the Primary Key from Result Set
									 */

									if (depobjInfo.getDep_Metadata().getAutokey() == false
									          && depobjInfo.getDep_Metadata().getKeyObjField() != null)
									{
										pkeyType = "String";
									}
									else if ((depobjInfo.getDep_Metadata().getAutokey() == true
									          && depobjInfo.getDep_Metadata().getKeyTableField() != null))
									{
										pkeyType = "Integer";
									}

									/*
									 * Substitute key in Keyentity
									 */
									try
									{
										if (pkeyType == "String")
										{
											keyEntity.setKey(rs.getString(depobjInfo.getPKey_Name()));
										}
										else if (pkeyType == "Integer")
										{
											keyEntity.setKey(rs.getInt(depobjInfo.getPKey_Name()));
										}
									}
									catch (Exception e)
									{
										// Do Nothing
									}

									/**
									 * Create POJO from ResultSet
									 */
									relQ.syncronizeProperties(test_instance, rs);

									/*
									 * Substitute POJO in Keyentity
									 */
									keyEntity.setPojoEntity((T) test_instance);
									/*
									 * Push to Result Finally
									 */
									result.add(keyEntity);
								}
							}
						}
					}
				}
			}

			rs.close();
		}
		return result;
	}

	private ResultSet prepare_executeQuery() throws SQLException
	{

		ResultSet rs = null;
		if (simpleQ != null)
		{
			/**
			 * Generate the Prepared Statement after Query Condition/Params
			 * updation
			 */

			simpleQ.createPreparedStatement();

			/**
			 * Execute the Query to Get Result in a resultSet
			 */

			rs = simpleQ.getPrepStmt().executeQuery();

		}
		else if (relQ != null)
		{
			// First get the Filter mode to decide furhter course
			if (relQ.isFilterON())
			{
				// Filter Mode ON - Get Related entities with Filter
				rs = relQ.getpStmnt().executeQuery();
			}

			else
			{
				// Filter Mode OFF - Get Related entities without any filter
				relQ.generateQueryStr();
				relQ.generatePreparedStmt();
				rs = relQ.getpStmnt().executeQuery();
			}

		}

		return rs;

	}

}
