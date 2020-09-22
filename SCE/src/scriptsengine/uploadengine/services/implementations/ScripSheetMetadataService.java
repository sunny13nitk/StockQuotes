package scriptsengine.uploadengine.services.implementations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.definitions.Object_Info;
import modelframework.exposed.FrameworkManager;
import scriptsengine.uploadengine.JAXB.definitions.FieldsMetadata;
import scriptsengine.uploadengine.JAXB.definitions.SheetMetadata;
import scriptsengine.uploadengine.definitions.SheetSplitAttrs;
import scriptsengine.uploadengine.services.interfaces.IScripSheetMetadata;

/**
 * Scrip Sheet Metadata Service - Utility Service
 * Won't hold any data - Can be autowired as and where needed
 * and relevant methods can be invoked
 * Will just be autowired to ScripDataService that is a Application Context bean and holds WBMetadata
 */
@Service("ScripSheetMetadataService")
public class ScripSheetMetadataService implements IScripSheetMetadata
{

	@Autowired
	private ScripDataService			scDataSrv;

	@Autowired
	private FrameworkManager			FWMgr;

	private String					baseSheetName;

	private String					baseSheetkeyField;

	private String					baseSheetkeyobjField;

	private ArrayList<String>		mandatorySheets;

	private SheetMetadata			sheetMdt;

	private ArrayList<SheetSplitAttrs>	sheetSplitAttsList;

	@Override
	public String getBaseSheetName()
	{
		String basesheet = null;
		if (scDataSrv != null)
		{

			if (this.baseSheetName != null)
			{
				basesheet = this.baseSheetName;
			}
			else
			{
				if (scDataSrv.getWBMetaData() != null)
				{
					basesheet = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getBasesheet() == true).findFirst().get()
					          .getSheetName();
				}
			}
		}

