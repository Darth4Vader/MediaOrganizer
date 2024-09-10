import javax.swing.JFrame;

import DataStructures.ManageFolder;
import Interface.FileExplorer;

public class mainSwing {

	public static void main(String[] args) {
		ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		JFrame frame = new JFrame();
		frame.setContentPane(new FileExplorer(moveFiles));
		frame.setVisible(true);
	}

}
