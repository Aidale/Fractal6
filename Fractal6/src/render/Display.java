package render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fractal.F;

public class Display
{
	private static BufferedImage image;

	/**
	 * Sets the private static BufferedImage field of {@code Display} equal to image so it can be displayed
	 * @param image the bufferedimage to be displayed
	 */
	public static void init(BufferedImage image)
	{
		Display.image = image;
	}

	/**
	 * Creates the display using a modified JPanel class, draws the image to the JPanel,
	 * creates a new JFrame, adds the JPanel class to the JFrame
	 */
	public static void openDisplay()
	{
		JPanel panel = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				g.drawImage(image, 10, 10, null);
			}
		};

		JFrame frame = new JFrame("ITERATION: " + F.iterations + ", MAGNIFICATION: " + F.magnification + ", CENTER_X: "
				+ F.centerX + ", CENTER_Y: " + F.centerY + ", COMPRESSION: " + F.COMPRESSION);
		
		frame.setSize(F.WIDTH + 35, F.HEIGHT + 20);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		panel.repaint();
	}
}
