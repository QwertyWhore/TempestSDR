package martin.tempest;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ImageVisualizer extends JPanel {

	private static final long serialVersionUID = -6754436015453195809L;
	private static final int COUNT_TO_AVG = 50;
	
	private BufferedImage todraw = null;

	private long prev = System.currentTimeMillis();
	private int count = 0;
	private int fps;
	
	public void drawImage(final BufferedImage image) {
		
		if (todraw != null) {
			synchronized (todraw) {
				todraw = image;
			}
		} else
			todraw = image;
		
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {

		count++;
		if (count > COUNT_TO_AVG) {
			final long now = System.currentTimeMillis();
			fps = (int) Math.round((1000.0 * COUNT_TO_AVG) / (double) (now - prev));
			count = 0;
			prev = now;
		}
		
		if (todraw != null) {
			synchronized (todraw) {
				g.drawImage(todraw, 0, 0, getWidth(), getHeight(), null);
			}
		} else {
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 40, 20);
		g.setColor(Color.black);
		g.drawString(fps+" fps", 10, 15);
		
	}
	
	public void saveImageToPNGFile(final File dir) {
		final File file = new File(dir, new Timestamp((new java.util.Date()).getTime()).toString().replace(" ", "").replace(":", "")+".png");
		if (todraw != null) {
			synchronized (todraw) {
				try {
					ImageIO.write(todraw, "png", file);
					System.out.println("File "+file+" saved!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else
			System.err.println("No file on screen to write!");
		
	}
}
