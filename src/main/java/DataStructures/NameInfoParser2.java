package DataStructures;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameInfoParser2 {
	
	private NameInfo nameInfo;
	
	public NameInfoParser2(NameInfo nameInfo) {
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
	
	public static String formatString(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		
		input = input.trim();

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < input.length(); i++) {
			char current = input.charAt(i);

			if (current == '-') {
				char left = (i > 0) ? input.charAt(i - 1) : ' ';
				char right = (i < input.length() - 1) ? input.charAt(i + 1) : ' ';

				boolean neighborIsSpace = (left == ' ' && right != ' ') || (left != ' ' && right == ' ');
				boolean partOfMultipleHyphens = (left == '-' || right == '-');
				boolean neighborIsDot = (left == '.' && right != '.') || (left != '.' && right == '.');

				// keep '-' only if both rules pass
				if (!neighborIsSpace && !partOfMultipleHyphens && !neighborIsDot) {
					result.append(current);
				}
			} else {
				result.append(current);
			}
		}

		return result.toString();
	}

    public static class MediaInfo {
        public String before = "";
        public String title = "";
        public Integer year = null;
        public Integer season = null;
        public Integer episode = null;
        public String extra = "";
        public String extension = "";

        @Override
        public String toString() {
            return String.format(
                "MediaInfo[before='%s', title='%s', year=%s, season=%s, episode=%s, extra='%s', extension='%s']",
                before, title, year, season, episode, extra, extension
            );
        }
    }
    
    private void createName(String str) {
		System.out.println("Original name: " + str);
		MediaInfo info = parseMediaFilename(str);
		if(info != null) {
			System.out.println("Parsed Media Info: " + info);
			System.out.println("Parsed Media Info: " + info.before);
			System.out.println("Parsed Media Info: " + info.title);
		}
    }
    
    /*String seasonEpisodePart = "(?:[\\s._-]+(?:" +
    // S05E05 or Season 05 Episode 05
    "(?:s(?<season01>\\d{1," + NameInfo.MAX_SEASON_LENGTH + "})" +
        "(?:e(?<episode01>\\d{1," + NameInfo.MAX_EPISODE_LENGTH + "}))?" +
    "|" +
    "season[\\s\\-.]*(?<season02>\\d{1," + NameInfo.MAX_SEASON_LENGTH + "})" +
        "(?:[\\s\\-.]+episode[\\s\\-.]*(?<episode02>\\d{1," + NameInfo.MAX_EPISODE_LENGTH + "}))?" +
    ")" +
    // Episode only: E05 or Episode 05
    "|" +
    "(?:e|ep|episode)[\\s\\-.]*(?<episodeOnly>\\d+)" +
    // 05x01 format
    "|" +
    "(?<seasonX>\\d{1," + NameInfo.MAX_SEASON_LENGTH + "})x(?<episodeX>\\d{1," + NameInfo.MAX_EPISODE_LENGTH + "})" +
"))?";*/

    public static MediaInfo parseMediaFilename(String filename) {
        // Structured parts
    	// before = any leading punctuation or whitespace
    	String beforePart = "(?<before>^[^\\p{L}\\p{N}]*)";

    	// title = anything after before, up to the first "year or season/episode" token
    	String titlePart = "(?<title>.+?)"; // non-greedy, will stop when next regex (year/season/episode) matches
        String yearPart = String.format("(?:[\\s._-]+(?<year>\\d{1,%d}))?", NameInfo.MAX_YEAR_LENGTH); // optional year

        // Season/Episode formats
        
        
        // S05E05 or Season 05 Episode 05
        String seasonEpisodePart1 = String.format("?:s(?<season01>\\d{1,%d})(?:e(?<episode01>\\d{1,%d}))?", NameInfo.MAX_SEASON_LENGTH, NameInfo.MAX_EPISODE_LENGTH);
        String seasonEpisodePart2 = String.format("season[\\s\\-.]*(?<season02>\\d{1,%d})(?:[\\s\\-.]+episode[\\s\\-.]*(?<episode02>\\d{1,%d}))?", NameInfo.MAX_SEASON_LENGTH, NameInfo.MAX_EPISODE_LENGTH);
        String seasonEpisodePart3 = "(?:e|ep|episode)[\\s\\-.]*(?<episodeOnly>\\d+)";
        String seasonEpisodePart4 = String.format("(?<seasonX>\\d{1,%d})x(?<episodeX>\\d{1,%d})", NameInfo.MAX_SEASON_LENGTH, NameInfo.MAX_EPISODE_LENGTH);
        String seasonEpisodePart = String.format(
			"(?:[\\s._-]+(?:(%s | %s)| (%s) | (%s)))?",
			seasonEpisodePart1,
			seasonEpisodePart2,
			seasonEpisodePart3,
			seasonEpisodePart4
		);

        String extraPart      = "(?:[\\s._-]+(?<extra>.+?))?";                 // optional extra info
        String extensionPart  = "(?:\\.(?<ext>mkv|mp4|avi|mov))?$";            // optional file extension

        // Combine all parts
        String fullRegex = "^" +
            beforePart +
            titlePart +
            yearPart;
            /*seasonEpisodePart +
            extraPart +
            extensionPart;*/;

        Pattern pattern = Pattern.compile(fullRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);

        if (!matcher.find()) return null;

        MediaInfo info = new MediaInfo();
        info.before = matcher.group("before") != null ? matcher.group("before") : "";
        info.title  = matcher.group("title");

        // Year
        String yearStr = matcher.group("year");
        if (yearStr != null) info.year = Integer.parseInt(yearStr);

        /*// Season
        if (matcher.group("season01") != null) info.season = Integer.parseInt(matcher.group("season01"));
        else if (matcher.group("season02") != null) info.season = Integer.parseInt(matcher.group("season02"));
        else if (matcher.group("seasonX") != null) info.season = Integer.parseInt(matcher.group("seasonX"));

        // Episode
        if (matcher.group("episode01") != null) info.episode = Integer.parseInt(matcher.group("episode01"));
        else if (matcher.group("episode02") != null) info.episode = Integer.parseInt(matcher.group("episode02"));
        else if (matcher.group("episodeX") != null) info.episode = Integer.parseInt(matcher.group("episodeX"));
        else if (matcher.group("episodeOnly") != null) info.episode = Integer.parseInt(matcher.group("episodeOnly"));

        info.extra     = matcher.group("extra") != null ? matcher.group("extra") : "";
        info.extension = matcher.group("ext") != null ? matcher.group("ext") : "";
*/
        return info;
    }
	
	private void createName2(String str) {
		System.out.println("Original name: " + str);
		str = formatString(str);
		System.out.println("Creating name from: " + str);
		boolean isPreviousDash = false;
		boolean endScan = false;
		Word word = Word.getFirstWord(str);
		String version = "";
		String year = "";
		String lastWordStr = "";
		SeasonInfo seasonInfo = null;
		EpisodeInfo episodeInfo = null;
		while(!endScan(word) && !endScan) {
			System.out.println("Current word: " + word.str);
			System.out.println("Current word: " + word.getFullStringFromCurrentStart());
			if((nameInfo.hasName() || nameInfo.hasYear()) && isVersionOfMedia(word)) {
				version = addWord(version, word);	
			}
			else {
				if(!version.isEmpty()) {
					nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), version));
					version = "";
				}
				seasonInfo = createSeason(word.getFullStringFromCurrentStart());
				System.out.println("Season Infooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo: " + seasonInfo);
				if(seasonInfo != null) {
					System.out.println("Detected season format in word: " + word.str);
					//System.out.println("Full string from current start: " + word.getFullStringFromCurrentStart());
					endScan = true;
				}
				else {
					episodeInfo = createEpisode(word.getFullStringFromCurrentStart());
					if(episodeInfo != null) {
						endScan = true;
					}
					else if((nameInfo.hasName() || nameInfo.hasYear()) && isVersionOfMedia(word)) {
						version = addWord(version, word);	
					} else {
						String yearExtracted = extractYear(word.str);
						if(!nameInfo.getNameWithoutYear().isEmpty() && yearExtracted != null) {
							System.out.println("Year Extracted: " + yearExtracted);
							if(!year.isEmpty()) {
								nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), year));
								if(isPreviousDash) {
									nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), "-"));
									isPreviousDash = false;
								}
							}
							year = word.str;
							nameInfo.setYear(yearExtracted);
						}
						else /*if(isName(word.str))*/ {
							if(word.str.equals("-"))
								isPreviousDash = true;
							else {
								if(!year.isEmpty()) {
									nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), year));
									nameInfo.setYear("");
									year = "";
								}
								if(!nameInfo.getNameWithoutYear().isEmpty() && isPreviousDash)
									nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), "-"));
								nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), word));
								isPreviousDash = false;
							}
						}
					}
				}
			}
			word = word.getNextWord();
		}
		System.out.println("Last Word: " + word.str);
		String lastStr = null;
		if(seasonInfo != null) {
			lastStr = seasonInfo.before;
		}
		else if(episodeInfo != null) {
			lastStr = episodeInfo.before;
		}
		System.out.println("Realy, The Last String: " + lastStr);
		word = Word.getFirstWord(lastStr != null ? lastStr : "");
		System.out.println("Processing last string: " + word.str);
		if((nameInfo.hasName() || nameInfo.hasYear()) && isVersionOfMedia(word)) {
			version = addWord(version, word);	
		}
		else {
			if(!version.isEmpty()) {
				nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), version));
				version = "";
			}
			if((nameInfo.hasName() || nameInfo.hasYear()) && isVersionOfMedia(word)) {
				version = addWord(version, word);	
			} else {
				String yearExtracted = extractYear(word.str);
				if(!nameInfo.getNameWithoutYear().isEmpty() && yearExtracted != null) {
					System.out.println("Year Extracted: " + yearExtracted);
					if(!year.isEmpty()) {
						nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), year));
						if(isPreviousDash) {
							nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), "-"));
							isPreviousDash = false;
						}
					}
					year = word.str;
					nameInfo.setYear(yearExtracted);
				}
				else /*if(isName(word.str))*/ {
					if(word.str.equals("-"))
						isPreviousDash = true;
					else {
						if(!year.isEmpty()) {
							nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), year));
							nameInfo.setYear("");
							year = "";
						}
						if(!nameInfo.getNameWithoutYear().isEmpty() && isPreviousDash)
							nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), "-"));
						nameInfo.setName(addWord(nameInfo.getNameWithoutYear(), word));
						isPreviousDash = false;
					}
				}
			}
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
	
	private SeasonInfo createSeason(String str) {
	    Word word = Word.getFirstWord(str); // Start from the first word
	    System.out.println("Creating season from: " + str);

	    // Extract season info using the combined extractor
	    SeasonInfo seasonInfo = extractSeasonCombinedFormat(word);
	    if (seasonInfo == null) return null; // No season found

	    nameInfo.setSeason(String.valueOf(seasonInfo.season));

	    // If the season already contains an episode (S01E02 or 01x02)
	    if (seasonInfo.episode != -1) {
	        createEpisode(word.getFullStringFromCurrentStart());
	        return seasonInfo;
	    }

	    // Scan ahead for episodes after the season word
	    Word nextWord = word.getNextWord();
	    while (nextWord.hasWord()) {
	        // Skip dashes or separators
	        if (nextWord.str.equals("-")) {
	            nextWord = nextWord.getNextWord();
	            continue;
	        }

	        // Check if next word contains an episode
	        if (isEpisodeAnyFormat(nextWord)) {
	            createEpisode(nextWord.getFullStringFromCurrentStart());
	            return seasonInfo;
	        }

	        // Stop scanning if no episode found
	        break;
	    }
	    return seasonInfo;
	}
	
	private EpisodeInfo createEpisode(String str) {
	    Word word = Word.getFirstWord(str); // Start from the first word

	    EpisodeInfo episodeInfo = extractEpisodeCombinedFormat(word);
	    if (episodeInfo == null) return null;

	    // Set the main episode
	    nameInfo.setEpisode(String.valueOf(episodeInfo.episode));

	    // Set the secondary episode if it’s a range (like E05-EP06)
	    if (episodeInfo.episode2 != -1) {
	        nameInfo.setToEpisode(String.valueOf(episodeInfo.episode2));
	    }

	    // Everything after the episode is the episode name
	    Word remaining = word.getNextWord();
	    if (remaining.hasWord()) {
	        createEpisodeName(remaining.getFullStringFromCurrentStart());
	    }
	    return episodeInfo;
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
	
	/*private boolean isYearFormat(String str) {
		System.out.println("Checking year format for string: " + str);
		str = getYearNumber(str);
		System.out.println("Year string after removing parentheses: " + str);
		String regex = String.format("\\d{1,%d}", NameInfo.MAX_YEAR_LENGTH);
		return str.matches(regex) && str.length() >= NameInfo.MAX_YEAR_LENGTH;
	}*/
	
	public static String extractYear(String str) {
		String regex = String.format(
			"(^|[\\s._-])\\(?(\\d{1,%d})\\)?($|[\\s._-])",
			NameInfo.MAX_YEAR_LENGTH
		);
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		
		System.out.println("Extracting year from string: " + str);
		
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	// Holds season/episode info
	public static class SeasonInfo {
	    int season = -1;
	    int episode = -1; // optional, if included like S01E02 or 01x02
	    String before = "";
	}

	public static class EpisodeInfo {
	    int episode = -1;
	    int episode2 = -1; // for ranges like E05-EP06
	    String before = "";
	}

	// ---------------- Season Extractor ----------------
	public static SeasonInfo extractSeasonCombinedFormat(Word word) {
	    String str = word.getFullStringFromCurrentStart().toLowerCase();
		//String str = word.str.toLowerCase();
	    //str = str.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", ""); // trim punctuation

	    SeasonInfo info = new SeasonInfo();

	    /*// The first character before the season keyword cannot be whitespace
	    // Only allow letters, numbers, or punctuation (like '-' or '_') before
	    if (!str.matches(".*(?:^|[-_])((s\\d{1," + MAX_SEASON_LENGTH + "})|(season\\d{1," + MAX_SEASON_LENGTH + "})|(\\d{1," + MAX_SEASON_LENGTH + "}x\\d{1," + MAX_EPISODE_LENGTH + "}))")) {
	    	System.out.println("This Word Is Not that great for season: " + str);
	        return null;
	    }*/
	    
	    System.out.println("Checking season format for string: " + str);

	    // Regex with named groups
	    String regex = "^(?<before>[^\\s]+[-_]?)?" // capture optional first token + separator
	             + "(?:"
	             + "s(?<season01>\\d{1," + NameInfo.MAX_SEASON_LENGTH + "})(?:e(?<extra01>\\d{1," + NameInfo.MAX_EPISODE_LENGTH + "}))?"
	             + "|season[\\s\\-.]*(?<season02>\\d{1," + NameInfo.MAX_SEASON_LENGTH + "})"
	             + "|(?<season03>\\d{1," + NameInfo.MAX_SEASON_LENGTH + "})x(?<extra03>\\d{1," + NameInfo.MAX_EPISODE_LENGTH + "})"
	             + ")";

	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(str);

	    if (matcher.matches()) {
	        if (matcher.group("season01") != null) {
	            info.season = Integer.parseInt(matcher.group("season01"));
	            if (matcher.group("extra01") != null) info.episode = Integer.parseInt(matcher.group("extra01"));
	        } else if (matcher.group("season02") != null) {
	            info.season = Integer.parseInt(matcher.group("season02"));
	        } else if (matcher.group("season03") != null) {
	            info.season = Integer.parseInt(matcher.group("season03"));
	            info.episode = Integer.parseInt(matcher.group("extra03"));
	        }
	        //word.start += matcher.start(); // Move word start forward
	        System.out.println("Matched season format in string: " + str);
	        System.out.println(matcher.start() + " to " + matcher.end());
	        System.out.println(matcher.group("before"));
	        info.before = matcher.group("before") != null ? matcher.group("before") : "";
	        return info;
	    }

	    // Special case: current word is "season" and next word is a number
	    System.out.println("Next word is number: " + word.getNextWord().str);
	    if (str.equals("season") && word.isNextWordNumber()) {
	        info.season = Integer.parseInt(word.getNextWord().str);
	        return info;
	    }
	    
	    System.out.println("No season format matched in string: " + str);

	    return null;
	}


	public boolean isSeasonAnyFormat(Word word) {
	    return extractSeasonCombinedFormat(word) != null;
	}

	// ---------------- Episode Extractor ----------------
	public static EpisodeInfo extractEpisodeCombinedFormat(Word word) {
	    String str = word.getFullStringFromCurrentStart().toLowerCase();
	    str = str.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");
	    EpisodeInfo info = new EpisodeInfo();

	    // Regex for E05, EP05, E05-EP06, EP 05, Episode 05
	    String regex = "^(?<before>[^\\s]+[-_]?)?" // capture optional first token + separator
	                 + "(?:"
	                 + "(?:e|ep)\\s*(?<episode01>\\d+)(?:-(?:e|ep)\\s*(?<episode02>\\d+))?" 
	                 + "|(?:ep|episode)\\s*(?<episode03>\\d+)"
	                 + ")";

	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(str);

	    if (matcher.matches()) {
	        if (matcher.group("episode01") != null) {
	            info.episode = Integer.parseInt(matcher.group("episode01"));
	            if (matcher.group("episode02") != null) info.episode2 = Integer.parseInt(matcher.group("episode02"));
	        } else if (matcher.group("episode03") != null) {
	            info.episode = Integer.parseInt(matcher.group("episode03"));
	        }
	        info.before = matcher.group("before") != null ? matcher.group("before") : "";
	        return info;
	    }

	    // Special case: first word is "ep" or "episode" and next word is number
	    if ((str.equals("ep") || str.equals("episode")) && word.isNextWordNumber()) {
	        info.episode = Integer.parseInt(word.getNextWord().str);
	        return info;
	    }

	    return null;
	}

	public boolean isEpisodeAnyFormat(Word word) {
	    return extractEpisodeCombinedFormat(word) != null;
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
		public String str = "";
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
		
		public Word getNextWord() {
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
		
		public boolean isNextWordNumber() {
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