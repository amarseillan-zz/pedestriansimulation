import csv
import sys

files = sys.argv[1:]
print files
list = []
for f in files:
	with open(f, 'rb') as csvfile:
		spamreader = csv.reader(csvfile, delimiter=',')
		for row in spamreader:
			list.append((int(row[0]), float(row[1])))

averages = {}

for i in list:
	if i[0] not in averages:
		averages[i[0]] = 1, i[1]
	else:
		averages[i[0]] = averages[i[0]][0]+1, averages[i[0]][1]+i[1]

aggregated = []

for i in averages:
	if averages[i][0] != 0:
		aggregated.append((i, averages[i][1]/averages[i][0]))
	else:
		aggregated.append((i, 0))

with open('octave.csv', 'wb') as csvfile:
    spamwriter = csv.writer(csvfile, delimiter=',')
    for i in aggregated:
    	spamwriter.writerow([str(i[0]),str(i[1])])