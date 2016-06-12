DELIMITER //
DROP TRIGGER update_score//
CREATE TRIGGER update_score BEFORE INSERT ON news_detect
    FOR EACH ROW BEGIN
    SET @prov = (COALESCE((Select 1 from news_detect_locations where news LIKE NEW.link limit 1),0));
	SET @feed = (COALESCE((SELECT feed.type from feed where feed.id = NEW.site_code_name limit 1),0));
	SET @alert = (COALESCE((select COALESCE(alert.type,risk.type) as val from alert, risk where alert.id = NEW.alert_detect_id or risk.id = NEW.alert_detect_id limit 1),0));
    SET NEW.score_lucene = NEW.score;
	SET NEW.score = NEW.score * (@feed+1) * (@alert+1) * (1+@prov*2);
	END//
    
DELIMITER ;