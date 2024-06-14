package Parsing;

import java.io.*;
import java.net.*;
import java.sql.*;
import org.json.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import lottonumber.LottoNumberDTO;
import lottonumber.LottoNumberDAO;

public class ParsingMain {
	public static void main(String[] args) {
		// DAO 인스턴스 생성
		LottoNumberDAO dao = new LottoNumberDAO();

		try {
			int latestRound = getLatestRound(); // 최신 회차 번호 가져오기

			// 1회차부터 최신 회차까지 반복
			for (int round = 1; round <= latestRound; round++) {
				String url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + round;

				// HTTP GET 요청 보내기
				HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
				con.setRequestMethod("GET");

				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder response = new StringBuilder();
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

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
					String drawDate = json.getString("drwNoDate");

					long[] winnings = WinningAmountParser.getWinnings(round);
					LottoNumberDTO dto = new LottoNumberDTO(round, numbers, drawDate, bonusNumber, winnings);

					// 디버그 메시지 출력
//					System.out.println("회차 " + round + "의 당첨 정보:");
//					for (int i = 0; i < winnings.length; i++) {
//						System.out.println((i + 1) + "등 당첨금: " + winnings[i]);
//					}

					// DTO 확인용 출력
//					System.out.println("회차: " + dto.getRound());
//					System.out.println("당첨번호: " + dto.getNumbers());
//					System.out.println("보너스 번호: " + dto.getBonusNumber());
//					System.out.println("당첨금 정보:");
//					System.out.println("1등: " + dto.getWinnings()[0]);
//					System.out.println("2등: " + dto.getWinnings()[1]);
//					System.out.println("3등: " + dto.getWinnings()[2]);
//					System.out.println("4등: " + dto.getWinnings()[3]);
//					System.out.println("5등: " + dto.getWinnings()[4]);

					// 데이터베이스에 저장
					dao.saveLottoNumber(dto);
				} else {
					System.out.println("HTTP GET 요청 실패: " + responseCode);
				}
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	// 최신 회차 번호를 가져오는 메서드
	public static int getLatestRound() throws IOException {
		String url = "https://www.dhlottery.co.kr/gameResult.do?method=byWin";
		Document doc = Jsoup.connect(url).get();
		Element selectElement = doc.selectFirst("select#dwrNoList");
		String latestRoundOption = selectElement.child(0).text();
		return Integer.parseInt(latestRoundOption);
	}
}
