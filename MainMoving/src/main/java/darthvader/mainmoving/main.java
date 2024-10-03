package darthvader.mainmoving;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ddf.EscherColorRef.SysIndexProcedure;
import org.apache.poi.hssf.usermodel.HSSFRangeCopier;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import DataStructures.ManageFolder;
import DataStructures.NameInfo;
import DataStructures.ManageFolder.FileOperation;
import DataStructures.ManageFolder.ManageFile;
import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import Interface.FIleExplorerSetup;
import Interface.FileExplorer;
import Interface.SideFilesList;
import OtherUtilities.ImageUtils;
import SwingUtilities.FocusPaneView;
import SwingUtilities.JLabelTextFill;
import SwingUtilities.SwingUtils;

public class main {
	
	public static void main(String[] args) throws Exception {
		
		String str = "C:\\Users\\itay5\\OneDrive\\Pictures\\Main";
		
		
		
		
		
		//str = "F:\\";
		//new moveFilesNewUI(str);
		
		//new moveFilesUI();
		
		//new moveFilesUISelect();
		
		
        final JFrame frame2 = new JFrame();
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLayout(new GridLayout(0, 1));
        FileDialog fd = new FileDialog(frame2, "Test", FileDialog.LOAD);
        fd.setDirectory("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024");
        fd.setVisible(true);
		
		
		
		/*NameInfo nameInfo = new NameInfo(
				/*"Superman.The.Animated.Series.S01E01.The.Last.Son.of.Krypton.Part.1.1080p.BluRay.REMUX.AVC.DTS-HD.MA.2.0-NOGRP"*/
				/*"Justice League (2001) - 1x01 - Secret Origins (1).eng.srt", false);
		System.out.println(new FileInfo(new File("E:\\Input\\Input\\Justice League (2001) - 1x14 - The Brave and the Bold (1).eng.srt")).getFullName());
		System.out.println(nameInfo.getMapName());
		System.out.println("Dorkrkrkrkkr");
		System.out.println(nameInfo.getFullName());
		NameInfo name2 = new NameInfo("Superman - The Animated Series (1996) - S01E01 - The Last Son of Krypton (1) (1080p BluRay x265 Ghost)", false);
		System.out.println(name2.getMapName());
		
		NameInfo name3 = new NameInfo("Superman - The Animated Series (1996) - S01 Episode 01 - The Last Son of Krypton (1) (1080p BluRay x265 Ghost)", false);
		System.out.println(name3.getFullName());
		*/
		
		/*NameInfo nameInfo = new NameInfo(
				//"Batman The Animated Series (1992) Season 1-4 S01-S04 + Extras (1080p BluRay x265 HEVC 10bit AAC 2.0 RCVR)"
				"Justice League (2001) Season 4 S04 (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK"
				, false);
		System.out.println(nameInfo.getFullName());
		System.out.println(nameInfo.getFolderTypeByInfo());*/
		
		/*printName("Justice League 2001 Season 4 S04 (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		
		printName("Justice League 2001 S04e04 name (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		
		printName("Justice League 2001 se04 name (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		
		printName("Justice League 2001 s name BluRay REPACK");
		
		printName("Justice League 2001 Season 4 episode 4 name (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		
		printName("Justice League 2001 episode 4 name (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		
		printName("Justice League (2001) (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		
		printName("Justice League 2001 (1080p BluRay x265 HEVC 10bit AAC 2.0 YOGI) REPACK");
		*/
		
		/*ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		moveFiles.moveFilesFromInput();*/
		
		//printName("Superman - The Animated Series -Extras");
		
		//printName("Superman - The Animated Series (1996) -Extras");
		
		//printName("Superman");
		
		//printName("Justice League Dark Apokolips War (2020) + Extras (1080p BluRay x265 HEVC 10bit DTS 5.1 SAMPA)");
		
		/*JFrame frame = new JFrame();
		frame.setContentPane(new moveFilesNewUI(moveFiles));;
		frame.setVisible(true);*/
		
		/*ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		
		System.out.println(moveFiles.getMainFolder(new FileInfo(new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main\\Input\\Bat Mans.mkv")),true));
		
		moveFiles.moveFilesFromInput();*/
		
		//use this for superman
		
		
		new FIleExplorerSetup();
		
		
		File file = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		
		/*ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		JFrame frame = new JFrame();
		frame.setContentPane(new FileExplorer(moveFiles));
		frame.setSize(400, 400);
		frame.setVisible(true);*/
		
		
		JFrame frame = new JFrame();
		frame.setSize(400, 401);
		//frame.setPreferredSize(new Dimension(400, 401));
		JPanel pnl = new JPanel(new BorderLayout());
		pnl.add(new SideFilesList(file), BorderLayout.CENTER);
		frame.setContentPane(pnl);
		//frame.setSize(400, 401);
		frame.setVisible(true);
		frame.setSize(frame.getSize());
		//frame.pack();
		//frame.revalidate();
		//frame.repaint();
		//frame.setSize(400, 400);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2");
		moveFiles.moveFilesFromInput();*/
		
		
		
		/*ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		JFrame frame = new JFrame();
		frame.setContentPane(new FileExplorer(moveFiles));
		frame.setVisible(true);*/
		
		
		
		
		
		/*ManageFolder moveFiles = new ManageFolder("D:\\");
		moveFiles.moveFilesFromInput();*/
		
       /* LookAndFeel old = UIManager.getLookAndFeel();
        try {
        	JFrame frame  = new JFrame();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			  Boolean olde = UIManager.getBoolean("FileChooser.readOnly");  
			 // UIManager.put("FileChooser.readOnly", Boolean.TRUE);  
			  
			  
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFileChooser files = new JFileChooser();
			
		    UIManager.put("FileChooser.readOnly", olde);  
		    frame.setLayout(new BorderLayout());
		    frame.add(files);
		    frame.setVisible(true);
        }
        catch (Throwable ex) {
            old = null;
        }*/
        
        
        
		/*FileChooser fileChooser = new FileChooser();
		fileChoose.showOpenDialog(null);*/
		
		
		
		/*frame = new JFrame();
		//frame.setSize(new Dimension(50, 40));
		//frame.setMinimumSize(new Dimension(50, 40));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		FocusPaneView view = new FocusPaneView(scroll) {
			
			/*@Override
			public Dimension getMaximumSize() {
				Dimension size = getPreferredSize();
				//Dimension size = FocusPaneView.getVerticalRatioDimension(this, 0.15);
				if(size != null) 
					return new Dimension(size.width, super.getMaximumSize().height);
				return super.getMaximumSize();
			}*/
		
		/*
		};
		//view.setLayout(new BoxLayout(view, BoxLayout.PAGE_AXIS));
		/*view.setLayout(new BorderLayout());
		view.add(new SideFilesList.SideFilePanel(new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main")));
		scroll.setViewportView(view);
		panel.add(scroll  
				/*new SideFilesList.ExpandPanel("J")*/   /*,
				BorderLayout.CENTER);
		panel.setSize(new Dimension(300, 300));
		panel.setPreferredSize(panel.getSize());
		*/
		
		/*frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //frame.setLayout(new BorderLayout());
	    //frame.add(new ExpandPanel("<"), BorderLayout.CENTER);
		frame.pack();
	    frame.setVisible(true);
	    
	    frame.revalidate();
	    frame.repaint();
	    */
	}
	
