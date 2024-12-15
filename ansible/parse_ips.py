#!/usr/bin/env python

import json
import sys


yu = sys.argv[1]

decoded_str = json.loads(yu)

parsed_json = json.loads(decoded_str)

final_value = parsed_json["value"][0][0]

print(f'[jenkins]\njenkins_service ansible_host={final_value}\n')
