package implementations;

import fractal.Complex;
import fractal.F;

public class Mandelbrot
{
	public static void init()
	{

	}

	public static void tick()
	{
		F.spectrumPhase++;
		F.magnification *= 1.05;
	}

	public static double getValue(Complex c)
	{
		int i;
		Complex z = new Complex(0, 0);
		for (i = 0; i < F.iterations && z.abs() < 4; i++)
		{
			// formula
			z = Complex.square(z);
			z = Complex.add(z, c);
		}
		
		// if in set, return MAX_ITERATION - 1, if out of set return 0
		if (i == F.iterations)
		{
			return F.iterations;
		}
		
		return i + F.SMOOTH_COLORING * (-2.0 + Math.log(Math.log(z.abs())) / Math.log(2.0));
	}
}
