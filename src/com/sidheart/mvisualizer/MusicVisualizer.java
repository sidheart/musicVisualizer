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
	
    public class MyGraphics extends JComponent {

		private static final long serialVersionUID = 9197032590874822717L;

		MyGraphics() {
            setPreferredSize(new Dimension(500, 100));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillRect(200, 62, 30, 10);
        }
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
		
		// setup Minim resources
		MinimHandler mh = new MinimHandler();
		Minim minim = new Minim(mh);
		String fp = "/home/milk/Music/Nothing_Was_the_Same/Furthest Thing.mp3";
		AudioPlayer song = minim.loadFile(fp);
		FFT fft = new FFT(song.bufferSize(), song.sampleRate());
		
		// define the play/pause button behavior on click
		Button playButton = new Button("Play");
		playButton.setBackground(UIManager.getColor("Button.background"));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(song.isPlaying()) {
					song.pause();
					playButton.setLabel("Play");
				} else {
					song.play();
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
		        song.close();
		    }
		});
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.text);
		panel.add(new MyGraphics());
		contentPane.add(panel, BorderLayout.CENTER);
		
		// add the play/pause button
		contentPane.add(playButton, BorderLayout.SOUTH);
		
		
		contentPane.setBackground(Color.WHITE);
		//contentPane.add(panel, BorderLayout.CENTER);
	}
}
