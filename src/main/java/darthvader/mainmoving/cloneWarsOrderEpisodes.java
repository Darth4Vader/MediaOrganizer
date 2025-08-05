package darthvader.mainmoving;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import DataStructures.FileInfo;
import DataStructures.FileInfoType;
import DataStructures.FileInfoType.FolderType;
import DataStructures.NameInfo.NameInfoType;
import DataStructures.FolderInfo;
import DataStructures.ManageFolder;
import DataStructures.NameInfo;

public class cloneWarsOrderEpisodes {
	
	public static void main(String[] args) throws IOException {
		File mainFolder = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars");
		//List<NameInfo> nameInfoList = OrderEpisodesUtils.loadFile(json);
		
		File folder = new File(mainFolder, "Clone Wars\\Full");
		
		File jsonFile = new File(mainFolder, THE_CLONE_WARS+".json"); 
		//createEmptyOrderedFiles(jsonFile, folder);
		
		
		
		//folder = new File("E:\\Clone Wars\\input");
		OrderEpisodesUtils.setOrderForEpisodes(jsonFile, folder);
		
		
		//createCloneWarsOrder(json);
		
		/*File folder = new File("E:\\Clone Wars\\input");
		List<FileInfo> fileInfos = new ArrayList<>();
		for(File file : folder.listFiles()) {
			FileInfo fileInfo = new FileInfo(file);
			if(file.isDirectory())
				fileInfos.add(fileInfo);
		}
		OrderEpisodesUtils.setOrderForEpisodes(fileInfos, nameInfoList);*/
	}
	
	public static final String SEARCH_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
	
