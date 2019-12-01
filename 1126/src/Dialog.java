import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dialog extends JDialog {
	
	JButton btnExit = new JButton("게임종료");
	//UpdatePan pan;	// ActionListener에서 사용하기 위해(UpdatePan의 deleteSQL())
	ImagePaper paper;
	JPanel pan;
	
	public Dialog(JFrame f, ImagePaper paper) {
		// Modal Dialog : 삭제 시 재확인하는 다이알로그
		// call by reference : 이미 생성되어 있는 객체를 다시 생성시키지 않고 참조하여 사용해야 됨
		// 매개변수(f) 전달 : BookFrame -> UpdatePan -> Modal
		super(f, true);	// f가 참조하는  JFrame에 종속된 모달 다이알로그가 됨
		this.paper = paper;	// ActionListener에서 사용하기 위해(UpdatePan의 deleteSQL())
		JLabel lbl = new JLabel("             당신의 점수는 " + paper.dScore + "점 입니다!");
		JLabel lbl2 = new JLabel("                      좀 더 분발하세요..");
		JLabel lblMax = new JLabel("                        최고 기록 갱신!!!");
		
		if((Double) paper.dScore > paper.sMaxScore)
		{
			add(lblMax, "North");
			add(lbl, "Center");
		}
		else
		{
			add(lbl2, "North");
			add(lbl, "Center");
		}
		
		pan = new JPanel();
		pan.add(btnExit);
		add(pan, "South");
		
		btnExit.addActionListener(btnHandler);
		setTitle("T-REX Game");
		setBounds(480, 250, paper.FRAME_W/3, paper.FRAME_H/3);	// 모달 위치와 사이즈 지정
		setVisible(true);
		
	}
	
	ActionListener btnHandler = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			Object eBtn = e.getSource();
			if(eBtn==btnExit)
			{
				Dialog.this.dispose(); // 현재 객체를 소멸시키겠다
				RunThread.main.dispose();
			}
		}
	};
	
}
