package scriptsengine.utilities.implementations;

import org.springframework.stereotype.Service;

import scriptsengine.enums.SCEenums.direction;
import scriptsengine.utilities.interfaces.IDeltaCalcService;

@Service
public class DeltaCalcService implements IDeltaCalcService
{

	@Override
	public double getDeltaPercentage(double fromVal, double toVal)
	{
		double delta = 0;

		delta = ((toVal - fromVal) / fromVal) * 100;

		return delta;
	}

	@Override
	public double getDelta(double fromVal, double toVal)
	{
		double delta = 0;

		delta = ((toVal - fromVal) / fromVal);

		return delta;
	}

	@Override
	public double getDeltaPercentage(int fromVal, int toVal)
	{
		double delta = 0;

		delta = ((toVal - fromVal) / fromVal) * 100;

		return delta;
	}

	@Override
	public double getDelta(int fromVal, int toVal)
	{
		double delta = 0;

		delta = ((toVal - fromVal) / fromVal);

		return delta;
	}

	@Override
	public double adjustNumberbyPercentage(double numtoAdjust, double percentage, direction direction)
	{
		double num = numtoAdjust;

		if (percentage != 0)
		{
			double perportion = percentage / 100 * numtoAdjust;

			if (direction == scriptsengine.enums.SCEenums.direction.INCREASE)
			{
				num = num + perportion;
			}
			else if (direction == scriptsengine.enums.SCEenums.direction.DECREASE)
			{
				num = num - perportion;
			}
		}

		return num;
	}

}
