output "instance_ips" {
  description = "The public IPs of the provisioned instances."
  value       = [for instance in linode_instance.ubuntu_instance : instance.ipv4]
}
