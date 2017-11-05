#!/bin/bash
echo "Starting"
(
count=0;
# Wait 10 seconds. Why? Because if we don't then for some reason
# RabbitMQ crashes :(. There's bound to be a better way to do this...
sleep 10;
# Wait until we get a successful user list
until timeout 5 rabbitmqctl list_users >/dev/null 2>/dev/null || (( count++ >= 60)); do sleep 1; done;
echo "RabbitMQ appears to be up, performing hooks";
source /hooks/on-startup.sh
) &

# Call original entrypoint
exec docker-entrypoint.sh rabbitmq-server $@
