package scriptsengine.uploadengineSC.appUtilities.defn;

public class YearEpsDps_PoJo
{
	public int	Year;
	public double	EPS;
	public double	DPS;

	public int getYear()
	{
		return Year;
	}

	public void setYear(int year)
	{
		Year = year;
	}

	public double getEPS()
	{
		return EPS;
	}

	public void setEPS(double ePS)
	{
		EPS = ePS;
	}

	public double getDPS()
	{
		return DPS;
	}

	public void setDPS(double dPS)
	{
		DPS = dPS;
	}

	public YearEpsDps_PoJo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public YearEpsDps_PoJo(int year, double ePS, double dPS)
	{
		super();
		Year = year;
		EPS = ePS;
		DPS = dPS;
	}

}
