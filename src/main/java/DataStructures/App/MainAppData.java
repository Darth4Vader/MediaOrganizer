package DataStructures.App;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import DataStructures.Json.DataInformation;

public class MainAppData {
	
	@JsonProperty("count_id")
	private int countID;
	
	@JsonProperty(DataInformation.MANAGE_FOLDER_HISTORY_lIST)
	private List<ManageFolderHistory> manageFoldersHistory;

	@JsonCreator
	public MainAppData() {
		this.manageFoldersHistory = new ArrayList<>();
	}
	
	public List<ManageFolderHistory> getManageFoldersHistory() {
		return this.manageFoldersHistory;
	}
	
	public ManageFolderHistory addManageFolderHistory(ManageFolderPojo pojo) {
		ManageFolderHistory history = new ManageFolderHistory(pojo, countID++);
		this.manageFoldersHistory.add(history);
		return history;
	}
	
	public void addManageFolderHistory(ManageFolderHistory pojo) {
		this.manageFoldersHistory.add(pojo);
		pojo.updateLastAccess();
	}
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	public static class ManageFolderHistory {
		
		@JsonProperty("id")
		private int id;
		
		private ManageFolderPojo manage;
		
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss")
		private LocalDateTime lastAccess;
		
		private String customName;
		
		@JsonCreator
		ManageFolderHistory() {}
		
		@JsonIgnore
		public ManageFolderHistory(ManageFolderPojo manage, int countID) {
			this.manage = manage;
			this.id = countID;
			updateLastAccess();
		}
		
		public int getId() {
			return id;
		}

		public ManageFolderPojo getManage() {
			return manage;
		}

		public LocalDateTime getLastAccess() {
			return lastAccess;
		}
		
		public String getCustomName() {
			return this.customName;
		}
		
		public void setManage(ManageFolderPojo manage) {
			this.manage = manage;
		}
		
		public void setCustomName(String customName) {
			this.customName = customName;
		}
		
		final void updateLastAccess() {
			this.lastAccess = LocalDateTime.now();
		}
		
		@Override
		public boolean equals(Object object) {
			if(object == null) return false;
			if(this == object) return true;
			if(!(object instanceof ManageFolderHistory history)) return false;
			return Objects.equals(this.id, history.id);
		}
	}
}