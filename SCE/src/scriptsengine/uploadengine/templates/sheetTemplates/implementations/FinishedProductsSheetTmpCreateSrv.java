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

import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengine.templates.sheetTemplates.interfaces.ISheetCreateTemplate;

@Service("FinishedProductsSheetTmpCreateSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class FinishedProductsSheetTmpCreateSrv implements ISheetCreateTemplate
{

	@Override
	public void createSheet(XSSFWorkbook wbCtxt, SheetMetadata shMdt) throws EX_General
	{
		if (wbCtxt != null && shMdt != null)
		{
			XSSFSheet sheet = wbCtxt.createSheet(shMdt.getSheetName());
			if (sheet != null)
			{
				int i = 0, c = 0;
				XSSFCellStyle styleKey = wbCtxt.createCellStyle();
				styleKey.setBorderBottom(CellStyle.BORDER_THICK);
				styleKey.setBottomBorderColor(IndexedColors.BLUE.getIndex());
				styleKey.setBorderLeft(CellStyle.BORDER_THICK);
				styleKey.setLeftBorderColor(IndexedColors.GREEN.getIndex());

				XSSFCellStyle styleNormal = wbCtxt.createCellStyle();
				styleNormal.setBorderBottom(CellStyle.BORDER_MEDIUM);
				styleNormal.setBottomBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
				styleNormal.setBorderLeft(CellStyle.BORDER_MEDIUM);
				styleNormal.setLeftBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());

				// Year Row
				Row row = sheet.createRow(i);
				if (row != null)
				{
					Cell cell = row.createCell(c);
					cell.setCellValue("Year");
					cell.setCellStyle(styleKey);

					cell = row.createCell(c + 2);
					cell.setCellValue("Maintain Year - Numerical Value(YYYY)");
					cell.setCellStyle(styleNormal);
				}
				i++;

				// Heads
				row = sheet.createRow(i);
				Cell cell = row.createCell(1);
				cell.setCellValue("Finished Product Category");
				cell.setCellStyle(styleKey);
				i++;

				row = sheet.createRow(i);
				cell = row.createCell(2);
				cell.setCellValue("Replace With % contribution to Sales");

				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
			}
		}

	}

}
