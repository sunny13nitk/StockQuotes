package scriptsengine.statistics.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import scriptsengine.statistics.definitions.StDev;
import scriptsengine.statistics.definitions.StDevHead;
import scriptsengine.statistics.definitions.StDevItem;
import scriptsengine.statistics.definitions.StDevResult;
import scriptsengine.statistics.exceptions.EX_StdDeviation;
import scriptsengine.statistics.interfaces.IStDev;

/**
 * Standard Deviation Service - Standard DEfinition
 *
 */

@Service("StdDeviationService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StdDeviationService implements IStDev
{
	@Autowired
	private MessagesFormatter	msgFormatter;
	private StDev				deviation;

	/**
	 * @return the deviation
	 */
	@Override
	public StDev getDeviation()
	{
		return deviation;
	}

	/**
	 * @param deviation
	 *             the deviation to set
	 */
	public void setDeviation(StDev deviation)
	{
		this.deviation = deviation;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initializefromXlsx(String filepath) throws EX_StdDeviation, IOException
	{
		if (filepath != null)
		{
			int cellcount = 0;
			String cellStrValue = null;
			double cellnumValue = 0;
			double cellnumoldValue = 0;

			File myFile = new File(filepath);
			// Initialize Standard Deviation Object
			this.deviation = new StDev();
			StDevHead devHead = null;
			StDevItem devItem = null;

			FileInputStream fis = new FileInputStream(myFile);
			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();
			// Traversing over each row of XLSX file
			while (rowIterator.hasNext())
			{

				Row row = rowIterator.next();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					switch (cell.getCellType())
					{
					case Cell.CELL_TYPE_STRING:
						cellStrValue = cell.getStringCellValue();
						cellcount += 1;
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (cellnumValue != 0)
						{
							cellnumoldValue = cellnumValue;
						}
						cellnumValue = cell.getNumericCellValue();
						cellcount += 1;
						break;

					default:

					}

				}

				// this is for Title AS The Row has only once cell filled in with title
				/**
				 * New Header Creation
				 */
				if (cellcount == 1)
				{

					// Create new deviation header
					// Assign Older dev Head if it is not null
					if (devHead != null)
					{
						deviation.addStdevHeader(devHead);
					}
					if (cellStrValue != null)
					{
						devHead = new StDevHead(cellStrValue);
					}
					else if (cellnumValue != 0)
					{
						devHead = new StDevHead(Double.toString(cellnumValue));
					}

				}
				/**
				 * Item creation by average
				 */
				else if (cellcount == 2)
				{
					devItem = new StDevItem(cellStrValue, cellnumValue);
					devHead.addStdevItem(devItem);
				}
				/**
				 * Item creation with maximum and minimum value
				 */
				else if (cellcount == 3)
				{
					devItem = new StDevItem(cellStrValue, cellnumoldValue, cellnumValue);
					devHead.addStdevItem(devItem);
				}

				cellcount = 0;
			}
			myWorkBook.close();
		}
		else
			throw new EX_StdDeviation("FILEERROR", new Object[]
			{ filepath
			});

	}

	/**
	 * Intialize the deviations from Class - NO file Upload
	 * Onus of filling the deviation structures is on the calling program
	 */
	@Override
	public void initialize()
	{
		this.deviation = new StDev();

	}

	/**
	 * Validate deviation Structure(s)
	 * - Must contain at least one deviation header
	 * -- Each header Must contain at least 3 or more deviation items
	 * --- Each Item must have a non zero val1/val2 or a non zero average to complete calcualtions
	 * 
	 * @throws EX_StdDeviation
	 */
	@Override
	public void validate() throws EX_StdDeviation
	{
		if (deviation.getDeviationHeads().size() == 0)
			throw new EX_StdDeviation("NODEVHEADER", null);
		else
		{
			for ( StDevHead devheader : this.deviation.getDeviationHeads() )
			{
				if (devheader.getItems().size() < 3)
					throw new EX_StdDeviation("LESSDEVITEMS", new Object[]
					{ devheader.getTitle()
					});
				else
				{
					// Evaluate each item
					for ( StDevItem stdev_item : devheader.getItems() )
					{
						if ((stdev_item.getVal1() == 0 && stdev_item.getVal2() == 0) && (stdev_item.getAverage() == 0))
							throw new EX_StdDeviation("NULLDEVITEM", new Object[]
							{ stdev_item.getItem_title()
							});
						else
						{
							/**
							 * Compute the average if it is not calcualted
							 */
							if (stdev_item.getAverage() == 0)
							{
								stdev_item.setAverage((stdev_item.getVal1() + stdev_item.getVal2()) / 2);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void process()
	{
		try
		{
			this.validate();
			/**
			 * Validation complete - let's get to some maths now
			 */

			// 1- Calcualte the mean
			for ( StDevHead devheader : this.deviation.getDeviationHeads() )
			{
				double mean = devheader.getItems().stream().mapToDouble(StDevItem::getAverage).average().getAsDouble();
				devheader.setMean(mean);
				/**
				 * Calcualte deviation and it's square value and set in Item
				 */
				for ( StDevItem devitem : devheader.getItems() )
				{
					devitem.setDeviation(devitem.getAverage() - mean);
					devitem.setDeviation_sq(devitem.getDeviation() * devitem.getDeviation());

				}

				/**
				 * Calculate deviation sq average and set in header
				 */

				double dev_sq_avg = devheader.getItems().stream().mapToDouble(StDevItem::getDeviation_sq).average().getAsDouble();
				devheader.setDeviation_average(dev_sq_avg);

				devheader.setStandard_deviation(Math.sqrt(dev_sq_avg));
				devheader.setRsd(((100 * devheader.getStandard_deviation()) / devheader.getMean()));
				devheader.setSpread_high(devheader.getMean() * (1 + (devheader.getRsd() / 100)));
			}

		}
		catch (EX_StdDeviation e)
		{
			msgFormatter.generate_message_snippet(e);
		}

	}

	@Override
	public ArrayList<StDevResult> getResults()
	{
		ArrayList<StDevResult> stdevSumm = new ArrayList<StDevResult>();

		for ( StDevHead devheader : this.getDeviation().getDeviationHeads() )
		{
			StDevResult newSumm = new StDevResult(devheader.getTitle(), devheader.getRsd(), devheader.getSpread_high());
			stdevSumm.add(newSumm);
		}

		return stdevSumm;
	}

	@Override
	public StDev getDetailedResults()
	{
		return this.deviation;
	}

	@Override
	public void initializefromWB(XSSFWorkbook wb, String sheetName) throws IOException, EX_StdDeviation
	{
		int cellcount = 0;
		String cellStrValue = null;
		double cellnumValue = 0;
		double cellnumoldValue = 0;

		// Initialize Standard Deviation Object
		this.deviation = new StDev();
		StDevHead devHead = null;
		StDevItem devItem = null;
		if (wb != null && sheetName != null)
		{
			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = wb.getSheet(sheetName);
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();
			// Traversing over each row of XLSX file
			while (rowIterator.hasNext())
			{

				Row row = rowIterator.next();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					switch (cell.getCellType())
					{
					case Cell.CELL_TYPE_STRING:
						cellStrValue = cell.getStringCellValue();
						cellcount += 1;
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (cellnumValue != 0)
						{
							cellnumoldValue = cellnumValue;
						}
						cellnumValue = cell.getNumericCellValue();
						cellcount += 1;
						break;

					default:

					}

				}

				// this is for Title AS The Row has only once cell filled in with title
				/**
				 * New Header Creation
				 */
				if (cellcount == 1)
				{

					// Create new deviation header
					// Assign Older dev Head if it is not null
					if (devHead != null)
					{
						deviation.addStdevHeader(devHead);
					}
					if (cellStrValue != null)
					{
						devHead = new StDevHead(cellStrValue);
					}
					else if (cellnumValue != 0)
					{
						devHead = new StDevHead(Double.toString(cellnumValue));
					}

				}
				/**
				 * Item creation by average
				 */
				else if (cellcount == 2)
				{
					devItem = new StDevItem(cellStrValue, cellnumValue);
					devHead.addStdevItem(devItem);
				}
				/**
				 * Item creation with maximum and minimum value
				 */
				else if (cellcount == 3)
				{
					devItem = new StDevItem(cellStrValue, cellnumoldValue, cellnumValue);
					devHead.addStdevItem(devItem);
				}

				cellcount = 0;
			}

		}

		else
			throw new EX_StdDeviation("INVALID_WB_SHEET", new Object[]
			{ sheetName
			});

	}

	@Override
	public void initializefromSheet(XSSFSheet sheet) throws EX_StdDeviation
	{

		int cellcount = 0;
		String cellStrValue = null;
		double cellnumValue = 0;
		double cellnumoldValue = 0;

		// Initialize Standard Deviation Object
		this.deviation = new StDev();
		StDevHead devHead = null;
		StDevItem devItem = null;
		if (sheet != null)
		{
			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = sheet;
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();
			// Traversing over each row of XLSX file
			while (rowIterator.hasNext())
			{

				Row row = rowIterator.next();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					switch (cell.getCellType())
					{
					case Cell.CELL_TYPE_STRING:
						cellStrValue = cell.getStringCellValue();
						cellcount += 1;
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (cellnumValue != 0)
						{
							cellnumoldValue = cellnumValue;
						}
						cellnumValue = cell.getNumericCellValue();
						cellcount += 1;
						break;

					default:

					}

				}

				// this is for Title AS The Row has only once cell filled in with title
				/**
				 * New Header Creation
				 */
				if (cellcount == 1)
				{

					// Create new deviation header
					// Assign Older dev Head if it is not null
					if (devHead != null)
					{
						deviation.addStdevHeader(devHead);
					}
					if (cellStrValue != null)
					{
						devHead = new StDevHead(cellStrValue);
					}
					else if (cellnumValue != 0)
					{
						devHead = new StDevHead(Double.toString(cellnumValue));
					}

				}
				/**
				 * Item creation by average
				 */
				else if (cellcount == 2)
				{
					devItem = new StDevItem(cellStrValue, cellnumValue);
					devHead.addStdevItem(devItem);
				}
				/**
				 * Item creation with maximum and minimum value
				 */
				else if (cellcount == 3)
				{
					devItem = new StDevItem(cellStrValue, cellnumoldValue, cellnumValue);
					devHead.addStdevItem(devItem);
				}

				cellcount = 0;
			}

		}

		else
			throw new EX_StdDeviation("INVALID_WB_SHEET", new Object[]
			{ sheet.getSheetName()
			});

	}

}