	private static Integer getInteger(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static int romanToInteger(String test) {
		test = test.toUpperCase();
		int result = 0;
		int[] decimal = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		String[] roman = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
		for (int i = 0; i < decimal.length; i++ ) {
		    while (test.indexOf(roman[i]) == 0) {
		        result += decimal[i];
		        test = test.substring(roman[i].length());
		    }
		}
		return result;
	}

	public static void main2(String[] args) throws IOException {
		File json = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars\\colenT.json");
		File folder = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars\\Clone Wars");
		//createCloneWarsOrder(json);
		List<NameInfo> nameInfoList = OrderEpisodesUtils.loadFile(json);
		/*for(NameInfo nameInfo : nameInfoList) {
			File file = new File(folder, nameInfo.getFullName());
			file.mkdir();
			File child = new File(file, file.getName()+".srt");
			writeToFile(child, "");
			System.out.println(nameInfo.getFullName());
		}*/
		
		ManageFolder manageFolder = new ManageFolder(folder.getPath());
		System.out.println(manageFolder.TVMap);
		//manageFolder.moveFilesFromInput();
		
		FolderInfo folderInfo = manageFolder.TVMap.get("theclonewars");
		//= new FolderInfo(new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars\\Clone Wars\\W-Output\\TV\\The Clone Wars"));
		OrderEpisodesUtils.setOrderForEpisodes(folderInfo, nameInfoList);
		
		/*
		
		//Map<NameInfo, String> map = new HashMap<>();
		List<FileInfo> fileInfos = new ArrayList<>();
		for(File file : folder.listFiles()) {
			FileInfo fileInfo = new FileInfo(file);
			if(file.isDirectory())
				fileInfos.add(fileInfo);
		}
		setOrderForEpisodes(fileInfos, nameInfoList);
		
		*/
	}
	
	private static void createEmptyOrderedFiles(File json, File destFolder) throws IOException {
		List<NameInfo> nameInfoList = OrderEpisodesUtils.loadFile(json);
		for(NameInfo nameInfo : nameInfoList) {
			File file = new File(destFolder, nameInfo.getFullName());
			file.mkdir();
			File child = new File(file, file.getName()+".srt");
			OrderEpisodesUtils.writeToFile(child, "");
			System.out.println(nameInfo.getFullName());
		}	
	}
	
	private static final String THE_CLONE_WARS = "The Clone Wars";
	
	public static void createCloneWarsOrder(File jsonFolder) throws IOException {
		final String search = "https://www.starwars.com/news/star-wars-the-clone-wars-chronological-episodeorder";
		Document googlePage = OrderEpisodesUtils.loadPage(search);
		List<NameInfo> nameInfoList = new ArrayList<>();
		Elements tables = googlePage.getElementsByTag("table");
		Element table = tables.first();
		Elements rows = table.getElementsByTag("tr");
		int orderNumber = 0;
		for(Element row : rows) {
			Elements columns = row.getElementsByTag("td");
			if(columns.size() >= 3) {
				Element orderNumberElm = columns.get(0);
				Element episodeNumberElm = columns.get(1);
				Element episodeNameElm = columns.get(2);
				if(orderNumberElm != null && episodeNumberElm != null
						&& episodeNameElm != null) {
					//String orderNumber = orderNumberElm.text();
					String episodeNumber = episodeNumberElm.text();
					Element episodeNameLinkElm = episodeNameElm.getElementsByTag("a").first();
					if(episodeNameLinkElm != null) {
						String episodeName = episodeNameLinkElm.ownText();
						//System.out.println(orderNumber + " " + episodeNumber + " " + episodeName);
						NameInfo nameInfo = new NameInfo();
						orderNumber++;
						nameInfo.setIndex(""+orderNumber);
						if(!episodeNumber.strip().equals("T")) {
							System.out.println(episodeNumber.strip());
							nameInfo.setName(THE_CLONE_WARS);
							nameInfo.setSeason(""+episodeNumber.charAt(0));
							nameInfo.setEpisode(""+episodeNumber.substring(1));
							nameInfo.setEpisodeName(episodeNameGood(episodeName));
						}
						else {
							nameInfo.setName(episodeName);
						}
						nameInfoList.add(nameInfo);
					}
				}
			}
		}
		OrderEpisodesUtils.saveMedia(nameInfoList, jsonFolder, THE_CLONE_WARS);
	}
	
	public static void createCloneWarsOrderTester(File jsonFolder) throws IOException {
		List<NameInfo> nameInfoList = OrderEpisodesUtils.loadFile(new File(jsonFolder, THE_CLONE_WARS+".json"));
		
		final String search = "https://www.starwars.com/news/star-wars-the-clone-wars-chronological-episodeorder";
		Document googlePage = OrderEpisodesUtils.loadPage(search);
		Elements tables = googlePage.getElementsByTag("table");
		Element table = tables.first();
		Elements rows = table.getElementsByTag("tr");
		for(Element row : rows) {
			Elements columns = row.getElementsByTag("td");
			if(columns.size() >= 3) {
				Element orderNumberElm = columns.get(0);
				Element episodeNumberElm = columns.get(1);
				Element episodeNameElm = columns.get(2);
				if(orderNumberElm != null && episodeNumberElm != null
						&& episodeNameElm != null) {
					String orderNumber = orderNumberElm.text();
					String episodeNumber = episodeNumberElm.text();
					Element episodeNameLinkElm = episodeNameElm.getElementsByTag("a").first();
					if(episodeNameLinkElm != null) {
						String episodeName = episodeNameLinkElm.ownText();
						//System.out.println(orderNumber + " " + episodeNumber + " " + episodeName);
						NameInfo nameInfo = new NameInfo();
						if(!episodeNumber.strip().equals("T")) {
							System.out.println(episodeNumber.strip());
							nameInfo.setName(THE_CLONE_WARS);
							nameInfo.setSeason(""+episodeNumber.charAt(0));
							nameInfo.setEpisode(""+episodeNumber.substring(1));
							nameInfo.setEpisodeName(episodeNameGood(episodeName));
							
							NameInfo fileInfo = nameInfoList.stream().filter(p -> 
							nameInfo.equalsBasedOnCriteria(p, NameInfoType.NAME, NameInfoType.SEASON, NameInfoType.EPISODE))
									.findFirst().orElse(null);
							if(fileInfo != null) {
								int i1 = getInteger(fileInfo.getIndex());
								int i2 = getInteger(orderNumber);
								if(i2 >= 3)
									i2++;
								if(i1 == i2) {
									System.err.println(i1 + " != " + i2 +" (" +(i1==i2)+")");
									System.err.println(orderNumber+" t : " + fileInfo.getFullNameWithIndex());
								}
							}
							else
								System.err.println(nameInfo);
						}
						else {
							nameInfo.setName(episodeName);
						}
					}
				}
			}
		}
	}
	
	public static String episodeNameGood(String episodeName) {
		Pattern pattern = Pattern.compile(": Pt. (?<num>.+)");
		Matcher matcher = pattern.matcher(episodeName);
		if(matcher.find()) {
			String numSt = matcher.group("num");
			Integer num = getInteger(numSt);
			if(num == null) {
				//then try roman
				num = romanToInteger(numSt);
				if(num == 0)
					num = null;
			}
			if(num != null) {
				episodeName = episodeName.substring(0, matcher.start()) + " ("+num+")" + episodeName.substring(matcher.end());
			}
		}
		return episodeName;
	}
}