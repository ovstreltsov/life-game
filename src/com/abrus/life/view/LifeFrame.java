package com.abrus.life.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.abrus.life.LifeCell;
import com.abrus.life.LifeGameRuller;

public class LifeFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	LifeViewBoard panel = new LifeViewBoard();
	
	private static final Dimension WINDOW_SIZE = new Dimension(800, 400);
	
	JButton startButton;
	JButton stopButton;
	JButton clearButton;
	
	JTextField periodText;
	
	Timer timer;
	
	LifeCell[][] cells = null;
	
	LifeFrame(){
		super();
		
		setTitle("Conwey's Game of Life");
		
		setSize(WINDOW_SIZE.width + 7, WINDOW_SIZE.height + 33 + 22);
		setResizable(false);
		
		this.createMenu();
		this.createButtons();
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(null);
		
		JPanel toolPanel = new JPanel();
		
		toolPanel.add(startButton);
		toolPanel.add(stopButton);
		toolPanel.add(clearButton);
		
		toolPanel.add(new JLabel("Tick period (ms): "));
		periodText = new JTextField("500", 5);
		toolPanel.add(periodText);
		
		
		mainPanel.add(toolPanel);
		panel.setCellSize(10);
		mainPanel.add(panel);
		
		toolPanel.setBounds(600,  0, 200, 600);
		panel.setBounds(0, 0, 600, WINDOW_SIZE.height);
		
		getContentPane().add(mainPanel);
		
		
	}
	
	private void createButtons() {
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		clearButton = new JButton("Clear");
		
		stopButton.setEnabled(false);
		clearButton.setEnabled(false);
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startButton.setEnabled(false);
				periodText.setEnabled(false);
				clearButton.setEnabled(false);
				
				stopButton.setEnabled(true);
				
				
				cells = createTestBoard(panel.getWidth()/panel.getCellSize(), panel.getHeight()/panel.getCellSize());
				
				List<Point> startPoints = panel.getMarkedCells(); 
				
				for(Point p: startPoints){
					int i = p.x/panel.getCellSize();
					int j = p.y/panel.getCellSize();
					
					cells[i][j].alive();
				}
				
				startPoints.clear();
				
				panel.updateMarkedCells(cells);
				
				int tickPeriod = 500;
				try{
					tickPeriod = Integer.parseInt(periodText.getText());
				} catch(NumberFormatException e) {
					// TODO: show error dialog message
				}
				
				timer = new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						cells = LifeGameRuller.tick(cells);
						panel.updateMarkedCells(cells);
						repaint();
					}}, 1000, tickPeriod);
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				clearButton.setEnabled(true);
				
				periodText.setEnabled(true);
				
				timer.cancel();
				
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cells == null) return;
				for (int i = 0; i < cells.length; ++i) {
					for (int j = 0; j < cells[i].length; ++j) {
						cells[i][j].die();
					}
				}
				panel.updateMarkedCells(cells);
				repaint();
			}
		});
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File"); 
		fileMenu.add(new JMenuItem("Exit"));
		
		menuBar.add(fileMenu);
		
		this.setJMenuBar(menuBar);
	}

	private LifeCell[][] createTestBoard(int rows, int cols) {
		LifeCell[][] cells = new LifeCell[rows][cols];

		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				cells[i][j] = new LifeCell();
			}
		}
		return cells;
	}
	
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(
				new Runnable(){
					@Override
					public void run() {
						createGui();
					}
				});
	}
	
	private static void createGui(){
		LifeFrame frame = new LifeFrame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class LifeViewBoard extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension PANEL_SIZE = new Dimension(600, 400);
	
	private static int CELL_SIZE = 10;

	private List<Point> mMarkedPoints = new ArrayList<Point>(); 
	
	public LifeViewBoard() {
		super();
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		
		this.addMouseListener(this);
	}

	public LifeViewBoard(int width, int heigth) {
		this();
		
		PANEL_SIZE.setSize(width, heigth);
	}
	
	public List<Point> getMarkedCells() {
		return mMarkedPoints;
	}

	public void updateMarkedCells(LifeCell[][] cells) {
		for(int i = 0; i < cells.length; ++i ){
			for(int j = 0; j < cells[i].length; ++j){
				Point p = new Point(i*CELL_SIZE, j*CELL_SIZE);
				if(cells[i][j].isAlive()){
					if(!mMarkedPoints.contains(p)) mMarkedPoints.add(p);
				} else {
					if(mMarkedPoints.contains(p)) mMarkedPoints.remove(p);
				}
			}
		}
		
	}

	@Override
	protected void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		
		arg0.setColor(Color.GRAY);
		
		final int xEndPoint = PANEL_SIZE.width - PANEL_SIZE.width%CELL_SIZE;
		final int yEndPoint = PANEL_SIZE.height - PANEL_SIZE.height%CELL_SIZE;
		
		for(int i = CELL_SIZE; i < PANEL_SIZE.height; i += CELL_SIZE){
			arg0.drawLine(0, i, xEndPoint, i);
		}
		
		for(int i = CELL_SIZE; i < PANEL_SIZE.width; i += CELL_SIZE){
			arg0.drawLine(i, 0, i, yEndPoint);
		}
		
		arg0.setColor(Color.BLACK);
		for(Point p: mMarkedPoints){
			int x = p.x - p.x%CELL_SIZE;
			int y = p.y - p.y%CELL_SIZE;
			
			arg0.fillRect(x, y, CELL_SIZE, CELL_SIZE);
		}
	}

	public void setCellSize(int cellSize){
		CELL_SIZE = cellSize;
	}
	
	public int getCellSize(){
		return CELL_SIZE;
	}
	
	public int getWidth() {
		return (int)PANEL_SIZE.getWidth();
	}
	
	public int getHeight(){
		return (int)PANEL_SIZE.getHeight();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//System.out.println(arg0.getPoint());
		
		final int xEndPoint = PANEL_SIZE.width - PANEL_SIZE.width%CELL_SIZE;
		final int yEndPoint = PANEL_SIZE.height - PANEL_SIZE.height%CELL_SIZE;
		
		final int x = (int)arg0.getPoint().getX();
		final int y = (int)arg0.getPoint().getY();
		
		if(x >= xEndPoint || y >= yEndPoint) return;
		
		mMarkedPoints.add(arg0.getPoint());
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }
}