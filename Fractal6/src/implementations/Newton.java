package implementations;

import fractal.Complex;
import fractal.F;

public class Newton
{
	public static Complex[] roots;
	private static int angle1 = 0;
	private static int time = 0;

	public static void init()
	{
		roots = new Complex[6];
		for (int i = 0; i < roots.length; i++)
		{
			if (i % 3 == 0)
			{
				double angle = i / 3 * 180 * F.toRadians;
				Complex arm = new Complex(Math.cos(angle), Math.sin(angle));
				roots[i] = Complex.add(arm, new Complex(Math.cos(angle), Math.sin(angle)));
				roots[i + 1] = Complex.add(arm,
						new Complex(Math.cos(angle + 120 * F.toRadians), Math.sin(angle + 120 * F.toRadians)));
				roots[i + 2] = Complex.add(arm,
						new Complex(Math.cos(angle + 240 * F.toRadians), Math.sin(angle + 240 * F.toRadians)));
			}
		}
	}

	public static void tick()
	{
		for (int i = 0; i < roots.length; i++)
		{
			if (i % 3 == 0)
			{
				double angle = i / 3 * 180 * F.toRadians;
				Complex arm = new Complex(Math.cos(angle + angle1 * F.toRadians),
						Math.sin(angle + angle1 * F.toRadians));
				roots[i].subtractEquals(arm);
				roots[i + 1].subtractEquals(arm);
				roots[i + 2].subtractEquals(arm);

				Complex cw = new Complex(Math.cos(-2 * F.toRadians), Math.sin(-2 * F.toRadians));
				angle1 += 1;
				roots[i].multiplyEquals(cw);
				roots[i + 1].multiplyEquals(cw);
				roots[i + 2].multiplyEquals(cw);

				arm = new Complex(Math.cos(angle + angle1 * F.toRadians), Math.sin(angle + angle1 * F.toRadians));
				roots[i].addEquals(arm);
				roots[i + 1].addEquals(arm);
				roots[i + 2].addEquals(arm);
			}
		}

		F.rComponent = Math.cos(time * F.toRadians) / 2 + 1;
		F.gComponent = Math.cos((time + 120) * F.toRadians) / 2 + 1;
		F.bComponent = Math.cos((time + 240) * F.toRadians) / 2 + 1;

		time += 1;
	}

	public static int getValue(Complex c)
	{
		for (int iter = 0; iter < F.iterations; iter++)
		{
			Complex numerator = new Complex(1, 0);
			Complex denominator = new Complex(0, 0);
			for (int i = 0; i < roots.length; i++)
			{
				Complex term = new Complex(1, 0);
				for (int j = 0; j < roots.length; j++)
				{
					if (j == i)
						continue;
					term.multiplyEquals(Complex.subtract(c, roots[j]));
				}
				numerator.multiplyEquals(Complex.subtract(c, roots[i]));
				denominator.addEquals(term);
			}

			Complex step = Complex.divide(numerator, denominator);
			c.subtractEquals(step);
		}

		double minDist = Double.MAX_VALUE;
		int closestRoot = 0;
		for (int i = 0; i < roots.length; i++)
		{
			double dist = Complex.subtract(c, roots[i]).abs();
			if (dist < minDist)
			{
				minDist = dist;
				closestRoot = i;
			}
		}
		return closestRoot;
	}
}
