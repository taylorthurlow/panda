package edu.TaylorThurlow;

import java.io.File;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class FileMP3
{

    private String path;
    private String artist;
    private String album;
    private String title;
    private int track;
    private int totalTracks;
    private int year;
    private Duration length;
    private String genre;
    private Image artwork = null;
    private String trackProxy;
    private String totalTracksProxy;
    private String yearProxy;
    //private String lengthProxy;

    private String[] validNames =
    {
        "artwork", "cover", "folder", "art"
    };

    public FileMP3(final String path) throws Exception
    {
        File fileToReadMetadata = new File(path);
        Media media = new Media(fileToReadMetadata.toURI().toURL().toString());
        final MediaPlayer tester = new MediaPlayer(media);
        final ObservableMap<String, Object> data = media.getMetadata();
        tester.setOnReady(new Runnable()
        {
            @Override
            public void run()
            {
                setArtist((String) data.get("artist"));
                setAlbum((String) data.get("album"));
                setTitle((String) data.get("title"));
                setGenre((String) data.get("genre"));
                setTrack((int) data.get("track number"));
                setTotalTracks((int) data.get("track count"));
                setYear(((int) data.get("year")));
                setLength((Duration) data.get("duration"));
                setArtwork((Image) data.get("image"));
                setPath(path);

                if (artwork == null)
                {
                    System.out.println("No artwork found in tags, searching directory.");
                    String fileName = path.substring(path.lastIndexOf('\\'), path.length());
                    String containingFolder = path.substring(0, path.length() - fileName.length());

                    System.out.println("Containing folder: " + containingFolder);
                    Boolean foundFile = false;
                    String foundName = "";
                    for (String name : validNames)
                    {
                        String searchFor = "\\" + containingFolder + "\\" + name + ".jpg";
                        System.out.println("Searching for: " + searchFor);
                        File f = new File(searchFor);
                        if (f.exists())
                        {
                            System.out.println("Found artwork in folder: " + containingFolder + "\\" + name + ".jpg");
                            foundFile = true;
                            foundName = name;
                            break;
                        }
                    }

                    if (foundFile)
                    {
                        System.out.println("Found " + foundName);
                        URL resource = getClass().getResource(containingFolder + "\\" + name + ".jpg");
                        setArtwork(new Image(new File(resource).getAbsolutePath().));
                    }
                }

                musicPlayer.getInstance().addMp3ToList(FileMP3.this);
            }
        });

    }

    /**
     * @return the path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * @return the artist
     */
    public String getArtist()
    {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    /**
     * @return the album
     */
    public String getAlbum()
    {
        return album;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(String album)
    {
        this.album = album;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the track
     */
    public int getTrack()
    {
        return track;
    }

    /**
     * @param track the track to set
     */
    public void setTrack(int track)
    {
        this.track = track;
        this.trackProxy = Integer.toString(track);
    }

    /**
     * @return the year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year)
    {
        this.year = year;
        this.yearProxy = Integer.toString(year);
    }

    /**
     * @return the length
     */
    public Duration getLength()
    {
        return length;
    }

    public void setLength(Duration length)
    {
        this.length = length;

        //int s = (int) length.toSeconds();
        //String formatted = String.format("%02d:%02d", (s%3600)/60, s%60);
        //this.lengthProxy = formatted;
    }

    /**
     * @return the artwork
     */
    public Image getArtwork()
    {
        return artwork;
    }

    /**
     * @param artwork the artwork to set
     */
    public void setArtwork(Image artwork)
    {
        this.artwork = artwork;
    }

    /**
     * @return the validNames
     */
    public String[] getValidNames()
    {
        return validNames;
    }

    /**
     * @param validNames the validNames to set
     */
    public void setValidNames(String[] validNames)
    {
        this.validNames = validNames;
    }

    /**
     * @return the genre
     */
    public String getGenre()
    {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    @Override
    public String toString()
    {
        return this.artist + " - " + this.album + " - " + this.track + " - " + this.title + " - " + this.genre;
    }

    /**
     * @return the totalTracks
     */
    public int getTotalTracks()
    {
        return totalTracks;
    }

    /**
     * @param totalTracks the totalTracks to set
     */
    public void setTotalTracks(int totalTracks)
    {
        this.totalTracks = totalTracks;
        this.totalTracksProxy = Integer.toString(totalTracks);
    }
}
