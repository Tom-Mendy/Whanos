# Ansible Playbook Documentation

## Introduction to Ansible

Ansible is an open-source automation tool that simplifies IT tasks such as configuration management, application deployment, and orchestration. By defining infrastructure as code, Ansible allows for repeatable, consistent, and scalable automation across your systems.

### Key Features

- **Agentless:** Operates over SSH and does not require additional software on managed nodes.
- **Declarative Configuration:** Uses YAML playbooks to define desired system states.
- **Extensibility:** Allows the use of roles and modules for modular and reusable code.

## About This Project

This project uses an Ansible playbook (`playbook.yml`) to automate the provisioning of infrastructure, Kubernetes cluster setup, and the installation of Jenkins. The playbook orchestrates tasks across different host groups and uses reusable roles to manage configurations effectively.

### Project Files

#### **playbook.yml**

This playbook defines the steps and host groups involved in the automation process. It:

1. Configures Terraform on the localhost.
2. Sets up base configurations for Kubernetes control planes and nodes.
3. Installs Kubernetes using the Kubespray playbook.
4. Installs and configures Jenkins on a dedicated host.

### How It Works

#### **Host Groups**

The playbook operates on the following host groups:

- `localhost`: The local machine used to run Terraform configurations.
- `kube_control_plane`: Hosts designated as Kubernetes control plane nodes.
- `kube_node`: Worker nodes for the Kubernetes cluster.
- `jenkins`: The host for the Jenkins server.

#### **Roles Used**

- **terraform**: Handles Terraform installation and execution.
- **base**: Configures essential packages, settings, and dependencies for target nodes.
- **kubernetes**: Installs and configures Kubernetes components.
- **docker**: Sets up Docker on the Jenkins host.
- **jenkins**: Installs and configures Jenkins.

#### **Playbook Workflow**

1. **Terraform Setup**

   ```yaml
   - hosts: localhost
     become: false
     roles:
       - terraform
   ```

   Runs Terraform configurations on the local machine.

2. **Base Configuration for Kubernetes Nodes**

   ```yaml
   - hosts: kube_control_plane
     roles:
       - base

   - hosts: kube_node
     roles:
       - base
   ```

   Prepares Kubernetes control plane and worker nodes by applying base configurations.

3. **Kubernetes Cluster Installation**

   ```yaml
   - name: Install Kubernetes
     ansible.builtin.import_playbook: kubernetes_sigs.kubespray.cluster
   ```

   Deploys the Kubernetes cluster using the Kubespray playbook.

4. **Kubernetes Control Plane Configuration**

   ```yaml
   - hosts: kube_control_plane
     roles:
       - kubernetes
   ```

   Configures Kubernetes-specific components on control plane nodes.

5. **Jenkins Setup**

   ```yaml
   - hosts: jenkins
     become: true
     roles:
       - base
       - docker
       - jenkins
   ```

   Sets up Docker and Jenkins on the designated Jenkins host.

### Prerequisites

1. **Ansible Installation:**
   Ensure Ansible is installed on the control machine. Install it using:

   ```bash
   sudo apt update && sudo apt install ansible
   ```

2. **Kubespray Installation:**
   Kubespray is required for Kubernetes cluster setup. Clone the Kubespray repository:

   ```bash
   git clone https://github.com/kubernetes-sigs/kubespray.git
   ```

   Install dependencies:

   ```bash
   pip install -r kubespray/requirements.txt
   ```

3. **Inventory Configuration:**
   Define your hosts in the Ansible inventory file.

### Running the Playbook

1. **Prepare Your Inventory:**
   Edit the Ansible inventory file to include the required host groups:

   ```ini
   [localhost]
   127.0.0.1

   [kube_control_plane]
   control-plane-1 ansible_host=<IP>

   [kube_node]
   node-1 ansible_host=<IP>

   [jenkins]
   jenkins ansible_host=<IP>
   ```

2. **Execute the Playbook:**
   Run the playbook using the following command:

   ```bash
   ansible-playbook -i inventory playbook.yml
   ```

3. **Verify Deployment:**
   - For Kubernetes, check the cluster status using:

     ```bash
     kubectl get nodes
     ```

   - For Jenkins, access the Jenkins web interface at `http://<jenkins-host>:8080`.

### Notes

- Ensure SSH keys are configured for passwordless access to remote nodes.
- Use Ansible Vault to store sensitive information such as credentials.

### Resources

- [Ansible Documentation](https://docs.ansible.com/)
- [Kubespray Documentation](https://kubespray.io/)
