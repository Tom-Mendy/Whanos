#!/usr/bin/env python3

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


def generate_kubernetes_manifest(config):
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
                            "image": "whanos/whanos:latest",
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
                    ]
                },
            },
        },
    }
    return deployment_yaml


try:
    whanos_config = parse_whanos_yaml('whanos.yml')
except ValueError as e:
    print(f"Error: {e}")
    exit(1)

whanos_manifest = generate_kubernetes_manifest(whanos_config)

output_file = 'whanos-deployment.yaml'
with open(output_file, 'w') as yaml_file:
    yaml.dump(whanos_manifest, yaml_file, default_flow_style=False)
