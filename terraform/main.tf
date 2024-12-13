terraform {
  required_providers {
    linode = {
      source  = "linode/linode"
      version = "~> 2.31.1"
    }
  }
}


provider "linode" {
  token = var.linode_api_token
}

resource "linode_instance" "ubuntu_instance" {
  count     = 1
  label     = "ubuntu-instance-${count.index + 1}"
  region    = "eu-west"
  type      = "g6-nanode-1"
  image     = "linode/ubuntu22.04"
  root_pass = "your-secure-password"

  authorized_keys = [
    file("~/id_rsa_single_line.pub")
  ]
}
