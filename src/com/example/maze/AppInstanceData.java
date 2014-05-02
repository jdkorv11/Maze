package com.example.maze;

public class AppInstanceData {
	
	private static boolean syncDownloaded = false;

	public static boolean isSyncDownloaded() {
		return syncDownloaded;
	}

	public static void setSyncDownloaded(boolean syncDownloaded) {
		AppInstanceData.syncDownloaded = syncDownloaded;
	}
	
	

}
