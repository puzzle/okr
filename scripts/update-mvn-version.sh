#!/bin/bash


# Function to retrieve a specific version property
# Usage: get_version_property "propertyName"
# For example: get_version_property "majorVersion"
get_version_property() {
    local property="$1"
    mvn build-helper:parse-version help:evaluate -Dexpression="parsedVersion.${property}" -q -DforceStdout
}

# Usage: ./update_version.sh {major|minor|patch|release}
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 {major|minor|patch|release}"
    exit 1
fi

if [ "$1" == "release" ]; then
    echo "Releasing from commit..."
    exit 0
fi

# Determine new version based on the argument
case "$1" in
    major)
        # Increment major version; reset minor and patch to 0.
        NEXT_MAJOR=$(get_version_property "nextMajorVersion")
        NEW_VERSION="${NEXT_MAJOR}.0.0"
        ;;
    minor)
        # Increment minor version; reset patch to 0.
        MAJOR=$(get_version_property "majorVersion")
        NEXT_MINOR=$(get_version_property "nextMinorVersion")
        NEW_VERSION="${MAJOR}.${NEXT_MINOR}.0"
        ;;
    patch)
        # Increment patch version while keeping major and minor intact.
        MAJOR=$(get_version_property "majorVersion")
        MINOR=$(get_version_property "minorVersion")
        NEXT_PATCH=$(get_version_property "nextIncrementalVersion")
        NEW_VERSION="${MAJOR}.${MINOR}.${NEXT_PATCH}"
        ;;
    *)
        echo "Invalid parameter: $1. Please use major, minor, patch, or release."
        exit 1
        ;;
esac

# Update the Maven version using the common function
echo "Setting new version to: $NEW_VERSION"
mvn versions:set -DnewVersion="${NEW_VERSION}" -DgenerateBackupPoms=false