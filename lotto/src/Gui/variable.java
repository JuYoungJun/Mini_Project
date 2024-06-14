package Gui;

import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

// GUI 요소들을 담는 변수 클래스
public class variable {

	// 1~45까지의 번호를 나타내는 버튼 배열
	public String[] btn_Title = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
			"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45" };

	// 1~45까지의 번호를 토글 버튼으로 표현하는 배열
	JToggleButton[] jbtn = new JToggleButton[btn_Title.length];

	public JPanel contentPane; // 프레임의 콘텐츠 패널

	List<Integer> selectNum = new ArrayList<>(); // 사용자가 선택한 번호 리스트

	// GUI 요소들
	public JButton ok;
	public JButton resetButton;
	public JButton autoButton;
	public JButton viewResults;
	public JButton allReset;
	public JButton allClose;
	public JButton watchDrawButton;
	public JButton purchaseButton;

	public int selectCount; // 사용자가 선택한 번호의 개수

	public boolean clickAuto; // 자동 선택 버튼을 클릭했는지 여부

	public JLabel resultPrice; // 결과 표시를 위한 가격 레이블
	public String price; // 가격 정보 문자열
	public int count = 0; // 카운트 변수

	public JButton[] btnChange = new JButton[5]; // 번호 변경을 위한 버튼 배열

	public JButton[] btnDelete = new JButton[5]; // 번호 삭제를 위한 버튼 배열

	public JLabel[][] showBall = new JLabel[5][6]; // 선택한 번호를 보여주는 레이블 배열

	public JLabel[] showBallTypes = new JLabel[5]; // 각 번호의 유형을 나타내는 레이블 배열

	public Integer[][] number = new Integer[5][6]; // 선택한 번호 배열

	public int size; // 크기 변수

	public Map<Integer, String> ballType = new HashMap<>(); // 번호와 해당하는 유형을 매핑하는 맵

	public boolean[] clickToggle = new boolean[45]; // 토글 상태를 나타내는 배열

	public JButton rank; // 등수 버튼

	public JComboBox<String> roundComboBox = new JComboBox<>(); // 회차 선택 콤보 박스
	public JPanel resultPanel = new JPanel(); // 결과 패널

	// 점수를 나타내는 맵
	public Map<Integer, Integer[][]> score = new HashMap<>();

	public JLabel timerLabel; // 타이머 레이블
	public JLabel prizeLabel = new JLabel(); // 상금 레이블
	public Timer timer; // 타이머 객체

	// 점수 맵 getter
	public Map<Integer, Integer[][]> getScore() {
		return score;
	}

	// 점수 맵 setter
	public void setScore(Map<Integer, Integer[][]> score) {
		this.score = score;
	}

	// 각 선택 번호의 유형 레이블 getter
	public JLabel[] getShowBallTypes() {
		return showBallTypes;
	}

	// 각 선택 번호의 유형 레이블 setter
	public void setShowBallTypes(JLabel[] showBallTypes) {
		this.showBallTypes = showBallTypes;
	}

	// 선택한 번호를 보여주는 레이블 getter
	public JLabel[][] getShowBall() {
		return showBall;
	}

	// 선택한 번호를 보여주는 레이블 setter
	public void setShowBall(JLabel[][] showBall) {
		this.showBall = showBall;
	}

	// 생성자
	public variable() {
		roundComboBox = new JComboBox<>(); // 회차 선택 콤보 상자 초기화
	}
}