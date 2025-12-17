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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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
import DataStructures.FolderInfo;
import DataStructures.ManageFolder;
import DataStructures.ManageFolder.FlagSearchMainFolderExeception;
import DataStructures.NameInfo;

public class batmanTASOrderEpisodes {
	
	public static void main(String[] args) throws IOException, FlagSearchMainFolderExeception {	
		File json = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars");
		
		
		/*File btas = new File(json, "BTAS");
		btas = new File("F:\\Check Yourself Bef");
		ManageFolder man = new ManageFolder(btas.getAbsolutePath());
		FolderInfo info = man.getMainFolder(new NameInfo("Batman The Animated Series (1992) s02"));
		man.changeMiniSeriesToTvSeries(info);*/
		
		
		// use to create json
		//createSupermanAndTheNewBatmanOrder(json);
		
		
		
		
		// use to add order to files
		File jsonFile = new File(json, BATMAN+".json"); 
		File folder = new File("F:\\Batman The Animated Series (1992)");
		OrderEpisodesUtils.setOrderForEpisodes(jsonFile, folder);
		
		/*List<NameInfo> supermanNameInfoList = loadFile(new File(json, SUPERMAN+".json"));
		for(NameInfo nameInfo : supermanNameInfoList) { 
			Integer number = getInteger(nameInfo.getEpisode());
			System.out.println(number);
			if(number != null)
				setSeasonEpisodeSupermanTAS(nameInfo, number);
		}
		
		
		convertProductionNumbersToEpisodeSeasonsSupermanTAS(supermanNameInfoList);*/
		
		
		
		
		//createCloneWarsOrder(json);
		
		//List<NameInfo> nameInfoList = loadFile(json);
		
		/*File folder = new File("E:\\Clone Wars\\input");
		List<FileInfo> fileInfos = new ArrayList<>();
		for(File file : folder.listFiles()) {
			FileInfo fileInfo = new FileInfo(file);
			if(file.isDirectory())
				fileInfos.add(fileInfo);
		}
		setOrderForEpisodes(fileInfos, nameInfoList);*/
	}
	
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
	
	private static List<TextNode> getPageTextNodes(String searchPath) throws IOException {
		List<TextNode> textNodesList = new ArrayList<>();
		Document googlePage = OrderEpisodesUtils.loadPage(searchPath);
		Element table = googlePage.getElementById("aContent");
		Elements rows = table.getElementsByTag("b");
		for(Element row : rows) {
			List<TextNode> textNodes = row.textNodes();
			if(textNodes != null)
				textNodesList.addAll(textNodes);
		}
		return textNodesList;
	}
	
	public static void createCloneWarsOrder(File jsonDir) throws IOException {
		final String search = "https://comicbookmovie.com/fan-fic/my-episode-timeline-for-batman-the-animated-series-version-20-a64619#gs.e8qmmw";
		List<NameInfo> nameInfoList = new ArrayList<>();
		List<TextNode> textNodes = getPageTextNodes(search);
		for(TextNode textNode : textNodes) {
			String text = textNode.text().trim();
			//System.out.println(text);
			int twoWords = text.indexOf(' ');
			if(twoWords != -1) {
				String firstText = text.substring(0, twoWords);
				String secondText = text.substring(twoWords+1);
				if(firstText.matches("\\d+a"))
					firstText = firstText.substring(0, firstText.length()-1);
				List<String> multipleTextList = getEpisodesInTheText(firstText);
				for(String textInList : multipleTextList) {
					if(!addToBatmanTASList(textInList, secondText, nameInfoList))
						System.err.println(text);
				}
			}
		}
		convertProductionNumbersToEpisodeSeasonsBatmanTAS(nameInfoList);
		OrderEpisodesUtils.saveMedia(nameInfoList, jsonDir, BATMAN);
	}
	
	private static List<String> getEpisodesInTheText(String firstText) {
		List<String> list = new ArrayList<>();
		int multipleEpisodes = firstText.indexOf('-');
		if(multipleEpisodes != -1) {
			String firstEpisode = firstText.substring(0, multipleEpisodes);
			String lastEpisode = firstText.substring(multipleEpisodes+1);
			Integer firstEpisodeNumber = getInteger(firstEpisode);
			Integer lastEpisodeNumber = getInteger(lastEpisode);
			if(firstEpisode != null && lastEpisode != null) {
				while(firstEpisodeNumber <= lastEpisodeNumber) {
					list.add(""+firstEpisodeNumber);
					firstEpisodeNumber++;
				}
			}
		}
		else
			list.add(firstText);
		return list;
	}
	
