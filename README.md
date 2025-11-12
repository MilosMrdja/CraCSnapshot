# Spring PetClinic with CRaC Support

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0--M3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![CRaC](https://img.shields.io/badge/CRaC-1.4.0-blue.svg)](https://github.com/CRaC/crac)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

Spring PetClinic is a sample web application that demonstrates the use of Spring Boot framework with CRaC (Coordinated Restore at Checkpoint) technology integration for dramatically faster application startup time.

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [CRaC Features](#crac-features)
- [How to Run](#how-to-run)
  - [Option 1: Traditional Startup (Cold Start)](#option-1-traditional-startup-cold-start)
  - [Option 2: Docker-based Checkpoint/Restore](#option-2-docker-based-checkpointrestore-recommended---part-i---cli-tool)
  - [Command Line Interface - Part 2](#command-line-interface---part-2---triggerable-seamless-solution)
- [CLI vs Scripts - Pros and Cons](#part-1-cli-vs-scripts---pros-and-cons)
- [Cold Start vs Restore Startup Comparison](#part-2-cold-start-vs-restore-startup-comparison)
- [License](#license)

## 🎯 Overview

This project is based on the [Spring PetClinic](https://github.com/spring-projects/spring-petclinic) sample application with added support for CRaC (Coordinated Restore at Checkpoint). CRaC enables creating a checkpoint of a Java application and later restoring from the checkpoint, resulting in **15-40x faster startup time** compared to traditional cold start approach.

### What is CRaC?

CRaC (Coordinated Restore at Checkpoint) is a technology that enables:

- **Checkpoint creation**: Capturing the current state of a Java application (heap, threads, state)
- **Restore**: Fast restoration of the application to a previously captured state
- **Instant readiness**: The application is immediately ready for requests without JIT warmup

## 📦 Prerequisites

Before you begin working on the project, you need to have the following installed:

### Basic Tools

- **Java 25** or newer (full JDK, not JRE)
  - For build: Java 25+
  - For CRaC: Java 21+ (with CRaC support)
  - **Important**: CRaC only works with Azul Zulu JVM
- **Maven 3.6+** or use the `./mvnw` wrapper
- **Git** command line tool
- **Docker 20.10+** (for Docker-based checkpoint/restore)

### IDE (Optional)

- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Eclipse](https://www.eclipse.org/) with m2e plugin
- [Spring Tools Suite](https://spring.io/tools)
- [VS Code](https://code.visualstudio.com)

### Docker (For CRaC)

- **Docker Desktop** or **Docker Engine** 20.10+
- Docker capabilities:
  - `CHECKPOINT_RESTORE`
  - `SYS_PTRACE`

## ✨ CRaC Features

- ✅ **Docker-based Checkpoint/Restore**: PowerShell scripts for Windows
- ✅ **Bash Scripts**: Linux-based checkpoint creation
- ✅ **REST API Endpoint**: `/checkpoint` endpoint for checkpoint creation
- ✅ **Command Line Interface**: Console-based commands (`/create-snapshot`, `/help`)
- ✅ **Command Pattern**: Extensible command system
- ✅ **Factory Pattern**: Command factory for dynamic command addition
- ✅ **Resource Management**: CRaC Resource interface for lifecycle callbacks

## 🚀 How to Run

### Option 1: Traditional Startup (Cold Start)

#### Build the project

```bash
# Clone repository
git clone <repository-url>
cd spring-petclinic

# Build the project
./mvnw clean package

# Or use Maven directly
mvn clean package
```

#### Run the application

```bash
# Run the JAR file
java -jar target/spring-petclinic-4.0.0-SNAPSHOT.jar

# Or use Maven plugin
./mvnw spring-boot:run
```

#### Access the application

- **Web UI**: http://localhost:8080

### Option 2: Docker-based Checkpoint/Restore (Recommended) - Part I - CLI Tool

#### Step 1: Build Docker Image

```bash
# Build Maven project
./mvnw clean package -DskipTests

# Build Docker image
docker build -t petclinic .
```

#### Step 2: Start the application

**On Windows (PowerShell):**

```powershell
.\checkpoint.ps1
```

**On Linux:**

```bash
docker run -d \
  --cap-add=CHECKPOINT_RESTORE \
  --cap-add=SYS_PTRACE \
  -p 8080:8080 \
  --name petclinic_container \
  -v "$(pwd)/crac-files:/opt/crac-files" \
  petclinic \
  java -XX:CRaCCheckpointTo=/opt/crac-files -jar /opt/app/petclinic.jar
```

**Wait**: Wait 5-10 seconds for the application to start.

#### Step 3: Create Checkpoint - Open new PowerShell (Windows)
```powershell
docker exec -it petclinic_container /opt/app/create_checkpoint.sh
```
**Wait**: Wait a few second for creation of the checkpoint. After that your application should be down.

#### Step 4: Restore from Checkpoint

**On Windows (PowerShell):**

```powershell
.\restore.ps1
```

**On Linux:**

```bash
docker run -d \
  --cap-add=CHECKPOINT_RESTORE \
  --cap-add=SYS_PTRACE \
  -p 8081:8080 \
  --name petclinic_restored \
  -v "$(pwd)/crac-files:/opt/crac-files" \
  petclinic \
  java -XX:CRaCRestoreFrom=/opt/crac-files
```

#### Step 5: Verification

```bash
# Check if the application is running
curl http://localhost:8081

```

### Command Line Interface - Part 2 - Triggerable Seamless Solution

The application provides a CLI for checkpoint operations that enables seamless checkpoint creation and restoration workflow.


#### Step 1: Build Docker Image

```bash
# Build Maven project
./mvnw clean package -DskipTests

# Build Docker image
docker build -t petclinic .
```

#### Step 2: Start the application

**On Windows (PowerShell):**

```powershell
.\checkpoint.ps1
```

**On Linux:**

```bash
docker run -d \
  --cap-add=CHECKPOINT_RESTORE \
  --cap-add=SYS_PTRACE \
  -p 8080:8080 \
  --name petclinic_container \
  -v "$(pwd)/crac-files:/opt/crac-files" \
  petclinic \
  java -XX:CRaCCheckpointTo=/opt/crac-files -jar /opt/app/petclinic.jar
```

**Wait**: Wait 5-10 seconds for the application to start.

#### Step 3: Create Checkpoint via CLI

Once the application is running, you can interact with it through the console:

```bash
# In the console, enter:
/create-snapshot  # Creates a checkpoint and automatically shuts down the application
/help            # Shows available commands
exit             # Exits the application (without creating checkpoint)
```

**What happens when you call `/create-snapshot`:**

- The application captures its current state (heap, threads, JVM state)
- A checkpoint is created and saved to disk (default: `./crac-files`)
- The application automatically shuts down after checkpoint creation
- The checkpoint files are persisted and ready for restoration

#### Step 3: Restore from Checkpoint

After the checkpoint has been created and the application has shut down, you can restore it using the restore script:

**On Windows (PowerShell):**

```powershell
.\restore.ps1
```

**On Linux:**

```bash
docker run -d \
  --cap-add=CHECKPOINT_RESTORE \
  --cap-add=SYS_PTRACE \
  -p 8081:8080 \
  --name petclinic_restored \
  -v "$(pwd)/crac-files:/opt/crac-files" \
  petclinic \
  java -XX:CRaCRestoreFrom=/opt/crac-files
```

**What happens during restore:**

- The JVM starts with the `-XX:CRaCRestoreFrom` flag
- Checkpoint files are loaded from disk
- Heap memory is restored
- Thread state is restored
- The application is immediately ready for requests (15-40x faster than cold start)

---

## 🔄 Part 1: CLI vs Scripts - Pros and Cons

This section compares the two approaches for creating and restoring checkpoints: **Command Line Interface (CLI)** and **Docker Scripts**.

### Command Line Interface (CLI) Approach

#### Advantages ✅

1. **On-demand checkpoint creation**: Create checkpoints when the application is in the desired state
3. **No Docker required**: Works without Docker, only requires Java and CRaC-compatible JVM
4. **Development friendly**: Easy to use during development and testing
5. **State inspection**: Can inspect application state before creating checkpoint

#### Disadvantages ❌

1. **Manual intervention**: Requires manual interaction through console
2. **Not suitable for automation**: Difficult to automate in CI/CD pipelines
3. **Single instance**: Works best for single instance scenarios
4. **Console dependency**: Requires console access to the running application
5. **Limited production use**: Not ideal for production environments with multiple instances
6. **No containerization**: Doesn't leverage Docker containerization benefits

### Docker Scripts Approach (checkpoint.ps1, restore.ps1)

#### Advantages ✅

1. **Automation ready**: Easy to integrate into CI/CD pipelines
2. **Containerization**: Leverages Docker containerization benefits
3. **Isolation**: Application runs in isolated container environment
4. **Consistent environment**: Same environment across different machines
5. **Production ready**: Suitable for production deployments
6. **Scalability**: Easy to scale across multiple instances
7. **Portability**: Works on any machine with Docker installed

#### Disadvantages ❌

1. **Docker requirement**: Requires Docker to be installed and running
2. **Limited state inspection**: Harder to inspect application state before checkpoint
3. **Docker capabilities**: Requires special Docker capabilities (CHECKPOINT_RESTORE, SYS_PTRACE)

### When to Use Each Approach

#### Use CLI Approach when:

- ✅ Developing or testing the application
- ✅ Working without Docker
- ✅ Single instance scenarios
- ✅ Need to inspect application state before checkpoint

#### Use Scripts Approach when:

- ✅ Production deployments
- ✅ CI/CD pipeline integration
- ✅ Multiple instance scenarios
- ✅ Need automation
- ✅ Containerized environments
- ✅ Standardized workflows
- ✅ Production environment

---

## 📊 Part 2: Cold Start vs Restore Startup Comparison

This section provides a detailed comparison between **Cold Start** (traditional startup) and **Restore Startup** (CRaC checkpoint restore) approaches.

### Cold Start (Traditional Startup)


#### Advantages ✅

1. **Deterministic**: Always the same process
2. **Predictable**: Easy to debug
3. **No dependencies**: Doesn't require checkpoint files
4. **Fresh state**: Always starts with fresh application state
5. **Universal**: Works with any JVM
6. **Simple**: Straightforward startup process

#### Disadvantages ❌

1. **Slow**: Long time to readiness
2. **Resource intensive**: High CPU usage during startup
3. **JIT warmup required**: Additional 30-60 seconds for optimal performance
4. **Database connections**: Slow database connection establishment
5. **Class loading**: Time-consuming class loading process
6. **Spring context**: Slow Spring Context initialization

### Restore Startup (CRaC Checkpoint Restore)

#### Advantages ✅

1. **Fast**: 15-40x faster than cold start
2. **Resource efficient**: Lower CPU usage during startup
3. **Instant readiness**: Application immediately ready for requests
4. **No JIT warmup**: JIT is already warmed up from checkpoint
5. **Pre-established connections**: Database connections already established
6. **Consistent state**: Application state is preserved
7. **Production ready**: Suitable for production environments

#### Disadvantages ❌

1. **Checkpoint creation time**: Requires 5-10 seconds to create checkpoint
2. **Memory overhead**: Checkpoint files require 50-200MB disk space
3. **Compatibility**: Requires CRaC-compatible JVM (Azul Zulu)
4. **Platform dependency**: Only works on Linux
5. **Checkpoint dependency**: Requires checkpoint files to exist
6. **State preservation**: Application state must be checkpoint-compatible
7. **Docker requirements**: Requires Docker with special capabilities

### Side-by-Side Comparison

| Aspect                    | Cold Start          | Restore Startup         | Winner                             |
| ------------------------- |---------------------|-------------------------| ---------------------------------- |
| **Startup Time**          | 6-10 seconds        | 200-800ms               | 🏆 Restore Startup (15-40x faster) |
| **JIT Warmup**            | 30-60 seconds       | 0 seconds               | 🏆 Restore Startup (instant)       |
| **CPU Usage**             | High during startup | Minimal                 | 🏆 Restore Startup                 |
| **Memory Usage**          | Gradual increase    | Immediate (restored)    | 🏆 Restore Startup                 |
| **Disk I/O**              | Many files          | One checkpoint file     | 🏆 Restore Startup                 |
| **Checkpoint Creation**   | N/A                 | 5-10 seconds (one-time) | 🏆 Cold Start                      |

### Performance Metrics

#### Startup Time Comparison

```
Cold Start:      ████████████████████ 5-8 seconds
Restore Startup: █ 200-400ms

Speedup: 15-40x faster with Restore Startup
```


#### JIT Warmup Comparison

```
Cold Start:      ████████████████████████████████████████ 30-60 seconds
Restore Startup: (not required - already warmed up)

Benefit: Instant optimal performance with Restore Startup
```

## 📄 License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

Spring PetClinic sample application is originally a project by the Spring team and is licensed under Apache License 2.0.

## 🙏 Acknowledgments

- [Spring PetClinic](https://github.com/spring-projects/spring-petclinic) - Original Spring PetClinic project

---
