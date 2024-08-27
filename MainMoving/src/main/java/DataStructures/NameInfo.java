package DataStructures;


import java.util.Arrays;
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
	private String episodeName = "";
	private String index = "";
	private String lang = "";
	private FileInfoType infoType;
	
	public static enum NameInfoType {
		NAME("Name"),
		YEAR("Year", MAX_YEAR_LENGTH),
		SEASON("Season", MAX_SEASON_LENGTH),
		EPISODE("Episode", MAX_EPISODE_LENGTH),
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
		map.put(NameInfoType.DESCRIPTION, episodeName);
		return map;
	}
	
	
	public void setContent(NameInfoType type, String str) {
		switch (type) {
		case NAME: setName(str);
			break;
		case YEAR: setYear(str);
			break;
		case SEASON: setSeason(str);
			break;
		case EPISODE: setEpisode(str);
			break;
		case DESCRIPTION: setEpisodeName(str);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Creates Empty
	 */
	public NameInfo() {
		
	}
	
	public NameInfo(String name) {
		setVal(name);
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
		return !this.name.isBlank();
	}
	
	public boolean hasYear() {
		return !this.year.isBlank();
	}
	
	public boolean hasSeason() {
		return !this.season.isBlank();
	}
	
	public boolean hasEpisode() {
		return !this.episode.isBlank();
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
	
	protected void setVal(String name) {
		Word number = Word.getFirstNumber(name);
		Word nextWord = number.getNextWord();
		int end = number.end;
		if(nextWord.hasWord() && number.start == 0 && name.charAt(end++) == '.' 
				&& end < name.length() && name.charAt(end) == ' ') {
			index = number.str;
			name = name.substring(end);
		}
		this.infoType = new FileInfoType(name);
		String newName = this.infoType.getNameWithoutFolderType();
		createName(newName);
	}
	
	
	protected void createName(String str) {
		boolean isPreviousDash = false;
		boolean endScan = false;
		Word word = Word.getFirstWord(str);
		String version = "";
		String year = "";
		while(!endScan(word) && !endScan) {
			if((hasName() || hasYear()) && isVersionOfMedia(word)) {
				version = addWord(version, word);	
			}
			else {
				if(!version.isEmpty()) {
					name = addWord(name, version);
					version = "";
				}
				if(!this.name.isEmpty() && isYearFormat(word.str)) {
					if(!year.isEmpty()) {
						name = addWord(name, year);
						if(isPreviousDash) {
							name = addWord(name, "-");
							isPreviousDash = false;
						}
					}
					year = word.str;
					this.year = getYearNumber(word.str);
				}
				else if(isSeasonAnyFormat(word)) {
					createSeason(word.getFullStringFromCurrentStart());
					endScan = true;
				}
				else if(isEpisodeAnyFormat(word)) {
					createEpisode(word.getFullStringFromCurrentStart());
					endScan = true;
				}
				else if((hasName() || hasYear()) && isVersionOfMedia(word)) {
					version = addWord(version, word);	
				}
				else /*if(isName(word.str))*/ {
					if(word.str.equals("-"))
						isPreviousDash = true;
					else {
						if(!year.isEmpty()) {
							name = addWord(name, year);
							this.year = "";
							year = "";
						}
						if(!this.name.isEmpty() && isPreviousDash)
							name = addWord(name, "-");
						name = addWord(name, word);
						isPreviousDash = false;
					}
				}
			}
			word = word.getNextWord();
		}
	}
	
	protected void createName2222(String str) {
		boolean isPreviousDash = false;
		boolean endScan = false;
		Word word = Word.getFirstWord(str);
		String version = "";
		String year = "";
		while(!endScan(word) && !endScan) {
			if(!this.name.isEmpty() && isYearFormat(word.str)) {
				if(!version.isEmpty()) {
					name = addWord(name, version);
					version = "";
				}
				year = word.str;
				if(!this.year.isEmpty()) {
					name = addWord(name, this.year);
					if(isPreviousDash) {
						name = addWord(name, "-");
						isPreviousDash = false;
					}
				}
				this.year = getYearNumber(word.str);
			}
			else if(isSeasonAnyFormat(word)) {
				createSeason(word.getFullStringFromCurrentStart());
				endScan = true;
			}
			else if(isEpisodeAnyFormat(word)) {
				createEpisode(word.getFullStringFromCurrentStart());
				endScan = true;
			}
			else if((hasName() || hasYear()) && isVersionOfMedia(word)) {
				version = addWord(version, word.str);	
			}
			else /*if(isName(word.str))*/ {
				if(!version.isEmpty()) {
					name = addWord(name, version);
					version = "";
				}
				if(word.str.equals("-"))
					isPreviousDash = true;
				else {
					if(!year.isEmpty()) {
						name = addWord(name, year);
						this.year = "";
						year = "";
					}
					if(!this.name.isEmpty() && isPreviousDash)
						name = addWord(name, "-");
					name = addWord(name, word.str);
					isPreviousDash = false;
				}
			}
			word = word.getNextWord();
		}
	}
	
	private String getYearNumber(String str) {
		if(str.startsWith("(") && str.endsWith(")"))
			str = str.substring(1, str.length()-1);
		return str;
	}
	
	private boolean isVersionOfMedia(Word word) {
		String[] arr = {"dc", "extended"};
		String str = word.str.toLowerCase();	
		for(String strArr : arr) 
			if(str.contains(strArr))
				return true;
		return false;
	}
	
	private void createSeason(String str) {
		Word number = Word.getFirstNumber(str);
		Word word = Word.getFirstWord(str);
		setSeason(number.str);
		str = str.substring(number.end);
		if(isSeasonFormat3(word)) //replace 01x04 with 01xe04
			str = "e" + str.substring(1, str.length());
		boolean endScan = false;
		word = Word.getFirstWord(str);
		while(!endScan(word) && !endScan) {
			if(word.str.equals("-")) {
				word = word.getNextWord();
			}
			else {
				endScan = true;
			}
		}
		if(isEpisodeAnyFormat(word))
			createEpisode(str);
	}
	
	private void createEpisode(String str) {
		Word word = Word.getFirstNumber(str);
		setEpisode(word.str);
		createEpisodeName(word.getNextString());
	}
	
	private void createEpisodeName(String str) {
		boolean isPreviousDash = false;
		Word word = Word.getFirstWord(str);
		while(!endScan(word)) {
			//if(isName(word.str)) {
				if(word.str.equals("-"))
					isPreviousDash = true;
				else {
					if(!this.episodeName.isEmpty() && isPreviousDash)
						episodeName = addWord(episodeName, "-");
					episodeName = addWord(episodeName, word);
					isPreviousDash = false;
				}
			//}
			word = word.getNextWord();
		}
	}
	
	private String addWord(String str, Word word) {
		return addWord(str, word.str);
	}
	
	private String addWord(String str, String word) {
		if(!str.isEmpty() && !word.isEmpty())
			str += ' ';
		return str + word;		
	}
	
	private boolean isYearFormat(String str) {
		str = getYearNumber(str);
		String regex = String.format("\\d{1,%d}", MAX_YEAR_LENGTH);
		return str.matches(regex) && str.length() >= MAX_YEAR_LENGTH;
	}
	
	private boolean isSeasonAnyFormat(Word word) {
		return isSeasonFormat1(word) || isSeasonFormat2(word) || isSeasonFormat3(word);
	}
	
	/**
	 * Season format of: ("s")(number)^+(("")||([isEpisodeFormat1]))
	 * @param word
	 * @return
	 */
	private boolean isSeasonFormat1(Word firstWord) {
		String str = firstWord.str.toLowerCase();
		Word number = Word.getFirstNumber(firstWord.getFullStringFromCurrentStart());
		String match = "s"+number.str;
		return number.hasWord() && (str.equals(match) || 
				(str.startsWith(match) && isEpisodeFormat1(number.getNextWord())));
	}
	
	/**
	 * Season format of: ("season")[spacing](number)^+
	 * @param word
	 * @return
	 */
	private boolean isSeasonFormat2(Word word) {
		String str = word.str.toLowerCase();
		if(Arrays.asList("season").stream()
				.noneMatch(p -> p.equals(str)))
			return false;
		return word.isNextWordNumber();
	}
	
	/**
	 * Season format of: (number)^+("x")(number)^+
	 * @param word
	 * @return
	 */
	private boolean isSeasonFormat3(Word word) {
		String str = word.str.toLowerCase();
		String regexSeason = String.format("\\d{1,%d}", MAX_SEASON_LENGTH);
		String regexEpisode = String.format("\\d{1,%d}", MAX_EPISODE_LENGTH);
		return str.matches(regexSeason + "x" + regexEpisode);
	}
	
	private boolean isEpisodeAnyFormat(Word word) {
		return isEpisodeFormat1(word) || isEpisodeFormat2(word);
	}
	
	/**
	 * Episode format of: (("e")||("ep"))(number)^+
	 * @param word
	 * @return
	 */
	private boolean isEpisodeFormat1(Word firstWord) {
		String str = firstWord.str.toLowerCase();
		Word number = Word.getFirstNumber(firstWord.getFullStringFromCurrentStart());
		return number.hasWord() && 
				Arrays.asList("e", "ep").stream()
				.anyMatch(p -> str.equals(p + number.str));
	}
	
	/**
	 * Episode format of: (("ep")||("episode"))[spacing](number)^+
	 * @param word
	 * @return
	 */
	private boolean isEpisodeFormat2(Word word) {
		String str = word.str.toLowerCase();
		return Arrays.asList("ep", "episode").stream()
				.anyMatch(p -> p.equals(str)) && word.isNextWordNumber();
	}
	
	public boolean isSeasonGood(String season) {
		return hasSeason() ? this.season.equals(season) : season.isBlank();
	}
	
	public boolean isEpisode(String season, String episode) {
		return isSeasonGood(season) && (hasEpisode() ? this.episode.equals(episode) : episode.isBlank());
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
	
	private boolean isEnd(Word word) {
		String[] arr = {"576p", "720p", "1080p", "2160p", "hdr", "bluray"};
		String str = word.str.toLowerCase();	
		for(String strArr : arr) 
			if(str.contains(strArr))
				return true;
		
		String seasonNumbers = String.format("\\d{1,%d}", MAX_SEASON_LENGTH);
		String regexSeason = "s" + seasonNumbers;
		if(str.matches(regexSeason+"-"+regexSeason))
			return true; //s01-s04
		
		Word nextWord = word.getNextWord();
		if(!Arrays.asList("season").stream()
				.noneMatch(p -> p.equals(str))) {
			if(nextWord.str.matches(seasonNumbers + "-" + seasonNumbers))
				return true; //season 01-04
		}
		
		if(word.str.equals("+") && nextWord.str.equalsIgnoreCase("extras")) {
			return true; //+ Extras
		}
		
		return false;
	}
	
	private boolean endScan(Word word) {
		return word.start == word.end || isEnd(word);	
	}
	
	public static class Word {
		String fullStr = "";
		String str = "";
		int start = 0, end = 0;
		
		Word(String str) {
			fullStr = str;
		}
		
		public String getFullStringFromCurrentStart() {
			return fullStr.substring(start);
		}
		
		public String getNextString() {
			return fullStr.substring(end);
		}
		
		boolean hasWord() {
			return !str.isEmpty();
		}
		
		public static Word getFirstWord(String str) {
			return new Word(str).getNextWord();
		}
		
		private Word getNextWord() {
			return getNextWordCondition(this, new WordCodition() {
				@Override
				public boolean condition(char c) {
					return c != ' ' && c != '.';
				}
			});
		}
		
		public static Word getLastWord(String str) {
			return getLastWordCondition(str, new WordCodition() {
				@Override
				public boolean condition(char c) {
					return c != ' ' && c != '.';
				}
			});
		}
		
		public static Word getFirstNumber(String str) {
			return getFirstWordCondition(str, new WordCodition() {
				@Override
				public boolean condition(char c) {
					return Character.isDigit(c);
				}
			});
		}
		
		private static Word getFirstWordCondition(String str, WordCodition condition) {
			Word word = new Word(str);
			return getNextWordCondition(word, condition);
		}
		
		private static Word getNextWordCondition(Word prevWord, WordCodition condition) {
			Word word = new Word(prevWord.fullStr);
			String fullStr = word.fullStr;
			int len = fullStr.length();
			boolean firstMatch = true;
			int start = prevWord.end;
			word.start = start;
			word.end = start;
			for(int i = start; i < len; i++) {
				char c = fullStr.charAt(i);
				word.end = i;
				if(condition.condition(c)) {
					if(firstMatch)
						word.start = i;
					firstMatch = false;
					word.str += c;
					word.end = (i == len-1) ? len : word.end;
				}
				else if(!word.str.isEmpty())
					break;
			}
			return word;
		}
		
		private static Word getLastWordCondition(String str, WordCodition condition) {
			Word lastWordReversed = getFirstWordCondition(new StringBuffer(str).reverse().toString(), condition);
			Word lastWord = new Word(str);
			lastWord.str = new StringBuffer(lastWordReversed.str).reverse().toString();
			int len = str.length();
			lastWord.start = len - lastWordReversed.end;
			lastWord.end = len;
			return lastWord;
		}
		
		boolean isNextWordNumber() {
			return this.getNextWord().equals(getFirstNumber(this.getNextString()));
		}
		
		boolean equals(Word word) {
			return (hasWord() && word.hasWord()) && str.equals(word.str);
		}
	}
	
	private interface WordCodition {
		boolean condition(char c);
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
}