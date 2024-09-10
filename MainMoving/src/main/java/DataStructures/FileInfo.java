package DataStructures;

import java.io.File;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

import DataStructures.FileInfoType.FolderType;
import FileUtilities.FileFormats;
import FileUtilities.FileFormats.FileFormat;
import FileUtilities.MimeUtils.MimeContent;
import FileUtilities.MimeUtils;

public class FileInfo extends NameInfo {
	
	private FileHandle file;
	private FileFormat fileFormat;
	
	public void setFileInformation(File file) {
		boolean change=  false;
		if(this.file != null) {
			if(MimeUtils.getMimeTypeAsExtension(file).equals(this.file.getType()))
				change = true;
		}
		else
			change = true;
		if(change) {
			this.fileFormat = FileFormats.getFileFormat(file);
			this.file = new FileHandle(file);
		}
	}
	
	/*public FileInfo duplicateNameInfo() {
		duplicateNameInfo();
	}*/
	
	public FileInfo(File file, NameInfo nameInfo) {
		super(nameInfo);
		setFileInformation(file);
	}
	
	public FileInfo(File file) {
		setFileInformation(file);
		String name = this.file.getNameWithoutType();
		//System.out.println("\n\nhelloooo");
		if(this.fileFormat == FileFormat.SUBTILTE) {
				//MimeUtils.isMimeContentGroup(file, MimeContent.SUBTITLES)) {
			System.out.println("LOVVEEEEEE");
			Word lastWord = Word.getLastWord(name);
			int start = lastWord.start;
			if(start-- >= 0) {
				char c = name.charAt(start);
				System.out.println(c);
				if(c == '.' || c == '_') {
					Locale locale = new Locale(lastWord.str);
					try {
						String con = locale.getISO3Language();
						System.out.println("We " + locale.getDisplayLanguage());
						name = name.substring(0, start);
						System.out.println("Teddddyyyyy");
						System.out.println(name);
					}
					catch (Exception e) {}					
				}
			}
			//System.out.println(lang);
			/*if(LocaleUtils.isAvailableLocale(locale)) {
				System.out.println("We " + locale.getDisplayLanguage());
				
			}*/
			//ULO
			//System.out.println("We " + locale.getISO3Language());
			//System.out.println(LocaleUtils.isAvailableLocale(locale));
		}
		/*System.out.println("Papare");
		System.out.println(newName);
		System.out.println(getLastWord(newName).str);*/
		//System.out.println("Hoo " + name);
		setVal(name);
		setFolderType(getFolderType());
	}
	
	@Override
	public void setFolderType(FolderType type) {
		super.setFolderType(getMoveType(file.getFile(), type));
	}
	
	public String getPath() {
		return this.file == null ? null : file.getPath();
	}
	
	public String getParentPath() {
		return this.file == null ? null : file.getParentPath();
	}
	
	public File getFile() {
		return this.file == null ? null : file.getFile();
	}
	
	private FolderType getMoveType(File file, FolderType type) {
		FileFormat format = FileFormats.getFileFormat(file);
		if(type == FolderType.FEATURETTES) {
			if(file.isDirectory() || format == FileFormat.VIDEO)
				return type;
		}
		else if(type == FolderType.POSTERS || type == FolderType.CHARACTER_POSTERS) {
			if(file.isDirectory() || format == FileFormat.IMAGE)
				return type;
		}
		else if(file.isDirectory()) {
			return type;
		}
		else if(format == FileFormat.VIDEO || format == FileFormat.SUBTILTE) {
			if(hasSeason() || hasEpisode())
				return FolderType.TV_EPISODE;
			else 
				return FolderType.MOVIE;
		}
		else if(format == FileFormat.LOGO) {
			if(type == FolderType.LOGO)
				return type;
		}
		return FolderType.NONE;
	}
	
	@Override
	public String getFullName() {
		return getFullName(this, getFileInfoType().getFolderTypeName());
	}
	
	public String getFullName(FolderType folderType) {
		return getFullName(this, folderType.getFolderName());
	}
	
	private String getFullName(NameInfo info, String folderName) {
		String str = super.getFullName(info);
		if(file.getFile().isDirectory() || info.getFolderType() == FolderType.POSTERS) {
			if(!folderName.isBlank()) {
				if(!folderName.contains("-")) {
					folderName = "- " + folderName;
				}
				if(!folderName.startsWith(" "))
					folderName = " " + folderName;
			}
			if(info.getFolderType() == FolderType.POSTERS) {
				int index = folderName.length()-1;
				if(folderName.charAt(index) == 's') {
					folderName = folderName.substring(0, index);
				}
			}
			str += folderName;
		}
		return str;
	}
	
	public String getFullNameWithMime() {
		return getFullNameWithMime(this, getFolderType(), getType());
	}
	
	public String getFullNameWithMime(NameInfo info, FolderType folderType, String mime) {
		return MimeUtils.createNameWithMime(getFullName(folderType), mime);
	}
	
	public FileFormat getFormat() {
		return this.fileFormat;
	}
	
	public String getType() {
		return this.file == null ? "" : file.getType();
	}
	
	public boolean hasType() {
		return !getType().equals("");
	}
	
	
}