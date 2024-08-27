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
import DataStructures.FolderInfo;
import DataStructures.ManageFolder;
import DataStructures.NameInfo;

public class cloneWarsOrderEpisodes {
	
	public static void main(String[] args) throws IOException {
		File json = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars\\colenT.json");
		List<NameInfo> nameInfoList = loadFile(json);
		
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
		final String search = "https://www.starwars.com/news/star-wars-the-clone-wars-chronological-episodeorder";
		Map<String, String> prop = new HashMap<>();
		prop.put("gl", "us");
		prop.put("hl", "en");
		Document googlePage = Jsoup.connect(search)
				.userAgent(SEARCH_AGENT)
				.data(prop)
				.header("Accept-Language", "en")
				.header("Accept-Language", "en-US").get();
		File file = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\Clone_Wars\\clone.txt");
		List<NameInfo> nameInfoList = new ArrayList<>();
		System.out.println(googlePage);
		Elements tables = googlePage.getElementsByTag("table");
		System.out.println(tables);
		Element table = tables.first();
		System.out.println(table);
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
							nameInfo.setName("The Clone Wars");
							nameInfo.setIndex(orderNumber);
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
		saveMedia(json, nameInfoList);
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