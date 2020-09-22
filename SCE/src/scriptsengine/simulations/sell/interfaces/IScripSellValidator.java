package scriptsengine.simulations.sell.interfaces;

import scriptsengine.enums.SCEenums.scripSellMode;
import scriptsengine.uploadengine.exceptions.EX_General;

public interface IScripSellValidator
{
	public scripSellMode validaeScripSaleforScrip(String scCode, int sellQty) throws EX_General;
}
