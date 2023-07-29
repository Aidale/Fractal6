package render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fractal.F;

public class Shader
{
	public static Color[] COLORS, BASE;
	public static BufferedImage[] frames;

	/**
	 * Creates the array of buffered images that will store the frame(s)
	 * Calls {@code initPalette()}
	 */
	public static void init()
	{
		frames = new BufferedImage[F.FRAME_COUNT];
		getBasePalette();
	}

	/**
	 * Passes the first frame of {@code frames} to the display to be shown
	 */
	public static void loadToDisplay()
	{
		Display.init(frames[0]);
	}

	/**
	 * If frames isn't null, and has only one element, then save that 
	 * element to a PNG file at the path specified in {@code F}. If
	 * frames has more than one element, then save them as GIF file
	 * at the path specified in {@code F}. 
	 */
	public static void save()
	{
		if (frames == null)
		{
			return;
		}
		
		if (frames.length == 1)
		{
			loadToPNG();
		}
		else
		{
			loadToGIF();
		}
	}
	
	/**
	 * Passes {@code frames} to {@code Giffer.generateFromBI} to be turned into a GIF 
	 */
	private static void loadToGIF()
	{
		try
		{
			String GIFPath = createNewFile(F.GIF_OUTPUT, "gif").getPath();
			Giffer.generateFromBI(frames, GIFPath, F.DELAY, true);
			System.out.println("Successfully created gif at " + GIFPath);
		} 
		catch (Exception e)
		{
			System.err.println("Could not load images into .gif format @Shader.loadToGIF");
			e.printStackTrace();
		}
	}

