package NameInfo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import DataStructures.NameInfo;

public class TVSeriesTest {

	@Test
	public void testHyphenFormatting() {

		/*// valid single hyphen
		sameFullNameSeason(
				"Show-Name",
				"01",
				"Show-Name Season 01"
		);

		// space before hyphen
		sameFullNameSeason(
				"Show Name",
				"01",
				"Show -Name Season 01"
		);

		// space after hyphen
		sameFullNameSeason(
				"Show Name",
				"01",
				"Show- Name Season 01"
		);

		// spaces on both sides
		sameFullNameSeason(
				"Show - Name",
				"01",
				"Show - Name Season 01"
		);

		// multiple hyphens
		sameFullNameSeason(
				"ShowName",
				"01",
				"Show--Name Season 01"
		);

		sameFullNameSeason(
				"ShowName",
				"01",
				"Show------Name Season 01"
		);

		// dot before hyphen (.- not allowed)
		sameFullNameSeason(
				"Show Name",
				"01",
				"Show.-Name Season 01"
		);

		// dot + space + hyphen
		sameFullNameSeason(
				"Show Name",
				"01",
				"Show. -Name Season 01"
		);
		
		// space + dot + hyphen
		sameFullNameSeason(
				"Show Name",
				"01",
				"Show -.Name Season 01"
		);
		
		// dot after hyphen (-. not allowed)
		sameFullNameSeason(
				"Show Name",
				"01",
				"Show-. Name Season 01"
		);
		
		// dot + hyphen + dot
		sameFullNameSeason(
				"Show - Name",
				"01",
				"Show.-.Name Season 01"
		);
		
		// space + dot + hyphen + dot + space
		sameFullNameSeason(
				"Show - Name",
				"01",
				"Show .-. Name Season 01"
		);*/
		
		// valid single hyphen
		sameNameInfo(
		        new NameInfo().setName("Show-Name").setSeason("01"),
		        "Show-Name Season 01"
		);

		// space before hyphen
		sameNameInfo(
		        new NameInfo().setName("Show Name").setSeason("01"),
		        "Show -Name Season 01"
		);

		// space after hyphen
		sameNameInfo(
		        new NameInfo().setName("Show Name").setSeason("01"),
		        "Show- Name Season 01"
		);

		// spaces on both sides
		sameNameInfo(
		        new NameInfo().setName("Show - Name").setSeason("01"),
		        "Show - Name Season 01"
		);

		// multiple hyphens
		sameNameInfo(
		        new NameInfo().setName("ShowName").setSeason("01"),
		        "Show--Name Season 01"
		);

		sameNameInfo(
		        new NameInfo().setName("ShowName").setSeason("01"),
		        "Show------Name Season 01"
		);

		// dot before hyphen (.- not allowed)
		sameNameInfo(
		        new NameInfo().setName("Show Name").setSeason("01"),
		        "Show.-Name Season 01"
		);

		// dot + space + hyphen
		sameNameInfo(
		        new NameInfo().setName("Show Name").setSeason("01"),
		        "Show. -Name Season 01"
		);

		// space + dot + hyphen
		sameNameInfo(
		        new NameInfo().setName("Show Name").setSeason("01"),
		        "Show -.Name Season 01"
		);

		// dot after hyphen (-. not allowed)
		sameNameInfo(
		        new NameInfo().setName("Show Name").setSeason("01"),
		        "Show-. Name Season 01"
		);

		// dot + hyphen + dot
		sameNameInfo(
		        new NameInfo().setName("Show - Name").setSeason("01"),
		        "Show.-.Name Season 01"
		);

		// space + dot + hyphen + dot + space
		sameNameInfo(
		        new NameInfo().setName("Show - Name").setSeason("01"),
		        "Show .-. Name Season 01"
		);
		
		
		sameNameInfo(
				new NameInfo().setName("Supernatural").setYear("2005").setSeason("05"),
				"Supernatural_(2005)_Season_05"
		);
		
		sameNameInfo(
				new NameInfo().setName("Supernatural").setYear("2005").setSeason("05"),
				"Supernatural_2005_Season_05"
		);

		// parentheses with hyphen
		sameNameInfo(
				new NameInfo().setName("Supernatural").setYear("2005").setSeason("05"),
				"Supernatural (2005)-Season 05"
		);
		
		sameFullName(
				"Supernatural (2005) Episode 05",
				"Supernatural (2005)-Episode 05"
		);

		// your original examples
		sameFullName(
				"Supernatural (2005) Season 05",
				"Supernatural (2005) Season 05"
		);

		sameFullName(
				"Supernatural (2005) Season 05",
				"Supernatural (2005) -Season 05"
		);

		sameFullName(
				"Supernatural Season 05",
				"Supernatural -Season 05"
		);
	}
	
	public void sameFullNameSeason(String correctName, String season, String inputName) {
		NameInfo info = new NameInfo(inputName);
		assertEquals(correctName, info.getName());
		assertEquals(true, info.hasSeason());
		assertEquals(false, info.hasEpisode());
		assertEquals(season, info.getSeason());
	}
	
	public void sameFullName(String correctName, String inputName) {
		NameInfo info = new NameInfo(inputName);
		assertEquals(correctName, info.getFullName());
	}
	
	public void sameNameInfo(NameInfo correctInfo, String inputName) {
		NameInfo info = new NameInfo(inputName);
		assertEquals(correctInfo.getNameWithoutYear(), info.getNameWithoutYear());
		assertEquals(correctInfo.getYear(), info.getYear());
		assertEquals(correctInfo.getSeason(), info.getSeason());
		assertEquals(correctInfo.getEpisode(), info.getEpisode());
		assertEquals(correctInfo.getDescription(), info.getDescription());
	}
}
