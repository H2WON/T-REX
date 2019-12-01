import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RunThread extends JFrame implements KeyListener{

	public static RunThread main;
	public static ImagePaper paper;
	
	Thread thread = new Thread(paper);
	
	public static JLabel timerlabel = new JLabel();
	
	public RunThread() {

		addKeyListener(this);
		
		timerlabel.setFont(new Font("Joystix Monospace", Font.BOLD, 15));
		timerlabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.setBackground(Color.white);
		
		c.add(paper);
		c.add(timerlabel, "North");
		
		setTitle("T-REX Game");
		//setLocationRelativeTo(null);	// 화면 정중앙에 프레임 띄우기 위한 코드, 하영 노트북 해상도 실제와 다르게 돼 있어서 주석처리 함
		setSize(800, 450);
		setBounds(200, 100, 800, 450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		thread = new Thread(paper);
		thread.start();
	}

	public void keyPressed(KeyEvent arg0) {
		int kc = arg0.getKeyCode();
		
		if(kc == KeyEvent.VK_UP && !paper.isUp)
		{
			paper.stJump();
		}
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

	public static void main(String[] args) {
		//paper = new ImagePaper(timerlabel);
		paper = new ImagePaper(timerlabel , Color.white, main);
		main = new RunThread();
	}

}
