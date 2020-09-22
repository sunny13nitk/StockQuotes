package scriptsengine.pricingengine.priceStratergies.services.types;

import java.util.ArrayList;

/**
 * 
 * List of Categories and Materials casted to Upper Case
 */
public class PPS_LTY_CatgMList
{
	private String				category;
	private ArrayList<String>	rawMaterials;

	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *             the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * @return the rawMaterials
	 */
	public ArrayList<String> getRawMaterials()
	{
		return rawMaterials;
	}

	/**
	 * @param rawMaterials
	 *             the rawMaterials to set
	 */
	public void setRawMaterials(ArrayList<String> rawMaterials)
	{
		this.rawMaterials = rawMaterials;
	}

	/**
	 * @param category
	 * @param rawMaterials
	 */
	public PPS_LTY_CatgMList(String category, ArrayList<String> rawMaterials)
	{
		super();
		this.category = category;
		this.rawMaterials = rawMaterials;
	}

	/**
	 * 
	 */
	public PPS_LTY_CatgMList()
	{
		super();
		this.rawMaterials = new ArrayList<String>();
	}

}
