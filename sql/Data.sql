INSERT INTO TROLE (ROL_NAME) VALUES ('Alumno');
INSERT INTO TROLE (ROL_NAME) VALUES ('Docente');
INSERT INTO TROLE (ROL_NAME) VALUES ('PAS');

INSERT INTO TEVENT_TYPE (ETYPE_NAME) VALUES ('Clase');
INSERT INTO TEVENT_TYPE (ETYPE_NAME) VALUES ('Exámen');
INSERT INTO TEVENT_TYPE (ETYPE_NAME) VALUES ('Conferencia');
INSERT INTO TEVENT_TYPE (ETYPE_NAME) VALUES ('Otros');

INSERT INTO TBUILDING (BUILD_ID,BUILD_ADDRES,BUILD_NAME,BUILD_ZIPCODE,BUILD_REGION,BUILD_COUNTRY,BUILD_PHONE,BUILD_LATITUDE,BUILD_LONGITUDE,BUILD_IMG_URL,BUILD_URL) VALUES (1,'Campus de Elviña s/n','Facultad de Informática','15071','A Coruña','España','881011299','43.332845','-8.410888999999997','http://www.iearobotics.com/personal/juan/conferencias/conf16/images/facultad.png','www.fic.udc.es');
INSERT INTO TBUILDING (BUILD_ID,BUILD_ADDRES,BUILD_NAME,BUILD_ZIPCODE,BUILD_REGION,BUILD_COUNTRY,BUILD_PHONE,BUILD_LATITUDE,BUILD_LONGITUDE,BUILD_IMG_URL,BUILD_URL) VALUES (2,'Campus de Elviña s/n','Escuela Técnica Superior de Ingeniería de Caminos, Canales y Puertos','15071','A Coruña','España','881011472','43.3335083','-8.409237200000007','http://caminos.udc.es/servicios/icam/res/ESCUELA.JPG','http://caminos.udc.es/');
INSERT INTO TBUILDING (BUILD_ID,BUILD_ADDRES,BUILD_NAME,BUILD_ZIPCODE,BUILD_REGION,BUILD_COUNTRY,BUILD_PHONE,BUILD_LATITUDE,BUILD_LONGITUDE,BUILD_IMG_URL,BUILD_URL) VALUES (3,'Campus de Elviña s/n','Facultad de Economía y Empresa','15071','A Coruña','España','881012454','43.3310393','-8.413000099999977','http://www.elidealgallego.com/media/idealgallego/images/2012/05/12/2012051200490768830.jpg','http://www.economicas.udc.es');
INSERT INTO TBUILDING (BUILD_ID,BUILD_ADDRES,BUILD_NAME,BUILD_ZIPCODE,BUILD_REGION,BUILD_COUNTRY,BUILD_PHONE,BUILD_LATITUDE,BUILD_LONGITUDE,BUILD_IMG_URL,BUILD_URL) VALUES (4,'Campus da Zapateira s/n','Escuela Técnica Superior de Arquitectura ','15071','A Coruña','España','881015011','43.3272492','-8.409226499999932','http://www.elidealgallego.com/media/idealgallego/images/2014/02/18/2014021802292425841.jpg','etsa.udc.es');

INSERT INTO TROOM(ROOM_ID,ROOM_NAME,BUILD_ID) VALUES (1,'Aula 3.0',1);
INSERT INTO TROOM(ROOM_ID,ROOM_NAME,BUILD_ID) VALUES (2,'Aula 3.1',1);

INSERT INTO TEVENT(EVENT_NAME,EVENT_SPEAKER,EVENT_DESC,USER_ID,ETYPE_ID,ROOM_ID)
VALUES ('Clase PESI','Oscar Predreira','Clase del MUEI de PESI',null,1,1);

INSERT INTO TEVENT(EVENT_NAME,EVENT_SPEAKER,EVENT_DESC,USER_ID,ETYPE_ID,ROOM_ID)
VALUES ('Clase APM','Tiago Fernandez','Clase del MUEI de APM',null,1,1);

INSERT INTO TSCHEDULE (SCHEDULE_START_HOUR,SCHEDULE_END_HOUR,SCHEDULE_START_DATE,SCHEDULE_END_DATE,SCHEDULE_DAY_MONDAY,
SCHEDULE_DAY_TUESDAY,SCHEDULE_DAY_WEDNESDAY,SCHEDULE_DAY_THURSDAY,SCHEDULE_DAY_FRIDAY,SCHEDULE_DAY_SATURDAY,SCHEDULE_DAY_SUNDAY,EVENT_ID)
VALUES ('19:30:00','21:30:00','2017-01-23','2017-02-20',TRUE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,1);

INSERT INTO TSCHEDULE (SCHEDULE_START_HOUR,SCHEDULE_END_HOUR,SCHEDULE_START_DATE,SCHEDULE_END_DATE,SCHEDULE_DAY_MONDAY,
SCHEDULE_DAY_TUESDAY,SCHEDULE_DAY_WEDNESDAY,SCHEDULE_DAY_THURSDAY,SCHEDULE_DAY_FRIDAY,SCHEDULE_DAY_SATURDAY,SCHEDULE_DAY_SUNDAY,EVENT_ID)
VALUES ('19:30','21:30:00','2017-01-23','2017-02-20',FALSE,FALSE,TRUE,TRUE,FALSE,FALSE,FALSE,2);

COMMIT;