	/**
	 * Passes the first frame of {@code frame} to be saved as an image file at the path specified by
	 * {@code F.PNG_OUTPUT}
	 */
	private static void loadToPNG()
	{
		try
		{
			File file = createNewFile(F.PNG_OUTPUT, "png");
			ImageIO.write(frames[0], "PNG", file);
			System.out.println("Successfully created png at " + file.getPath());
		} 
		catch (Exception e)
		{
			System.err.println("Could not load image into .png format @Shader.loadToPNG");
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new file at path, an increments a counter if the file at 
	 * path already exists. When it finds a file location that doesn't exist,
	 * returns a new file at the String {@code path + index}
	 * 
	 * @param path the file path to be used to store a GIF or PNG
	 * @return a new File at the path provided. This file does not overwrite any existing files
	 */
	private static File createNewFile(String path, String extension)
	{
		File file = new File(path + "." + extension);
		for (int i = 2; file.exists(); i++)
		{
			file = new File(path + " - " + i + "." + extension);
		}
		return file;
	}
	
	//TODO interpolate frames of a GIF, bezier curves
	
	
	/**
	 * Shrinks the {@code BufferedImage} uncompressed by a factor of {@code F.COMPRESSION}, 
	 * averaging the color values.
	 * 
	 * @param uncompressed	the initial {@code BufferedImage}
	 * @return the compressed version of uncompressed
	 */
	public static BufferedImage compress(BufferedImage uncompressed)
	{
		int a = F.COMPRESSION;
		if (a == 1)
		{
			return uncompressed;
		}
		
		int count = F.COMPRESSION * F.COMPRESSION;
		BufferedImage compressed = new BufferedImage(F.WIDTH, F.HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < F.C_HEIGHT; y += F.COMPRESSION)
		{
			for (int x = 0; x < F.C_WIDTH; x += F.COMPRESSION)
			{
				int r = 0, g = 0, b = 0;
				for (int dy = 0; dy < F.COMPRESSION; dy++)
				{
					for (int dx = 0; dx < F.COMPRESSION; dx++)
					{
						Color c = new Color(uncompressed.getRGB(x + dx, y + dy));
						r += c.getRed();
						g += c.getGreen();
						b += c.getBlue();
					}
				}
				r /= count;
				g /= count;
				b /= count;
				compressed.setRGB(x / F.COMPRESSION, y / F.COMPRESSION, new Color(r, g, b).getRGB());
			}
		}
		return compressed;
	}

	/**
	 * Create a new frame in {@code frames} at index and colors it according to the 2D double array 
	 * based on settings provided in {@code F}
	 * 
	 * @param index		the index at which the new {@code BufferedImage} is stored
	 * @param values	the 2D double array of values to be converted into a 2D array of pixel colors
	 */
	public static void loadFrame(int index, double[][] values)
	{
		frames[index] = new BufferedImage(F.C_WIDTH, F.C_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < F.C_HEIGHT; y++)
		{
			for (int x = 0; x < F.C_WIDTH; x++)
			{
				if (F.FILL_BLACK && values[y][x] == F.iterations)
				{
					frames[index].setRGB(x, y, new Color(0, 0, 0).getRGB());
				}
				else if (F.SMOOTH_COLORING != 0)
				{
					boolean lerp = true;
					boolean cerp = false;
					if (lerp)
					{
						// lerp between given color values, when values[y][x] is not an integer
						int floorValue = (int)values[y][x];
						
						int lowColorIndex  = mod(floorValue + F.spectrumPhase, COLORS.length);
						int highColorIndex = mod(floorValue + F.spectrumPhase + 1, COLORS.length);
						
						Color low  = COLORS[lowColorIndex];
						Color high = COLORS[highColorIndex];

						Color inter = lerp(low, high, values[y][x] - floorValue);
						Color processed = processColor(inter);
						frames[index].setRGB(x, y, processed.getRGB());
					}
					else if (cerp)
					{
						// cerp between given color values, when values[y][x] is not an integer
						int floor = (int)values[y][x];
						
						int indexA = mod(floor + F.spectrumPhase - 1, COLORS.length);
						int indexB = mod(floor + F.spectrumPhase    , COLORS.length);
						int indexC = mod(floor + F.spectrumPhase + 1, COLORS.length);
						int indexD = mod(floor + F.spectrumPhase + 2, COLORS.length);
						
						Color a = COLORS[indexA];
						Color b = COLORS[indexB];
						Color c = COLORS[indexC];
						Color d = COLORS[indexD];
						
						Color inter = cerp(a, b, c, d, values[y][x] - floor);
						Color processed = processColor(inter);
						frames[index].setRGB(x, y, processed.getRGB());
					}
				} 
				else
				{
					Color unprocessed = COLORS[((int)values[y][x] + F.spectrumPhase) % COLORS.length];
					Color processed = processColor(unprocessed);
					frames[index].setRGB(x, y, processed.getRGB());
				}
			}
		}
		
		frames[index] = compress(frames[index]);
		System.out.println("Frame " + index + " finished");
		
		
	}
	
	/**
	 * Performs the operation a mod (base) and returns the value
	 * 
	 * @param a
	 * @param base
	 * @return a mod(base)
	 */
	private static int mod(int a, int base)
	{
		int m = a % base;
		if (a < 0) m += base;
		return m;
	}

	/**
	 * Linearly interpolates between the colors c1 and c2
	 * 
	 * @param c1 the first bound of the interpolation
	 * @param c2 the second bound of the interpolation
	 * @param t the value at which to interpolate based on c1 and c2
	 * @return the interpolated color
	 */
	public static Color lerp(Color c1, Color c2, double t)
	{
		int r = (int) ((1.0 - t) * c1.getRed() + t * c2.getRed());
		int g = (int) ((1.0 - t) * c1.getGreen() + t * c2.getGreen());
		int b = (int) ((1.0 - t) * c1.getBlue() + t * c2.getBlue());
		return new Color(r, g, b);
	}
	
	/**
	 * Quadratically interpolations between the colors c1, c2, and c2.
	 * 
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param t
	 * @return
	 */
	public static Color qerp(Color c1, Color c2, Color c3, double t)
	{
		Color l12 = lerp(c1, c2, t);
		Color l23 = lerp(c2, c3, t);
		Color q123 = lerp(l12, l23, t);
		return q123;
	}
	
	/**
	 * Cubically interpolates between the colors c1, c2, c3, and c4
	 * 
	 * @param c1
	 * @param c2
	 * @param c3
	 * @param c4
	 * @param t
	 * @return
	 */
	public static Color cerp(Color c1, Color c2, Color c3, Color c4, double t)
	{
		Color q123 = qerp(c1, c2, c3, t);
		Color q234 = qerp(c2, c3, c4, t);
		Color c1234 = lerp(q123, q234, t);
		return c1234;
	}
	
	/**
	 * Linearly interpolates between n colors, where n is the length of parameter colors.
	 * This is a generalization of lerp, qerp, and cerp.
	 * 
	 * @param colors The colors to be interpolated between
	 * @param t The distance on the interpolated path between 0 and 1 inclusive
	 * @return The color at t along the interpolated path
	 */
	private static Color nerp(Color[] colors, double t)
	{
		if (colors.length == 1)
		{
			return colors[0];
		}
		
		Color[] lerped = new Color[colors.length - 1];
		for (int i = 0; i < lerped.length; i++)
		{
			lerped[i] = lerp(colors[i], colors[i + 1], t); 
		}
		return nerp(lerped, t);
	}
	

	/**
	 * Creates the palette, an array of colors that determines what each value from the 2D double array
	 * maps to which color. The palette is a lerp of the initial palette read from a file.
	 */
	private static void getBasePalette()
	{
		//TODO implement bezier curves within 3D color space
		BufferedImage palette = null;
		try
		{
			palette = ImageIO.read(new File(F.PALETTE_INPUT));
		} 
		catch (IOException ex)
		{
			System.err.println("Error fetching palette image @Shader.initPalette");
			System.exit(0);
		}

		BASE = new Color[palette.getWidth()];
		for (int i = 0; i < BASE.length; i++)
		{
			BASE[i] = new Color(palette.getRGB(i, 0));
		}

		// Currently this code lerps the color palette (if F.spectrumWavelength > 1), 
		// negating the effects of cerping the color values at post processing in loadFrame()
		
		int index = 0;

		COLORS = new Color[F.spectrumWavelength * BASE.length];
		for (int i = 0; i < BASE.length; i++)
		{
			// currently lerping with 'i' serving as 't' value
			Color base1 = BASE[i];
			Color base2 = BASE[(i + 1) % BASE.length];

			double dr = (base2.getRed() - base1.getRed()) / (double) F.spectrumWavelength;
			double dg = (base2.getGreen() - base1.getGreen()) / (double) F.spectrumWavelength;
			double db = (base2.getBlue() - base1.getBlue()) / (double) F.spectrumWavelength;

			double r = base1.getRed();
			double g = base1.getGreen();
			double b = base1.getBlue();

			for (int j = 0; j < F.spectrumWavelength; j++)
			{
				COLORS[index++] = new Color((int) r, (int) g, (int) b);
				r += dr;
				g += dg;
				b += db;
			}
		}
		
	}
	
	public static void saveRefinedPalette(String path, double deltaT)
	{
		int numColors = (int)(Math.ceil(1 / deltaT));
		BufferedImage refined = new BufferedImage(numColors, numColors, BufferedImage.TYPE_INT_RGB);
		
		Color[] cycle = new Color[BASE.length + 1];
		for (int i = 0; i < BASE.length; i++)
		{
			cycle[i] = BASE[i];
		}
		cycle[BASE.length] = cycle[0];
		
		int x = 0;
		for (double t = 0; t < 1 && x < numColors; t += deltaT, x++)
		{			
			Color inter = nerp(cycle, t);
			
//			int indexA = mod(floor - 1, BASE.length);
//			int indexB = mod(floor    , BASE.length);
//			int indexC = mod(floor + 1, BASE.length);
//			int indexD = mod(floor + 2, BASE.length);
//			
//			Color a = BASE[indexA];
//			Color b = BASE[indexB];
//			Color c = BASE[indexC];
//			Color d = BASE[indexD];
			
//			Color inter = cerp(a, b, c, d, t - floor);
			// inter = lerp(b, c, t - floor);
			// inter = qerp(a, b, c, t - floor);
			
			Color processed = processColor(inter);
			
			for (int y = 0; y < numColors; y++)
			{
				refined.setRGB(x, y, processed.getRGB());
			}
		}
		
		try
		{
			File file = createNewFile(path, "png");
			ImageIO.write(refined, "PNG", file);
			System.out.println("Successfully created palette at " + file.getPath());
		} 
		catch (Exception e)
		{
			System.err.println("Could not load image into .png format @Shader.saveRefinedPalette");
			e.printStackTrace();
		}
	}

	/**
	 * Does some color post-processing based on settings provided in {@code F}. Modifies the rgb 
	 * values of c
	 * 
	 * @param c the color to be modified
	 * @return the modified color
	 */
	private static Color processColor(Color c)
	{
		int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
		if (F.rComponent < 0)
			r = (255 - r);
		if (F.gComponent < 0)
			g = (255 - g);
		if (F.bComponent < 0)
			b = (255 - b);

		int ir = colorBound(r / Math.abs(F.rComponent));
		int ig = colorBound(g / Math.abs(F.gComponent));
		int ib = colorBound(b / Math.abs(F.bComponent));
		return new Color(ir, ig, ib);
	}

	/**
	 * Restricts a to be between 0 and 255 inclusive and converts 
	 * it from a double to an int
	 * 
	 * @param a double value to be restricted
	 * @return the restricted double value
	 */
	private static int colorBound(double a)
	{
		int x = (int) a;
		return x > 255 ? 255 : x < 0 ? 0 : x;
	}
}