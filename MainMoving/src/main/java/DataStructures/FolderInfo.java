package DataStructures;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import DataStructures.FileInfoType.FolderType;

public class FolderInfo extends FileInfo {
	
	public static final FolderTypeHierarchy hierarchy = FolderTypeHierarchy.createHierarchy();
	
	private FolderType folderType = FolderType.NONE;
	
	@JsonCreator
	public FolderInfo(File file) {
		super(file);
	}

	@Override
	public FolderType getFolderType() {
		return folderType;
	}

	@Override
	public void setFolderType(FolderType folderType) {
		System.out.println("Set    " + folderType);
		this.folderType = folderType;
	}
	
	public File getFolderByType(NameInfo nameInfo, FolderType type) {
		return getFolderByTypeWithCheck(nameInfo, type, false);
	}
	
	public File createFolderByType(NameInfo nameInfo, FolderType type) {
		return getFolderByTypeWithCheck(nameInfo, type, true);
	}
	
	private File getFolderByTypeWithCheck(NameInfo nameInfo, FolderType type, boolean createIfNotExists) {
		File file = getFile();
		if(type == FolderType.MAIN_FOLDER)
			return file;
		return getFolderByTypeWithCheck(file, nameInfo, FolderType.MAIN_FOLDER, type, createIfNotExists);
	}

	
	private File getFolderByTypeWithCheck(File currentFile, NameInfo nameInfo, FolderType sourceType, FolderType destType, boolean createIfNotExists) {
		FolderType ascendingType = getAscendingFolderType(sourceType, destType);
		String name = getNameByFolderType(nameInfo, ascendingType);
		boolean check = true;
		if(ascendingType == FolderType.NONE)
			check = false;
		//System.out.println(nameInfo.getFullName());
		System.out.println(sourceType + " until " + destType);
		System.out.println(ascendingType + " -> " + name);
		if(ascendingType == FolderType.TV_EPISODE) {
			FileInfo subInfo = ManageFolder.getEpisodeFolder(currentFile, nameInfo);
			if(subInfo != null) {
				check = false;
				currentFile = subInfo.getFile();
				String subDescription = subInfo.getDescription(), description = nameInfo.getDescription();
				if(subDescription.length() >= description.length()) {
					nameInfo.setEpisodeName(subDescription);
				}
				/*else if(createIfNotExists && currentFile.isDirectory()) {
					File rename = currentFile;
					currentFile = new File(FilesUtils.checkPath(rename.getParent()) + name);
					rename.renameTo(currentFile);
				}*/
			}
		}
		else if(ascendingType == FolderType.TV_SERIES) {
			if(!nameInfo.hasSeason()) {
				check = false;
				if(nameInfo.getFolderType() == FolderType.NONE) {
					currentFile = null;
				}
			}
		}
		System.out.println(check + "   :  " + currentFile);
		if(check)
			currentFile = ManageFolder.getFolderByType(currentFile, name, ascendingType, createIfNotExists);
		System.out.println(currentFile);
		if(currentFile == null || ascendingType == destType)
			return currentFile;
		return getFolderByTypeWithCheck(currentFile, nameInfo, ascendingType, destType, createIfNotExists);
	}
	
	/**
	 * Returns the next ascending FolderType from sourceType to destType 
	 * @param sourceType
	 * @param destType
	 * @return
	 */
	public FolderType getAscendingFolderType(FolderType sourceType, FolderType destType) {
		FolderTypeHierarchy media = hierarchy.getMainMediaHierarchy(this.folderType);
		System.out.println("soooo " + folderType);
		//System.out.println(media.);
		FolderType typp = media.getAscendingFolderType(sourceType, destType);
		System.out.println(sourceType + " Result: " + typp);
		return typp; 
	}
	
	public static class FolderTypeHierarchy {
		private final FolderType folderType;
		private FolderType mediaType;
		private List<FolderTypeHierarchy> nextHierarchy;
		
		public FolderTypeHierarchy(FolderType type) {
			this.folderType = type;
			this.nextHierarchy = new ArrayList<>();
			this.mediaType = FolderType.NONE;
		}
		
		private static FolderTypeHierarchy createMediaMain(FolderType mediaType) {
			FolderTypeHierarchy hierarchy = new FolderTypeHierarchy(FolderType.MAIN_FOLDER);
			hierarchy.mediaType = mediaType;
			return hierarchy;
		}
		
		private void addHierarchy(FolderType... types) {
			this.nextHierarchy.addAll(Arrays.asList(types).stream().map(t -> new FolderTypeHierarchy(t)).toList());
		}
		
