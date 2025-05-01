#!/bin/bash

# Check if branch is provided
if [ -z "$1" ]; then
    echo "Usage: $0 <branch> [commit_hash]" >&2
    exit 1
fi

branch="$1"
commit_hash="$2"

# If commit hash is not provided, default to the tip of the branch
if [ -z "$commit_hash" ]; then
    commit_hash=$(git rev-parse "$branch")
fi

# Verify that the commit hash is valid
if ! git rev-parse --quiet --verify "$commit_hash" > /dev/null; then
    echo "Commit \"$commit_hash\" is not valid" >&2
    exit 1
fi

# Normalize the commit hash
commit_hash=$(git rev-parse --verify "$commit_hash")

# Check if the commit belongs to the specified branch
#if [ "$(git merge-base $commit_hash $branch)" != "$commit_hash" ]; then
#    echo "Commit is not from branch \"$branch\"" >&2
#    exit 1
#fi

# Output the commit hash so that it can be captured
echo "$commit_hash"
