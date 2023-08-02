# ganyu

![ganyu](https://github.com/ksewen/ganyu/actions/workflows/ci.yml/badge.svg)
[![codecov](https://codecov.io/gh/ksewen/ganyu/branch/release/graph/badge.svg?token=GGQISY2M7R)](https://codecov.io/gh/ksewen/ganyu)
![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.6-blue.svg)

Ein kleiner Helfer für meine Familie und Freunde.

## Einführung

Wegen des Interesses wurde dieses Projekt erstellt. Momentan ist es in Bearbeitung. In diesem Projekt finden Sie nur
Code für den Backend-Server. In Zukunft werde ich Frontend-Seiten [ganyu-web](https://github.com/ksewen/ganyu-web) und
noch eine App für IOS anbieten.

## Konventionen der Codierung

- Wenn die Kontrolllogik ungeeignet ist, markieren Sie sie mit Kommentaren wie **// TODO** oder **// FIXME**.
- Der Codestil (Standards) [google](https://google.github.io/styleguide/javaguide.html).
- Alle drei Schichten von „Controller/Service/DAO(Repository)“ können **nicht** weggelassen werden.
- Die SOLID-Prinzipien sind willkommen.

## Wie erhält man Informationen von aktuellem Benutzer

Nach der Authentifizierung wurden die Benutzerinformationen im Objekt „com.github.ksewen.ganyu.security.Authentication“ gespeichert. Sie können es an der Stelle einfügen, an der Sie das Benutzerobjekt verwenden und über die Methode des Objekts „com.github.ksewen.ganyu.security.Authentication“ abrufen möchten.  

Der folgende Code zeigt, wie Informationen von aktuellem Benutzer abgerufen werden.

```java 
// Zuerst sollte Sie es an der Stelle einfügen, an der Sie das Benutzerobjekt verwenden möchten.
@Autowired
private Authentication authentication;

// userId erhalten
Long userId = authentication.getUserId();

// der Name des Benutzers erhalten
String username = authentication.getUsername();
```

## So führen Sie es auf Ihrem eigenen Computer aus
### Docker
#### Image erstellen
```shell
# Am Anfang klonen Sie den Code auf Ihren Computer und wechseln Sie zum Hauptverzeichnis. Und dann führen Sie den folgenden Code aus.
resources/scripts/build-image.sh -d ..
```
Benutzen Sie „./build-image.sh -h“, um andere Optionen zu erkennen. 

Warten Sie einen Moment und bekommen Sie ein Docker Image.  

#### Mit docker-compose ausführen
Ein Beispiel Datei sehen Sie hier:  
```yaml
services:
  ganyu:
    image: ksewen/ganyu:1.0.0.RELEASE # Ihre Konfiguration für „IMAGE_NAME“
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/ganyu?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&nullCatalogMeansCurrent=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 123456
      SPRING_MAIL_HOST: # Ihr Anbieter der E-Mail 
      SPRING_MAIL_PORT: # Port des Anbieters
      SPRING_MAIL_USERNAME: # Ihr Benutzername der E-Mail
      SPRING_MAIL_PASSWORD: # Ihr Passwort der E-Mail
      DEPENDENCY: mysql
      DEPENDENCY_PORT: 3306
    ports:
      - 8080:8080
    networks:
      - ganyu_net

  mysql:
    platform: linux/x86_64
    image: mysql:5.7.42
    command: --default-authentication-plugin=mysql_native_password
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: ganyu
      MYSQL_ROOT_HOST: "%"
    ports:
      - 3306:3306
    networks:
      - ganyu_net

networks:
  ganyu_net:
    driver: bridge
```
Mit diesem Beispiel können Sie einen neuen Service starten.
```shell
docker-compose -f docker-compose.yaml up
```