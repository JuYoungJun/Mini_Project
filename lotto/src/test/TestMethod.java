package test;

import javax.swing.*;

import Mysqlconnection.DBConnection;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import test.TestUI;
import test.Testvariable;

public class TestMethod extends JFrame {

	public TestUI ui = new TestUI();
	public Testvariable var = new Testvariable();

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

	public void showPrice() {
		var.price = "0원";

		switch (var.count) {
		case 1:
			var.price = "1000원";
			break;
		case 2:
			var.price = "2000원";
			break;
		case 3:
			var.price = "3000원";
			break;
		case 4:
			var.price = "4000원";
			break;
		case 5:
			var.price = "5000원";
			break;
		}

		var.resultPrice.setText(var.price);
	}

	public void viewPreviousRecords(boolean useNotification) {
		String selectedRound = (String) var.roundComboBox.getSelectedItem();
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

	public String getWinningDetailsMessage(int round, String drawingDate) {
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

	public String getDrawingDateForRound(int round) {
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

	public void viewResults() {
		JOptionPane.showMessageDialog(this, "결과를 표시하는 기능이 아직 구현되지 않았습니다.", "결과", JOptionPane.INFORMATION_MESSAGE);
	}

	public void handleToggleButton(int index) {
		// 토글 버튼 처리
		if (var.jbtn[index].isSelected()) {
			if (var.selectCount < 6) {
				var.selectNum.add(Integer.parseInt(var.jbtn[index].getText()));
				var.selectCount++;
				var.jbtn[index].setBackground(Color.LIGHT_GRAY);
			} else {
				var.jbtn[index].setSelected(false);
				JOptionPane.showMessageDialog(this, "최대 6개의 숫자만 선택할 수 있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			var.selectNum.remove(Integer.valueOf(var.jbtn[index].getText()));
			var.selectCount--;
			var.jbtn[index].setBackground(Color.white);
		}
		var.ok.setEnabled(var.selectCount == 6);
	}

	public void autoSelect() {
		reset();
		Random rand = new Random();
		while (var.selectNum.size() < 6) {
			int num = rand.nextInt(45) + 1;
			if (!var.selectNum.contains(num)) {
				var.selectNum.add(num);
				var.jbtn[num - 1].setSelected(true);
				var.jbtn[num - 1].setBackground(Color.lightGray);
				var.selectCount++;
			}
		}
		var.ok.setEnabled(true);
	}

	public void reset() {
		var.selectNum.clear();
		var.selectCount = 0;
		for (int i = 0; i < var.jbtn.length; i++) {
			var.jbtn[i].setSelected(false);
			var.jbtn[i].setBackground(Color.white);
		}
		var.ok.setEnabled(false);
	}

	public void confirmSelection() {
		for (int i = 0; i < 6; i++) {
			int selectedNumber = var.selectNum.get(i);
			var.showBall[var.size][i].setText(String.valueOf(selectedNumber));
			var.showBall[var.size][i].setText("");

			String imagePath = "src/numbers_png/ball_" + selectedNumber + ".png";

			ImageIcon icon = new ImageIcon(imagePath);
			Image image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
			ImageIcon resizedIcon = new ImageIcon(image);
			var.showBall[var.size][i].setIcon(resizedIcon);
		}
		var.size++;

		if (var.size == 5) {
			var.ok.setEnabled(false);
			var.resetButton.setEnabled(false); // 초기화 버튼 비활성화
			var.autoButton.setEnabled(false); // 자동 선택 버튼 비활성화

			// 1~45 버튼 비활성화
			for (int i = 0; i < var.btn_Title.length; i++) {
				var.jbtn[i].setEnabled(false);
			}
		}
		reset();

		var.count++;
		showPrice();

		var.purchaseButton.setVisible(true);
	}

	public void allReset() {
		var.size = 0;
		for (int i = 0; i < var.showBall.length; i++) {
			for (int j = 0; j < var.showBall[i].length; j++) {
				var.showBall[i][j].setText("");
				var.showBall[i][j].setIcon(null); // 아이콘 초기화 추가
			}
			var.showBallTypes[i].setIcon(null); // 아이콘 초기화 추가
		}
		reset();

		var.resetButton.setEnabled(true); // 초기화 버튼 비활성화
		var.autoButton.setEnabled(true); // 자동 선택 버튼 비활성화

		// 1~45 버튼 비활성화
		for (int i = 0; i < var.btn_Title.length; i++) {
			var.jbtn[i].setEnabled(true);
		}

		var.purchaseButton.setVisible(false);

		var.count = 0;
		showPrice();
	}

	public void changeSelection(int index) {
		var.selectNum.clear();
		var.selectCount = 0;
		if (var.number[index] != null) {
			System.out.println("인덱스 " + index + "의 숫자 배열은 null이 아닙니다.");
			System.out.println("숫자 배열 길이: " + var.number[index].length);
			for (int i = 0; i < var.number[index].length; i++) {
				System.out.println("인덱스 " + index + "의 숫자 배열의 " + i + "번째 요소: " + var.number[index][i]);
				if (var.number[index][i] == null) {
					JOptionPane.showMessageDialog(this, "수정할 내용이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					continue;
				}
				int selectedNumber = var.number[index][i].intValue();
				var.selectNum.add(selectedNumber);
				var.selectCount++;
				var.jbtn[selectedNumber - 1].setSelected(true);
				var.jbtn[selectedNumber - 1].setBackground(Color.lightGray);
			}
		} else {
			System.out.println("인덱스 " + index + "의 숫자 배열은 null입니다.");
			JOptionPane.showMessageDialog(this, "수정할 내용이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
		}
		var.ok.setEnabled(true);
	}

	public void deleteSelection(int index) {
		for (int i = 0; i < var.showBall[index].length; i++) {
			var.showBall[index][i].setText(""); // 해당하는 라벨 비움
			var.showBall[index][i].setIcon(null); // 이미지 제거
		}
		var.size--;

		var.count--;
		showPrice();

		var.resetButton.setEnabled(true); // 초기화 버튼 활성화
		var.autoButton.setEnabled(true); // 자동 선택 버튼 활성화

		for (int i = 0; i < var.btn_Title.length; i++) {
			var.jbtn[i].setEnabled(true);
		}

		if (var.size == 0) {
			var.purchaseButton.setVisible(false);
		}

	}

	public void fetchDataFromBatabase() {
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
						var.roundComboBox.addItem(round + "회차");
					}
					var.roundComboBox.setSelectedIndex(0);
					showSelectedRoundNumbers();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showSelectedRoundNumbers() {
		String selectedRound = (String) var.roundComboBox.getSelectedItem();
		if (selectedRound != null && !selectedRound.isEmpty()) {
			int round = Integer.parseInt(selectedRound.substring(0, selectedRound.indexOf("회차")));
			List<String> numbers = getNumbersForRound(round);
			displayImages(numbers);
		}
	}

	public List<String> getNumbersForRound(int round) {
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

	public void displayImages(List<String> numbers) {
		var.resultPanel.removeAll();

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

		var.resultPanel.add(imagePanel);
		var.resultPanel.revalidate();
		var.resultPanel.repaint();
	}

	// 추첨방송 페이지 열기
	public void openDrawPage() {
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
	public int getLatestRound() {
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
	public LocalDateTime getNextDrawTime() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextDraw = now.with(DayOfWeek.SATURDAY).withHour(20).withMinute(34).withSecond(0).withNano(0);
		if (now.compareTo(nextDraw) >= 0) {
			nextDraw = nextDraw.plusWeeks(1);
		}
		return nextDraw;
	}

	// 타이머 업데이트 및 남은 시간 표시
	public void updateTimer() {
		int latestRound = getLatestRound();
		LocalDateTime nextDrawTime = getNextDrawTime();
		Duration duration = Duration.between(LocalDateTime.now(), nextDrawTime);
		long days = duration.toDays();
		long hours = duration.toHours() % 24;
		long minutes = duration.toMinutes() % 60;
		long seconds = duration.getSeconds() % 60;
		var.timerLabel.setText(
				String.format("%04d회차까지 남은시간: %d일 %02d:%02d:%02d", latestRound + 1, days, hours, minutes, seconds));
	}

	// 최신 회차의 당첨금 및 남은 시간 초기화
	public void updatePrizeAndTimer() {
		displayPrize();
		updateTimer();
	}

	// 최신 회차의 당첨금 표시
	public void displayPrize() {
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
							var.prizeLabel.setText(latestRound + " 회차 1등 당첨 금액: " + formattedPrize + "원");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void goToBuyPage() {
		try {
			String url = "https://dhlottery.co.kr/gameInfo.do?method=buyLotto";
			System.out.println("URL을 열려고 시도 중: " + url);
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}
}