	public static void createSupermanAndTheNewBatmanOrder(File jsonDir) throws IOException {
		final String search = "https://comicbookmovie.com/fan-fic/my-episode-timeline-for-the-new-batman-superman-adventures-a66635#gs.e945ia";
		List<NameInfo> supermanNameInfoList = new ArrayList<>();
		List<NameInfo> batmanNameInfoList = new ArrayList<>();
		List<TextNode> textNodes = getPageTextNodes(search);
		for(TextNode textNode : textNodes) {
			String text = textNode.text().trim();
			System.out.println(text);
			int twoWords = text.indexOf(' ');
			if(twoWords != -1) {
				String firstText = text.substring(0, twoWords);
				String secondText = text.substring(twoWords+1);
				if(firstText.equals("01"))
					firstText = "B01";
				String tvName = null;
				if(firstText.startsWith("S")) {
					firstText = firstText.substring(1);
					List<String> multipleTextList = getEpisodesInTheText(firstText);
					for(String textInList : multipleTextList) {
						if(!addToSupermanTASList(textInList, secondText, supermanNameInfoList))
							System.err.println(text);
					}
				}
				else if(firstText.startsWith("B")) {
					firstText = firstText.substring(1);
					List<String> multipleTextList = getEpisodesInTheText(firstText);
					for(String textInList : multipleTextList) {
						if(!addToTheNewBatmanAdventuresTASList(textInList, secondText, batmanNameInfoList))
							System.err.println(text);
					}
				}
				else {
					if(!addToTheNewBatmanAdventuresTASList(null, text, batmanNameInfoList))
						System.err.println(text);
				}
			}
		}
		convertProductionNumbersToEpisodeSeasonsSupermanTAS(supermanNameInfoList);
		OrderEpisodesUtils.saveMedia(supermanNameInfoList, jsonDir, SUPERMAN);
		
		convertProductionNumbersToEpisodeSeasonsTheNewBatmanAdveturesTAS(batmanNameInfoList);
		OrderEpisodesUtils.saveMedia(batmanNameInfoList, jsonDir, THE_NEW_BATMAN_ADVENTURES);
	}
	
	private static NameInfo addTvEpisodeToList(String firstText, String secondText, List<NameInfo> nameInfoList, String tvSeriesName) {
		Integer number = getInteger(firstText);
		NameInfo nameInfo = null;
		if(number != null) {
			nameInfo = new NameInfo();
			nameInfo.setName(tvSeriesName);
			nameInfo.setEpisode(""+number);
			//setSeasonEpisode(nameInfo, number);
			nameInfo.setEpisodeName(secondText);
			nameInfoList.add(nameInfo);
			nameInfo.setIndex(""+nameInfoList.size());
		}
		return nameInfo;
	}
	
	private static boolean addToBatmanTASList(String firstText, String secondText, List<NameInfo> nameInfoList) {
		NameInfo nameInfo = addTvEpisodeToList(firstText, secondText, nameInfoList, BATMAN);
		if(nameInfo == null) {
			if(firstText.startsWith("MOVIE") && (secondText.equals("Mask of the Phantasm") || secondText.equals("Sub Zero"))) {
				nameInfo = new NameInfo();
				if(secondText.equals("Mask of the Phantasm")) {
					nameInfo.setName("Batman Mask of the Phantasm");
					nameInfo.setYear("1993");
				} else if(secondText.equals("Sub Zero")) {
					nameInfo.setName("Batman & Mr. Freeze SubZero");
					nameInfo.setYear("1998");
				}
				nameInfoList.add(nameInfo);
				nameInfo.setIndex(""+nameInfoList.size());
				return true;
			}
		}
		else {
			Integer number = fixBatmanTASIncorrectNumbers(nameInfo.getEpisode());
			if(number != null)
				nameInfo.setEpisode(""+number);
			return true;
		}
		return false;
	}
	
	private static final String BATMAN = "Batman The Animated Series";
	private static final String SUPERMAN = "Superman - The Animated Series";
	private static final String THE_NEW_BATMAN_ADVENTURES = "The New Batman Adventures";
	
	private static boolean addToSupermanTASList(String firstText, String secondText, List<NameInfo> nameInfoList) {
		NameInfo nameInfo = addTvEpisodeToList(firstText, secondText, nameInfoList, SUPERMAN);
		if(nameInfo != null) {
			Integer number = fixSupermanTASIncorrectNumbers(nameInfo.getEpisode());
			if(number != null)
				setSeasonEpisodeSupermanTAS(nameInfo, number);
			return true;
		}
		return false;
	}
	
