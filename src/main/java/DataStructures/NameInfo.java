package DataStructures;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import DataStructures.FileInfoType.FolderType;

public class NameInfo {
	
	public static final String firstSeason = "01";
	public static final int MAX_YEAR_LENGTH = 4, MAX_SEASON_LENGTH = 4, MAX_EPISODE_LENGTH = 4;
	public static final int FILE_NAME_MAX_LENGTH = 256;
	
	
	private String name = "";
	private String year = "";
	private String season = "";
	private String episode = "";
	private String toEpisode = "";
	private String episodeName = "";
	private String index = "";
	private String lang = "";
	private FileInfoType infoType;
	
	public static enum NameInfoType {
		NAME("Name"),
		YEAR("Year", MAX_YEAR_LENGTH),
		SEASON("Season", MAX_SEASON_LENGTH),
		EPISODE("Episode", MAX_EPISODE_LENGTH),
		TO_EPISODE("Episode", MAX_EPISODE_LENGTH),
		DESCRIPTION("Episode Name");
		
		private final String name;
		private final int length;
		
		NameInfoType() {
			this("");
		}
		
		NameInfoType(String name) {
			this.name = name;
			this.length = FILE_NAME_MAX_LENGTH;
		}
		
		NameInfoType(String name, int num) {
			this.name = name;
			this.length = num;
		}
		
		public String getInfoName() {
			return this.name;
		}
		
		public int getInfoLength() {
			return this.length;
		}
	}
	
	public Map<NameInfoType, String> createInfoMap() {
		Map<NameInfoType, String> map = new HashMap<>();
		map.put(NameInfoType.NAME, name);
		map.put(NameInfoType.YEAR, year);
		map.put(NameInfoType.SEASON, season);
		map.put(NameInfoType.EPISODE, episode);
		map.put(NameInfoType.TO_EPISODE, toEpisode);
		map.put(NameInfoType.DESCRIPTION, episodeName);
		return map;
	}
	
	
	public NameInfo setContent(NameInfoType type, String str) {
		switch (type) {
		case NAME: setName(str);
			break;
		case YEAR: setYear(str);
			break;
		case SEASON: setSeason(str);
			break;
		case EPISODE: setEpisode(str);
			break;
		case TO_EPISODE: setToEpisode(str);
		    break;
		case DESCRIPTION: setEpisodeName(str);
			break;
		default:
			break;
		}
		return this;
	}
	
	/**
	 * Creates Empty
	 */
	public NameInfo() {
		
	}
	
	public NameInfo(String name) {
		parseName(name);
	}
	
	public void parseName(String name) {
		new NameInfoParser(this).parse(name);
	}
	
	public void reset() {
		this.name = "";
		this.year = "";
		this.season = "";
		this.episode = "";
		this.toEpisode = "";
		this.episodeName = "";
		this.index = "";
		this.lang = "";
		this.infoType = null;
	}
	
	public NameInfo duplicateNameInfo() {
		NameInfo info = new NameInfo(createInfoMap());
		info.infoType = this.infoType;
		return info;
	}
	
	public NameInfo(NameInfo info) {
		this(info.createInfoMap());
		this.infoType = new FileInfoType(info.infoType);
	}
	
	private NameInfo(Map<NameInfoType, String> map) {
		setMap(map);
	}
	
	public void setMap(Map<NameInfoType, String> map) {
		for(NameInfoType type : map.keySet()) {
			setContent(type, map.get(type));
		}
	}
			
	
	public void setMissing(NameInfo info) {
		if(info.hasName() && this.hasName()) {
			if(!info.getNameWithoutYear().equalsIgnoreCase(this.getNameWithoutYear()))
				return;
		}
		if(!hasName())
			setName(info.name);
		if(!hasYear())
			setYear(info.year);
		if(!hasSeason())
			setSeason(info.season);
		if(!hasEpisode())
			setEpisode(info.episode);
		if (!hasToEpisode())
			setToEpisode(info.toEpisode);
		if(!hasDescription())
			setEpisodeName(info.episodeName);
	}
	
	public void renameNameInfo(NameInfo newNameInfo) {
		if(newNameInfo.hasName())
			setName(newNameInfo.getNameWithoutYear());
		if(newNameInfo.hasYear())
			setYear(newNameInfo.getYear());
		if(newNameInfo.hasSeason())
			setSeason(newNameInfo.getSeason());
		if(hasEpisode() && newNameInfo.hasDescription()) {
			setEpisodeName(newNameInfo.getDescription());
		}
	}
	
	public boolean equalsBasedOnCriteria(NameInfo nameInfo, NameInfoType... types) {
		if(types == null)
			return false;
		boolean bol = true;
		for(NameInfoType type : types) {
			bol &= equalsBasedOnCriteria(nameInfo, type);
		}
		return bol;
	}
	
