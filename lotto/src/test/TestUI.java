package test;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import test.Testvariable;
import test.TestMethod;

public class TestUI extends JFrame implements ActionListener {

	public TestMethod method = new TestMethod();
	public Testvariable var = new Testvariable();

	public TestUI() {
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(500, 200, 975, 600);
		var.contentPane = new JPanel();
		var.contentPane.setForeground(Color.white);
		var.contentPane.setBackground(Color.white);
		var.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(var.contentPane);
		var.contentPane.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.white);
		panel_2.setBounds(78, 140, 246, 365);
		var.contentPane.add(panel_2);
		panel_2.setLayout(new BorderLayout(50, 20));
		JPanel panel = new JPanel();
		panel_2.add(panel, "Center");
		panel.setBackground(Color.white);
		panel.setForeground(Color.white);
		panel.setLayout(new GridLayout(0, 5, 26, 5));

		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, "South");
		panel_1.setForeground(Color.white);
		panel_1.setBackground(Color.white);
		panel_1.setLayout(new FlowLayout(1, 5, 5));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(null);
		panel_3.setBounds(400, 135, 520, 382);
		panel_3.setBackground(Color.white);
		var.contentPane.add(panel_3);
		panel_3.setLayout(null);

		ImageIcon im = new ImageIcon("src/numbers_png/test.png");
		JLabel lbNewLabel = new JLabel();
		lbNewLabel.setIcon(im);
		lbNewLabel.setBounds(360, 30, 650, 60);
		var.contentPane.add(lbNewLabel);

		var.resetButton = new JButton("초기화");
		var.resetButton.setFont(new Font("맑은 고딕", 1, 12));
		var.resetButton.setBackground(Color.white);
		panel_1.add(var.resetButton);
		var.resetButton.addActionListener(this);

		var.autoButton = new JButton("자동 선택");
		var.autoButton.setFont(new Font("맑은 고딕", 1, 12));
		var.autoButton.setBackground(Color.white);
		panel_1.add(var.autoButton);
		var.autoButton.addActionListener(this);

		var.ok = new JButton("확인");
		var.ok.setFont(new Font("맑은 고딕", 1, 12));
		var.ok.setBackground(Color.white);
		panel_1.add(var.ok);
		var.ok.addActionListener(this);

		var.rank = new JButton("상세보기");
		var.rank.setFont(new Font("맑은 고딕", 1, 12));
		var.rank.setBackground(Color.white);
		var.rank.setBounds(55, 65, 100, 25);
		var.rank.setPreferredSize(new Dimension(100, 100));
		var.contentPane.add(var.rank);
		var.rank.addActionListener(this);

		var.watchDrawButton = new JButton("추첨방송 보러가기");
		var.watchDrawButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		var.watchDrawButton.setBackground(Color.white);
		var.watchDrawButton.setBounds(170, 65, 150, 25); // 위치 조정
		var.contentPane.add(var.watchDrawButton);
		var.watchDrawButton.addActionListener(this);

		// 구매하러가기 버튼 추가
		var.purchaseButton = new JButton("구매하러가기");
		var.purchaseButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		var.purchaseButton.setBackground(Color.white);
		var.purchaseButton.setBounds(400, 100, 120, 25);
		var.contentPane.add(var.purchaseButton);
		var.purchaseButton.addActionListener(this);
		var.purchaseButton.setVisible(false);

		// 타이머 레이블 생성 및 설정
		var.timerLabel = new JLabel("0000회차까지 남은시간: 0일 00:00:00");
		var.timerLabel.setBounds(34, 10, 273, 22);
		var.contentPane.add(var.timerLabel);
		var.timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		var.timerLabel.setForeground(Color.gray);

		// 상금 레이블 생성 및 설정
		var.prizeLabel = new JLabel("전 회차 1등 당첨 금액: ");
		var.prizeLabel.setBounds(34, 33, 400, 22);
		var.contentPane.add(var.prizeLabel);
		var.prizeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		var.prizeLabel.setForeground(Color.gray);

		var.timer = new Timer();
		var.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				method.updateTimer();
			}
		}, 0, 1000);

		method.updatePrizeAndTimer();

		int i;

		for (i = 0; i < var.btn_Title.length; i++) {
			panel.add(var.jbtn[i] = new JToggleButton(var.btn_Title[i]));
			var.jbtn[i].setFont(new Font("맑은 고딕", 1, 12));
			var.jbtn[i].setMargin(new Insets(0, 0, 0, 0));
			var.jbtn[i].setBackground(Color.WHITE);
			var.jbtn[i].addActionListener(this);
			var.clickToggle[i] = false;
		}

		for (i = 0; i < var.btnChange.length; i++) {
			var.btnChange[i] = new JButton("수정");
			var.btnChange[i].setFont(new Font("맑은 고딕", 1, 12));
			var.btnChange[i].setBounds(360, 20 + i * 60, 60, 23);
			var.btnChange[i].setBackground(Color.white);
			panel_3.add(var.btnChange[i]);
			var.btnChange[i].addActionListener(this);
		}

		for (i = 0; i < var.btnDelete.length; i++) {
			var.btnDelete[i] = new JButton("삭제");
			var.btnDelete[i].setFont(new Font("맑은 고딕", 1, 12));
			var.btnDelete[i].setBounds(430, 20 + i * 60, 60, 23);
			var.btnDelete[i].setBackground(Color.white);
			panel_3.add(var.btnDelete[i]);
			var.btnDelete[i].addActionListener(this);
		}

		var.viewResults = new JButton("결과 보기");
		var.viewResults.setFont(new Font("맑은 고딕", 1, 13));
		var.viewResults.setBounds(140, 340, 120, 32);
		var.viewResults.setBackground(Color.white);
		var.contentPane.add(var.viewResults);
		var.viewResults.addActionListener(this);
		panel_3.add(var.viewResults);

		var.allReset = new JButton("초기화");
		var.allReset.setFont(new Font("맑은 고딕", 1, 12));
		var.allReset.setBackground(Color.white);
		var.contentPane.add(var.allReset);
		var.allReset.setBounds(270, 340, 120, 32);
		panel_3.add(var.allReset);
		var.allReset.setBackground(Color.white);
		var.allReset.addActionListener(this);

		var.allClose = new JButton("종료");
		var.allClose.setFont(new Font("맑은 고딕", 1, 12));
		var.allClose.setBounds(400, 340, 120, 32);
		var.allClose.setBackground(Color.white);
		panel_3.add(var.allClose);
		var.allClose.addActionListener(this);

		JLabel word = new JLabel("총 가격 : ");
		word.setFont(new Font("맑은 고딕", 1, 13));
		word.setBackground(Color.white);
		word.setHorizontalTextPosition(0);
		word.setHorizontalAlignment(0);
		word.setBounds(5, 340, 70, 32);
		panel_3.add(word);

		var.resultPrice = new JLabel("0원");
		var.resultPrice.setFont(new Font("맑은 고딕", 1, 13));
		var.resultPrice.setBackground(Color.white);
		var.resultPrice.setHorizontalTextPosition(0);
		var.resultPrice.setHorizontalAlignment(0);
		var.resultPrice.setBounds(37, 340, 120, 32);
		panel_3.add(var.resultPrice);

		JLabel[] showBallName = new JLabel[5];
		String[] ballName = { "A", "B", "C", "D", "E" };
		int k;
		for (k = 0; k < showBallName.length; k++) {
			showBallName[k] = new JLabel(ballName[k]);
			showBallName[k].setFont(new Font("맑은 고딕", 1, 12));
			showBallName[k].setBounds(12, 25 + 60 * k, 18, 15);
			panel_3.add(showBallName[k]);
		}

		var.ballType.put(Integer.valueOf(1), "");
		var.ballType.put(Integer.valueOf(2), "수동");
		var.ballType.put(Integer.valueOf(3), "반자동");
		var.ballType.put(Integer.valueOf(4), "자동");

		for (k = 0; k < var.showBallTypes.length; k++) {
			var.showBallTypes[k] = new JLabel(var.ballType.get(Integer.valueOf(1)));
			var.showBallTypes[k].setFont(new Font("맑은 고딕", 1, 12));
			var.showBallTypes[k].setBounds(35, 25 + 60 * k, 62, 15);
			panel_3.add(var.showBallTypes[k]);
		}

		for (i = 0; i < var.showBall.length; i++) {
			for (int j = 0; j < var.showBall[i].length; j++) {
				var.showBall[i][j] = new JLabel();
				var.showBall[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				var.showBall[i][j].setBounds(60 * j, 20 + i * 60, 50, 50);
				panel_3.add(var.showBall[i][j]);
			}
		}

		for (i = 0; i < var.showBallTypes.length; i++) {
			var.showBallTypes[i] = new JLabel();
			var.showBallTypes[i].setBounds(300, 20 + i * 60, 60, 50);
			var.showBallTypes[i].setHorizontalAlignment(SwingConstants.CENTER);
			panel_3.add(var.showBallTypes[i]);
		}

		var.roundComboBox = new JComboBox<>();
		var.roundComboBox.setBounds(800, 50, 120, 30);
		var.roundComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				method.showSelectedRoundNumbers();
			}
		});
		var.contentPane.add(var.roundComboBox);

		var.resultPanel.setBounds(750, 80, 210, 35);
		var.resultPanel.setBackground(Color.white);
		var.resultPanel.setLayout(new FlowLayout());
		var.contentPane.add(var.resultPanel);

		method.fetchDataFromBatabase();

		var.ok.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 자동 선택 버튼
		if (e.getSource() == var.autoButton) {
			var.clickAuto = true;
			method.autoSelect();
		}
		// 초기화 버튼
		else if (e.getSource() == var.resetButton) {
			method.reset();
		}
		// 확인 버튼
		else if (e.getSource() == var.ok) {
			method.confirmSelection();
		}
		// 결과 보기 버튼
		else if (e.getSource() == var.viewResults) {
			method.viewResults();
		}
		// 전체 초기화 버튼
		else if (e.getSource() == var.allReset) {
			method.allReset();
		}
		// 종료 버튼
		else if (e.getSource() == var.allClose) {
			System.exit(0);
		}
		// 이전 기록 버튼
		else if (e.getSource() == var.rank) {
			// 알림 창으로 표시할지 여부를 사용자에게 물음
			int choice = JOptionPane.showConfirmDialog(this, "알림 창으로 표시하시겠습니까?", "이전 기록", JOptionPane.YES_NO_OPTION);
			// 사용자의 선택에 따라 boolean 값 설정
			boolean useNotification = (choice == JOptionPane.YES_OPTION);
			// 선택된 옵션을 기반으로 이전 기록을 표시
			method.viewPreviousRecords(useNotification);
		}
		// 추첨방송 보러가기 버튼
		else if (e.getSource() == var.watchDrawButton) {
			method.openDrawPage();
		}
		// 구매하러가기 버튼
		else if (e.getSource() == var.purchaseButton) {
			method.goToBuyPage();
		}
		// 숫자 버튼
		else {
			for (int i = 0; i < var.jbtn.length; i++) {
				if (e.getSource() == var.jbtn[i]) {
					method.handleToggleButton(i);
					break;
				}
			}
			// 수정 버튼
			for (int i = 0; i < var.btnChange.length; i++) {
				if (e.getSource() == var.btnChange[i]) {
					method.changeSelection(i);
					break;
				}
			}
			// 삭제 버튼
			for (int i = 0; i < var.btnDelete.length; i++) {
				if (e.getSource() == var.btnDelete[i]) {
					method.deleteSelection(i);
					break;
				}
			}
		}
	}
}
