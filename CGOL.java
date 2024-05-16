import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;


import javax.swing.*;

public class CGOL {

	static Board board;

	public static void main(String[] args) throws InterruptedException {

		board = new Board(896,900);

		while(true) {

			if(board.getGenNum()>0) {

				board.nextGen();

			}

			Thread.sleep(200);

		}

	}

}
class Board {

	private int x;

	private int y;

	private JButton[][] buttonArr;

	private ImageIcon black = new ImageIcon("src/Black_30x30.png");

	private ImageIcon yellow = new ImageIcon("src/Cadmiumyellow.jpg");

	private JFrame boardFrame;

	static int genNum = 0;

	public Board(int x, int y) {

		buttonArr = new JButton[x/20][y/20];

		int buttonsize=20;

		boardFrame = new JFrame();

		this.x = x;

		this.y = y;

		boardFrame.setSize(new Dimension(x,y));

		boardFrame.setTitle("CGOL");

		boardFrame.setFocusable(true);

		keyboardListener frameListener = new keyboardListener(this);

		boardFrame.addKeyListener(frameListener);

		for(int i = 0; i<=x-buttonsize;i=i+buttonsize) {

			for (int j = 0; j<=y-buttonsize; j=j+buttonsize) { //fix this		

				JButton button = new JButton(black);

				button.setBounds(new Rectangle(i,j,buttonsize,buttonsize));//x y width height

				Clicklistener click = new Clicklistener(buttonArr, black, yellow);

				button.addActionListener(click);

				button.addKeyListener(frameListener);

				boardFrame.add(button); 

				buttonArr[i/buttonsize][j/buttonsize]=button;

			}

		}	

		

		boardFrame.setResizable(false);

		boardFrame.setLocationRelativeTo(null);

		boardFrame.setVisible(true);

	}

	public void nextGen(){

		boardFrame.setTitle("CGOL - Generation " + genNum);

		genNum++;

		JButton[][] oldArr = new JButton[buttonArr.length][buttonArr[0].length];

		for (int i = 0; i<oldArr.length;i++) {

			for (int j = 0; j<oldArr[i].length;j++) {

				oldArr[i][j] = new JButton(buttonArr[i][j].getIcon());

			}

		}

		

		for (int i = 0; i<oldArr.length;i++) {

			for (int j = 0; j<oldArr[i].length;j++) {

//				Any live cell with fewer than two live neighbours dies, as if by underpopulation.

//				Any live cell with two or three live neighbours lives on to the next generation.

//				Any live cell with more than three live neighbours dies, as if by overpopulation.

//				Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

				int liveNeighbors = 0;

				if (i>0 && j>0 && oldArr[i-1][j-1].getIcon()==yellow) {liveNeighbors++;}

				if(j>0 && oldArr[i][j-1].getIcon()==yellow) {liveNeighbors++;}

				if(j>0 && i<oldArr.length-1 && oldArr[i+1][j-1].getIcon()==yellow) {liveNeighbors++;}

				if(i<oldArr.length-1 && oldArr[i+1][j].getIcon()==yellow) {liveNeighbors++;}

				if(i<oldArr.length-1 && j<oldArr[i].length-3 && oldArr[i+1][j+1].getIcon()==yellow) {liveNeighbors++;}

				if(j<oldArr[i].length-3 && oldArr[i][j+1].getIcon()==yellow) {liveNeighbors++;}

				if(i>0 && j<oldArr[i].length-3 && oldArr[i-1][j+1].getIcon()==yellow) {liveNeighbors++;}

				if(i>0 && oldArr[i-1][j].getIcon()==yellow) {liveNeighbors++;}

				

				if (liveNeighbors<2 && oldArr[i][j].getIcon()==yellow){

					buttonArr[i][j].setIcon(black);

				}

				else if (liveNeighbors>3&& oldArr[i][j].getIcon()==yellow){

					buttonArr[i][j].setIcon(black);

					

				}

				else if (liveNeighbors==3 && oldArr[i][j].getIcon()==black) {

					buttonArr[i][j].setIcon(yellow);

				}

			}

		}

	}

	public int getGenNum() {

		return genNum;

	}

	public String toString() {

		String output="";

		for(JButton[] jArr : buttonArr) {

			output += "[";

			for(JButton j : jArr) {

				output += "["+j.toString()+"]";

			}

			output = output.substring(0,output.length()-2) + "],\n";

		}

		output=output.substring(0,output.length()-1);

		return output;

	}

}

class Clicklistener implements ActionListener{

	JButton[][] buttonArr;

	ImageIcon black;

	ImageIcon yellow;

	public Clicklistener(JButton[][] buttonArr, ImageIcon black, ImageIcon yellow) {

		this.buttonArr=buttonArr;

		this.black=black;

		this.yellow=yellow;

	}

	public void actionPerformed(ActionEvent e) {

		for (JButton[] jArr : buttonArr) {

			for(JButton j: jArr) {

				if (e.getSource() == j) {

					if(j.getIcon()==black) {

						j.setIcon(yellow);

					}

					else if(j.getIcon()==yellow) {

						j.setIcon(black);

					}

					else {

						System.out.println("NOT BLACK OR YELLLOW!!?!:");

					}

				}

			}

		}

	}

}

class keyboardListener extends KeyAdapter {

	private Board board;

	public keyboardListener(Board b) {

		board=b;

	}

	public void keyPressed(KeyEvent event) {

		int keyCode=event.getKeyCode();

		if (keyCode==10) {

			board.nextGen();

		}

	}
}