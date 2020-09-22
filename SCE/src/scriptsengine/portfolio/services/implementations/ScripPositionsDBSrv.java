package scriptsengine.portfolio.services.implementations;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import modelframework.implementations.MessagesFormatter;
import modelframework.interfaces.IQueryService;
import modelframework.managers.QueryManager;
import modelframework.types.TY_NameValue;
import scriptsengine.portfolio.definitions.TY_Scrip_PositionModel;
import scriptsengine.portfolio.pojos.OB_Positions_Header;
import scriptsengine.portfolio.pojos.OB_Positions_Item;
import scriptsengine.portfolio.services.interfaces.IScripPositionsDB;
import scriptsengine.uploadengine.exceptions.EX_General;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScripPositionsDBSrv implements IScripPositionsDB
{

	/**
	 * REturn Position model for a Scrip - Does not Involves Validation of Scrip Code
	 * If Used without Sc Code Validation Wrapper for Scrip Code Validation to be Used before
	 * 
	 * @param scCode
	 *             - Scrip Code
	 * @return - Position Model
	 * @throws EX_General
	 *              - Exception
	 */

	@Autowired
	private MessagesFormatter	msgFormatter;

	private final String		objName	= "OB_Positions_Header";
	private final String		relName	= "OB_Positions_Items_Rel";

	@Override
	public TY_Scrip_PositionModel getPosModelforScCode(String scCode) throws EX_General
	{
		TY_Scrip_PositionModel posModel = null;
		// Get Scrip Positions for a Particular Scrip from DB
		if (scCode != null)
		{

			try
			{
				IQueryService qs = (IQueryService) QueryManager.getQuerybyRootObjname(objName);
				if (qs != null)
				{
					ArrayList<TY_NameValue> params = new ArrayList<TY_NameValue>();

					params.add(new TY_NameValue("ScCode", scCode));

					String condn = "ScCode = ?";

					ArrayList<OB_Positions_Header> poshList = qs.executeQuery(condn, params);
					if (poshList != null)
					{
						ArrayList<OB_Positions_Item> posItems = poshList.get(0).getRelatedEntities(relName);
						if (posItems != null)
						{
							posModel = new TY_Scrip_PositionModel(poshList.get(0), posItems);
						}
					}

				}
			}
			catch (Exception e)
			{
				EX_General msgChgErr = new EX_General("ERR_LOAD_POS", new Object[]
				{ scCode, e.getMessage()
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}

		return posModel;
	}

}