	private boolean equalsBasedOnCriteria(NameInfo nameInfo, NameInfoType type) {
		boolean bol;
		switch (type) {
		case NAME: bol = hasName() && nameInfo.hasName() && this.name.equals(nameInfo.name);
			break;
		case YEAR: bol = hasYear() && nameInfo.hasYear() && this.year.equals(nameInfo.year);
			break;
		case SEASON: bol = hasSeason() && nameInfo.hasSeason() && this.season.equals(nameInfo.season);
			break;
		case EPISODE: bol = hasEpisode() && nameInfo.hasEpisode() && this.episode.equals(nameInfo.episode);
			break;
		case TO_EPISODE: bol = hasToEpisode() && nameInfo.hasToEpisode() && this.toEpisode.equals(nameInfo.toEpisode);
		    break;
		case DESCRIPTION: bol = hasDescription() && nameInfo.hasDescription() && this.episodeName.equals(nameInfo.episodeName);
			break;
		default:
			bol = false;
			break;
		}
		return bol;
	}
	
	public boolean equalsPartOfBasedOnCriteria(NameInfo nameInfo, NameInfoType... types) {
		if(types == null)
			return false;
		boolean bol = true;
		for(NameInfoType type : types) {
			bol &= equalsPartOfBasedOnCriteria(nameInfo, type);
		}
		return bol;
	}
	
