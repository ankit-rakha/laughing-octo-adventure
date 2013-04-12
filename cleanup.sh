#!/bin/bash

. CONFIG

"$PWD"/"$hbase_version"/bin/stop-hbase.sh

rm -rf titan work logs data "$hbase_version" "$titan_version"

"$PWD"/init.sh
