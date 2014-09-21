#!/bin/sh

#  stop-device-server.sh
#  
#
#  Created by Jeremy Reeve on 21/09/2014.
#

lockdir=/var/tmp/device-service.lock
pidfile=$lockdir/pid

if [ -f $pidfile ]; then
echo Killing Device Service...

PID=$(cat $pidfile)
kill $PID
else
echo Failed to find PID.  Is the Device Service running?
exit 0
fi
