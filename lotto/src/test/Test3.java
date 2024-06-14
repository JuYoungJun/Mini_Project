package test;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;
import java.util.Timer;
import java.text.*;
import java.time.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Mysqlconnection.DBConnection;

public class Test3 extends JFrame implements ActionListener {

	// 1~45버튼 배열
	private String[] btn_Title = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
			"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45" };

	JToggleButton[] jbtn = new JToggleButton[btn_Title.length];

	private JPanel contentPane;

	List<Integer> selectNum = new ArrayList<>();

	private JButton ok;
	private JButton resetButton;
	private JButton autoButton;
	private JButton viewResults;
	private JButton allReset;
	private JButton allClose;
	private JButton watchDrawButton;
	private JButton purchaseButton;

	private int selectCount;

	private boolean clickAuto;

	private JLabel resultPrice;
	private String price;
	private int count = 0;

	private JButton[] btnChange = new JButton[5];

	private JButton[] btnDelete = new JButton[5];

	private JLabel[][] showBall = new JLabel[5][6];

	private JLabel[] showBallTypes = new JLabel[5];

	private Integer[][] number = new Integer[5][6];

	private int size;

	private Map<Integer, String> ballType = new HashMap<>();

	private boolean[] clickToggle = new boolean[45];

	private JButton rank;

	private JComboBox<String> roundComboBox;
	private JPanel resultPanel = new JPanel();

	// 점수 맵
	private Map<Integer, Integer[][]> score = new HashMap<>();

	private JLabel timerLabel; // 타이머 레이블
	private JLabel prizeLabel; // 상금 레이블
	private Timer timer;

	public Map<Integer, Integer[][]> getScore() {
		return score;
	}

	public void setScore(Map<Integer, Integer[][]> score) {
		this.score = score;
	}

	public JLabel[] getShowBallTypes() {
		return showBallTypes;
	}

	public void setShowBallTypes(JLabel[] showBallTypes) {
		this.showBallTypes = showBallTypes;
	}

	public JLabel[][] getShowBall() {
		return showBall;
	}

	public void setShowBall(JLabel[][] showBall) {
		this.showBall = showBall;
	}

	public Test3() {
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(500, 200, 975, 600);
		contentPane = new JPanel();
		contentPane.setForeground(Color.white);
		contentPane.setBackground(Color.white);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.white);
		panel_2.setBounds(78, 140, 246, 365);
		contentPane.add(panel_2);
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
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		ImageIcon im = new ImageIcon("src/numbers_png/test.png");
		JLabel lbNewLabel = new JLabel();
		lbNewLabel.setIcon(im);
		lbNewLabel.setBounds(360, 30, 650, 60);
		contentPane.add(lbNewLabel);

		resetButton = new JButton("초기화");
		resetButton.setFont(new Font("맑은 고딕", 1, 12));
		resetButton.setBackground(Color.white);
		panel_1.add(resetButton);
		resetButton.addActionListener(this);

		autoButton = new JButton("자동 선택");
		autoButton.setFont(new Font("맑은 고딕", 1, 12));
		autoButton.setBackground(Color.white);
		panel_1.add(autoButton);
		autoButton.addActionListener(this);

		ok = new JButton("확인");
		ok.setFont(new Font("맑은 고딕", 1, 12));
		ok.setBackground(Color.white);
		panel_1.add(ok);
		ok.addActionListener(this);

		rank = new JButton("상세보기");
		rank.setFont(new Font("맑은 고딕", 1, 12));
		rank.setBackground(Color.white);
		rank.setBounds(55, 65, 100, 25);
		rank.setPreferredSize(new Dimension(100, 100));
		contentPane.add(rank);
		rank.addActionListener(this);

		watchDrawButton = new JButton("추첨방송 보러가기");
		watchDrawButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		watchDrawButton.setBackground(Color.white);
		watchDrawButton.setBounds(170, 65, 150, 25); // 위치 조정
		contentPane.add(watchDrawButton);
		watchDrawButton.addActionListener(this);

		// 구매하러가기 버튼 추가
		purchaseButton = new JButton("구매하러가기");
		purchaseButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		purchaseButton.setBackground(Color.white);
		purchaseButton.setBounds(400, 100, 120, 25);
		contentPane.add(purchaseButton);
		purchaseButton.addActionListener(this);
		purchaseButton.setVisible(false);

		// 타이머 레이블 생성 및 설정
		timerLabel = new JLabel("0000회차까지 남은시간: 0일 00:00:00");
		timerLabel.setBounds(34, 10, 273, 22);
		contentPane.add(timerLabel);
		timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		timerLabel.setForeground(Color.gray);

		// 상금 레이블 생성 및 설정
		prizeLabel = new JLabel("전 회차 1등 당첨 금액: ");
		prizeLabel.setBounds(34, 33, 400, 22);
		contentPane.add(prizeLabel);
		prizeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		prizeLabel.setForeground(Color.gray);

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateTimer();
			}
		}, 0, 1000);

		updatePrizeAndTimer();

		int i;

		for (i = 0; i < btn_Title.length; i++) {
			panel.add(jbtn[i] = new JToggleButton(btn_Title[i]));
			jbtn[i].setFont(new Font("맑은 고딕", 1, 12));
			jbtn[i].setMargin(new Insets(0, 0, 0, 0));
			jbtn[i].setBackground(Color.WHITE);
			jbtn[i].addActionListener(this);
			clickToggle[i] = false;
		}

		for (i = 0; i < btnChange.length; i++) {
			btnChange[i] = new JButton("수정");
			btnChange[i].setFont(new Font("맑은 고딕", 1, 12));
			btnChange[i].setBounds(360, 20 + i * 60, 60, 23);
			btnChange[i].setBackground(Color.white);
			panel_3.add(btnChange[i]);
			btnChange[i].addActionListener(this);
		}

		for (i = 0; i < btnDelete.length; i++) {
			btnDelete[i] = new JButton("삭제");
			btnDelete[i].setFont(new Font("맑은 고딕", 1, 12));
			btnDelete[i].setBounds(430, 20 + i * 60, 60, 23);
			btnDelete[i].setBackground(Color.white);
			panel_3.add(btnDelete[i]);
			btnDelete[i].addActionListener(this);
		}

		viewResults = new JButton("결과 보기");
		viewResults.setFont(new Font("맑은 고딕", 1, 13));
		viewResults.setBounds(140, 340, 120, 32);
		viewResults.setBackground(Color.white);
		contentPane.add(viewResults);
		viewResults.addActionListener(this);
		panel_3.add(viewResults);

		allReset = new JButton("초기화");
		allReset.setFont(new Font("맑은 고딕", 1, 12));
		allReset.setBackground(Color.white);
		contentPane.add(allReset);
		allReset.setBounds(270, 340, 120, 32);
		panel_3.add(allReset);
		allReset.setBackground(Color.white);
		allReset.addActionListener(this);

		allClose = new JButton("종료");
		allClose.setFont(new Font("맑은 고딕", 1, 12));
		allClose.setBounds(400, 340, 120, 32);
		allClose.setBackground(Color.white);
		panel_3.add(allClose);
		allClose.addActionListener(this);

		JLabel word = new JLabel("총 가격 : ");
		word.setFont(new Font("맑은 고딕", 1, 13));
		word.setBackground(Color.white);
		word.setHorizontalTextPosition(0);
		word.setHorizontalAlignment(0);
		word.setBounds(5, 340, 70, 32);
		panel_3.add(word);

		resultPrice = new JLabel("0원");
		resultPrice.setFont(new Font("맑은 고딕", 1, 13));
		resultPrice.setBackground(Color.white);
		resultPrice.setHorizontalTextPosition(0);
		resultPrice.setHorizontalAlignment(0);
		resultPrice.setBounds(37, 340, 120, 32);
		panel_3.add(resultPrice);

		JLabel[] showBallName = new JLabel[5];
		String[] ballName = { "A", "B", "C", "D", "E" };
		int k;
		for (k = 0; k < showBallName.length; k++) {
			showBallName[k] = new JLabel(ballName[k]);
			showBallName[k].setFont(new Font("맑은 고딕", 1, 12));
			showBallName[k].setBounds(12, 25 + 60 * k, 18, 15);
			panel_3.add(showBallName[k]);
		}

		ballType.put(Integer.valueOf(1), "");
		ballType.put(Integer.valueOf(2), "수동");
		ballType.put(Integer.valueOf(3), "반자동");
		ballType.put(Integer.valueOf(4), "자동");

		for (k = 0; k < showBallTypes.length; k++) {
			showBallTypes[k] = new JLabel(ballType.get(Integer.valueOf(1)));
			showBallTypes[k].setFont(new Font("맑은 고딕", 1, 12));
			showBallTypes[k].setBounds(35, 25 + 60 * k, 62, 15);
			panel_3.add(showBallTypes[k]);
		}

		for (i = 0; i < showBall.length; i++) {
			for (int j = 0; j < showBall[i].length; j++) {
				showBall[i][j] = new JLabel();
				showBall[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				showBall[i][j].setBounds(60 * j, 20 + i * 60, 50, 50);
				panel_3.add(showBall[i][j]);
			}
		}

		for (i = 0; i < showBallTypes.length; i++) {
			showBallTypes[i] = new JLabel();
			showBallTypes[i].setBounds(300, 20 + i * 60, 60, 50);
			showBallTypes[i].setHorizontalAlignment(SwingConstants.CENTER);
			panel_3.add(showBallTypes[i]);
		}

		roundComboBox = new JComboBox<>();
		roundComboBox.setBounds(800, 50, 120, 30);
		roundComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showSelectedRoundNumbers();
			}
		});
		contentPane.add(roundComboBox);

		resultPanel.setBounds(750, 80, 210, 35);
		resultPanel.setBackground(Color.white);
		resultPanel.setLayout(new FlowLayout());
		contentPane.add(resultPanel);

		fetchDataFromBatabase();

		ok.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 자동 선택 버튼
		if (e.getSource() == autoButton) {
			clickAuto = true;
			autoSelect();
		}
		// 초기화 버튼
		else if (e.getSource() == resetButton) {
			reset();
		}
		// 확인 버튼
		else if (e.getSource() == ok) {
			confirmSelection();
		}
		// 결과 보기 버튼
		else if (e.getSource() == viewResults) {
			viewResults();
		}
		// 전체 초기화 버튼
		else if (e.getSource() == allReset) {
			allReset();
		}
		// 종료 버튼
		else if (e.getSource() == allClose) {
			System.exit(0);
		}
		// 이전 기록 버튼
		else if (e.getSource() == rank) {
			// 알림 창으로 표시할지 여부를 사용자에게 물음
			int choice = JOptionPane.showConfirmDialog(this, "알림 창으로 표시하시겠습니까?", "이전 기록", JOptionPane.YES_NO_OPTION);
			// 사용자의 선택에 따라 boolean 값 설정
			boolean useNotification = (choice == JOptionPane.YES_OPTION);
			// 선택된 옵션을 기반으로 이전 기록을 표시
			viewPreviousRecords(useNotification);
		}
		// 추첨방송 보러가기 버튼
		else if (e.getSource() == watchDrawButton) {
			openDrawPage();
		}
		// 구매하러가기 버튼
		else if (e.getSource() == purchaseButton) {
			goToBuyPage();
		}
		// 숫자 버튼
		else {
			for (int i = 0; i < jbtn.length; i++) {
				if (e.getSource() == jbtn[i]) {
					handleToggleButton(i);
					break;
				}
			}
			// 수정 버튼
			for (int i = 0; i < btnChange.length; i++) {
				if (e.getSource() == btnChange[i]) {
					changeSelection(i);
					break;
				}
			}
			// 삭제 버튼
			for (int i = 0; i < btnDelete.length; i++) {
				if (e.getSource() == btnDelete[i]) {
					deleteSelection(i);
					break;
				}
			}
		}
	}

	// 수동 자동 반자동 부분
	public int whatBefore(String before) {
		if (before.equals("수동"))
			return 2;
		if (before.equals("반자동"))
			return 3;
		if (before.equals("자동"))
			return 4;
		return 1;
	}

	private void showPrice() {
		price = "0원";

		switch (count) {
		case 1:
			price = "1000원";
			break;
		case 2:
			price = "2000원";
			break;
		case 3:
			price = "3000원";
			break;
		case 4:
			price = "4000원";
			break;
		case 5:
			price = "5000원";
			break;
		}

		resultPrice.setText(price);
	}

	private void viewPreviousRecords(boolean useNotification) {
		String selectedRound = (String) roundComboBox.getSelectedItem();
		if (selectedRound != null && !selectedRound.isEmpty()) {
			int round = Integer.parseInt(selectedRound.substring(0, selectedRound.indexOf("회차")));
			String drawingDate = getDrawingDateForRound(round);
			String message = getWinningDetailsMessage(round, drawingDate);

			if (useNotification) {
				JOptionPane.showMessageDialog(this, message, "이전 기록", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JFrame frame = new JFrame("이전 기록");
				frame.setResizable(false);
				frame.setSize(300, 200);
				JTextArea textArea = new JTextArea(message);
				textArea.setEditable(false);
				frame.add(new JScrollPane(textArea));
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		}
	}

	private String getWinningDetailsMessage(int round, String drawingDate) {
		StringBuilder message = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#,###");
		try {
			Connection conn = DBConnection.getConnection();
			if (conn != null) {
				String sql = "select * from lotto_numbers where round = ?";
				try (PreparedStatement psmt = conn.prepareStatement(sql)) {
					psmt.setInt(1, round);
					try (ResultSet rs = psmt.executeQuery()) {
						if (rs.next()) {
							long prize1st = rs.getLong("first_prize");
							long prize2nd = rs.getLong("second_prize");
							long prize3rd = rs.getLong("third_prize");
							long prize4th = rs.getLong("fourth_prize");
							long prize5th = rs.getLong("fifth_prize");

							String formattedPrize1st = df.format(prize1st);
							String formattedPrize2nd = df.format(prize2nd);
							String formattedPrize3rd = df.format(prize3rd);
							String formattedPrize4th = df.format(prize4th);
							String formattedPrize5th = df.format(prize5th);

							message.append(String.format("회차: %d\n추첨일: %s\n", round, drawingDate));
							message.append(String.format("1등 당첨금: %s\n2등 당첨금: %s\n3등 당첨금: %s\n4등 당첨금: %s\n5등 당첨금: %s\n",
									formattedPrize1st, formattedPrize2nd, formattedPrize3rd, formattedPrize4th,
									formattedPrize5th));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return message.toString();
	}

	private String getDrawingDateForRound(int round) {
		try {
			Connection conn = DBConnection.getConnection();
			if (conn != null) {
				String sql = "select date from lotto_numbers where round = ?";
				try (PreparedStatement psmt = conn.prepareStatement(sql)) {
					psmt.setInt(1, round);
					try (ResultSet rs = psmt.executeQuery()) {
						if (rs.next()) {
							return rs.getString("date");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "에러";
	}

	private void viewResults() {
		JOptionPane.showMessageDialog(this, "결과를 표시하는 기능이 아직 구현되지 않았습니다.", "결과", JOptionPane.INFORMATION_MESSAGE);
	}

	private void handleToggleButton(int index) {
		// 토글 버튼 처리
		if (jbtn[index].isSelected()) {
			if (selectCount < 6) {
				selectNum.add(Integer.parseInt(jbtn[index].getText()));
				selectCount++;
				jbtn[index].setBackground(Color.LIGHT_GRAY);
			} else {
				jbtn[index].setSelected(false);
				JOptionPane.showMessageDialog(this, "최대 6개의 숫자만 선택할 수 있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			selectNum.remove(Integer.valueOf(jbtn[index].getText()));
			selectCount--;
			jbtn[index].setBackground(Color.white);
		}
		ok.setEnabled(selectCount == 6);
	}

	private void autoSelect() {
		reset();
		Random rand = new Random();
		while (selectNum.size() < 6) {
			int num = rand.nextInt(45) + 1;
			if (!selectNum.contains(num)) {
				selectNum.add(num);
				jbtn[num - 1].setSelected(true);
				jbtn[num - 1].setBackground(Color.lightGray);
				selectCount++;
			}
		}
		ok.setEnabled(true);
	}

	private void reset() {
		selectNum.clear();
		selectCount = 0;
		for (int i = 0; i < jbtn.length; i++) {
			jbtn[i].setSelected(false);
			jbtn[i].setBackground(Color.white);
		}
		ok.setEnabled(false);
	}

	private void confirmSelection() {
		for (int i = 0; i < 6; i++) {
			int selectedNumber = selectNum.get(i);
			showBall[size][i].setText(String.valueOf(selectedNumber));
			showBall[size][i].setText("");

			String imagePath = "src/numbers_png/ball_" + selectedNumber + ".png";

			ImageIcon icon = new ImageIcon(imagePath);
			Image image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
			ImageIcon resizedIcon = new ImageIcon(image);
			showBall[size][i].setIcon(resizedIcon);
		}
		size++;

		if (size == 5) {
			ok.setEnabled(false);
			resetButton.setEnabled(false); // 초기화 버튼 비활성화
			autoButton.setEnabled(false); // 자동 선택 버튼 비활성화

			// 1~45 버튼 비활성화
			for (int i = 0; i < btn_Title.length; i++) {
				jbtn[i].setEnabled(false);
			}
		}
		reset();

		count++;
		showPrice();

		purchaseButton.setVisible(true);
	}

	private void allReset() {
		size = 0;
		for (int i = 0; i < showBall.length; i++) {
			for (int j = 0; j < showBall[i].length; j++) {
				showBall[i][j].setText("");
				showBall[i][j].setIcon(null); // 아이콘 초기화 추가
			}
			showBallTypes[i].setIcon(null); // 아이콘 초기화 추가
		}
		reset();

		resetButton.setEnabled(true); // 초기화 버튼 비활성화
		autoButton.setEnabled(true); // 자동 선택 버튼 비활성화

		// 1~45 버튼 비활성화
		for (int i = 0; i < btn_Title.length; i++) {
			jbtn[i].setEnabled(true);
		}

		purchaseButton.setVisible(false);

		count = 0;
		showPrice();
	}

	private void changeSelection(int index) {
		selectNum.clear();
		selectCount = 0;
		if (number[index] != null) {
			System.out.println("인덱스 " + index + "의 숫자 배열은 null이 아닙니다.");
			System.out.println("숫자 배열 길이: " + number[index].length);
			for (int i = 0; i < number[index].length; i++) {
				System.out.println("인덱스 " + index + "의 숫자 배열의 " + i + "번째 요소: " + number[index][i]);
				if (number[index][i] == null) {
					JOptionPane.showMessageDialog(this, "수정할 내용이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					continue;
				}
				int selectedNumber = number[index][i].intValue();
				selectNum.add(selectedNumber);
				selectCount++;
				jbtn[selectedNumber - 1].setSelected(true);
				jbtn[selectedNumber - 1].setBackground(Color.lightGray);
			}
		} else {
			System.out.println("인덱스 " + index + "의 숫자 배열은 null입니다.");
			JOptionPane.showMessageDialog(this, "수정할 내용이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
		}
		ok.setEnabled(true);
	}

	private void deleteSelection(int index) {
		for (int i = 0; i < showBall[index].length; i++) {
			showBall[index][i].setText(""); // 해당하는 라벨 비움
			showBall[index][i].setIcon(null); // 이미지 제거
		}
		size--;

		count--;
		showPrice();

		resetButton.setEnabled(true); // 초기화 버튼 활성화
		autoButton.setEnabled(true); // 자동 선택 버튼 활성화

		for (int i = 0; i < btn_Title.length; i++) {
			jbtn[i].setEnabled(true);
		}

		if (size == 0) {
			purchaseButton.setVisible(false);
		}

	}

	private void fetchDataFromBatabase() {
		try {
			Connection conn = DBConnection.getConnection();
			if (conn != null) {
				String sql = "select round from lotto_numbers order by round desc";

				try (PreparedStatement psmt = conn.prepareStatement(sql); ResultSet rs = psmt.executeQuery()) {
					List<String> rounds = new ArrayList<>();
					while (rs.next()) {
						int round = rs.getInt("round");
						rounds.add(String.valueOf(round));
					}
					for (String round : rounds) {
						roundComboBox.addItem(round + "회차");
					}
					roundComboBox.setSelectedIndex(0);
					showSelectedRoundNumbers();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showSelectedRoundNumbers() {
		String selectedRound = (String) roundComboBox.getSelectedItem();
		if (selectedRound != null && !selectedRound.isEmpty()) {
			int round = Integer.parseInt(selectedRound.substring(0, selectedRound.indexOf("회차")));
			List<String> numbers = getNumbersForRound(round);
			displayImages(numbers);
		}
	}

	private List<String> getNumbersForRound(int round) {
		List<String> numbers = new ArrayList<String>();
		try {
			Connection conn = DBConnection.getConnection();
			if (conn != null) {
				String sql = "select numbers, bonus_number from lotto_numbers where round = ?";
				try (PreparedStatement psmt = conn.prepareStatement(sql)) {
					psmt.setInt(1, round);
					try (ResultSet rs = psmt.executeQuery()) {
						if (rs.next()) {
							String[] nums = rs.getString("numbers").split(", ");
							numbers.addAll(Arrays.asList(nums));
							numbers.add(String.valueOf(rs.getInt("bonus_number")));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numbers;
	}

	private void displayImages(List<String> numbers) {
		resultPanel.removeAll();

		JPanel imagePanel = new JPanel(new FlowLayout());

		for (int i = 0; i < numbers.size() - 1; i++) {
			String number = numbers.get(i);
			String imagePath = "src/numbers_png/ball_" + number + ".png";
			ImageIcon icon = new ImageIcon(imagePath);
			try {
				Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
				ImageIcon scaledIcon = new ImageIcon(scaledImage);
				JLabel label = new JLabel(scaledIcon);
				imagePanel.add(label);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String bonusNumber = numbers.get(numbers.size() - 1);
		String bonusImagePath = "src/numbers_png/ball_" + bonusNumber + ".png";
		ImageIcon bonusIcon = new ImageIcon(bonusImagePath);
		try {
			Image scaledBonusImage = bonusIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			ImageIcon scaledBonusIcon = new ImageIcon(scaledBonusImage);
			JLabel bonusLabel = new JLabel(scaledBonusIcon);
			imagePanel.add(new JLabel(" + "));
			imagePanel.add(bonusLabel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultPanel.add(imagePanel);
		resultPanel.revalidate();
		resultPanel.repaint();
	}

	// 추첨방송 페이지 열기
	private void openDrawPage() {
		String url;
		LocalDateTime nextDrawTime = getNextDrawTime();
		if (LocalDateTime.now().compareTo(nextDrawTime) >= 0) {
			url = "https://onair.imbc.com/";
		} else {
			url = "https://program.imbc.com/lotto";
		}
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	// 최신 회차를 가져오는 메서드
	private int getLatestRound() {
		int latestRound = 0;
		try (Connection conn = DBConnection.getConnection()) {
			if (conn != null) {
				String sql = "select max(round) as latest_round from lotto_numbers";
				try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						latestRound = rs.getInt("latest_round");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return latestRound;
	}

	// 주어진 회차 이후의 다음 추첨 시간을 계산
	private LocalDateTime getNextDrawTime() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextDraw = now.with(DayOfWeek.SATURDAY).withHour(20).withMinute(34).withSecond(0).withNano(0);
		if (now.compareTo(nextDraw) >= 0) {
			nextDraw = nextDraw.plusWeeks(1);
		}
		return nextDraw;
	}

	// 타이머 업데이트 및 남은 시간 표시
	private void updateTimer() {
		int latestRound = getLatestRound();
		LocalDateTime nextDrawTime = getNextDrawTime();
		Duration duration = Duration.between(LocalDateTime.now(), nextDrawTime);
		long days = duration.toDays();
		long hours = duration.toHours() % 24;
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;
		timerLabel.setText(
				String.format("%04d회차까지 남은시간: %d일 %02d:%02d:%02d", latestRound + 1, days, hours, minutes, seconds));
	}

	// 최신 회차의 당첨금 및 남은 시간 초기화
	private void updatePrizeAndTimer() {
		displayPrize();
		updateTimer();
	}

	// 최신 회차의 당첨금 표시
	private void displayPrize() {
		int latestRound = getLatestRound();
		try (Connection conn = DBConnection.getConnection()) {
			if (conn != null) {
				String sql = "select first_prize from lotto_numbers where round = ?";
				try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setInt(1, latestRound);
					try (ResultSet rs = pstmt.executeQuery()) {
						if (rs.next()) {
							long firstPrize = rs.getLong("first_prize");
							DecimalFormat df = new DecimalFormat("#,###");
							String formattedPrize = df.format(firstPrize);
							prizeLabel.setText(latestRound + " 회차 1등 당첨 금액: " + formattedPrize + "원");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void goToBuyPage() {
		try {
			String url = "https://dhlottery.co.kr/gameInfo.do?method=buyLotto";
			System.out.println("URL을 열려고 시도 중: " + url);
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Test3 f = new Test3();
		f.setVisible(true);

	}

}
