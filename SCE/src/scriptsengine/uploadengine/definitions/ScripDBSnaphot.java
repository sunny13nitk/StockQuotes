package scriptsengine.uploadengine.definitions;

import java.util.ArrayList;

import scriptsengine.pojos.OB_Scrip_General;

/**
 * Scrip DB Snaphot Base Type
 *
 */
public class ScripDBSnaphot
{
	private OB_Scrip_General			scRoot;

	private ArrayList<SheetDBSnapShot>	sheetsDBSS;

	/**
	 * @return the scRoot
	 */
	public OB_Scrip_General getScRoot()
	{
		return scRoot;
	}

	/**
	 * @param scRoot
	 *             the scRoot to set
	 */
	public void setScRoot(OB_Scrip_General scRoot)
	{
		this.scRoot = scRoot;
	}

	/**
	 * @return the sheetsDBSS
	 */
	public ArrayList<SheetDBSnapShot> getSheetsDBSS()
	{
		return sheetsDBSS;
	}

	/**
	 * @param sheetsDBSS
	 *             the sheetsDBSS to set
	 */
	public void setSheetsDBSS(ArrayList<SheetDBSnapShot> sheetsDBSS)
	{
		this.sheetsDBSS = sheetsDBSS;
	}

	/**
	 * @param scRoot
	 * @param sheetsDBSS
	 */
	public ScripDBSnaphot(OB_Scrip_General scRoot, ArrayList<SheetDBSnapShot> sheetsDBSS)
	{
		super();
		this.scRoot = scRoot;
		this.sheetsDBSS = sheetsDBSS;
	}

	/**
	 * 
	 */
	public ScripDBSnaphot()
	{
		super();
		this.sheetsDBSS = new ArrayList<SheetDBSnapShot>();
	}

}
