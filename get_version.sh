#!/bin/bash -e

TMP=${TMP:-"."}

if [ "$OVERRIDE_BUILD_REPOS" == "true " ]
then
	SBT_ARGS="-Dsbt.override.build.repos=true"
fi

sbt $SBT_ARGS -no-colors version | tee /dev/stderr | tail -n 1 | sed -e 's/^\[info\][[:space:]]*//' > "$TMP/RELEASE_VERSION"

NEW_VERSION=$(head -n 1 "$TMP/RELEASE_VERSION")
if [[ $NEW_VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+(\-play\-[0-9]{2}){0,1}$ ]] ; then

# validate if tag already exists
    TAG=$(echo "v$NEW_VERSION" | cut -d'-' -f 1) # remove -play-25 suffix if exists
    echo "Testing if tag '$TAG' already exists"

    if [ $(git tag -l $TAG) ]; then
    echo "------------------------------------------------------------------------------"
    echo "ERROR!!!"
    echo "Attempted to create a new version '$NEW_VERSION' however tag '$TAG' already exists."
    echo "Git repo is in unexpected state and the build will fail now to prevent further problems."
    echo "Example reasons for this situation:"
    echo " - force pushing to master"
    echo " - amending commits on a hotfix branch"
    echo " - merging hotfix branch back to master"
    echo " - other kinds of history rewriting"
    echo "Please analyze if you've done any of the above, if you can't find anything please feel free to ask in #team-platops for help"
    echo "git describe = $(git describe)"
    echo "------------------------------------------------------------------------------"
    echo "TAG_TO_BE_IGNORED_BY_NEXT_STEPS" > "$TMP/RELEASE_VERSION"
    exit 1
    fi
    echo "Tag '$TAG' doesn't exist yet, safe to proceed"
# end of tag validation

echo "Valid next release version found: $NEW_VERSION"
else
echo "--------------------------------------------------------------------------------"
echo "Error: invalid next release version: $NEW_VERSION"
echo "--------------------------------------------------------------------------------"
exit -1
fi
