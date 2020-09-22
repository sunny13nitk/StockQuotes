package scriptsengine.statistics.alerts.implementations;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import modelframework.implementations.GeneralMessage;
import modelframework.implementations.MessagesFormatter;
import scriptsengine.enums.SCEenums;
import scriptsengine.enums.SCEenums.alertType;
import scriptsengine.reportsengine.repDS.definitions.TY_AlertInfo;
import scriptsengine.reportsengine.repDS.definitions.TY_Attr_Container;
import scriptsengine.statistics.alerts.definitions.TY_Alert;
import scriptsengine.statistics.alerts.interfaces.IGenericDeltaAlerts;
import scriptsengine.uploadengine.exceptions.EX_General;

/**
 * Generic Service to Process Delta Specific Alert(s) for a given Attribute Container and Alerts Invoked Info as
 * triggerd by Delta alerts Annotations
 * For Translation of Alerts Annotations to Alerts Info refer ReportDataSourceAspect
 *
 */
@Service
public class GenericDeltaAlertSrv implements IGenericDeltaAlerts
{

	@Autowired
	private MessagesFormatter msgFormatter;

	@Override
	public ArrayList<TY_Alert> getAlertsforAttrContainer(TY_Attr_Container attrContainer, ArrayList<TY_AlertInfo> deltaalertsInfo) throws EX_General
	{
		ArrayList<TY_Alert> alerts = null;

		if (deltaalertsInfo != null)
		{
			if (deltaalertsInfo.size() > 0)
			{
				/**
				 * Validate Alerts Info
				 */
				validateAlertsInfo(deltaalertsInfo);

				/**
				 * Process Penultimate Alerts
				 */
				processPenultimateAlerts(attrContainer, deltaalertsInfo);

				/**
				 * Process DElta Trend Alerts
				 */
				processDeltaTrendAlerts(attrContainer, deltaalertsInfo);

			}
		}

		return alerts;
	}

	/**
	 * -------------------------------------------------------------------------------------------------
	 * Validate Alerts Info - Check for alert types and if any invalid alert type is there throw error
	 * 
	 * @param deltaalertsInfo
	 *             - Alert Info List
	 * @throws EX_General
	 *              -------------------------------------------------------------------------------
	 */
	private void validateAlertsInfo(ArrayList<TY_AlertInfo> deltaalertsInfo) throws EX_General
	{
		try
		{
			TY_AlertInfo invalidalert = deltaalertsInfo.stream()
			          .filter(x -> x.getAlertType() != SCEenums.alertType.PENULTIMATE && x.getAlertType() != SCEenums.alertType.TREND).findFirst()
			          .get();
			if (invalidalert != null)
			{
				EX_General msgChgErr = new EX_General("ERR_INVALID_ALERTTYPE", new Object[]
				{ invalidalert.getAlertType().toString(), this.getClass().getSimpleName()
				});
				msgFormatter.generate_message_snippet(msgChgErr);
				throw msgChgErr;
			}
		}
		catch (NoSuchElementException e)
		{
			// Do Nothing - Infact no Invalid Alert type invoked
		}
	}

	private void processPenultimateAlerts(TY_Attr_Container attrContainer, ArrayList<TY_AlertInfo> deltaalertsInfo)
	{
		/**
		 * Get the penultimate alertInfo
		 */
		ArrayList<TY_AlertInfo> penualertsInfo = new ArrayList<TY_AlertInfo>();
		TY_Alert newPenuAlert = null;

		try
		{
			penualertsInfo = deltaalertsInfo.stream().filter(x -> x.getAlertType() == alertType.PENULTIMATE)
			          .collect(Collectors.toCollection(ArrayList::new));
			if (penualertsInfo.size() > 0)
			{
				/*
				 * Get the penultimate delta figure
				 */
				int deltaSize = attrContainer.getDeltaFigures().size();
				if (deltaSize >= 2)
				{
					double penuDeltaFig = attrContainer.getDeltaFigures().get(deltaSize - 1).getFigure();

					for ( TY_AlertInfo ty_AlertInfo : penualertsInfo )
					{
						GeneralMessage msgGen = null;
						switch (ty_AlertInfo.getAlertCmpDirection())
						{
						case INCREASE:
							if (penuDeltaFig > ty_AlertInfo.getValuetoCmp())
							{
								/**
								 * Prepare messages accd. to genericMsg ON or NOT
								 */
								if (ty_AlertInfo.isGenericmsg())
								{
									// Prepare Msg Text
									msgGen = new GeneralMessage(ty_AlertInfo.getMsgID(), new Object[]
									{ attrContainer.getAttrLabel(), "INCREASING", ty_AlertInfo.getValuetoCmp()
									});

								}
								else
								{
									// For Non Generic Msg - Only provide the value
									msgGen = new GeneralMessage(ty_AlertInfo.getMsgID(), new Object[]
									{ ty_AlertInfo.getValuetoCmp()
									});

								}

							}
							break;
						case DECREASE:
							if (penuDeltaFig < (ty_AlertInfo.getValuetoCmp() * -1))
							{
								/**
								 * Prepare messages accd. to genericMsg ON or NOT
								 */
								if (ty_AlertInfo.isGenericmsg())
								{
									// Prepare Msg Text
									msgGen = new GeneralMessage(ty_AlertInfo.getMsgID(), new Object[]
									{ attrContainer.getAttrLabel(), "DECREASING", ty_AlertInfo.getValuetoCmp()
									});

								}
								else
								{
									// For Non Generic Msg - Only provide the value
									msgGen = new GeneralMessage(ty_AlertInfo.getMsgID(), new Object[]
									{ ty_AlertInfo.getValuetoCmp()
									});

								}

							}
							break;
						case NONE:
							break;
						default:
							break;
						}
						if (msgGen != null)
						{
							// Create Alert Msg Instance
							newPenuAlert = new TY_Alert(ty_AlertInfo.getAlertMode(), msgFormatter.generate_message_snippet(msgGen));

							// append Msg to Alerts of Attribute Container
							if (newPenuAlert != null)
							{
								attrContainer.getAlerts().add(newPenuAlert);
							}
						}

					}
				}
			}
		}
		catch (NoSuchElementException e)
		{
			// Do Nothing
		}

	}

