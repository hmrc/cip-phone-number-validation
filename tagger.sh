#!/bin/bash -e
RELEASE_VERSION="$(head -n 1 "$TMP/RELEASE_VERSION")"

if [ "$RELEASE_VERSION" == "" ]
then
	echo "No release number found from $TMP/RELEASE_VERSION"
	exit 1
fi

RELEASE="v$RELEASE_VERSION"

if [ "$1" == "" ]
then
	echo "Usage: ./tagger.sh tag - to tag the release from the version number"
	echo "       ./tagger.sh untag - to remove the tag"
	exit 1
fi

if [ "$1" == "tag" ]
then

	echo "Tagging release as $RELEASE"

	DIFF=$(git log $(git describe --tags --abbrev=0)..HEAD --oneline)

	git tag -a $RELEASE -m "Released version $RELEASE"
	git push origin $RELEASE
fi

if [ "$1" == "untag" ]
then
	echo "Untagging release $RELEASE"
	if [ $(git tag -l $RELEASE) ]
	then
		git tag -d $RELEASE
		git push origin :${RELEASE}
	else
		echo "No tag created not removing"
	fi
fi
