package DataStructures;

import java.util.Arrays;
import java.util.Set;

public class NameInfoParser {
	
	private NameInfo nameInfo;
	
	public NameInfoParser(NameInfo nameInfo) {
		this.nameInfo = nameInfo;
	}
	
	public void parse(String name) {
		nameInfo.reset();
		Word number = Word.getFirstNumber(name);
		Word nextWord = number.getNextWord();
		int end = number.end;
		if(nextWord.hasWord() && number.start == 0 && name.charAt(end++) == '.' 
				&& end < name.length() && name.charAt(end) == ' ') {
			nameInfo.setIndex(number.str);
			name = name.substring(end);
		}
		nameInfo.setInfoType(new FileInfoType(name));
		String newName = nameInfo.getInfoType().getNameWithoutFolderType();
		createName(newName);
	}
	
	private void createName(String str) {
		boolean isPreviousDash = false;
		boolean endScan = false;
		Word word = Word.getFirstWord(str);
		String version = "";
		String year = "";
		while(!endScan(word) && !endScan) {
			if((nameInfo.hasName() || nameInfo.hasYear()) && isVersionOfMedia(word)) {
				version = addWord(version, word);	
			}
			else {
				if(!version.isEmpty()) {
					nameInfo.setName(addWord(nameInfo.getName(), version));
					version = "";
				}
				if(!nameInfo.getName().isEmpty() && isYearFormat(word.str)) {
					if(!year.isEmpty()) {
						nameInfo.setName(addWord(nameInfo.getName(), year));
						if(isPreviousDash) {
							nameInfo.setName(addWord(nameInfo.getName(), "-"));
							isPreviousDash = false;
						}
					}
					year = word.str;
					nameInfo.setYear(getYearNumber(word.str));
				}
				else if(isSeasonAnyFormat(word)) {
					createSeason(word.getFullStringFromCurrentStart());
					endScan = true;
				}
				else if(isEpisodeAnyFormat(word)) {
					createEpisode(word.getFullStringFromCurrentStart());
					endScan = true;
				}
				else if((nameInfo.hasName() || nameInfo.hasYear()) && isVersionOfMedia(word)) {
					version = addWord(version, word);	
				}
				else /*if(isName(word.str))*/ {
					if(word.str.equals("-"))
						isPreviousDash = true;
					else {
						if(!year.isEmpty()) {
							nameInfo.setName(addWord(nameInfo.getName(), year));
							nameInfo.setYear("");
							year = "";
						}
						if(!nameInfo.getName().isEmpty() && isPreviousDash)
							nameInfo.setName(addWord(nameInfo.getName(), "-"));
						nameInfo.setName(addWord(nameInfo.getName(), word));
						isPreviousDash = false;
					}
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
		nameInfo.setSeason(number.str);
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
		nameInfo.setEpisode(word.str);
		if(isEpisodeFormat3(Word.getFirstWord(str))) {
			word = Word.getFirstNumber(word.getNextString());
			nameInfo.setToEpisode(word.str);
		}
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
					if(!nameInfo.getDescription().isEmpty() && isPreviousDash)
						nameInfo.setEpisodeName(addWord(nameInfo.getDescription(), "-"));
					nameInfo.setEpisodeName(addWord(nameInfo.getDescription(), word));
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
		String regex = String.format("\\d{1,%d}", NameInfo.MAX_YEAR_LENGTH);
		return str.matches(regex) && str.length() >= NameInfo.MAX_YEAR_LENGTH;
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
				(str.startsWith(match) && 
						(isEpisodeFormat1(number.getNextWord()) || isEpisodeFormat3(number.getNextWord()))));
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
		String regexSeason = String.format("\\d{1,%d}", NameInfo.MAX_SEASON_LENGTH);
		String regexEpisode = String.format("\\d{1,%d}", NameInfo.MAX_EPISODE_LENGTH);
		return str.matches(regexSeason + "x" + regexEpisode);
	}
	
	private boolean isEpisodeAnyFormat(Word word) {
		return isEpisodeFormat1(word) || isEpisodeFormat2(word) || isEpisodeFormat3(word);
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
	
	/**
	 * Episode format of: (("ep")||("e"))[spacing](number)^+(("-"))[0,1](("ep"))[spacing](number)^+
	 * @param word
	 * @return
	 */
	private boolean isEpisodeFormat3(Word word) {
		String str = word.str.toLowerCase();
		Word number = Word.getFirstNumber(word.getFullStringFromCurrentStart());
		if(number.hasWord() && 
				Arrays.asList("e", "ep").stream()
				.anyMatch(p -> str.startsWith(p + number.str))) {
			word = number.getNextWord();
			if (word.str.startsWith("-")) {
				word = word.getSubWord(1);
			}
			return isEpisodeFormat1(word);
		}
		return false;
	}
	
	private boolean isEnd(Word word) {
		Set<String> END_MARKERS = Set.of(
			// resolution
			"576p","720p","1080p","2160p",
			
			// source
			"bluray","bdrip","dvdrip","hdtv",
			"web","webdl","webrip",
			"uhd","remux",
			
			// hdr / dv
			"hdr","hdr10","hdr10plus",
			"dv","dolbyvision",
			
			// video codec
			"x264","x265","avc","hevc","h264","h265",
			
			// audio
			"aac","ac3","eac3","dd","ddp",
			"dts","dtshd","truehd","lpcm",
			"atmos",
			
			// misc
			"10bit","repack","proper","internal"
		);
		String str = normalize(word.str);
		// exact match only
		for(String endMarker : END_MARKERS) {
			if(str.contains(endMarker))
				return true;
		}
		
		// resolution pattern (safety net)
		if (str.matches("\\d{3,4}p")) {
			return true;
		}

		String seasonNumbers = String.format("\\d{1,%d}", NameInfo.MAX_SEASON_LENGTH);
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
	
	private static String normalize(String s) {
	    return s.toLowerCase()
	            .replace(".", "")
	            .replace("-", "")
	            .replace("_", "");
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
		
		public Word getSubWord(int from) {
			Word word = new Word(str.substring(from));
			word.str = word.fullStr;
			word.start = 0;
			word.end = word.fullStr.length();
			return word;
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
}