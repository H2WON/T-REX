import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class ImagePaper extends JPanel implements Runnable{
	
//	public static final int TREX_MIN_Y = 130;

	public static final int TREX_W = 90;
	public static final int TREX_H = 90;
	public static final int TREX_Y = 270;
	public static final int JUMP_H = 150;
	
	public static final int GROUND_Y = TREX_Y + TREX_H;

	public static final int DEFAULT_ALPHA = 25;
	public static final int ALPHA_DIF = 2;

	public static final int IMG_GROUND_N = 10;
	public static final int IMG_CACTUS_N = 6;

	public static final int FRAME_W = 800;
	public static final int FRAME_H = 450;
	
	public static final int CACTUS_W[] = {50, 50, 50, 50, 50, 50};
	public static final int CACTUS_H[] = {50, 50, 50, 50, 50, 50};
	
	public static final int CLOUD_W = 98;
	public static final int CLOUD_H = 31;

	public static boolean GameOver = false;

	public int trex_x = 30;
	public int trex_y = TREX_Y;	
	
	public boolean jumpDir = false;
	
	public boolean isUp = false;
	
	public int cactusX = -100;
	public int cactusType = 0;
	
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image cloud = new ImageIcon("imgs/cloud.png").getImage();
	Random random = new Random();
	public int cloudX[] = {1000, 1400, 1900};
	public int cloudY[] = {130, 60, 160};
	
	public int speed = 13;

	BufferedImage ImgBG;
	public Image grounds[], cactus[];
	
	
	
	Image smalls[];
	
	int imgStIndex, imgEdIndex;
	int nowImgX = -200;
	
	
	Image buffImage;
	Graphics buffg;
	int[] cx ={0, 0, 0}; 
	int bx = 0; 
	
	Image trexImg;

	
	String trexImgName = "trex";
	int trexFlag = 0;
	int alpha = DEFAULT_ALPHA;
	
	
	//선인장
	String cactusName[] = {"small1", "small2", "small3", "big1", "big2", "big3"};
	Image cactusImg[] = new Image[6];
	Image cactusShow;
	int cactusRandom = 0;
	int cactusCheck = 0;
	
	
	//현재 점수를 위한 변수
	Double dScore;
	String str;
	
	
	//최고점수와 현재점수를 보여주기 위한 label
	JLabel lblScore = new JLabel("SCORE : " + dScore);
	
	
	//Max Score를 위한 변수
	Double sMaxScore;
	
	
	//DB연결을 위한 변수
	PreparedStatement pstmt;
	Connection con;
	
	JFrame f;
	
	JLabel timerlabel;

	
	public ImagePaper(JLabel timerlabel, Color white, JFrame f) {
		this.timerlabel = timerlabel;
		
		Play("sound/bgm.wav");
		
		this.f = f;
		imgStIndex = 0;
		imgEdIndex = 4;
		
		grounds = new Image[IMG_GROUND_N];
		for(int i=0; i<IMG_GROUND_N; i++) {
			grounds[i] =  Toolkit.getDefaultToolkit().getImage("imgs/gruond_" + i + ".png");
		}
		
		cactus = new Image[IMG_CACTUS_N];
		for(int i=0; i<IMG_CACTUS_N; i++) {
			cactus[i] =  Toolkit.getDefaultToolkit().getImage("imgs/cactus_" + i + ".png");
		}
		
		
		smalls = new Image[2];
		for(int i=0;i<2;i++)
		{
			smalls[i] = Toolkit.getDefaultToolkit().getImage("imgs/small_"+i+".png");
		}
		
		makeBG();
		makeBG();
		makeBG();
		makeBG();
		makeBG();
		makeBG();
		
		repaint();
	}

	public void Play(String fileName) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
			Clip clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
		} catch (Exception ex) {}
	}
	
	public void makeBG() {

		ImgBG = new BufferedImage((int)(FRAME_W * 1.25), FRAME_H, BufferedImage.TYPE_INT_RGB);
		Graphics2D Bg2 = (Graphics2D)ImgBG.getGraphics();
		
		Bg2.setColor(new Color(255, 255, 255));
		Bg2.fillRect(0, 0, (int)(FRAME_W * 1.25), FRAME_H);
		
		for(int i=0, j=imgStIndex; i<5; i++) {
			Bg2.drawImage(grounds[j], (int)(i * FRAME_W * 0.25), TREX_Y + 88, this);
			j = (j + 1) % 10;
		}
		
		imgStIndex = (imgStIndex + 1) % 10;
		
		if (cactusX + CACTUS_W[cactusType] < 0) {
			System.out.println(cactusX);
			
			//Random rand = new Random();
			cactusType = random.nextInt(CACTUS_W.length);
			
			//int tmpBlockW = (int)(FRAME_W * 1);
			//Bg2.drawImage(cactus[cactusType], cactusX, GROUND_Y - CACTUS_H[cactusType], this);
			
			cactusX = FRAME_W + (FRAME_W - CACTUS_W[cactusType]) / 2;
		}
		
		else {
			/*
			Bg2.setColor(Color.BLACK);
			Bg2.fillRect(cactusX, GROUND_Y - CACTUS_H[cactusType], CACTUS_W[cactusType], CACTUS_H[cactusType]);
			*/
			
			Bg2.drawImage(cactus[cactusType], cactusX, GROUND_Y - CACTUS_H[cactusType], this);
		}
		
		for(int i=0; i<cloudX.length; i++)
		{
			if (cloudX[i] + CLOUD_W < 200) {
				
				if(i==0)
				{
					cloudX[i] = 1200;
				}
				if(i==1)
				{
					cloudX[i] = 1400;
				}
				if(i==2)
				{
					cloudX[i] = 1900;
				}
			}
			
			else {
				for(i=0; i<cloudY.length; i++)
				{
					Bg2.drawImage(cloud, cloudX[i], cloudY[i], CLOUD_W, CLOUD_H, this);
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {	
		super.paint(g);

		if (nowImgX < -200) {
			nowImgX = 0;
			makeBG();
		}

		BufferedImage bg = new BufferedImage(FRAME_W, FRAME_H, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D)bg.getGraphics();
		
		g2.drawImage(ImgBG, nowImgX, 0, this);
		nowImgX -= speed;
		cactusX -= speed;
		for(int i=0; i<cloudX.length; i++)
		{
			cloudX[i] -= speed;
		}
		
		
		/*
		g2.setColor(Color.RED);
		g2.fillRect(trex_x, trex_y, TREX_W, TREX_H);
		*/
		
		trexImg = Toolkit.getDefaultToolkit().getImage("imgs/" + trexImgName + ".png");
		g2.drawImage(trexImg, trex_x, trex_y, this);
		
		
		g.drawImage(bg, 0, 0, this);

	}
	
	public void stJump() {	//초기값
		isUp = true;
		alpha = DEFAULT_ALPHA;
		trex_y = TREX_Y;
		jumpDir = false;
		speed = 13;
	}
	
	public void jumpTrex() {
		if (!jumpDir) {
			trex_y -= alpha;
			alpha -= ALPHA_DIF;
			
			if (alpha <= 0) {
				alpha = 1;
			}
			
			if (trex_y < TREX_Y - JUMP_H) {
				jumpDir = true;
				trex_y = TREX_Y - JUMP_H;
				alpha = 0;
			}
		}

		if (jumpDir) {
			trex_y += alpha;
			alpha += ALPHA_DIF;
			
			if (alpha <= 0) {
				alpha = 1;
			}
			
			if (trex_y > TREX_Y) {
				trex_y = TREX_Y;
				isUp = false;
			}
		}
	}
	
	public boolean isHit() {
		
		int x1 = cactusX;
		int y1 = GROUND_Y - CACTUS_H[cactusType];

		int x2 = cactusX + CACTUS_W[cactusType];
		int y2 = GROUND_Y;

		int tx = trex_x;
		int ty = trex_y + TREX_H;
		
		if (x1 <= tx && tx <= x2 && y1 <= ty && ty <= y2) {
			return true;
		}
		
		tx = trex_x + TREX_W;
		ty = trex_y + TREX_H;
		
		if (x1 <= tx && tx <= x2 && y1 <= ty && ty <= y2) {
			return true;
		}

		return false;
	}
	
	public void run() {

		execSQL();
		// 아래 while문이 아닌 run() 시작에 둬야 sMaxScore가 게임 중에 필요없는 repaint 되는 걸 막을 수 있음
		
		GameOver = true;

		long stTime = System.currentTimeMillis();
		long time = 0;

		//System.out.println(GameOver);
		
		try {
			
		while(GameOver) {
			
			long tempTime = System.currentTimeMillis();
			
			time = tempTime - stTime;
			
			str = (time / 1000) + "." + ((time % 1000) / 100);
			
			dScore = Double.parseDouble(str);
			
			timerlabel.setText("Hi : " + sMaxScore + "   score : " + str + "  ");
			
			if(dScore != 0 && dScore % 10 == 0)
			{
				speed += 1;
			}
			
			if(dScore != 0 && (dScore % 10 == 0 || dScore % 10.2 == 0 || dScore % 10.4 == 0
					|| dScore % 10.5 == 0))
			{
				timerlabel.setText("SPEED UP!!!   Hi : " + sMaxScore + "   score : " + str + "  ");
			}

			//RunThread.main.setTitle(str);
			lblScore.setText("SCORE : " + dScore);
			System.out.println("score : " + dScore);
			lblScore.setLocation(300,300);
			add(lblScore);
			
			if (isUp) {
				jumpTrex();
			}
			
			if(isUp == false)
			{
				if(trexFlag==0)
				{
					trexImgName="trex";
					trexFlag=1;
				}
				else if(trexFlag==1)
				{
						trexImgName="trex_R";
					trexFlag=2;
				}
				else if(trexFlag==2)
				{
					trexImgName="trex_L";
					trexFlag=0;
				}
			}
			repaint();
			
			if (isHit()) {
				GameOver = false;
				trexImgName = "trex_die";
				insertSQL();
				new Dialog(f, ImagePaper.this);
			}
			Thread.sleep(25);
		}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println((time / 100) + "��");
	}
	
	//score 칼럼은 double임
	
	public void insertSQL() {
		con = MyDB.getCon();
		String sql = "insert into ranking(score) values(?)";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setDouble(1, dScore);
			pstmt.executeUpdate();
			
			if (con != null)
			{
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void execSQL() {
		con = MyDB.getCon();
		
		String sql = "SELECT max(score) from ranking;";

		try {
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("==============");
			while(rs.next())
			{
				sMaxScore = rs.getDouble("max(score)");
				System.out.println("sMaxScore : " + sMaxScore);
			}
			
			if (con != null)
			{
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}