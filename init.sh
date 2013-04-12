#!/bin/bash

. CONFIG

# Download hbase and titan graph database -- use only once and then comment as follows

#curl -O http://download.nextag.com/apache/hbase/stable/"$hbase_version".tar.gz
#curl -O http://s3.thinkaurelius.com/downloads/titan/"$titan_version".zip

# Unpack hbase and titan

tar -zxvf "$hbase_version".tar.gz
unzip "$titan_version".zip

# Start hbase and gremlin shell

"$PWD"/"$hbase_version"/bin/start-hbase.sh
"$PWD"/"$titan_version"/bin/gremlin.sh -e "$PWD"/DataLoader.groovy
