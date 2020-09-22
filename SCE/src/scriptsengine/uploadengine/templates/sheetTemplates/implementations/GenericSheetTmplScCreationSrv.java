package scriptsengine.uploadengine.templates.sheetTemplates.implementations;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scriptsengine.uploadengine.JAXB.definitions.FieldsMetadata;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.templates.sheetTemplates.interfaces.ISheetCreateTemplate;

/**
 * Generic Sheet Template Creation Service
 *
 */
@Service("GenericSheetTmplScCreationSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenericSheetTmplScCreationSrv implements ISheetCreateTemplate
{

	@SuppressWarnings("deprecation")
	@Override
	public void createSheet(XSSFWorkbook wbCtxt, SheetMetadata shMdt) throws EX_General
	{
		if (wbCtxt != null && shMdt != null)
		{
			XSSFSheet sheet = wbCtxt.createSheet(shMdt.getSheetName());
			if (sheet != null)
			{
				int i = 0, c = 0;
				for ( FieldsMetadata fldMdt : shMdt.getFieldsDetails() )
				{

					Row row = sheet.createRow(i);
					if (row != null)
					{
						Cell cell = row.createCell(c);
						cell.setCellValue(fldMdt.getSheetField());

						// Highlight Keys
						if (fldMdt.getKey())
						{
							XSSFCellStyle styleKey = wbCtxt.createCellStyle();
							styleKey.setBorderBottom(CellStyle.BORDER_THICK);
							styleKey.setBottomBorderColor(IndexedColors.BLUE.getIndex());
							styleKey.setBorderLeft(CellStyle.BORDER_THICK);
							styleKey.setLeftBorderColor(IndexedColors.GREEN.getIndex());
							cell.setCellStyle(styleKey);

						}
						else // Normal Cells
						{
							XSSFCellStyle styleNormal = wbCtxt.createCellStyle();
							styleNormal.setBorderBottom(CellStyle.BORDER_MEDIUM);
							styleNormal.setBottomBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
							styleNormal.setBorderLeft(CellStyle.BORDER_MEDIUM);
							styleNormal.setLeftBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
							cell.setCellStyle(styleNormal);

						}

					}
					i++;

				}

				sheet.autoSizeColumn(c);

			}
		}

	}

}
