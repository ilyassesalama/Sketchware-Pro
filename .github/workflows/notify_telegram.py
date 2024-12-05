import os
import subprocess

# # Switch to the 'host' branch
# subprocess.run(['git', 'checkout', 'host'], check=True)

# List all files in the current directory
for root, dirs, files in os.walk('.'):
  for file in files:
    print(os.path.join(root, file))
