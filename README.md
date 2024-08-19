## Prerequisites
The dependency is shown below:
| **Dependency** | **Version**      |
|----------------|------------------|
| Java           | 17               |
| Kotlin         | 1.9.24           |
| Spring Boot    | 3.4.0-SNAPSHOT   |


clone the project
```bash
git clone https://github.com/cjy513203427/IADBE_Server.git
cd IADBE_Server/
```

## Development
Our project is developed with IntelliJ IDEA. After cloning, you can run IadbeServerApplication.kt directly.

If you use another IDE or CLI, follow the command below.
Run the Application:
```bash
./gradlew bootRun
```
Again, if you're on Windows:
```bash
gradlew.bat bootRun
```


## Production
<details>
<summary>Run pre-built JAR</summary>
You can run the pre-built JAR file directly:

```bash
cd jar/
java -jar IADBE_Server-0.0.1-SNAPSHOT.jar
```
</details>

<details>
<summary>Docker</summary>

```bash
# Build docker image
docker build -t iadbe_server .
# Run docker container
docker run --rm --name iadbe_server -p 8080:8080 iadbe_server
```

</details>