	private boolean equalsPartOfBasedOnCriteria(NameInfo nameInfo, NameInfoType type) {
		boolean bol;
		switch (type) {
		case NAME: bol = hasName() ? nameInfo.hasName() && this.name.equals(nameInfo.name) : !nameInfo.hasName();
			break;
		case YEAR: bol = hasYear() ? nameInfo.hasYear() && this.year.equals(nameInfo.year) : !nameInfo.hasYear();
			break;
		case SEASON: bol = hasSeason() ? nameInfo.hasSeason() && this.season.equals(nameInfo.season) : !nameInfo.hasDescription();
			break;
		case EPISODE: bol = hasEpisode() ? nameInfo.hasEpisode() && this.episode.equals(nameInfo.episode) : !nameInfo.hasEpisode();
			break;
		case TO_EPISODE: bol = hasToEpisode() ? nameInfo.hasToEpisode() && this.toEpisode.equals(nameInfo.toEpisode) : !nameInfo.hasToEpisode();
		    break;
		case DESCRIPTION: bol = hasDescription() ? nameInfo.hasDescription() && this.episodeName.equals(nameInfo.episodeName) : !nameInfo.hasDescription();
			break;
		default:
			bol = false;
			break;
		}
		return bol;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NameInfo> T setName(String name) {
		this.name = name;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NameInfo> T setYear(String year) {
		this.year = year;
		return (T) this;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T extends NameInfo> T setSeason(String season) {
		this.season = getNumberInFormat(season);
		return (T) this;
	}
	
	public void removeSeason() {
		setSeason("");
	}
	
	@SuppressWarnings("unchecked")
	public  <T extends NameInfo> T setEpisode(String episode) {
		this.episode = getNumberInFormat(episode);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NameInfo> T setToEpisode(String toEpisode) {
		this.toEpisode = getNumberInFormat(toEpisode);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends NameInfo> T setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
		return (T) this;
	}
	
	public void setFolderType(FolderType type) {
		this.infoType.setFolderType(type);
	}
	
	public FileInfoType getFileInfoType() {
		return this.infoType;
	}
	
	public String getNameByFolderType(NameInfo info, FolderType type) {
		if(type == FolderType.MAIN_FOLDER)
			return getName();
		if(type == FolderType.TV_EPISODE)
			return getFullName(info);
		if(type == FolderType.NONE)
			return null;
		return getNameSeason(info);		
	}
	
	/**
	 * TV_EPISODE: hasSeason() && hasEpisode()<br> 
	 * TV_SEASON: hasSeason() && !hasEpisode()<br> 
	 * MAIN_FOLDER: !hasSeason() && !hasEpisode()
	 * @return one of the following.
	 */
	public FolderType getFolderTypeByInfo() {
		return hasEpisode() ? FolderType.TV_EPISODE : 
			hasSeason() ? FolderType.TV_SERIES : FolderType.MAIN_FOLDER;
	}
	
	public String getNameWithoutYear() {
		return this.name;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public String getSeason() {
		return this.season;
	}
	
	public String getEpisode() {
		return this.episode;
	}
	
	public String getToEpisode() {
		return this.toEpisode;
	}
	
	public String getDescription() {
		return this.episodeName;
	}
	
	public FolderType getFolderType() {
		return this.infoType.getFolderType();
	}
	
	public String getMapName() {
		return getMapNameFormat(getName());
	}
	
	public static String getMapNameFormat(String str) {
		String name = "";
		for(char c : str.toCharArray())
			if(c != ' ' && c != '-' && !(c+"").equals("'"))
				name += Character.toLowerCase(c);
		return name;
	}
	
	public String getName() {
		String str = this.name;
		if(!str.isEmpty()) {
			if(!this.year.isEmpty())
				str += " (" + this.year + ")";
		}
		return str;
	}
	
	public String getNameSeason() {
		return getNameSeason(this);
	}
	
	public String getNameSeason(NameInfo info) {
		return getNameSeason(info.getSeason());
	}
	
	public String getNameSeason(String season) {
		String str = getName();
		if(!str.isEmpty() && !season.isEmpty())
			str += " Season " + season;
		return str;
	}
	
	public String getNameSeasonEpisode() {
		return getNameSeasonEpisode(this);
	}
	
	public String getNameSeasonEpisode(NameInfo info) {
		String str = getName();
		if(!str.isEmpty()) {
			str += " - ";
			str += info.hasSeason() ? "S" + info.getSeason() : "";
			str += info.hasEpisode() ? "E" + info.getEpisode() : "";
			if(info.hasToEpisode())
				str += "-E" + info.getToEpisode();
		}
		return str;
	}
	
	public String getFullName() {
		return getFullName(this);
	}
	
	public String getFullName(NameInfo info) {
		String str = info.hasEpisode() ? getNameSeasonEpisode(info) : getNameSeason(info);
		if(!str.isEmpty())
			if(info.hasDescription())
				str += " - " + info.getDescription();
		return str;
	}
	
	public String getFullNameWithIndex() {
		String str = getFullName();
		if(!str.isEmpty())
			if(hasIndex())
				str = index + ". " + str;
		return str;
	}
	
	public boolean hasName() {
		return this.name != null && !this.name.isBlank();
	}
	
	public boolean hasYear() {
		return !this.year.isBlank();
	}
	
	public boolean hasSeason() {
		return this.season != null && !this.season.isBlank();
	}
	
	public boolean hasEpisode() {
		return this.episode != null && !this.episode.isBlank();
	}
	
	public boolean hasToEpisode() {
		return this.toEpisode != null && !this.toEpisode.isBlank();
	}
	
	public boolean hasDescription() {
		return !this.episodeName.isBlank();
	}
	
	public boolean compareSeasons(String season) {
		return hasSeason() && this.season.equals(getNumberInFormat(season));
	}
	
	private String getNumberInFormat(String number) {
		String str = number;
		if(StringUtils.isNumeric(number)) {
			int num = Integer.parseInt(str);
			str = num < 10 ? "0" : "";
			str += num;
		}
		return str;
	}
	
	public boolean isSeasonGood(String season) {
		return hasSeason() ? this.season.equals(season) : season.isBlank();
	}
	
	public boolean isEpisode(FileInfo episodeInfo) {
		return isEpisode(episodeInfo.getSeason(), episodeInfo.getEpisode(), episodeInfo.getToEpisode());
	}
	
	public boolean isEpisode(String season, String episode) {
		System.out.println("Season: " + season + " Episode: " + episode);
		return isSeasonGood(season) && (hasEpisode() ? this.episode.equals(episode) : episode.isBlank());
	}
	
	public boolean isEpisode(String season, String episode, String toEpisode) {
		return isSeasonGood(season) && (hasEpisode() ? this.episode.equals(episode) : episode.isBlank())
				&& (hasToEpisode() ? this.toEpisode.equals(toEpisode) : toEpisode.isBlank());
	}
	
	public boolean logical(boolean b1, boolean b2) {
		return !b1 || b2; /* p -> q = !p || q */
	}
	
	public boolean isPartOf(NameInfo info) {
		return logical(hasYear(), (info.hasYear() && this.year.equals(info.year)))
				&& logical(hasSeason(), (info.hasSeason() && compareSeasons(info.season)));
		//return () (hasSeason() && info.hasSeason() && this.season.equals(info.season));
	}
	
	public boolean equalsFullName(NameInfo folderInfo) {
		return this.getFullName().equals(folderInfo.getFullName());
	}

	public String getIndex() {
		return index;
	}
	
	public boolean hasIndex() {
		return !this.index.isBlank();
	}
	
	public void setIndex(String index) {
		this.index = index;
	}
	
	public FileInfoType getInfoType() {
		return infoType;
	}
	
	public void setInfoType(FileInfoType infoType) {
		this.infoType = infoType;
	}
}