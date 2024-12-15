# Terraform and Linode Project Documentation

## What is Terraform?

Terraform is an open-source Infrastructure as Code (IaC) tool created by HashiCorp. It allows you to define and manage your infrastructure resources (such as virtual machines, storage, and networking) through declarative configuration files. By using Terraform, you can automate infrastructure provisioning, make your deployments consistent, and maintain reproducible environments.

### Key Features of Terraform

- **Declarative Configuration:** Define what your infrastructure should look like in simple configuration files.
- **Provider Ecosystem:** Interact with multiple platforms (e.g., AWS, Linode, GCP) through providers.
- **State Management:** Keep track of your infrastructure in a state file to ensure consistency.
- **Change Automation:** Plan and apply infrastructure changes safely.

## About This Project

This project leverages Terraform to provision virtual machines (Linode instances) in the Linode cloud platform. Specifically, it provisions 4 Ubuntu 22.04 virtual machines in the `eu-central` region.

### Directory Structure

```plaintext
project/
|-- main.tf
|-- output.tf
|-- ../linode_token.json
|-- ../id_rsa_single_line.pub
```

### Configuration Details

#### **main.tf**

This file defines the core Terraform configuration, including the required provider, authentication, and the resources to be provisioned.

- **Provider Configuration**:

  ```hcl
  terraform {
    required_providers {
      linode = {
        source  = "linode/linode"
        version = "~> 2.31.1"
      }
    }
  }

  provider "linode" {
    token = jsondecode(file("../linode_token.json")).linode_token
  }
  ```

  The `linode` provider is configured to use an API token stored in a JSON file (`linode_token.json`). This token must have appropriate permissions for instance management.

- **Resource Definition**:

  ```hcl
  resource "linode_instance" "ubuntu_instance" {
    count     = 4
    label     = "ubuntu-instance-${count.index + 1}"
    region    = "eu-central"
    type      = "g6-standard-2"
    image     = "linode/ubuntu22.04"
    root_pass = "your-secure-password"

    authorized_keys = [
      file("../id_rsa_single_line.pub")
    ]
  }
  ```

  This configuration creates 4 instances labeled `ubuntu-instance-1` through `ubuntu-instance-4`. Each instance is a `g6-standard-2` type in the `eu-central` region and runs Ubuntu 22.04. Public key authentication is enabled using the key stored in `id_rsa_single_line.pub`.

#### **output.tf**

This file defines the outputs for the Terraform run. It outputs the public IPs of the created instances.

```hcl
output "instance_ips" {
  description = "The public IPs of the provisioned instances."
  value       = [for instance in linode_instance.ubuntu_instance : instance.ipv4]
}
```

The result will be a list of public IPv4 addresses for the created Linode instances.

## Prerequisites

1. **Terraform Installed:** Ensure Terraform is installed on your system. You can download it from [terraform.io](https://www.terraform.io/downloads.html).
2. **Linode API Token:** Generate an API token with appropriate permissions for managing Linode instances. Save it in a file (`linode_token.json`) with the following structure:

   ```json
   {
     "linode_token": "YOUR_API_TOKEN"
   }
   ```

3. **SSH Key:** Ensure you have an SSH public key file (`id_rsa_single_line.pub`) to use for authentication on the Linode instances.

## Basic Usage

1. **Initialize Terraform:**

   ```bash
   terraform init
   ```

   This command initializes the Terraform project, downloads required providers, and prepares the working directory.

2. **Plan the Infrastructure:**

   ```bash
   terraform plan
   ```

   This command shows what Terraform will do when applying the configuration, without actually provisioning resources.

3. **Apply the Configuration:**

   ```bash
   terraform apply
   ```

   Confirm the prompt to provision the infrastructure. Terraform will create 4 Linode instances and display their public IPs as output.

4. **Access Instances:**
   Use the SSH public key (`id_rsa_single_line.pub`) to connect to the instances via their public IPs:

   ```bash
   ssh -i ../id_rsa your-user@INSTANCE_PUBLIC_IP
   ```

5. **Destroy the Infrastructure:**
   When done, destroy the resources to avoid incurring costs:

   ```bash
   terraform destroy
   ```

## Our Usage
In our case all the terrafrom process is automated in an ansible job. That permit us to have a more secure and fast process.

## Notes

- Replace `your-secure-password` with a strong password for the root user.
- Be mindful of your Linode account limits and API quotas when creating instances.
- Never hardcode sensitive values (e.g., API tokens or passwords) directly into the configuration files.

## Resources

- [Terraform Documentation](https://www.terraform.io/docs)
- [Linode Provider for Terraform](https://registry.terraform.io/providers/linode/linode/latest/docs)
