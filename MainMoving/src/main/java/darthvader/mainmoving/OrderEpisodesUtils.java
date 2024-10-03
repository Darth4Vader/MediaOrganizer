package darthvader.mainmoving;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import DataStructures.FileInfo;
import DataStructures.FolderInfo;
import DataStructures.NameInfo;
import DataStructures.NameInfo.NameInfoType;
import DataStructures.FileInfoType.FolderType;

public class OrderEpisodesUtils {
	
	public static void setOrderForEpisodes(File jsonFile, File folder) throws IOException {
		List<NameInfo> nameInfoList = loadFile(jsonFile);
		List<FileInfo> fileInfos = new ArrayList<>();
		for(File file : folder.listFiles()) {
			FileInfo fileInfo = new FileInfo(file);
			if(file.isDirectory())
				fileInfos.add(fileInfo);
		}
		setOrderForEpisodes(fileInfos, nameInfoList);
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
	
	public static void setOrderForEpisodes2(List<FileInfo> fileInfos, List<NameInfo> episodesOrderList) {
		for(NameInfo nameInfo : episodesOrderList) {
			if(nameInfo.hasIndex() && nameInfo.hasName()) {
				FileInfo fileInfo = fileInfos.stream().filter(p -> 
							nameInfo.equalsBasedOnCriteria(p, NameInfoType.NAME, NameInfoType.SEASON, NameInfoType.EPISODE))
							.findFirst().orElse(null);
				/*FileInfo fileInfo = fileInfos.stream().filter(p -> 
							nameInfo.getName().equals(p.getName()) && nameInfo.getEpisode().equals(p.getEpisode()) && nameInfo.getSeason().equals(p.getSeason()))
							.findFirst().orElse(null);*/
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
	
	public static final String SEARCH_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
	
	public static Document loadPage(String searchPath) throws IOException {
		Map<String, String> prop = new HashMap<>();
		prop.put("gl", "us");
		prop.put("hl", "en");
		return Jsoup.connect(searchPath)
				.userAgent(SEARCH_AGENT)
				.data(prop)
				.header("Accept-Language", "en")
				.header("Accept-Language", "en-US").get();
	}
	
	public static void writeToFile(File file, String text) throws IOException {
    	FileWriter fileWriter = new FileWriter(file);
    	BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(text);
        writer.close();
	}

	public static List<NameInfo> loadFile(File file) throws IOException {
		ObjectMapper mapper = createPolymorphismMediaSimple();
		CollectionType type = TypeFactory.defaultInstance().constructCollectionType(List.class, NameInfo.class);
		return mapper.readValue(file, type);
	}
	
	public static void saveMedia(Object object, File destFolder, String destName) throws IOException {
		File jsonFile = new File(destFolder, destName+".json");
		saveMedia(object, jsonFile);
	}
	
	public static void saveMedia(Object object, File file) throws IOException {
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