	private static boolean addToTheNewBatmanAdventuresTASList(String firstText, String secondText, List<NameInfo> nameInfoList) {
		NameInfo nameInfo = addTvEpisodeToList(firstText, secondText, nameInfoList, THE_NEW_BATMAN_ADVENTURES);
		if(nameInfo == null) {
			if(secondText.equals("Mystery of the Batwoman")) {
				nameInfo = new NameInfo();
				nameInfo.setName("Batman Mystery of the Batwoman");
				nameInfo.setYear("2003");
				nameInfoList.add(nameInfo);
				nameInfo.setIndex(""+nameInfoList.size());
				return true;
			}
		}
		else {
			return true;
			/*
			Integer number = fixBatmanTASIncorrectNumbers(nameInfo.getEpisode());
			if(number != null)
				nameInfo.setEpisode(""+number);
			*/
		}
		return false;
	}
	
	/**
	 * fix problems with the list that is on the site
	 * @param number
	 * @return
	 */
	private static Integer fixBatmanTASIncorrectNumbers(String numberText) {
		Integer number = getInteger(numberText);
		if(number == 11) //Two-Face, Part 2
			return 17;
		if(number == 12) //It’s Never Too Late
			return 11;
		if(number == 13) //I’ve Got Batman in My Basement
			return 12;
		if(number == 15) //The Cat and the Claw, Part 1
			return 13;
		if(number == 17) //See No Evil
			return 15;
		if(number == 20) //Joker’s Favor
			return 22;
		if(number == 21) //Feat of Clay, Part 1
			return 20;
		if(number == 22) //Feat of Clay, Part 2
			return 21;
		if(number == 33) //Robin’s Reckoning, Part 2
			return 37;
		if(number == 34) //The Laughing Fish
			return 33;
		if(number == 35) //Night of the Ninja
			return 34;
		if(number == 36) //Cat Scratch Fever
			return 35;
		if(number == 37) //The Strange Secret of Bruce Wayne
			return 36;
		if(number == 39) //Heart of Steel, Part 2
			return 44;
		if(number == 40) //If You’re So Smart, Why Aren’t You Rich?
			return 39;
		if(number == 41) //Joker’s Wild
			return 40;
		if(number == 42) //Tyger, Tyger
			return 41;
		if(number == 43) //Moon of the Wolf
			return 42;
		if(number == 44) //Day of the Samurai
			return 43;
		if(number == 58) //Shadow of the Bat, Part 2
			return 61;
		if(number == 59) //Blind as a Bat
			return 58;
		if(number == 60) //The Demon’s Quest, Part 1
			return 59;
		if(number == 61) //The Demon’s Quest, Part 2
			return 63;
		if(number == 62) //His Silicon Soul
			return 60;
		if(number == 63) //Fire from Olympus
			return 62;
		return number;
	}
	
	/**
	 * fix problems with the list that is on the site
	 * @param number
	 * @return
	 */
	private static Integer fixSupermanTASIncorrectNumbers(String numberText) {
		Integer number = getInteger(numberText);
		if(number == 32) //Bizarro’s World
			return 33;
		if(number == 33) //The Hand of Fate
			return 32;
		return number;
	}
	
	public static void convertProductionNumbersToEpisodeSeasonsBatmanTAS(List<NameInfo> nameInfoList) throws IOException {
		final String searchPath = "https://dcau.fandom.com/wiki/Batman:_The_Animated_Series#Season_One";
		convertProductionNumbersToEpisodeSeasons(searchPath, nameInfoList, BATMAN, null);
	}
	
	public static void convertProductionNumbersToEpisodeSeasonsSupermanTAS(List<NameInfo> nameInfoList) throws IOException {
		final String searchPath = "https://dcau.fandom.com/wiki/Superman:_The_Animated_Series#Season_One";
		convertProductionNumbersToEpisodeSeasons(searchPath, nameInfoList, SUPERMAN, TableType.EPISODE_NUMBER);
	}
	
	public static void convertProductionNumbersToEpisodeSeasonsTheNewBatmanAdveturesTAS(List<NameInfo> nameInfoList) throws IOException {
		final String searchPath = "https://dcau.fandom.com/wiki/The_New_Batman_Adventures#Season_One";
		convertProductionNumbersToEpisodeSeasons(searchPath, nameInfoList, THE_NEW_BATMAN_ADVENTURES, null);
	}
	
