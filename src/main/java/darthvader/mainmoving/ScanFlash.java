package darthvader.mainmoving;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ScanFlash {
	
	public static void main(String[] args) {
		File outJson = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\out.json");
		ObjectMapper mapper = new ObjectMapper();
		/*File dir = new File("F:\\");
		List<FileJson> files = new ArrayList<>();
		
		scanFolder(dir, files);
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\out.json"), files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		/*File createReplicateDir = new File("C:\\Users\\itay5\\OneDrive\\מסמכים\\output\\replicate");
		createReplicateDir.mkdirs();
		try {
			List<FileJson> files = mapper.readValue(outJson, 
					mapper.getTypeFactory().constructCollectionType(List.class, FileJson.class));
			replicateFolderStructure(files, createReplicateDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		File source = new File("D:\\");
		File destination = new File("C:\\Main_1_With_Marvel_backup");

		List<FileJson> files = new ArrayList<>();

		scanFolder(source, files);

		replicateFolderStructureWithFiles(
		    files,
		    source,
		    destination
		);
	}
	

	private static final Set<String> EMPTY_FILE_EXTENSIONS = Set.of(
			// Video
			"mp4", "mkv", "avi", "mov", "wmv",
			"flv", "webm", "m4v", "mpeg", "mpg",
			"ts", "m2ts", "3gp",

			// Audio
			"eac3", "ac3", "dts", "dtshd",
			"flac", "wav", "ape", "mka",
			"m4a", "aac", "ogg", "opus"
		);

	public static void scanFolder(File folder, List<FileJson> files) {
		File[] folderFiles = folder.listFiles();
		if(folderFiles != null) for (File file : folderFiles) {
			System.out.println(file);
			FileJson fileJson = new FileJson();
			fileJson.name = file.getName();
			if(file.isDirectory()) {
				fileJson.type = "folder";
				fileJson.children = new ArrayList<>();
				scanFolder(file, fileJson.children);
			} else {
				fileJson.type = "file";
			}
			files.add(fileJson);
		}
	}
	
	public static void replicateFolderStructure(List<FileJson> files, File parentFolder) {
		for (FileJson fileJson : files) {
			File newFile = new File(parentFolder, fileJson.name);
			if(fileJson.type.equals("folder")) {
				newFile.mkdirs();
				replicateFolderStructure(fileJson.children, newFile);
			} else {
				try {
					newFile.createNewFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static void replicateFolderStructureWithFiles(
			List<FileJson> files,
			File sourceFolder,
			File destinationFolder) {

		for (FileJson fileJson : files) {

			File sourceFile = new File(sourceFolder, fileJson.name);
			File destinationFile = new File(destinationFolder, fileJson.name);
			
			if (shouldSkipFile(sourceFile)) {
				System.out.println("SKIPPING: " + sourceFile);
				continue;
			}

			if (fileJson.type.equals("folder")) {

				if (!destinationFile.exists()) {
					destinationFile.mkdirs();
				}

				replicateFolderStructureWithFiles(
					fileJson.children,
					sourceFile,
					destinationFile
				);
				
				// Then copy the folder's metadata
				System.out.println("BEFORE METADATA: " + sourceFile);

				copyMetadata(sourceFile, destinationFile);

				System.out.println("AFTER METADATA: " + sourceFile);

			} else {

				try {

					if (isLargeMediaFile(sourceFile)) {

						// Video → create empty file
						Files.createFile(destinationFile.toPath());

						System.out.println(
							"VIDEO (empty): " + sourceFile
						);

					} else {

						// Everything else → copy the file
						Files.copy(
							sourceFile.toPath(),
							destinationFile.toPath(),
							StandardCopyOption.REPLACE_EXISTING
						);

						System.out.println(
							"COPIED: " + sourceFile
						);
					}
					

					// Copy file metadata
					System.out.println("BEFORE METADATA: " + sourceFile);

					copyMetadata(sourceFile, destinationFile);

					System.out.println("AFTER METADATA: " + sourceFile);
					
					

				} catch (Exception e) {

					System.err.println(
						"FAILED: " + sourceFile
					);

					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	private static boolean isLargeMediaFile(File file) {

		String name = file.getName().toLowerCase();

		int dot = name.lastIndexOf('.');

		if (dot == -1) {
			return false;
		}

		String extension = name.substring(dot + 1);

		return EMPTY_FILE_EXTENSIONS.contains(extension);
	}
	
	private static void copyMetadata(File source, File destination) {
		try {
			// Basic timestamps
			BasicFileAttributeView sourceBasicView =
				Files.getFileAttributeView(
					source.toPath(),
					BasicFileAttributeView.class
				);

			BasicFileAttributes basicAttributes =
				sourceBasicView.readAttributes();

			BasicFileAttributeView destinationBasicView =
				Files.getFileAttributeView(
					destination.toPath(),
					BasicFileAttributeView.class
				);

			destinationBasicView.setTimes(
				basicAttributes.lastModifiedTime(),
				basicAttributes.lastAccessTime(),
				basicAttributes.creationTime()
			);

			// Windows DOS attributes
			DosFileAttributeView sourceDosView =
				Files.getFileAttributeView(
					source.toPath(),
					DosFileAttributeView.class
				);

			DosFileAttributes dosAttributes =
				sourceDosView.readAttributes();

			DosFileAttributeView destinationDosView =
				Files.getFileAttributeView(
					destination.toPath(),
					DosFileAttributeView.class
				);

			destinationDosView.setReadOnly(dosAttributes.isReadOnly());
			destinationDosView.setHidden(dosAttributes.isHidden());
			destinationDosView.setSystem(dosAttributes.isSystem());
			destinationDosView.setArchive(dosAttributes.isArchive());

		} catch (Exception e) {
			System.err.println(
				"Could not copy metadata: " + source
			);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static boolean shouldSkipFile(File file) {
		String name = file.getName();

		return name.equalsIgnoreCase("desktop.ini")
			|| name.equalsIgnoreCase("Thumbs.db");
	}
	
	private static class FileJson {
		public String name;
		public String type;
		public List<FileJson> children;
	} 

}
