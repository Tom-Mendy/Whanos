#!/usr/bin/env python3

import sys
import yaml


def parse_whanos_yaml(file_path):
    try:
        with open(file_path, 'r') as file:
            try:
                data = yaml.safe_load(file)
                deployment = data.get('deployment', {})
                if not deployment:
                    raise ValueError(
                        "Invalid whanos.yml: Missing deployment section")
                return deployment
            except yaml.YAMLError as e:
                raise ValueError(f"Invalid YAML format: {e}")
    except FileNotFoundError:
        print(f"Error: File not found: {file_path}")
        exit(2)


def generate_kubernetes_manifest(config, image_name):
    deployment_yaml = {
        "apiVersion": "apps/v1",
        "kind": "Deployment",
        "metadata": {"name": "whanos"},
        "spec": {
            "replicas": config.get("replicas", 1),
            "selector": {"matchLabels": {"app": "whanos"}},
            "template": {
                "metadata": {"labels": {"app": "whanos"}},
                "spec": {
                    "containers": [
                        {
                            "name": "whanos-container",
                            "image": image_name,
                            "ports": [
                                {"containerPort": port} for port in config.get(
                                    "ports", [8080])
                            ],
                            "resources": config.get(
                                "resources",
                                {
                                    "limits": {
                                        "cpu": "100M",
                                        "memory": "128Mi"
                                    },
                                    "requests": {
                                        "cpu": "100M",
                                        "memory": "128Mi"
                                    }
                                },
                            ),
                        }
                    ],
                    "imagePullSecrets": [
                        {
                            "name": "dockerconfigjson-github-com"
                        }
                    ]
                },
            },
        },
    }
    return deployment_yaml


def generate_service_manifest(config):
    service_yaml = {
        "apiVersion": "v1",
        "kind": "Service",
        "metadata": {"name": "whanos-service", "namespace": "default"},
        "spec": {
            "selector": {"app": "whanos"},
            "ports": [
                {"protocol": "TCP", "port": port, "targetPort": port, "nodePort": 30100}
                for port in config.get("ports", [8080])
            ],
            "type": "NodePort",
        },
    }
    return service_yaml


if (len(sys.argv) < 2):
    print("Usage: generate_kubernetes_cluster.py <image_name>")
    exit(1)

try:
    whanos_config = parse_whanos_yaml('whanos.yml')
except ValueError as e:
    print(f"Error: {e}")
    exit(1)

whanos_manifest = generate_kubernetes_manifest(whanos_config, sys.argv[1])

output_file = 'whanos-deployment.yaml'
with open(output_file, 'w') as yaml_file:
    yaml.dump(whanos_manifest, yaml_file, default_flow_style=False)

whanos_service = generate_service_manifest(whanos_config)

output_file = 'whanos-service.yaml'
with open(output_file, 'w') as yaml_file:
    yaml.dump(whanos_service, yaml_file, default_flow_style=False)
