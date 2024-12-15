#!/usr/bin/env bash

git clone git@github.com:kubernetes-sigs/kubespray.git

VENVDIR=kubespray-venv
KUBESPRAYDIR=kubespray
python3 -m venv $VENVDIR
source $VENVDIR/bin/activate
cd $KUBESPRAYDIR
pip install -U -r requirements.txt

cd ..

pip install distlib

ansible-galaxy install -r requirements.yml