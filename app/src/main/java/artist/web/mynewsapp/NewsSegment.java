package artist.web.mynewsapp;

/**
 * Created by User on 7/16/2017.
 */

public class NewsSegment {

    /**
     * News Title
     */
    private String newsTitle;

    /**
     * News Section
     */
    private String newsSection;

    /**
     * News Published Date
     */
    private String newsPublishedDate;

    /**
     * News Author
     */
    private String newsAuthor;
    /**
     * News Web URL
     */
    private String newsUrl;

    public NewsSegment(String newsTitle, String newsSection,
                       String newsPublishedDate, String newsAuthor, String newsUrl) {
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsPublishedDate = newsPublishedDate;
        this.newsAuthor = newsAuthor;
        this.newsUrl = newsUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsPublishedDate() {
        return newsPublishedDate;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}
