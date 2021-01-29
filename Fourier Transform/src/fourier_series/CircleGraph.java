package fourier_series;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class CircleGraph extends JPanel{
	
	private static final Color[] colors = 
	{Color.RED,
	Color.GREEN, 
	Color.BLACK, 
	Color.BLUE, 
	Color.PINK, 
	Color.MAGENTA, 
	Color.YELLOW, 
	Color.CYAN, 
	Color.ORANGE, 
	new Color(180, 0, 180)};
	private Graphics2D g2d;
	private Image image;
	private Image graph;
	private final int size;
	protected static int maxRadius;
	protected static int circles = 1;
	
	protected CircleGraph(int size) {
		this.size = 4*size/5;
		this.graph = createBackground();
		maxRadius = 49*(13*this.size/15)/100;
		
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(this.size, this.size));
		setVisible(true);
	}
	
	protected void paintComponent(Graphics g) {
		if(image == null) {
			image = createImage(size, size);
			g2d = (Graphics2D) image.getGraphics();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	public int update() {
		float theta = FourierVisualization.index;
		g2d.drawImage(graph, 0, 0, null);
		
		/*
		 * draws the main circle
		 */
		int radius = ControlPanel.radii[0].getValue();
		int cx = size/2, cy = size/2;
		g2d.setStroke(new BasicStroke(2));
		g2d.setPaint(Color.RED);
		g2d.drawOval(cx - radius, cx - radius, radius*2, radius*2);
		
		/*
		 * draws the circles
		 */
		int pcx, pcy;
		pcx = pcy = cx;
		cx = cx + (int)(Math.cos(theta)*(double)radius);
		cy = cy + (int)(Math.sin(theta)*(double)radius);
		g2d.drawLine(pcx, pcy, cx, cy);
		for(int i = 1; i < circles; i++) {
			radius = ControlPanel.radii[i].getValue();
			g2d.setPaint(colors[i]);
			g2d.drawOval(cx - radius, cy - radius, radius*2, radius*2);
			
			pcx = cx;
			pcy = cy;
			cx = cx + (int)(Math.cos(theta*(i+1))*(double)radius);
			cy = cy + (int)(Math.sin(theta*(i+1))*(double)radius);
			g2d.drawLine(pcx, pcy, cx, cy);
		}
		g2d.setPaint(Color.DARK_GRAY);
		g2d.drawLine(cx, cy, size, cy);
		
		repaint();
		return cy;
	}
	
	
	protected static void addCircle() {
		if(circles >= 10) return;
		circles++;
		for(int i = 0; i < ControlPanel.radii.length; i++) {
			ControlPanel.radii[i].setValue(maxRadius/(circles));
		}
	}
	
	protected static void removeCircle() {
		if(circles <= 1) return;
		circles--;
		for(int i = 0; i < ControlPanel.radii.length; i++) {
			ControlPanel.radii[i].setValue(maxRadius/(circles));
		}
	}
	
	private Image createBackground() {
		BufferedImage background = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) background.getGraphics();
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[]{2}, 0);
		Stroke normal = new BasicStroke(1);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 20);
		Color dashColor = new Color(200, 200, 200);
		g.setFont(font);
		g.setPaint(Color.WHITE);
		g.fillRect(0, 0, size, size);
		
		int line = size/70;
		int go = size/15, gsize = size - go*2;
		int units = 6;
		int div = gsize/units;
		
		
		for(int i = 1; i < units; i++) {
			int length = i*div, height = gsize+go, width = go+length;
			String num = "  " + (units/2 - i) + "";
			
			g.setPaint(dashColor);
			g.setStroke(dashed);
			g.drawLine(width, height, width, go);
			g.drawLine(go, width, height, width);
			
			g.setPaint(Color.BLACK);
			g.setStroke(normal);
			g.drawLine(width, height, width, height-line);
			g.drawString(num, size - width - (font.getSize()*num.length()/2), height + font.getSize());
			g.drawLine(go, width, go+line, width);
			g.drawString(num, go - (int)(font.getSize()*num.length()*.67), width + font.getSize()/3);
		}
		
		
		g.setPaint(Color.BLACK);
		g.setStroke(normal);
		g.drawRect(go, go, gsize, gsize);
		
		return background;
	}
	
}