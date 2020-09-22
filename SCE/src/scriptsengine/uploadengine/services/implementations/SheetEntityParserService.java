package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import modelframework.implementations.DependantObject;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.pojos.OB_Scrip_General;
import scriptsengine.uploadengine.JAXB.definitions.FieldsMetadata;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.ISheetEntityParser;

/**
 * Parses the Sheet(s) of Workbook Sheet Into respective Pojos and returns the entities list
 *
 */
@SuppressWarnings("rawtypes")
@Service("SheetEntityParserService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SheetEntityParserService implements ISheetEntityParser
{
	@Autowired
	private ScripSheetMetadataService	scMdtSrv;

	@Autowired
	private FrameworkManager			FWmgr;

	@Autowired
	private MessagesFormatter		msgFormatter;

	private SheetMetadata			sheetMdt;

	private Object_Info				objInfo;

	@SuppressWarnings(
	{ "unchecked", "static-access", "rawtypes"
	})
	@Override
	public SheetEntities getEntitiesfromSheet(XSSFSheet sheet)
	          throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_General
	{

		SheetEntities shEntities = new SheetEntities<>();
		shEntities.setSheetName(sheet.getSheetName());

		if (sheet != null)
		{
			if (scMdtSrv != null && FWmgr != null)
			{
				this.sheetMdt = scMdtSrv.getSheetMdtbySheetName(sheet.getSheetName());

				// Get Object Information from Object Factory
				this.objInfo = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(sheetMdt.getBobjName());
				// Get the Class of the business object name from Object Info.
				Class obj_class = objInfo.getCurr_Obj_Class();
				/**
				 * Check if sheet is Base Sheet - If it is trigger Root Entity Creation
				 */
				if (sheetMdt.getBasesheet())
				{
					// Only Single Root Object expected to be returned
					if (!sheetMdt.getCollection())
					{
						Object rootObj = obj_class.newInstance();
						if (rootObj != null)
						{
							rootObj = getObjfromSheet(sheet, rootObj);
							if (rootObj instanceof OB_Scrip_General)
							{
								String Sector = ((OB_Scrip_General) rootObj).getscSector();
								((OB_Scrip_General) rootObj).setscSector(Sector.toUpperCase());
							}
							shEntities.getSheetEntityList().add(rootObj);
						}
					}

				}
				/**
				 * Not a base sheet - Normal Sheet
				 */
				else
				{
					// Only Single Dependant Object Expected
					if (!sheetMdt.getCollection())
					{
						Object depObj = obj_class.newInstance();
						if (depObj != null)
						{
							depObj = getObjfromSheet(sheet, depObj);
							shEntities.getSheetEntityList().add(depObj);
						}
					}
					// Multiple Dependant Objects Expected
					else
					{
						shEntities = getObjCollectionfromSheet(sheet);
					}

				}
			}
		}

		// TODO Auto-generated method stub
		return shEntities;
	}

	/**
	 * Return Single Object from Sheet
	 * 
	 * @param sheet
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws EX_General
	 */
	@SuppressWarnings("deprecation")
	private Object getObjfromSheet(XSSFSheet sheet, Object iObj)
	          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_General
	{
		Object obj = null;
		double decimalval = 0;
		int intval = 0;

		if (iObj != null)
		{
			obj = iObj;
			ArrayList<FieldsMetadata> fldsMdt = sheetMdt.getFieldsDetails();
			if (fldsMdt != null)
			{
				if (fldsMdt.size() > 0)
				{
					// Get iterator to all the rows in current sheet
					Iterator<Row> rowIterator = sheet.iterator();
					while (rowIterator.hasNext())
					{
						Row row = rowIterator.next();
						/** ***************Operations for Each New Row *************************/
						boolean cellone = true;
						FieldsMetadata fldMdt = null;
						Method setter = null;
						/** ***************Operations for Each New Row *************************/
						// For each row, iterate through each columns
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext())
						{
							Cell cell = cellIterator.next();
							switch (cell.getCellType())
							{

								// Column Name - Description
								case Cell.CELL_TYPE_STRING:
									String cellStrValue = cell.getStringCellValue();
									if (cellone == true && cellStrValue != null)
									{
										/**
										 * Reading only 1st Column as Description of the field to be mapped
										 * and not otherwise - 2nd column in same row can be string but it
										 * should
										 * be value
										 */

										/**
										 * Get Field meatdata to check for Bobj Field Name, Key and Mandatory
										 * Condition
										 */
										fldMdt = fldsMdt.stream().filter(x -> x.getSheetField().equals(cellStrValue)).findFirst().get();

										/**
										 * Get the Field Setter Method Corresponding to Sheet Field
										 */
										if (fldMdt != null)
										{
											setter = objInfo.Get_Setter_for_FieldName(fldMdt.getObjField());
										}

										cellone = false;
									}
									// Value is String not a Number Set in Bobj
									else
									{
										if (setter != null)
										{
											/**
											 * Issue Exception in case the field is defined as mandatory but
											 * value
											 * not maintained
											 */

											if (fldMdt.getKey() == true || fldMdt.getMandatory() == true)
											{
												if (cellStrValue == null)
												{
													/**
													 * Scrip already exists - Should Not allow to create
													 * further
													 */
													EX_General egen = new EX_General("ERRNOMANDTVALUE", new Object[]
													{ fldMdt.getSheetField(), sheet.getSheetName()
													});
													msgFormatter.generate_message_snippet(egen);
													throw egen;
												}
											}

											setter.invoke(obj, cellStrValue);
										}
									}
									break;
								// Value is a Number decide from Mdt if it is DEcimal or Int
								case Cell.CELL_TYPE_NUMERIC:
								{
									if (fldMdt.getPrecision_ON())
									{
										decimalval = cell.getNumericCellValue();
										if (setter != null)
										{
											/**
											 * Issue Exception in case the field is defined as mandatory but
											 * value
											 * not maintained
											 */

											if (fldMdt.getKey() == true || fldMdt.getMandatory() == true)
											{
												// If the value has to be non zero and it is zero
												if (decimalval == 0 && fldMdt.getNonZero() == true)
												{
													EX_General egen = new EX_General("ERRNOMANDTVALUE", new Object[]
													{ fldMdt.getSheetField(), sheet.getSheetName()
													});
													msgFormatter.generate_message_snippet(egen);
													throw egen;
												}
											}
											setter.invoke(obj, decimalval);
										}
									}
									else
									{
										intval = (int) cell.getNumericCellValue();
										if (setter != null)
										{
											/**
											 * Issue Exception in case the field is defined as mandatory but
											 * value
											 * not maintained
											 */

											if (fldMdt.getKey() == true || fldMdt.getMandatory() == true)
											{
												// If the value has to be non zero and it is zero
												if (intval == 0 && fldMdt.getNonZero() == true)
												{
													/**
													 * Scrip already exists - Should Not allow to create
													 * further
													 */
													EX_General egen = new EX_General("ERRNOMANDTVALUE", new Object[]
													{ fldMdt.getSheetField(), sheet.getSheetName()
													});
													msgFormatter.generate_message_snippet(egen);
													throw egen;
												}
											}
											setter.invoke(obj, intval);
										}
									}
									break;

								}

							}// Switch Brace

						}
					}
				}
			}
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	private SheetEntities getObjCollectionfromSheet(XSSFSheet sheet)
	          throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_General
	{
		SheetEntities shEntities = new SheetEntities();
		ArrayList<DependantObject> objList = getObjListfromSheet(sheet);
		if (objList != null)
		{
			if (objList.size() > 0)
			{
				objList = populateEntSheet(objList, sheet);
			}
		}
		shEntities.setSheetName(sheet.getSheetName());
		shEntities.setSheetEntityList(objList);

		return shEntities;
	}

	@SuppressWarnings(
	{ "unchecked"
	})
	private <T> ArrayList<T> getObjListfromSheet(XSSFSheet sheet) throws InstantiationException, IllegalAccessException
	{
		ArrayList<T> objList = new ArrayList<T>();

		if (sheet != null)
		{
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = sheet.iterator();
			// Get Top Row to Create as many dependent Objects as Columns
			Row rowTop = rowIterator.next();
			if (rowTop != null)
			{
				// Get the Sheet Metadata to determine Object Type to be instantiated
				// Already there in Class

				/** ***************Operations for Each New Row *************************/
				boolean cellone = true;
				/** ***************Operations for Each New Row *************************/
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = rowTop.cellIterator();
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();

					/**
					 * 2nd Column Onwards that holds the Entity
					 * Create Corresponding Entities and add to List
					 */
					if (cellone == false && cell != null)
					{
						// Get the Class of the business object name from Object Info.
						Class obj_class = objInfo.getCurr_Obj_Class();
						Object depObj = obj_class.newInstance();
						// DependantObject depObj = (DependantObject) obj_class.newInstance();
						if (depObj != null)
						{
							objList.add((T) depObj);
						}

					}
					cellone = false;
				}

			}
		}

		return objList;
	}

	private <T> ArrayList<T> populateEntSheet(ArrayList<T> objList, XSSFSheet sheet)
	          throws EX_General, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{

		double decimalval = 0;
		int intval = 0;
		int rowidx = 0;
		ArrayList<T> objListPopultaed = new ArrayList<T>();

		ArrayList<FieldsMetadata> fldsMdt = sheetMdt.getFieldsDetails();
		if (fldsMdt != null)
		{
			if (fldsMdt.size() > 0)
			{
				// Get iterator to all the rows in current sheet
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext())
				{
					rowidx++;
					Row row = rowIterator.next();
					/** ***************Operations for Each New Row *************************/
					boolean cellone = true;
					int colidx = 0;
					FieldsMetadata fldMdt = null;
					Method setter = null;
					/** ***************Operations for Each New Row *************************/

					// For each row, iterate through each columns
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext())
					{
						Cell cell = cellIterator.next();
						switch (cell.getCellType())
						{
							// Column Name - Description
							case Cell.CELL_TYPE_STRING:
								String cellStrValue = cell.getStringCellValue();
								if (cellone == true && cellStrValue != null)
								{
									/**
									 * Reading only 1st Column as Description of the field to be mapped
									 * and not otherwise - 2nd column in same row can be string but it should
									 * be value
									 */

									/**
									 * Get Field meatdata to check for Bobj Field Name, Key and Mandatory
									 * Condition
									 */
									fldMdt = fldsMdt.stream().filter(x -> x.getSheetField().equals(cellStrValue)).findFirst().get();

									/**
									 * Get the Field Setter Method Corresponding to Sheet Field
									 */
									if (fldMdt != null)
									{
										setter = objInfo.Get_Setter_for_FieldName(fldMdt.getObjField());
									}

									cellone = false;

								}

								// Value is String not a Number Set in Bobj
								else
								{
									if (setter != null)
									{
										/**
										 * Issue Exception in case the field is defined as mandatory but value
										 * not maintained
										 */

										if (fldMdt.getKey() == true || fldMdt.getMandatory() == true)
										{
											if (cellStrValue == null)
											{
												/**
												 * Scrip already exists - Should Not allow to create further
												 */
												EX_General egen = new EX_General("ERRNOMANDTVALUEMULTI", new Object[]
												{ fldMdt.getSheetField(), rowidx, sheet.getSheetName()
												});
												msgFormatter.generate_message_snippet(egen);
												throw egen;
											}
										}
										// Get Object Entity at right Index from Obj List
										Object obj = objList.get(colidx);
										if (obj != null)
										{
											setter.invoke(obj, cellStrValue);
										}
										colidx++; // Increment Column Counter to get next entity next time -
										          // in the
										          // next iteration of columns for same row
									}
								}
								break;

							// Value is a Number decide from Mdt if it is DEcimal or Int
							case Cell.CELL_TYPE_NUMERIC:
							{
								if (fldMdt.getPrecision_ON())
								{
									decimalval = cell.getNumericCellValue();
									if (setter != null)
									{
										/**
										 * Issue Exception in case the field is defined as mandatory but value
										 * not maintained
										 */

										if (fldMdt.getKey() == true || fldMdt.getMandatory() == true)
										{
											// If the value has to be non zero and it is zero
											if (decimalval == 0 && fldMdt.getNonZero() == true)
											{
												/**
												 * Scrip already exists - Should Not allow to create further
												 */
												EX_General egen = new EX_General("ERRNOMANDTVALUEMULTI", new Object[]
												{ fldMdt.getSheetField(), rowidx, sheet.getSheetName()
												});
												msgFormatter.generate_message_snippet(egen);
												throw egen;
											}
										}
										// Get Object Entity at right Index from Obj List
										Object obj = objList.get(colidx);
										if (obj != null)
										{
											setter.invoke(obj, decimalval);
										}
										colidx++; // Increment Column Counter to get next entity next time -
										          // in the
										          // next iteration of columns for same row
									}
								}
								else
								{
									intval = (int) cell.getNumericCellValue();
									if (setter != null)
									{
										/**
										 * Issue Exception in case the field is defined as mandatory but value
										 * not maintained
										 */

										if (fldMdt.getKey() == true || fldMdt.getMandatory() == true)
										{
											// If the value has to be non zero and it is zero
											if (intval == 0 && fldMdt.getNonZero() == true)
											{
												/**
												 * Scrip already exists - Should Not allow to create further
												 */
												EX_General egen = new EX_General("ERRNOMANDTVALUEMULTI", new Object[]
												{ fldMdt.getSheetField(), rowidx, sheet.getSheetName()
												});
												msgFormatter.generate_message_snippet(egen);
												throw egen;
											}
										}
										// Get Object Entity at right Index from Obj List
										Object obj = objList.get(colidx);
										if (obj != null)
										{
											setter.invoke(obj, intval);
										}
										colidx++; // Increment Column Counter to get next entity next time -
										          // in the
										          // next iteration of columns for same row
									}
								}
								break;

							}

						} // Switch Brace

					}

				}

			}
		}
		objListPopultaed = objList;
		return objListPopultaed;
	}

}
