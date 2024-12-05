import requests
import json
import logging
from datetime import datetime, timedelta, timezone
import os

# Setup logging
logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

# Constants
GITHUB_REPO = "Sketchware-Pro/Sketchware-Pro"
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
GITHUB_API_BASE = "https://api.github.com"
GITHUB_ABOUT_TEAM_URL = "https://raw.githubusercontent.com/Sketchware-Pro/Sketchware-Pro/refs/heads/host/about_team.json"

HEADERS = {
  "Accept": "application/vnd.github+json",
  "Authorization": f"Bearer {GITHUB_TOKEN}",
  "X-GitHub-Api-Version": "2022-11-28",
}

def get_collaborators():
  logging.info("Fetching collaborators...")
  url = f"{GITHUB_API_BASE}/repos/{GITHUB_REPO}/collaborators"
  response = requests.get(url, headers=HEADERS)
  if response.status_code != 200:
    logging.error(f"Failed to fetch collaborators: {response.status_code} {response.text}")
    return []
  logging.info("Fetched collaborators successfully.")
  return response.json()

def get_contributors():
  logging.info("Fetching contributors...")
  url = f"{GITHUB_API_BASE}/repos/{GITHUB_REPO}/contributors"
  response = requests.get(url, headers=HEADERS)
  if response.status_code != 200:
    logging.error(f"Failed to fetch contributors: {response.status_code} {response.text}")
    return []
  logging.info("Fetched contributors successfully.")
  return response.json()

def has_recent_activity(username):
  logging.info(f"Checking recent activity for user: {username}")
  url = f"{GITHUB_API_BASE}/repos/{GITHUB_REPO}/commits"
  params = {"author": username, "since": (datetime.now(timezone.utc) - timedelta(days=30)).isoformat() + "Z"}
  response = requests.get(url, headers=HEADERS, params=params)
  if response.status_code == 200:
    activity = len(response.json()) > 0
    logging.info(f"User {username} has recent activity: {activity}")
    return activity
  else:
    logging.warning(f"Could not determine activity for user {username}: {response.status_code} {response.text}")
    return False

def update_team_data(collaborators, contributors):
  logging.info("Updating team data...")
  try:
    # Fetch the existing JSON file from the URL
    logging.info("Fetching existing about_team.json file...")
    response = requests.get(GITHUB_ABOUT_TEAM_URL)
    if response.status_code != 200:
      logging.error(f"Failed to fetch about_team.json: {response.status_code} {response.text}")
      return

    data = response.json()
    updated_team = []

    # Process collaborators
    logging.info("Processing collaborators...")
    for user in collaborators:
      logging.info(f"Processing collaborator: {user.get('login')}")
      updated_team.append({
        "user_username": user.get("login"),
        "user_img": user.get("avatar_url"),
        "is_core_team": True,
        "is_active": has_recent_activity(user.get("login")),
      })

    # Process contributors (excluding collaborators)
    logging.info("Processing contributors...")
    collaborator_usernames = {user["user_username"] for user in updated_team}
    for user in contributors:
      if user.get("login") not in collaborator_usernames:
        logging.info(f"Processing contributor: {user.get('login')}")
        updated_team.append({
          "user_username": user.get("login"),
          "user_img": user.get("avatar_url"),
          "is_core_team": False,
          "is_active": has_recent_activity(user.get("login")),
        })

    # Replace the team array while keeping other fields intact
    data["team"] = updated_team

    # Save the updated file locally
    logging.info("Saving updated team data to about_team.json...")
    with open("about_team.json", "w") as file:
      json.dump(data, file, indent=2)
    logging.info("Updated team data successfully.")

  except Exception as e:
    logging.error(f"Failed to update team data: {e}")

def main():
  logging.info("Starting script to update team data...")
  collaborators = get_collaborators()
  contributors = get_contributors()
  update_team_data(collaborators, contributors)
  logging.info("Script completed.")

if __name__ == "__main__":
  main()
