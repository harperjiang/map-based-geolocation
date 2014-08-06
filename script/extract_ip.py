import re
import subprocess


reg_pattern = 'has address ([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3})'

file = open('../data/university.tsv')
output = open('../data/university_ip.tsv','w')

def parse(inputs):
    for line in inputs:
        search_res = re.search(reg_pattern,line)
        if search_res != None:
            # yield search_res.group(1)
            return search_res.group(1)
    return None

header = 0

for line in file:
    line = line.strip()
    if header == 0:
        header=1
        output.write(line+'\n')
        continue
    cols = re.split('\t*',line)
    if len(cols) == 2: # With website info
        host_lines = subprocess.check_output(['host',cols[1]]).decode("utf8").split('\n')
        ip_addresses = parse(host_lines)
        if ip_addresses != None :
            output.write('\t'.join([cols[0],cols[1],ip_addresses])+'\n')
    
file.close()
output.close()