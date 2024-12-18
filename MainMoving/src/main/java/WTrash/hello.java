package WTrash;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class hello {
	
    public static void main(String[] args) {
    	nu.pattern.OpenCV.loadLocally();
    	String str = "C:\\Users\\itay5\\Downloads\\2022 Questions Bank\\2022 Questions Bank";
        String name = "C:\\Users\\itay5\\OneDrive\\Pictures\\2022-01-27_11-25-14 unknown.png"; 
    	try {
    		File folder = new File(str);
            PDDocument doc = new PDDocument();
            for(File file : folder.listFiles()) {
	            PDPage page = new PDPage();
	            doc.addPage(page);
	            PDImageXObject pdImage = JPEGFactory.createFromImage(doc, resizeImage(file.getAbsolutePath(), 0, 610, 785), 1);
	            //= PDImageXObject.createFromFile(file.getAbsolutePath(), doc);
	            PDPageContentStream contents = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true);
	            contents.drawImage(pdImage, 5, 5); 
	            System.out.println("Image inserted");  
	            contents.close();      	
            }
            doc.save("C:\\Users\\itay5\\Downloads\\test.pdf");
            doc.close();            
    	}
    	catch(Exception exp) {
    		exp.printStackTrace();
    	}
    	
    	
    	
    	//Loader.load(opencv_java.class);
    	//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	//String name = "C:\\Users\\itay5\\Downloads\\2022-01-27_11-25-14 unknown.png";
    	
    	/*
    	Tesseract tesseract = new Tesseract();
		Mat image = null;
		BufferedImage buff = null;
        try {
        	BufferedImage read = ImageIO.read(new File(name));
        	tesseract.setDatapath("C:\\Java\\OCR\\tessdata");
			image = Imgcodecs.imread(name, Imgcodecs.IMREAD_COLOR);
			Mat mask = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
			Mat thresh = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
			List<MatOfPoint> contours = new ArrayList<>();
			
			Imgproc.cvtColor(image, mask, Imgproc.COLOR_BGR2GRAY);
			
			//Imgproc.threshold(image, thresh, 0, 255, Imgproc.THRESH_BINARY_INV);
			
			Imgproc.GaussianBlur(thresh, mask, new Size(3, 3), 0);
			Imgproc.Canny(mask, mask, 120, 255, 3);
			Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9,9));
			Imgproc.dilate(mask, mask, kernel);
		    Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL,
				      Imgproc.CHAIN_APPROX_SIMPLE);
		    for(int i = 0; i < contours.size(); i++) {
		    	//System.out.println(""+i);
		    	double d = Imgproc.contourArea(contours.get(i));
		    	Rect rect = Imgproc.boundingRect(contours.get(i));
		    	//Imgproc.rectangle(image, rect, new Scalar(36, 255, 12));
		    	Mat crop = image.submat(rect);
				//HighGui.imshow("Image2", crop);
				//HighGui.waitKey();
			    int type = BufferedImage.TYPE_BYTE_GRAY;
			    if (crop.channels() > 1) {
			        type = BufferedImage.TYPE_3BYTE_BGR;
			    }		    	
			    buff = new BufferedImage(crop.cols(),crop.rows(), type);
			    crop.get(0,0,((DataBufferByte)buff.getRaster().getDataBuffer()).getData());
		    	String ocr = tesseract.doOCR(read, new Rectangle(rect.x, rect.y, rect.width, rect.height));
		    	System.out.println(ocr);
			    //Imgproc.drawContours(mask, contours, 0, new Scalar(255,255,0));
		    }
			//HighGui.imshow("Image2", thresh);
			//HighGui.waitKey();
		    
			Imgproc.GaussianBlur(mask, mask, new Size(3, 3), 0);
			//Imgproc.medianBlur(mask, mask, 3);
			Imgproc.threshold(mask, mask, 0, 255, Imgproc.THRESH_OTSU);
			Imgproc.adaptiveThreshold(mask, mask, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4);
			
			//HighGui.imshow("Image", mask);
			//HighGui.waitKey();
			
        	//tesseract.setDatapath("C:\\Java\\OCR\\tessdata");
        	//String ocr = tesseract.doOCR(mask.get);
        	//System.out.println(ocr);
        }
        catch(Exception exp) {
        	exp.printStackTrace();
        }
        */
    }
    
	public static Mat cropImageAndAddBorder(String path, Color color, int borderSize) {
		Mat image = null;
		try {
			image = Imgcodecs.imread(path, Imgcodecs.IMREAD_UNCHANGED);
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Mat mask = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
		    /*Imgproc.cvtColor(convertImageBlackToWhite(image), mask, Imgproc.COLOR_BGR2GRAY);
		    Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL,
				      Imgproc.CHAIN_APPROX_SIMPLE);
		    image = new Mat(image, Imgproc.boundingRect(contours.get(0)));
		    Core.copyMakeBorder(image, image, borderSize, borderSize, borderSize, borderSize, Core.BORDER_ISOLATED, new Scalar(color.getBlue(), color.getGreen(), color.getRed(), 255));*/
		}
		catch(Exception exp) {exp.printStackTrace();}
		return image;
	}
	
	public static BufferedImage resizeImage(String url, int type2, int artWidth, int artHeight) throws IOException {
		Mat m = Imgcodecs.imread(url);
		Imgproc.resize(m, m, new Size(artWidth, artHeight));
	    int type = BufferedImage.TYPE_BYTE_GRAY;
	    if ( m.channels() > 1) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    }
        
	    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	    m.get(0,0,((DataBufferByte)image.getRaster().getDataBuffer()).getData());
	    return image;
	}
	
	public static Image loadImage(String path) {
		try {
			File pic = new File(path+".png");
			if(pic.exists() != true) {
				pic = new File(path+".jpg");
				if(pic.exists() != true)
					return null;
			}
			return ImageIO.read(pic);
		}
		catch(Exception exp) {exp.printStackTrace();}
		return null;
	}
}
