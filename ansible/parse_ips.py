#!/usr/bin/env python

import json
import sys

change_env = False

try:
    arg = sys.argv[1]
except IndexError:
    arg = ""

if arg == "":
    change_env = True


def change_ini_file(arg):
    decoded_str = json.loads(arg)

    parsed_json = json.loads(decoded_str)

    final_value_jenkins = parsed_json["value"][0][0]
    final_value_control_plane = parsed_json["value"][1][0]
    final_value_node = parsed_json["value"][2][0]
    final_value_node_sub = parsed_json["value"][3][0]

    print(f'[jenkins]\njenkins_service ansible_host={final_value_jenkins}\n')
    print(f'[kube_control_plane]\nnode1 ansible_host={final_value_control_plane}\n')
    print(f'[kube_node]\nnode2 ansible_host={final_value_node}\nnode3 ansible_host={final_value_node_sub}\n')


def change_env_file():
    value = ""
    with open('inventory.ini', 'r') as file:
        lines = file.readlines()
        for line in lines:
            if 'node1 ansible_host=' in line:
                node1 = line.split('=')[1].strip()
                node1 = node1.split(' ')[0]
                value = node1
                print(f'KUBE_SERVER=\"{value}:6443\"\n')


if change_env:
    change_env_file()
else:
    change_ini_file(arg)
