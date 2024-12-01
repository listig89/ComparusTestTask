# Comparus Test Task Project

Comparus Test Task Project is a one which is a result of test task implementation according to requirements provided
in [task description PDF](Test%20task%20for%20Java%20Developer.pdf)

## Steps to run this project

1. Build it

```bash
./gradlew build
```

2. Run in docker container (including databases)

```bash
docker-compose -f docker-compose.yml up -d
```

3. Wait until containers are up (watch output)
4. Check how it works in browser or use simple GET request:

```http request
GET http://localhost:8080/users
```

5. Use different requests to check filter work:

<ul>
    <li>http://localhost:8080/users?id=20003</li>
    <li>http://localhost:8080/users?username=mysql-ldap-user-name-5</li>
    <li>http://localhost:8080/users?name=mysql-ldap-name-6</li>
    <li>http://localhost:8080/users?surname=maria-saml-surname-6li>
</ul>