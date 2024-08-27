package NameInfo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import DataStructures.NameInfo;

public class MediaVersion {

	@Test
	public void test() {
		
		sameFullName("F9 (2021)",
				"F9 (2021) DC (1080p BluRay x265 HEVC 10bit AAC 7.1 Tigole)");
		sameFullName("DC League of Super-Pets (2022)",
				"DC League of Super-Pets (2022) (1080p BluRay x265 HEVC 10bit AAC 7.1 Tigole)");
		sameFullName("The DC League of Super-Pets (2022)",
				"The DC League of Super-Pets (2022) (1080p BluRay x265 HEVC 10bit AAC 7.1 Tigole)");
		sameFullName("The Expendables (2010)",
				"The Expendables (2010) Extended DC (1080p BluRay x265 HEVC 10bit AAC 7.1 Tigole) [QxR]");
		sameFullName("The Extended Run",
				"The Extended Run (1080p BluRay x265 HEVC 10bit AAC 7.1 Tigole) [QxR]");
		sameFullName("The Extended (2010)",
				"The Extended (2010)");
		sameFullName("The Extended Season 01",
				"The Extended S01");
		sameFullName("F9 (2021)",
				"F9 (2021) DC");
	}
	
	
	public void sameFullName(String correctName, String inputName) {
		NameInfo info = new NameInfo(inputName);
		assertEquals(info.getFullName(), correctName);
	}
	
	

}