	/*private static class SC extends JScrollPane {
		
		@Override
		public void setViewportView(Component comp) {
			super.setViewportView(comp);
			if(comp instanceof FocusPaneView)
				((FocusPaneView) comp).set
		}
	}*/
	
	static JFrame frame;
	
	public static Font getMaximumFont(Graphics g, String text, Font font, Dimension size) {
		int stringWidth = g.getFontMetrics(font).stringWidth(text);
		int height = size.height, width = size.width;
		double widthRatio = (double)width / (double)stringWidth;
		int newSize = (int)(font.getSize() * widthRatio);
		float fontSizeToUse = Math.min(newSize, Math.min(height, width));
		Font newFont = font.deriveFont(fontSizeToUse);
		int newWidth = g.getFontMetrics(newFont).stringWidth(text);
		
		{
			int newHeight = g.getFontMetrics(newFont).getHeight();
			int radius = (int) Math.sqrt((newWidth*newWidth) + (newHeight*newHeight));
			if(radius < fontSizeToUse)
				newFont = font.deriveFont(radius);
		}
		
		
		if(newWidth > width)
			newFont = newFont.deriveFont(fontSizeToUse-1);
		return newFont;
	}
	
	static class ExpandPanel extends JLabel {
		
		private Font prevFont = getFont();
		int angle=0;
		private int removeFromFont;
		private Dimension prevSize = new Dimension(0,0);
		private boolean expanded;
		
