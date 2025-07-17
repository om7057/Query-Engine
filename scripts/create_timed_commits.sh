#!/usr/bin/env bash
set -euo pipefail

# Script to create 10 time-stamped commits evenly spread over ~90 days ending on 2025-10-15
# Usage: run from repository root. It will create a branch 'reconstructed-history'.

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"

BRANCH_NAME="reconstructed-history"

if git rev-parse --verify "$BRANCH_NAME" >/dev/null 2>&1; then
  echo "Branch $BRANCH_NAME already exists. Aborting to avoid overwriting."
  exit 1
fi

git checkout -b "$BRANCH_NAME" || git switch -c "$BRANCH_NAME"

# Target end date (last commit date)
END_DATE="2025-10-15"
# Number of commits to create
N=10
# Total days span (~90 days)
SPAN=90

declare -a DATES
for i in $(seq 0 $((N-1))); do
  # distribute across SPAN days ending at END_DATE
  # position from 0..SPAN, latest for i=N-1 is 0
  pos=$(( (SPAN * (N-1-i)) / (N-1) ))
  # compute date: END_DATE - pos days
  date_str=$(date -I -d "$END_DATE - $pos days")
  # use a fixed time to keep times consistent
  DATES[$i]="$date_str 12:00:00 +0000"
done

echo "Creating $N commits on branch $BRANCH_NAME with dates:" 
for d in "${DATES[@]}"; do echo "  $d"; done

for idx in $(seq 0 $((N-1))); do
  entry_date="${DATES[$idx]}"
  echo "- Reconstructed change #$((idx+1)) on $entry_date" >> HISTORY.md
  git add HISTORY.md
  GIT_AUTHOR_DATE="$entry_date" GIT_COMMITTER_DATE="$entry_date" git commit -m "chore(history): reconstructed commit #$((idx+1)) dated $entry_date"
done

echo "Finished creating commits. Run 'git log --oneline --decorate' to review."
