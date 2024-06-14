package Gui;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.*;
import java.util.*;
import java.util.List;

// GUI 메서드를 정의하는 클래스
public class Method extends JFrame {

	DAO DAO; // 데이터 액세스 객체
	UI ui; // 사용자 인터페이스 객체
	variable var; // 변수 객체

	// 생성자
	public Method(variable var, DAO DAO) {
		this.var = var;
		this.DAO = DAO;
	}

	// UI 설정 메서드
	public void setUI(UI ui) {
		this.ui = ui;
	}

	// DAO 설정 메서드
	public void setDAO(DAO DAO) {
		this.DAO = DAO;
	}

	// 이전에 선택한 옵션에 따라 정수 반환
	public int whatBefore(String before) {
		if (before.equals("수동"))
			return 2;
		if (before.equals("반자동"))
			return 3;
		if (before.equals("자동"))
			return 4;
		return 1;
	}

	// 선택된 번호에 따라 가격 표시
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

	// 이전 기록 보기
	public void viewPreviousRecords(boolean useNotification) {
		String selectedRound = (String) var.roundComboBox.getSelectedItem();
		if (selectedRound != null && !selectedRound.isEmpty()) {
			int round = Integer.parseInt(selectedRound.substring(0, selectedRound.indexOf("회차")));
			String drawingDate = DAO.getDrawingDateForRound(round);
			String message = DAO.getWinningDetailsMessage(round, drawingDate);

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

	// 토글 버튼 처리
	public void handleToggleButton(int index) {
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

	// 자동 선택
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

	// 선택 초기화
	public void reset() {
		var.selectNum.clear();
		var.selectCount = 0;
		for (int i = 0; i < var.jbtn.length; i++) {
			var.jbtn[i].setSelected(false);
			var.jbtn[i].setBackground(Color.white);
		}
		var.ok.setEnabled(false);
	}

	// 선택 확인
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
			var.resetButton.setEnabled(false);
			var.autoButton.setEnabled(false);

			for (int i = 0; i < var.btn_Title.length; i++) {
				var.jbtn[i].setEnabled(false);
			}
		}
		reset();

		var.count++;
		showPrice();

		var.purchaseButton.setVisible(true);
	}

	// 전체 초기화
	public void allReset() {
		var.size = 0;
		for (int i = 0; i < var.showBall.length; i++) {
			for (int j = 0; j < var.showBall[i].length; j++) {
				var.showBall[i][j].setText("");
				var.showBall[i][j].setIcon(null);
			}
			var.showBallTypes[i].setIcon(null);
		}
		reset();

		var.resetButton.setEnabled(true);
		var.autoButton.setEnabled(true);

		for (int i = 0; i < var.btn_Title.length; i++) {
			var.jbtn[i].setEnabled(true);
		}

		var.purchaseButton.setVisible(false);

		var.count = 0;
		showPrice();
	}

	// 수정 부분
	public void changeSelection(int index) {
		var.selectNum.clear();
		var.selectCount = 0;
		if (var.number[index] != null && var.number[index].length == 6) {
			for (int i = 0; i < var.number[index].length; i++) {
				Integer selectedNumber = var.number[index][i];
				if (selectedNumber != null) {
					var.selectNum.add(selectedNumber);
					var.selectCount++;
					if (selectedNumber >= 1 && selectedNumber <= 45) { // 수정된 부분
						var.jbtn[selectedNumber - 1].setSelected(true);
						var.jbtn[selectedNumber - 1].setBackground(Color.lightGray);
					} else {
						System.out.println("잘못된 숫자: " + selectedNumber);
					}
				}
			}
		} else {
			System.out.println("인덱스 " + index + "의 배열이 유효하지 않습니다.");
		}
		var.ok.setEnabled(true);
		System.out.println("선택된 숫자 변경됨: " + var.selectNum);
		System.out.println("선택된 숫자 개수: " + var.selectCount);
	}

	// 선택 삭제
	public void deleteSelection(int index) {
		for (int i = 0; i < var.showBall[index].length; i++) {
			var.showBall[index][i].setText(""); // 해당하는 라벨 비움
			var.showBall[index][i].setIcon(null); // 이미지 제거
		}
		var.size--;

		var.count--;
		showPrice();

		var.resetButton.setEnabled(true);
		var.autoButton.setEnabled(true);

		for (int i = 0; i < var.btn_Title.length; i++) {
			var.jbtn[i].setEnabled(true);
		}

		if (var.size == 0) {
			var.purchaseButton.setVisible(false);
		}
	}

	// 선택된 회차의 숫자 보여주기
	public void showSelectedRoundNumbers() {
		String selectedRound = (String) var.roundComboBox.getSelectedItem();
		if (selectedRound != null && !selectedRound.isEmpty()) {
			int round = Integer.parseInt(selectedRound.substring(0, selectedRound.indexOf("회차")));
			System.out.println("선택된 회차: " + round);
			List<String> numbers = DAO.getNumbersForRound(round);
			System.out.println(round + "회차에 대한 숫자 조회 결과: " + numbers);
			displayImages(numbers);
		} else {
			System.out.println("선택된 회차가 null이거나 비어 있습니다.");
		}
	}

	// 숫자 이미지 표시
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
			System.out.println("URL을 열려고 시도 중: " + url);
		} else {
			url = "https://program.imbc.com/lotto";
			System.out.println("URL을 열려고 시도 중: " + url);
		}
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	// 다음 추첨 시간 계산
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
		if (var.timerLabel != null) {
			int latestRound = DAO.getLatestRound();
			LocalDateTime nextDrawTime = getNextDrawTime();
			Duration duration = Duration.between(LocalDateTime.now(), nextDrawTime);
			long days = duration.toDays();
			long hours = duration.toHours() % 24;
			long minutes = duration.toMinutes() % 60;
			long seconds = duration.getSeconds() % 60;
			var.timerLabel.setText(
					String.format("%04d회차까지 남은시간: %d일 %02d:%02d:%02d", latestRound + 1, days, hours, minutes, seconds));
		} else {
			System.out.println("timerLabel이 초기화되지 않았습니다.");
		}
	}

	// 최신 회차의 당첨금 및 남은 시간 초기화
	public void updatePrizeAndTimer() {
		DAO.displayPrize();
		updateTimer();
	}

	// 구매 페이지로 이동
	public void goToBuyPage() {
		try {
			String url = "https://dhlottery.co.kr/gameInfo.do?method=buyLotto";
			System.out.println("URL을 열려고 시도 중: " + url);
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	// 결과 표시를 위한 메시지 다이얼로그를 띄우는 메서드
	public void viewResults() {
		JOptionPane.showMessageDialog(this, "결과를 표시하는 기능이 아직 구현되지 않았습니다.", "결과", JOptionPane.INFORMATION_MESSAGE);
	}

}