		private void addHierarchy(FolderTypeHierarchy... hierarchy) {
			this.nextHierarchy.addAll(Arrays.asList(hierarchy));
		}
		
		public boolean isInCurrentLevel(FolderType type) {
			return this.folderType == type;
		}
		
		public boolean isFolderTypeAscendingHierarchy(FolderType sourceType, FolderType destType, boolean canReturn) {
			if(!canReturn && this.isInCurrentLevel(sourceType))
				canReturn = true;
			if(canReturn && this.isInCurrentLevel(destType))
				return true;
			boolean b;
			for(FolderTypeHierarchy next : this.nextHierarchy) {
				b = next.isFolderTypeAscendingHierarchy(sourceType, destType, canReturn);
				if(b == true)
					return true;
			}
			return false;
		}
		
		public boolean isFolderTypeAscendingHierarchy(FolderType sourceType, FolderType destType) {
			return isFolderTypeAscendingHierarchy(sourceType, destType, false);
		}
		
		public FolderTypeHierarchy getFolderTypeInHierarchy(FolderType type) {
			if(this.folderType == type)
				return this;
			for(FolderTypeHierarchy next : this.nextHierarchy) {
				FolderTypeHierarchy child = next.getFolderTypeInHierarchy(type);
				if(child != null)
					return child;
			}
			return null;
		}
		
		public FolderType getAscendingFolderType(FolderType sourceType, FolderType destType) {
			return getAscendingFolderType(sourceType, destType, false);
		}
		
		private FolderType getAscendingFolderType(FolderType sourceType, FolderType destType, boolean canReturn) {
			if(!this.isFolderTypeAscendingHierarchy(sourceType, destType))
				return FolderType.NONE;
			if(canReturn)
				return this.folderType;
			if(this.isInCurrentLevel(sourceType))
				canReturn = true;
			FolderType outputType = FolderType.NONE;
			for(FolderTypeHierarchy next : this.nextHierarchy) {
				FolderType nextType;
				if(canReturn) {	
					nextType = next.folderType;
				}
				else
					nextType = sourceType;
				outputType = next.getAscendingFolderType(nextType, destType, canReturn);
				if(outputType != FolderType.NONE)
					return outputType;
			}
			if(!this.isFolderTypeAscendingHierarchy(sourceType, destType))
				return FolderType.NONE;
			return outputType;
		}
		
		public static FolderTypeHierarchy createHierarchy() {
			FolderTypeHierarchy extras = new FolderTypeHierarchy(FolderType.EXTRAS);
			extras.addHierarchy(FolderType.FEATURETTES, FolderType.POSTERS, FolderType.CHARACTER_POSTERS, FolderType.INFORMATION);
			FolderTypeHierarchy movie = new FolderTypeHierarchy(FolderType.MOVIE);
			FolderTypeHierarchy episode = new FolderTypeHierarchy(FolderType.TV_EPISODE);
			FolderTypeHierarchy season = new FolderTypeHierarchy(FolderType.TV_SERIES);
			season.addHierarchy(episode, extras);
			FolderTypeHierarchy movieType = createMediaMain(FolderType.MOVIE);
			movieType.addHierarchy(movie, extras);
			FolderTypeHierarchy miniSeriesType = createMediaMain(FolderType.MINI_SERIES);
			miniSeriesType.addHierarchy(episode, extras);
			FolderTypeHierarchy TVSeriesType = createMediaMain(FolderType.TV_SERIES);
			TVSeriesType.addHierarchy(season, extras);
			FolderTypeHierarchy TVSeriesAndMovieType = createMediaMain(FolderType.TV_SERIES_AND_MOVIE);
			TVSeriesAndMovieType.addHierarchy(season, movie, extras);
			FolderTypeHierarchy noneType = new FolderTypeHierarchy(FolderType.NONE);
			noneType.addHierarchy(extras);
			FolderTypeHierarchy hierarchy = new FolderTypeHierarchy(FolderType.MAIN_FOLDER);
			hierarchy.addHierarchy(movieType, miniSeriesType, TVSeriesType, TVSeriesAndMovieType, noneType);
			return hierarchy;
		}
		
		public FolderTypeHierarchy getMainMediaHierarchy(FolderType mediaType) {
			System.out.println("Media Type: " + mediaType);
			if(!this.isInCurrentLevel(FolderType.MAIN_FOLDER))
				return null;
			for(FolderTypeHierarchy media : this.nextHierarchy) {
				System.out.println(media.mediaType);
				if(media.mediaType == mediaType)
					return media;
			}
			return null;
		}
	}
	
}
