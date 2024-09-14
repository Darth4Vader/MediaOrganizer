package DataStructures;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import DataStructures.FileInfoType.FolderType;
import FileUtilities.FileFormats;
import FileUtilities.FileFormats.FileFormat;
import FileUtilities.FilesUtils;
import FileUtilities.MimeUtils;
import FileUtilities.MimeUtils.MimeContent;
import OtherUtilities.ImageUtils;
import OtherUtilities.JSONUtils;
import OtherUtilities.JSoupUtils;

public class ManageFolder {
	
	private final String urlParent;
	public Map<String, FolderInfo> movieMap = new HashMap<>();
	public Map<String, FolderInfo> TVMap = new HashMap<>();
	public Map<String, FolderInfo> unkownMediaMap = new HashMap<>();
	private List<ManageFile> sideMovesList;
	private boolean flagSearchMainFolderExists = true;
	
	public static final String DEFAULT_INPUT = "Input", DEFAULT_OUTPUT = "W-Output", ICONS = "Icons";
	public static final String OUTPUT_TV = "TV", OUTPUT_MOVIE = "Movies", OUTPUT_UNKOWN = "Media";
	
	
	public ManageFolder(String mainFolderPath) {
		this.urlParent = mainFolderPath;
		this.sideMovesList = new ArrayList<>();
		setMap(checkStartingPath(DEFAULT_OUTPUT));
	}
	
	public ManageFolder(String mainFolderPath, List<File> readDirectory) {
		this.urlParent = mainFolderPath;
		this.sideMovesList = new ArrayList<>();
		List<File> read = new ArrayList<>();
		for(File child : readDirectory) {
			String childPath = child.getAbsolutePath();
			boolean add = true;
			for(File directory : readDirectory) {
				if(childPath.startsWith(directory.getAbsolutePath()) && !childPath.equals(directory.getAbsolutePath())) {
					add = false;
					break;
				}
			}
			if(add)
				read.add(child);
		}
		System.out.println(read);
		
		if(read.contains(checkStartingPath(DEFAULT_OUTPUT))) {
			read.remove(checkStartingPath(DEFAULT_OUTPUT));
		}
		setMap(checkStartingPath(DEFAULT_OUTPUT));
		
		for(File file : read) {
			FolderType type = getMainFolderType(file);
			if(type != FolderType.NONE)
				addToMap(new FolderInfo(file), type);
			else {
				System.out.println("NONE: " + file);
				setMap(file);
			}
		}
	}
	
	public String getMainFolderPath() {
		return this.urlParent;
	}
	
	private void setMap(File folder) {
		File[] arr = folder.listFiles();
		System.out.println(folder + " to: " + arr);
		if(arr != null) for(File file : arr) {
			String fileName = file.getName();
			if(file.isDirectory()) {
				/*(fileName.equals("W-Keep") != true && fileName.equals("input") != true && fileName.equals("W-Input") != true && fileName.equals("Icons") != true
				&& fileName.equals("W-Output") != true && fileName.equals("Input") != true))//&& fileName.equals("w Movies") != true*/
				if(!file.isHidden() && 
						Arrays.asList(DEFAULT_OUTPUT, DEFAULT_INPUT, "W-Keep")
										.stream()
										.noneMatch(e -> e.equalsIgnoreCase(fileName))) {
					System.out.println(file);
					toAddInsideMap(file);
				}
			}
		}
	}
	
	public FolderType toAddInsideMap(File folder) {
		if(folder == null || !folder.isDirectory())
			return null;
		return toAddInsideMap(new FileInfo(folder));
	}
	
	public FolderType toAddInsideMap(FileInfo parentInfo) {
		File folder = parentInfo.getFile();
		FolderType typeOfFolder = parentInfo.getFolderType();
		FolderType typeByInfo = parentInfo.getFolderTypeByInfo();
		//System.out.println(typeOfFolder + " " +typeByInfo);
		if(typeOfFolder != FolderType.MAIN_FOLDER && typeOfFolder != FolderType.NONE) {
			return typeOfFolder;
		}
		else if(typeByInfo == FolderType.TV_EPISODE)
			return typeByInfo;
		File[] arr = folder.listFiles();
		FileInfo childInfo;
		Set<FolderType> types = new HashSet<>();
		if(arr != null) for(File file : arr) {
			String fileName = file.getName();
			if(file.isDirectory()) {
				FileInfo fileInfo = new FileInfo(file);
				FolderType childType = toAddInsideMap(fileInfo);
				if(folder.getName().contains("Bonus")) {
					System.out.println(folder.getName() + " " + childType);
				}
				if(childType != null) {
					if(parentInfo.isPartOf(fileInfo))
						types.add(childType);
				}
				/*childInfo = toAddInsideMap(file);
				if(childInfo != null) {
					FolderType typeOfChildFolder = childInfo.getFolderType();
					FolderType typeByChildInfo = childInfo.getFolderTypeByInfo();
					if(typeOfChildFolder != FolderType.MAIN_FOLDER && typeOfChildFolder != FolderType.NONE) {
						types.add(typeOfChildFolder);
					}
					else if(typeByChildInfo == FolderType.TV_EPISODE)
						types.add(typeByChildInfo);
					//types.add(typeOfChildFolder);
				}*/
				
				
				/*System.out.println(type + " " + file);
				System.out.println(info.getFolderTypeByInfo());
				System.out.println(type + " " + new FileInfo(file).getFolderType());*/
				
				
				
				/*
				if(!file.isHidden() && 
						Arrays.asList(DEFAULT_OUTPUT, DEFAULT_INPUT, "W-Keep")
										.stream()
										.noneMatch(e -> e.equalsIgnoreCase(fileName)))
						/*(fileName.equals("W-Keep") != true && fileName.equals("input") != true && fileName.equals("W-Input") != true && fileName.equals("Icons") != true
						&& fileName.equals("W-Output") != true && fileName.equals("Input") != true))//&& fileName.equals("w Movies") != true*/
				
				/*
						setMap(file.getAbsolutePath(), true);
				
				if(file.isDirectory())
					addToMap(file);*/
			}
		}
		if(types.isEmpty()) {
			return null;
		}
		//System.out.println(typeOfFolder + " " +typeByInfo);
		System.out.println(folder.getName() + " " + types);
		if(typeByInfo == FolderType.TV_SERIES) {
			return typeByInfo;
		}
		
		//here we add it to the map
		FolderType mainType = getFolderTypeFromChildTypes(types);
		if(mainType != FolderType.NONE)
			addToMap(new FolderInfo(folder), mainType);
		
		return null;
	}
	
	private FolderType getFolderTypeFromChildTypes(Set<FolderType> types) {
		if(types.containsAll(Arrays.asList(FolderType.TV_SERIES, FolderType.MOVIE)))
			return FolderType.TV_SERIES_AND_MOVIE;
		else if(types.containsAll(Arrays.asList(FolderType.TV_SERIES)))
			return FolderType.TV_SERIES;
		else if(types.containsAll(Arrays.asList(FolderType.MOVIE)))
			return FolderType.MOVIE;
		else if(types.containsAll(Arrays.asList(FolderType.TV_EPISODE)))
			return FolderType.MINI_SERIES;
		else if(types.containsAll(Arrays.asList(FolderType.EXTRAS)))
			return FolderType.MAIN_FOLDER;
		return FolderType.NONE;
	}
	
	private void addToMap(File file) {
		FolderInfo info = new FolderInfo(file);
		FolderType type = getMainFolderType(file);
		addToMap(info, type);
	}
	
