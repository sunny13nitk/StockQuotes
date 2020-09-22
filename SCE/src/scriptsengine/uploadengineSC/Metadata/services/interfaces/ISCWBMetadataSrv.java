package scriptsengine.uploadengineSC.Metadata.services.interfaces;

import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.definitions.BaseSheetKey;
import scriptsengine.uploadengineSC.Metadata.definitions.SCSheetMetadata;
import scriptsengine.uploadengineSC.Metadata.definitions.SCWBMetadata;
import scriptsengine.uploadengineSC.Metadata.definitions.SheetFieldsMetadata;

public interface ISCWBMetadataSrv
{
	public SCSheetMetadata getMetadataforSheet(String SheetName) throws EX_General;

	public SheetFieldsMetadata getFieldMetadata(String SheetName, String FieldName) throws EX_General;

	public BaseSheetKey getBaseSheetKey() throws EX_General;

	public SCSheetMetadata getMetadataforBaseSheet() throws EX_General;

	public SCWBMetadata getWbMetadata() throws EX_General;

	public String getRelationNameforSheetName(String sheetName) throws EX_General;

}
