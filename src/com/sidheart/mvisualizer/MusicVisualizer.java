package com.sidheart.mvisualizer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ddf.minim.*;
import ddf.minim.analysis.FFT;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class MusicVisualizer extends JFrame {

	Minim minim;
	FFT fft;
	AudioPlayer player;
	int x = 200;
	int y = 62;
	private static final long serialVersionUID = -5468431429808172197L;

	private JPanel contentPane;

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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		DrawPanel panel = new DrawPanel();
		panel.setBackground(SystemColor.text);
		//panel.add(new MyGraphics(fft, song));
		contentPane.add(panel, BorderLayout.CENTER);
		
		// setup Minim resources
		MinimHandler mh = new MinimHandler();
		minim = new Minim(mh);
		String fp = "/home/milk/Music/Nothing_Was_the_Same/Furthest Thing.mp3";
		player = minim.loadFile(fp);
		fft = new FFT(player.bufferSize(), player.sampleRate());
		
		// define the play/pause button behavior on click
		Button playButton = new Button("Play");
		playButton.setBackground(UIManager.getColor("Button.background"));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(player.isPlaying()) {
					player.pause();
					playButton.setLabel("Play");
				} else {
					player.play();
					panel.repaint();
					playButton.setLabel("Pause");
					/* TODO add in canvas FFT graph */
				}
			}
		});
		
		// release audio resources on window close
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		        player.close();
		    }
		});
		
		
		
		// add the play/pause button
		contentPane.add(playButton, BorderLayout.SOUTH);
		
		
		contentPane.setBackground(Color.WHITE);
		//contentPane.add(panel, BorderLayout.CENTER);

	}
	
	class DrawPanel extends JPanel {
		
		public Dimension getPreferredSize() {
            return new Dimension(500, 500);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            fft.forward(player.mix);
            //g.fillRect(0, 200, 30, 10);
            for(int i = 0; i < player.bufferSize(); i++) {
                int b = (int) fft.getBand(i);
                System.out.println(b);
                g.fillRect(i*2, 200 - b,  30, 10);
            }
        }
	}
}
