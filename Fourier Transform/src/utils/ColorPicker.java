package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ColorPicker extends JPanel {
	
	private static final int gap = 4;
	private final int width, height;
	private final JPanel color;
	private final Set<Runnable> runnables = new HashSet<Runnable>();
	
	public ColorPicker(int size) {
		this.height = 3*size/4;
		this.width = size;
		
		
		ColorWheel wheel = new ColorWheel(height/2 - gap/2, 1.0f, getBackground());
		
		JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
		slider.setPreferredSize(new Dimension(width/4 - gap, 3*height/4 - gap));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				wheel.setValue(slider.getValue());
			}
		});
		
		color = new JPanel();
		color.setPreferredSize(new Dimension(width/4 - gap, height/4 - gap));
		color.setBackground(Color.WHITE);
		color.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		
		setPreferredSize(new Dimension(width, height));
		setVisible(true);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		gbc.gridheight = 2;
		gbc.gridx = 0;
		add(wheel, gbc);
		
		gbc.gridy = 0;
		gbc.gridx = 1;
		gbc.gridheight = 1;
		add(slider, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 1;
		add(color, gbc);
	}
	
	protected void setColor(Color color) {
		this.color.setBackground(color);
		this.color.repaint();
		for(Runnable runnable : runnables) {
			runnable.run();
		}
	}
	
	public void addColorChangeListener(Runnable runnable) {
		runnables.add(runnable);
	}
	
	public void removeColorChangeListener(Runnable runnable) {
		runnables.remove(runnable);
	}
	
	public Color getColor() {
		return color.getBackground();
	}
	
}

@SuppressWarnings("serial")
class ColorWheel extends JPanel {
	
	/*
	 * when it comes to making color wheels,
	 * a. take a cartesian coordinate (x, y), then convert it to polar coordinate (r, theta)
	 * b. take value constant
	 * c. make an HSV (hue, saturation, value) color where H = theta, S = r, and V = value
	 */
	private static final float tau = (float)Math.PI*2f;
	private static final int selectedRadius = 5;
	private final int size;
	private final int radius;
	private final BufferedImage wheel;
	private final Color background;
	private Image image;
	private Graphics2D g2d;
	private int x, y;
	
	protected ColorWheel(int radius, float value, Color background) {
		this.radius = radius;
		this.size = radius*2;
		this.background = background;
		this.wheel = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		this.x = this.y = radius;
		
		drawWheel(value);
		setPreferredSize(new Dimension(size, size));
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = Math.max(Math.min(e.getX(), size-1), 0);
				int y = Math.max(Math.min(e.getY(), size-1), 0);
				Color color = new Color(wheel.getRGB(x, y));
				if(!color.equals(background)) {
					((ColorPicker)getParent()).setColor(color);
					ColorWheel.this.x = x;
					ColorWheel.this.y = y;
					g2d.drawImage(wheel, 0, 0, null);
					g2d.drawOval(x - selectedRadius, y - selectedRadius, selectedRadius*2, selectedRadius*2);
					repaint();
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(image == null) {
			image = createImage(size, size);
			g2d = (Graphics2D) image.getGraphics();
			g2d.drawImage(wheel, 0, 0, null);
			g2d.setPaint(Color.BLACK);
			g2d.drawOval(x-selectedRadius, y-selectedRadius, selectedRadius*2, selectedRadius*2);
		}
		g.drawImage(image, 0, 0, null);
	}
	
	protected void drawWheel(float value) {
		float r, theta;
		int ydiff, xdiff;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				xdiff = radius - x;
				ydiff = radius - y;
				theta = (float)Math.atan2(ydiff, xdiff) + (float)Math.PI;
				r = (float)Math.hypot(xdiff, ydiff);
				if((int)(r+.5f) <= radius) wheel.setRGB(x, y, Color.HSBtoRGB(theta/tau, r/(float)radius, value));
				else wheel.setRGB(x, y, background.getRGB());
			}
		}
	}
	
	protected void setValue(int value) {
		drawWheel((float)value/100f);
		g2d.drawImage(wheel, 0, 0, null);
		repaint();
		if(x != -1 && y != -1) {
			((ColorPicker)getParent()).setColor(new Color(wheel.getRGB(x, y)));
			int num = 255 - (int)((double)value*2.5);
			g2d.setPaint(new Color(num, num, num));
			g2d.drawOval(x - selectedRadius, y - selectedRadius, selectedRadius*2, selectedRadius*2);
		}
	}
	
}
