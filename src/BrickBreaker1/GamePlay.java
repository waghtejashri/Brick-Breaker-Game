package BrickBreaker1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.DelayQueue;

import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePlay extends JPanel implements KeyListener, ActionListener{
	
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	private Timer timer;
	
	private int delay = 8;
	private int playerx = 310;
	
	private int ballposx = 120;
	private int ballposy = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGeberator map;
	
	public GamePlay() {
		map = new MapGeberator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0,  0, 3, 592);
		g.fillRect(0,  0,  692,  3);
		g.fillRect(691, 0, 3, 592);
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 590, 30);
		
		//the paddle
		g.setColor(Color.green);
		g.fillRect(playerx, 550, 100, 8);
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposx, ballposy, 20, 20);
		
		if(totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won: ", 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Press Enter t Restart", 230, 350);
		}
		
		if(ballposy > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over,Scores: ", 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Press Enter t Restart", 230, 350);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {
			if(new Rectangle(ballposx, ballposy, 20, 20).intersects(new Rectangle(playerx, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}
			
			A: for(int i=0; i<map.map.length; i++) {
				for(int j=0; j<map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j* map.brickWidth + 80;
						int brickY = i* map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposx, ballposy, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposx + 19 <= brickRect.x || ballposx + 1>= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							}else {
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
				}
			}
			
			ballposx += ballXdir; 
			ballposy += ballYdir;
			if(ballposx < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposy < 0) {
				ballYdir = -ballYdir;
			}
			if(ballposx > 670) {
				ballXdir = -ballXdir;
			}
		}
		
		repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerx >= 600) {
				playerx = 600;
			}else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerx < 10) {
				playerx = 10;
			}else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballposx = 120;
				ballposy = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerx = 310;
				score =0;
				totalBricks = 21;
				map = new MapGeberator(3, 7);
				
				repaint();
				
			}
		}
	}
	
	public void moveRight() {
		play = true;
		playerx+=20;
	}
	
	public void moveLeft() {
		play = true;
		playerx-=20;
	}

	
}



