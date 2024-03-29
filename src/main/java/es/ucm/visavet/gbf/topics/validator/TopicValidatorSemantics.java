package es.ucm.visavet.gbf.topics.validator;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import es.ucm.visavet.gbf.app.domain.Topic;
import es.ucm.visavet.gbf.topics.manager.ITopicsManager;

public class TopicValidatorSemantics {
	private ITopicsManager topicsManager;
	private String definedTopic;
	private List<Topic> references;

	public TopicValidatorSemantics(String definedTopic, ITopicsManager topicsManager) {
		this.definedTopic = definedTopic;
		this.topicsManager = topicsManager;
		this.references = Lists.newArrayList();
	}

	public void validateReferenceToTopic(String reference) throws ParseException {
		String refName = reference.substring(1);
		if (!topicsManager.existsTopic(refName))
			errorTopicDoesNotExists(reference);
		List<String> cyclicPath = new LinkedList<String>();
		cyclicPath.add(definedTopic);
		if (cyclicDependency(refName, cyclicPath))
			errorCyclicDependency(cyclicPath);
		references.add(topicsManager.getTopic(refName));
	}

	public void validateSourceType(String sourceType) throws ParseException {
		String type = sourceType.substring(2);
		if (!topicsManager.existsSourceType(type))
			errorSourceTypeDoesNotExists(type);
	}

	public void validateSourceLocation(String sourceLoc) throws ParseException {
		String loc = sourceLoc.substring(2);
		if (!topicsManager.existsSourceLocation(loc))
			errorSourceLocationDoesNotExists(loc);
	}

	private boolean cyclicDependency(String topic, List<String> path) {
		path.add(topic);
		if (topic.equals(definedTopic))
			return true;
		else {
			for (String referedTopic : topicsManager.getDependencies(topic)) {
				if (cyclicDependency(referedTopic, path))
					return true;
			}
			path.remove(path.size() - 1);
			return false;
		}
	}

	private void errorTopicDoesNotExists(String topic) throws ParseException {
		throw new TopicDoesNotExistsException(topic);
	}

	private void errorCyclicDependency(List<String> cyclicPath) throws ParseException {
		throw new CyclicDependencyException(cyclicPath);
	}

	private void errorSourceTypeDoesNotExists(String type) throws ParseException {
		throw new SourceTypeDoesNotExistsException(type);
	}

	private void errorSourceLocationDoesNotExists(String loc) throws ParseException {
		throw new SourceLocationDoesNotExistsException(loc);
	}

	public List<Topic> getReferences() {
		return references;
	}

	public void setReferences(List<Topic> references) {
		this.references = references;
	}

}
