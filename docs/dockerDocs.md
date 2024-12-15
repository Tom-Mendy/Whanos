# Docker Images Documentation: Standalone and Base using C and C++ for examples

## Overview

This documentation describes the structure and usage of two types of Docker images for C/C++ applications: **Standalone** and **Base** images. These images are used for building and deploying applications, leveraging multi-stage builds and the `ONBUILD` instruction for automation.

---

## **Standalone Image**

A **Standalone image** is a fully configured image that includes everything needed to build and run an application. It copies source code, builds the application, and provides a minimal runtime environment with only the necessary files.

### **Example: Standalone Dockerfile for C++**

```dockerfile
FROM gcc:13.2 AS build

SHELL ["/bin/bash", "-c"]

WORKDIR /app

COPY . /app

RUN make

FROM ubuntu:24.04 AS standalone

SHELL ["/bin/bash", "-c"]

WORKDIR /app

COPY --from=build /app/compiled-app /app/compiled-app

CMD ["/app/compiled-app"]
```

### Explanation Standalone

- **Multi-Stage Build**: The build stage compiles the application, and the standalone stage creates a lightweight image by copying only the necessary output (the compiled binary).
- **Minimal Runtime Image**: The runtime image uses `ubuntu:24.04` to keep the image as small as possible.
- **CMD**: The default command is set to run the compiled binary `compiled-app`.

---

## **Base Image**

A **Base image** is designed to provide a foundational environment for building and running an application. It can be extended by other Dockerfiles, which can customize the behavior (e.g., copying application code or adding build steps). The `ONBUILD` instruction is used to automate tasks in the derived images.

### **Example: Base Dockerfile for C**

```dockerfile
FROM gcc:13.2

SHELL ["/bin/bash", "-c"]

WORKDIR /app

LABEL org.opencontainers.image.title="Whanos C Base Image" \
      org.opencontainers.image.description="A base image for building and deploying C applications using Makefile." \
      org.opencontainers.image.version="1.0"

ONBUILD COPY . /app

ONBUILD RUN set -eux; \
            make

ONBUILD RUN set -eux; \
            rm -rf *.c *.h Makefile

ONBUILD CMD ["./compiled-app"]
```

### Explanation Base

- **set -eux;**: Configures the shell to:
  - `-e`: Exit immediately if a command exits with a non-zero status.
  - `-u`: Treat unset variables as an error and exit immediately.
  - `-x`: Print each command to the terminal before executing it.
- **ONBUILD**: Specifies tasks (like copying files, building the app, and cleaning up unnecessary files) that execute when this base image is used in a derived Dockerfile.
- **Metadata**: Labels are included for traceability and documentation.
- **Build and Clean**: The derived image automatically copies files, builds the application using `make`, and cleans up unnecessary files (source code and Makefile).

---

## **How to Use These Images**

### 1. **Standalone Image Usage Example**

You can create used this image with a specifiq project archi:

```bash
tree c-hello-world 
 c-hello-world
├──  app
│  └──  hello.c
├──  Dockerfile <== docker standalone c
└──  Makefile
```

#### Build the Image

```bash
docker build -t my-cpp-app .
```

#### Run the Application

```bash
docker run --rm my-cpp-app
```

---

### 2. **Base Image Usage Example**

To use the Base image, you would typically have a Dockerfile like the following:

```dockerfile
FROM whanos-cpp-base

# Optionally override the CMD or add more configurations
CMD ["/app/custom-compiled-app"]
```

#### Build the Base Image

```bash
docker build -t whanos-cpp-base -f Dockerfile.base .
```

#### Build the Derived Image

```bash
docker build -t my-cpp-app .
```

#### Run the Application

```bash
docker run --rm my-cpp-app
```

---

## **Comparison: Standalone vs Base Image**

| **Feature**           | **Standalone Image**                                   | **Base Image**                                             |
|------------------------|-------------------------------------------------------|-----------------------------------------------------------|
| **Purpose**            | A complete image ready for running the app.           | A base image for building the app in derived images.       |
| **Application Files**  | Includes compiled application and runtime tools.      | Does not include application files; extends with `ONBUILD`.|
| **Dependencies**       | Fully configured with dependencies.                   | Needs to be extended by another Dockerfile to configure dependencies. |
| **Customization**      | Ready to use but less flexible.                       | Allows full customization by the derived image.           |
| **Size**               | Larger, includes everything to run the app.           | Smaller, only contains necessary tools for building.       |

---

## **Conclusion**

Both Standalone and Base Docker images have their specific use cases:

- **Standalone images** are best for fully packaged applications, ready to run with minimal configuration.
- **Base images** are ideal for creating reusable and flexible development environments, where the build process and additional configurations can be specified in derived Dockerfiles.

This approach provides flexibility, efficiency, and scalability for managing Docker images in C/C++ projects.
