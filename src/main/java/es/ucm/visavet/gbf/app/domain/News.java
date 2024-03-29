package es.ucm.visavet.gbf.app.domain;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class News extends BaseEntity {
	
	public static String fieldTitle = "title";
	public static String fieldTitleNoCase = "titleN";
	public static String fieldBody = "body";
	public static String fieldBodyNoCase = "bodyN";
	public static String fieldDatePub = "datePub";
	public static String fieldDateCreate = "dateCreate";
	public static String fieldUrl = "id";
	public static String fieldSite = "feed";
	public static String fieldSiteLoc = "siteLoc";
	public static String fieldSiteType = "siteType";
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	@Lob
	private String title;
	@Lob
	private String content;
	private Long site;
	@Lob
	private String url;
	private Date pubDate = new Date(System.currentTimeMillis());

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getSite() {
		return site;
	}

	public void setSite(Long site) {
		this.site = site;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	
	public static class Comparators {

        public static Comparator<News> PUBDATE = new Comparator<News>() {
            @Override
            public int compare(News o1, News o2) {
                return o1.getPubDate().compareTo(o2.getPubDate());
            }
        };
    }
	
}
