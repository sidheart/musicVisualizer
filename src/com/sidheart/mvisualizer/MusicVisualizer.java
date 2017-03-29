package com.sidheart.mvisualizer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import ddf.minim.*;
import ddf.minim.analysis.*;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class MusicVisualizer extends JFrame {


	private static final long serialVersionUID = -5468431429808172197L;
	private JPanel contentPane;
	private Minim minim;
	private AudioPlayer player;
	Timer timer;
	boolean pause;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicVisualizer frame = new MusicVisualizer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public MusicVisualizer() {
		// define JFrame layout
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 200, 250);


		
		// setup Minim resources
		MinimHandler mh = new MinimHandler();
		minim = new Minim(mh);
		String fp = "C:\\Users\\sidheart\\Music\\In The Aeroplane Over The Sea\\04 Two-Headed Boy.mp3";
		player = minim.loadFile(fp, 1024);
		//System.out.println("Buffer size is: " + player.bufferSize());
		
		// setup visualization panel
		DrawPanel panel = new DrawPanel();
		panel.setBackground(Color.BLACK);
		
		// setup timer to allow for animated audio graph
		timer = new Timer(100, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        panel.repaint();
		    }
		});
		
		// define the play/pause button behavior on click
		Button playButton = new Button("Play");
		playButton.setBackground(UIManager.getColor("Button.background"));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(player.isPlaying()) {
					player.pause();
					timer.stop();
					playButton.setLabel("Play");
				} else {
					player.play();
					timer.start();
					playButton.setLabel("Pause");
					/* TODO add in canvas FFT graph */
				}
			}
		});

		
		// Create and add contents to the content panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(playButton, BorderLayout.SOUTH);
		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.setBackground(Color.BLACK);
		
		/**
		 * Release audio resources on window close.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		        player.close();
		    }
		});
	}
	
	/**
	 * The visualization panel class. Defines a paintComponent method to specify the drawing
	 * of the FFT analysis.
	 */
	class DrawPanel extends JPanel {
		
		private static final long serialVersionUID = 1412100151873396811L;

		public Dimension getPreferredSize() {
            return new Dimension(200, 250);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g.setColor(Color.WHITE);
            for(int i = 0; i < player.bufferSize() - 1; i++)
            {
              float left1 = 50 + (player.left.get(i) * 50);
              float left2 = 50 + (player.left.get(i+1) * 50);
              float right1 =  60 + (player.right.get(i) * 50);
              float right2 =  60 + (player.right.get(i+1) * 50);
              Shape line1 = new Line2D.Double(i, left1, i+1, left2);
              Shape line2 = new Line2D.Double(i, right1, i+1, right2);
              g2.draw(line1);
              g2.draw(line2);
            }
        }
	}
}
