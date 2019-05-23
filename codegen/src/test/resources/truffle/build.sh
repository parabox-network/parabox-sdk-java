#!/usr/bin/env bash

targets="
MetaCoin/MetaCoin
"

for target in ${targets}; do
    dirName=$(dirname $target)
    fileName=$(basename $target)

    cd $dirName
    echo "Generating citaj bindings"
    citaj truffle generate \
        build/contracts/${fileName}.json \
        -p com.cryptape.cita.generated \
        -o ../../../../../../integration-tests/src/test/java/ > /dev/null
    echo "Complete"

    cd -
done
