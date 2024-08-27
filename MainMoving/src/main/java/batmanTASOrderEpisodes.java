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
import DataStructures.NameInfo;

public class batmanTASOrderEpisodes {
	
	public static void main(String[] args) throws IOException {
		File json = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars\\batmanTAS.json");
		createCloneWarsOrder(json);
		
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
		List<NameInfo> nameInfoList = loadFile(json);
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
		setOrderForEpisodes(folderInfo, nameInfoList);
		
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
	
	public static void setOrderForEpisodes(FolderInfo folderInfo, List<NameInfo> episodesOrderList) {
		for(NameInfo nameInfo : episodesOrderList) {
			if(nameInfo.hasIndex()) {
				if(nameInfo.hasEpisode()) {
					File file = folderInfo.getFolderByType(nameInfo, FolderType.TV_EPISODE);
					if(file.isDirectory()) {
						FileInfo fileInfo = new FileInfo(file);
						try {
							renameToIndex(fileInfo, nameInfo);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static void setOrderForEpisodes(List<FileInfo> fileInfos, List<NameInfo> episodesOrderList) {
		for(NameInfo nameInfo : episodesOrderList) {
			if(nameInfo.hasIndex()) {
				FileInfo fileInfo = fileInfos.stream().filter(p -> 
							nameInfo.getName().equals(p.getName()) && nameInfo.getEpisode().equals(p.getEpisode()) && nameInfo.getSeason().equals(p.getSeason()))
						.findFirst().orElse(null);
				if(fileInfo != null) {
					File file = fileInfo.getFile();
					System.out.println("Man " + file);
					if(file.isDirectory()) {
						try {
							renameToIndex(fileInfo, nameInfo);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private static void renameToIndex(FileInfo fileInfo, NameInfo nameInfo) throws IOException {
		if(fileInfo.hasIndex() && nameInfo.hasIndex() && fileInfo.getIndex().equals(nameInfo.getIndex()))
			return;
		fileInfo.setIndex(nameInfo.getIndex());
		File newFile = new File(fileInfo.getParentPath(),fileInfo.getFullNameWithIndex());
		Files.move(fileInfo.getFile().toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static void createCloneWarsOrder(File json) throws IOException {
		final String search = "https://comicbookmovie.com/fan-fic/my-episode-timeline-for-batman-the-animated-series-version-20-a64619#gs.e8qmmw";
		Map<String, String> prop = new HashMap<>();
		prop.put("gl", "us");
		prop.put("hl", "en");
		Document googlePage = Jsoup.connect(search)
				.userAgent(SEARCH_AGENT)
				.data(prop)
				.header("Accept-Language", "en")
				.header("Accept-Language", "en-US").get();
		List<NameInfo> nameInfoList = new ArrayList<>();
		System.out.println(googlePage);
		Element table = googlePage.getElementById("aContent");
		Elements rows = table.getElementsByTag("b");
		int listIndex = 1;
		for(Element row : rows) {
			List<TextNode> textNodes = row.textNodes();
			for(TextNode textNode : textNodes) {
				String text = textNode.text().trim();
				System.out.println(text);
				int twoWords = text.indexOf(' ');
				if(twoWords != -1) {
					String firstText = text.substring(0, twoWords);
					String secondText = text.substring(twoWords+1);
					int twoParts = firstText.indexOf('-');
					if(twoParts != -1) {
						if(addToList(firstText.substring(0, twoParts), secondText, nameInfoList, listIndex))
							listIndex++;
						firstText = firstText.substring(twoParts+1);
					}
					if(addToList(firstText, secondText, nameInfoList, listIndex))
						listIndex++;
					/*Integer number = getInteger(firstText);
					NameInfo nameInfo = new NameInfo();
					if(number != null) {
						nameInfo.setName("Batman The Animated Series");
						nameInfo.setIndex(""+listIndex++);
						nameInfo.setEpisode(""+number);
						//setSeasonEpisode(nameInfo, number);
						nameInfo.setEpisodeName(secondText);
						nameInfoList.add(nameInfo);
					}
					else if(firstText.startsWith("MOVIE")) {
						nameInfo.setIndex(""+listIndex++);
						nameInfoList.add(nameInfo);
					}
					System.out.println(text + " = " + nameInfo.getFullNameWithIndex());*/
					
				}
				
			}
		}
		convertProductionNumbersToEpisodeSeasons(nameInfoList);
		saveMedia(json, nameInfoList);
	}
	
	public static void createSupermanAndTheNewBatmanOrder(File jsonDir) throws IOException {
		final String search = "https://comicbookmovie.com/fan-fic/my-episode-timeline-for-the-new-batman-superman-adventures-a66635#gs.e945ia";
		Map<String, String> prop = new HashMap<>();
		prop.put("gl", "us");
		prop.put("hl", "en");
		Document googlePage = Jsoup.connect(search)
				.userAgent(SEARCH_AGENT)
				.data(prop)
				.header("Accept-Language", "en")
				.header("Accept-Language", "en-US").get();
		List<NameInfo> nameInfoList = new ArrayList<>();
		System.out.println(googlePage);
		Element table = googlePage.getElementById("aContent");
		Elements rows = table.getElementsByTag("b");
		int listIndex = 1;
		for(Element row : rows) {
			List<TextNode> textNodes = row.textNodes();
			for(TextNode textNode : textNodes) {
				String text = textNode.text().trim();
				System.out.println(text);
				int twoWords = text.indexOf(' ');
				if(twoWords != -1) {
					String firstText = text.substring(0, twoWords);
					String secondText = text.substring(twoWords+1);
					if(firstText.startsWith("B")) {
						firstText = 
					}
					else if(firstText.startsWith("S")) {
						
					}
					int twoParts = firstText.indexOf('-');
					if(twoParts != -1) {
						if(addToList(firstText.substring(0, twoParts), secondText, nameInfoList, listIndex))
							listIndex++;
						firstText = firstText.substring(twoParts+1);
					}
					if(addToList(firstText, secondText, nameInfoList, listIndex))
						listIndex++;
				}
				
			}
		}
		convertProductionNumbersToEpisodeSeasons(nameInfoList);
		saveMedia(json, nameInfoList);
	}
	
	private static boolean addToList(String firstText, String secondText, List<NameInfo> nameInfoList, int listIndex) {
		Integer number = getInteger(firstText);
		NameInfo nameInfo = new NameInfo();
		if(number != null) {
			nameInfo.setName("Batman The Animated Series");
			nameInfo.setIndex(""+listIndex++);
			nameInfo.setEpisode(""+fixThePageEpisodeNumberIncorrect(number));
			//setSeasonEpisode(nameInfo, number);
			nameInfo.setEpisodeName(secondText);
			nameInfoList.add(nameInfo);
			return true;
		}
		else if(firstText.startsWith("MOVIE") && (secondText.equals("Mask of the Phantasm") || secondText.equals("Sub Zero"))) {
			nameInfo.setIndex(""+listIndex++);
			if(secondText.equals("Mask of the Phantasm")) {
				nameInfo.setName("Batman Mask of the Phantasm");
				nameInfo.setYear("1993");
			} else if(secondText.equals("Sub Zero")) {
				nameInfo.setName("Batman & Mr. Freeze SubZero");
				nameInfo.setYear("1998");
			}
			nameInfoList.add(nameInfo);
			return true;
		}
		return false;
	}
	
	/**
	 * fix problems with the list that is on the site
	 * @param number
	 * @return
	 */
	private static Integer fixThePageEpisodeNumberIncorrect(Integer number) {
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
	
	public static void convertProductionNumbersToEpisodeSeasons(List<NameInfo> nameInfoList) throws IOException {
		final String search = "https://dcau.fandom.com/wiki/Batman:_The_Animated_Series#Season_One";
		Map<String, String> prop = new HashMap<>();
		prop.put("gl", "us");
		prop.put("hl", "en");
		Document googlePage = Jsoup.connect(search)
				.userAgent(SEARCH_AGENT)
				.data(prop)
				.header("Accept-Language", "en")
				.header("Accept-Language", "en-US").get();
		//System.out.println(googlePage);
		Elements rows = googlePage.getElementsByTag("h4");
		System.out.println(rows);
		for(Element row : rows) {
			Element seasonTextElm = row.getElementsByTag("span").first();
			String text = seasonTextElm.text();
			System.out.println(text);
			String seasonText = "Season ";
			if(text.startsWith(seasonText)) {
				String seasonNumber = getNumberFromNumberName(text.substring(seasonText.length()));
				System.out.println(seasonNumber);
				Element element = row;
				while(element != null) {
					element = element.nextElementSibling();
					if(element.is("table"))
						break;
				}
				if(element != null && element.is("table")) {
					Elements tableRows = element.getElementsByTag("tr");
					for(Element tableRow : tableRows) {
						Elements rowVals = tableRow.getElementsByTag("td");
						if(rowVals.size() >= 3) {
							String episodeNumber = rowVals.get(0).text();
							String productionCode = rowVals.get(1).ownText();
							String episodeName = rowVals.get(2).text();
							Pattern pattern = Pattern.compile("406-5(?<number>\\d\\d)");
							Matcher matcher = pattern.matcher(productionCode);
							System.out.println(productionCode);
							if(matcher.matches()) {
								Integer productionValueNum = getInteger(matcher.group("number"));
								if(productionValueNum != null) {
									Optional<NameInfo> nameInfoOpt = nameInfoList.stream().filter(n -> n.hasEpisode() && !n.hasSeason() && productionValueNum == getInteger(n.getEpisode()))
											.findFirst();
									if(nameInfoOpt.isPresent()) {
										NameInfo nameInfo = nameInfoOpt.get();
										String description = nameInfo.getDescription();
										if(!description.toLowerCase().replaceAll(" ", "").equals(episodeName.toLowerCase().replaceAll(" ", "")))
											System.out.print(nameInfo.getFullNameWithIndex() + " = ");
										nameInfo.setSeason(seasonNumber);
										nameInfo.setEpisode(""+getInteger(episodeNumber));
										nameInfo.setEpisodeName(episodeName);
										if(!description.toLowerCase().replaceAll(" ", "").equals(episodeName.toLowerCase().replaceAll(" ", "")))
											System.out.print(nameInfo.getFullNameWithIndex() + "\n");
									}
								}
							}
						}
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
	
	private static String getNumberFromNumberName(String numberName) {
		Map<String, String> map = new HashMap<>(Map.ofEntries(
			    Map.entry("zero", "0"),
			    Map.entry("one", "1"),
			    Map.entry("two", "2"),
			    Map.entry("three", "3"),
			    Map.entry("four", "4"),
			    Map.entry("five", "5"),
			    Map.entry("six", "6"),
			    Map.entry("seven", "7"),
			    Map.entry("eight", "8"),
			    Map.entry("nine", "9")
			));
		numberName = numberName.toLowerCase();
		return map.get(numberName);
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
	
	private static void writeToFile(File file, String text) throws IOException {
        try {
        	FileWriter fileWriter = new FileWriter(file);
        	BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(text);
            writer.close();
        }
        catch ( IOException e){
        	e.printStackTrace();
        }
	}
	
	public static List<NameInfo> loadFile(File file) throws IOException {
		ObjectMapper mapper = createPolymorphismMediaSimple();
		CollectionType type = TypeFactory.defaultInstance().constructCollectionType(List.class, NameInfo.class);
		return mapper.readValue(file, type);
	}
	
	public static void saveMedia(File file, Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper = JsonMapper.builder().build();
		mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
		//mapper.addMixIn(MediaSimple.class, MediaSimpleMixIn.class);
		//MapType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, MediaSimple.class);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.writeValue(file, object);
		System.out.println(file);
	}
	
	private static ObjectMapper createPolymorphismMediaSimple() {
		ObjectMapper mapper = new ObjectMapper();
		mapper = JsonMapper.builder().build();
		mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		return mapper;
	}
}