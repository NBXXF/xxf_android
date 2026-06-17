#!/usr/bin/env bash
#
# XXF Android Skills install / upgrade script
#
# Usage:
#   bash install.sh <agent> [scope]
#
# agent:
#   claude      - Claude Code (~/.claude/skills or .claude/skills)
#   codex       - Codex CLI (injects an AGENTS.md reference block)
#   cursor      - Cursor (.cursor/rules/*.mdc)
#
# scope:
#   user        - global install for claude
#   project     - project-local install for claude/codex/cursor

set -euo pipefail

SKILLS_REPO="${XXF_SKILLS_REPO:-https://github.com/NBXXF/xxf_android.git}"
CACHE_DIR="${XXF_SKILLS_CACHE:-$HOME/.cache/xxf-android-skills}"
AGENT="${1:-}"
SCOPE="${2:-user}"

if [[ "$CACHE_DIR" == "$HOME"/* ]]; then
  CACHE_DIR_DISPLAY='$HOME'"${CACHE_DIR#$HOME}"
else
  CACHE_DIR_DISPLAY="$CACHE_DIR"
fi

err() { echo "error: $*" >&2; exit 1; }
info() { echo "-> $*"; }

[[ -n "$AGENT" ]] || err "missing agent. usage: $0 <claude|codex|cursor> [user|project]"

if [[ -d "$CACHE_DIR/.git" ]]; then
  info "updating cache: $CACHE_DIR"
  git -C "$CACHE_DIR" pull --ff-only --quiet
else
  info "cloning to cache: $CACHE_DIR"
  mkdir -p "$(dirname "$CACHE_DIR")"
  git clone --depth 1 --quiet "$SKILLS_REPO" "$CACHE_DIR"
fi

SKILLS_SRC="$CACHE_DIR/skills"
[[ -d "$SKILLS_SRC" ]] || err "skills/ not found in cache"

install_claude() {
  local target
  case "$SCOPE" in
    user) target="$HOME/.claude/skills" ;;
    project) target="$PWD/.claude/skills" ;;
    *) err "scope must be 'user' or 'project'" ;;
  esac

  mkdir -p "$target"
  local count=0
  for skill_dir in "$SKILLS_SRC"/xxf-*/; do
    [[ -d "$skill_dir" ]] || continue
    local name
    name=$(basename "$skill_dir")
    ln -sfn "$skill_dir" "$target/$name"
    count=$((count + 1))
  done
  info "installed $count skills to $target"
}

install_codex() {
  [[ "$SCOPE" == "project" ]] || err "codex scope must be 'project' (AGENTS.md is per-project)"

  local agents_md="$PWD/AGENTS.md"
  local marker_begin="<!-- BEGIN: xxf-android-skills (managed by install.sh) -->"
  local marker_end="<!-- END: xxf-android-skills -->"

  if [[ -f "$agents_md" ]] && grep -qF "$marker_begin" "$agents_md"; then
    info "refreshing existing xxf-android-skills block in AGENTS.md"
    python3 - "$agents_md" "$marker_begin" "$marker_end" <<'PY'
import pathlib
import re
import sys

path, begin, end = sys.argv[1:]
text = pathlib.Path(path).read_text()
pattern = re.compile(r'\n*' + re.escape(begin) + r'.*?' + re.escape(end) + r'\n?', re.DOTALL)
pathlib.Path(path).write_text(pattern.sub('', text))
PY
  fi

  local skills_list=""
  for skill_dir in "$SKILLS_SRC"/xxf-*/; do
    [[ -d "$skill_dir" ]] || continue
    skills_list+="- $(basename "$skill_dir")"$'\n'
  done

  {
    if [[ -f "$agents_md" ]]; then
      local existing
      existing=$(cat "$agents_md")
      if [[ -n "$existing" ]]; then
        printf '%s\n\n' "$existing"
      fi
    fi
    echo "$marker_begin"
    echo "## XXF Android Skills"
    echo ""
    echo "For normal coding tasks in an XXF Android project, first read:"
    echo ""
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-delivery-loop/SKILL.md"
    echo ""
    echo "Then load any additional relevant skills:"
    echo ""
    echo "- Matching published Android library module skills under:"
    echo "    $CACHE_DIR_DISPLAY/skills/<skill-name>/SKILL.md"
    echo "- Engineering constraints when relevant:"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-coding-style/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-coding-arch/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-class-declaration-guidelines/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-test-strategy/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-unit-test-writer/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-auto-test-orchestrator/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-code-reviewer/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-risk-gate/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-android-performance-gate/SKILL.md"
    echo "    $CACHE_DIR_DISPLAY/skills/xxf-aaa-module-scaffold/SKILL.md"
    echo ""
    echo "Default verification uses the narrowest relevant Gradle task, for example ./gradlew :lib_ktx:compileDebugKotlin or an associated demo assemble listed in the module skill."
    echo ""
    echo "Available skills:"
    echo ""
    echo "$skills_list"
    echo "Update cache: \`git -C $CACHE_DIR_DISPLAY pull\` (or re-run install.sh)."
    echo "$marker_end"
  } > "$agents_md.new"
  mv "$agents_md.new" "$agents_md"

  info "injected managed block into $agents_md"
}

install_cursor() {
  local target
  case "$SCOPE" in
    user) err "Cursor rules are project-scoped; use 'project'" ;;
    project) target="$PWD/.cursor/rules" ;;
    *) err "scope must be 'project' for Cursor" ;;
  esac

  mkdir -p "$target"
  local count=0
  for skill_dir in "$SKILLS_SRC"/xxf-*/; do
    [[ -d "$skill_dir" ]] || continue
    local name
    name=$(basename "$skill_dir")
    cp "$skill_dir/SKILL.md" "$target/$name.mdc"
    count=$((count + 1))
  done
  info "installed $count rules to $target"
}

case "$AGENT" in
  claude) install_claude ;;
  codex) install_codex ;;
  cursor) install_cursor ;;
  *) err "unknown agent '$AGENT'. use: claude | codex | cursor" ;;
esac

info "done."
