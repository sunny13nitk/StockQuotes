package mypackage.Executions;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.Metadata.definitions.SheetFieldsMetadata;
import scriptsengine.uploadengineSC.Metadata.services.implementations.SCWBMetadataSrv;
import scriptsengine.uploadengineSC.tools.definitions.FieldsParserProp;
import scriptsengine.uploadengineSC.tools.definitions.FldVals;
import scriptsengine.uploadengineSC.tools.interfaces.ISheetRowParserSrv;

@Service("SheetRowParserSrvExe")
public class SheetRowParserSrvExe implements IExecutable
{

	@Autowired
	private SCWBMetadataSrv wbMdtSrv;

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String sheetName = "Analysis";
			String fieldName = "Operating Profit Margin (OPM%)";

			ISheetRowParserSrv shRPSrv = appctxt.getBean(ISheetRowParserSrv.class);
			if (shRPSrv != null)
			{
				FieldsParserProp parserProp = new FieldsParserProp(fieldName, 2, 11);
				FldVals fldVals = shRPSrv.getFldValsbyWbPathandSheetName("C://WBConfig//Avanti Feeds.xlsx", sheetName, parserProp);
				if (fldVals != null)
				{
					System.out.println("Field Name -  " + fldVals.getFieldName());
					if (fldVals.getFieldVals() != null)
					{
						if (fldVals.getFieldVals().size() > 0)
						{
							if (wbMdtSrv != null)
							{
								SheetFieldsMetadata fldMdt = wbMdtSrv.getFieldMetadata(sheetName, fieldName);

								switch (fldMdt.getDataType())
								{
									case Numerical:

										ArrayList<Integer> intList = new ArrayList<Integer>();

										for ( Object obj : fldVals.getFieldVals() )
										{
											intList.add((int) obj);
										}

										for ( Integer integer : intList )
										{
											System.out.println(integer);
										}

										break;

									case Decimal:

										ArrayList<Double> doubleList = new ArrayList<Double>();

										for ( Object obj : fldVals.getFieldVals() )
										{
											doubleList.add((double) obj);
										}

										for ( Double double1 : doubleList )
										{
											System.out.println(double1);
										}
										break;

								}
							}
						}
					}
				}

			}
		}

	}

}
