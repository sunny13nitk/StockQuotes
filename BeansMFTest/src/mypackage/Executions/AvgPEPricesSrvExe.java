package mypackage.Executions;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import mypackage.Interfaces.IExecutable;
import scriptsengine.uploadengineSC.appUtilities.defn.AvgPEPricesPojo;
import scriptsengine.uploadengineSC.appUtilities.interfaces.IAvgPEPricesSrv;
import scriptsengine.utilities.implementations.CAGRCalcSrv;

@Service("AvgPEPricesSrvExe")
public class AvgPEPricesSrvExe implements IExecutable
{

	@Override
	public void execute(ApplicationContext appctxt) throws Exception
	{
		if (appctxt != null)
		{
			String scDesc = "Avenue";

			IAvgPEPricesSrv avpPEPriceSrv = appctxt.getBean(IAvgPEPricesSrv.class);
			if (avpPEPriceSrv != null)
			{
				AvgPEPricesPojo pricesPOJO = avpPEPriceSrv.getAvgPricesforScDesc(scDesc);
				if (pricesPOJO != null)
				{
					System.out.println(
					          pricesPOJO.getScCode() + "-" + pricesPOJO.getEPS() + "-" + pricesPOJO.getCMP() + "-" + pricesPOJO.getPECurr()
					                    + " || " + pricesPOJO.getPE3Yr() + " : " + pricesPOJO.getPrice3Yr() + "//" + pricesPOJO.getPrDelta3Yr()
					                    + " || " + pricesPOJO.getPE5Yr() + " : " + pricesPOJO.getPrice5Yr() + "//" + pricesPOJO.getPrDelta5Yr());
				}
			}

			double cagr = CAGRCalcSrv.getCAGR(1, 2, 3);
			System.out.println(cagr);

			double iniVal = CAGRCalcSrv.getiniValforCAGR(20, 6520, 5);
			System.out.println(iniVal);

		}

	}

}
