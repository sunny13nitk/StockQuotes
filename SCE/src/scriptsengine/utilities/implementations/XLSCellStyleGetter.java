package scriptsengine.utilities.implementations;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSCellStyleGetter
{

	public static final String	styleHeaderBlueUline			= "HEADERBLUEULINE";
	public static final String	styleKeyGold					= "KEYGOLD";
	public static final String	styleKeyGoldHlink				= "KEYGOLDHLINK";
	public static final String	styleGood						= "GOOD";
	public static final String	styleBad						= "BAD";
	public static final String	styleNormal					= "NORMAL";
	public static final String	styleLightBlue					= "LIGHTBLUE";
	public static final String	styleTeal						= "TEAL";
	public static final String	styleLightGray					= "LIGHTGRAY";
	public static final String	styleNoFill					= "NOFILL";
	public static final String	styleGraySolid					= "GRAYSOLID";
	public static final String	styleTotalBlue					= "TOTALBLUE";
	public static final String	styleTotalBlueGrayItalic			= "TOTALBLUEGRAYITALIC";
	public static final String	styleBlankBorderBlack			= "BLANKBLACKBORDER";
	public static final String	styleNoFillBorderBottom			= "BLANKBOTTOMBLACKBORDER";
	public static final String	styleBottomThickBorderLGrayBold	= "BOLDBLACKFONTGRAYFILLBOTTOMBORDER";
	public static final String	styleNoFillBorderBottomThick		= "BLANKBOTTOMBLACKBORDERTHICK";
	public static final String	styleYellowFillBoldBlack			= "YELLOWFILLBOLDDLACKFONT";

	public static XSSFCellStyle getCellStyleforCode(String Code, XSSFWorkbook wbCtxt, CellType cellType)
	{
		XSSFCellStyle styleRet = null;
		Font fontH = null;
		DataFormat fmt = wbCtxt.createDataFormat();
		styleRet = wbCtxt.createCellStyle();
		if (wbCtxt != null)
		{
			switch (cellType)
			{
			case STRING:
				styleRet.setDataFormat(fmt.getFormat("@"));
			case NUMERIC:
				styleRet.setDataFormat(fmt.getFormat("#,##0.00"));
				break;
			case _NONE: // Using this for Percentage
				styleRet.setDataFormat(fmt.getFormat("0.00%"));
			default:
				styleRet.setDataFormat(fmt.getFormat("@"));
				break;
			}

			if (Code != null)
			{
				switch (Code)
				{
				case styleHeaderBlueUline:
					XSSFColor myColor = new XSSFColor(new java.awt.Color(199, 216, 248));
					styleRet.setFillForegroundColor(myColor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_THICK);
					styleRet.setBottomBorderColor(new XSSFColor(new java.awt.Color(97, 114, 146)));
					break;

				case styleKeyGold:
					styleRet.setBorderBottom(CellStyle.BORDER_MEDIUM);
					styleRet.setBottomBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					break;

				case styleKeyGoldHlink:
					styleRet.setBorderBottom(CellStyle.BORDER_MEDIUM);
					styleRet.setBottomBorderColor(IndexedColors.LIGHT_ORANGE.getIndex());
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					break;

				case styleGood:
					XSSFColor myColor2 = new XSSFColor(new java.awt.Color(201, 228, 201));
					styleRet.setFillForegroundColor(myColor2);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.GREEN.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					break;

				case styleYellowFillBoldBlack:
					XSSFColor myColorY = new XSSFColor(new java.awt.Color(255, 255, 178));
					styleRet.setFillForegroundColor(myColorY);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					styleRet.setVerticalAlignment(VerticalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					styleRet.setWrapText(true);
					break;

				case styleBad:
					XSSFColor myColorBad = new XSSFColor(new java.awt.Color(255, 229, 234));
					styleRet.setFillForegroundColor(myColorBad);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.RED.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					break;
				case styleNormal:
					XSSFColor myColorNormal = new XSSFColor(new java.awt.Color(255, 255, 178));
					styleRet.setFillForegroundColor(myColorNormal);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					Font fontN = wbCtxt.createFont();
					fontN.setColor(IndexedColors.ORANGE.index);
					fontN.setFontName("Calibri");
					styleRet.setFont(fontN);
					break;
				case styleLightBlue:
					XSSFColor wicolor = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(wicolor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.LIGHT_BLUE.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.NO_FILL);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);

					break;
				case styleTeal:
					XSSFColor whicolor = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(whicolor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.TEAL.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.NO_FILL);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);
					break;

				case styleLightGray:
					XSSFColor whitecolor = new XSSFColor(new java.awt.Color(249, 249, 249));
					styleRet.setFillForegroundColor(whitecolor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.BLACK.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.NO_FILL);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);
					break;

				case styleNoFill:
					XSSFColor whcolor = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(whcolor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.BLACK.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.NO_FILL);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);
					break;

				case styleNoFillBorderBottom:
					XSSFColor whitcolor = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(whitcolor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.BLACK.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_THIN);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);
					break;

				case styleNoFillBorderBottomThick:
					XSSFColor whitcolor1 = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(whitcolor1);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.BLACK.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_THICK);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);
					break;
				case styleGraySolid:
					XSSFColor graycolor = new XSSFColor(new java.awt.Color(229, 229, 229));
					styleRet.setFillForegroundColor(graycolor);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					styleRet.setVerticalAlignment(VerticalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(IndexedColors.BLACK.index);
					fontH.setFontName("Calibri");
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_DOUBLE);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.BORDER_DOUBLE);
					break;

				case styleTotalBlue:
					XSSFColor colTotal = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(colTotal);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_DOUBLE);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.BORDER_MEDIUM);
					styleRet.setBottomBorderColor(new XSSFColor(new java.awt.Color(90, 134, 213)));
					styleRet.setTopBorderColor(new XSSFColor(new java.awt.Color(90, 134, 213)));
					break;

				case styleTotalBlueGrayItalic:
					XSSFColor colTot = new XSSFColor(new java.awt.Color(241, 241, 241));
					styleRet.setFillForegroundColor(colTot);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					fontH.setItalic(true);
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_DOUBLE);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.BORDER_MEDIUM);
					styleRet.setBottomBorderColor(new XSSFColor(new java.awt.Color(90, 134, 213)));
					styleRet.setTopBorderColor(new XSSFColor(new java.awt.Color(90, 134, 213)));
					break;

				case styleBlankBorderBlack:
					XSSFColor colwhite = new XSSFColor(new java.awt.Color(255, 255, 255));
					styleRet.setFillForegroundColor(colwhite);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_THIN);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.BORDER_THIN);
					styleRet.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
					styleRet.setTopBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));
					break;

				case styleBottomThickBorderLGrayBold:
					XSSFColor colgray = new XSSFColor(new java.awt.Color(229, 229, 229));
					styleRet.setFillForegroundColor(colgray);
					styleRet.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleRet.setAlignment(HorizontalAlignment.CENTER);
					styleRet.setVerticalAlignment(VerticalAlignment.CENTER);
					fontH = wbCtxt.createFont();
					fontH.setColor(HSSFColor.BLACK.index);
					fontH.setFontName("Calibri");
					fontH.setBold(true);
					styleRet.setFont(fontH);
					styleRet.setBorderBottom(CellStyle.BORDER_THICK);
					styleRet.setBorderLeft(CellStyle.NO_FILL);
					styleRet.setBorderRight(CellStyle.NO_FILL);
					styleRet.setBorderTop(CellStyle.NO_FILL);
					styleRet.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 0, 0)));

					break;
				default:
					break;
				}

			}

		}

		return styleRet;
	}

}
