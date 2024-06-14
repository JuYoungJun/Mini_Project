package Parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class WinningAmountParser {
	// 해당 회차의 등위별 총 당첨금을 가져와서 배열로 반환합니다.
	public static long[] getWinnings(int round) throws IOException {
		String url = "https://www.dhlottery.co.kr/gameResult.do?method=byWin&drwNo=" + round;
		Document doc = Jsoup.connect(url).get();

		// 등위별 총 당첨금 정보가 있는 특정 HTML 요소 식별
		Element winningsElement = doc.selectFirst("table.tbl_data.tbl_data_col");

		if (winningsElement == null) {
			return new long[5]; // 테이블이 없으면 기본값으로 배열 반환
		}

		// 등위별 총 당첨금을 저장할 배열
		long[] totalWinnings = new long[5];
		// 1게임당 당첨금액을 저장할 배열
		long[] winningsPerGame = new long[5];

		// 테이블에서 각 행을 반복하며 등위별 총 당첨금을 추출합니다.
		Elements rows = winningsElement.select("tbody tr");
		int index = 0;
		for (Element row : rows) {
			Element rankElement = row.selectFirst("td"); // 등위 정보를 포함하는 열 선택
			if (rankElement != null) {
				String rank = rankElement.text(); // 등위 텍스트 가져오기
				if (rank.endsWith("등")) {
					String totalWinningsStr = row.select("td.tar strong").first().text(); // 등위별 총 당첨금액 텍스트 가져오기
					totalWinnings[index] = parseAmountString(totalWinningsStr); // 등위별 총 당첨금액을 배열에 저장

					// 당첨게임 수 가져오기
					Element gameCountElement = row.select("td").get(2); // 등위별 총 당첨금액 다음에 있는 열 선택
					int gameCount = Integer.parseInt(gameCountElement.text().replaceAll("[^0-9]", ""));
					if (gameCount != 0) {
						winningsPerGame[index] = totalWinnings[index] / gameCount; // 1게임당 당첨금액 계산
					}

					index++;
				}
			}
		}

		return winningsPerGame; // 1게임당 당첨금액 배열 반환
	}

	// 당첨금 문자열을 숫자로 파싱하는 메서드
	private static long parseAmountString(String amountStr) {
		try {
			// 숫자와 쉼표로 구성된 문자열을 숫자로 변환합니다.
			String cleanAmountStr = amountStr.replaceAll("[^0-9]", "");
			return Long.parseLong(cleanAmountStr);
		} catch (NumberFormatException e) {
			// Parsing 실패 시 기본값으로 0을 반환합니다.
			return 0;
		}
	}
}