	private void addToMap(FolderInfo info, FolderType type) {
		System.out.println(type + " Addddd: " + info.getFile());
		info.setFolderType(type);
		if(type == FolderType.TV_SERIES_AND_MOVIE) {
			movieMap.put(info.getMapName(), info);
			TVMap.put(info.getMapName(), info);
		}
		else
			this.getMainDefaultMap(type).put(info.getMapName(), info);
	}
	
	private File checkStartingPath(String path) {
		File file;
		if(path.startsWith(urlParent))
			file = new File(path);
		else
			file = new File(urlParent, path);
		if(!file.exists())
			file.mkdir();
		return file;
	}
	
	/**
	 * Returns The {@code FolderType} of a given {@code File}:
	 * MOVIE contains a movie folder.
	 * TV_SERIES contains at least one season.
	 * MINI_SERIES contains at least one episode.
	 * @param folder a given folder
	 * @return the {@code FolderType} if exists otherwise FolderType.UNKOWN_MEDIA
	 */
	private FolderType getMainFolderType(File folder) {
		if(folder.isDirectory()) for(File file : folder.listFiles()) {
			if(!file.isHidden() && !FilesUtils.isSystemFile(file)) {
				if(FileInfoType.getFolderType(file) == FolderType.MOVIE)
					return FolderType.MOVIE;
				else {
					FileInfo info = new FileInfo(file);
					if(info.hasSeason())
						return FolderType.TV_SERIES;
					else if(info.hasEpisode())
						return FolderType.MINI_SERIES;
				}
			}
			
		}
		return FolderType.NONE;
	}
	
	private Map<String, FolderInfo> getMainDefaultMap(FolderType type) {
		if(type == FolderType.TV_SERIES || type == FolderType.MINI_SERIES || type == FolderType.TV_EPISODE)
			return this.TVMap;
		if(type == FolderType.MOVIE)
			return this.movieMap;
		if(type == FolderType.NONE || type == FolderType.MAIN_FOLDER)
			return unkownMediaMap;
		return null;
	}
	
	/**
	 * Converts a MINI_SERIES to a TV_SERIES
	 * @param folderInfo a given folder info.
	 * @throws IOException {@link #Files.move(Path, Path)}
	 */
	private void changeMiniSeriesToTvSeries(FolderInfo folderInfo) throws IOException {
		File folder = folderInfo.getFile();
		FolderType folderType = folderInfo.getFolderType();
		if(folderType != FolderType.MINI_SERIES) return; /* throw new IllegalArgumentException("The folder is already a TV Series"); */
		folderInfo.setFolderType(FolderType.TV_SERIES);
		NameInfo seasonInfo = folderInfo.duplicateNameInfo().setSeason(FileInfo.firstSeason);
		File seasonFile = folderInfo.getFolderByType(seasonInfo, FolderType.TV_SERIES);
		if(seasonFile != null) /* because the mini series becomes the first season */
			return;
		seasonFile = folderInfo.createFolderByType(seasonInfo, FolderType.TV_SERIES); /* create inside the folder */
		if(folder.isDirectory()) for(File file : folder.listFiles())
			if(!file.equals(seasonFile) && file.isDirectory())
				Files.move(file.toPath(), new File(seasonFile, file.getName()).toPath());
		renameFiles(seasonFile, seasonInfo); /* rename every child */
	}
	
	/**
	 * Sets a given folder icon to a given icon only if the <code>FileInfo</code of 
	 * fullName of the 2 files are equal. 
	 * @param folder the destination folder to set the icon on
	 * @param file an icon file
	 */
	public List<Exception> setIconsToFolder() {
		File mainIconFolder = checkStartingPath(ICONS);
		if(!mainIconFolder.exists())
			mainIconFolder.mkdir();
		File[] arr = mainIconFolder.listFiles();
		List<Exception> list = new ArrayList<>();
		if(arr != null)
			for(File folder : arr) {
				if(folder.isDirectory()) {
					for(File file : folder.listFiles()) {
						if(!file.isDirectory()) {
							FileInfo info = new FileInfo(file);
							if(info.getFolderType() == FolderType.LOGO) {
								ManageFile manage = new ManageFile(info, FileOperation.SET_ICON);
								try {
									manage.printFileMoves();
								} catch (IOException | ActionAlreadyActivatedException e) {
									list.add(e);
								}
							}
						}
					}
				}
			}
		return list;
	}
	
	public static ObjectMapper getObjectMapperSerialization() {
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
		
		mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
		return mapper;
	}
	
