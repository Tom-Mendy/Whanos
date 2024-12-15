#!/usr/bin/env bash

cd ansible || exit 1

git clone git@github.com:kubernetes-sigs/kubespray.git

VENVDIR=kubespray-venv
KUBESPRAYDIR=kubespray
python3 -m venv $VENVDIR
source $VENVDIR/bin/activate
cd $KUBESPRAYDIR || exit 1
pip install -U -r requirements.txt
cd .. || exit 1

pip install distlib

ansible-galaxy install -r requirements.yml

ansible-playbook playbook.yml