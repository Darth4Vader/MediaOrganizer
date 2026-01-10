package darthvader.mainmoving;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataStructures.NameInfo;
import DataStructures.NameInfoParser.Word;
import DataStructures.NameInfoParserRegex.MediaInfo;

public class TestNameInfo {
	
	public static final String firstSeason = "01";
	public static final int MAX_YEAR_LENGTH = 4, MAX_SEASON_LENGTH = 4, MAX_EPISODE_LENGTH = 4;
	public static final int FILE_NAME_MAX_LENGTH = 256;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//NameInfo info = new NameInfo("Supernatural - S01E01 REMUX.eng.srt");
		//System.out.println(info.getFullName());
		//System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-Season 05")));
		/*System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-Season05")));
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-S05")));
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-S 05")));
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-S05")));
		
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-S")));
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("(2005)-Season")));
		
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("show-name season 01")));
		System.out.println(extractSeasonCombinedFormat(Word.getFirstWord("season 01")));*/
		
		//System.out.println(new NameInfo("(2005)-Season 05"));
//		new NameInfo("Show-Name Season 01");
		//new NameInfo("Show-Name (2005)");

		createName("Show-Name");
		
		createName("Show-Name (2005)");
		
		createName("2002 2005");
		createName("2002 2005 ..");
		
		createName("2002 2s");
		
		createName("Show-Name Season 01");
		
		createName("Show-Name 2005  Season 01");
		
		createName("Show-Name 2005 Season 01");
		
		createName("Show-Name 2005 Season 01 ---- ---- Episode 02  ");
		
		createName("Show-Name 2005 Season 01 ---- ---- Episode 02     Nothng Babbbeee");
		
		createName("Show-Name Season 01 ---- ---- Episode 022005 ");
		
		createName("Show-Name ---- ---- Episode 0220 05 ");
		
