#!/usr/bin/env python

import json
import sys


yu = sys.argv[1]


decoded_str = json.loads(yu)

parsed_json = json.loads(decoded_str)

final_value_jenkins = parsed_json["value"][0][0]
final_value_control_plane = parsed_json["value"][1][0]
final_value_node = parsed_json["value"][2][0]
final_value_node_sub = parsed_json["value"][3][0]

print(f'[jenkins]\njenkins_service ansible_host={final_value_jenkins}\n')
print(f'[kube_control_plane]\nnode1 ansible_host={final_value_control_plane}\n')
print(f'[kube_node]\nnode2 ansible_host={final_value_node}\nnode3 ansible_host={final_value_node_sub}\n')
