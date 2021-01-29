package fourier_series;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class FourierVisualization extends JFrame {
	
	private final int width;
	private final int height;
	private final ControlPanel controls;
	private final CircleGraph circle;
	private final FunctionGraph function;
	
	protected static float index = 0f;
	
	public FourierVisualization(int size) {
		this.width = size*2;
		this.height = size;
		
		circle = new CircleGraph(size);
		
		controls = new ControlPanel(size);
		
		function = new FunctionGraph(size);
		
		
		setVisible(true);
		setFocusable(true);
		setResizable(false);
		setTitle("Fourier Series");
		getContentPane().setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		setLayout(layout);
		
		getContentPane().add(circle);
		getContentPane().add(function);
		getContentPane().add(controls);
		
		pack();
		
		new Timer(5, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				function.update(circle.update());
				index += .01f;
			}
		}).start();
	}
	
}
