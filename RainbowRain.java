package rainbowrain;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.Random;

class Parameters{
	public final static Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int rainNumber = 50;
	public static boolean color = true;
	public static boolean random = false;
	public static String customStr = "Click \'yes\' after inputing a new string";
	
	public int getRainNumber() {
		return rainNumber;
	}
}

class Rain extends Thread{
	
	private RainCanvas canvas = null;
	private String content = null;
	private int x, y, speed, size, length;
	private final Color rainbowColor[] = new Color[]{
		new Color(255, 0, 0),
        new Color(255, 165, 0),
        new Color(255, 255, 0),
        new Color(0, 255, 0),
        new Color(0, 127, 0),
        new Color(0, 127, 255),
        new Color(139, 0, 255),
	};
	
	public Rain(RainCanvas _canvas) {
		canvas = _canvas;

		x = 10 + Math.abs(new Random().nextInt())%Parameters.sSize.width;
		
		speed = 5 + Math.abs(new Random().nextInt())%30;
		size = 15 + Math.abs(new Random().nextInt())%30;
		length = 7 + Math.abs(new Random().nextInt())%20;
		y = 10 + Math.abs(new Random().nextInt())%Parameters.sSize.height - length*size;
		
		if (Parameters.random) {
			content = randomString(length);
		}else {
			content = Parameters.customStr;
		}
	}
	
	@Override
	public void run() {
		while(true) {
			y += speed;
			try{
				sleep(30);
			}catch(InterruptedException e) {
				System.err.println("Thread interrupted");
			}
			canvas.repaint();
			
			if (y >Parameters.sSize.height) {
				x = 10 + Math.abs(new Random().nextInt())%Parameters.sSize.width;
				
				speed = 5 + Math.abs(new Random().nextInt())%30;
				size = 15 + Math.abs(new Random().nextInt())%30;
				length = 7 + Math.abs(new Random().nextInt())%20;
				y = 0 - length*size;
				
				if (Parameters.random) {
					content = randomString(length);
				}else {
					content = Parameters.customStr;
				}
			}
		}
	}
	public void draw(Graphics g) {
		Font f1 = new Font("Courier", Font.PLAIN, size);
		g.setFont(f1);
		
		String[] strSplit= content.split("");
		if (Parameters.color) {
			for (int i = 0; i < strSplit.length; i++) {
				g.setColor(rainbowColor[i%7]);
				g.drawString(strSplit[i], x, y+i*size);
			}
		}else {
			for (int i = 0; i < strSplit.length; i++) {
				g.setColor(rainbowColor[3]);
				g.drawString(strSplit[i], x, y+i*size);
			}
		}
		//g.setColor(Color.GREEN);
		//g.drawString(content, x, y);
	}
	
	public String randomString(int length) {
		StringBuffer give = new StringBuffer("");
		for (int i = 0; i < length; i++) {
			final String charSource = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			give.append(charSource.charAt((int)(Math.random()*62)));
		}
		return give.toString();
	}
	
}

class MainFrame extends Frame{
	
