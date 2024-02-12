use proiectMES;

DROP TRIGGER IF EXISTS notatie;
DELIMITER //

CREATE TRIGGER notatie BEFORE INSERT ON note 
FOR EACH ROW
	BEGIN
		CASE 
			WHEN new.notatie_muz LIKE 'C%' THEN SET new.notatie_uzuala = 'do';
            WHEN new.notatie_muz LIKE 'D%' THEN SET new.notatie_uzuala = 're';
            WHEN new.notatie_muz LIKE 'E%' THEN SET new.notatie_uzuala = 'mi';
            WHEN new.notatie_muz LIKE 'F%' THEN SET new.notatie_uzuala = 'fa';
            WHEN new.notatie_muz LIKE 'G%' THEN SET new.notatie_uzuala = 'sol';
            WHEN new.notatie_muz LIKE 'A%' THEN SET new.notatie_uzuala = 'la';
            WHEN new.notatie_muz LIKE 'B%' THEN SET new.notatie_uzuala = 'si';
		END CASE;
        
        CASE 
			WHEN new.notatie_muz LIKE '%2' THEN SET new.octava = 'octava mica';
            WHEN new.notatie_muz LIKE '%3' THEN SET new.octava = 'octava 1';
            WHEN new.notatie_muz LIKE '%4' THEN SET new.octava = 'octava 2';
            WHEN new.notatie_muz LIKE '%5' THEN SET new.octava = 'ocatava 3';
		END CASE;
	END //

DELIMITER ;