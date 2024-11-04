package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import charting.Song;

public class Songs {
    public static List<Song> songs = new ArrayList<>();

    public static void load() {
        String songsDirectory = "Songs"; 
        File dir = new File(songsDirectory);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".mp3") || name.endsWith(".wav")); // Adjust file extensions as needed
            
            if (files != null) {
                for (File file : files) {
                    songs.add(new Song(file.getName())); // Assuming Song constructor takes the file name or path
                }
            }
        } else {
            System.out.println("Songs directory does not exist or is not a directory.");
        }
    }
}
