package es.ucm.visavet.gbf.app.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.google.common.collect.Lists;

@Entity
public class Feed extends BaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	private String name;
	private String dateFormat;
	@Enumerated(EnumType.STRING)
	private Language languaje = Language.SPANISH;
	@Lob
	private String lastNewsLink = "";
	@Lob
	private String urlNews;
	private boolean isRSS = false;
	private boolean isAuto = false;
	@Enumerated(EnumType.ORDINAL)
	private WebLevel type = WebLevel.red;
	@Enumerated(EnumType.ORDINAL)
	private FeedTypeEnum feedType = FeedTypeEnum.general;
	@Enumerated(EnumType.ORDINAL)
	@ElementCollection(fetch = FetchType.EAGER)
	private List<FeedPlaceEnum> feedPlace = Lists.newArrayList(FeedPlaceEnum.general);
	private Integer numNewNews;
	private Date dateFirstNews;
	private Timestamp ultimaRecuperacion;
	private boolean actived = true;
	private String comment;
	private Integer minRefresh = 120;
	private String selectorTitle;
	private String selectorContent;
	private String selectorPubDate;
	private boolean selectorTitleMeta;
	private boolean selectorContentMeta;
	private boolean selectorPubDateMeta;
	@Enumerated(EnumType.STRING)
	private CharsetEnum charSet = CharsetEnum.UTF8;
	@Enumerated(EnumType.ORDINAL)
	private UpdateStateEnum state = UpdateStateEnum.WAIT;
	private String newsLink;
	private ExtractionType extractionType = ExtractionType.ARTICLE_EXTRACTOR;
	@Lob
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> crawlerNews;
	
	@Transient
	private boolean updateIndex=false;
	@Transient
	private String nextExecution="";

	public Feed() {
	}

	public Feed(FeedForm feed) {
		this.name = feed.getName();
		this.dateFormat = feed.getDateFormat();
		this.languaje = feed.getLanguaje();
		this.isRSS = feed.getIsRSS();
		this.urlNews = feed.getUrlNews();
		this.newsLink = feed.getNewsLink();
		this.type = feed.getType();
		this.minRefresh = feed.getMinRefresh();
		this.selectorContent = feed.getSelectorContent();
		this.selectorContentMeta = feed.getSelectorContentMeta();
		this.selectorTitle = feed.getSelectorTitle();
		this.selectorTitleMeta = feed.getSelectorTitleMeta();
		this.selectorPubDate = feed.getSelectorPubDate();
		this.selectorPubDateMeta = feed.getSelectorPubDateMeta();
		this.actived = feed.isActived();
		this.charSet = feed.getCharSet();
		this.feedPlace = feed.getFeedPlace();
		this.feedType = feed.getFeedType();
		this.extractionType = feed.getExtractionType();
		this.isAuto = feed.getIsAuto();
	}

	public void changeValues(FeedForm feed) {
		if (this.feedPlace.hashCode() != feed.getFeedPlace().hashCode() || this.getFeedType().hashCode() != feed.getFeedType().hashCode())
			this.updateIndex=true;
		this.name = feed.getName();
		this.dateFormat = feed.getDateFormat();
		this.languaje = feed.getLanguaje();
		this.urlNews = feed.getUrlNews();
		this.isRSS = feed.getIsRSS();
		this.newsLink = feed.getNewsLink();
		this.type = feed.getType();
		this.minRefresh = feed.getMinRefresh();
		this.selectorContent = feed.getSelectorContent();
		this.selectorContentMeta = feed.getSelectorContentMeta();
		this.selectorTitle = feed.getSelectorTitle();
		this.selectorTitleMeta = feed.getSelectorTitleMeta();
		this.selectorPubDate = feed.getSelectorPubDate();
		this.selectorPubDateMeta = feed.getSelectorPubDateMeta();
		this.actived = feed.isActived();
		this.charSet = feed.getCharSet();
		this.feedPlace = feed.getFeedPlace();
		this.feedType = feed.getFeedType();
		this.extractionType = feed.getExtractionType();
		this.isAuto = feed.getIsAuto();
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WebLevel getType() {
		return type;
	}

	public void setType(WebLevel type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Language getLanguaje() {
		return languaje;
	}

	public void setLanguaje(Language languaje) {
		this.languaje = languaje;
	}

	public String getLastNewsLink() {
		return lastNewsLink;
	}

	public void setLastNewsLink(String lastNewsLink) {
		this.lastNewsLink = lastNewsLink;
	}

	public String getUrlNews() {
		return urlNews;
	}

	public void setUrlNews(String urlNews) {
		this.urlNews = urlNews;
	}

	public boolean isRSS() {
		return isRSS;
	}

	public void setRSS(boolean isRSS) {
		this.isRSS = isRSS;
	}
	
	public String getNewsLink() {
		return newsLink;
	}

	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}

	public Integer getNumNewNews() {
		return numNewNews;
	}

	public void setNumNewNews(Integer numNewNews) {
		this.numNewNews = numNewNews;
	}

	public Timestamp getUltimaRecuperacion() {
		return ultimaRecuperacion;
	}

	public void setUltimaRecuperacion(Timestamp ultimaRecuperacion) {
		this.ultimaRecuperacion = ultimaRecuperacion;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getMinRefresh() {
		return minRefresh;
	}

	public void setMinRefresh(Integer minRefresh) {
		this.minRefresh = minRefresh;
	}

	public String getSelectorTitle() {
		return selectorTitle;
	}

	public void setSelectorTitle(String selectorTitle) {
		this.selectorTitle = selectorTitle;
	}

	public String getSelectorContent() {
		return selectorContent;
	}

	public void setSelectorContent(String selectorContent) {
		this.selectorContent = selectorContent;
	}

	public String getSelectorPubDate() {
		return selectorPubDate;
	}

	public void setSelectorPubDate(String selectorPubDate) {
		this.selectorPubDate = selectorPubDate;
	}

	public boolean getSelectorTitleMeta() {
		return selectorTitleMeta;
	}

	public void setSelectorTitleMeta(boolean selectorTitleMeta) {
		this.selectorTitleMeta = selectorTitleMeta;
	}

	public boolean getSelectorContentMeta() {
		return selectorContentMeta;
	}

	public void setSelectorContentMeta(boolean selectorContentMeta) {
		this.selectorContentMeta = selectorContentMeta;
	}

	public boolean getSelectorPubDateMeta() {
		return selectorPubDateMeta;
	}

	public void setSelectorPubDateMeta(boolean selectorPubDateMeta) {
		this.selectorPubDateMeta = selectorPubDateMeta;
	}

	public Date getDateFirstNews() {
		return dateFirstNews;
	}

	public void setDateFirstNews(Date dateFirstNews) {
		this.dateFirstNews = dateFirstNews;
	}

	public CharsetEnum getCharSet() {
		return charSet;
	}

	public void setCharSet(CharsetEnum charSet) {
		this.charSet = charSet;
	}	

	public UpdateStateEnum getState() {
		return state;
	}

	public void setState(UpdateStateEnum state) {
		this.state = state;
	}

	public FeedTypeEnum getFeedType() {
		return feedType;
	}

	public void setFeedType(FeedTypeEnum feedType) {
		this.feedType = feedType;
	}

	public List<FeedPlaceEnum> getFeedPlace() {
		return feedPlace;
	}

	public void setFeedPlace(List<FeedPlaceEnum> feedPlace) {
		this.feedPlace = feedPlace;
	}

	public boolean isUpdateIndex() {
		return updateIndex;
	}

	public void setUpdateIndex(boolean updateIndex) {
		this.updateIndex = updateIndex;
	}

	public String getNextExecution() {
		return nextExecution;
	}

	public void setNextExecution(String nextExecution) {
		this.nextExecution = nextExecution;
	}

	public ExtractionType getExtractionType() {
		return extractionType;
	}

	public void setExtractionType(ExtractionType extractionType) {
		this.extractionType = extractionType;
	}

	public List<String> getCrawlerNews() {
		if (this.crawlerNews==null)
			this.crawlerNews= Lists.newArrayList();
		return crawlerNews;
	}

	public void setCrawlerNews(List<String> crawlerNews) {
		this.crawlerNews = crawlerNews;
	}

	public boolean isAuto() {
		return isAuto;
	}
	
	public boolean getIsAuto() {
		return isAuto;
	}
	
	public boolean getIsRSS() {
		return isRSS;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}
	
	
	
}
