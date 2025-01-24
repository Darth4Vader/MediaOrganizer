package darthvader.mainmoving;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import DataStructures.FileInfo;
import DataStructures.NameInfo;
import FileUtilities.FileFormats.FileFormat;

public class avatar {

	public static void main(String[] args) {
		
		NameInfo info = new NameInfo("Avatar The Last Airbender (2005) - S02E19-E20 - The Guru & The Crossroads of Destiny  ");
		
		System.out.println(info.getSeason() + "  " + info.getEpisode());
	
	
		File folder = new File("F:\\Avatar The Last Airbender (2005) Season 1-3 S01-S03 (1080p AMZN WEB-DL x265 HEVC 10bit EAC3 2.0 RCVR) REPACK\\Season 2");
		List<FileInfo> videos = new ArrayList<>();
		List<FileInfo> subtitles = new ArrayList<>();
		for (File file : folder.listFiles()) {
			FileInfo fileInfo = new FileInfo(file);
			System.out.println(fileInfo.getFullName() + " " + fileInfo.getEpisode() + "-" + fileInfo.getToEpisode());
			if(fileInfo.getFormat() == FileFormat.VIDEO) {
				videos.add(fileInfo);
			}
			if (fileInfo.getFormat() == FileFormat.SUBTILTE) {
				subtitles.add(fileInfo);
			}
		}
		for (FileInfo video : videos) {
			//System.out.println(video.getFullName() + " " + video.getSeason() + " " + video.getEpisode());
			FileInfo matchingSubtitle = subtitles.stream().filter(subtitle -> video.isEpisode(subtitle)).findFirst().orElse(null);
			if(matchingSubtitle != null) {
				File subtitle = matchingSubtitle.getFile();
				subtitle.renameTo(new File(subtitle.getParentFile(), video.getFileInfoType().getNameWithoutFolderType()+".srt"));
				subtitles.remove(matchingSubtitle);
			}
		}
	}

}