		return basesheet;

	}

	@Override
	public String getBaseSheetKeyField()
	{
		String keyfield = null;

		if (this.baseSheetkeyField != null)
		{
			keyfield = baseSheetkeyField;
		}

		else
		{

			if (scDataSrv != null)
			{
				keyfield = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getBasesheet() == true).findFirst().get()
				          .getFieldsDetails().stream().filter(y -> y.getMandatory() == true).findFirst().get().getSheetField();
				baseSheetkeyField = keyfield;

			}
		}

		return keyfield;
	}

	@Override
	public SheetMetadata getSheetMdtbySheetName(String sheetName)
	{

		SheetMetadata shtMdt = null;

		if (sheetName != null)
		{
			try
			{
				shtMdt = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getSheetName().equals(sheetName)).findFirst().get();
			}

			catch (NoSuchElementException e)
			{
				// Do Nothing
			}
		}

		// TODO Auto-generated method stub
		return shtMdt;
	}

	@Override
	public ArrayList<String> getMandatorySheets()
	{
		ArrayList<String> mSheets = null;

		if (this.mandatorySheets != null)
		{
			mSheets = mandatorySheets;
		}
		else
		{
			// Get the Mandatory sheets from Metadata
			ArrayList<SheetMetadata> MndtSheetsmdt = new ArrayList<SheetMetadata>();

			MndtSheetsmdt = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getRequiredCreate() == true)
			          .collect(Collectors.toCollection(ArrayList::new));
			if (MndtSheetsmdt != null)
			{
				if (MndtSheetsmdt.size() > 0)
				{
					mSheets = new ArrayList<String>();
					for ( SheetMetadata sheetMetadata : MndtSheetsmdt )
					{
						String sheetName = sheetMetadata.getSheetName();
						mSheets.add(sheetName);
					}
				}
			}
		}
		return mSheets;
	}

	@Override
	public String getBaseSheetKeyObjField()
	{
		String keyobjfield = null;

		if (this.baseSheetkeyobjField != null)
		{
			keyobjfield = baseSheetkeyobjField;
		}

		else
		{

			if (scDataSrv != null)
			{
				keyobjfield = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getBasesheet() == true).findFirst().get()
				          .getFieldsDetails().stream().filter(y -> y.getMandatory() == true).findFirst().get().getObjField();
				baseSheetkeyobjField = keyobjfield;

			}
		}

		return keyobjfield;
	}

	/**
	 * Get the RelationName for the sheet name join b/w WbMetadata and objschemasaddendum
	 * 
	 * @return - Relation Name for Dependant Object for Sheet Name
	 */
	@Override
	public String getRelationNameforSheetName(String sheetName)
	{
		String relName = null;

		if (sheetName != null)
		{
			// first get the sheet Metadata and validate it is for not a base sheet
			SheetMetadata shmdt = this.getSheetMdtbySheetName(sheetName);
			if (!shmdt.getBasesheet())
			{
				try
				{
					relName = FrameworkManager.getObjectSchemaFactory().get_Dependant_Metadata_byObjName(shmdt.getBobjName()).getRelationname();
				}

				catch (Exception e)
				{
					// Do nothing - Sheet handled via X Sheet Validator
				}

			}
		}

		return relName;
	}

	@Override
	public ArrayList<String> getKeyfieldsforSheet(String sheetName)
	{
		ArrayList<String> keyfields = new ArrayList<String>();
		ArrayList<FieldsMetadata> fldsmdt = new ArrayList<FieldsMetadata>();

		if (sheetName != null)
		{
			this.sheetMdt = this.getSheetMdtbySheetName(sheetName);
			if (sheetMdt != null)
			{
				fldsmdt = sheetMdt.getFieldsDetails().stream().filter(x -> x.getKey() == true).collect(Collectors.toCollection(ArrayList::new));
				if (fldsmdt.size() > 0)
				{
					for ( FieldsMetadata fieldMetadata : fldsmdt )
					{
						keyfields.add(fieldMetadata.getObjField());
					}
				}
			}
		}

		return keyfields;
	}

	/**
	 * Get Integer Key Fields for the Sheet - Used for Scrip Update Key Comparisons. e.g. Raw Materials maintianed till
	 * which year etc.
	 * 
	 * @param sheetName
	 * @return
	 */
	@SuppressWarnings("static-access")
	@Override
	public ArrayList<String> getIntKeyfieldsforSheet(String sheetName)
	{
		ArrayList<String> keyfields = new ArrayList<String>();
		ArrayList<FieldsMetadata> fldsmdt = new ArrayList<FieldsMetadata>();

		if (sheetName != null)
		{
			this.sheetMdt = this.getSheetMdtbySheetName(sheetName);
			if (sheetMdt != null)
			{
				fldsmdt = sheetMdt.getFieldsDetails().stream().filter(x -> x.getKey() == true).collect(Collectors.toCollection(ArrayList::new));
				if (fldsmdt.size() > 0)
				{
					/**
					 * Get the Business Object Information from sheet Metadata
					 */
					if (FWMgr != null)
					{
						Object_Info objInfo = FrameworkManager.getObjectsInfoFactory().Get_ObjectInfo_byName(this.sheetMdt.getBobjName());
						if (objInfo != null)
						{
							for ( FieldsMetadata fieldMetadata : fldsmdt )
							{
								// Get Getter for the field
								Method getter = objInfo.Get_Getter_for_FieldName(fieldMetadata.getObjField());
								if (getter != null)
								{

									if (getter.getReturnType().equals(int.class) || getter.getReturnType().equals(Integer.class))
									{
										keyfields.add(fieldMetadata.getObjField());
									}
								}

							}
						}
					}
				}
			}
		}

		return keyfields;
	}

	@Override
	public ArrayList<SheetSplitAttrs> getSplitAwareSheetAttrs()
	{
		if (this.sheetSplitAttsList != null)
			return this.sheetSplitAttsList;
		else
		{
			this.sheetSplitAttsList = new ArrayList<SheetSplitAttrs>();

			if (scDataSrv.getWBMetaData() != null)
			{
				for ( SheetMetadata shMdt : scDataSrv.getWBMetaData().getSheetsMetadata() )
				{
					ArrayList<String> splitAwareflds = new ArrayList<String>();
					if (shMdt.getFieldsDetails() != null)
					{
						for ( FieldsMetadata fldMdt : shMdt.getFieldsDetails() )
						{
							if (fldMdt.getSplitAware())
							{
								splitAwareflds.add(fldMdt.getObjField());
							}
						}
						if (splitAwareflds.size() > 0)
						{
							SheetSplitAttrs sheetSplitAttrs = new SheetSplitAttrs(shMdt.getSheetName(),
							          this.getRelationNameforSheetName(shMdt.getSheetName()), splitAwareflds);
							this.sheetSplitAttsList.add(sheetSplitAttrs);

						}
					}
				}
			}

			return this.sheetSplitAttsList;

		}

	}

	@Override
	public String getTableNameforSheetName(String sheetName)
	{
		String tabName = null;

		if (sheetName != null)
		{
			// first get the sheet Metadata and validate it is for not a base sheet
			SheetMetadata shmdt = this.getSheetMdtbySheetName(sheetName);
			if (!shmdt.getBasesheet())
			{
				try
				{
					tabName = FrameworkManager.getObjectSchemaFactory().get_Dependant_Metadata_byObjName(shmdt.getBobjName()).getTablename();
				}

				catch (Exception e)
				{
					// Do nothing - Sheet handled via X Sheet Validator
				}

			}
		}

		return tabName;
	}

	@Override
	public String getRootObjectName()
	{
		String rootObjName = null;

		if (scDataSrv != null)
		{
			rootObjName = scDataSrv.getWBMetaData().getSheetsMetadata().stream().filter(x -> x.getBasesheet() == true).findFirst().get()
			          .getBobjName();
		}

		return rootObjName;
	}

	@Override
	public String getUpdateReqdProcessBeanforSheetName(String sheetName)
	{
		String beanName = null;

		if (sheetName != null)
		{
			// first get the sheet Metadata and validate it is for not a base sheet
			SheetMetadata shmdt = this.getSheetMdtbySheetName(sheetName);
			if (!shmdt.getBasesheet())
			{

				beanName = shmdt.getUpdateReqdProcessorBean();

			}
		}

		return beanName;
	}

}
