package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ParsingTest {
	public static void main(String[] args) {
		MySQLConnector connector = null;
		try {
			connector = new MySQLConnector();
			// 최신 회차 가져오기
			int latestRound = getLatestRound();

			// 1회차부터 최신 회차까지의 당첨번호 가져오기
			for (int round = 1; round <= latestRound; round++) {
				// 대한민국 로또 당첨번호 페이지 URL
				String url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + round;

				// URL 연결 설정
				HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
				con.setRequestMethod("GET");

				// 응답 코드 확인
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					// JSON 형식의 데이터 읽기
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuilder response = new StringBuilder();
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					// JSON 데이터 파싱
					JSONObject json = new JSONObject(response.toString());
					int drwtNo1 = json.getInt("drwtNo1");
					int drwtNo2 = json.getInt("drwtNo2");
					int drwtNo3 = json.getInt("drwtNo3");
					int drwtNo4 = json.getInt("drwtNo4");
					int drwtNo5 = json.getInt("drwtNo5");
					int drwtNo6 = json.getInt("drwtNo6");
					String numbers = drwtNo1 + ", " + drwtNo2 + ", " + drwtNo3 + ", " + drwtNo4 + ", " + drwtNo5 + ", "
							+ drwtNo6;
					int bonusNumber = json.getInt("bnusNo");

					// 당첨번호를 MySQL에 저장
					boolean saved = connector.saveLottoNumbers(round, numbers, bonusNumber);
					if (saved) {
						// 콘솔에 해당 회차 번호 출력
						System.out.println("회차 " + round + "의 당첨번호: " + numbers + ", 보너스 번호: " + bonusNumber);
					}
				} else {
					System.out.println("HTTP GET 요청 실패: " + responseCode);
				}
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (connector != null) {
				try {
					connector.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 최신 회차 가져오는 메소드
	public static int getLatestRound() throws IOException {
		// 대한민국 로또 당첨결과 페이지 URL
		String url = "https://www.dhlottery.co.kr/gameResult.do?method=byWin";

		// Jsoup을 사용하여 웹페이지에서 HTML 가져오기
		Document doc = Jsoup.connect(url).get();

		// 회차 선택 드롭다운 메뉴에서 최신 회차 가져오기
		Element selectElement = doc.selectFirst("select#dwrNoList");
		String latestRoundOption = selectElement.child(0).text(); // 최신 회차는 첫 번째 옵션에 있음
		int latestRound = Integer.parseInt(latestRoundOption);

		return latestRound;
	}
}

class MySQLConnector {
	// MySQL 연결 정보
	private static final String URL = "jdbc:mysql://localhost:3306/lotto_database";
	private static final String USER = "root";
	private static final String PASSWORD = "1234";

	// MySQL 연결 객체
	private Connection conn;

	// 생성자에서 MySQL에 연결
	public MySQLConnector() throws SQLException {
		conn = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	// 당첨번호를 MySQL에 저장하는 메소드
	public boolean saveLottoNumbers(int round, String numbers, int bonusNumber) throws SQLException {
		// 이미 존재하는 회차인지 확인
		boolean isDuplicate = isDuplicate(round);
		if (isDuplicate) {
			System.out.println("회차 " + round + "의 당첨번호는 이미 데이터베이스에 존재합니다.");
			System.out.println("회차 " + round + "의 당첨번호: " + numbers + ", 보너스 번호: " + bonusNumber);
		} else {
			// 중복되지 않는 경우 데이터베이스에 저장
			String sql = "insert into lotto_numbers (round, numbers, bonus_number) values (?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, round);
				pstmt.setString(2, numbers);
				pstmt.setInt(3, bonusNumber);
				pstmt.executeUpdate();
			}
		}
		return !isDuplicate; // 중복 여부 반환
	}

	// 해당 회차가 이미 데이터베이스에 존재하는지 확인하는 메소드
	private boolean isDuplicate(int round) throws SQLException {
		String sql = "select round from lotto_numbers WHERE round = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, round);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}

	// MySQL 연결 종료 메소드
	public void close() throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}
}
