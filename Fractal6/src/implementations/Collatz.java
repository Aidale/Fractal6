package implementations;

import fractal.Complex;
import fractal.F;

public class Collatz
{
	private static double oddCoefficient = 3;
	
	public static void init()
	{
		// Collatz currently does not support smooth coloring
	}
	
	public static void tick()
	{
//		oddCoefficient += 0.02;
		F.spectrumPhase--;
	}
	
	public static double getValue(Complex c)
	{
		int i;
		Complex z = c;
		for (i = 0; i < F.iterations && z.abs() < 40; i++)
		{
			// formula
			Complex angZ = Complex.multiplyReal(z, Math.PI / 2);
			Complex cos2 = Complex.square(Complex.cos(angZ));
			Complex sin2 = Complex.multiplyReal(Complex.addReal(cos2, -1), -1);
			Complex evenTerm = Complex.multiply(z, cos2);
			Complex oddTerm = Complex.multiply(Complex.addReal(Complex.multiplyReal(z, oddCoefficient), 1), sin2);
			Complex sum = Complex.multiplyReal(Complex.add(evenTerm, oddTerm), 0.5);
			z = sum;
		}	
		
		if (i == F.iterations)
		{
			return F.iterations;
		}
		
		return i + F.SMOOTH_COLORING * (-2 + Math.log(Math.log(z.abs())) / Math.log(2.0));
	}
}
