package lottonumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Mysqlconnection.DBConnection;

public class LottoNumberDAO {
	private Connection conn;

	// 데이터베이스 연결 설정
	public LottoNumberDAO() {
		try {
			this.conn = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 로또 번호와 보너스 번호를 저장하는 메서드
	public boolean saveLottoNumber(LottoNumberDTO lottoNumber) throws SQLException {
		boolean isDuplicate = isDuplicate(lottoNumber);
		if (isDuplicate) {
			System.out.println("회차 " + lottoNumber.getRound() + "의 당첨번호와 보너스 번호는 이미 데이터베이스에 존재합니다.");
			System.out.println("당첨번호: " + lottoNumber.getNumbers() + ", 보너스 번호: " + lottoNumber.getBonusNumber());
			System.out.println("회차 " + lottoNumber.getRound() + "의 당첨금 정보는 이미 데이터베이스에 존재합니다.");
		} else {
			String sql = "INSERT INTO lotto_numbers (round, date, numbers, bonus_number, first_prize, second_prize, third_prize, fourth_prize, fifth_prize) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, lottoNumber.getRound());
				pstmt.setString(2, lottoNumber.getDate());
				pstmt.setString(3, lottoNumber.getNumbers());
				pstmt.setInt(4, lottoNumber.getBonusNumber());

				long[] winnings = lottoNumber.getWinnings();
				// 최소 5개의 요소를 가지도록 배열 크기 조정
				if (winnings.length < 5) {
					long[] newWinnings = new long[5];
					System.arraycopy(winnings, 0, newWinnings, 0, winnings.length);
					winnings = newWinnings;
				}

				// 당첨금 정보 설정
				pstmt.setLong(5, winnings[0]); // 1등
				pstmt.setLong(6, winnings[1]); // 2등
				pstmt.setLong(7, winnings[2]); // 3등
				pstmt.setLong(8, winnings[3]); // 4등
				pstmt.setLong(9, winnings[4]); // 5등

				System.out.println("데이터베이스에 삽입 중:");
				System.out.println("회차: " + lottoNumber.getRound());
				System.out.println("날짜: " + lottoNumber.getDate());
				System.out.println("당첨번호: " + lottoNumber.getNumbers());
				System.out.println("보너스 번호: " + lottoNumber.getBonusNumber());
				System.out.println("당첨금 정보:");
				System.out.println("1등: " + winnings[0]);
				System.out.println("2등: " + winnings[1]);
				System.out.println("3등: " + winnings[2]);
				System.out.println("4등: " + winnings[3]);
				System.out.println("5등: " + winnings[4]);

				int affectedRows = pstmt.executeUpdate();
				if (affectedRows > 0) {
					System.out.println("회차 " + lottoNumber.getRound() + "의 당첨번호와 보너스 번호가 데이터베이스에 성공적으로 저장되었습니다.");
					System.out.println("회차 " + lottoNumber.getRound() + "의 당첨금 정보가 데이터베이스에 성공적으로 저장되었습니다.");
				}
			} catch (SQLException e) {
				System.out.println("DB 저장 중 오류 발생: " + e.getMessage());
				return false;
			}
		}
		return !isDuplicate;
	}

	// 중복 체크를 위한 메서드
	private boolean isDuplicate(LottoNumberDTO lottoNumber) throws SQLException {
		String sql = "SELECT round FROM lotto_numbers WHERE round = ? AND numbers = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, lottoNumber.getRound());
			pstmt.setString(2, lottoNumber.getNumbers());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}

	// 데이터베이스에서 로또 번호와 보너스 번호를 가져와서 LottoNumberDTO 객체로 변환하여 반환하는 메서드
	public List<LottoNumberDTO> getLottoNumbers() {
		List<LottoNumberDTO> lottoNumbers = new ArrayList<>();
		String sql = "SELECT * FROM lotto_numbers";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int round = rs.getInt("round");
					// 날짜를 추가합니다.
					String date = rs.getString("date");
					String numbers = rs.getString("numbers");
					int bonusNumber = rs.getInt("bonus_number");

					LottoNumberDTO lottoNumber = new LottoNumberDTO(round, date, numbers, bonusNumber, null); // winnings는
																												// null로
																												// 설정
					lottoNumbers.add(lottoNumber);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lottoNumbers;
	}
}
