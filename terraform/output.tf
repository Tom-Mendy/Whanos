output "instance_ips" {
  description = "The public IPs of the provisioned instances."
  value       = google_compute_instance.ubuntu_instance[*].network_interface[0].access_config[0].nat_ip
}

output "instance_private_ips" {
  description = "The private IPs of the provisioned instances."
  value       = google_compute_instance.ubuntu_instance[*].network_interface[0].network_ip
}

output "instance_names" {
  description = "The names of the provisioned instances."
  value       = google_compute_instance.ubuntu_instance[*].name
}
