package test;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.*;
import java.util.*;
import java.util.List;
import Mysqlconnection.DBConnection;

public class Test2 extends JFrame {

	// 필드 선언
	private JComboBox<String> roundComboBox;
	private JPanel resultPanel;
	private JButton watchDrawButton;
	private JLabel timerLabel;
	private JLabel prizeLabel;

	// 생성자
	public Test2() {
		setTitle("로또 회차 선택");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		// 컨텐츠 패널 생성
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		// 결과 패널
		resultPanel = new JPanel();
		c.add(resultPanel);

		// 상단 패널
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		c.add(topPanel, BorderLayout.NORTH);

		// 회차 선택 패널
		JPanel selectionPanel = new JPanel();
		topPanel.add(selectionPanel, BorderLayout.WEST);
		JLabel label = new JLabel("지난회차 번호보기 ");
		selectionPanel.add(label);
		roundComboBox = new JComboBox<>();
		selectionPanel.add(roundComboBox);

		// 추첨방송 보기 버튼
		watchDrawButton = new JButton("추첨방송 보러가기");
		watchDrawButton.setFont(new Font("맑은고딕", Font.BOLD, 12));
		watchDrawButton.setBackground(Color.white);
		selectionPanel.add(watchDrawButton);

		// 타이머 및 상금 패널
		JPanel timerPanel = new JPanel();
		timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
		topPanel.add(timerPanel, BorderLayout.EAST);
		timerLabel = new JLabel("0000회차까지 남은시간: 0일 00:00:00");
		timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		timerLabel.setForeground(Color.gray);
		timerPanel.add(timerLabel);
		prizeLabel = new JLabel("전 회차 1등 당첨 금액: ");
		prizeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		prizeLabel.setForeground(Color.gray);
		timerPanel.add(prizeLabel);

		// 데이터베이스에서 회차 정보 가져오기
		fetchRoundNumbersFromDatabase();

		// 콤보박스 선택 이벤트 리스너 등록
		roundComboBox.addActionListener(e -> showSelectedRoundNumbers());

		// 초기 이미지 표시
		showSelectedRoundNumbers();

		// 추첨방송 보러가기 버튼 클릭 이벤트 리스너 등록
		watchDrawButton.addActionListener(e -> openDrawPage());

		// 타이머 업데이트
		Timer timer = new Timer(1000, e -> updateTimer());
		timer.start();

		// 프레임 설정
		setPreferredSize(new Dimension(1280, 800));
		pack();
		setVisible(true);

		// 최신 회차의 당첨금 및 남은 시간 초기화
		updatePrizeAndTimer();
	}

	// 데이터베이스에서 회차 정보를 가져와서 콤보 박스에 추가
	private void fetchRoundNumbersFromDatabase() {
		try (Connection conn = DBConnection.getConnection()) {
			if (conn != null) {
				// 콤보박스에 회차 정보 추가
				fetchDataFromDatabase();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 데이터베이스에서 회차 정보를 가져와서 콤보 박스에 추가하는 메서드
	private void fetchDataFromDatabase() {
		try {
			Connection conn = DBConnection.getConnection(); // 데이터베이스 연결
			if (conn != null) {
				String sql = "select round from lotto_numbers order by round desc"; // SQL 쿼리문 작성하여 최신 순서로 정렬
				try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
					List<String> rounds = new ArrayList<>(); // 회차 정보를 담을 리스트 생성
					while (rs.next()) {
						int round = rs.getInt("round"); // 결과에서 회차 정보 가져오기
						rounds.add(String.valueOf(round)); // 리스트에 추가
					}
					// 콤보 박스에 가져온 회차 정보 추가
					for (String round : rounds) {
						roundComboBox.addItem(round + "회차");
					}
					// 최신 회차를 선택한 상태로 초기화
					roundComboBox.setSelectedIndex(0);
					// 초기 상태에서 이미지 표시
					showSelectedRoundNumbers();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 선택한 회차의 당첨 번호 이미지를 가져와서 결과 패널에 표시
	private void showSelectedRoundNumbers() {
		String selectedRound = (String) roundComboBox.getSelectedItem();
		if (selectedRound != null && !selectedRound.isEmpty()) {
			int round = Integer.parseInt(selectedRound.substring(0, selectedRound.indexOf("회차")));
			List<String> numbers = getNumbersForRound(round);
			displayImages(numbers);
		}
	}

	// 선택한 회차의 당첨 번호를 데이터베이스에서 가져오는 메서드
	private List<String> getNumbersForRound(int round) {
		List<String> numbers = new ArrayList<>(); // 당첨 번호를 담을 리스트 생성
		try {
			Connection conn = DBConnection.getConnection(); // 데이터베이스 연결
			if (conn != null) {
				String sql = "select numbers, bonus_number from lotto_numbers where round = ?"; // SQL 쿼리문 작성
				try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setInt(1, round); // SQL 쿼리에 회차 값 설정
					try (ResultSet rs = pstmt.executeQuery()) {
						if (rs.next()) {
							String[] nums = rs.getString("numbers").split(", "); // 당첨번호 가져오기
							numbers.addAll(Arrays.asList(nums)); // 당첨 번호 추가
							numbers.add(String.valueOf(rs.getInt("bonus_number"))); // 보너스 번호 추가
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numbers; // 결과 반환
	}

	// 이미지 표시
	private void displayImages(List<String> numbers) {
		resultPanel.removeAll();
		JPanel imagePanel = new JPanel(new FlowLayout());
		for (int i = 0; i < numbers.size() - 1; i++) {
			String number = numbers.get(i);
			String imagePath = "src/numbers_png/ball_" + number + ".png";
			ImageIcon icon = new ImageIcon(imagePath);
			Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon scaledIcon = new ImageIcon(scaledImage);
			JLabel label = new JLabel(scaledIcon);
			imagePanel.add(label);
		}
		String bonusNumber = numbers.get(numbers.size() - 1);
		String bonusImagePath = "src/numbers_png/ball_" + bonusNumber + ".png";
		ImageIcon bonusIcon = new ImageIcon(bonusImagePath);
		Image scaledBonusImage = bonusIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon scaledBonusIcon = new ImageIcon(scaledBonusImage);
		JLabel bonusLabel = new JLabel(scaledBonusIcon);
		imagePanel.add(new JLabel(" + "));
		imagePanel.add(bonusLabel);
		resultPanel.add(imagePanel);
		resultPanel.revalidate();
		resultPanel.repaint();
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

	// 주어진 회차 이후의 다음 추첨 시간을 계산
	private LocalDateTime getNextDrawTime() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextDraw = now.with(DayOfWeek.SATURDAY).withHour(20).withMinute(34).withSecond(0).withNano(0);
		if (now.compareTo(nextDraw) >= 0) {
			nextDraw = nextDraw.plusWeeks(1);
		}
		return nextDraw;
	}

	// 최신 회차의 당첨금 및 남은 시간 초기화
	private void updatePrizeAndTimer() {
		displayPrize();
		updateTimer();
	}

	public static void main(String[] args) {
		new Test2();
	}
}