	/**
	 * -----------------------------------------------------------------------------------------------
	 * Process Delta Trend Alerts for Delta Figures
	 * 
	 * @param attrContainer
	 *             - Attribute Container
	 * @param deltaalertsInfo
	 *             - Delta Alerts Info List
	 *             ----------------------------------------------------------------------------------
	 */
	private void processDeltaTrendAlerts(TY_Attr_Container attrContainer, ArrayList<TY_AlertInfo> deltaalertsInfo)
	{
		/**
		 * Get the penultimate alertInfo
		 */
		ArrayList<TY_AlertInfo> deltaalertsInfoList = new ArrayList<TY_AlertInfo>();
		TY_Alert newDeltaAlert = null;

		try
		{
			deltaalertsInfoList = deltaalertsInfo.stream().filter(x -> x.getAlertType() == alertType.TREND)
			          .collect(Collectors.toCollection(ArrayList::new));
			if (deltaalertsInfoList.size() > 0)
			{
				/*
				 * Get Number of Delta Figures Present- Use for Comaprison with Comparison period in Alert Info
				 */
				int deltaSize = attrContainer.getDeltaFigures().size();
				if (deltaSize >= 2)
				{

					for ( TY_AlertInfo ty_AlertInfo : deltaalertsInfoList )
					{
						boolean alertTriggered = false, consistentTrend = true;
						GeneralMessage msgGen = null;
						int sizeTo = ty_AlertInfo.getOccurances();
						/**
						 * FOr a particular Case - Delta Key figs size is 2 and request period for Comparison is 4
						 * occurances, so need to adjust occurances to max available in this case
						 */
						if (sizeTo > deltaSize)
						{
							sizeTo = deltaSize; // Cannot exceed
						}

						int loopcount = deltaSize - sizeTo;
						switch (ty_AlertInfo.getAlertCmpDirection())
						{
						case INCREASE:
							for ( int x = (deltaSize - 1); x >= loopcount; x-- )
							{
								if (attrContainer.getDeltaFigures().get(x).getFigure() >= ty_AlertInfo.getValuetoCmp())
								{
									alertTriggered = true;
								}
								else
								{

									alertTriggered = false;
									consistentTrend = false;
								}
							}
							break;
						case DECREASE:
							for ( int x = (deltaSize - 1); x >= loopcount; x-- )
							{
								if (attrContainer.getDeltaFigures().get(x).getFigure() <= (ty_AlertInfo.getValuetoCmp() * -1))
								{
									alertTriggered = true;
								}
								else
								{

									alertTriggered = false;
									consistentTrend = false;
								}
							}
							break;
						case NONE:
							break;
						default:
							break;
						}
						if (alertTriggered && consistentTrend)
						{
							/**
							 * Prepare messages accd. to genericMsg ON or NOT
							 */
							if (ty_AlertInfo.isGenericmsg())
							{
								// Prepare Msg Text
								msgGen = new GeneralMessage(ty_AlertInfo.getMsgID(), new Object[]
								{ attrContainer.getAttrLabel(), ty_AlertInfo.getAlertCmpDirection().toString(), ty_AlertInfo.getValuetoCmp(),
								          sizeTo
								});

							}
							else
							{
								// For Non Generic Msg - Provide value and occurances only
								msgGen = new GeneralMessage(ty_AlertInfo.getMsgID(), new Object[]
								{ ty_AlertInfo.getValuetoCmp(), sizeTo
								});

							}
							if (msgGen != null)
							{
								// Create Alert Msg Instance
								newDeltaAlert = new TY_Alert(ty_AlertInfo.getAlertMode(), msgFormatter.generate_message_snippet(msgGen));

								// append Msg to Alerts of Attribute Container
								if (newDeltaAlert != null)
								{
									attrContainer.getAlerts().add(newDeltaAlert);
								}
							}

						}
					}

				}
			}
		}
		catch (NoSuchElementException e)
		{
			// Do Nothing
		}

	}

}