	public MainFrame() {
		setTitle("Rainbow Rain");
		setSize(Parameters.sSize.width/2, Parameters.sSize.height/2);
		setLocation(Parameters.sSize.width/4, Parameters.sSize.height/4);
		
		Button start = new Button("Start Raining!");
		
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RainFrame rf = new RainFrame();
			}
		});
		
		Panel buttonPanel = new Panel();
		buttonPanel.add(start);
		add(buttonPanel, BorderLayout.SOUTH);
		
		Panel settingPanel = new Panel();
		settingPanel.setLayout(new GridLayout(4, 1));

		Panel subPanelA = new Panel();
		Label rainNumberLabel = new Label("How heavy you want it: ");
		TextField rainNumberField = new TextField(20);
		Button setRainNumber = new Button("yes");
		subPanelA.add(rainNumberLabel);
		subPanelA.add(rainNumberField);
		subPanelA.add(setRainNumber);
		rainNumberField.setText(String.valueOf(Parameters.rainNumber));
		setRainNumber.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Parameters.rainNumber  = Integer.parseInt(rainNumberField.getText());
				}catch(Exception exc) {
					rainNumberField.setText("Input a legal number!");
				}
			}
		});
		
		Panel subPanelB = new Panel();
		Label colorModeLabel = new Label("Color Mode: ");
		JRadioButton colorFalse = new JRadioButton("MATRIX");
		JRadioButton colorTrue = new JRadioButton("Rainbow Rain");
		ButtonGroup bg = new ButtonGroup();
		bg.add(colorFalse);
		bg.add(colorTrue);
		colorTrue.setSelected(true);
		colorFalse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Parameters.color = false;
			}
		});
		colorTrue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Parameters.color = true;
			}
		});
		subPanelB.add(colorModeLabel);
		subPanelB.add(colorFalse);
		subPanelB.add(colorTrue);
		
		Panel subPanelC = new Panel();
		Label customLabel = new Label("Characters: ");
		JRadioButton customFalse = new JRadioButton("Random");
		JRadioButton customTrue = new JRadioButton("Use this: ");
		TextField customContent = new TextField(30);
		Button setCustomStr = new Button("yes");
		customContent.setEnabled(true);
		customContent.setText(Parameters.customStr);
		ButtonGroup bgC = new ButtonGroup();
		bgC.add(customFalse);
		bgC.add(customTrue);
		customTrue.setSelected(true);
		customTrue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Parameters.random = false;
				customContent.setEnabled(true);
				customContent.setText(Parameters.customStr);
			}
		});
		customFalse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Parameters.random = true;
				customContent.setEnabled(false);
			}
		});
		setCustomStr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Parameters.customStr = customContent.getText();
			}
		});
		subPanelC.add(customLabel);
		subPanelC.add(customFalse);
		subPanelC.add(customTrue);
		subPanelC.add(customContent);
		subPanelC.add(setCustomStr);
		
		settingPanel.add(subPanelA);
		settingPanel.add(subPanelB);
		settingPanel.add(subPanelC);
		
		add(settingPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing (WindowEvent e){
				System.exit(0);
			}
		});
	}
	
}

class RainFrame extends Frame{
	
	public RainFrame() {
		 setSize(Parameters.sSize);
		 setUndecorated(true);	// Must be used before setVisible
		 setVisible(true);
		 
		 addMouseListener(new MouseListener() {
	        @Override 
	        public void mouseClicked(MouseEvent e) { 
	        	dispose();
	        }
	        @Override
	        public void mousePressed(MouseEvent e){}
	        @Override 
	        public void mouseReleased(MouseEvent e){}
	        @Override
	        public void mouseEntered(MouseEvent e){}
	        @Override
	        public void mouseExited(MouseEvent e){}
		 });
		 
		 addKeyListener(new KeyListener() {
	        @Override 
	       	public void keyPressed(KeyEvent e) { 
	       		dispose();
	       	}
	       	@Override 
	       	public void keyReleased(KeyEvent e){} 
	       	@Override 
	       	public void keyTyped(KeyEvent e){}
		 });
		 
		 addWindowListener(new WindowAdapter(){
			 @Override
			 public void windowClosing (WindowEvent e){
				 dispose();
			 }
		 });
	        
		 RainCanvas rc = new RainCanvas(this);
		 add(rc);
	}
	
}

class RainCanvas extends Canvas{
	
	private RainFrame frame = null;
	private Rain[] rains = new Rain[Parameters.rainNumber];
	
	private Image iBuffer;  
	private Graphics gBuffer;  
	
	public RainCanvas(RainFrame _frame) {
		frame = _frame;
		
		setBackground(Color.BLACK);
		
		addMouseListener(new MouseListener() {
	        @Override 
	        public void mouseClicked(MouseEvent e) { 
	        	frame.dispose();
	        }
	        @Override
	        public void mousePressed(MouseEvent e){}
	        @Override 
	        public void mouseReleased(MouseEvent e){}
	        @Override
	        public void mouseEntered(MouseEvent e){}
	        @Override
	        public void mouseExited(MouseEvent e){}
		 });
		
		for (int i = 0; i < rains.length; i++) {
			rains[i] = new Rain(this);
			rains[i].start();
		}
	}

	// Double buffer
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < rains.length; i++) {
			rains[i].draw(g);
		}
	}
	
	@Override
	public void update(Graphics g) {
		if (iBuffer == null) {
			iBuffer=createImage(this.getSize().width,this.getSize().height);  
			gBuffer=iBuffer.getGraphics();  
		}
		
		gBuffer.setColor(getBackground());  
		gBuffer.fillRect(0,0,this.getSize().width,this.getSize().height);  
	    paint(gBuffer); 
	    g.drawImage(iBuffer,0,0,this);
	}
}

public class RainbowRain {
	
	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
	}
	
}