		public ExpandPanel(String text) {
			super(text);
			setSize(50, 40);
			setHorizontalAlignment(CENTER);
			setVerticalTextPosition(TOP);
			//setBorder(BorderFactory.createLineBorder(Color.GREEN));
			//setFont(getFont().deriveFont(80f));
			addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					expanded = !expanded;
					System.out.println("Clicked");
					frame.revalidate();
					frame.repaint();
				}
			});
            Timer timer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    angle += 5;
                    repaint();
                }
            });
            timer.start();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g.create();
			if(!getSize().equals(prevSize)) {
				prevSize = getSize();
				prevFont = getMaximumFont(g2d, getText(), prevFont, prevSize);
						//SwingUtils.getMaximumFont(this, getText());
				int size = prevFont.getSize();
				/*if(expanded)
					size += 8;*/
				prevFont = prevFont.deriveFont((float) size - removeFromFont);
				setFont(prevFont);
				
				
			}
			
			/*{
				int x = 0;
				int y = 0;
				int horizontal = getHorizontalTextPosition();
				int vertical = getVerticalTextPosition();
				ComponentOrientation direction = this.getComponentOrientation();
				if(horizontal == LEFT || (horizontal == LEADING && direction.isHorizontal())
						|| (horizontal == TRAILING && !direction.isHorizontal())) {
					x = getX();
				}
				else if(horizontal == RIGHT || (horizontal == TRAILING && direction.isHorizontal())
					|| (horizontal == LEADING && !direction.isHorizontal())) {
					x = getX() + getWidth();
				}
				else if(horizontal == CENTER)
					x = getX() + (getWidth() / 2);
				if(vertical == TOP)
					y = getY();
				else if(vertical == CENTER)
					y = getY() + (getHeight() / 2);
				else if(vertical == BOTTOM)
					y = getY() + getHeight();
				System.out.println(x + "  gghgu " + y);
				System.out.println(getBounds());
				System.out.println(horizontal + "  " + vertical);
				g2d.drawOval(x-10, y-10, 50, 50);
			}*/
			
			{
				FontMetrics met = g2d.getFontMetrics();
				int height = met.getHeight() - met.getDescent() - met.getLeading();
				int width = met.stringWidth(getText());
				//met.getLe
				System.out.println(width + "  hhhjh " + height);
				g2d.drawRect(0, 0, width, height);
			}
			
			Shape shape = SwingUtils.getStringShape(this, this.getText());
			//g2d.drawRect(shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
			
			Point point = SwingUtils.getStringCenter(this, this.getText(), g2d.getFont());
			//g2d.drawOval(point.x, point.y, 5, 5);
			
			System.out.println(g.getFont());
			
	        Rectangle viewR = new Rectangle();
	        viewR.width = getSize().width;
	        viewR.height = getSize().height;
	        Rectangle iconR = new Rectangle();
	        Rectangle textR = new Rectangle();

	        String clippedText = SwingUtilities.layoutCompoundLabel
	        (
	            this,
	            g2d.getFontMetrics(),
	            getText(),
	            getIcon(),
	            getVerticalAlignment(),
	            getHorizontalAlignment(),
	            getVerticalTextPosition(),
	            getHorizontalTextPosition(),
	            viewR,
	            iconR,
	            textR,
	            getIconTextGap()
	        );
	        
			shape = textR;
	        Point center = ImageUtils.getRectCenter(shape.getBounds());
	        System.out.println(center + "  " + shape);
	        g2d.setColor(Color.RED);
	        g2d.drawOval(center.x, center.y, 5, 5);
	        
	        /*{
	        	
	        }*/
	        
	        
	        
			//g2d.drawRect(shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
			g2d.draw(shape);
	        g2d.setColor(Color.BLACK);
			AffineTransform saveAT = g2d.getTransform();
			//if(expanded) {
		        int w2 = center.x;//shape.getBounds().width / 2;
		        int h2 = center.y;//shape.getBounds().height / 2;
		        //g2d.rotate(-Math.PI / 2, w2, h2);
				
		        AffineTransform at = new AffineTransform();
		        
		        at.concatenate(saveAT);
		        
	            int x = (center.x - getX()) / 2;
	            int y = (getHeight() - center.y /*- shape.getBounds().y - shape.getBounds().height*/) / 2;
	            //at.translate(x, y);
		        at.rotate(Math.toRadians(angle/*90*/), center.x, center.y);
		        //at.rotate(Math.toRadians(angle), shape.getBounds().width / 2, shape.getBounds().height / 2);
		        //at.setToQuadrantRotation(1, center.x, center.y);//shape.getBounds().width / 2, shape.getBounds().height / 2);
		        //at.translate(-3, -1);
		        
		        //at.concatenate(saveAT);
		        
		        g2d.setTransform(at);
		        
		        System.out.println("Sorry");
		        //g2d.setFont(getFont().deriveFont(getFont().getSize()+100f));
		        Dimension size = getSize();
		        Font font = getFont();
		        /*String text = getText();
		        {
		    		int stringWidth = g.getFontMetrics(font).stringWidth(text);
		    		int height = size.height, width = size.width;
		    		double widthRatio = (double)width / (double)stringWidth;
		    		int newSize = (int)(font.getSize() * widthRatio);
		    		float fontSizeToUse = Math.min(newSize, height);
		    		Font newFont = font.deriveFont(fontSizeToUse);
		    		int newWidth = g.getFontMetrics(newFont).stringWidth(text);
		    		//if(newWidth > width)
		    			newFont = newFont.deriveFont(fontSizeToUse-1);
		    			g2d.setFont(font.deriveFont(stringWidth));
		        }*/
		        g2d.draw(shape);
		        
		        //g2d.translate(-w2, h2);
				//g2d.transform(AffineTransform.getQuadrantRotateInstance(-1));
				
				/*g2d.rotate*/
		        
		        //g2d.rotate(Math.toRadians(-30));
			/*}
			else {
				
			}*/
			System.out.println("Start");
			super.paintComponent(g2d);
			g2d.setTransform(saveAT);
			System.out.println("Miss");
			System.out.println(g2d.getFont());
			
			
			
			
			/*{
		        viewR = new Rectangle();
		        viewR.width = getSize().width;
		        viewR.height = getSize().height;
		        iconR = new Rectangle();
		        textR = new Rectangle();

		        clippedText = SwingUtilities.layoutCompoundLabel
		        (
		            this,
		            g2d.getFontMetrics(),
		            getText(),
		            getIcon(),
		            getVerticalAlignment(),
		            getHorizontalAlignment(),
		            getVerticalTextPosition(),
		            getHorizontalTextPosition(),
		            viewR,
		            iconR,
		            textR,
		            getIconTextGap()
		        );
		        g2d.setColor(Color.GREEN);
		        g2d.draw(textR);
			}*/
			
			
			
			
			
			g2d.dispose();
			//g2d.drawString(">", getWidth(), getHeight());
		}
	}
	
	public static void printName(String str) {
		NameInfo nameInfo = new NameInfo(
				//"Batman The Animated Series (1992) Season 1-4 S01-S04 + Extras (1080p BluRay x265 HEVC 10bit AAC 2.0 RCVR)"
				str
				);
		System.out.println(nameInfo.getFolderTypeByInfo() + " : " + nameInfo.getFullName() + "    ---- " + nameInfo.getYear());
	}
	
	public static void main457(String[] args) throws Exception {
		
		//new moveFilesNewUI("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		
		/*FileInfo info = new FileInfo("Hi 2018 s1e01", false);
		System.out.println(info.getFullName());*/
		
		
		
		
		ManageFolder moveFiles = new ManageFolder("C:\\Users\\itay5\\OneDrive\\Pictures\\Main");
		String input = "";
		//input = "C:\\Users\\Lenovo\\Desktop\\Main\\Input\\Gladiator (2000) - Poster.jpg";
		//input = "C:\\Users\\Lenovo\\Desktop\\Main\\Input\\Gladiator (2000).mkv";
		//input = "C:\\Users\\Lenovo\\Desktop\\Main\\Input\\Smallville.S09E02.1080p.BluRay.Remux.eng.srt";
		
		
		//input = "C:\\Users\\Lenovo\\Desktop\\Main\\Input\\Smallville.2001.S09E01.1080p.BluRay.Remux.eng.srt";
		
		
		
		
		moveFiles.moveFilesFromInput();
		
		
		
		
		input = "C:\\Users\\Lenovo\\Desktop\\Main\\Input\\Smallville.S09E02.1080p.BluRay.Remux.eng.srt";
		System.out.println(input);
		//ManageFile move = moveFiles.getMove(input, FileOperation.MOVE);
		
		System.out.println("OKKKKKKKKKKKKKKKKKKKKKKK");
		/*
		System.out.println(move);
		move.printFileMoves();
		*/
		
		
		
		
		
		
		
		
		
		/*moveFiles.urlParent = "C:\\Users\\Lenovo\\Desktop\\Main";
		moveFiles.setMap("W-Output");
		moveFiles.moveFiles("input");*/
		
		
		/*int x = 5;
		int[][] arr = new int[x][x];
		
		for(int i = 0; i < arr.length; i++) {
			arr[0][i] = 1;
			arr[i][i] = 1;
		}
		
		for(int i = 1; i < arr.length; i++) {
			for(int j = 1; j < arr.length; j++) {
				if(i != j)
					arr[i][j] = arr[i-1][j-1] + (i+1)*arr[i][j-1];
			}
		}
		
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr.length; j++) {
				System.out.print(arr[i][j] + " , ");
			}
			System.out.println();
		}
		
		System.out.println();
		int sum = 0;
		for(int i = 0; i < arr.length; i++)
			sum += arr[i][arr.length-1];
		
		System.out.println(sum);*/
		
		
		
		
		
	
		/*FileInfo info = new FileInfo("Smallville.S09E19.1080p.BluRay.Remux.eng.srt", false);
		System.out.println(info.getFullName());*/
	}
	
	public static void mainMy(String[] args) {
		//moveFiles.urlParent = "D:\\sort\\";
		//moveFiles.urlParent = "C:\\Users\\itay5\\OneDrive\\Desktop\\Main";
		
		//moveFiles.urlParent = "C:\\Users\\itay5\\OneDrive\\Desktop\\Main";
		
		//oragnizeRARBG("D:\\The.X-Files.S09.1080p.BluRay.x265-RARBG");
		
		/*String mainPath = "C:\\Users\\itay5\\OneDrive\\Desktop\\Main";
		String input = mainPath + "\\input\\The.X-Files.S11.1080p.BluRay.x265-RARBG";
		String output = mainPath + "\\input";
		moveFiles.oragnizeRARBG(input, output);*/
		
		//moveFiles.oragnizeRARBG(moveFiles.urlParent + "\\input\\The.X-Files.S11.1080p.BluRay.x265-RARBG", moveFiles.urlParent + "\\input");
		
		//moveFiles.urlParent = "D:\\sort\\";
		/*try {
		System.out.println(Files.probeContentType(Paths.get("C:\\Users\\itay5\\OneDrive\\Desktop\\Main\\W-Output\\TV\\Daisy Jones and the Six\\Daisy Jones and the Six Season 01\\Daisy Jones and the Six - S01E08\\Daisy Jones and the Six - S01E08.mkv")));
		}
		catch(Exception exp) {}*/
		
		
		String name = "Mail Man - Poster";
		int n = name.lastIndexOf("- Poster");
		if(n != -1 && (n + "- Poster".length()) == name.length())
			System.out.println(name.substring(0, n));
		
		
		String gun = "The LONE Gunmen - s01Ep01 - - - - -  Pilot - - - -  (576p - DVDRip)";
		
		String[] array = gun.split(" ");
		System.out.println(Arrays.asList(array));
		
		
		/*try {
			moveFiles.moveExtras(null, "", FolderType.EXTRAS);
		}
		catch(Exception exp ) {
			exp.printStackTrace();
		}*/
		
		/* x files */
		/*moveFiles.setMap("W-Output");
		moveFiles.moveFiles("input");*/
		/*FileInfo info = new FileInfo("The LONE Gunmen - s01 Ep01 - - - - -  Pilot - - - -  (576p - DVDRip)", false);
		
		System.out.println(info.getNameSeason()+"  " + info.getNameSeasonEpisode()  + " "+ info.getFullName());
		
		System.out.println(new FileInfo("John Wick - Chapter 4 (2023) - Poster", false).getFullName());
		
		System.out.println("Woman");
		System.out.println(new FileInfo("2 Season 2 Furious", false).getFullName());
		System.out.println("Flash");
		
		
		//Map<Object, Object> map = JSONUtils.loadMap("./formats.json");
		
		/*Map<String, Set<String>> map = FileFormats.FORMATS_MAP;
		Object object = map.get("Subtitle");
		if(object instanceof Collection) {
			//String[] arr = {"video/x-matroska", "video/mp4", "video/x-msvideo"};
			//String[] arr = {"application/x-subrip", "text/vnd.dvb.subtitle"};
			//String[] arr = {"image/png", "image/jpeg"};
			//String[] arr = {"image/x-icon"};
			//((Collection) object).addAll(Arrays.asList(arr));
			System.out.println(((Collection) object));
			System.out.println(((Collection) object).contains("video/mp4"));
			System.out.println("Famesss    ");
		}
		System.out.println(map);
		
		System.out.println( JSONValue.toJSONString(map));
		
		
		/*Set<String> set = new HashSet<>();
		map.put("Video", set);
		set = new HashSet<>();
		map.put("Subtitle", set);
		set = new HashSet<>();
		map.put("Poster", set);
		set = new HashSet<>();
		map.put("Logo", set);*/
		
		//JSONUtils.saveJSON(map, "./formats.json");
		
		
		
		/*String str = "sas0.";
		if(str.startsWith("(") && str.endsWith(")"))
			str = str.substring(1, str.length()-1);
		String regex = "^[\\w]+";//String.format("\\d{1,%d}", MAX_SEASON_LENGTH);
		//String regex = String.format("\\d{1,%d}", MAX_YEAR_LENGTH, MAX_YEAR_LENGTH);
		//return str.matches(regex);
		System.out.println(str.matches(regex));
		//System.out.println(isSeason(str));
		/*String match = String.format("s\\d{1,%d}", 4);
		//match.
		String matchEp = String.format("e\\d{1,%d}", 4);
		System.out.println(str.matches(match + matchEp));*/
		
		System.out.println("-".equals("-"));
		
		String sub = "The.X-Files.1993.S08E21.1080p.BluRay.x265-RARBG";
		sub = "03. Smallville.2001.S09E04.Superman.Lives.1080p.srt";
		sub = "Smallville (2001) - S01E02.srt";
		//sub = "Love 2022 2001 - S01"; //"- A Space Odyssey";
		//sub = "2001 - a pss 1968";
		/*info = new FileInfo(sub, false);
		System.out.println(info.getName());
		System.out.println(info.getNameSeason());
		System.out.println(info.getFullName());
		System.out.println(info.getFullName());
		System.out.println(info.getSeason());
		String ext2 = FilenameUtils.getExtension("bar.exenstr");
		System.out.println(ext2);
		System.out.println(info.getMapName());
		
		
		
		
		/*String type = new Tika().detect(sub);
		System.out.println(type);
		try {
		 System.out.println(Files.probeContentType(new File(sub).toPath()));
		 
		 
			TikaConfig tika = new TikaConfig();

			Metadata metadata = new Metadata();
			metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, sub);
			String mimetype = tika.getDetector().detect(new FileInputStream(new File(sub)), metadata).getSubtype();
			System.out.println(mimetype + " nder ");
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		//System.out.println(("mime.json"));
		
		System.out.println(Action.getMimeType(sub));*/
		
	}
	
	
	private static final int MAX_YEAR_LENGTH = 4, MAX_SEASON_LENGTH = 4, MAX_EPISODE_LENGTH = 4;
	
	private static boolean isSeason(String str) {
		str = str.toLowerCase();
		String regex = String.format("s\\d{1,%d}", MAX_SEASON_LENGTH);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return str.equals("season") || (matcher.find() &&(matcher.hitEnd() || isEpisode(str.substring(matcher.end()))));
	}
	
	private static boolean isEpisode(String str) {
		str = str.toLowerCase();
		String regex = String.format("e\\d{1,%d}", MAX_EPISODE_LENGTH);
		return str.equals("episode") || str.matches(regex);
	}
	
	/*public static void organizeRARBG(String fileName, String mainPath) {
		String outputPath = Action.checkPath(mainPath) + "input";
	}*/
	
	public static void oragnizeRARBG(String filePath, String outputPath) {
		try {
			File mainFile = new File(filePath);
			if(!mainFile.exists())
				return;
			new File(outputPath).mkdir();
			for(File file : mainFile.listFiles()) {
				try {
					String fullName = file.getName();
					if(fullName.equals("Subs")) {
						for(File subtitlesFile : file.listFiles()) {
							String name = subtitlesFile.getName();
							File[] files = subtitlesFile.listFiles();
							Arrays.sort(files, new Comparator<File>() {
								@Override
								public int compare(File o1, File o2) {
									return o1.getName().compareTo(o2.getName());
								}
							});
							if(file.length() != 0) {
								File firstFile = files[0];
								String firstName = firstFile.getName();
								firstName = firstName.substring(firstName.lastIndexOf('.'));
								String newName = name + firstName;
								Files.move(Paths.get(firstFile.getAbsolutePath()), Paths.get(FilesUtils.checkPath(outputPath)+newName));
							}
						}
					}
					else {
						/*String type = Mime.getMimeContentType(file.isDirectory() ? "" : file.getAbsolutePath());
						if(type.startsWith("video/"))
							Files.move(Paths.get(file.getAbsolutePath()), Paths.get(Action.checkPath(outputPath) + file.getName()));*/
					}
				}
				catch(Exception exp) {exp.printStackTrace();}
			}
		}
		catch(Exception exp) {exp.printStackTrace();}
	}
}
