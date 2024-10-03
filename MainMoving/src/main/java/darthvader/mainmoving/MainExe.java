package darthvader.mainmoving;
import java.io.File;

import javax.swing.JFrame;

import DataStructures.ManageFolder;
import Interface.FIleExplorerSetup;
import Interface.FileExplorer;
import Interface.SideFilesList;

public class MainExe {

	public static void main(String[] args) {
		//new FIleExplorerSetup();
		File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		
		//ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		JFrame frame = new JFrame();
		//frame.setContentPane(new FileExplorer(moveFiles));
		frame.setContentPane(new SideFilesList(file));
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

}
