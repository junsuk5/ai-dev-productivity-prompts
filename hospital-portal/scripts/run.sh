#!/usr/bin/env sh
set -eu

project_dir=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
output_dir="$project_dir/out/run"
sources="$output_dir/sources.txt"

if [ -d "$output_dir" ]; then
    find "$output_dir" -depth -mindepth 1 -delete
fi
mkdir -p "$output_dir"

find "$project_dir/src/main/java" -name '*.java' -type f | sort > "$sources"

javac --release 17 -encoding UTF-8 -Xlint:all -Werror \
    -d "$output_dir" \
    @"$sources"

if [ -d "$project_dir/src/main/resources" ]; then
    cp -R "$project_dir/src/main/resources/." "$output_dir/"
fi

java -cp "$output_dir" kr.or.publicdata.portal.HospitalPortalApplication
