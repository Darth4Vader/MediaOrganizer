package darthvader.mainmoving;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.input.UnsynchronizedByteArrayInputStream;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.slf4j.simple.SimpleServiceProvider;

import FileUtils.FileDetailsUtils;

public class TestFunction {

	public TestFunction() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws FileNotFoundException {
		File ico = new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024\\W-Output\\Media\\Star Wars Episode I - The Phantom Menace\\Star Wars Episode I - The Phantom Menace -Extras\\Star Wars Episode I - The Phantom Menace -Info\\Star Wars Episode I - The Phantom Menace - Logo.ico");
		
		
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(ico);
		ParseContext context = new ParseContext();
		ImageParser parser = new ImageParser();
		try {
			parser.parse(inputstream, new ExpandedTitleContentHandler(handler), metadata, context);
		} catch (Throwable e) {
			System.out.println("Da");
			e.printStackTrace();
		}
		System.out.println(context.keySet());
		System.out.println(metadata);
		System.out.println(handler);
		
		//System.out.println(FileDetailsUtils.loadMetadata(new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024\\W-Output\\Media\\Star Wars Episode I - The Phantom Menace\\Star Wars Episode I - The Phantom Menace -Extras\\Star Wars Episode I - The Phantom Menace -Info\\Star Wars Episode I - The Phantom Menace - Logo.ico")));
		
		System.out.println(UnsynchronizedByteArrayInputStream.builder());
		
		System.out.println(FileDetailsUtils.loadMetadata(new File("C:\\Users\\itay5\\OneDrive\\Pictures\\Main2024\\W-Output\\Media\\Star Wars Episode I - The Phantom Menace - Logo.ico")));
	}

}
