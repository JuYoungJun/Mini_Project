package Gui;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import Mysqlconnection.DBConnection;

// DAO 클래스는 데이터베이스와 관련된 작업을 처리합니다.
public class DAO extends JFrame {

	Method method;
	variable var;

	// DAO 클래스의 생성자
	public DAO(variable var, Method method) {
		this.var = var;
		this.method = method;
	}

	// Method 객체를 설정하는 메서드
	public void setMethod(Method method) {
		this.method = method;
	}

	// 주어진 회차의 당첨 상세 정보 메시지를 반환하는 메서드
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

	// 주어진 회차의 추첨일을 반환하는 메서드
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

	// 데이터베이스에서 정보를 가져와 화면에 표시하는 메서드
	public void fetchDataFromBatabase() {
		if (method != null) {
			System.out.println("Method 객체가 초기화되었습니다."); // 디버깅용 로그
			try {
				Connection conn = DBConnection.getConnection();
				if (conn != null) {
					System.out.println("데이터베이스 연결 성공"); // 디버깅용 로그
					String sql = "select round from lotto_numbers order by round desc";
					try (PreparedStatement psmt = conn.prepareStatement(sql); ResultSet rs = psmt.executeQuery()) {
						List<String> rounds = new ArrayList<>();
						while (rs.next()) {
							int round = rs.getInt("round");
							rounds.add(String.valueOf(round));
						}
						System.out.println("회차 정보 검색 완료"); // 디버깅용 로그
						for (String round : rounds) {
							var.roundComboBox.addItem(round + "회차");
						}
						var.roundComboBox.setSelectedIndex(0);
						System.out.println("회차 콤보박스 설정 완료"); // 디버깅용 로그
						method.showSelectedRoundNumbers();
						System.out.println("선택된 회차 숫자 표시 완료"); // 디버깅용 로그
					}
				} else {
					System.out.println("데이터베이스 연결 실패"); // 디버깅용 로그
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Method 객체가 초기화되지 않았습니다."); // 디버깅용 로그
		}
	}

	// 주어진 회차의 당첨 번호 목록을 가져오는 메서드
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

	// 최신 회차의 1등 당첨 금액을 화면에 표시하는 메서드
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

}
