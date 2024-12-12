project_id          = jsondecode(file("whanos-app.json")).project_id
region              = "us-central1"
zone                = "us-central1-a"
ssh_user            = "ubuntu"
ssh_public_key_path = "~/.ssh/id_rsa.pub"
credentials_file = "./whanos-app.json"