	public static ObjectMapper getObjectMapperDeserialization() {
		ObjectMapper mapper = new ObjectMapper();
		mapper = JsonMapper.builder().build();
		
		
		
		mapper.setVisibility(mapper.getDeserializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
		//mapper.addMixIn(MediaSimple.class, MediaSimpleMixIn.class);
		//MapType type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, MediaSimple.class);
		
		
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		
		mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
		return mapper;
	}
	
	private FolderInformation loadFolderInformation(FolderInfo folderInfo, FileInfo fileInfo) {
		File informationFile = getFolderInformationFile(folderInfo, fileInfo);
		try {
			ObjectMapper mapper = getObjectMapperSerialization();
			setFolderInformationInjection(mapper, folderInfo);
			FolderInformation folderInformation = mapper.readValue(informationFile, FolderInformation.class);
			return folderInformation;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(informationFile);
		}
		return null;
	}
	
	private void setFolderInformationInjection(ObjectMapper mapper, FolderInfo folderInfo) {
		if(folderInfo != null) {
			InjectableValues inj = new InjectableValues.Std().addValue(FolderInfo.class, folderInfo);
			mapper.setInjectableValues(inj);
		}
	}
	
	private void createFolderInformation(FolderInfo folderInfo, FileInfo fileInfo, FolderInformation folderInformation) {
		File informationFolder = folderInfo.createFolderByType(fileInfo, FolderType.INFORMATION);
		File informationFile = new File(informationFolder, "information.json");
		try {
			ObjectMapper mapper = getObjectMapperDeserialization();
			setFolderInformationInjection(mapper, folderInfo);
			mapper.writeValue(informationFile, folderInformation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private File getFolderInformationFile(FolderInfo folderInfo, FileInfo fileInfo) {
		File informationFolder = folderInfo.getFolderByType(fileInfo, FolderType.INFORMATION);
		if(informationFolder == null)
			return null;
		File informationFile = new File(informationFolder, "information.json");
		return informationFile;
	}
	
	private String getPathFromFolder(FolderInfo folderInfo, File file) {
		File folder = folderInfo.getFile();
		Path filePath = file.toPath();
		Path folderPath = folder.toPath();
		if(filePath.startsWith(folderPath)) {
			System.out.println("Hello");
			return filePath.subpath(folderPath.getNameCount(), filePath.getNameCount()).toString();
		}
		return null;
	}
	
	private FolderInformation getFolderInformation(FolderInfo folderInfo, FileInfo fileInfo) {
		FolderInformation folderInformation = loadFolderInformation(folderInfo, fileInfo);
		if(folderInformation == null) {
			createFolderInformation(folderInfo, fileInfo, new FolderInformation());
			folderInformation = loadFolderInformation(folderInfo, fileInfo);
		}
		return folderInformation;
	}
	
	public void setIconToFolder(File folder, File poster) {
		FileInfo fileInfo = new FileInfo(folder);
		FolderInfo folderInfo = getMainFolderInPath(fileInfo);
		setIconToFolder(folderInfo, fileInfo, poster);
	}
	
	public void setIconToFolder(FolderInfo folderInfo, FileInfo fileInfo, File poster) {
		File fileToSetIcon = fileInfo.getFile();
		System.out.println("People: " + folderInfo);
		FolderInformation folderInformation = getFolderInformation(folderInfo, fileInfo);
		if(folderInformation != null) {
			Map<FileInfo, File> posterMap = folderInformation.getPostersOfFolders();
			System.out.println("JH: " + posterMap);
			if(posterMap == null) {
				posterMap = new HashMap<>();
				folderInformation.setPostersOfFolders(posterMap);
			}
			File originalPoster = null;
			for(Entry<FileInfo, File> entry : posterMap.entrySet()) {
				File entryFile = entry.getKey().getFile();
				if(entryFile.equals(fileToSetIcon)) {
					originalPoster = posterMap.get(entry.getKey());
					posterMap.remove(entry.getKey());
					break;
				}
			}
			boolean addNewPosterAsLogo = true;
			
			if(originalPoster != null) {
				FileInfo posterInfo = new FileInfo(originalPoster);
				File infoFolder = folderInfo.getFolderByType(fileInfo, FolderType.INFORMATION);
				File[] infoFiles = infoFolder.listFiles();
				if(infoFiles != null) for(File infoFile : infoFiles) {
					FileInfo infoFileInfo = new FileInfo(infoFile);
					if(infoFileInfo.getFolderType() == FolderType.LOGO) {
						if(infoFileInfo.equalsFullName(posterInfo)) {
							System.out.println("Got Me");
							addNewPosterAsLogo = false;
							break;
						}
					}
				}
			}
			FileInfo logo = isPosterOnListAsLogo(poster, folderInfo, fileInfo);
			if(logo != null) {
				System.out.println("hohohoho");
				ManageFile manage = getSetIcon(logo, fileToSetIcon);
				try {
					manage.printFileMoves();
				} catch (IOException | ActionAlreadyActivatedException e) {
					e.printStackTrace();
				}
			}
			else if(addNewPosterAsLogo) {
				System.out.println("Mad");
				FileInfo newPosterInfo = new FileInfo(poster);
				ManageFile manage = getCreateIcon(newPosterInfo, folderInfo, fileToSetIcon); 
				try {
					manage.printFileMoves();
				} catch (IOException | ActionAlreadyActivatedException e) {
					e.printStackTrace();
				}
			}
			posterMap.put(fileInfo, poster);
			if(originalPoster != null && !poster.equals(originalPoster) && posterMap.containsValue(originalPoster)) {
				System.out.println("Miss me");
			}
			System.out.println("OG " + originalPoster);
			System.out.println("End here");
			System.out.println(posterMap);
			createFolderInformation(folderInfo, fileInfo, folderInformation);
			System.out.println("Saved");
		}
	}
	
	private FileInfo isPosterOnListAsLogo(File poster, FolderInfo folderInfo, FileInfo fileInfo) {
		FileInfo posterInfo = new FileInfo(poster);
		File infoFolder = folderInfo.getFolderByType(fileInfo, FolderType.INFORMATION);
		File[] infoFiles = infoFolder.listFiles();
		if(infoFiles != null) for(File infoFile : infoFiles) {
			FileInfo infoFileInfo = new FileInfo(infoFile);
			if(infoFileInfo.getFolderType() == FolderType.LOGO) {
				if(infoFileInfo.equalsFullName(posterInfo)) {
					return infoFileInfo;
				}
			}
		}
		return null;
	}
	
	/*private boolean isIconInside() {
		
	}*/
	
	public void createIconToFolder() {
		for(FolderInfo folderInfo : movieMap.values()) {
			System.out.println(folderInfo);
			createIconToFolder(folderInfo, folderInfo);
		}
		for(FolderInfo folderInfo : TVMap.values()) {
			createIconToFolder(folderInfo, folderInfo);
		}
	}
	
	public void createIconToFolder(File file) {
		FileInfo fileInfo = new FileInfo(file);
		FolderInfo folderInfo = getMainFolderInPath(fileInfo);
		createIconToFolder(folderInfo, fileInfo);
	}
	
	public void createIconToFolder(FolderInfo folderInfo, FileInfo fileInfo) {
		System.out.println(folderInfo);
		File posterFolder = folderInfo.getFolderByType(fileInfo, FolderType.POSTERS);
		System.out.println(posterFolder);
		if(posterFolder != null) {
			File[] arr = posterFolder.listFiles();
			File poster = null;
			if(arr != null) {
				for(File child : arr) {
					System.out.println(child + " " + FileInfoType.getFolderType(child));
					if(FileInfoType.getFolderType(child) == FolderType.POSTERS) {
						poster = child;
						System.out.println("Good: " + child);
						break;
					}
				}
				if(poster == null)
					for(File child : arr) {
						System.out.println(child + " " + FileFormats.comapreFileFormats(child, FileFormat.IMAGE));
						if(FileFormats.comapreFileFormats(child, FileFormat.IMAGE)) {
							poster = child;
							System.out.println("Good: " + child);
							break;
						}
					}
				if(poster != null) {
					setIconToFolder(folderInfo, fileInfo, poster);
					System.out.println("Good");
				}
			}
		}
	}
	
	/*private static File getFile(String... paths) {
		String path = "", mainPath = "";
		for(int i = 0;i < paths.length; i++) {
			path = paths[i];
			if(i == paths.length-1) mainPath += path;
			else                    mainPath += FilesUtils.checkPath(path);
		}
		return new File(mainPath);
	}*/
	
	private String getDefaultOutputName(FolderType type) {
		if(type == FolderType.TV_SERIES || type == FolderType.TV_EPISODE || type == FolderType.MINI_SERIES)
			return OUTPUT_TV;
		if(type == FolderType.MOVIE)
			return OUTPUT_MOVIE;
		return OUTPUT_UNKOWN;
	}
	
	private File getDefaultOutputFolder(FolderType type) {
		String str = getDefaultOutputName(type);
		File folder = new File(checkStartingPath(DEFAULT_OUTPUT), str);
		if(!folder.exists())
			folder.mkdir();
		return folder;
	}
	
	private File createFolderInDefult(NameInfo info, FolderType type) {
		return getFolderByType(getDefaultOutputFolder(type), info.getName(), FolderType.MAIN_FOLDER, true);
	}
	
	public static File getFolderByType(File file, String fileName, FolderType type, boolean createIfNotExists) {
		return file == null ? null : getFolderByType(file.getAbsolutePath(), fileName, type, createIfNotExists);
	}
	
	private static File getFolderByType(String path, String fileName, FolderType type, boolean createIfNotExists) {
		String name = fileName + type.getFolderName();
		String iniType = null;
		switch(type) {
		case MAIN_FOLDER:
			iniType = FilesUtils.INI_TYPE_PICTURES;
			break;
		case EXTRAS:
			iniType = FilesUtils.INI_TYPE_PICTURES;
			break;
		case FEATURETTES:
			iniType = FilesUtils.INI_TYPE_VIDEOS;
			break;
		case POSTERS:
			iniType = FilesUtils.INI_TYPE_PICTURES;
			break;
		case CHARACTER_POSTERS:
			iniType = FilesUtils.INI_TYPE_PICTURES;
			break;
		case MOVIE:
			iniType = FilesUtils.INI_TYPE_VIDEOS;
			break;
		case TV_EPISODE:
			iniType = FilesUtils.INI_TYPE_VIDEOS;
			break;
		case TV_SERIES:
			iniType = FilesUtils.INI_TYPE_PICTURES;
			break;
		default:
			break;
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdir();
		}
		File file;
		if(fileName != null)
			file = new File(path, name);
		else
			file = new File(path);
		if(!createIfNotExists)
			return file.exists() ? file : null;
		else if(file.exists()) return file;
		return FilesUtils.createFolderWithType(path, name, iniType);
	}
	
	public void moveFilesFromInput() {
		moveFiless(DEFAULT_INPUT);
	}
	
	public void moveFiless(String path) {
		moveFiles(checkStartingPath(path), null);
	}
	
	private void moveFiles(String path, FileInfo parentInfo) {
		File parent = checkStartingPath(path);
		moveFiles(parent, parentInfo);
	}
	
	public void moveFiles(File parent, FileInfo parentInfo) {
		File[] arr = parent.listFiles();
		if(arr != null) {
			//organize input: season beafore featurettes and posters.
			List<File> first = new ArrayList<>();
			List<File> posters = new ArrayList<>();
			List<File> last = new ArrayList<>();
			for(File file : arr) {
				try {
					if(!FilesUtils.isSystemFile(file)) {
						FileInfo info = new FileInfo(file);
						FolderType type = info.getFolderType();
						if(type == FolderType.EXTRAS || type == FolderType.FEATURETTES 
								|| type == FolderType.CHARACTER_POSTERS) {
							last.add(file);
						}
						else if(type == FolderType.POSTERS || FileFormats.getFileFormat(file) == FileFormat.IMAGE) {
							posters.add(file);
						}
						else
							first.add(file);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			first.addAll(posters);
			first.addAll(last);
			//start reading and moving
			for(File file : first) {
				try {
					FileInfo info = new FileInfo(file);
					FolderType typeee = info.getFolderType();
					boolean canMoveFile = false;
					if(parentInfo != null) {
						FolderType type = parentInfo.getFolderType();
						FolderType subType = info.getFolderType();
						if(subType == FolderType.NONE)
							subType = info.getFolderTypeByInfo();
						if(type == FolderType.NONE)
							type = parentInfo.getFolderTypeByInfo();
						
						
						
						System.out.println(file);
						System.out.println(type + "  " + subType);
						System.out.println("Scobbbiiiii " + FolderInfo.hierarchy.isFolderTypeAscendingHierarchy(type, subType));
						
						info.setMissing(parentInfo);
						if(FolderInfo.hierarchy.isFolderTypeAscendingHierarchy(type, subType) && type != subType
								&& subType != FolderType.FEATURETTES && subType != FolderType.CHARACTER_POSTERS) {
							if(file.isDirectory())
								moveFiles(file.getAbsolutePath(), info);
							else
								canMoveFile = true;
						}
						else {
							if(FolderType.EXTRAS == type)
								System.out.println("Contttt");
							if(type == FolderType.EXTRAS) {
								FileFormat format = info.getFormat();
								if(format == FileFormat.VIDEO) {
									info = new FileInfo(file, parentInfo);
									info.setFolderType(FolderType.FEATURETTES);
								}
							}
							else if(type == FolderType.POSTERS) {
								FileFormat format = info.getFormat();
								if(format == FileFormat.IMAGE)
									info.setFolderType(FolderType.POSTERS);
							}
							/*type = parentInfo.getFolderType();
							if(type != FolderType.MAIN_FOLDER && type != FolderType.NONE)
								info.setFolderType(type);*/
							System.out.println("Billlll");
							System.out.println(info.getFolderType());
							canMoveFile = true;
						}
					}
					else if(file.isDirectory() && typeee != FolderType.FEATURETTES && typeee != FolderType.CHARACTER_POSTERS) {
						info.setFolderType(FolderType.MAIN_FOLDER);
						moveFiles(file.getAbsolutePath(), info);
					}
					else
						canMoveFile = true;
					if(canMoveFile) {
						ManageFile moveFile = new ManageFile(info, FileOperation.MOVE);
						moveFile.printFileMoves();
					}
					//removeTrashFiles(file);
				}
				catch(Exception exp) {
					exp.printStackTrace();
				}
			}
		}
	}
	
	public static boolean isTrashFile(String name) {
		final String type = MimeUtils.getExtension(name);
		if(type.equals("ini"))
			return false;
		if(type.equals("dat")) {
			if(name.startsWith("~BitTorrentPartFile"))
				return true;
		}
		return false;
	}
	
	public static void removeTrashFiles(File file) {
		boolean b = false;
		if(file.isDirectory()) {
			File[] children = file.listFiles();
			b = children != null && children.length == 0;
		}
		else
			b = isTrashFile(file.getName());
		if(b) {
			Desktop.getDesktop().moveToTrash(file);	
		}
	}
	
	/**
	 * This method is similar to {@link #getMainFolder(FileInfo, boolean)} {@inheritDoc}, with the following differences:
	 * The {@code createIfMissing} is false
 	 * @see #getMainFolder(FileInfo, boolean)
	 * @param nameInfo {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws FlagSearchMainFolderExeception {@inheritDoc}
	 */
	public FolderInfo getMainFolder(NameInfo nameInfo) throws FlagSearchMainFolderExeception {
		return getMainFolder(nameInfo, false);
	}
	
	/**
	 * Returns the matching FileInfo in the system to a given FileInfo.
	 * The matching is found in the format NAME_YEAR_SEASON in small letters.
	 * @param nameInfo
	 * @return the FolderInfo that is used for the NameInfo.
	 * @throws FlagSearchMainFolderExeception if the matching FolderInfo does not exists, and there are matching FolderInfos
	 */
	public FolderInfo getMainFolder(NameInfo nameInfo, boolean createIfMissing) throws FlagSearchMainFolderExeception {
		FolderType folderInfoType = nameInfo.getFolderTypeByInfo();
		FolderInfo mainInfo = null;
		Map<String, FolderInfo> mainMap;
		String mapName = nameInfo.getMapName();
		FolderType fileType = nameInfo.getFolderType();
		//if the NameInfo contains season or episode then it can be in 2 maps: the TVMap or unkownMediaMap
		if(folderInfoType == FolderType.TV_EPISODE || folderInfoType == FolderType.TV_SERIES) {
			//check if it is in the TVMap
			mainMap = TVMap;
			mainInfo = mainMap.get(mapName);
			if(folderInfoType == FolderType.TV_EPISODE || folderInfoType == FolderType.TV_SERIES) {
				if(nameInfo.compareSeasons(FileInfo.firstSeason) || !nameInfo.hasSeason())
					folderInfoType = FolderType.MINI_SERIES;
				else
					folderInfoType = FolderType.TV_SERIES;
			}
		}
		else {
			mainMap = movieMap;
			mainInfo = mainMap.get(mapName);
			if(fileType == FolderType.MOVIE)
				folderInfoType = fileType;
			else {//this is for things like: posters, featurettes, etc.s
				folderInfoType = FolderType.NONE;
				//same as before, let's see it for posters: a poster can be for a movie, tv series, individual tv season, or for an unknown media.
				//if a poster have season or episode, it can't be in the movieMap, and can only be found in TVMap or unknownMediaMap
				if(!mainMap.containsKey(mapName)) { //if not in movieMap, check if in TVMap.
					if(fileType == FolderType.POSTERS || fileType == FolderType.CHARACTER_POSTERS
							|| fileType == FolderType.FEATURETTES) {
						mainMap = TVMap;
						mainInfo = mainMap.get(mapName);
					}
				}
			}
		}
		List<FolderInfo> listOfMatching = new ArrayList<>();
		
		System.out.println("NOt y1: " + !mainMap.containsKey(mapName));
		
		if(!mainMap.containsKey(mapName)) {
			//the last map that can contain the media is in the unkownMediaMap.
			Map<String, FolderInfo> checkMap = unkownMediaMap;
			mainInfo = checkMap.get(mapName);
			
			System.out.println(checkMap);
			System.out.println(mapName);
			System.out.println("NOt y: " + checkMap.containsKey(mapName));
			
			
			if(!checkMap.containsKey(mapName)) {
				//if the NameInfo is not inside any map, check for all the matching ones: like: same name without year (if 2 have different years, then it is different)
				listOfMatching.addAll(checkMatchingFolderInfo(nameInfo, mainMap));
				if(folderInfoType == FolderType.NONE) {
					Map<String, FolderInfo> secondCheck = TVMap;
					if(mainMap == TVMap)
						secondCheck = movieMap;
					listOfMatching.addAll(checkMatchingFolderInfo(nameInfo, secondCheck));
				}
				listOfMatching.addAll(checkMatchingFolderInfo(nameInfo, checkMap));
				if(mainInfo == null) {
					//if there are matching FolderInfos to the NameInfo, throw an exception.
					if(listOfMatching.isEmpty()) {
						//create the new NameInfo in the folderInfoType map only if is is requested.
						if(createIfMissing) {
							File mainFolder = createFolderInDefult(nameInfo, folderInfoType);
							mainInfo = new FolderInfo(mainFolder);
							addToMap(mainInfo, folderInfoType);
						}
						else
							mainInfo = null;
					}
					else { /* flagSearchMainFolderExists */
						throw new FlagSearchMainFolderExeception(listOfMatching);
					}
				}
			}
			else {
				
				System.out.println(folderInfoType);
				
				
				//if the FolderInfo is unkown, and the current type is valid for changing: move from unkownMediaMap to the folderInfoType map
				if(folderInfoType != FolderType.NONE) {
					mainInfo.setFolderType(folderInfoType);
					File mainFolder = mainInfo.getFile();
					checkMap.remove(mapName);
					if(inDefult(mainFolder)) { //change to new default folder. 
						File destTypeFolder = getDefaultOutputFolder(folderInfoType);
						File moveTo = new File(destTypeFolder, mainFolder.getName());
						mainFolder.renameTo(moveTo);
						mainInfo.setFileInformation(moveTo);
					}
					mainMap.put(mapName, mainInfo);
				}
			}
		}
		
		System.out.println();
		System.out.println("PIMP " + mainInfo.getFile());
		
		
		return mainInfo;
	}
	
	/**
	 * Return all the FolderInfo that have a matching name to the given NameInfo, one of the following:<p> 
	 * NAME == GIVEN_NAME<br> 
	 * NAME (!hasYear) == GIVEN_NAME (hasYear)<br> 
	 * NAME (hasYear) == GIVEN_NAME (!hasYear)<br> 
	 * NAME_YEAR == GIVEN_NAME_YEAR<br> 
	 * @param nameInfo
	 * @param map
	 * @return all the FolderInfo that have a matching name to the given NameInfo.
	 */
	private List<FolderInfo> checkMatchingFolderInfo(NameInfo nameInfo, Map<String, FolderInfo> map) {
		List<FolderInfo> list = new ArrayList<>();
		if(flagSearchMainFolderExists) {
			String name = NameInfo.getMapNameFormat(nameInfo.getNameWithoutYear());
			for(FolderInfo info : map.values()) {
				String infoName = NameInfo.getMapNameFormat(info.getNameWithoutYear());
				if(name.equalsIgnoreCase(infoName))
					if(!(info.hasYear() && nameInfo.hasYear()) || (info.getYear().equals(nameInfo.getYear())))
						list.add(info);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param nameInfo
	 * @param createIfMissing
	 * @return
	 * @throws FlagSearchMainFolderExeception 
	 * @throws IOException 
	 */
	private void confingInfoFromFolderInfo(FolderInfo folderInfo, NameInfo nameInfo, boolean createIfMissing) throws FlagSearchMainFolderExeception, IOException {
		//add year to the FolderInfo, if the NameInfo has Year and the FolderInfo does not
		if(nameInfo.hasYear() && !folderInfo.hasYear()) {
			renameFiles(folderInfo, folderInfo.duplicateNameInfo().setYear(nameInfo.getYear()));
		}
		nameInfo.renameNameInfo(folderInfo);
		if(!nameInfo.hasSeason() && !nameInfo.hasEpisode())
			return;
		FolderType type = folderInfo.getFolderType();
		if(type == FolderType.MINI_SERIES) {
			if(!nameInfo.hasSeason() || nameInfo.compareSeasons(FileInfo.firstSeason))
				nameInfo.removeSeason(); //MINI_SERIES does not have a season
			else if(createIfMissing) {
				// set mini series to season 1 and add new season 
				changeMiniSeriesToTvSeries(folderInfo);
			}
		}
		else if(type == FolderType.TV_SERIES) {
			if(!nameInfo.hasSeason() && nameInfo.hasEpisode())
				nameInfo.setSeason(FileInfo.firstSeason); //inside TV_SERIES, names with only episode and without season are inside season 1.
		}
	}
	
	/**
	 * {@return the FolderInfo that is the folder ancestor of the given FileInfo file}
	 * @param info a given FileInfo.
	 */
	private FolderInfo getMainFolderInPath(FileInfo info) {
		File file = info.getFile();
		FolderInfo mainFolderInfo = null;
		try {
			FolderInfo folderInfo = getMainFolder(info);
			if(fileInFolderInfo(folderInfo, file))
				mainFolderInfo = folderInfo;
		}
		catch (FlagSearchMainFolderExeception e) {
			List<FolderInfo> list = e.getList();
			for(FolderInfo folderInfo : list)
				if(fileInFolderInfo(folderInfo, file)) {
					mainFolderInfo = folderInfo;
					break;
				}
		}
		return mainFolderInfo;
	}
	
	/** 
	 * {@return whether a given FolderInfo is an ancestor of a given file}
	 * @param folderInfo the FolderInfo of the ancestor folder.
	 * @param file a given file.
	 */
	private boolean fileInFolderInfo(FolderInfo folderInfo, File file) {
		boolean bol = false;
		if(folderInfo != null && file != null) {
			File folder = folderInfo.getFile();
			if(folder != null)
				bol = file.getAbsolutePath().startsWith(folder.getAbsolutePath());
		}
		return bol;
	}
	
	private boolean inDefult(File file) {
		return file.getAbsolutePath().contains(DEFAULT_OUTPUT);
	}
	
	public class FlagSearchMainFolderExeception extends Exception {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final List<FolderInfo> list;
		
		public FlagSearchMainFolderExeception(List<FolderInfo> list) {
			this.list = list;
		}
		
		public List<FolderInfo> getList() {
			return list;
		}
	}
	
	public static FileInfo getEpisodeFolder(File folder, NameInfo info) {
		return getFolderByFolderType(folder, info, FolderType.TV_EPISODE);
	}
	
	private static FileInfo getFolderByFolderType(File folder, NameInfo info, FolderType type) {
		File[] arr = folder.listFiles();
		if(arr != null)
			for(File file : arr) {
				if(file.isDirectory()) {
					if(type == FolderType.TV_EPISODE) {
						FileInfo fileInfo = new FileInfo(file);
						if(info.isPartOf(fileInfo)) {
							if(fileInfo.isEpisode(info.getSeason(), info.getEpisode())) {
								return fileInfo;
							}
						}						
					}
				}
			}
		return null; 
	}
	
	/**
	 * Renames a given fileInfo folder similar to {@link #renameFiles(File, NameInfo)}, <br>
	 * and updates the FolderInfo with the given NameInfo.
	 * @param info the fileInfo to update its file using itself (The NameInfo can be different from the original)
	 * @return the renamed given folder.
	 * @throws IOException as described in {@link #_renameFile(File, NameInfo)}
	 */
	public File renameFiles(FileInfo info) throws IOException {
		File folder = renameFiles(info.getFile(), info);
		info.setFileInformation(folder);
		return folder;
	}
	
	/**
	 * Renames a given file similar to {@link #_renameFilesWithRootDirectory(File, NameInfo)}, <br>
	 * and updates the FolderInfo with the given NameInfo.
	 * @param rootFile the file to rename from.
	 * @param newNameInfo the new NameInfo to update the FileInfo of the files to rename.
	 * @return the renamed given folder.
	 * @throws IOException as described in {@link #_renameFile(File, NameInfo)}
	 */
	public File renameFiles(File rootFile, NameInfo newNameInfo) throws IOException {
		FileInfo fileInfo = new FileInfo(rootFile);
		FolderInfo folderInfo = getMainFolderInPath(fileInfo);
		File folder = null;
		if(folderInfo != null) {
			if(rootFile.equals(folderInfo.getFile()))
				folder = renameFiles(folderInfo, newNameInfo);
		}
		if(folder == null)
			folder = _renameFilesWithRootDirectory(rootFile, newNameInfo);
		return folder;
	}
	
	/**
	 * Renames a given folderInfo folder similar to {@link #_renameFilesWithRootDirectory(File, NameInfo)}, <br>
	 * and updates the FolderInfo with the given NameInfo.
	 * @param folderInfo the given FolderInfo to rename.
	 * @param newNameInfo the new NameInfo to update the FileInfo of the files to rename.
	 * @return the renamed given folder.
	 * @throws IOException as described in {@link #_renameFile(File, NameInfo)}
	 */
	public File renameFiles(FolderInfo folderInfo, NameInfo newNameInfo) throws IOException {
		String orignalMapName = folderInfo.getMapName();
		File folder = folderInfo.getFile();
		folder = _renameFilesWithoutRootDirectory(folder, newNameInfo);
		folder = _renameFile(folderInfo, newNameInfo);
		replaceFolderInfo(folderInfo, orignalMapName);
		return folder;
	}
	
	/**
	 * Rename the given file, and all of its descendants as described in {@link #_renameFilesWithoutRootDirectory(File, NameInfo)}
	 * @param file a given file to rename from.
	 * @param newNameInfo the new NameInfo to update the FileInfo of the files to rename.
	 * @return the renamed given file.
	 * @throws IOException as described in {@link #_renameFile(File, NameInfo)}
	 */
	private File _renameFilesWithRootDirectory(File file, NameInfo newNameInfo) throws IOException {
		file = _renameFilesWithoutRootDirectory(file, newNameInfo);
		return _renameFile(file, newNameInfo);
	}
	
	/**
	 * Renames all the children of a given folder, and renames all of their descendants files. <br>
	 * The renaming is like renaming a tree (data structure) without it's root. <br>
	 * The File renaming is used with the function {@link #_renameFile(File, NameInfo)}
	 * @param folder a given folder to rename its descendants.
	 * @param newNameInfo the new NameInfo to update the FileInfo of the files to rename.
	 * @return the given file.
	 * @throws IOException as described in {@link #_renameFile(File, NameInfo)}
	 */
	private File _renameFilesWithoutRootDirectory(File folder, NameInfo newNameInfo) throws IOException {
		if(folder.isDirectory()) for(File file : folder.listFiles()) {
			if(file.isDirectory()) {
				FolderType type = FileInfoType.getFolderType(file);
				if(type != FolderType.FEATURETTES && type != FolderType.CHARACTER_POSTERS)
					_renameFilesWithRootDirectory(file, newNameInfo);
			}
			else {
				FileFormat format = FileFormats.getFileFormat(file);
				if(format == FileFormat.UNKOWN)
					continue;
			}
			_renameFile(file, newNameInfo);
		}
		return folder;
	}
	
	/**
	 * Renames the given File to a given NameInfo, that is the updated FileInfe of the File getFullName() name. <br>
	 * if the file is a folder, and it has a logo as an icon, renames the icon name to the new file name. 
	 * @param file a given file to rename.
	 * @param newNameInfo the new NameInfo to update the FileInfo and the file to.
	 * @return the renamed given file.
	 * @throws IOException when the logo name cannot be updated in the ini file of the folder (As seen above).
	 */
	private File _renameFile(File file, NameInfo newNameInfo) throws IOException {
		if(FilesUtils.isSystemFile(file))
			return file;
		return _renameFile(new FileInfo(file), newNameInfo);
	}
	
	/**
	 * Renames the given FileInfo to a given NameInfo, and rename the File of the FileInfo to it's new getFullName() name. <br>
	 * if the file is a folder, and it has a logo as an icon, renames the icon name to the new file name. 
	 * @param fileInfo the FileInfo of the given file
	 * @param newNameInfo the new NameInfo to update the FileInfo and the file to.
	 * @return the renamed given file.
	 * @throws IOException when the logo name cannot be updated in the ini file of the folder (As seen above).
	 */
	private File _renameFile(FileInfo fileInfo, NameInfo newNameInfo) throws IOException {
		File file = fileInfo.getFile();
		if(FilesUtils.isSystemFile(file))
			return file;
		fileInfo.renameNameInfo(newNameInfo);
		String name = fileInfo.getFullNameWithMime();
		File newFile = new File(file.getParent(), name);
		file.renameTo(newFile);
		fileInfo.setFileInformation(newFile);
		if(newFile.isDirectory()) {
			File folderIcon = FilesUtils.getFileLogo(newFile);
			if(folderIcon != null) {
				File dir = folderIcon.getParentFile();
				File mainIconFolder = dir.getParentFile();
				if(mainIconFolder.equals(checkStartingPath(ICONS))) {
					FileInfo iconInfo = new FileInfo(folderIcon);
					iconInfo.renameNameInfo(newNameInfo);
					File newIcon = new File(folderIcon.getParent(), iconInfo.getFullNameWithMime());
					folderIcon.renameTo(newIcon);
					FilesUtils.setIniFileIcon(newFile, newIcon);
				}
			}
		}
		return newFile;
	}
	
	/**
	 * Updates a given FolderInfo place in its map from its old map name to its new one.
	 * @param folderInfo a given FolderInfo.
	 * @param oldMapName the current map name of the FolderInfo.
	 */
	private void replaceFolderInfo(FolderInfo folderInfo, String oldMapName) {
		FolderType type = folderInfo.getFolderType();
		Map<String, FolderInfo> map = getMainDefaultMap(type);
		String newMapName = folderInfo.getMapName();
		if(map.containsKey(oldMapName)) {
			map.remove(oldMapName);
			map.put(newMapName, folderInfo);
		}
	}
	
	public static void oragnizeRARBG(String filePath, String outputPath) {
		try {
			File mainFile = new File(filePath);
			if(!mainFile.exists())
				return;
			File outputFolder = new File(outputPath);
			outputFolder.mkdir();
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
								Files.move(firstFile.toPath(), new File(outputFolder, newName).toPath());
								//Files.move(Paths.get(firstFile.getAbsolutePath()), Paths.get(FilesUtils.checkPath(outputPath)+newName));
							}
						}
					}
					else {
						if(MimeUtils.isMimeContentGroup(file, MimeContent.VIDEO))
							Files.move(file.toPath(), new File(outputFolder, file.getName()).toPath());
					}
				}
				catch(Exception exp) {exp.printStackTrace();}
			}
		}
		catch(Exception exp) {exp.printStackTrace();}
	}
	
	
	private static String getFolderTypeExceptionMessage(FolderType type) {
		String message = "the FolderType %s cannot be used for this operation";
		String str  = String.format(message, type.toString());
		return str;
	}
	
	public static enum FileOperation {
		MOVE,
		RENAME,
		CREATE_ICON,
		SET_ICON,
		REPLACE_ICON;
	}
	
	public class FileOperationHandler {
		
		private File sourceFile;
		private String destPath;
		private ManageFile moveFileInfo;
		private FileOperation action;
		private boolean isFinished;
		
		public FileOperationHandler(ManageFile moveFile, File sourceFile, String destPath) {
			this(sourceFile, destPath);
			this.moveFileInfo = moveFile;
		}
		
		public FileOperationHandler(ManageFile moveFile, File sourceFile, String destPath, FileOperation action) {
			this(sourceFile, destPath);
			this.moveFileInfo = moveFile;
			this.action = action;
		}
		
		public FileOperationHandler(File sourceFile, String destPath) {
			this(sourceFile, destPath, FileOperation.MOVE);
		}
		
		public FileOperationHandler(File sourceFile, String destPath, FileOperation action) {
			this.sourceFile = sourceFile;
			this.destPath = destPath;
			this.action = action;
			this.isFinished = false;
			this.moveFileInfo = null;
		}
		
		private FolderInfo getFolderInfo() {
			return moveFileInfo != null ? moveFileInfo.getFolderInfo() : null;
		}
		
		private boolean hasMoveFileInfo() {
			return this.moveFileInfo != null;
		}

		public synchronized void activateAction() throws IOException, ActionAlreadyActivatedException {
			if(isFinished)
				throw new ActionAlreadyActivatedException();
			switch (action) {
			case CREATE_ICON: createIcon();
				break;
			case MOVE: move();
				break;
			case REPLACE_ICON: replaceIcon();
				break;
			case SET_ICON: setIcon();
				break;
			default:
				break;
			}
			this.isFinished = true;
			System.out.println("Green");
			System.out.print("\u001B[32m");
			System.out.print(sourceFile + " (" + action + ") -> " + destPath);
			System.out.print("\u001B[0m");
			System.out.println();
		}
		
		private void move() throws IOException {
			Files.move(sourceFile.toPath(), Paths.get(destPath));
			logoActions();
		}
		
		/**
		 * Creates an icon from the sourceFile only if is part of the IMAGE FileFormats format.
		 * @throws IOException
		 */
		private void createIcon() throws IOException {
			if(moveFileInfo.canDoIconActions()) {
				File fil = ImageUtils.createIconFromFile(sourceFile, destPath);
				System.out.println(fil);
				if(fil != null) {
					System.out.println("Goodd");
					sideMovesList.add(new ManageFile(fil, FileOperation.MOVE, getFolderInfo()));
				}
				else {
					
				}	
			}			
		}
		
		private void setIcon() throws IOException {
			if(moveFileInfo.canDoIconActions()) {
				FilesUtils.setIniFileIcon(new File(destPath), sourceFile);
			}
		}
		
		private void replaceIcon() throws IOException {
			File icon = FilesUtils.removeFileLogo(sourceFile);
			if(icon != null)
				removeTrashFiles(icon);
			logoActions();
		}
		
		private void logoActions() {
			System.out.println("Dest Path: " + destPath);
			if(hasMoveFileInfo()) {
				if(moveFileInfo.moveType == FolderType.POSTERS)
					sideMovesList.add(new ManageFile(new File(destPath), FileOperation.CREATE_ICON, getFolderInfo()));
				if(moveFileInfo.moveType == FolderType.LOGO)
					sideMovesList.add(new ManageFile(new File(destPath), FileOperation.SET_ICON, getFolderInfo()));
			}
		}
		
	}
	
	
	/*public ManageFile getMove(String path, FileOperation action) throws FileNotFoundException {
		return new ManageFile(new File(path), action);
	}*/
	
	public ManageFile getSetIcon(FileInfo sourceIconInfo, File destFolder) {
		ManageFile manage = new ManageFile(sourceIconInfo, FileOperation.SET_ICON);
		manage.iconFolder = destFolder;
		return manage;
	}
	
	public ManageFile getCreateIcon(FileInfo sourceIconInfo, FolderInfo folderInfo, File destFolder) {
		ManageFile manage = new ManageFile(sourceIconInfo, FileOperation.CREATE_ICON, folderInfo);
		manage.iconFolder = destFolder;
		return manage;
	}
	
	public class ManageFile {
		
		//private FileInfo movedFile; //can be a featurette video
		private FileInfo sorceFileInfo;
		private FileInfo destFileInfo;
		private FolderInfo folderInfo;
		private FolderType moveType;
		private FileOperation action;
		private FlagSearchMainFolderExeception exp;
		private List<FileOperationHandler> moveList;
		private File iconFolder;
		private boolean valid;
		
		public ManageFile(File sourceFile, FileOperation action) {
			this(new FileInfo(sourceFile), action);
		}
		
		public ManageFile(File sourceFile, FileOperation action, FolderInfo folderInfo) {
			this(new FileInfo(sourceFile), action, folderInfo);
		}
		
		public ManageFile(FileInfo sourceFileInfo, FileOperation action) {
			initilize(sourceFileInfo, action, true);
			searchFolderInfo(true);
		}
		
		/*public ManageFile(FileInfo moveFile, FileOperation action, FileInfo infoFile) {
			initilize(info, action, true);
			searchFolderInfo(true);
		}*/
		
		public ManageFile(FileInfo sourceFileInfo, FileOperation action, FolderInfo folderInfo) {
			this.folderInfo = folderInfo;
			initilize(sourceFileInfo, action, true);
			if(folderInfo == null)
				searchFolderInfo(true);
		}
		
		private void initilize(FileInfo sourceFileInfo, FileOperation action, boolean validate) {
			this.valid = true;
			this.moveList = new ArrayList<>();
			this.sorceFileInfo = sourceFileInfo;
			this.moveType = sorceFileInfo.getFolderType();
			this.action = action;
			if(validate) {
				if(moveType == FolderType.NONE) {
					if(FileFormats.getFileFormat(sourceFileInfo.getFile()) == FileFormat.IMAGE) {
						System.out.println("Spedd: " + folderInfo);
						if(folderInfo == null)
							searchFolderInfo(false);
						System.out.println("Spedd: " + folderInfo);
						if(exp != null || folderInfo != null) {
							sorceFileInfo.setFolderType(FolderType.POSTERS);
							this.moveType = sorceFileInfo.getFolderType();
						}
					}
				}
			} 
			if(moveType == FolderType.NONE)
				this.valid = false;	
		}
		
		private void searchFolderInfo(boolean createIfMissing) {
			if(valid) {
				try {
					this.folderInfo = getMainFolder(sorceFileInfo, createIfMissing);
					if(folderInfo == null)
						this.valid = false;
				} catch (FlagSearchMainFolderExeception e) {
					this.folderInfo = null;
					this.exp = e;
					List<FolderInfo> list = e.getList();
					if(list.size() == 1)
						this.folderInfo = list.get(0);
					else
						this.valid = false;
				}
			}
		}
		
		public FolderInfo getFolderInfo() {
			return this.folderInfo;
		}
		
		private void checkExists(File file) throws FileNotFoundException {
			String message = String.format("The file %s doesn't exists", file);
			if(file == null || !file.exists())
				throw new FileNotFoundException(message);
		}
		
		public void createMoveInfo() throws IOException {
			if(!valid)
				return;
			if(this.folderInfo != null)
				try {
					confingInfoFromFolderInfo(folderInfo, sorceFileInfo, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println("Start");
			//System.out.println(fileInfo.);
			System.out.println(sorceFileInfo.getFolderType());
			if(action == FileOperation.MOVE) {
				System.out.println(moveType);
				switch (moveType) {
				case CHARACTER_POSTERS: moveExtras();
					break;
				case EXTRAS:
					break;
				case FEATURETTES: moveExtras();
					break;
				case LOGO: moveIcon();
					break;
				case MAIN_FOLDER:
					break;
				case MINI_SERIES:
					break;
				case MOVIE: moveMedia();
					break;
				case NONE:
					break;
				case POSTERS: moveExtras();
					break;
				case TV_EPISODE: moveMedia();
					break;
				case TV_SERIES:
					break;
				default:
					break;
				}
			}
			else if(action == FileOperation.CREATE_ICON) {
				if(moveType == FolderType.POSTERS) {
					createIcon();
				}
			}
			else if(action == FileOperation.SET_ICON) {
				if(moveType == FolderType.LOGO) {
					setIcon();
				}
			}
		}
		
		private void moveExtras() throws IllegalArgumentException {
			if(moveType != FolderType.FEATURETTES && moveType != FolderType.CHARACTER_POSTERS
					&& moveType != FolderType.POSTERS) {
				throw new IllegalArgumentException(getFolderTypeExceptionMessage(moveType));
			}
			File file = sorceFileInfo.getFile();
			File destFolder = folderInfo.createFolderByType(sorceFileInfo, moveType);
			List<File> arr = file.isDirectory() ? Arrays.asList(file.listFiles()) : Arrays.asList(file);
			if(moveType == FolderType.POSTERS && !file.isDirectory()) {
				System.out.println("See This: " + sorceFileInfo.getFullNameWithMime());
				FileOperationHandler action = new FileOperationHandler(this, file, new File(destFolder, sorceFileInfo.getFullNameWithMime()).getAbsolutePath());
				moveList.add(action);
			}
			else for(File child : arr) {
				FileInfo childInfo = new FileInfo(child);
				System.out.println("Child:    " + child);
				System.out.println(FileFormats.getFileFormat(child));
				System.out.println(childInfo.getFolderType());
				childInfo.setFolderType(moveType);
				System.out.println(childInfo.getFolderType());
				if(childInfo.getFolderType() == moveType) {
					//if(child)
					FileOperationHandler action = new FileOperationHandler(this, child, new File(destFolder, child.getName()).getAbsolutePath());
					moveList.add(action);
				}
			}
		}
		
		private void moveMedia() throws IOException {
			File file = sorceFileInfo.getFile();
			System.out.println("ffffffff " + folderInfo.getFile());
			File mediaFolder = folderInfo.createFolderByType(sorceFileInfo, moveType);
			if(moveType == FolderType.TV_EPISODE) {
				mediaFolder = renameFiles(mediaFolder, sorceFileInfo);
			}
			if(mediaFolder != null) {
				String finalPath = new File(mediaFolder, sorceFileInfo.getFullNameWithMime()).getAbsolutePath();
				moveList.add(new FileOperationHandler(file, finalPath));
			}
		}
		
		private void moveIcon_old() {
			File mainIconFolder = checkStartingPath(ICONS);
			setIconFolder();
			if(canDoIconActions()) {
				File iconFolder = new File(mainIconFolder, folderInfo.getName());
				if(!iconFolder.exists())
					iconFolder.mkdir();	
				String name = MimeUtils.createNameWithMime(folderInfo.getNameSeason(sorceFileInfo) + FolderType.LOGO.getFolderName(), MimeUtils.MIME_ICON);
				String destPath = new File(iconFolder, name).getAbsolutePath();
				moveList.add(new FileOperationHandler(this, sorceFileInfo.getFile(), destPath, FileOperation.MOVE));
			}
		}
		
		private void moveIcon() {
			setIconFolder();
			System.out.println("Hello icon");
			System.out.println(FilesUtils.getFileLogo(iconFolder));
			if(canDoIconActions()) {
				File mainIconFolder = folderInfo.createFolderByType(sorceFileInfo, FolderType.INFORMATION);
				String name = MimeUtils.createNameWithMime(folderInfo.getNameSeason(sorceFileInfo) + FolderType.LOGO.getFolderName(), MimeUtils.MIME_ICON);
				String destPath = new File(mainIconFolder, name).getAbsolutePath();
				moveList.add(new FileOperationHandler(this, sorceFileInfo.getFile(), destPath, FileOperation.MOVE));
			}
		}
		
		private void createIcon() {
			File iconFolder = checkStartingPath(DEFAULT_INPUT);
			setIconFolder();
			if(canDoIconActions()) {
				String destFile = folderInfo.getFullNameWithMime(sorceFileInfo, FolderType.LOGO, MimeUtils.MIME_ICON);
				String destPath = new File(iconFolder, destFile).getAbsolutePath();
				moveList.add(new FileOperationHandler(this, sorceFileInfo.getFile(), destPath, FileOperation.CREATE_ICON));
			}
		}
		
		private void setIcon() {
			setIconFolder();
			if(canDoIconActions()) {
				String relativeIconPath = FolderInformation.getPathFromFolder(folderInfo.getFile(), sorceFileInfo.getFile());
				String relativeFolderPath = FolderInformation.getPathFromFolder(folderInfo.getFile(), iconFolder);
				if(relativeIconPath != null && relativeFolderPath != null) {
					Path relativePath = Paths.get(relativeFolderPath).relativize(Paths.get(relativeIconPath));
					System.out.println("Reli: " + relativeIconPath + " | " + relativeFolderPath + " | " + relativePath);
					System.out.println(new File(relativeFolderPath).toPath().relativize(new File(relativeIconPath).toPath()));
					moveList.add(new FileOperationHandler(this, new File(relativePath.toString()), this.iconFolder.getAbsolutePath(), FileOperation.SET_ICON));
				}
				else
					moveList.add(new FileOperationHandler(this, sorceFileInfo.getFile(), this.iconFolder.getAbsolutePath(), FileOperation.SET_ICON));
			}
		}
		
		public boolean canDoIconActions() {
			return !FilesUtils.fileHaveLogo(iconFolder);
		}
		
		private void setIconFolder() {
			if(this.iconFolder != null) return;
			FolderType type = sorceFileInfo.getFolderType();
			if(type == FolderType.LOGO) {
				type = sorceFileInfo.getFolderTypeByInfo();
			}
			this.iconFolder = folderInfo.getFolderByType(sorceFileInfo, type);
		}
		
		public List<FileOperationHandler> getFileMoves() {
			return this.moveList;
		}
		
		public void printFileMoves() throws IOException, ActionAlreadyActivatedException {
			System.out.println(" Start valid: " + valid);
			System.out.println(sorceFileInfo.getFile());
			System.out.println(action);
			System.out.println(moveType);
			if(!valid)
				return;
			System.out.println(folderInfo.getPath());
			System.out.println("OK Man");
			System.out.println(moveList);
			System.out.println(this.action);
			System.out.println(sorceFileInfo.getFile());
			createMoveInfo();
			System.out.println(moveList);
			for(FileOperationHandler han : moveList)
				han.activateAction();
			List<ManageFile> list = new ArrayList<>();
			for(ManageFile han : sideMovesList) {
				list.add(han);
			}
			sideMovesList.clear();
			for(ManageFile han : list)
				han.printFileMoves();
		}
	}
}