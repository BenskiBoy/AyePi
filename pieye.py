import os
import time
import sys, getopt
import subprocess
import commands
import csv

import picamera
from PIL import Image
import math,operator
camera = picamera.PiCamera()

import MySQLdb
import datetime

import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(17,GPIO.IN)
input = GPIO.input(17)

##########################################################
#Connect to DB
try:
	db = MySQLdb.connect("localhost", "root", "password", "pieye")
	curs=db.cursor()
	curs.execute("SELECT VERSION()")
	results = curs.fetchone()
except:
	print '\033[91m' + "Error: database error" + '\033[0m'
	sys.exit(0)
	db.rollback()

##########################################################
#Delete all files in temporary folder (contents stay in RAM)
FNULL = open(os.devnull, 'w')

os.system("rm -rf var/tmp/")	

#used to check to see if airodump is already running (useful after a reboot)
if commands.getstatusoutput('ifplugstatus mon0')[1] != 'mon0: link beat detected':
	print '\033[92m' + "[STARTING mon0]" + '\033[0m'
	os.system("airmon-ng check kill")
	os.system("airmon-ng start wlan1")
	
print '\033[92m' + "[ STARTING airodump-ng ]" + '\033[0m'
airodump = subprocess.Popen(['airodump-ng', 'mon0', '-w', '/var/tmp/dump'],stdout=FNULL,stderr=FNULL)
	
##########################################################
time.sleep(5)

#To store previous lock value
prevLockStatus = False
firstCycle = True

while 1:	

	#get all the MAC addresses
	list = []
	
	reader = open('/var/tmp/dump-01.csv', 'r')

	for line in reader:
		list.append(line.strip().split(','))
		
	for x in range(2, len(list)):
		if list[x][0].find(':')!=-1: #Filters out if there is blank line or a title so only get mac addresses
			print list[x][0] + " " + list[x][2]
			curs.execute("SELECT userID FROM `user` WHERE _mac=%s",(list[x][0]))
			result_set = curs.fetchone()

			if not result_set is None:	
				try:
					curs.execute("INSERT INTO user_history (userID, timeDetected) VALUES (%s, %s)", (result_set[0], list[x][2]))
				except:
					print result_set
					print "fail"

##########################################################
	
	camera.capture('/var/tmp/image1.png')
	print "image1 taken"
	time.sleep(3)
	camera.capture('/var/tmp/image2.png')
	print "image 2 taken"

	h1 = Image.open('/var/tmp/image1.png').histogram()
	h2 = Image.open('/var/tmp/image2.png').histogram()

	rms = math.sqrt(reduce(operator.add,map(lambda a,b: (a-b)**2, h1, h2))/len(h1))

	print "rms: ", rms
	
	# lager the value, greater the change
	if rms > 5000:
		curs.execute("INSERT INTO camera (motion, timestamp, rms) VALUES (1, NOW(), %s)", (rms))
		db.commit()
	
##########################################################
	
	lockValue = GPIO.input(17)
	if (firstCycle == True):
		if (lockValue):
			curs.execute("INSERT INTO door (isLocked, timestamp) VALUES (1, NOW())")
		else:
			curs.execute("INSERT INTO door (isLocked, timestamp) VALUES (0, NOW())")
		firstCycle = False
	else:
		if (lockValue != prevLockStatus):
			curs.execute("INSERT INTO door (isLocked, timestamp) VALUES (%s, NOW())", (lockValue))

	print "Door = " + str(lockValue)
	print ""
		
	db.commit()
#########################################################
