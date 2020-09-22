package scriptsengine.statistics.JAXB.definitions;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Statistics Attributes List
 */

@XmlRootElement(name = "StatsAttrList")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatsAttrList
{
	private ArrayList<StatsAttrDetails> statAttrs;

	/**
	 * @return the statAttrs
	 */
	public ArrayList<StatsAttrDetails> getStatAttrs()
	{
		return statAttrs;
	}

	/**
	 * @param statAttrs
	 *             the statAttrs to set
	 */
	public void setStatAttrs(ArrayList<StatsAttrDetails> statAttrs)
	{
		this.statAttrs = statAttrs;
	}

	/**
	 * 
	 */
	public StatsAttrList()
	{
		super();
		this.statAttrs = new ArrayList<StatsAttrDetails>();
	}

	/**
	 * @param statAttrs
	 */
	public StatsAttrList(ArrayList<StatsAttrDetails> statAttrs)
	{
		super();
		this.statAttrs = statAttrs;
	}

}
