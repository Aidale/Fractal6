package fractal;

public class Complex
{
	public double r, i;

	public Complex(double r, double i)
	{
		this.r = r;
		this.i = i;
	}

	public double abs()
	{
		return Math.sqrt(r * r + i * i);
	}

	public String toString()
	{
		return "[" + r + ", " + i + "]";
	}

	public void NaN(String name)
	{
		double abs = abs();
		if (!(abs <= 0 || abs > 0))
		{
			System.out.println(name + ": " + this);
			System.exit(0);
		}
	}

	public void addEquals(Complex c)
	{
		r += c.r;
		i += c.i;
	}

	public void subtractEquals(Complex c)
	{
		r -= c.r;
		i -= c.i;
	}

	public void multiplyRealEquals(double re)
	{
		r *= re;
		i *= re;
	}

	public void multiplyEquals(Complex c)
	{
		double oldR = r;
		r = r * c.r - i * c.i;
		i = oldR * c.i + i * c.r;
	}

	public static Complex square(Complex c)
	{
		return new Complex(c.r * c.r - c.i * c.i, 2 * c.r * c.i);
	}

	public static Complex add(Complex c1, Complex c2)
	{
		return new Complex(c1.r + c2.r, c1.i + c2.i);
	}

	public static Complex add(Complex... complexes)
	{
		double r = 0, i = 0;
		for (Complex c : complexes)
		{
			r += c.r;
			i += c.i;
		}
		return new Complex(r, i);
	}
	
	public static Complex cos(Complex c)
	{
		Complex ic = Complex.multiplyImaginary(c, 1);
		double ePos = Math.exp(ic.r);
		double eNeg = Math.exp(-ic.r);
		double eSum = (ePos + eNeg) / 2;
		double eDif = (ePos - eNeg) / 2;
		Complex cos = new Complex(eSum * Math.cos(ic.i), eDif * Math.sin(ic.i));
		return cos;
	}
	
	public static Complex exp(Complex c)
	{
		double realConstant = Math.exp(c.r);
		double re = Math.cos(c.i);
		double im = Math.sin(c.i);
		return new Complex(realConstant * re, realConstant * im);
	}

	public static Complex subtract(Complex c1, Complex c2)
	{
		return new Complex(c1.r - c2.r, c1.i - c2.i);
	}

	public static Complex negative(Complex c)
	{
		return new Complex(-c.r, -c.i);
	}

	public static Complex multiply(Complex c1, Complex c2)
	{
		return new Complex(c1.r * c2.r - c1.i * c2.i, c1.r * c2.i + c1.i * c2.r);
	}

	public static Complex divide(Complex c1, Complex c2)
	{
		double denominator = c2.r * c2.r + c2.i * c2.i;
		return new Complex((c1.r * c2.r + c1.i * c2.i) / denominator, (c1.i * c2.r - c1.r * c2.i) / denominator);
	}

	public static Complex multiplyReal(Complex c, double r)
	{
		return new Complex(c.r * r, c.i * r);
	}

	public static Complex multiplyImaginary(Complex c, double i)
	{
		return new Complex(-c.i * i, c.r * i);
	}

	public static Complex multiplicativeInverse(Complex c)
	{
		double denominator = c.r * c.r + c.i * c.i;
		return new Complex(c.r / denominator, -c.i / denominator);
	}

	public static Complex addReal(Complex c, double r)
	{
		return new Complex(c.r + r, c.i);
	}

	public static Complex addImaginary(Complex c, double i)
	{
		return new Complex(c.r, c.i + i);
	}

	public static Complex additiveInverse(Complex c)
	{
		return new Complex(-c.r, -c.i);
	}

	public static Complex complement(Complex c)
	{
		return new Complex(c.r, -c.i);
	}
}
