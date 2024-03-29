package es.ucm.visavet.gbf.app.domain.topic;

import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

public abstract class AccessibleHitCollector implements Collector {

	  protected Scorer scorer;
	  protected boolean shouldScore;
	  protected int docBase;
	  protected boolean outOfOrder;

	  public abstract int getTotalHits();
	  
	  public abstract int getDocId(int pos);
	  
	  public abstract float getScore(int pos);
	  
	  public abstract void reset();

}
