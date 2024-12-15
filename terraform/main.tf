terraform {
  required_providers {
    linode = {
      source  = "linode/linode"
      version = "~> 2.31.1"
    }
  }
}


provider "linode" {
  token = jsondecode(file("./linode_token.json")).linode_token
}

resource "linode_instance" "ubuntu_instance" {
  count     = 4
  label     = "ubuntu-instance-${count.index + 1}"
  region    = "eu-central"
  type      = "g6-standard-2"
  image     = "linode/ubuntu22.04"
  root_pass = "your-secure-password"

  authorized_keys = [
    file("~/id_rsa_single_line.pub")
  ]
}
