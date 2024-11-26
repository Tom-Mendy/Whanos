# Ansible

## SSh Private key

have the private key in `private_key`

with the permission `.r-------- private_key`

to do so

```bash
chmod 400 private_key
```

## Server IP

setup your server ip address in `inventory`

## Collection

[Ansible Docker Collection](https://docs.ansible.com/ansible/latest/collections/community/docker/index.html)

## SSH Connection

```bash
ssh -i private_key whanos@[IP]
```