	public static void convertProductionNumbersToEpisodeSeasons(String searchPath, List<NameInfo> nameInfoList, String seriesName, TableType findBy) throws IOException {
		Document googlePage = OrderEpisodesUtils.loadPage(searchPath);
		//System.out.println(googlePage);
		Elements rows = googlePage.select("h3, h4");
		System.out.println(rows);
		boolean firstTable = true;
		for(Element row : rows) {
			Element seasonTextElm = row.getElementsByTag("span").first();
			if(seasonTextElm != null) {
				String text = seasonTextElm.text();
				System.out.println(text);
				String seasonText = "Season ";
				if(text.startsWith(seasonText)) {
					Integer seasonNumber = getNumberFromNumberName(text.substring(seasonText.length()));
					System.out.println(seasonNumber);
					Element element = row;
					while(element != null) {
						element = element.nextElementSibling();
						if(element.is("table"))
							break;
					}
					if(isTable(element)) {
						if(firstTable) {
							if(findBy == null) {
								findBy = findTableType(element);
							}
							firstTable = false;
						}
						parseTable(element, seasonNumber, nameInfoList, seriesName, findBy);
					}
				}
			}
		}
		System.out.println("End");
		for(NameInfo nameInfo : nameInfoList) {
			if(!nameInfo.hasSeason()) {
				System.out.println("Myyy: " + nameInfo.getFullNameWithIndex());
			}
		}
	}
	
	private static TableType findTableType(Element element) {
		TableType tableType = null;
		if(isTable(element)) {
			Elements headersRows = element.getElementsByTag("th");
			for(Element headerRow : headersRows) {
				String headerName = headerRow.text();
				System.out.println(headerName);
				if(headerName.equals("Prod. Order")) {
					tableType = TableType.PRODUCTION_ORDER;
					break;
				} else if(headerName.equals("Prod. Code")) {
					tableType = TableType.PRODUCTION_CODE;
					break;
				}
			}
		}
		return tableType;
	}
	
	private static boolean isTable(Element element) {
		return element != null && element.is("table");
	}
	
	private static void parseTable(Element table, Integer seasonNumber, List<NameInfo> nameInfoList, String seriesName, TableType findBy) {
		if(findBy == null || !isTable(table))
			return;
		Elements tableRows = table.getElementsByTag("tr");
		for(Element tableRow : tableRows) {
			Elements rowVals = tableRow.getElementsByTag("td");
			if(rowVals.size() >= 3) {
				String episodeNumber = rowVals.get(0).text();
				String productionNumber = rowVals.get(1).ownText();
				String episodeName = rowVals.get(2).text();
				if(findBy == TableType.PRODUCTION_CODE) {
					Pattern pattern = Pattern.compile("406-5(?<number>\\d\\d)");
					Matcher matcher = pattern.matcher(productionNumber);
					if(matcher.matches()) {
						saveTableRow(seasonNumber, episodeNumber, matcher.group("number"), episodeName, nameInfoList, seriesName);
					}
				}
				else if(findBy == TableType.PRODUCTION_ORDER) {
					List<String> productionNumbers = getEpisodesInTheText(productionNumber);
					List<String> episodesNumbers = getEpisodesInTheText(episodeNumber);
					if(productionNumbers.size() == episodesNumbers.size()) {
						for(int i = 0; i < productionNumbers.size(); i++) {
							String productionNumberStr = productionNumbers.get(i);
							String episodeNumberStr = episodesNumbers.get(i);
							String subEpisodeName = episodeName;
							if(productionNumbers.size() > 1)
								subEpisodeName += ", Part "+(i+1);
							saveTableRow(seasonNumber, episodeNumberStr, productionNumberStr, subEpisodeName, nameInfoList, seriesName);
						}
					}
					else
						System.err.println("Not same size: " + "(" + productionNumber + ") " + productionNumbers + " != " + "(" + productionNumber + ") " + episodesNumbers);
				}
				else if(findBy == TableType.EPISODE_NUMBER) {
					List<String> episodesNumbers = getEpisodesInTheText(episodeNumber);
					for(int i = 0; i < episodesNumbers.size(); i++) {
						String episodeNumberStr = episodesNumbers.get(i);
						String subEpisodeName = episodeName;
						if(episodesNumbers.size() > 1)
							subEpisodeName += ", Part "+(i+1);
						saveTableRow(seasonNumber, episodeNumberStr, subEpisodeName, nameInfoList, seriesName);
					}
				}
			}
		}
	}

