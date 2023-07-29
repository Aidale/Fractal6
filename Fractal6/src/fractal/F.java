package fractal;

import render.Display;
import render.Shader;

public class F
{
	public static final double toRadians = Math.PI / 180;

	public static final int WIDTH = 2160, HEIGHT = 3456, FRAME_COUNT = 1, DELAY = 4, COMPRESSION = 2,
			C_WIDTH = WIDTH * COMPRESSION, C_HEIGHT = HEIGHT * COMPRESSION;
	public static int iterations = 500;
	public static double centerX = -0.743643887037158704752191506114774, centerY = -0.131825904205311970493132056385139, magnification = 300 * 400, 
			rComponent = 1, gComponent = 1, bComponent = 1;

	// value ranges between -1 and 1
	// -1: Smooth coloring	1: Reverse smooth coloring  0: No smooth coloring
	public static double SMOOTH_COLORING = -1;

	public static boolean FILL_BLACK = true;
	public static int spectrumPhase = 0, spectrumWavelength = 7;
	public static final String 
			PALETTE_INPUT = "res/paletteTemplates/palette3.png",
			PNG_OUTPUT    = "res/background",
			GIF_OUTPUT    = "res/annie";
			
	public static void main(String[] args)
	{
		// the first step is to initialize the shaders
		Shader.init();

		// test out palettes
		// Shader.saveRefinedPalette("res/palettes/palette", 0.001);
		
		// then choose a fractal to create
		// Fractal.collatz();
		 Fractal.mandelbrot();
//		Fractal.julia();

		// depending on if we are creating an image or a GIF, store an image or a GIF
		Shader.save();
		
		// show the image or the first frame of the GIF to the display
		Shader.loadToDisplay();
		
		// open the display
		Display.openDisplay();
	}
}
