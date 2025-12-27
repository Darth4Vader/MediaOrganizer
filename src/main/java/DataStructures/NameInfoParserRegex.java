package DataStructures;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataStructures.NameInfoParser.Word;

public class NameInfoParserRegex {

	private NameInfo nameInfo;
	
	public NameInfoParserRegex(NameInfo nameInfo) {
		this.nameInfo = nameInfo;
	}
	
	public void parse(String name) {
		nameInfo.reset();
		// The Index at the beginning of the name
		// edit it later
		/*Word number = Word.getFirstNumber(name);
		Word nextWord = number.getNextWord();
		int end = number.end;
		if(nextWord.hasWord() && number.start == 0 && name.charAt(end++) == '.' 
				&& end < name.length() && name.charAt(end) == ' ') {
			nameInfo.setIndex(number.str);
			name = name.substring(end);
		}*/
		nameInfo.setInfoType(new FileInfoType(name));
		String newName = nameInfo.getInfoType().getNameWithoutFolderType();
		createName(newName);
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
}