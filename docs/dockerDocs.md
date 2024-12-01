## Docker Documentation

## **Introduction**

In the Whanos project, Docker images are essential for containerizing and running applications. Two types of images are defined:

1. **Base Image**: Provides a foundational environment for building and running applications. These are general-purpose and intended for further customization by applications that need specific configurations.
2. **Standalone Image**: A complete and self-sufficient image for running applications without further modification.

Both image types are designed to align with Whanos' infrastructure and support the specified languages (C, Java, JavaScript, Python, and Befunge).

---

### **Base Image**

A base image sets up the basic environment required to run applications. It includes:

- Essential runtime tools and dependencies.
- No application-specific files or build steps.
- Prepared for customization through `FROM`.

#### **Example**: Base Image for Python

```Dockerfile
FROM debian:stable-slim

RUN set -eux; \
    apt-get update && apt-get install -y --no-install-recommends \
        python-is-python3

RUN apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

LABEL org.opencontainers.image.source=https://github.com/Tom-Mendy/Whanos

CMD ["python3.12", "-m", "app"]
```

**Commands Explanation**:

- **`FROM debian:stable-slim`**: Sets the base operating system for the image (Debian in this case).
- **`RUN set -eux;`**: Enables safe scripting by:
  - Exiting on errors (`-e`).
  - Treating unset variables as errors (`-u`).
  - Printing commands and arguments during execution (`-x`).
- **`apt-get install`**: Installs required tools and dependencies (e.g., Python in this example).
- **`apt-get clean` & `rm -rf /var/lib/apt/lists/*`**: Cleans up package manager cache to minimize image size.
- **`WORKDIR /app`**: Defines the working directory for application files.
- **`LABEL org.opencontainers.image.source`**: Adds metadata for the image's source repository.
- **`CMD`**: Specifies the default command when the container starts. It can be overridden during runtime.

---

#### **Standalone Image**

A standalone image builds upon the base image by including application-specific files and performing any required build or setup steps. It’s ready to run the application without further modifications.

##### **Example**: Standalone Image for JavaScript

```Dockerfile
FROM whanos-javascript

COPY . /app

RUN set -eux; \
    npm install

CMD ["node", "."]
```

**Commands Explanation**:

- **`FROM whanos-javascript`**: Inherits from the JavaScript base image.
- **`COPY . /app`**: Copies application files into the `/app` directory.
- **`RUN npm install`**: Installs application dependencies using `npm`.
- **`CMD ["node", "."]`**: Executes the application using Node.js.

---

#### **Key Differences**

| Feature               | Base Image                       | Standalone Image                            |
| --------------------- | -------------------------------- | ------------------------------------------- |
| **Purpose**           | Foundation for customization.    | Fully configured for execution.             |
| **Application Files** | Not included.                    | Included and prepared to run.               |
| **Dependencies**      | General runtime dependencies.    | Includes application-specific dependencies. |
| **Example Use**       | As `FROM` in another Dockerfile. | Direct deployment.                          |

---

#### **Dockerfile Commands Overview**

- **`FROM`**: Specifies the base image for the build. All instructions after this will layer onto the specified image.
- **`RUN`**: Executes commands during image build, such as installing dependencies or cleaning up.
- **`WORKDIR`**: Sets the working directory for `RUN`, `CMD`, and other instructions.
- **`COPY`**: Copies files from the host to the image.
- **`LABEL`**: Adds metadata to the image, such as the repository source.
- **`CMD`**: Defines the default command executed when the container runs. It can be overridden with `docker run`.

---

#### **Best Practices**

1. **Minimize Layers**: Combine `RUN` commands to reduce image layers.
2. **Clean Up**: Remove unnecessary files and caches to keep the image size small.
3. **Metadata**: Use `LABEL` to provide useful information about the image.
4. **Use `set -eux`**: Ensures reliability and debugging information during builds.

By understanding the role and components of base and standalone images, you can build efficient and reusable Docker images tailored for the Whanos infrastructure.
