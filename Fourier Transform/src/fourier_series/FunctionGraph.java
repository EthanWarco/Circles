package fourier_series;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class FunctionGraph extends JPanel {
	
	private static final Stroke line = new BasicStroke(2);
	
	private final int width;
	private final int height;
	private final int unit;
	private final int xend;
	private final int gdist;
	private final List<Line2D> lines = new ArrayList<Line2D>();
	private Graphics2D g2d;
	private Image image;
	private Image background;
	
	private int prevy = 0;
	private int prevx = 0;
	private float xmovement = 0f;
	private final float xadd;
	
	protected FunctionGraph(int size) {
		this.width = 6*size/5;
		this.height = 4*size/5;
		this.background = createBackground();
		this.unit = CircleGraph.maxRadius/3;
		this.gdist = width - height/15 - 1;
		this.xend = gdist - (int)(Math.PI*2.5*(double)unit);
		this.xadd = (float)unit*.01f;
		this.background = createBackground();
		
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(this.width, this.height));
		setVisible(true);
	}
	
	protected void paintComponent(Graphics g) {
		if(image == null) {
			image = createImage(width, height);
			g2d = (Graphics2D) image.getGraphics();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	protected void update(int y) {
		g2d.translate(-xmovement, 0);
		int xpos = Math.max(gdist - (int)(FourierVisualization.index*(float)unit + .5f), xend);
		if(FourierVisualization.index == 0f) {
			prevy = y;
			prevx = xpos;
		}
		g2d.drawImage(background, 0, 0, null);
		
		/*
		 * draws line
		 */
		g2d.setStroke(line);
		g2d.setPaint(Color.DARK_GRAY);
		g2d.drawLine(0, y, xpos, y);
		
		/*
		 * Draws Function
		 */
		lines.add(0, new Line2D.Float(xpos - xmovement, y, prevx - xmovement, prevy));
		if(xpos == xend) {
			xmovement+=xadd;
			g2d.translate(xmovement, 0);
			lines.remove(lines.size()-1);
		}
		for(Line2D line : lines) {
			g2d.draw(line);
		}
		
		repaint();
		prevy = y;
		prevx = xpos;
	}
	
	private Image createBackground() {
		BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) background.getGraphics();
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[]{2}, 0);
		Stroke normal = new BasicStroke(1);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 20);
		Color dashColor = new Color(200, 200, 200);
		g.setFont(font);
		g.setPaint(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		int line = height/70;
		int go = height/15, gwidth = width - go*2, gheight = height - go*2;
		int hunits = 6;
		int div = gheight/hunits;
		int wunits = (int)Math.round((double)gwidth/(double)div);
		
		
		for(int i = 1; i < hunits; i++) {
			int length = i*div;
			String num = "  " + (hunits/2 - i) + "";
			
			g.setPaint(dashColor);
			g.setStroke(dashed);
			g.drawLine(go, go+length, go+gwidth, go+length);
			
			g.setPaint(Color.BLACK);
			g.setStroke(normal);
			g.drawLine(go, go+length, go+line, go+length);
			
			g.drawString(num, go - (int)(font.getSize()*num.length()*.67), go + length + font.getSize()/3);
		}
		for(int i = 0; i < wunits; i++) {
			int length = i*div;
			
			g.setPaint(dashColor);
			g.setStroke(dashed);
			g.drawLine(go+length, go+gheight, go+length, go);
			
			g.setPaint(Color.BLACK);
			g.setStroke(normal);
			g.drawLine(go+length, go+gheight, go+length, go+gheight-line);
			
			g.drawString("" + i + "", go + length - (int)((double)font.getSize()*.33), (go+gheight) + font.getSize());
		}
		
		
		g.drawRect(go, go, gwidth, gheight);
		
		return background;
	}
	
}
