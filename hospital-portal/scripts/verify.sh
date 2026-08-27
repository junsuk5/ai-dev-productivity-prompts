#!/usr/bin/env sh
set -eu

project_dir=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
output_dir="$project_dir/out/verify"
main_output="$output_dir/main"
test_output="$output_dir/test"
main_sources="$output_dir/main-sources.txt"
test_sources="$output_dir/test-sources.txt"
junit_jar="$project_dir/lib/junit-4.13.2.jar"
hamcrest_jar="$project_dir/lib/hamcrest-core-1.3.jar"

if [ ! -f "$junit_jar" ] || [ ! -f "$hamcrest_jar" ]; then
    echo "JUnit 4 실행 라이브러리를 찾을 수 없습니다: $project_dir/lib" >&2
    exit 1
fi

if [ -d "$output_dir" ]; then
    find "$output_dir" -depth -mindepth 1 -delete
fi
mkdir -p "$main_output" "$test_output"

find "$project_dir/src/main/java" -name '*.java' -type f | sort > "$main_sources"
find "$project_dir/src/test/java" -name '*Test.java' -type f | sort > "$test_sources"

javac --release 17 -encoding UTF-8 -Xlint:all -Werror \
    -d "$main_output" \
    @"$main_sources"

if [ -d "$project_dir/src/main/resources" ]; then
    cp -R "$project_dir/src/main/resources/." "$main_output/"
fi

test_classpath="$main_output:$junit_jar:$hamcrest_jar"
javac --release 17 -encoding UTF-8 -Xlint:all -Werror \
    -cp "$test_classpath" \
    -d "$test_output" \
    @"$test_sources"

if [ -d "$project_dir/src/test/resources" ]; then
    cp -R "$project_dir/src/test/resources/." "$test_output/"
fi

test_classes=""
while IFS= read -r source_file; do
    relative_path=${source_file#"$project_dir/src/test/java/"}
    class_name=${relative_path%.java}
    class_name=$(printf '%s' "$class_name" | tr '/' '.')
    test_classes="$test_classes $class_name"
done < "$test_sources"

if [ -z "$test_classes" ]; then
    echo "실행할 JUnit 4 테스트를 찾을 수 없습니다." >&2
    exit 1
fi

runtime_classpath="$main_output:$test_output:$junit_jar:$hamcrest_jar"
# test_classes는 Java 클래스 이름으로만 구성되므로 의도적으로 단어 분리한다.
# shellcheck disable=SC2086
java -cp "$runtime_classpath" org.junit.runner.JUnitCore $test_classes
