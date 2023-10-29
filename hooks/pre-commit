#!/bin/sh

# cp hooks/pre-commit .git/hooks/
# chmod +x .git/hooks/pre-commit

# Run cljfmt fix
cljfmt fix

# If cljfmt fix fails, exit with a non-zero status
if [ $? -ne 0 ]; then
  echo "cljfmt fix failed. Aborting commit."
  exit 1
fi

