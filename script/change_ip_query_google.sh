#!/bin/bash
# This script change local ip and query google for the website's url as free queries are almost forbidden by google.
# Disable network manager and switch to ipconfig before using this script.
for i in {5..120}
do
	ip="128.153.23.$i"
	echo "Trying ip $ip"
	ifconfig eth0 $ip netmask 255.255.248.0 broadcast 128.153.23.255 up
	# ifconfig will remove the default route from routing table
	route add default gw 128.153.16.1
	cd ..
	. script/java_query_google_univ.sh
	cd script
done

ifdown eth0
ifup eth0
