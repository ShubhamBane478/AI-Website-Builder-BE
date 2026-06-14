#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
# docker-publish.sh
#
# Builds the SiteForge backend image and pushes it to Docker Hub.
#
# Usage:
#   ./docker-publish.sh              # tags image as :latest only
#   ./docker-publish.sh 1.2.0        # tags image as :latest AND :1.2.0
#
# Required environment variables (never hardcode these):
#   DOCKER_USERNAME   — your Docker Hub username
#   DOCKER_PASSWORD   — your Docker Hub password or access token
#                       (create a token at hub.docker.com → Account Settings → Security)
#
# Example:
#   export DOCKER_USERNAME=shubhambane478
#   export DOCKER_PASSWORD=<your-access-token>
#   ./docker-publish.sh 1.0.0
# ─────────────────────────────────────────────────────────────────────────────

set -euo pipefail

IMAGE_NAME="shubhambane478/siteforge-backend"
VERSION="${1:-latest}"

# ── 1. Validate credentials ───────────────────────────────────────────────────
if [[ -z "${DOCKER_USERNAME:-}" || -z "${DOCKER_PASSWORD:-}" ]]; then
  echo ""
  echo "❌  Error: DOCKER_USERNAME and DOCKER_PASSWORD must be set as environment variables."
  echo "    Run:"
  echo "      export DOCKER_USERNAME=shubhambane478"
  echo "      export DOCKER_PASSWORD=<your-docker-hub-token>"
  echo ""
  exit 1
fi

# ── 2. Login ──────────────────────────────────────────────────────────────────
echo ""
echo "🔐  Logging in to Docker Hub as ${DOCKER_USERNAME}..."
echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
echo "✅  Login successful."

# ── 3. Build image (tag as :latest and :{version}) ───────────────────────────
echo ""
echo "🔨  Building image..."
echo "    Tags: ${IMAGE_NAME}:latest  ${IMAGE_NAME}:${VERSION}"
docker build \
  -t "${IMAGE_NAME}:latest" \
  -t "${IMAGE_NAME}:${VERSION}" \
  .
echo "✅  Image built successfully."

# ── 4. Push :latest ──────────────────────────────────────────────────────────
echo ""
echo "📤  Pushing ${IMAGE_NAME}:latest..."
docker push "${IMAGE_NAME}:latest"
echo "✅  Pushed: ${IMAGE_NAME}:latest"

# ── 5. Push :{version} (only if different from "latest") ─────────────────────
if [[ "${VERSION}" != "latest" ]]; then
  echo ""
  echo "📤  Pushing ${IMAGE_NAME}:${VERSION}..."
  docker push "${IMAGE_NAME}:${VERSION}"
  echo "✅  Pushed: ${IMAGE_NAME}:${VERSION}"
fi

# ── 6. Logout ─────────────────────────────────────────────────────────────────
echo ""
echo "🔓  Logging out from Docker Hub..."
docker logout
echo ""
echo "🎉  Done! Image is live at:"
echo "    https://hub.docker.com/r/${IMAGE_NAME}"
echo ""
