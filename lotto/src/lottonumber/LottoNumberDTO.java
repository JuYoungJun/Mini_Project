package lottonumber;

public class LottoNumberDTO {
	private int round;
	private String date;
	private String numbers;
	private int bonusNumber;
	private long[] winnings;

	public LottoNumberDTO(int round, String numbers, String date, int bonusNumber, long[] winnings) {
		this.round = round;
		this.date = date;
		this.numbers = numbers;
		this.bonusNumber = bonusNumber;
		this.winnings = winnings;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public int getBonusNumber() {
		return bonusNumber;
	}

	public void setBonusNumber(int bonusNumber) {
		this.bonusNumber = bonusNumber;
	}

	public long[] getWinnings() {
		return winnings;
	}

	public void setWinnings(long[] winnings) {
		this.winnings = winnings;
	}
}
