package root.scripsEngine.uploadEngine.Metadata.services.interfaces;

import root.scripsEngine.uploadEngine.Metadata.definitions.BaseSheetKey;
import root.scripsEngine.uploadEngine.Metadata.definitions.SCSheetMetadata;
import root.scripsEngine.uploadEngine.Metadata.definitions.SCWBMetadata;
import root.scripsEngine.uploadEngine.Metadata.definitions.SheetFieldsMetadata;
import root.scripsEngine.uploadEngine.exceptions.EX_General;

public interface ISCWBMetadataSrv
{
	public SCSheetMetadata getMetadataforSheet(
	        String SheetName
	) throws EX_General;
	
	public SheetFieldsMetadata getFieldMetadata(
	        String SheetName, String FieldName
	) throws EX_General;
	
	public BaseSheetKey getBaseSheetKey(
	) throws EX_General;
	
	public SCSheetMetadata getMetadataforBaseSheet(
	) throws EX_General;
	
	public SCWBMetadata getWbMetadata(
	) throws EX_General;
	
	public String getRelationNameforSheetName(
	        String sheetName
	) throws EX_General;
	
}
