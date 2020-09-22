package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.InvocationTargetException;
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
import scriptsengine.pojos.OB_Scrip_FinishedProduct;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetEntities;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.services.interfaces.ISheetEntityParser;

@Service("FinishedProductSheetEntityParser")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FinishedProductSheetEntityParser implements ISheetEntityParser
{
	@Autowired
	private ScripSheetMetadataService	scMdtSrv;
	@Autowired
	private FrameworkManager			FWmgr;
	private Object_Info				objInfo;
	private SheetMetadata			sheetMdt;

	@SuppressWarnings(
	{ "unchecked", "rawtypes", "rawtypes", "static-access"
	})
	@Override
	public SheetEntities getEntitiesfromSheet(XSSFSheet sheet)
	          throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EX_General
	{
		SheetEntities shEntities = new SheetEntities();

		SheetEntities shEntitiesoneD = new SheetEntities();
		shEntities.setSheetName(sheet.getSheetName());
		String fpCatg = null;

		double decimalval;

		if (sheet != null)
		{
			this.sheetMdt = scMdtSrv.getSheetMdtbySheetName(sheet.getSheetName());
			if (this.sheetMdt != null)
			{
				// Get Object Information from Object Factory
				this.objInfo = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(sheetMdt.getBobjName());
				// Get the Class of the business object name from Object Info.
				Class obj_class = objInfo.getCurr_Obj_Class();
				if (obj_class != null)
				{
					// Get iterator to all the rows in current sheet
					Iterator<Row> rowIterator = sheet.iterator();
					int rownum = 0;
					while (rowIterator.hasNext())
					{

						Row row = rowIterator.next();
						rownum++;
						if (rownum == 1)
						{
							int colnumrow1 = 0;

							// For each row, iterate through each columns
							Iterator<Cell> cellIterator = row.cellIterator();
							while (cellIterator.hasNext())
							{
								Cell cell = cellIterator.next();
								colnumrow1++;
								if (colnumrow1 > 1)
								{
									Object depObj = obj_class.newInstance();
									if (depObj != null)
									{
										if (depObj instanceof OB_Scrip_FinishedProduct)
										{
											int intval = (int) cell.getNumericCellValue();
											((OB_Scrip_FinishedProduct) depObj).setYear(intval);
											shEntitiesoneD.getSheetEntityList().add(depObj);
										}
									}
								}
							}
						}

						if (rownum > 2)
						{
							int colnumrow = 0;

							// For each row, iterate through each columns
							Iterator<Cell> cellIterator = row.cellIterator();
							while (cellIterator.hasNext())
							{
								Cell cell = cellIterator.next();
								colnumrow++;

								if (colnumrow == 1)
								{
									fpCatg = cell.getStringCellValue();
								}

								if (colnumrow > 1)
								{
									decimalval = cell.getNumericCellValue();
									OB_Scrip_FinishedProduct fpEnt = (OB_Scrip_FinishedProduct) shEntitiesoneD.getSheetEntityList()
									          .get(colnumrow - 2);
									if (fpEnt != null && decimalval != 0)
									{
										Object depObj = obj_class.newInstance();
										if (depObj != null)
										{
											if (depObj instanceof OB_Scrip_FinishedProduct)
											{

												((OB_Scrip_FinishedProduct) depObj).setYear(fpEnt.getYear());
												((OB_Scrip_FinishedProduct) depObj).setFPCatg(fpCatg);
												((OB_Scrip_FinishedProduct) depObj).setPerSales(decimalval);

												shEntities.getSheetEntityList().add(depObj);
											}
										}

									}
								}
							}
						}

					}

				}
			}
		}

		return shEntities;
	}

}
