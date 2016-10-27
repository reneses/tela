#!/bin/bash

# This prepared to run on an Amazon Linux distribution, which already include Docker in their repositories
# If this is not the case, please, add it first to yum

sudo yum update -y
sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user
docker run --restart=always -d \
    -p 2424:2424 \
    -p 2480:2480 \
    -e ORIENTDB_ROOT_PASSWORD=telatelatela \
    -v /opt/orientdb/databases:/orientdb/databases \
    -v /opt/orientdb/backup:/orientdb/backup \
    orientdb:latest