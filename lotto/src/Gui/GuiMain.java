package Gui;

import java.awt.EventQueue;

public class GuiMain {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					variable var = new variable();
					Method method = new Method(var, null);
					DAO dao = new DAO(var, method);
					method.setDAO(dao);
					dao.fetchDataFromBatabase();
					UI frame = new UI(var, dao, method);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
}
