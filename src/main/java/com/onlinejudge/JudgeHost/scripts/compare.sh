#!/bin/sh

# 将用户的输出和正确输出进行比较
# example: ./compare <用户输出> <期望输出>

SUBMISSTION_OUTPUT_PATH="$1"
RESOLUTION_PATH="$2"


# 判断文件是否存在
if [ ! -f $SUBMISSTION_OUTPUT_PATH ]; then
  echo 2
  exit 0
elif [ ! -f $RESOLUTION_PATH ]; then
  echo 2
  exit 0
fi

# 文件对比
diff -q -b "$SUBMISSTION_OUTPUT_PATH" "$RESOLUTION_PATH"
EXITCODE=$?
echo $EXITCODE