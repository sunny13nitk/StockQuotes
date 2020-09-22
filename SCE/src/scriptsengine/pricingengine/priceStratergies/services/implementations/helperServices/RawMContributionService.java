package scriptsengine.pricingengine.priceStratergies.services.implementations.helperServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums.direction;
import scriptsengine.pojos.OB_Scrip_RawMaterial;
import scriptsengine.pricingengine.definitions.TY_PFactor;
import scriptsengine.pricingengine.definitions.TY_PFactorDetail;
import scriptsengine.pricingengine.priceStratergies.services.interfaces.helperSrvices.IRawMContributionService;
import scriptsengine.pricingengine.priceStratergies.services.types.PPS_LTY_CatgMList;
import scriptsengine.pricingengine.priceStratergies.services.types.PPS_LTY_RawMCatgMap;
import scriptsengine.uploadengine.exceptions.EX_General;
import scriptsengine.utilities.implementations.LambdaUtilities;

/**
 * Raw Material Effective Contribution Service for Raw Materials
 * Will compute effective contributions per Category of Raw Material derived for factored raw Materials considering
 * Raw Materails belong to same or different Categories
 */
@Service
public class RawMContributionService implements IRawMContributionService
{

	@Override
	public ArrayList<TY_PFactorDetail> getEffectiveRMC(TY_PFactor rawMFactor, ArrayList<OB_Scrip_RawMaterial> RawMaterialsList) throws EX_General
	{

		ArrayList<TY_PFactorDetail> catgFffPer = new ArrayList<TY_PFactorDetail>();
		ArrayList<PPS_LTY_CatgMList> catgRawMCastedList = null;
		// this.rawMCatgMapList = new ArrayList<PPS_LTY_RawMCatgMap>();
		ArrayList<PPS_LTY_RawMCatgMap> rawMCatgMapList = new ArrayList<PPS_LTY_RawMCatgMap>();
		ArrayList<PPS_LTY_RawMCatgMap> rawMCatgMapListDistinct = new ArrayList<PPS_LTY_RawMCatgMap>();

		/**
		 * 1. Get Upper Casted List of Categories and Associated Raw Materials for the Scrip from DB
		 */
		if (rawMFactor != null && RawMaterialsList != null)
		{
			catgRawMCastedList = getCategoryRawMUCaseList(RawMaterialsList);
			/**
			 * 2. Evaluate each Factor to determine relevant Category for Each Individual Raw Material
			 */
			for ( TY_PFactorDetail factorDetails : rawMFactor.getFactoritems() )
			{
				String rawMQ = factorDetails.getDesc().toUpperCase();
				PPS_LTY_RawMCatgMap rawMCatgMap = new PPS_LTY_RawMCatgMap(factorDetails.getDesc(), factorDetails.getDirection(),
				          factorDetails.getPercentage(), null);
				rawMCatgMap.setRawMCatg(this.getCategoryforRawM(rawMQ, catgRawMCastedList));
				rawMCatgMapList.add(rawMCatgMap);

			}

			/**
			 * 3. Determine Effective Contributions by Category
			 */
			if (rawMCatgMapList != null)
			{
				// Loop at each of the categories one by one : first get the distinct categories
				rawMCatgMapListDistinct = rawMCatgMapList.stream().filter(LambdaUtilities.distinctByKey(x -> x.getRawMCatg()))
				          .collect(Collectors.toCollection(ArrayList::new));
				if (rawMCatgMapListDistinct != null)
				{
					for ( PPS_LTY_RawMCatgMap uniqueRawMCatg : rawMCatgMapListDistinct )
					{
						/**
						 * Loop over the rawMCatgMapList for Current Unique Material Category
						 */
						double effPer = 0;
						TY_PFactorDetail effPerItem = new TY_PFactorDetail();
						for ( PPS_LTY_RawMCatgMap factoredRawM : rawMCatgMapList )
						{
							if (factoredRawM.getRawMCatg().equals(uniqueRawMCatg.getRawMCatg()))
							{

								if (factoredRawM.getDirection() == direction.INCREASE)
								{
									effPer = effPer + (factoredRawM.getPercentage() * 1);
								}
								else if (factoredRawM.getDirection() == direction.DECREASE)
								{
									if (effPer != 0)
									{
										effPer = effPer + (factoredRawM.getPercentage() * -1);
									}
									else
									{
										effPer = factoredRawM.getPercentage();
									}
								}
							}
						}
						if (effPer != 0)
						{
							effPerItem.setDesc(uniqueRawMCatg.getRawMCatg());
							if (effPer > 0)
							{
								effPerItem.setDirection(scriptsengine.enums.SCEenums.direction.INCREASE);
							}
							else if (effPer < 0)
							{
								effPerItem.setDirection(scriptsengine.enums.SCEenums.direction.DECREASE);
							}
							effPerItem.setPercentage(effPer);
							catgFffPer.add(effPerItem);
						}
					}
				}
			}

		}

		return catgFffPer;
	}

	private ArrayList<PPS_LTY_CatgMList> getCategoryRawMUCaseList(ArrayList<OB_Scrip_RawMaterial> RawMaterialsList)
	{
		ArrayList<PPS_LTY_CatgMList> catgRawMCastedList = new ArrayList<PPS_LTY_CatgMList>();

		if (RawMaterialsList.size() > 0)
		{
			for ( OB_Scrip_RawMaterial rawM : RawMaterialsList )
			{
				PPS_LTY_CatgMList catgMList = new PPS_LTY_CatgMList();
				catgMList.setCategory(rawM.getRawMCatg());

				// Split Raw Material separated into ArrayList of Strings
				if (rawM.getRawM().contains(","))

				{
					catgMList.setRawMaterials(new ArrayList<String>(Arrays.asList(rawM.getRawM().toUpperCase().split(","))));
				}
				else // Single Raw Material - Add to List
				{
					catgMList.getRawMaterials().add(rawM.getRawM().toUpperCase());
				}
				catgRawMCastedList.add(catgMList);

			}
		}

		return catgRawMCastedList;
	}

	private String getCategoryforRawM(String rawM, ArrayList<PPS_LTY_CatgMList> catgMcastedList)
	{
		String Catg = null;

		if (catgMcastedList != null)
		{
			if (catgMcastedList.size() > 0)
			{
				for ( PPS_LTY_CatgMList catgMList : catgMcastedList )
				{
					try
					{
						String found = catgMList.getRawMaterials().stream().filter(x -> x.equals(rawM)).findFirst().get();
						if (found != null)
							return catgMList.getCategory();
					}
					catch (NoSuchElementException e)
					{
						// Do Nothing Move on to NeXt Row
					}

				}
			}
		}

		return Catg;
	}

}
