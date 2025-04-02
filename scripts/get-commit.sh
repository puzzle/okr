#!/bin/bash

branch="$1"
commit_hash="$2"

# If commit hash is not provided, default to the tip of the branch
if [ -z "$commit_hash" ]; then
    echo "Commit hash not provided, defaulting to last commit of branch \"$branch\""
    commit_hash=$(git rev-parse "$branch")
fi

# Check if the commit hash is valid
if git rev-parse --quiet --verify "$commit_hash" > /dev/null; then
    commit_hash=$(git rev-parse --verify "$commit_hash")
    echo "Commit is valid"
else
    echo "Commit \"$commit_hash\" is not valid"
    exit 1
fi

# Check if the commit belongs to the specified branch
if [ "$(git merge-base "$commit_hash" "$branch")" != "$commit_hash" ]; then
    echo "Commit is not from branch \"$branch\""
    exit 1
fi

echo "Commit is set to \"$commit_hash\""