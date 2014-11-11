import csv
import sys

fps = 16
fd = 1/float(fps) #frame duration
_file = sys.argv[1]
print _file
lines_by_instant = {}
static = {}


with open(_file, 'rb') as csvfile:
	spamreader = csv.reader(csvfile, delimiter=' ')
	for row in spamreader:
		_id = row[0]
		frame = int(row[1])
		x = float(row[2])/100
		y = float(row[3])/100
		instant = frame*fd

		if instant not in lines_by_instant:
			lines_by_instant[instant] = []
		if _id not in static:
			static[_id] = _id + "/1/60/0.25/1000/0.8"
		lines_by_instant[instant].append(_id+"/"+str(y)+" "+str(x)+"/0 0/"+str(y)+" "+str(x))

import collections

od = collections.OrderedDict(sorted(lines_by_instant.items()))


dynamic_f = open('dynamic.txt', 'w')
static_f = open('static.txt', 'w')
for k, v in od.iteritems():
	dynamic_f.write(str(k) + '\n')
	for row in v:
		dynamic_f.write(row + '\n')


for k in static:
	static_f.write(static[k] + '\n')
