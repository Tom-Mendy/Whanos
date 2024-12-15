# Kubernetes Cluster Documentation

## Introduction to Kubernetes

Kubernetes, often abbreviated as K8s, is an open-source container orchestration platform designed to automate the deployment, scaling, and management of containerized applications. It allows you to manage clusters of containers, ensuring high availability, scalability, and efficient resource utilization.

### Key Features

- **Self-Healing:** Restarts failed containers and reschedules them on healthy nodes.
- **Scaling:** Automatically scales applications up or down based on demand.
- **Load Balancing:** Distributes traffic across multiple instances of an application.
- **Declarative Configuration:** Manage infrastructure through YAML/JSON manifests.

## About This Project

This project automates the generation of Kubernetes manifests for deploying a custom application (`whanos`) using a Python script, `generate_kubernetes_cluster.py`. The script reads configuration from a YAML file (`whanos.yml`) and generates a Kubernetes `Deployment` manifest.

### Project Files

#### **generate_kubernetes_cluster.py**

This Python script automates the generation of a Kubernetes deployment YAML manifest based on configuration parameters from a `whanos.yml` file.

- **Script Highlights:**
  - Parses and validates the `whanos.yml` configuration file.
  - Generates a Kubernetes deployment manifest dynamically based on provided configuration.
  - Outputs the manifest to `whanos-deployment.yaml`.

#### **whanos.yml**

This YAML file contains the configuration for the deployment, such as replica count, resource requirements, and ports.

### How It Works

1. **Configuration Parsing:**
   The script reads `whanos.yml` to extract deployment-specific parameters. For example:

   ```yaml
   deployment:
     replicas: 3
     ports:
       - 8080
       - 9090
     resources:
       limits:
         cpu: "200M"
         memory: "256Mi"
       requests:
         cpu: "100M"
         memory: "128Mi"
   ```

2. **Manifest Generation:**
   The script generates a `Deployment` manifest with the following structure:

   ```yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: whanos
   spec:
     replicas: 3
     selector:
       matchLabels:
         app: whanos
     template:
       metadata:
         labels:
           app: whanos
       spec:
         containers:
         - name: whanos-container
           image: <image_name>
           ports:
           - containerPort: 8080
           - containerPort: 9090
           resources:
             limits:
               cpu: "200M"
               memory: "256Mi"
             requests:
               cpu: "100M"
               memory: "128Mi"
         imagePullSecrets:
         - name: dockerconfigjson-github-com
   ```

3. **Deployment:**
   Apply the generated manifest to a Kubernetes cluster using:

   ```bash
   kubectl apply -f whanos-deployment.yaml
   ```

### Usage Instructions

1. **Prerequisites:**
   - Python 3 installed.
   - `PyYAML` library installed. Install it using:

     ```bash
     pip install pyyaml
     ```

   - A valid Kubernetes cluster and `kubectl` configured.

2. **Running the Script:**
   To generate the Kubernetes manifest, provide the Docker image name as an argument:

   ```bash
   ./generate_kubernetes_cluster.py <image_name>
   ```

   Replace `<image_name>` with the name of the container image (e.g., `myrepo/whanos:latest`).

3. **Generated Output:**
   The script produces a `whanos-deployment.yaml` file, which contains the deployment manifest for Kubernetes.

4. **Applying the Manifest:**
   Deploy the application to your cluster:

   ```bash
   kubectl apply -f whanos-deployment.yaml
   ```

### Error Handling

- **Missing Configuration File:**
  If `whanos.yml` is not found, the script exits with an error message:

  ```bash
  Error: File not found: whanos.yml
  ```

- **Invalid YAML Format:**
  If the `whanos.yml` file has syntax issues, the script raises an error:

  ```bash
  Error: Invalid YAML format
  ```

- **Missing Deployment Section:**
  If the `deployment` section is not present in `whanos.yml`, the script exits with:

  ```bash
  Error: Invalid whanos.yml: Missing deployment section
  ```

### Notes

- **Image Pull Secrets:** The script adds an `imagePullSecrets` section to the deployment. Ensure the secret `dockerconfigjson-github-com` is created in your Kubernetes cluster.
- **Scalability:** Adjust the `replicas` field in `whanos.yml` to scale the deployment.
- **Custom Ports and Resources:** Customize the ports and resource limits/requests in `whanos.yml` as needed.

### Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [PyYAML Documentation](https://pyyaml.org/wiki/PyYAMLDocumentation)
