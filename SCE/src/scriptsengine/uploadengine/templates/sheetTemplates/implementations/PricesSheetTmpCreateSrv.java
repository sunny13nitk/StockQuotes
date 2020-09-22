package scriptsengine.uploadengine.templates.sheetTemplates.implementations;

import java.text.DateFormatSymbols;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
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

/**
 * Prices Sheet Template Creation Service
 *
 */
@Service("PricesSheetTmpCreateSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PricesSheetTmpCreateSrv implements ISheetCreateTemplate
{
	private final String EOF = "EOF";

	@Override
	public void createSheet(XSSFWorkbook wbCtxt, SheetMetadata shMdt) throws EX_General
	{
		if (wbCtxt != null && shMdt != null)
		{
			XSSFSheet sheet = wbCtxt.createSheet(shMdt.getSheetName());
			if (sheet != null)
			{
				int i = 0, c = 0;

				// Year Row
				Row row = sheet.createRow(i);
				if (row != null)
				{

					Cell cell = row.createCell(c);

					DataFormat fmt = wbCtxt.createDataFormat();
					cell.setCellValue("Maintain Year <Text Cell Format>");

					XSSFCellStyle styleKey = wbCtxt.createCellStyle();
					styleKey.setBorderBottom(CellStyle.BORDER_THICK);
					styleKey.setBottomBorderColor(IndexedColors.BLUE.getIndex());
					styleKey.setBorderLeft(CellStyle.BORDER_THICK);
					styleKey.setLeftBorderColor(IndexedColors.GREEN.getIndex());
					styleKey.setDataFormat(fmt.getFormat("@"));
					cell.setCellStyle(styleKey);
				}

				// Monthconfig Row
				String[] shortMonths = new DateFormatSymbols().getShortMonths();
				int size = shortMonths.length;
				int loop_st = size - 2;
				while (loop_st >= 0)
				{
					if (i < 12)
					{
						i++;
						Row rowMon = sheet.createRow(i);
						if (rowMon != null)
						{
							Cell cell = rowMon.createCell(c);
							cell.setCellValue(shortMonths[loop_st]);
							XSSFCellStyle styleNormal = wbCtxt.createCellStyle();
							styleNormal.setBorderBottom(CellStyle.BORDER_MEDIUM);
							styleNormal.setBottomBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
							styleNormal.setBorderLeft(CellStyle.BORDER_MEDIUM);
							styleNormal.setLeftBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
							cell.setCellStyle(styleNormal);

						}
					}
					loop_st--;
				}

				// Last Row for EOF

				i++;
				Row rowEOF = sheet.createRow(i);
				if (rowEOF != null)
				{
					Cell cell = rowEOF.createCell(c);
					cell.setCellValue(EOF);

				}

				// autoSize 1st Column
				sheet.autoSizeColumn(c);

			}
		}
	}

}
