# Whanos main documentation

## How to launch the project

First, you need to run the `run.sh` script.

```bash
./run.sh
```

## Needed Files

To launch this project you need to have the following files at the root of the repository:

- `linode_token.json`: This file should contain your access token to Linode API. It should be a JSON file with the following structure:

```json
{
  "linode_token": "YOUR_TOKEN"
}
```

To get this token you need to create a Linode account and generate a token [here](https://cloud.linode.com/profile/tokens), by clicking on the **"Create A Personal Access Token"** button.

- `private_key`: This file should contain your private key to access the Linode instances. [Here](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) is a guide on how to generate a private key. After, generating the key, take your public one, and add it [here](https://cloud.linode.com/profile/keys), and click on the **"Add An SSH Key"**. Do not forget to add a blank line at the end of the file that contains the private key.

- `id_rsa_single_line.pub`: This file should contain your public key to access the Linode instances. It should be a single line file.

- `.env`: This file should contain the following variables:
  - `GITHUB_DOCKER_REGISTRY`: The URL of the Docker registry.
