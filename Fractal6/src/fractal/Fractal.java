package fractal;

import implementations.Collatz;
import implementations.Julia;
import implementations.Mandelbrot;
import implementations.Newton;
import render.Shader;

public class Fractal
{
	public static void mandelbrot()
	{
		Mandelbrot.init();
		for (int t = 0; t < F.FRAME_COUNT; Mandelbrot.tick(), t++)
		{
			double[][] values = new double[F.C_HEIGHT][F.C_WIDTH];
			for (int jy = 0; jy < F.C_HEIGHT; jy++)
			{
				for (int jx = 0; jx < F.C_WIDTH; jx++)
				{
					double cx = toCartesianX(jx), cy = toCartesianY(jy);
					values[jy][jx] = Mandelbrot.getValue(new Complex(cx, cy));
				}
			}
			Shader.loadFrame(t, values);
		}
	}

	public static void julia()
	{
		Julia.init();
		for (int t = 0; t < F.FRAME_COUNT; Julia.tick(), t++)
		{
			double[][] values = new double[F.C_HEIGHT][F.C_WIDTH];
			for (int jy = 0; jy < F.C_HEIGHT; jy++)
			{
				for (int jx = 0; jx < F.C_WIDTH; jx++)
				{
					double cx = toCartesianX(jx), cy = toCartesianY(jy);
					values[jy][jx] = Julia.getValue(new Complex(cx, cy));
				}
			}
			Shader.loadFrame(t, values);
		}
	}

	public static void newton()
	{
		Newton.init();
		for (int t = 0; t < F.FRAME_COUNT; Newton.tick(), t++)
		{
			double[][] values = new double[F.C_HEIGHT][F.C_WIDTH];
			for (int jy = 0; jy < F.C_HEIGHT; jy++)
			{
				for (int jx = 0; jx < F.C_WIDTH; jx++)
				{
					double cx = toCartesianX(jx), cy = toCartesianY(jy);
					values[jy][jx] = Newton.getValue(new Complex(cx, cy));
				}
			}
			Shader.loadFrame(t, values);
		}
	}
	
	public static void collatz()
	{
		Collatz.init();
		for (int t = 0; t < F.FRAME_COUNT; Collatz.tick(), t++)
		{
			double[][] values = new double[F.C_HEIGHT][F.C_WIDTH];
			for (int jy = 0; jy < F.C_HEIGHT; jy++)
			{
				for (int jx = 0; jx < F.C_WIDTH; jx++)
				{
					double cx = toCartesianX(jx), cy = toCartesianY(jy);
					values[jy][jx] = Collatz.getValue(new Complex(cx, cy));
				}
			}
			Shader.loadFrame(t, values);
		}
	}

	private static double toCartesianX(int javaX)
	{
		return (javaX - F.C_WIDTH / 2) / (F.magnification * F.COMPRESSION) + F.centerX;
	}

	private static double toCartesianY(int javaY)
	{
		return (F.C_HEIGHT / 2 - javaY) / (F.magnification * F.COMPRESSION) + F.centerY;
	}
}
