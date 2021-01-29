package fourier_series;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
class ControlPanel extends JPanel implements ActionListener {
	
	private final int width;
	private final int height;
	private final JButton add;
	private final JButton remove;
	
	protected static final JSlider[] radii = new JSlider[10];
	private final JPanel panels[] = new JPanel[radii.length];
	
	protected ControlPanel(int size) {
		this.width = size*2;
		this.height = size/5;
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 15);
		
		
		remove = new JButton("Remove Circle");
		remove.setFont(font);
		remove.setEnabled(false);
		remove.addActionListener(this);
		
		add = new JButton("Add Circle");
		add.setFont(font);
		add.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2, 0, 10, 10));
		buttons.add(add);
		buttons.add(remove);
		
		
		setPreferredSize(new Dimension(this.width, this.height));
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
		setVisible(true);
		
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		add(buttons);
		
		double unit = (double)CircleGraph.maxRadius/3.0;
		for(int i = 0; i < radii.length; i++) {
			panels[i] = new JPanel();
			panels[i].setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			panels[i].setPreferredSize(new Dimension((width - (int)buttons.getPreferredSize().getWidth())/10, height - 1));
			panels[i].setVisible(false);
			
			JLabel label = new JLabel("<html>Circle " + (i+1) + "<br/>Radius: " + round((double)CircleGraph.maxRadius/unit) + "</html>");
			label.setFont(font.deriveFont(12f));
			
			
			radii[i] = new JSlider(JSlider.VERTICAL, 1, CircleGraph.maxRadius, CircleGraph.maxRadius);
			radii[i].setPreferredSize(new Dimension(20, (int)(height - 10)));
			
			int index = i;
			radii[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					label.setText("<html>Circle " + (index+1) + "<br/>Radius: " + round((double)radii[index].getValue()/unit) + "</html>");
				}
			});
			
			
			panels[i].add(label);
			panels[i].add(radii[i]);
			
			add(panels[i]);
		}
		panels[0].setVisible(true);
	}
	
	private String round(double num) {
		DecimalFormat df = new DecimalFormat("0.0000");
		return df.format(num);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add) {
			remove.setEnabled(true);
			
			panels[CircleGraph.circles].setVisible(true);
			CircleGraph.addCircle();
			
			if(CircleGraph.circles == 10) add.setEnabled(false);
			revalidate();
		} else if(e.getSource() == remove) {
			add.setEnabled(true);
			
			CircleGraph.removeCircle();
			panels[CircleGraph.circles].setVisible(false);
			
			if(CircleGraph.circles == 1) remove.setEnabled(false);
			revalidate();
		}
	}
	
}
