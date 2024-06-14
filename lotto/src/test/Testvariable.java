package test;

import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

public class Testvariable extends JFrame {

	// 1~45버튼 배열
	public String[] btn_Title = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
			"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45" };

	JToggleButton[] jbtn = new JToggleButton[btn_Title.length];

	public JPanel contentPane;

	List<Integer> selectNum = new ArrayList<>();

	public JButton ok;
	public JButton resetButton;
	public JButton autoButton;
	public JButton viewResults;
	public JButton allReset;
	public JButton allClose;
	public JButton watchDrawButton;
	public JButton purchaseButton;

	public int selectCount;

	public boolean clickAuto;

	public JLabel resultPrice;
	public String price;
	public int count = 0;

	public JButton[] btnChange = new JButton[5];

	public JButton[] btnDelete = new JButton[5];

	public JLabel[][] showBall = new JLabel[5][6];

	public JLabel[] showBallTypes = new JLabel[5];

	public Integer[][] number = new Integer[5][6];

	public int size;

	public Map<Integer, String> ballType = new HashMap<>();

	public boolean[] clickToggle = new boolean[45];

	public JButton rank;

	public JComboBox<String> roundComboBox;
	public JPanel resultPanel = new JPanel();

	// 점수 맵
	public Map<Integer, Integer[][]> score = new HashMap<>();

	public JLabel timerLabel; // 타이머 레이블
	public JLabel prizeLabel; // 상금 레이블
	public Timer timer;

	public Map<Integer, Integer[][]> getScore() {
		return score;
	}

	public void setScore(Map<Integer, Integer[][]> score) {
		this.score = score;
	}

	public JLabel[] getShowBallTypes() {
		return showBallTypes;
	}

	public void setShowBallTypes(JLabel[] showBallTypes) {
		this.showBallTypes = showBallTypes;
	}

	public JLabel[][] getShowBall() {
		return showBall;
	}

	public void setShowBall(JLabel[][] showBall) {
		this.showBall = showBall;
	}

}