	private static void saveTableRow(Integer seasonNumber, String episodeNumber, String productionNumber, String episodeName, List<NameInfo> nameInfoList, String seriesName) {
		Integer productionValueNum = getInteger(productionNumber);
		Integer episodeValueNum = getInteger(episodeNumber);
		if(productionValueNum != null && episodeNumber != null) {
			Optional<NameInfo> nameInfoOpt = nameInfoList.stream().filter(n -> n.hasEpisode() && !n.hasSeason() && n.hasName() && n.getName().equals(seriesName) && productionValueNum == getInteger(n.getEpisode()))
					.findFirst();
			if(!saveTableRow(nameInfoOpt, seasonNumber, episodeValueNum, episodeName))
				System.err.println("Not Found for: " + "Prod num (" + productionValueNum + ") " + "S"+seasonNumber+"E"+episodeNumber + " - " + episodeName);
		}
	}
	
	private static void saveTableRow(Integer seasonNumber, String episodeNumber, String episodeName, List<NameInfo> nameInfoList, String seriesName) {
		Integer episodeValueNum = getInteger(episodeNumber);
		if(episodeValueNum != null) {
			Optional<NameInfo> nameInfoOpt = nameInfoList.stream().filter(n -> n.hasEpisode() && n.hasSeason() && seasonNumber == getInteger(n.getSeason()) && n.hasName() && n.getName().equals(seriesName) && episodeValueNum == getInteger(n.getEpisode()))
					.findFirst();
			if(!saveTableRow(nameInfoOpt, seasonNumber, episodeValueNum, episodeName))
				System.err.println("Not Found for: " + "S"+seasonNumber+"E"+episodeValueNum + " - " + episodeName);
		}
	}
	
	private static boolean saveTableRow(Optional<NameInfo> nameInfoOpt, Integer seasonNumber, Integer episodeNumber, String episodeName) {
		if(nameInfoOpt.isPresent()) {
			NameInfo nameInfo = nameInfoOpt.get();
			String description = nameInfo.getDescription();
			if(!description.toLowerCase().replaceAll(" ", "").equals(episodeName.toLowerCase().replaceAll(" ", "")))
				System.out.print(nameInfo.getFullNameWithIndex() + " = ");
			nameInfo.setSeason(""+seasonNumber);
			nameInfo.setEpisode(""+episodeNumber);
			nameInfo.setEpisodeName(episodeName);
			if(!description.toLowerCase().replaceAll(" ", "").equals(episodeName.toLowerCase().replaceAll(" ", "")))
				System.out.print(nameInfo.getFullNameWithIndex() + "\n");
			return true;
		}
		return false;
	}
	
	private static enum TableType {
		PRODUCTION_CODE,
		PRODUCTION_ORDER,
		EPISODE_NUMBER
	}
	
	private static Integer getNumberFromNumberName(String numberName) {
		Map<String, Integer> map = new HashMap<>(Map.ofEntries(
			    Map.entry("zero", 0),
			    Map.entry("one", 1),
			    Map.entry("two", 2),
			    Map.entry("three", 3),
			    Map.entry("four", 4),
			    Map.entry("five", 5),
			    Map.entry("six", 6),
			    Map.entry("seven", 7),
			    Map.entry("eight", 8),
			    Map.entry("nine", 9)
			));
		numberName = numberName.toLowerCase();
		return map.get(numberName);
	}
	
	private static void setSeasonEpisodeSupermanTAS(NameInfo nameInfo, int number) {
		final int S1 = 13, S2 = 41, S3 = 51, S4 = 54;
		int season = -1, episode = -1;
		if(1 <= number &&  number <= S1) {
			season = 1;
			episode = number;
		} else if(S1 < number && number <= S2) {
			season = 2;
			episode = number - S1;
		} else if(S2 < number && number <= S3) {
			season = 3;
			episode = number - S2;
		} else if(S3 < number && number <= S4) {
			season = 4;
			episode = number - S3;
		}
		if(season != -1 && episode != -1) {
			nameInfo.setEpisode(""+episode);
			nameInfo.setSeason(""+season);
		}
	}
	
	private static void setSeasonEpisode(NameInfo nameInfo, int number) {
		final int S1 = 60, S2 = 70, S3 = 80, S4 = 85;
		int season = -1, episode = -1;
		if(1 <= number &&  number <= S1) {
			season = 1;
			episode = number;
		} else if(S1 < number && number <= S2) {
			season = 2;
			episode = number - S1;
		} else if(S2 < number && number <= S3) {
			season = 3;
			episode = number - S2;
		} else if(S3 < number && number <= S4) {
			season = 4;
			episode = number - S3;
		}
		if(season != -1 && episode != -1) {
			nameInfo.setEpisode(""+episode);
			nameInfo.setSeason(""+season);
		}
	}
}