		createName("Show-Name_2004- ---- ---- Episode 0220 05 ");
	}
	
    public static void createName(String str) {
		System.out.println("Original name: " + str);
		MediaInfo info = parseMediaFilename(str);
		if(info != null) {
			System.out.println("Parsed Media Info: " + info);
			System.out.println("Parsed Media Info: " + info.before);
			System.out.println("Parsed Media Info: " + info.title);
		}
    }
	
    
    
    
    
    
    
    
    public static MediaInfo parseMediaFilename(String filename) {
    	String whitespaceCharacters = "\\s._\\-\\)\\(";
        String beforePart = "(?<before>^[^\\p{L}\\p{N}]*)";

        String titlePart = "(?<title>.+?)";

        String yearPart = String.format(
            "[%s]+(?<year>\\d{1,%d})",
            whitespaceCharacters,
            NameInfo.MAX_YEAR_LENGTH
        );

        String seasonEpisodePart1 = String.format(
            "s(?<season01>\\d{1,%d})(?:e(?<episode01>\\d{1,%d}))?",
            NameInfo.MAX_SEASON_LENGTH,
            NameInfo.MAX_EPISODE_LENGTH
        );

        String seasonEpisodePart2 = String.format(
            "season[%s]*(?<season02>\\d{1,%d})(?:[%s]+episode[%s]*(?<episode02>\\d{1,%d}))?",
            whitespaceCharacters,
            NameInfo.MAX_SEASON_LENGTH,
      	  	whitespaceCharacters,
			whitespaceCharacters,
            NameInfo.MAX_EPISODE_LENGTH
        );

        String seasonEpisodePart3 = String.format(
            "(?:e|ep|episode)[%s]*(?<episodeOnly>\\d{1,%d})[%s]",
            whitespaceCharacters,
            NameInfo.MAX_EPISODE_LENGTH,
            whitespaceCharacters
		);
        String seasonEpisodePart4 = String.format(
            "(?<seasonX>\\d{1,%d})x(?<episodeX>\\d{1,%d})",
            NameInfo.MAX_SEASON_LENGTH,
            NameInfo.MAX_EPISODE_LENGTH
        );
        String seasonEpisodePart = String.format(
        	"[%s]+(?:%s|%s|%s|%s)",
        	whitespaceCharacters,
	        seasonEpisodePart1,
	        seasonEpisodePart2,
	        seasonEpisodePart3,
	        seasonEpisodePart4
    	);
        
        String extraPart      = "(?:[\\s._-]+(?<extra>.+?))";                 // optional extra info
        String extensionPart  = "(?:\\.(?<ext>mkv|mp4|avi|mov))?$";            // optional file extension

        String fullRegex =
            "^" +
            beforePart +
            titlePart +
            /*"(?=" +
                "(?:" + seasonEpisodePart + ")" +
                "|" +
                "(?:" + yearPart + ")" +
            ")"*/
            
            /*"(?=" +
                "(?:" + yearPart + ")" +
                "|" +
                "(?:" + seasonEpisodePart + ")" +
            ")"*/
            
            
            "(?=" +
				"(?:" + yearPart + ")?" +
				"[\\s._\\-\\)]*" +
				"(?:" + seasonEpisodePart + "|" + "$" + ")" +
				//"([\\s._\\-\\)]+|$)" + 
				"[\\s._\\-\\)]*" +
				//"(?:" + extraPart + extensionPart + ")" +
				//"(?:" + extraPart + ")?" +
				
				"(?<extra>.+)?" +  // everything after season/episode
				
				//"(?:[\\s._\\-\\)]<extra>.+)" +  // everything after season/episode
			")"
            
            
            /*"(?=" +
                "(?:" + yearPart + ")?" + "(?:" + seasonEpisodePart + ")?" + 
            ")"*/
                
            
            ;

        Pattern pattern = Pattern.compile(fullRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);

        if (!matcher.find()) {
        	MediaInfo info = new MediaInfo();
        	info.title = filename;
        	return info;
        }

        MediaInfo info = new MediaInfo();
        info.before = matcher.group("before") != null ? matcher.group("before") : "";
        info.title  = matcher.group("title");

        // Year
        String yearStr = matcher.group("year");
        if (yearStr != null) info.year = Integer.parseInt(yearStr);
        
        // Season
        if (matcher.group("season01") != null) info.season = Integer.parseInt(matcher.group("season01"));
        else if (matcher.group("season02") != null) info.season = Integer.parseInt(matcher.group("season02"));
        else if (matcher.group("seasonX") != null) info.season = Integer.parseInt(matcher.group("seasonX"));

        // Episode
        if (matcher.group("episode01") != null) info.episode = Integer.parseInt(matcher.group("episode01"));
        else if (matcher.group("episode02") != null) info.episode = Integer.parseInt(matcher.group("episode02"));
        else if (matcher.group("episodeX") != null) info.episode = Integer.parseInt(matcher.group("episodeX"));
        else if (matcher.group("episodeOnly") != null) info.episode = Integer.parseInt(matcher.group("episodeOnly"));
        
        info.extra     = matcher.group("extra") != null ? matcher.group("extra") : "";
        //info.extension = matcher.group("ext") != null ? matcher.group("ext") : "";
        
        return info;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
    public static MediaInfo parseMediaFilename2(String filename) {
        /*// Structured parts
    	// before = any leading punctuation or whitespace
    	String beforePart = "(?<before>^[^\\p{L}\\p{N}]*)";

    	// title = anything after before, up to the first "year or season/episode" token
    	String titlePart = "(?<title>.+?)"; // non-greedy, will stop when next regex (year/season/episode) matches
        String yearPart = String.format("[\\s._\\-\\(]+(?<year>\\d{1,%d})", NameInfo.MAX_YEAR_LENGTH); // optional year
        
        String seasonEpisodePart1 = String.format("?:s(?<season01>\\d{1,%d})(?:e(?<episode01>\\d{1,%d}))?", NameInfo.MAX_SEASON_LENGTH, NameInfo.MAX_EPISODE_LENGTH);
        String seasonEpisodePart2 = String.format("season[\\s\\-.]*(?<season02>\\d{1,%d})(?:[\\s\\-.]+episode[\\s\\-.]*(?<episode02>\\d{1,%d}))?", NameInfo.MAX_SEASON_LENGTH, NameInfo.MAX_EPISODE_LENGTH);
        String seasonEpisodePart3 = "(?:e|ep|episode)[\\s\\-.]*(?<episodeOnly>\\d+)";
        String seasonEpisodePart4 = String.format("(?<seasonX>\\d{1,%d})x(?<episodeX>\\d{1,%d})", NameInfo.MAX_SEASON_LENGTH, NameInfo.MAX_EPISODE_LENGTH);
        String seasonEpisodePart = String.format(
			"([\\s._-]+(?:((%s | %s)| (%s) | (%s))))",
			seasonEpisodePart1,
			seasonEpisodePart2,
			seasonEpisodePart3,
			seasonEpisodePart4
		);

        // Combine all parts
        String fullRegex = "^" +
            beforePart +
            titlePart + "(?=" + "(" + seasonEpisodePart + ")" + "|" + "(" + yearPart + ")" + ")"
            ;

        Pattern pattern = Pattern.compile(fullRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);*/
    	

        String beforePart = "(?<before>^[^\\p{L}\\p{N}]*)";

        String titlePart = "(?<title>.+?)";

        String yearPart = String.format(
            "[\\s._\\-\\(]+(?<year>\\d{1,%d})",
            NameInfo.MAX_YEAR_LENGTH
        );

        String seasonEpisodePart1 = String.format(
            "s(?<season01>\\d{1,%d})(?:e(?<episode01>\\d{1,%d}))?",
            NameInfo.MAX_SEASON_LENGTH,
            NameInfo.MAX_EPISODE_LENGTH
        );

        String seasonEpisodePart2 = String.format(
            "season[\\s\\-.]*(?<season02>\\d{1,%d})(?:[\\s\\-.]+episode[\\s\\-.]*(?<episode02>\\d{1,%d}))?",
            NameInfo.MAX_SEASON_LENGTH,
            NameInfo.MAX_EPISODE_LENGTH
        );

        String seasonEpisodePart3 = String.format(
            "(?:e|ep|episode)[\\s\\-.]*(?<episodeOnly>\\d{1,%d})[\\s._\\-\\)]",
            NameInfo.MAX_EPISODE_LENGTH
		);
        String seasonEpisodePart4 = String.format(
            "(?<seasonX>\\d{1,%d})x(?<episodeX>\\d{1,%d})",
            NameInfo.MAX_SEASON_LENGTH,
            NameInfo.MAX_EPISODE_LENGTH
        );

        /*String seasonEpisodePart = String.format(
            "(?:%s|%s|%s|%s)",
            seasonEpisodePart1,
            seasonEpisodePart2,
            seasonEpisodePart3,
            seasonEpisodePart4
        );*/
        
        String seasonEpisodePart = String.format(
        	"[\\s._\\-\\)]+(?:%s|%s|%s|%s)",
	        seasonEpisodePart1,
	        seasonEpisodePart2,
	        seasonEpisodePart3,
	        seasonEpisodePart4
    	);
        
        String extraPart      = "(?:[\\s._-]+(?<extra>.+?))";                 // optional extra info
        String extensionPart  = "(?:\\.(?<ext>mkv|mp4|avi|mov))?$";            // optional file extension

        String fullRegex =
            "^" +
            beforePart +
            titlePart +
            /*"(?=" +
                "(?:" + seasonEpisodePart + ")" +
                "|" +
                "(?:" + yearPart + ")" +
            ")"*/
            
            /*"(?=" +
                "(?:" + yearPart + ")" +
                "|" +
                "(?:" + seasonEpisodePart + ")" +
            ")"*/
            
            
            "(?=" +
				"(?:" + yearPart + ")?" +
				"[\\s._\\-\\)]*" +
				"(?:" + seasonEpisodePart + "|" + "$" + ")" +
				//"([\\s._\\-\\)]+|$)" + 
				"[\\s._\\-\\)]*" +
				//"(?:" + extraPart + extensionPart + ")" +
				//"(?:" + extraPart + ")?" +
				
				"(?<extra>.+)" +  // everything after season/episode
				
				//"(?:[\\s._\\-\\)]<extra>.+)" +  // everything after season/episode
			")"
            
            
            /*"(?=" +
                "(?:" + yearPart + ")?" + "(?:" + seasonEpisodePart + ")?" + 
            ")"*/
                
            
            ;

        Pattern pattern = Pattern.compile(fullRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);

        if (!matcher.find()) {
        	MediaInfo info = new MediaInfo();
        	info.title = filename;
        	return info;
        }

        MediaInfo info = new MediaInfo();
        info.before = matcher.group("before") != null ? matcher.group("before") : "";
        info.title  = matcher.group("title");

        // Year
        String yearStr = matcher.group("year");
        if (yearStr != null) info.year = Integer.parseInt(yearStr);
        
        // Season
        if (matcher.group("season01") != null) info.season = Integer.parseInt(matcher.group("season01"));
        else if (matcher.group("season02") != null) info.season = Integer.parseInt(matcher.group("season02"));
        else if (matcher.group("seasonX") != null) info.season = Integer.parseInt(matcher.group("seasonX"));

        // Episode
        if (matcher.group("episode01") != null) info.episode = Integer.parseInt(matcher.group("episode01"));
        else if (matcher.group("episode02") != null) info.episode = Integer.parseInt(matcher.group("episode02"));
        else if (matcher.group("episodeX") != null) info.episode = Integer.parseInt(matcher.group("episodeX"));
        else if (matcher.group("episodeOnly") != null) info.episode = Integer.parseInt(matcher.group("episodeOnly"));
        
        info.extra     = matcher.group("extra") != null ? matcher.group("extra") : "";
        //info.extension = matcher.group("ext") != null ? matcher.group("ext") : "";
        
        return info;
    }
	
	
	private static class SeasonInfo {
	    int season = -1;
	    int episode = -1;
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
	    String regex = "^(?:[^\\s]+[-_])?" // start of string, optionally first token + separator
	             + "(?:"
	             + "s(?<season01>\\d{1," + MAX_SEASON_LENGTH + "})(?:e(?<extra01>\\d{1," + MAX_EPISODE_LENGTH + "}))?"
	             + "|season\\s*(?<season02>\\d{1," + MAX_SEASON_LENGTH + "})"
	             + "|(?<season03>\\d{1," + MAX_SEASON_LENGTH + "})x(?<extra03>\\d{1," + MAX_EPISODE_LENGTH + "})"
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
	        return info;
	    }

	    // Special case: current word is "season" and next word is a number
	    /*if (str.equals("season") && word.isNextWordNumber()) {
	        info.season = Integer.parseInt(word.getNextWord().str);
	        return info;
	    }*/
	    
	    System.out.println("No season format matched in string: " + str);

	    return null;
	}
	
	/**
	 * Combined season check with optional prefix
	 * Handles formats like "(2005)-Season 05", "Superman-S01", "01x02", etc.
	 */
	/*public static SeasonInfo extractSeasonCombinedFormat(Word word) {
	    String firstWord = word.getFullStringFromCurrentStart().toLowerCase();
	    SeasonInfo info = new SeasonInfo();

	    // Remove leading/trailing punctuation
	    firstWord = firstWord.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");

	    // Regex with unique named groups and flexible spaces
	    String regex = "(?:.*?[-_])?" // optional prefix
	                 + "(?:s\\s*(?<season01>\\d{1," + MAX_SEASON_LENGTH + "})\\s*" // S05 or S 05
	                 + "(?:e\\s*(?<extra01>\\d{1," + MAX_EPISODE_LENGTH + "}))?"      // optional E02 or E 02
	                 + "|season\\s*(?<season02>\\d{1," + MAX_SEASON_LENGTH + "})"     // Season05 or Season 05
	                 + "|(?<season03>\\d{1," + MAX_SEASON_LENGTH + "})x(?<extra03>\\d{1," + MAX_EPISODE_LENGTH + "}))";

	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(firstWord);

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
	        return info;
	    }

	    // Special case: first word is just "season" and next word is a number
	    if (firstWord.equals("season") && word.isNextWordNumber()) {
	        info.season = Integer.parseInt(word.getNextWord().str);
	        return info;
	    }

	    return null;
	}
	
	public static class EpisodeInfo {
	    int episode = -1;
	    int episode2 = -1; // for ranges like E05-EP06
	}

	public static EpisodeInfo extractEpisodeCombinedFormat(Word word) {
	    String firstWord = word.getFullStringFromCurrentStart().toLowerCase();
	    EpisodeInfo info = new EpisodeInfo();

	    // Remove leading/trailing punctuation
	    firstWord = firstWord.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");

	    // Regex with unique named groups for all episode formats
	    String regex = "(?:.*?[-_])?" // optional prefix
	                 + "(?:"
	                 + "(?:e|ep)\\s*(?<episode01>\\d+)"             // E05 or EP05, optional space
	                 + "(?:-(?:e|ep)\\s*(?<episode02>\\d+))?"      // optional range: E05-EP06
	                 + "|(?:ep|episode)\\s*(?<episode03>\\d+)"     // EP 05 or Episode 05
	                 + ")";

	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(firstWord);

	    if (matcher.matches()) {
	        if (matcher.group("episode01") != null) {
	            info.episode = Integer.parseInt(matcher.group("episode01"));
	            if (matcher.group("episode02") != null) info.episode2 = Integer.parseInt(matcher.group("episode02"));
	        } else if (matcher.group("episode03") != null) {
	            info.episode = Integer.parseInt(matcher.group("episode03"));
	        }
	        return info;
	    }

	    // Special case: first word is "ep" or "episode" and next word is number
	    if ((firstWord.equals("ep") || firstWord.equals("episode")) && word.isNextWordNumber()) {
	        info.episode = Integer.parseInt(word.getNextWord().str);
	        return info;
	    }

	    return null; // no match
	}*/


}
