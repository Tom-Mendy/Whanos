variable "project_id" {
  description = "The Google Cloud project ID."
  type        = string
}

variable "region" {
  description = "The Google Cloud region to deploy resources."
  type        = string
  default     = "us-central1"
}

variable "zone" {
  description = "The zone within the region."
  type        = string
  default     = "us-central1-a"
}

variable "ssh_user" {
  description = "The SSH username for accessing instances."
  type        = string
}

variable "ssh_public_key_path" {
  description = "The path to the SSH public key file."
  type        = string
}

variable "credentials_file" {
  description = "The path to the Google Cloud credentials file."
  type        = string
}