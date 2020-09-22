package scriptsengine.uploadengineSC.appUtilities.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.exceptions.EX_InvalidRelationException;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.uploadengineSC.Metadata.services.implementations.SCWBMetadataSrv;
import scriptsengine.uploadengineSC.appUtilities.defn.ScCalcData_PoJo;
import scriptsengine.uploadengineSC.appUtilities.interfaces.IEPS_CAGRSrv;
import scriptsengine.uploadengineSC.appUtilities.interfaces.IScDataRetvSrv;
import scriptsengine.uploadengineSC.entities.EN_SC_BalSheet;
import scriptsengine.uploadengineSC.entities.EN_SC_General;
import scriptsengine.uploadengineSC.scripSheetServices.definitions.SheetsNames;
import scriptsengine.uploadengineSC.scripSheetServices.interfaces.ISCExistsDB_Srv;

@Service("ScDataRetvSrv")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScDataRetvSrv implements IScDataRetvSrv
{

	/**
	 * AUTOWIRED BEANS
	 */

	@Autowired
	private ISCExistsDB_Srv	scEXSrv;

	@Autowired
	private IEPS_CAGRSrv	epsCAGRSrv;

	@Autowired
	private SCWBMetadataSrv	wbMdtSrv;
	/**
	 * PRIVATE PROPERTIES
	 */
	private EN_SC_General	scRoot;

	/**
	 * INTERFACE METHODS
	 */

	@Override
	public ScCalcData_PoJo retrieveDataforSCCode(String ScCode, int DurationCAGR) throws EX_General
	{
		ScCalcData_PoJo scDataCalc = null;

		if (ScCode != null && scEXSrv != null && (DurationCAGR > 0 && DurationCAGR <= 10))
		{
			scRoot = scEXSrv.Get_ScripExisting_DB(ScCode);
			scDataCalc = getScripData(DurationCAGR);
		}

		return scDataCalc;

	}

	@Override
	public ScCalcData_PoJo retrieveDataforSCRoot(EN_SC_General scRoot, int DurationCAGR) throws EX_General
	{
		ScCalcData_PoJo scDataCalc = null;

		if (scRoot != null && (DurationCAGR > 0 && DurationCAGR <= 10))
		{
			this.scRoot = scRoot;
			scDataCalc = getScripData(DurationCAGR);
		}

		return scDataCalc;
	}

	/*
	 * PRIVATE METHODS
	 */

	private ScCalcData_PoJo getScripData(int DurationCAGR) throws EX_General
	{
		ScCalcData_PoJo scDataCalc = new ScCalcData_PoJo();
		double divPPen_Yr = 0;

		// EPS CAGR - via an Bean that implements IEPS_CAGRSrv;
		if (epsCAGRSrv != null)
		{
			scDataCalc.setEPS_CAGR(epsCAGRSrv.getEPSCAGR(scRoot, DurationCAGR));
		}
		String relName = wbMdtSrv.getRelationNameforSheetName(SheetsNames.sheet_Analysis);
		try
		{
			ArrayList<EN_SC_BalSheet> balSheetEntList = scRoot.getRelatedEntities(relName);
			if (balSheetEntList != null)
			{
				if (balSheetEntList.size() > 0)
				{
					scDataCalc.setDivP_Avg(this.calc_AVG_DIvP(balSheetEntList));
					scDataCalc.setAvgROCE(this.calc_AVG_ROCE(balSheetEntList));
					scDataCalc.setAvgROE(this.calc_AVG_ROE(balSheetEntList));

					divPPen_Yr = balSheetEntList.get((balSheetEntList.size() - 1)).getDividendPaid();

				}
			}
		}

		catch (EX_InvalidRelationException e)
		{
			EX_General ex = new EX_General(new Object[]
			{ e.getMessage()
			});
			throw ex;
		}
		scDataCalc.setNumShares(scRoot.getNumShares());
		scDataCalc.setDPS_Curr((divPPen_Yr / scRoot.getNumShares()));

		return scDataCalc;

	}

	private double calc_AVG_DIvP(ArrayList<EN_SC_BalSheet> balSheetEntList)
	{
		double avgDP = 0;

		if (balSheetEntList != null)
		{
			avgDP = balSheetEntList.stream().mapToDouble(EN_SC_BalSheet::getDividendPayout).average().getAsDouble();
		}

		return avgDP;
	}

	private double calc_AVG_ROE(ArrayList<EN_SC_BalSheet> balSheetEntList)
	{
		double avgROE = 0;

		if (balSheetEntList != null)
		{
			avgROE = balSheetEntList.stream().mapToDouble(EN_SC_BalSheet::getROE).average().getAsDouble();
		}

		return avgROE;
	}

	private double calc_AVG_ROCE(ArrayList<EN_SC_BalSheet> balSheetEntList)
	{
		double avgROCE = 0;

		if (balSheetEntList != null)
		{
			avgROCE = balSheetEntList.stream().mapToDouble(EN_SC_BalSheet::getROCE).average().getAsDouble();
		}

		return avgROCE;
	}
}
